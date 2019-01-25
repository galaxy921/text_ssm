package ssm.service.Impl;

import com.sun.xml.internal.xsom.SCD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ssm.dao.ShopCategoryDao;
import ssm.model.ShopCategory;
import ssm.service.ShopCategoryService;

import java.util.List;
@Service
public class ShopCategoryServiceImpl implements ShopCategoryService {

    @Autowired
    private ShopCategoryDao shopCategoryDao;
    @Override
    public List<ShopCategory> getShopCategoryList(ShopCategory shopCategoryCondition) {
        return shopCategoryDao.queryShopCategory(shopCategoryCondition);
    }
}
