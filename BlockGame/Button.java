
import greenfoot.*;

public class Button extends BaseActor {

    GreenfootImage normalImg, pressingImg;
    int bgcolor = 0;
    int color = 0;

    public Button(GreenfootImage normal, GreenfootImage pressing) {
        normalImg = normal;
        pressingImg = pressing;
        setImage(normalImg);
    }

	@Override
	public void onMouseIn(MouseInfo mouse){
        if (getActorStatus() != ActorStatus.ALIVE) {
            return;
        }

        super.onMouseIn(mouse);
		(new GreenfootSound("se/button-onMouseIn.mp3")).play();
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
	public void onMouseClicked(MouseInfo mouse){
        if (getActorStatus() != ActorStatus.ALIVE) {
            return;
        }

		super.onMouseClicked(mouse);
		(new GreenfootSound("se/button-onMouseClicked.mp3")).play();
	}
}
