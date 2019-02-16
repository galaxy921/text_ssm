package ssm.util;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class ImageUtil {
    private static String basePath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
    private static final SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    private static final Random r = new Random();

    /**
     * 处理缩略图，并返回新生成图片的相对路径
     * @param thumbnailInputStream
     * @param targetAddr
     * @return
     */

    public static String generateThumbnail(InputStream thumbnailInputStream,String fileName, String targetAddr) {
        String realFileName = getRandomFileName();
        String extension = getFileExtension(fileName);
        makeDirPath(targetAddr);
        String relativeAddr = targetAddr + realFileName + extension;
        File dest = new File(PathUtil.getImgBasePath() + relativeAddr);
        try {
            Thumbnails.of(thumbnailInputStream).size(200, 200)
                    .watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(basePath + "/watermark.jpg")), 0.25f)
                    .outputQuality(0.8f).toFile(dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return relativeAddr;
    }

    /**
     * 创建目标路径所涉及到的目录，即/Users/mac/Desktop/industrious/images/xxx.jpg
     * 那么 Users mac Desktop industrious images 需要自动创建
     * @param targetAddr
     */
    private static void makeDirPath(String targetAddr) {
        String realFileParentPath=PathUtil.getImgBasePath()+targetAddr;
        File dirPath=new File(realFileParentPath);
        if(!dirPath.exists()){
            dirPath.mkdirs();
        }
    }

    /**
     * 获取输入文件留的扩展名
     * @param fileName
     * @return
     */
    private static String getFileExtension(String fileName)
    {
        return fileName.substring(fileName.lastIndexOf("."));
    }

    /**
     * 生成随即文件名，当前年月日小时分钟秒钟+五位随机数
     * @return
     */
    public static String getRandomFileName() {
        //获取随机五位数
        int rannum=r.nextInt(89999)+10000;
        String nowTimeStr=sDateFormat.format(new Date());
        return nowTimeStr+rannum;
    }

    public static void main(String[] args) throws IOException {
        String basePath =Thread.currentThread().getContextClassLoader().getResource("").getPath();
        System.out.println(basePath);
        Thumbnails.of(new File("/Users/mac/Desktop/industrious/test/4qr.jpg"))
                .size(200,200).watermark(Positions.BOTTOM_RIGHT,
                ImageIO.read(new File(basePath+"/watermark.jpg")),0.25f).outputQuality(0.8f)
        .toFile("/Users/mac/Desktop/industrious/test/5qr.jpg");
    }

    /**
     * storePath是文件路径还是目录的路径，
     * 如果storePath是文件路径则删除该文件，
     * 如果storePath是目录路径则删除该目录下的所有文件
     * @param storePath
     */
    public static void deleteFileOrPath(String storePath){
        File fileOrPath=new File(PathUtil.getImgBasePath()+storePath);
        if(fileOrPath.exists()){
            if(fileOrPath.isDirectory()){
                File files[]=fileOrPath.listFiles();
                for(int i=0;i<files.length;i++){
                    files[i].delete();
                }
            }
            fileOrPath.delete();
        }

    }
}

