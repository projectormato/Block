
import greenfoot.*;

public abstract class Button extends BaseActor {

    GreenfootImage normalImg, pressingImg;
    int bgcolor = 0;
    int color = 0;
    private GreenfootSound onMouseInSound;
    private GreenfootSound onMouseClickedSound;
    protected String key;

    public Button() {
        onMouseInSound = Config.getSound(this.getClass(), "mouseIn");
        onMouseClickedSound = Config.getSound(this.getClass(), "mouseClicked");
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
