
import greenfoot.Greenfoot;
import greenfoot.GreenfootImage;
import greenfoot.GreenfootSound;
import java.awt.Color;
import java.awt.Font;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Properties;

/**
 * ゲームの設定を管理する。設定はJSON形式でローカルストレージに保存される。
 */
final public class Config {

    final static int WORLD_WIDTH = 1200;
    final static int WORLD_HEIGHT = 800;

    final static Color OVERLAY_COLOR = new Color(0x55000000, true);

    final static int BLINK_MSGBOX_WIDTH = 30 * 11;
    final static int BLINK_MSGBOX_HEIGHT = 50;
    final static Color BLINK_MSGBOX_FONT_COLOR = Color.WHITE;
    final static Color BLINK_MSGBOX_BG_COLOR = new Color(0, true);
    final static Font BLINK_MSGBOX_FONT = new Font("SansSerif", Font.PLAIN, 30);

    final static int STAGE_BUTTON_WIDTH = 200;
    final static int STAGE_BUTTON_HEIGHT = 50;
    final static int STAGE_BUTTON_MARGIN = 20;
    final static Color STAGE_BUTTON_BG_COLOR = Color.GREEN;
    final static Color STAGE_BUTTON_BG_COLOR2 = Color.YELLOW;
    final static Font STAGE_BUTTON_FONT = new Font("SansSerif", Font.PLAIN, 30);

    /**
     * ステージの遷移先一覧 次に遷移するワールドを取得は以下のように行う Class nextWorld =
     * stageTransition.get(beforeWorld).get(nextState);
     */
    private final static Object[][] WORLD_TRANSITION_PATTERN = {
        // {from, key, to}
        {StartWorld.class, "start", Stage1.class},
        {StartWorld.class, "stage-select", StageSelectWorld.class},
        {StartWorld.class, "settings", SettingsWorld.class},
        {PlayWorld.class, "title", StartWorld.class},
        {Stage1.class, "replay", Stage1.class},
        {Stage1.class, "next", StageGH.class},
        {StageGH.class, "replay", StageGH.class},
        {StageGH.class, "next", StageKK.class},
        {StageKK.class, "replay", StageKK.class},
        {StageKK.class, "next", StageKK2.class},
        {StageKK2.class, "replay", StageKK2.class},
        {StageKK2.class, "next", StageKK3.class},
        {StageKK3.class, "replay", StageKK3.class},
        {StageKK3.class, "next", StartWorld.class},};
    /**
     * ステージの一覧。レベル順に並べること。
     */
    public final static Class[] STAGES = {
        Stage1.class,
        StageGH.class,
        StageKK.class,
        StageKK2.class,
        StageKK3.class};
    /**
     * 背景画像の一覧。
     */
    private final static Object[][] IMAGES = {
        // {World or Actor, type, file, width, height, rotateImage}
        // width, heightが共に0なら、リサイズを行いません
        // リサイズしてから回転を行う。
        {null, StartWorld.class, "rogo", "rogo/title2.png", 0, 0, 0},
        {null, StartWorld.class, "bg", "bg/titleBG4.jpg", WORLD_WIDTH, WORLD_HEIGHT, 0},
        {null, StageSelectWorld.class, "bg", "bg/titleBG4.jpg", WORLD_WIDTH, WORLD_HEIGHT, 0},
        {null, PlayWorld.class, "bg", "bg/space.jpg", 0, 0, 0},
        {BaseWorld.class, Ball.class, "bg", "ball/r2.png", 40, 40, 0},
        {BaseWorld.class, Block.class, "bg", "block/b.png", 20, 20, 0},
        {BaseWorld.class, Wall.class, "bg", "debris/deb4-1.png", 72, 50, 0},
        {BaseWorld.class, CursorBarrier.class, "bg", "barrier.png", 71, 179, 0},
        {BaseWorld.class, Cursor.class, "bg", "ufoShip.png", 70, 70, -90},
        {BaseWorld.class, Goal.class, "bg", "goal/jupiter.png", 100, 100, 0},
        {BaseWorld.class, GoalAnimate.class, "bg", "goal/jupiter.png", 100, 100, 0},
        {BaseWorld.class, GoalAnimate.class, "broken", "goal/bomb.png", 400, 400, 0},};

