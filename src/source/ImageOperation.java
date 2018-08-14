package source;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import sun.misc.BASE64Encoder;

/**
 * 图片的相关操作
 * @author 张爽
 * @date 2017-8-2
 */
public class ImageOperation {
    public static void main(String[] args) throws IOException {
        CreateImage.createImage();
    }

}
/**
 * 生成图片
 * 1.将图片写到磁盘里
 * 2.将图片写到流里
 * 3.拿到图片的base64
 * 参考博客：
 * 	1.利用Data URL(data:image/jpg;base64,)将小图片生成数据流形式
 * 		https://www.lvtao.net/dev/php-image-data-url.html
 *	2.Java生成图片
 *		http://blog.csdn.net/liuxin191863128/article/details/38081465
 * @author 张爽
 * @date 2017-8-2
 */
class CreateImage {
    /**
     * 创建一张图片
     * 图片存放路径：E:/学习笔记/createImage.jpg
     * 1.将图片写到磁盘里
     * 2.将图片写到流里
     * 3.拿到图片的base64
     * @author 张爽
     * @throws IOException
     * @date 2017-8-2
     */
    public static void createImage() throws IOException {
        int width = 1920;
        int height = 1080;
        String content = "你好";
        String[] contents={""};
        File file = new File("F:\\td\\a.jpg");
        Font font = new Font("Serif", Font.BOLD, 50);

        BufferedImage bufferedImage = new BufferedImage(
                width,
                height,
                BufferedImage.TYPE_INT_RGB
        );

        Graphics2D graphics2D = (Graphics2D) bufferedImage.getGraphics();
        graphics2D.setBackground(Color.WHITE);
        graphics2D.clearRect(0, 0, width, height);
        graphics2D.setPaint(Color.RED);
        FontRenderContext fontRenderContext = graphics2D.getFontRenderContext();
        Rectangle2D stringBounds = font.getStringBounds(content, fontRenderContext);
        for (int i=0;i<contents.length;i++){
            double x = (width - stringBounds.getWidth()) / 2;
            double y = (height - stringBounds.getHeight()) / 2;
            double ascent = -stringBounds.getY();
            double baseY = y + ascent;
            graphics2D.drawString(contents[i], (int)x, (int)baseY);

        }


        // 1.将图片写到实体图片里
        ImageIO.write(bufferedImage, "jpg", file);
        // 2.将图片写到流里
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "jpg", byteArrayOutputStream);
        // 3.将图片以base64的形式展示
        BASE64Encoder base64Encoder = new BASE64Encoder();
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        System.out.println("data:image/png;base64," +
                base64Encoder.encodeBuffer(byteArray).trim());;
    }


}