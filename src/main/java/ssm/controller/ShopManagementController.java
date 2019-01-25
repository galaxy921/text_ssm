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


    @RequestMapping(value ="/registershop",method= RequestMethod.POST)
    @ResponseBody
    private Map<String,Object> registerShop(HttpServletRequest request) {
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
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "上传图片不能为空");
            return modelMap;
        }
        //2.注册店铺
        if (shop != null && shopImg != null) {
            PersonInfo owner = new PersonInfo();
            owner.setUserId(1L);
            shop.setOwner(owner);
            ShopExecution se ;
            try{
                se = shopService.addShop(shop, shopImg.getInputStream(),shopImg.getOriginalFilename());
                System.out.println(se.toString());
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
