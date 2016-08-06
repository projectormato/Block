
import greenfoot.GreenfootImage;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.WritableRaster;

/**
 * 画像関連の便利なメソッド集
 *
 * @author yuuki0xff
 */
public final class Utils {

    // Utilsクラスのインスタンスを作成できなくする
    private Utils() {
    }

    /**
     * Rotates an image. Actually rotates a new copy of the image.
     *
     * @param img The image to be rotated
     * @param angle The angle in degrees
     * @return The rotated image
     */
    public static GreenfootImage rotateImage(GreenfootImage img, double angle) {
        double sin = Math.abs(Math.sin(Math.toRadians(angle)));
        double cos = Math.abs(Math.cos(Math.toRadians(angle)));
        int w = img.getWidth();
        int h = img.getHeight();
        int neww = (int) Math.floor(w * cos + h * sin);
        int newh = (int) Math.floor(h * cos + w * sin);
        GreenfootImage gimg = new GreenfootImage(neww, newh);
        Graphics2D g = gimg.getAwtImage().createGraphics();

        g.translate((neww - w) / 2, (newh - h) / 2);
        g.rotate(Math.toRadians(angle), w / 2, h / 2);
        g.drawRenderedImage(img.getAwtImage(), null);
        g.dispose();

        return gimg;
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
    public static void drawStringInRect(String str, Graphics graphics,
            int x, int y, int width, int height) {
        Utils.drawString(str, graphics, x, y, width, height, false);
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
    public static void drawStringToCenter(String str, Graphics graphics,
            int x, int y, int width, int height) {
        // 描画領域を取得する。画像に変更は加えない
        int[] drawnSpace = Utils.drawString(str, graphics, x, y, width, height, true);

        // 中央に描画する
        Utils.drawString(str, graphics,
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
    public static int[] drawString(String str, Graphics graphics,
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

    /**
     * 画像を中央に描画する
     *
     * @param to 描画される側
     * @param from 描画する画像 (非破壊)
     */
    public static void drawImageToCenter(GreenfootImage to, GreenfootImage from) {
        to.drawImage(from,
                (to.getWidth() - from.getWidth()) / 2,
                (to.getHeight() - from.getHeight()) / 2);
    }

    /**
     * 透過度の上限を変更する。
     *
     * @param img
     * @param maxAlpha 透過度の上限 (0: 透明, 0xff: 不透明)
     */
    public static void updateMaxAlpha(GreenfootImage img, int maxAlpha) {
        int w = img.getWidth();
        int h = img.getHeight();
        int[] rasterData = new int[w * h];
        WritableRaster raster = img.getAwtImage().getAlphaRaster();

        raster.getPixels(0, 0, w, h, rasterData);
        for (int j = 0; j < rasterData.length; j++) {
            rasterData[j] = Math.min(rasterData[j], maxAlpha);
        }
        raster.setPixels(0, 0, w, h, rasterData);
    }
}
