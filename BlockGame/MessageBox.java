
import greenfoot.Greenfoot;
import greenfoot.GreenfootImage;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MessageBox extends BaseActor {

    final static int DEFAULT_FONT_SIZE = 20;

    String msg;
    GreenfootImage msgboxImg;
    Color fontColor = Color.WHITE;
    Color backgroundColor = new Color(0x228b22);
    Font font = new Font("SansSerif", Font.PLAIN, DEFAULT_FONT_SIZE);
    boolean isDrawn2center = true; //　中央に描かれるか

    public MessageBox(String msgConfigName, int width, int height) {
        // メッセージをファイルから読み込み
        String fileName = String.format("messages/%s.txt", msgConfigName);
        try {
            msg = new String(
                    Files.readAllBytes(Paths.get(fileName)),
                    StandardCharsets.UTF_8);
        } catch (Exception e) {
            System.out.println(String.format("cannot access \"%s\".", fileName));
            Greenfoot.stop();
        }

        msgboxImg = new GreenfootImage(width, height);
        setImage(msgboxImg);
        draw();
    }

    public void setColor(Color fontColor, Color backgroundColor) {
        this.fontColor = fontColor;
        this.backgroundColor = backgroundColor;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    /**
     * メッセージボックスを描画する
     */
    public void draw() {
        // 背景を描画
        msgboxImg.clear();
        msgboxImg.setColor(backgroundColor);
        msgboxImg.fill();

        // メッセージを描画
        Graphics graphics = msgboxImg.getAwtImage().getGraphics();
        graphics.setFont(font);
        graphics.setColor(fontColor);
        if (isDrawn2center) {
            drawStringToCenter(msg, graphics, 0, 0,
                    msgboxImg.getWidth(), msgboxImg.getHeight());
        } else {
            drawStringInRect(msg, graphics, 0, 0,
                    msgboxImg.getWidth(), msgboxImg.getHeight());
        }
    }

    /**
     * 画像の指定した領域に文字列を描画する。
     *
     * @param str 描画対象の文字列
     * @param graphics 描画対象の画像
     * @param x 描画する範囲の左上のx座標
     * @param y 描画する範囲の左上のy座標
     * @param width 描画する領域の幅
     * @param height 描画する領域の高さ
     */
    public void drawStringInRect(String str, Graphics graphics,
            int x, int y, int width, int height) {
        drawString(str, graphics, x, y, width, height, false);
    }

    /**
     * 画像の指定した領域の中央に文字列を描画する。
     *
     * @param str 描画対象の文字列
     * @param graphics 描画対象の画像
     * @param x 描画する範囲の左上のx座標
     * @param y 描画する範囲の左上のy座標
     * @param width 描画する領域の幅
     * @param height 描画する領域の高さ
     */
    public void drawStringToCenter(String str, Graphics graphics,
            int x, int y, int width, int height) {
        // 描画領域を取得する。画像に変更は加えない
        int[] drawnSpace = drawString(str, graphics, x, y, width, height, true);

        // 中央に描画する
        drawString(str, graphics,
                x + width / 2 - drawnSpace[0] / 2,
                y + height / 2 - drawnSpace[1] / 2,
                width, height, false);
    }

    /**
     * 文字列を描画して、描画した領域の高さと幅を返す。
     *
     * @param str 描画対象の文字列
     * @param graphics 描画対象の画像
     * @param x 描画する範囲の左上のx座標
     * @param y 描画する範囲の左上のy座標
     * @param width 描画する領域の幅
     * @param height 描画する領域の高さ
     * @param isDryRun trueならgraphicsに文字列を描画しない
     * @return {width, height}
     */
    private int[] drawString(String str, Graphics graphics,
            int x, int y, int width, int height, boolean isDryRun) {
        int drawnWidth = 0;
        int drawnHeight = 0;

        for (String targetStr : str.split("\n")) {
            FontMetrics fontmetrics = graphics.getFontMetrics();

            // 空行が処理されない問題の回避策です
            if (targetStr.equals("")) {
                targetStr = " ";
            }

            while (true) {
                // 横幅に収まるような最大文字列長を探る
                int i = 1; // 切り取る文字数+1
                double strWidth = 0;
                double strHeight = 0;
                while (i <= targetStr.length()) {
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
                    break;
                }

                // 描画領域のサイズを更新
                drawnWidth = (int) Math.max(drawnWidth, strWidth);
                drawnHeight += strHeight;

                // 画像に描画して、次の行の描画領域を設定
                if (!isDryRun) {
                    System.out.println("draw: " + targetStr.substring(0, i));
                    graphics.drawString(targetStr.substring(0, i), x, y + (int) strHeight);
                }
                y += strHeight;
                height -= strHeight;
                targetStr = targetStr.substring(i);
            }
        }

        int[] drawnSpace = {drawnWidth, drawnHeight};
        return drawnSpace;
    }
}
