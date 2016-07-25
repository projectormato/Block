
import greenfoot.*;

public class StageSelectWorld extends BaseWorld {

    public StageSelectWorld() {
        prepare();
    }

    private void prepare() {
        GreenfootImage normalImage = new GreenfootImage(
                Config.STAGE_BUTTON_WIDTH, Config.STAGE_BUTTON_HEIGHT);
        normalImage.setColor(Config.STAGE_BUTTON_BG_COLOR);
        normalImage.fill();

        GreenfootImage pressingImage = new GreenfootImage(normalImage);
        pressingImage.setColor(Config.STAGE_BUTTON_BG_COLOR2);
        pressingImage.fill();

        int i = 0;
        int offsetY = Config.STAGE_BUTTON_HEIGHT / 2 + Config.STAGE_BUTTON_MARGIN;
        for (final Class stageCls : Config.STAGES) {
            String stageName = stageCls.getSimpleName();
            Button button = new StringButton(stageName);
            button.addListner(new EventListener() {
                @Override
                public void onMouseClicked(MouseInfo mouse) {
                    // クリックしたら、指定したステージに切り替え
                    try {
                        BaseWorld nextWorld = (BaseWorld) stageCls.newInstance();
                        Greenfoot.setWorld(nextWorld);
                    } catch (Exception e) {
                        e.printStackTrace(System.out);
                        Greenfoot.stop();
                    }
                }
            });
            addObject(button, getWidth() / 2,
                    offsetY + (Config.STAGE_BUTTON_HEIGHT + Config.STAGE_BUTTON_MARGIN) * i);
            i++;
        }
    }
}
