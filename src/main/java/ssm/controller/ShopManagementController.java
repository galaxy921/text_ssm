package ssm.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import ssm.dto.ShopExecution;
import ssm.enums.ShopStateEnum;
import ssm.exceptions.ShopOperationException;
import ssm.model.Area;
import ssm.model.PersonInfo;
import ssm.model.Shop;
import ssm.model.ShopCategory;
import ssm.service.AreaService;
import ssm.service.ShopCategoryService;
import ssm.service.ShopService;
import ssm.util.HttpServeltRequestUtil;
import ssm.util.ImageUtil;
import ssm.util.PathUtil;
import sun.jvm.hotspot.runtime.ObjectMonitor;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

@Controller
@RequestMapping("/shopadmin")
public class ShopManagementController {
    @Autowired
    private ShopService  shopService;
    @Autowired
    private ShopCategoryService shopCategoryService;
    @Autowired
    private AreaService areaService;
    @RequestMapping(value = "/getshopmanagementinfo",method = RequestMethod.GET)
    @ResponseBody
    private Map<String,Object> getShopManagementInfo(HttpServletRequest request){
        Map<String,Object> modelMap=new HashMap<String, Object>();
        long shopId=HttpServeltRequestUtil.getLong(request,"shopId");
        if(shopId<=0){
            Object currentShopObj=request.getSession().getAttribute("currentShop");
            if(currentShopObj==null){
                modelMap.put("redirect",true);
                modelMap.put("url","/shopadmin/shoplist");
            }else {
                Shop currentShop=(Shop) currentShopObj;
                modelMap.put("redirect",false);
                modelMap.put("shopId",currentShop.getShopId());
            }
        }else {
            Shop currentShop=new Shop();
            currentShop.setShopId(shopId);
            request.getSession().setAttribute("currentShop",currentShop);
            modelMap.put("redirect",false);
        }
        return modelMap;
    }

    @RequestMapping(value = "/getshoplist",method = RequestMethod.GET)
    @ResponseBody
    private Map<String,Object> getShopList(HttpServletRequest request){
        Map<String,Object> modelMap=new HashMap<String,Object>();
        PersonInfo user=new PersonInfo();
        user.setUserId(1L);
        user.setName("test");
        request.getSession().setAttribute("user",user);
        user=(PersonInfo) request.getSession().getAttribute("user");
        try{
            Shop shopCondition=new Shop();
            shopCondition.setOwner(user);
            ShopExecution se=shopService.getShopList(shopCondition,0,100);
            modelMap.put("shopList",se.getShopList());
            modelMap.put("user",user);
            modelMap.put("success",true);
        }catch (Exception e){
            modelMap.put("success",false);
            modelMap.put("errMsg",e.getMessage());
        }
        return modelMap;
    }
    @RequestMapping(value = "/getshopbyid",method = RequestMethod.GET)
    @ResponseBody
    private Map<String,Object> getShopById(HttpServletRequest request){
        Map<String,Object> modelMap=new HashMap<String, Object>();
        Long shopId =HttpServeltRequestUtil.getLong(request,"shopId");
        if(shopId>-1){
            try {
                Shop shop=shopService.getByShopId(shopId);
                List<Area> areaList=areaService.getAreaList();
                modelMap.put("shop",shop);
                modelMap.put("shopList",areaList);
                modelMap.put("success",true);
            }catch (Exception e){
                modelMap.put("success",false);
                modelMap.put("errMsg",e.toString());
            }
        }else {
            modelMap.put("success",false);
            modelMap.put("errMsg","empty shopId");
        }
        return modelMap;
    }


    @RequestMapping(value = "/getshopinitinfo",method = RequestMethod.GET)
    @ResponseBody
    private Map<String,Object> getShopInitInfo(){
        Map<String,Object> modelMap = new HashMap<String, Object>();
        List<ShopCategory> shopCategoryList = new ArrayList<ShopCategory>();
        List<Area> areaList = new ArrayList<Area>();
        try {
            shopCategoryList = shopCategoryService.getShopCategoryList(new ShopCategory());
            areaList = areaService.getAreaList();
            modelMap.put("areaList",areaList);
            modelMap.put("shopCategoryList",shopCategoryList);
            modelMap.put("success",true);
        }catch (Exception e){
            modelMap.put("success",false);
            modelMap.put("errMsg",e.getMessage());
        }
        return modelMap;
    }