    /**
     * サウンドの一覧。
     */
    private final static Object[][] SOUNDS = {
        // {World or Actor, type, file}
        {StartWorld.class, "bgm", "bgm/title.mp3"},
        {PlayWorld.class, "bgm", "bgm/battle.mp3"},
        {Button.class, "mouseIn", "se/button-onMouseIn.mp3"},
        {Button.class, "mouseClicked", "se/button-onMouseClicked.mp3"},
        {Block.class, "broken", "se/block-broken.mp3"},
        {Goal.class, "broken", "se/core-destroyed.mp3"},};

    private final static String CONFIG_FILE_PATH = "config.properties";

    private static boolean isLoaded = false;
    private static Properties prop = new Properties();

    /**
     * configファイルによって変更可能な値
     */
    private final static String[][] PROPERTIES = {
        {"enableBGM", "true"},
        {"enableSoundEffect", "true"},
        {"enableAutoSave", "true"},};

    /**
     * ファイルから設定を読み込む。プログラム起動時に一回だけ実行すればよい。 ファイルが存在しなければ、何もせず、読み込みが完了したとみなす。
     */
    public static void load() {
        String filePaht = CONFIG_FILE_PATH.replaceFirst("^~", System.getProperty("user.home"));
        try (InputStreamReader configFile = new InputStreamReader(new FileInputStream(Paths.get(filePaht).toFile()), StandardCharsets.UTF_8)) {
            prop.load(configFile);
            isLoaded = true;
        } catch (FileNotFoundException e) {
            isLoaded = true;
        } catch (IOException e) {
            e.printStackTrace(System.out);
            Greenfoot.stop();
        }
    }

    /**
     * ファイルへ設定を書き込む。設定変更を行った直後に必ず呼び出す必要がある。
     */
    public static void save() {
        String filePaht = CONFIG_FILE_PATH.replaceFirst("^~", System.getProperty("user.home"));
        try (OutputStreamWriter configFile = new OutputStreamWriter(new FileOutputStream(Paths.get(filePaht).toFile()), StandardCharsets.UTF_8)) {
            prop.store(configFile, "BlockGame config file");
        } catch (IOException e) {
            e.printStackTrace(System.out);
            Greenfoot.stop();
        }
    }

    public static String getProperty(String key) {
        if (!isLoaded) {
            throw new IllegalStateException();
        }

        for (String[] tuple : PROPERTIES) {
            String key2 = tuple[0];
            String defaultValue = tuple[1];
            if (key2.equals(key)) {
                return prop.getProperty(key, defaultValue);
            }
        }
        throw new IllegalArgumentException();
    }

    public static boolean getBoolean(String key) {
        return Boolean.parseBoolean(getProperty(key));
    }

    /**
     * 次に遷移するべきWorldを取得する。
     *
     * @param from
     * @param key
     * @return 次に遷移するWorldクラス。遷移先が未定義ならエラー。
     */
    public static Class getNextWorld(Class from, String key) {
        if (!isLoaded) {
            throw new IllegalStateException();
        }

        for (Object[] tuple : WORLD_TRANSITION_PATTERN) {
            if (tuple.length != 3) {
                throw new IllegalStateException("worldTransitionPattern の要素に、長さが3以外の配列が入っている: " + Arrays.deepToString(tuple));
            }

            Class fromClass = (Class) tuple[0];
            String key2 = (String) tuple[1];
            Class toClass = (Class) tuple[2];

            if (!BaseWorld.class.isAssignableFrom(fromClass) || !BaseWorld.class.isAssignableFrom(toClass)) {
                throw new IllegalStateException("worldTransitionPattern の要素に、BaseWorld のサブクラス以外を含めてはいけない: " + Arrays.deepToString(tuple));
            }

            if (from.equals(fromClass) && key.equals(key2)) {
                return toClass;
            }
        }
        throw new IllegalArgumentException("遷移先が存在しない");
    }

