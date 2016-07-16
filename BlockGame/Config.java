
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
        {StageKK2.class, "next", StartWorld.class},};
    /**
     * ステージの一覧。レベル順に並べること。
     */
    private final static Object[] STAGES = {
        Stage1.class,};
    /**
     * 背景画像の一覧。
     */
    private final static Object[][] IMAGES = {
        // {World or Actor, type, file, width, height, rotateImage}
        // width, heightが共に0なら、リサイズを行いません
        // リサイズしてから回転を行う。
        {StartWorld.class, "rogo", "rogo/title2.png", 0, 0, 0},
        {StartWorld.class, "bg", "bg/titleBG4.jpg", WORLD_WIDTH, WORLD_HEIGHT, 0},
        {Stage1.class, "bg", "bg/space.png", WORLD_WIDTH, WORLD_HEIGHT, 0},
        {Ball.class, "bg", "ball/r2.png", 40, 40, 0},
        {Block.class, "bg", "block/b.png", 20, 20, 0},
        {CursorBarrier.class, "bg", "block/y.png", 150, 100, 90},
        {Cursor.class, "bg", "ufoShip.png", 70, 70, -90},};
    /**
     * サウンドの一覧。
     */
    private final static Object[][] SOUNDS = {
        // {World or Actor, type, file}
        {StartWorld.class, "bgm", "bgm/title.mp3"},
        {PlayWorld.class, "bgm", "bgm/battle.mp3"},
        {Button.class, "mouseIn", "se/button-onMouseIn.mp3"},
        {Button.class, "mouseClicked", "se/button-onMouseClicked.mp3"},
        {Block.class, "broken", "se/block-broken.mp3"},};

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

    public static GreenfootImage getImage(Class clazz, String key) {
        return getOrigianlImage(clazz, key, true);
    }

    public static GreenfootImage getImage(Class clazz, String key, int width, int height) {
        GreenfootImage img = getOrigianlImage(clazz, key, false);
        img.scale(width, height);
        return img;
    }

    private static GreenfootImage getOrigianlImage(Class clazz, String key, boolean isResize) {
        for (Object[] tuple : IMAGES) {
            if (tuple.length != 6) {
                throw new IllegalStateException("IMAGES の要素は、長さ6以外の配列を含めてはいけない" + Arrays.deepToString(tuple));
            }

            Class clazz2 = (Class) tuple[0];
            String key2 = (String) tuple[1];
            String filePath = (String) tuple[2];
            int width = (int) tuple[3];
            int height = (int) tuple[4];
            int rotate = (int) tuple[5];

            if (clazz2.isAssignableFrom(clazz) && key2.equals(key)) {
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
