
import greenfoot.*;
import java.awt.Color;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

/**
 * スタート画面を表示する。
 */
public class StartWorld extends BaseWorld {

    private GreenfootSound bgm;

    public StartWorld() {
        logger = Logger.getLogger("SampleLogging");
        if (ENABLE_LOGGING) {
            try {
                FileHandler fh = new FileHandler(LOG_FILE, true);
                fh.setFormatter(new java.util.logging.SimpleFormatter());
                logger.addHandler(fh);
            } catch (IOException e) {
                e.printStackTrace(System.out);
                // とにかく実行を中断
                throw new AssertionError();
            }
        }

        Config.load();

        if (Config.getBoolean("enableBGM")) {
            bgm = Config.getSound(this.getClass(), "bgm");
            bgm.playLoop();
        }
        prepare();
    }

    /**
     * Prepare the world for the start of the program. That is: create the
     * initial objects and add them to the world.
     */
    private void prepare() {
        // 背景とロゴを描画
        GreenfootImage rogo = Config.getImage(this.getClass(), "rogo");
        getBackground().drawImage(rogo, getWidth() / 2 - rogo.getWidth() / 2, 50);

        // ボタンを追加
        Button startButton = addButton("Game Start", 0, 400, 200, 50, 20, Color.BLACK);
        Button stageButton = addButton("Select Stage", 0, 470, 200, 50, 20, Color.BLACK);
        Button settingsButton = addButton("Settings", 0, 540, 200, 50, 20, Color.BLACK);

        startButton.addListner(new EventListener() {
            @Override
            public void onMouseClicked(MouseInfo mouse) {
                System.out.println("Game Start");
                changeWorld("start");
            }
        });

        stageButton.addListner(new EventListener() {
            @Override
            public void onMouseClicked(MouseInfo mouse) {
                System.out.println("Select Stage");
                changeWorld("stage-select");
            }
        });

        settingsButton.addListner(new EventListener() {
            @Override
            public void onMouseClicked(MouseInfo mouse) {
                System.out.println("Settings");
                changeWorld("settings");
            }
        });
    }

    /**
     * BGMを止めてからWorldを切り替える
     *
     * @param key 遷移先
     */
    @Override
    public void changeWorld(String key) {
        if (bgm != null) {
            bgm.stop();
            bgm = null;
        }

        super.changeWorld(key);
    }

    private Button addButton(String str, int x, int y, int width, int height, int fontSize, Color fontColor) {
        return addButton(str, x, y, width, height, fontSize, fontColor, Color.GREEN, Color.YELLOW);
    }

    private Button addButton(String str, int x, int y, int width, int height, int fontSize, Color fontColor, Color bgColor1, Color bgColor2) {
        GreenfootImage[] buttonImages = new GreenfootImage[2];

        GreenfootImage buttonString = new GreenfootImage(str, fontSize, fontColor, new Color(0, 0, 0, 0));

        buttonImages[0] = new GreenfootImage(width, height);
        buttonImages[1] = new GreenfootImage(buttonImages[0]);
        buttonImages[0].setColor(bgColor1);
        buttonImages[0].fill();
        buttonImages[0].drawImage(buttonString, width / 2 - buttonString.getWidth() / 2, height / 2 - buttonString.getHeight() / 2);

        buttonImages[1].setColor(bgColor2);
        buttonImages[1].fill();
        buttonImages[1].drawImage(buttonString, width / 2 - buttonString.getWidth() / 2, height / 2 - buttonString.getHeight() / 2);

        Button button = new Button(buttonImages[0], buttonImages[1]);
        addObject(button, getWidth() / 2, y);
        return button;
    }
}
