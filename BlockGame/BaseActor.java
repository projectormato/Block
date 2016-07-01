
import greenfoot.*;
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

    protected Logger logger;
    private List<String> listenKeys = new ArrayList<>();
    private Map<String, Boolean> lastKeyStatusMap = new HashMap<>();
    private int mouseStatus;
    private EventListener listener;
    private int maxHp;
    private int hp;
    private ActorStatus actorStatus = ActorStatus.ALIVE;

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
    public void onMouseIn(MouseInfo mouse) {
        if (listener != null) {
            listener.onMouseIn();
        }
    }

    @Override
    public void onMouseOut(MouseInfo mouse) {
        if (listener != null) {
            listener.onMouseOut();
        }
    }

    @Override
    public void onMouseMoved(MouseInfo mouse) {
        if (listener != null) {
            listener.onMouseMoved();
        }
    }

    @Override
    public void onMouseDown(MouseInfo mouse) {
        if (listener != null) {
            listener.onMouseDown();
        }
    }

    @Override
    public void onMouseHolding(MouseInfo mouse) {
        if (listener != null) {
            listener.onMouseHolding();
        }
    }

    @Override
    public void onMouseUp(MouseInfo mouse) {
        if (listener != null) {
            listener.onMouseUp();
        }
    }

    @Override
    public void onMouseClicked(MouseInfo mouse) {
        if (listener != null) {
            listener.onMouseClicked();
        }
    }

    @Override
    public void onMouseDragging(MouseInfo mouse) {
        if (listener != null) {
            listener.onMouseDragging();
        }
    }

    @Override
    public void onMouseDragged(MouseInfo mouse) {
        if (listener != null) {
            listener.onMouseDragged();
        }
    }

    @Override
    public void onKeyDown(String key) {
        if (listener != null) {
            listener.onKeyDown();
        }
    }

    @Override
    public void onKeyHolding(String key) {
        if (listener != null) {
            listener.onKeyHolding();
        }
    }

    @Override
    public void onKeyUp(String key) {
        if (listener != null) {
            listener.onKeyUp();
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

    final public int getMaxHp() {
        return maxHp;
    }

    final public int getHp() {
        return hp;
    }

    public ActorStatus getActorStatus() {
        return actorStatus;
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
     * 対戦を実行する
     *
     * @param damage
     */
    public void fight(Damage damage) {
        if (damage.getAttacker() == this) {
            hp -= damage.getAttackerDamage();
        } else if (damage.getDefender() == this) {
            hp -= damage.getDefenderDamage();
        }
        hp = Math.max(0, hp);

        // ステータスを更新
        if (hp == 0) {
            actorStatus = ActorStatus.DIED;
        }
    }
}
