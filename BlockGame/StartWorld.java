
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
    private GreenfootImage blackBg, bg, rogo;
    private boolean isButtonsAdded = false;
    private Button startButton;
    private Button stageButton;
    private Button settingsButton;

    private int count = 0;
    private final int ANIME_SPEED = 5;
    private final int ANIME_STEP = 20;

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
        rogo = Config.getImage(this, "rogo");
        bg = getBackground();
        // 背景を真っ黒にする
        blackBg = new GreenfootImage(getWidth(), getHeight());
        blackBg.setColor(Color.BLACK);
        blackBg.fill();
        setBackground(blackBg);
    }

    @Override
    public void tick() {
        super.tick();
        count++;
        if (count % ANIME_SPEED != 0) {
            return;
        }

        int step = count / ANIME_SPEED;
        if (step <= ANIME_STEP) {
            // ロゴが光って登場するアニメーションをする
            // ロゴのalpha値の上限を上げていくことで実現している。
            int alpha = 0xff * step / ANIME_STEP;
            GreenfootImage newRogo = new GreenfootImage(rogo);
            GreenfootImage newBg = new GreenfootImage(blackBg);

            // 調整したロゴを描画する
            Utils.updateMaxAlpha(newRogo, alpha);
            newBg.drawImage(newRogo, getWidth() / 2 - rogo.getWidth() / 2, 0);
            setBackground(newBg);
        } else if (step < 2 * ANIME_STEP) {
            // 初回だけはボタンを追加する
            if (!isButtonsAdded) {
                isButtonsAdded = true;
                addButtons();
            }

            // 背景とロゴを一瞬光らせるアニメーションをする
            // 背景のalpha値の上限を上げていくことで実現している。
            int alpha = 0xff * (step - ANIME_STEP) / ANIME_STEP;
            GreenfootImage newBg = new GreenfootImage(bg);

            // 調整した背景にロゴを描画する
            Utils.updateMaxAlpha(newBg, alpha);
            newBg.drawImage(rogo, getWidth() / 2 - rogo.getWidth() / 2, 0);
            setBackground(newBg);

            startButton.setMaxAlpha(alpha);
            stageButton.setMaxAlpha(alpha);
            settingsButton.setMaxAlpha(alpha);
        } else if (step == 3 * ANIME_STEP) {
            // ボタンを追加する
        }
    }

    private void addButtons() {
        startButton = addButton("Game Start", 900, 450);
        stageButton = addButton("Select Stage", 975, 550);
        settingsButton = addButton("Settings", 1050, 650);

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
        Button button = new ImageButton(key);
        addObject(button, x, y);
        return button;
    }
}
