
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
    public void onMouseDown(MouseInfo mouse) {
        super.onMouseDown(mouse);
        setImage(pressingImg);
    }

    @Override
    public void onMouseUp(MouseInfo mouse) {
        super.onMouseUp(mouse);
        setImage(normalImg);
    }
}
