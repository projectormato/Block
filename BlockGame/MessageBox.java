
import greenfoot.Greenfoot;
import greenfoot.GreenfootImage;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * 画面にオーバーレイする形でメッセージを表示する。 クリックしたときのアクションは、EventListenerで登録しないと動作しない。
 *
 * 表示するメッセージ内容は、指定されたテキストファイルから読み込む。
 *
 * @author yuuki0xff
 */
public class MessageBox extends BaseActor {

    final static int DEFAULT_FONT_SIZE = 20;

    String msg;
    GreenfootImage msgboxImg;
    Color fontColor = Color.WHITE;
    Color backgroundColor = new Color(0x228b22);
    Font font = new Font("SansSerif", Font.PLAIN, DEFAULT_FONT_SIZE);
    boolean isDrawn2center = true; //　中央に描かれるか

    /**
     * 指定したサイズで画面に覆いかぶさるようなメッセージボックスを作成する。
     *
     * @param msgConfigName
     * @param width
     * @param height
     */
    public MessageBox(String msgConfigName, int width, int height) {
        // メッセージをファイルから読み込み
        String fileName = String.format("messages/%s.txt", msgConfigName);
        try {
            msg = new String(
                    Files.readAllBytes(Paths.get(fileName)),
                    StandardCharsets.UTF_8);
        } catch (Exception e) {
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
            Utils.drawStringToCenter(msg, graphics, 0, 0,
                    msgboxImg.getWidth(), msgboxImg.getHeight());
        } else {
            Utils.drawStringInRect(msg, graphics, 0, 0,
                    msgboxImg.getWidth(), msgboxImg.getHeight());
        }
    }
}
