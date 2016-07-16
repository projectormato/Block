
import greenfoot.*;

public class StageSelectWorld extends BaseWorld {

    public StageSelectWorld() {
        prepare();
    }

    private void prepare() {
        setBackground(Config.getImage(this.getClass(), "bg"));

        GreenfootImage normalImage = new GreenfootImage(
                Config.STAGE_BUTTON_WIDTH, Config.STAGE_BUTTON_HEIGHT);
        normalImage.setColor(Config.STAGE_BUTTON_BG_COLOR);
        normalImage.fill();

        GreenfootImage pressingImage = new GreenfootImage(normalImage);
        pressingImage.setColor(Config.STAGE_BUTTON_BG_COLOR2);
        pressingImage.fill();

        int i = 0;
        int offsetY = Config.STAGE_BUTTON_HEIGHT / 2 + Config.STAGE_BUTTON_MARGIN;
        for (Class stageCls : Config.STAGES) {
            String stageName = stageCls.getSimpleName();
            Button button = new Button(stageName, Config.STAGE_BUTTON_FONT, normalImage, pressingImage);
            addObject(button, getWidth() / 2,
                    offsetY + (Config.STAGE_BUTTON_HEIGHT + Config.STAGE_BUTTON_MARGIN) * i);
            i++;
        }
    }
}
