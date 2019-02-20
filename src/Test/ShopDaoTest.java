import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ssm.dao.ShopDao;
import ssm.dto.ShopExecution;
import ssm.model.Area;
import ssm.model.PersonInfo;
import ssm.model.Shop;
import ssm.model.ShopCategory;
import ssm.service.ShopService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ShopDaoTest extends BaseTest{
    @Autowired
    private ShopDao shopDao;
    private ShopService shopService;
    @Test
    @Ignore
    public void testInsertShop() throws FileNotFoundException {
        Shop shop=new Shop();
        PersonInfo owner=new PersonInfo();
        Area area=new Area();
        ShopCategory shopCategory=new ShopCategory();
        owner.setUserId(1L);
        area.setAreaId(2);
        shopCategory.setShopCategoryId(1L);
        shop.setOwner(owner);
        shop.setArea(area);
        shop.setShopCategory(shopCategory);
        shop.setShopName("测试店铺3");
        shop.setShopDesc("test3");
        shop.setShopAddr("test3");
        shop.setPhone("test3");
        shop.setShopImg("test3");
        shop.setCreateTime(new Date());
        shop.setEnableStatus(1);
        shop.setAdvice("审核中");
        File shopImg=new File("/Users/mac/Desktop/industrious/test/4qr.jpg");
        InputStream is=new FileInputStream(shopImg);
        ShopExecution se=shopService.addShop(shop,is,shopImg.getName());
//        int effectedNum=shopDao.insertShop(shop);
//        assertEquals(1,effectedNum);
    }
    @Test
    public void testQueryShopList(){
        Shop shopCondition =new Shop();
        PersonInfo owner =new PersonInfo();
        owner.setUserId(1L);
        shopCondition.setOwner(owner);
        List<Shop> shopList=shopDao.queryShopList(shopCondition,0,5);
        int count=shopDao.queryShopCount(shopCondition);
        System.out.println("店铺列表大小："+shopList.size());
        System.out.println("店铺总数："+count);
        ShopCategory sc=new ShopCategory();
        sc.setShopCategoryId(1L);
        shopCondition.setShopCategory(sc);
        shopList=shopDao.queryShopList(shopCondition,0,2);
        count=shopDao.queryShopCount(shopCondition);
        System.out.println("xin店铺列表大小："+shopList.size());
        System.out.println("xin店铺总数："+count);
    }
    @Test
    @Ignore
    public void testQueryShopId(){
        long shopId=1;
        Shop shop=shopDao.queryByShopId(shopId);
        System.out.println("areaId:"+shop.getArea().getAreaId());
        System.out.println("areaName:"+shop.getArea().getAreaName());

    }
    @Test
    @Ignore
    public void testUpdateShop(){
        Shop shop=new Shop();
        shop.setShopId(32L);
        shop.setShopDesc("测试描述");
        shop.setShopAddr("测试地址");
        shop.setLastEditTime(new Date());
        int effectedNum=shopDao.updateShop(shop);
        assertEquals(1,effectedNum);

    }
}
