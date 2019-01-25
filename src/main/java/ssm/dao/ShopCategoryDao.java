package ssm.dao;

import org.apache.ibatis.annotations.Param;
import ssm.model.ShopCategory;

import java.util.List;

public interface ShopCategoryDao  {
    List<ShopCategory> queryShopCategory(@Param("shopCategoryCondition")
                                                 ShopCategory shopCategoryCondition);
}
