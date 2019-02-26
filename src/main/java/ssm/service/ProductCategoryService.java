package ssm.service;

import org.springframework.stereotype.Component;
import ssm.dto.ProductCategoryExecution;
import ssm.exceptions.ProductCategoryOperationException;
import ssm.model.ProductCategory;

import java.util.List;
@Component
public interface ProductCategoryService {
    /**
     * 查询知道某个店铺下的所有商品类别信息
     * @param shopId
     * @return list
     */
    List<ProductCategory> getProductCategoryList(long shopId);

    /**
     * 查询指定某个店铺下的所有商品类别信息
     *
     * @param  shopId
     * @return List<ProductCategory>
     */
//    List<ProductCategory> getByShopId(long shopId);

    /**
     *
     * @param productCategoryList
     * @return
     * @throws RuntimeException
     */
    ProductCategoryExecution batchAddProductCategory(List<ProductCategory> productCategoryList) throws ProductCategoryOperationException;

    /**
     * 将此类别下的商品Id置为空，在删除掉该商品类别
     * @param productCategoryId
     * @param shopId
     * @return
     * @throws ProductCategoryOperationException
     */

    ProductCategoryExecution deleteProductCategory(long productCategoryId,long shopId) throws ProductCategoryOperationException;
}
