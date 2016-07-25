
import greenfoot.*;

/**
 * アニメーションをするボタン。
 *
 * @author yuuki0xff
 */
public abstract class Button extends BaseAnimationActor {

    GreenfootImage normalImg, pressingImg;
    int bgcolor = 0;
    int color = 0;
    private GreenfootSound onMouseInSound;
    private GreenfootSound onMouseClickedSound;
    protected String key;
    protected int maxAlpha = 0xff;

    public Button() {
        onMouseInSound = Config.getSound(this.getClass(), "mouseIn");
        onMouseClickedSound = Config.getSound(this.getClass(), "mouseClicked");
    }

    /**
     * このActorの透過度を変更します。この値が考慮されるのは、アニメーションを行っている最中だけです。
     *
     * @param maxAlpha
     */
    public void setMaxAlpha(int maxAlpha) {
        this.maxAlpha = maxAlpha;
    }

    @Override
    public void onMouseIn(MouseInfo mouse) {
        super.onMouseIn(mouse);
        if (getActorStatus() != ActorStatus.ALIVE) {
            return;
        }
        if (Config.getBoolean("enableSoundEffect") && onMouseInSound != null) {
            onMouseInSound.play();
        }
    }

    @Override
    public void onMouseDown(MouseInfo mouse) {
        super.onMouseDown(mouse);
        if (getActorStatus() != ActorStatus.ALIVE) {
            return;
        }

        // setImage(pressingImg);
    }

    @Override
    public void onMouseUp(MouseInfo mouse) {
        super.onMouseUp(mouse);
        if (getActorStatus() != ActorStatus.ALIVE) {
            return;
        }

        // setImage(normalImg);
    }

    @Override
    public void onMouseClicked(MouseInfo mouse) {
        super.onMouseClicked(mouse);
        if (getActorStatus() != ActorStatus.ALIVE) {
            return;
        }

        if (Config.getBoolean("enableSoundEffect") && onMouseClickedSound != null) {
            onMouseClickedSound.play();
        }
    }
}
