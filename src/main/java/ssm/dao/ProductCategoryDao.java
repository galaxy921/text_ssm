package ssm.dao;

import org.apache.ibatis.annotations.Param;
import ssm.model.ProductCategory;

import java.util.List;

public interface ProductCategoryDao {
    /**
     * 通过shop id查询店铺产品类别
     * @param shopId
     * @return List<ProductCategory>
     */
    List<ProductCategory> queryProductCategoryList(long shopId);

    /**
     *  批量新增
     * @param productCategoryList
     * @return
     */
    int batchInsertProductCategory(List<ProductCategory> productCategoryList);

    /**
     * 删除指定
     * @param productCategoryId
     * @param shopId
     * @return
     */

    int deleteProductCategory(@Param("productCategoryId") long productCategoryId,@Param("shopId") long shopId);

}
