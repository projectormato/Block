
import greenfoot.*;

public class Button extends BaseActor {

    GreenfootImage normalImg, pressingImg;
    int bgcolor = 0;
    int color = 0;
    private GreenfootSound onMouseInSound;
    private GreenfootSound onMouseClickedSound;

    public Button(GreenfootImage normal, GreenfootImage pressing) {
        normalImg = normal;
        pressingImg = pressing;
        setImage(normalImg);

        onMouseInSound = Config.getSound(this.getClass(), "mouseIn");
        onMouseClickedSound = Config.getSound(this.getClass(), "mouseClicked");
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
