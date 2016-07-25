
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
        GreenfootImage rogo = Config.getImage(this, "rogo");
        getBackground().drawImage(rogo, getWidth() / 2 - rogo.getWidth() / 2, 0);
        
        Button startButton = addButton("Game Start", 900, 450);
        Button stageButton = addButton("Select Stage", 975, 550);
        Button settingsButton = addButton("Settings", 1050, 650);

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

    private Button addButton(String key, int x, int y) {
        Button button = new Button(key);
        addObject(button, x, y);
        return button;
    }
}
