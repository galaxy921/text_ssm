package ssm.service;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import ssm.dto.ShopExecution;
import ssm.exceptions.ShopOperationException;
import ssm.model.Shop;

import java.io.InputStream;

@Component
public interface ShopService {
    ShopExecution addShop(Shop shop, InputStream shopImgInputStream,String targetAddr) throws ShopOperationException;
}