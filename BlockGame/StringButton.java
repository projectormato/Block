
import greenfoot.GreenfootImage;
import greenfoot.World;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

/**
 * 背景画像と文字列からなるボタン
 *
 * @author yuuki0xff
 */
public class StringButton extends Button {

    Font font = Config.STAGE_BUTTON_FONT;
    Color color = Color.WHITE;

    /**
     * 指定した画像に指定したフォントで文字を描画したボタンを作成する。
     *
     * @param str ボタンに描画する文字列
     */
    public StringButton(String str) {
        GreenfootImage img;
        Graphics g;

        // 文字を描画するのに必要な最小のサイズを調べる
        img = new GreenfootImage(1000, 100);
        g = img.getAwtImage().getGraphics();
        g.setFont(font);
        int[] drawedSize = Utils.drawString(str, g, 0, 0, img.getWidth(), img.getHeight(), true);
        int width, height;
        // 文字列が描画できるよう、少し余裕を持たせておく
        width = drawedSize[0] + 1;
        height = drawedSize[1] + 1;

        // 文字列を画像に変換
        img = new GreenfootImage(width, height);
        g = img.getAwtImage().getGraphics();
        g.setColor(color);
        g.setFont(font);
        Utils.drawStringToCenter(str, g, 0, 0, width, height);
        g.dispose();

        normalImg = img;
        setImage(normalImg);
    }

    @Override
    public void addedToWorld(World world) {
        super.addedToWorld(world);
        draw();
    }
}
