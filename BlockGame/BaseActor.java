
import greenfoot.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * 全てのActorの親クラス
 */
public class BaseActor extends Actor implements EventHandler {

    final private static int ATTACK_ABILITY = 100;
    final private static int DEFENSIVE_ABILITY = 0;

    // シルエットの画像をキャッシュする
    static GreenfootImage imageCache, silhouetteCache;

    protected Logger logger;
    private List<String> listenKeys = new ArrayList<>();
    private Map<String, Boolean> lastKeyStatusMap = new HashMap<>();
    private int mouseStatus;
    private EventListener listener;
    private int maxHp;
    private int hp;
    private ActorStatus actorStatus = ActorStatus.ALIVE;
    // 衝突判定用のシルエット画像
    private GreenfootImage silhouette;

    public BaseActor() {
        maxHp = 100;
        hp = maxHp;
    }

    /**
     * 何もしない。このメソッドはオーバーライドできません。BaseWorldクラスから呼び出されるイベントハンドラ内で必要な動作を実装してください。
     */
    final public void act() {
    }

    protected void setImage() {
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void addListner(EventListener listener) {
        this.listener = listener;
    }

    @Override
    public void tick() {
    }

    @Override
    public void onDied() {
        if (actorStatus == ActorStatus.DIED) {
            // 死んだら即座に削除
            actorStatus = ActorStatus.REMOVED;
        }
    }

    @Override
    public void onMouseIn(MouseInfo mouse) {
        if (listener != null) {
            listener.onMouseIn(mouse);
        }
    }

    @Override
    public void onMouseOut(MouseInfo mouse) {
        if (listener != null) {
            listener.onMouseOut(mouse);
        }
    }

    @Override
    public void onMouseMoved(MouseInfo mouse) {
        if (listener != null) {
            listener.onMouseMoved(mouse);
        }
    }

    @Override
    public void onMouseDown(MouseInfo mouse) {
        if (listener != null) {
            listener.onMouseDown(mouse);
        }
    }

    @Override
    public void onMouseHolding(MouseInfo mouse) {
        if (listener != null) {
            listener.onMouseHolding(mouse);
        }
    }

    @Override
    public void onMouseUp(MouseInfo mouse) {
        if (listener != null) {
            listener.onMouseUp(mouse);
        }
    }

    @Override
    public void onMouseClicked(MouseInfo mouse) {
        if (listener != null) {
            listener.onMouseClicked(mouse);
        }
    }

    @Override
    public void onMouseDragging(MouseInfo mouse) {
        if (listener != null) {
            listener.onMouseDragging(mouse);
        }
    }

    @Override
    public void onMouseDragged(MouseInfo mouse) {
        if (listener != null) {
            listener.onMouseDragged(mouse);
        }
    }

    @Override
    public void onKeyDown(String key) {
        if (listener != null) {
            listener.onKeyDown(key);
        }
    }

    @Override
    public void onKeyHolding(String key) {
        if (listener != null) {
            listener.onKeyHolding(key);
        }
    }

    @Override
    public void onKeyUp(String key) {
        if (listener != null) {
            listener.onKeyUp(key);
        }
    }

    @Override
    public void setListenKeys(List<String> keys) {
        this.listenKeys = keys;
    }

    @Override
    public List<String> getListenKeys() {
        return listenKeys;
    }

    @Override
    public void setLastKeyStatus(String key, boolean isKeyDown) {
        lastKeyStatusMap.put(key, isKeyDown);
    }

    @Override
    public boolean getLastKeyStatus(String key) {
        try {
            return lastKeyStatusMap.get(key);
        } catch (NullPointerException e) {
            return false;
        }
    }

    @Override
    public void setLastMouseStatus(int type, boolean flag) {
        assert type != 0;

        int mask = Integer.MAX_VALUE & type;
        if (flag) {
            // set flag
            mouseStatus = mouseStatus | mask;
        } else {
            // unset flag
            mouseStatus = mouseStatus & (~mask);
        }
    }

    @Override
    public boolean getLastMouseStatus(int type) {
        assert type != 0;

        int mask = Integer.MAX_VALUE & type;
        return (mouseStatus & mask) != 0;
    }

    /**
     * 隣接するオブジェクトを返す。存在しない場合は、空のListを返す。
     *
     * @param cls
     * @return
     */
    protected List getIntersectingObjects(java.lang.Class cls) {
        List objs = super.getIntersectingObjects(cls);
        if (objs == null) {
            return new ArrayList();
        }
        return objs;
    }

    @Override
    protected boolean intersects(Actor other) {
        if (!super.intersects(other)) {
            return false;
        }

        // 角度調整済みのシルエット
        BufferedImage img, otherImg;
        // thisを基準としたときのotherのoffset
        int offsetX, offsetY;
        // (thisの左上の位置を基点座標とした)thisとotherが重なっている範囲 
        /*
         *  +----------------+               +----------------+
         *  |(0,0)    this   |               |        other   |
         *  |    +-----------+--+       +----+----------------+----+
         *  |    |(x1,x2)    |  |       |    |(x1,x2)         |    |
         *  |    |    (x2,y2)|  |       |    |         (x2,y2)|    |
         *  +----+-----------+  |       |    +----------------+    |
         *       |     other    |       |          this            |
         *       +--------------+       |--------------------------+
         */
        int x1, y1; // upper-left
        int x2, y2; // lowwer-right

        // シルエットを準備
        {
            GreenfootImage gimg = new GreenfootImage(silhouette);
            gimg.rotate(getRotation());
            img = gimg.getAwtImage();

            GreenfootImage otherGImg = new GreenfootImage(((BaseActor) other).getSilhouette());
            otherGImg.rotate(other.getRotation());
            otherImg = otherGImg.getAwtImage();
        }

        offsetX = (other.getX() - otherImg.getWidth() / 2) - (getX() - img.getWidth() / 2);
        offsetY = (other.getY() - otherImg.getWidth() / 2) - (getY() - img.getWidth() / 2);

        x1 = Math.max(0, offsetX);
        y1 = Math.max(0, offsetY);
        x2 = Math.min(img.getWidth(), offsetX + otherImg.getWidth());
        y2 = Math.min(img.getHeight(), offsetY + otherImg.getHeight());

        for (int y = y1; y < y2; y++) {
            for (int x = x1; x < x2; x++) {
                // 不透明な部分が重なっていたら、隣接していると判定
                if (otherImg.getRGB(x - offsetX, y - offsetY) != 0
                        && img.getRGB(x, y) != 0) {
                    return true;
                }
            }
        }
        return false;
    }

    final public int getMaxHp() {
        return maxHp;
    }

    final public int getHp() {
        return hp;
    }

    public ActorStatus getActorStatus() {
        return actorStatus;
    }

    public boolean isFightable() {
        switch (actorStatus) {
            case DIED:
            case REMOVED:
                return false;
        }
        return true;
    }

    /**
     * 攻撃能力を返す
     *
     * @param defender 攻撃対象
     * @return 攻撃能力
     */
    public int getAttackAbility(BaseActor defender) {
        return ATTACK_ABILITY;
    }

    /**
     * 防御能力を返す。この値が大きいほど、攻撃のダメージが軽減される。防御力が上回る場合はノーダメージとする。HPが回復することは無い。
     *
     * @param attacker
     * @return 防御能力
     */
    public int getDefensiveAbility(BaseActor attacker) {
        return DEFENSIVE_ABILITY;
    }

    /**
     * 対戦を実行する。対戦不能なら何もしない。
     *
     * @param damage
     */
    public void fight(Damage damage) {
        if (!isFightable()) {
            return;
        }

        if (damage.getAttacker() == this) {
            hp -= damage.getAttackerDamage();
        } else if (damage.getDefender() == this) {
            hp -= damage.getDefenderDamage();
        }
        hp = Math.max(0, hp);

        // ステータスを更新
        if (hp == 0) {
            actorStatus = ActorStatus.DIED;
            onDied();
        }
    }

    @Override
    public void setImage(GreenfootImage img) {
        super.setImage(img);

        if (imageCache == img) {
            // キャッシュを利用
            silhouette = silhouetteCache;
            return;
        } else {
            // シルエットを作成する
            silhouette = new GreenfootImage(img);
            BufferedImage awtimg = silhouette.getAwtImage();
            Raster raster = awtimg.getAlphaRaster();

            for (int x = 0; x < img.getWidth(); x++) {
                for (int y = 0; y < img.getHeight(); y++) {
                    int[] arr = new int[1];
                    if (raster.getPixel(x, y, arr)[0] != 0) {
                        // 不透明な部分は黒で塗り潰す
                        awtimg.setRGB(x, y, 0xFF000000);
                    }
                }
            }

            // キャッシュを更新
            imageCache = img;
            silhouetteCache = silhouette;
        }
    }

    protected GreenfootImage getSilhouette() {
        return silhouette;
    }
}
