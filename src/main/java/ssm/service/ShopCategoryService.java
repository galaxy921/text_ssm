package ssm.service;

import org.springframework.stereotype.Component;
import ssm.model.ShopCategory;

import java.util.List;
@Component
public interface ShopCategoryService {
    List<ShopCategory> getShopCategoryList(ShopCategory shopCategoryCondition);

}
