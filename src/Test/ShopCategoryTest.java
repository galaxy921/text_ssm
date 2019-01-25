import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ssm.dao.ShopCategoryDao;
import ssm.model.ShopCategory;

import java.util.List;

import static net.sf.ezmorph.test.ArrayAssertions.assertEquals;

public class ShopCategoryTest extends BaseTest {
    @Autowired
    private ShopCategoryDao shopCategoryDao;
    @Test
    public void testQueryShopCategory(){
        List<ShopCategory> shopCategoryList=shopCategoryDao.queryShopCategory(new ShopCategory());
        System.out.println(shopCategoryList.size());
        ShopCategory testCategory=new ShopCategory();
        ShopCategory parentCategory=new ShopCategory();
        parentCategory.setShopCategoryId(1L);
        testCategory.setParent(parentCategory);
        shopCategoryList=shopCategoryDao.queryShopCategory(testCategory);
        assertEquals(1,shopCategoryList.size());
        System.out.println(shopCategoryList.get(0).getShopCategoryName());


    }
}