    /**
     * 指定したステージのスコアを取得する。
     *
     * @param playWorld
     * @return ステージのスコア。もし、まだまだプレイしていないステージなら0を返す。
     */
    public static int getScore(Class playWorld) {
        if (!isLoaded) {
            throw new IllegalStateException();
        }

        return Integer.parseInt(prop.getProperty("score/" + playWorld.getSimpleName(), "0"));
    }

    /**
     * 指定したステージのスコアを設定する。
     *
     * @param playWorld
     * @param score 0以上
     */
    public static void setScore(Class playWorld, int score) {
        if (!isLoaded) {
            throw new IllegalStateException();
        }
        if (score < 0) {
            throw new IllegalArgumentException("score >= 0を満たしていません: score=" + score);
        }

        prop.setProperty("score/" + playWorld.getSimpleName(), "" + score);
    }

    /**
     * 指定したオブジェクトに対応する画像を返す
     *
     * @param obj
     * @param key
     * @return 画像
     */
    public static GreenfootImage getImage(Object obj, String key) {
        return getOrigianlImage(obj, key, true);

    }

    /**
     * 指定したオブジェクトに対応する、リサイズ済みの画像を返す
     *
     * @param obj
     * @param key
     * @param width 画像の幅
     * @param height 画像の高さ
     * @return リサイズ後の画像
     */
    public static GreenfootImage getImage(Object obj, String key, int width, int height) {
        GreenfootImage img = getOrigianlImage(obj, key, false);
        img.scale(width, height);
        return img;
    }

    private static GreenfootImage getOrigianlImage(Object obj, String key, boolean isResize) {
        Class clazz = obj.getClass();
        Class worldClazz = null;
        if (obj instanceof BaseActor) {
            worldClazz = ((BaseActor) obj).getWorld().getClass();
        }

        for (Object[] tuple : IMAGES) {
            if (tuple.length != 7) {
                throw new IllegalStateException("IMAGES の要素は、長さ7以外の配列を含めてはいけない" + Arrays.deepToString(tuple));
            }

            Class worldClass = (Class) tuple[0];
            Class targetClass = (Class) tuple[1];
            String key2 = (String) tuple[2];
            String filePath = (String) tuple[3];
            int width = (int) tuple[4];
            int height = (int) tuple[5];
            int rotate = (int) tuple[6];

            if (obj instanceof BaseActor) {
                // 異なるWorld用の設定なら無視
                if (worldClass == null || !worldClass.isAssignableFrom(worldClazz)) {
                    continue;
                }
            }

            if (targetClass.isAssignableFrom(clazz) && key2.equals(key)) {
                GreenfootImage img = new GreenfootImage(filePath);
                if (isResize && width != 0 && height != 0) {
                    img.scale(width, height);
                }
                img = Utils.rotateImage(img, rotate);
                return img;
            }
        }
        throw new IllegalArgumentException("このクラスに対応する画像がConfigクラスで指定されていない");
    }

    public static GreenfootSound getSound(Class playWorld, String key) {
        for (Object[] tuple : SOUNDS) {
            if (tuple.length != 3) {
                throw new IllegalStateException("SOUNDS の要素に、長さ3以外の配列を含めてはいけない: " + tuple);
            }

            Class<PlayWorld> world = (Class<PlayWorld>) tuple[0];
            String key2 = (String) tuple[1];
            String soundFilePath = (String) tuple[2];

            if (world.isAssignableFrom(playWorld) && key2.equals(key)) {
                return new GreenfootSound(soundFilePath);
            }
        }
        throw new IllegalArgumentException("存在しないパターンです");
    }
}
