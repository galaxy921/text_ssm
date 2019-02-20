package ssm.service;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import ssm.dto.ShopExecution;
import ssm.exceptions.ShopOperationException;
import ssm.model.Shop;

import java.io.InputStream;
import java.util.concurrent.locks.Condition;

@Component
public interface ShopService {
    /**
     * 根据shopCondition分页返回相应店铺列表
     * @param shopCondition
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public ShopExecution getShopList(Shop shopCondition,int pageIndex,int pageSize);

    /**
     *
     * @param shop
     * @param shopImgInputStream
     * @param targetAddr
     * @return
     * @throws ShopOperationException
     */
    ShopExecution addShop(Shop shop, InputStream shopImgInputStream,String targetAddr) throws ShopOperationException;

    /**
     * 通过店铺Id获取店铺信息
     * @param shopId
     * @return
     */
    Shop getByShopId(long shopId);

    /**
     * 更新店铺信息，包括对图片的处理
     * @param shop
     * @param shopImgInputStream
     * @param fileName
     * @return
     * @throws ShopOperationException
     */
    ShopExecution  modifyShop(Shop shop,InputStream shopImgInputStream,String fileName) throws ShopOperationException;
}