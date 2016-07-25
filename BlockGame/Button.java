
import greenfoot.*;
import java.awt.Font;
import java.awt.Graphics;

public class Button extends BaseActor {

    GreenfootImage normalImg, pressingImg;
    int bgcolor = 0;
    int color = 0;
    private GreenfootSound onMouseInSound;
    private GreenfootSound onMouseClickedSound;
    private String key;

    private Button() {
        onMouseInSound = Config.getSound(this.getClass(), "mouseIn");
        onMouseClickedSound = Config.getSound(this.getClass(), "mouseClicked");
    }

    public Button(String key) {
        this();
        this.key = key;
    }

    public Button(String str, Font font, GreenfootImage normal, GreenfootImage pressing) {
        this();

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

    @Override
    public void addedToWorld(World world) {
        // コンストラクタでnormalImgが設定されていたら、Configから画像を取得しない
        if (normalImg != null) {
            return;
        }

        normalImg = Config.getImage(this, key);
        setImage(normalImg);
        pressingImg = new GreenfootImage(normalImg);
        // TODO: ボタンが押された時の画像を生成する処理を入れる
    }

    @Override
    public void onMouseIn(MouseInfo mouse) {
        if (getActorStatus() != ActorStatus.ALIVE) {
            return;
        }

        super.onMouseIn(mouse);
        if (Config.getBoolean("enableSoundEffect") && onMouseInSound != null) {
            onMouseInSound.play();
        }
    }

    @Override
    public void onMouseDown(MouseInfo mouse) {
        if (getActorStatus() != ActorStatus.ALIVE) {
            return;
        }

        super.onMouseDown(mouse);
        setImage(pressingImg);
    }

    @Override
    public void onMouseUp(MouseInfo mouse) {
        if (getActorStatus() != ActorStatus.ALIVE) {
            return;
        }

        super.onMouseUp(mouse);
        setImage(normalImg);
    }

    @Override
    public void onMouseClicked(MouseInfo mouse) {
        if (getActorStatus() != ActorStatus.ALIVE) {
            return;
        }

        super.onMouseClicked(mouse);
        if (Config.getBoolean("enableSoundEffect") && onMouseClickedSound != null) {
            onMouseClickedSound.play();
        }
    }
}
