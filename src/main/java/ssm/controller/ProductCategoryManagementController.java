package ssm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ssm.dto.ProductCategoryExecution;
import ssm.dto.Result;
import ssm.enums.ProductCategoryStateEnum;
import ssm.model.ProductCategory;
import ssm.model.Shop;
import ssm.service.ProductCategoryService;
import ssm.service.ShopService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/shopadmin")
public class ProductCategoryManagementController {
    @Autowired
    private ProductCategoryService productCategoryService;
    @Autowired
    private ShopService shopService;

    @RequestMapping(value="/getproductcategorylist",method = RequestMethod.GET)
    @ResponseBody
    private Result<List<ProductCategory>> getProdudctCategoryList(HttpServletRequest request){
//        Shop shop=new Shop();
//        shop.setShopId( 20L);
//        request.getSession().setAttribute("currentShop",shop);

        Shop currentShop =(Shop) request.getSession().getAttribute("currentShop");
        System.out.println("888888"+currentShop.getShopName());
        List<ProductCategory> list=null;
        if(currentShop!=null&&currentShop.getShopId()>0){
        list=productCategoryService.getProductCategoryList(currentShop.getShopId());
            System.out.println("11111"+list.toString());
        return new Result<List<ProductCategory>>(true,list);
        }else {
            ProductCategoryStateEnum ps=ProductCategoryStateEnum.INNER_ERROR;
            return new Result<List<ProductCategory>>(false,ps.getStateInfo(),ps.getState());
        }
    }
    @RequestMapping(value="/addproductcategorys",method = RequestMethod.POST)
    @ResponseBody
    private Map<String,Object> addProductCategorys(@RequestBody List<ProductCategory> productCategoryList,
                                                    HttpServletRequest request){

    Map<String,Object> modelMap=new HashMap<String, Object>();
    Shop currentShop=(Shop)request.getSession().getAttribute("currentShop");
        System.out.println("999999addProductCategorys"+currentShop.toString());
    for(ProductCategory pc:productCategoryList){
        pc.setShopId(currentShop.getShopId());
    }
        if (productCategoryList != null && productCategoryList.size()> 0) {
            try {
                ProductCategoryExecution pe = productCategoryService.batchAddProductCategory(productCategoryList);
        if (pe.getState() == ProductCategoryStateEnum.SUCCESS.getState()) {
            modelMap.put("success", true);
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", pe.getStateInfo());
        }
    } catch (RuntimeException e) {
        modelMap.put("success", false);
        modelMap.put("errMsg", e.toString());
        return modelMap;
    }

} else {
        modelMap.put("success", false);
        modelMap.put("errMsg", "请至少选择一个商品类别");
        }
        return modelMap;
        }
    @RequestMapping(value="/removeproductcategory",method = RequestMethod.POST)
    @ResponseBody
    private Map<String,Object> removeProductCategory(Long productCategoryId,HttpServletRequest request){
        Map<String,Object> modelMap=new HashMap<String, Object>();
        if (productCategoryId != null && productCategoryId> 0) {
            try {

                Shop currentShop=(Shop) request.getSession().getAttribute("currentShop");
                ProductCategoryExecution pe = productCategoryService.deleteProductCategory(productCategoryId,currentShop.getShopId());
                if (pe.getState() == ProductCategoryStateEnum.SUCCESS.getState()) {
                    modelMap.put("success", true);
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", pe.getStateInfo());
                }
            } catch (RuntimeException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.toString());
                return modelMap;
            }

        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请至少选择一个商品类别");
        }
        return modelMap;
    }

}

