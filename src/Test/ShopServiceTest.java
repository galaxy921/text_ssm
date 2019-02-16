import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ssm.dto.ShopExecution;
import ssm.exceptions.ShopOperationException;
import ssm.model.Shop;
import ssm.service.ShopService;

import java.beans.IndexedPropertyChangeEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class ShopServiceTest extends BaseTest{
    @Autowired
    private ShopService shopService;
    @Test
    public void testModifyShop() throws ShopOperationException, FileNotFoundException{
        Shop shop=new Shop();
        shop.setShopId(15L);
        shop.setShopName("修改后的店铺名称");
        File shopImg=new File("/Users/mac/Downloads/1.jpg");
        InputStream is=new FileInputStream(shopImg);
        ShopExecution shopExecution=shopService.modifyShop(shop,is,"1.jpg");
        System.out.println("新的图片地址为"+shopExecution.getShop().getShopImg());

    }
    @Test
    public void testAddShop() throws ShopOperationException,FileNotFoundException{

    }
}
