import com.sun.org.apache.bcel.internal.generic.NEW;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;


public class ClassMyTest {

    /**
     * 图片添加水印
     *
     * @paramsrcImgPath需要添加水印的图片的路径
     * @paramoutImgPath添加水印后图片输出路径
     * @parammarkContentColor水印文字的颜色
     * @paramfontSize文字大小
     * @paramwaterMarkContent水印的文字
     */
    public void waterPress(String srcImgPath, String outImgPath, Color markContentColor, int fontSize, List<String> jokeContent) {
        try {
            String currentJoke = "";//当前笑话
            System.out.println("filesrc:"+srcImgPath);
// 读取原图片信息
            File srcImgFile = new File(srcImgPath);
            BufferedImage bufferedImage = new BufferedImage(
                    1920,
                    1080,
                    BufferedImage.TYPE_INT_RGB
            );
            ImageIO.write(bufferedImage, "jpg", srcImgFile);
            Image srcImg = ImageIO.read(srcImgFile);
            int srcImgWidth = srcImg.getWidth(null);
            int srcImgHeight = srcImg.getHeight(null);
// 加水印
            BufferedImage bufImg = new BufferedImage(srcImgWidth, srcImgHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = bufImg.createGraphics();
            g.drawImage(srcImg, 0, 0, srcImgWidth, srcImgHeight, null);
            g.setBackground(Color.WHITE);
            g.clearRect(0,0,srcImgWidth,srcImgHeight);
//Font font = new Font("Courier New", Font.PLAIN, 12);
            Font font = new Font("黑体", Font.PLAIN, fontSize);
            g.setColor(markContentColor);//根据图片的背景设置水印颜色

            g.setFont(font);
            int fontlen = getWatermarkLength(currentJoke, g);

            int line = fontlen / srcImgWidth;//文字长度相对于图片宽度应该有多少行

            int y = fontSize;
            System.out.println("水印文字总长度:" + fontlen + ",图片宽度:" + srcImgWidth + ",字符个数:" + currentJoke.length());
            double tempXD=0.25*srcImgWidth;
            int tempX = (int)tempXD;
            System.out.println("x:"+tempX);
            int tempY = (int)(y+(int)srcImgHeight*0.2);
            //记录当前行最大Y
            int currentMaxY=y;
            for (String jc : jokeContent) {

                //currentY
                int currentY=tempY;
                //currentX
                int currentX=tempX;
//文字叠加,自动换行叠加
                int tempCharLen = 0;//单字符长度
                int tempLineLen = 0;//单行字符总长度临时计算
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < jc.length(); i++) {
                    char tempChar = jc.charAt(i);
                    tempCharLen = getCharLen(tempChar, g);
                    double charCount=0d;
                    if(tempCharLen!=0) charCount=500/tempCharLen;//一行能放多少个字符
                   double countHang= jc.length()/charCount;//行数
                    double currentHeight=countHang*fontSize;//这段笑话的高度
                    if (currentHeight+currentY>srcImgHeight)break;
                    tempLineLen += tempCharLen;
                    if (tempLineLen >= 500 || (tempX+tempLineLen)>=srcImgWidth-srcImgWidth*0.25) {
//长度已经满一行,进行文字叠加
                        g.drawString(sb.toString(), tempX, tempY);

                        sb.delete(0, sb.length());//清空内容,重新追加

                        tempY += fontSize;

                        tempLineLen = 0;

                    }
                    sb.append(tempChar);//追加字符
                }
                g.drawString(sb.toString(), tempX, tempY);//最后叠加余下的文字
                currentMaxY=tempY>currentMaxY?tempY:currentMaxY;
                tempY=currentY;
                tempX+=520;
                if ((tempX+tempLineLen)>=srcImgWidth-srcImgWidth*0.25){tempY=currentMaxY+40;tempX=(int)tempXD;}
            }
            g.dispose();

// 输出图片
            FileOutputStream outImgStream = new FileOutputStream(outImgPath);
            ImageIO.write(bufImg, "jpg", outImgStream);
            outImgStream.flush();
            outImgStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getCharLen(char c, Graphics2D g) {
        return g.getFontMetrics(g.getFont()).charWidth(c);
    }

    /**
     * 获取水印文字总长度
     *
     * @paramwaterMarkContent水印的文字
     * @paramg
     * @return水印文字总长度
     */
    public int getWatermarkLength(String waterMarkContent, Graphics2D g) {
        return g.getFontMetrics(g.getFont()).charsWidth(waterMarkContent.toCharArray(), 0, waterMarkContent.length());
    }

    public static void main(String[] args) {
// 原图位置, 输出图片位置, 水印文字颜色, 水印文字
//        String content ="qqqqqqssss2222..... ooooddd水印效果测水印效果整水印效果测水印效果整水印效果测水印效果整水印效果测水印效果整水印效果测水印效果整水印效果测水印效果整印效果测水印效果整水印效果测水印效果";
//        new ClassMyTest().waterPress("F:\\td\\a.jpg","F:\\td\\a1.jpg", Color.black,20, content);
        createImage();
    }


    public static void createImage() {
        String jsonStr = WebUtils.webGetString("https://www.apiopen.top/satinGodApi?type=2&page=1");
        java.util.List<String> jokeList = new ArrayList<String>();
        ClassMyTest CM = new ClassMyTest();
        try {
            JSONObject JO = new JSONObject(jsonStr);
            JSONArray jokes = new JSONArray(JO.getString("data"));
            for (int i = 0; i < jokes.length(); i++) {
                JSONObject currentJson = (JSONObject) jokes.get(i);
                jokeList.add(currentJson.getString("text"));
            }
            CM.waterPress(new File(".").getCanonicalPath()+ "\\"  +"wallpaper.jpg", new File(".").getCanonicalPath()+ "\\"  +"wallpaper.jpg", Color.black, 20, jokeList);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}