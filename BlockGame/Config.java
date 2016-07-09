
import greenfoot.Greenfoot;
import greenfoot.GreenfootSound;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * ゲームの設定を管理する。設定はJSON形式でローカルストレージに保存される。
 */
final public class Config {

    /**
     * ステージの遷移先一覧 次に遷移するワールドを取得は以下のように行う Class nextWorld =
     * stageTransition.get(beforeWorld).get(nextState);
     */
    private final static Object[][] WORLD_TRANSITION_PATTERN = {
        // {from, key, to}
        {StartWorld.class, "start", Stage1.class},
        {StartWorld.class, "stage-select", StageSelectWorld.class},
        {StartWorld.class, "settings", SettingsWorld.class},
        {Stage1.class, "next", StartWorld.class},};
    /**
     * ステージの一覧。レベル順に並べること。
     */
    private final static Object[] STAGES = {
        Stage1.class,};
    /**
     * 背景画像の一覧。
     */
    private final static Object[][] IMAGES = {
        // {World or Actor, file}
        {StartWorld.class, "bg/background.png"},
        {Stage1.class, "bg/background.png"},};
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

    private final static String CONFIG_FILE_PATH = "~/.config/BlockGame/config.ini";

    private static boolean isLoaded = false;
    private static Properties prop = new Properties();

    /**
     * configファイルによって変更可能な値
     */
    private final static String[][] PROPERTIES = {
        {"enableBGM", "true"},
        {"enableSooundEffect", "true"},
        {"enableAutoSave", "true"},};

    /**
     * ファイルから設定を読み込む。プログラム起動時に一回だけ実行すればよい。 ファイルが存在しなければ、何もせず、読み込みが完了したとみなす。
     */
    public static void load() {
        String filePaht = CONFIG_FILE_PATH.replaceFirst("^~", System.getProperty("user.home"));
        try (InputStreamReader configFile = new InputStreamReader(new FileInputStream(Paths.get(filePaht).toFile()), StandardCharsets.UTF_8)) {
            prop.load(configFile);
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
        // プロパティリスト入っているならすぐに結果を返す
        for (String[] tuple : PROPERTIES) {
            String key2 = tuple[0];
            String defaultValue = tuple[1];
            return prop.getProperty(key, defaultValue);
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
                throw new IllegalStateException("worldTransitionPattern の要素に、長さが3以外の配列が入っている: " + tuple);
            }

            Class fromClass = (Class) tuple[0];
            String key2 = (String) tuple[1];
            Class toClass = (Class) tuple[2];

            if (!fromClass.isAssignableFrom(BaseWorld.class) || !toClass.isAssignableFrom(BaseWorld.class)) {
                throw new IllegalStateException("worldTransitionPattern の要素に、BaseWorld のサブクラス以外を含めてはいけない: " + tuple);
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
