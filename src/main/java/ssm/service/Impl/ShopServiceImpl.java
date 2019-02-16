package ssm.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import ssm.dao.ShopDao;
import ssm.dto.ShopExecution;
import ssm.enums.ShopStateEnum;
import ssm.exceptions.ShopOperationException;
import ssm.model.Shop;
import ssm.service.ShopService;
import ssm.util.ImageUtil;
import ssm.util.PathUtil;

import java.io.InputStream;
import java.util.Date;

@Service("shopService")
public class ShopServiceImpl implements ShopService {

    @Autowired
    private ShopDao shopDao;
    @Override
    @Transactional
    public ShopExecution addShop(Shop shop, InputStream shopImgInputStream,String fileName) {
        //空值判断
        if(shop==null){
            return new ShopExecution(ShopStateEnum.NULL_SHOP);
        }
        try{
            //给店铺信息赋初始值
            shop.setEnableStatus(0);
            shop.setCreateTime(new Date());
            shop.setLastEditTime(new Date());
            //添加店铺信息
            int effectedNum=shopDao.insertShop(shop);
            if(effectedNum<=0){
                throw new RuntimeException("店铺创建失败");
            }else{
                if(shopImgInputStream!=null){
                    //存储图片
                    try {
                        addShopImg(shop, shopImgInputStream,fileName);
                    }catch (Exception e){
                        throw new RuntimeException("addShopImg error"+e.getMessage());
                    }
                    //更新店铺图片地址
                    effectedNum=shopDao.updateShop(shop);
                    if(effectedNum<=0){
                        throw new RuntimeException("更新图片地址失败");
                    }
                }
            }
        }catch (Exception e){
            throw new RuntimeException("assShop error"+e.getMessage());}
        return new ShopExecution(ShopStateEnum.CHECK,shop);
    }

    @Override
    public Shop getByShopId(long shopId) {
        return shopDao.queryByShopId(shopId);
    }

    @Override
    public ShopExecution modifyShop(Shop shop, InputStream shopImgInputStream, String fileName) throws ShopOperationException {
        if(shop==null||shop.getShopId()==null){
            return new ShopExecution(ShopStateEnum.NULL_SHOP);
        }else {
            //1.判断是否要处理图片
            try{
            if(shopImgInputStream!=null&&fileName!=null&&!"".equals(fileName)){
                Shop tempShop=shopDao.queryByShopId(shop.getShopId());
                if(tempShop.getShopImg()!=null){
                    ImageUtil.deleteFileOrPath(tempShop.getShopImg());
                }
                addShopImg(shop,shopImgInputStream,fileName);
            }
        //2.更新店铺信息
        shop.setLastEditTime(new Date());
        int effectedNum=shopDao.updateShop(shop);
                System.out.println("asfjahlfal"+effectedNum);
        if(effectedNum<=0){
            return new ShopExecution(ShopStateEnum.INNER_EORROR);
        }else{
            shop=shopDao.queryByShopId(shop.getShopId());
            return new ShopExecution(ShopStateEnum.SUCCESS,shop);
        }}catch(Exception e){
                throw new ShopOperationException("modifyShop error:"+e.getMessage());
            }
        }
    }

    private void addShopImg(Shop shop, InputStream shopImgInputStream,String fileName){
        //获取shop图片目录的相对值路径
        String dest= PathUtil.getShopImagePath(shop.getShopId());
        String shpoImgAddr= ImageUtil.generateThumbnail(shopImgInputStream,fileName,dest);
        shop.setShopImg(shpoImgAddr);
    }
}






















