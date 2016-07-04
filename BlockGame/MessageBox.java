
import greenfoot.Greenfoot;
import greenfoot.GreenfootImage;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MessageBox extends BaseActor {

    String msg;
    GreenfootImage msgboxImg;

    public MessageBox(String msgConfigName) {
        String fileName = String.format("messages/%s.txt", msgConfigName);
        try {
            msg = new String(
                    Files.readAllBytes(Paths.get(fileName)),
                    StandardCharsets.UTF_8);
        } catch (Exception e) {
            System.out.println(String.format("cannot access \"%s\".", fileName));
            Greenfoot.stop();
        }

    }

    @Override
    public void tick() {
        if (msgboxImg == null) {
            msgboxImg = new GreenfootImage(getWorld().getWidth(), getWorld().getHeight());
            msgboxImg = new GreenfootImage(100, 100);
            msgboxImg.setColor(Color.BLACK);
            msgboxImg.fill();
            msgboxImg.setColor(Color.WHITE);
            drawStringInRect(msg, msgboxImg.getAwtImage(), 0, 0,
                    msgboxImg.getWidth(), msgboxImg.getHeight());
        }
        setImage(msgboxImg);
    }

    /**
     * 画像の指定した領域に文字列を描画する。
     *
     * @param str
     * @param img
     * @param x
     * @param y
     * @param width
     * @param height
     */
    public void drawStringInRect(String str, BufferedImage img,
            int x, int y, int width, int height) {
        String targetStr = str;
        Graphics graphics = img.getGraphics();
        FontMetrics fontmetrics = img.getGraphics().getFontMetrics();

        while (true) {
            // 横幅に収まるような最大文字列長を探る
            int i = 1;
            double strWidth = 0;
            double strHeight = 0;
            while (i < targetStr.length()) {
                strWidth = fontmetrics.stringWidth(targetStr.substring(0, i));
                strHeight = fontmetrics.getLineMetrics(targetStr.substring(0, i), graphics).getHeight();

                if (strWidth < width && strHeight < height) {
                    i++;
                    continue;
                }
                break;
            }
            i--;

            // これ以上文字列を描画できなくなったら終了
            if (i == 0) {
                return;
            }

            // 画像に描画して、次の行の描画領域を設定
            System.out.println("draw: "+targetStr.substring(0, i));
            img.getGraphics().drawString(targetStr.substring(0, i), x, y+(int)strHeight);
            y += strHeight;
            height -= strHeight;
            targetStr = targetStr.substring(i);
        }
    }
}