    @RequestMapping(value ="/registershop",method=RequestMethod.POST)
    @ResponseBody
    private Map<String,Object> registerShop(HttpServletRequest request) {
        System.out.println("123123123123");
        System.out.println(request.getParameter("verifyCodeActual"));
        System.out.println(HttpServeltRequestUtil.getString(request, "verifyCodeActual"));
        Map<String, Object> modelMap = new HashMap<String, Object>();
        //1.接收并转化相应的参数，包括店铺信息以及图片信息
        String shopStr = HttpServeltRequestUtil.getString(request, "shopStr");
        ObjectMapper mapper = new ObjectMapper();
        Shop shop = null;
        try {
            shop = mapper.readValue(shopStr, Shop.class);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
            return modelMap;
        }
        CommonsMultipartFile shopImg = null;
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        if (commonsMultipartResolver.isMultipart(request)) {
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
            shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
        }
        //2.注册店铺
        if (shop != null && shopImg != null) {
            PersonInfo owner = (PersonInfo) request.getSession().getAttribute("user");
            shop.setOwner(owner);
            ShopExecution se;
            try {
                se = shopService.addShop(shop, shopImg.getInputStream(), shopImg.getOriginalFilename());
                if(se.getState()==ShopStateEnum.CHECK.getState()){
                    modelMap.put("success",true);
                    @SuppressWarnings("unchecked")
                    List<Shop> shopList=(List<Shop>)request.getSession().getAttribute("shopList");
                    if(shopList==null||shopList.size()==0){
                        shopList=new ArrayList<Shop>();
                    }
                        shopList.add(se.getShop());
                        request.getSession().setAttribute("shopList",shopList);
                }else {
                    modelMap.put("success",false);
                    modelMap.put("errMsg",se.getStateInfo());
                }
                System.out.println(se.toString());
            } catch (ShopOperationException e) {
                modelMap.put("success", false);
                modelMap.put("success", true);
                modelMap.put("errMsg", e.getStackTrace());
            } catch (IOException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
            }
            return modelMap;
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请输入店铺信息");
            return modelMap;
        }
    }
        @RequestMapping(value ="/modifyshop",method= RequestMethod.POST)
        @ResponseBody
        private Map<String,Object> modifyShop(HttpServletRequest request) {
            System.out.println("123123123123");
            System.out.println(request.getParameter("verifyCodeActual"));
            System.out.println(HttpServeltRequestUtil.getString(request,"verifyCodeActual"));
            Map<String, Object> modelMap = new HashMap<String, Object>();
            //1.接收并转化相应的参数，包括店铺信息以及图片信息
            String shopStr = HttpServeltRequestUtil.getString(request, "shopStr");
            ObjectMapper mapper = new ObjectMapper();
            Shop shop = null;
            try {
                shop = mapper.readValue(shopStr, Shop.class);
            } catch (Exception e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
                return modelMap;
            }
            CommonsMultipartFile shopImg = null;
            CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
            if (commonsMultipartResolver.isMultipart(request)) {
                MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
                shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
            }
            //2.修改店铺信息
        if (shop != null && shop.getShopId() != null) {
            ShopExecution se ;
            try{
                if(shopImg==null){
                    se=shopService.modifyShop(shop,null,null);
                }else {
                    se = shopService.addShop(shop, shopImg.getInputStream(), shopImg.getOriginalFilename());
                }
                System.out.println(se.toString());
                if(se.getState()==ShopStateEnum.CHECK.getState()){
                    modelMap.put("success",true);
                }else {
                    modelMap.put("success",false);
                    modelMap.put("errMsg",se.getStateInfo());
                }
            }catch (ShopOperationException e){
                modelMap.put("success",false);
                modelMap.put("success",true);
                modelMap.put("errMsg",e.getStackTrace());
            } catch (IOException e) {
                modelMap.put("success",false);
                modelMap.put("errMsg",e.getMessage());
            }
            return modelMap;
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请输入店铺信息");
            return modelMap;
        }
        //3.返回结果
    }
}
