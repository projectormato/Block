
import greenfoot.GreenfootImage;
import java.awt.Font;
import java.awt.Graphics;

/**
 * 背景画像と文字列からなるボタン
 *
 * @author yuuki0xff
 */
public class StringButton extends Button {

    /**
     * 指定した画像に指定したフォントで文字を描画したボタンを作成する。
     *
     * @param str ボタンに描画する文字列
     * @param font 文字列のフォント
     * @param normal 通常時の背景画像
     * @param pressing ボタンが押された時の背景画像
     */
    public StringButton(String str, Font font, GreenfootImage normal, GreenfootImage pressing) {
        // 渡された画像を破壊しないよう、複製を作る
        normalImg = new GreenfootImage(normal);
        pressingImg = new GreenfootImage(pressing);

        // 通常時の画像(デフォルト)
        Graphics g;
        g = normalImg.getAwtImage().createGraphics();
        g.setFont(font);
        Utils.drawStringToCenter(str, g, 0, 0, normalImg.getWidth(), normalImg.getHeight());
        setImage(normalImg);

        // 押された時の画像
        g = pressingImg.getAwtImage().createGraphics();
        g.setFont(font);
        Utils.drawStringToCenter(str, g, 0, 0, pressingImg.getWidth(), pressingImg.getHeight());
    }
}
