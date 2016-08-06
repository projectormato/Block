
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Actorにダメージを与える。
 *
 * @author yuuki0xff
 */
final public class Damage {

    private BaseActor attacker, defender;
    private int attackerDamage, defenderDamage;

    /**
     * 攻撃が成立するパターン
     */
    // 表記ルール
    // {攻撃側, 防御側, (1)}
    // 攻撃側 -> 防御側
    //
    // (1): 攻撃側と防御側のうち、オブジェクト数が少ない方のクラス。判定処理の軽量化のために使用する
    final static public Class[][] FIGHT_PATTERNS = {
        // Ball -> *
        {Ball.class, Block.class, Ball.class},
        {Ball.class, Goal.class, Ball.class},
        {Ball.class, Wall.class, Ball.class},
        {Ball.class, Ball.class, Ball.class},
        {Ball.class, Cursor.class, Ball.class},
        {Ball.class, CursorBarrier.class, Ball.class},
        // * -> Cursor
        {Wall.class, Cursor.class, Cursor.class},
        {Block.class, Cursor.class, Cursor.class},
        {Goal.class, Cursor.class, Cursor.class},};

    /**
     * 攻撃が成立するかチェックするクラス。オブジェクト数が多いクラスを処理対象に含めないようにすると、処理負荷軽減が出来る。
     */
    final static private Set<Class> CHECK_CLASSES;

    static {
        CHECK_CLASSES = new HashSet<>();
        for (Class[] tuple : FIGHT_PATTERNS) {
            CHECK_CLASSES.add(tuple[2]);
        }
    }

    private Damage(BaseActor attacker, BaseActor defender) {
        this.attacker = attacker;
        this.defender = defender;

        calcDamage();
    }

    /**
     * 複数のActorに対して一斉攻撃する。
     *
     * @param attacker
     * @param defenders
     */
    private static void fights(BaseActor attacker, List<BaseActor> defenders) {
        for (BaseActor defender : defenders) {
            (new Damage(attacker, defender)).fight();
        }
    }

    /**
     * 複数のActorに対して一斉攻撃する。
     *
     * @param attackers
     * @param defenders
     */
    private static void fights(List<BaseActor> attackers, BaseActor defenders) {
        for (BaseActor attacker : attackers) {
            (new Damage(attacker, defenders)).fight();
        }
    }

    /**
     * 指定したオブジェクトの中で、対戦を実行する
     *
     * @param actors
     */
    public static void fightsAll(List<BaseActor> actors) {
        Map<Class, List<BaseActor>> filteredActorListCache = new HashMap<>();

        for (BaseActor actor : actors) {
            if (!CHECK_CLASSES.contains(actor.getClass())) {
                continue;
            }

            // マッチする対戦可能条件に従って対戦を行う
            for (Class[] pattern : FIGHT_PATTERNS) {
                Class attackerClass = pattern[0];
                Class defenderClass = pattern[1];

                if (attackerClass.isInstance(actor)) {
                    // 攻撃者なら
                    List<BaseActor> defenders = getFilteredActorList(filteredActorListCache, actors, defenderClass);
                    fights(actor, defenders);
                } else if (defenderClass.isInstance(actor)) {
                    // 防衛者なら
                    List<BaseActor> attackers = getFilteredActorList(filteredActorListCache, actors, attackerClass);
                    fights(attackers, actor);
                }
            }
        }
    }

    /**
     * actorsの中から、clsのインスタンスだけを返す。この結果はキャッシュを利用する
     *
     * @param cache フィルタリングした結果のキャッシュ
     * @param actors 絞りこみ対象のActor
     * @param cls 絞り込み条件
     * @return clsのインスタンスのリスト
     */
    private static List<BaseActor> getFilteredActorList(
            Map<Class, List<BaseActor>> cache, List<BaseActor> actors, Class cls) {
        // キャッシュにヒットしたら逸れを利用
        List<BaseActor> filtered = cache.get(cls);
        if (filtered != null) {
            return filtered;
        }

        // キャッシュミスしたら、フィルタリングをして、その結果をキャッシュする
        filtered = new ArrayList<>();
        for (BaseActor actor : actors) {
            if (cls.isInstance(actor)) {
                filtered.add(actor);
            }
        }
        cache.put(cls, filtered);
        return filtered;
    }

    /**
     * 攻撃が成立するかを返す
     *
     * @return
     */
    private boolean isFightable() {
        if (attacker == defender
                || attacker.getActorStatus() != ActorStatus.ALIVE
                || defender.getActorStatus() != ActorStatus.ALIVE) {
            return false;
        }

        for (Class[] pattern : FIGHT_PATTERNS) {
            Class attackerClass = pattern[0], defenderClass = pattern[1];
            if (attackerClass.isInstance(attacker) && defenderClass.isInstance(defender)) {
                // 攻撃可能なパターンに一致
                // 互いが隣接していれば攻撃成立
                // 互いに隣接するオブジェクトが多数存在するステージでの処理負荷軽減のため、ここで隣接するか判定している。
                if (attacker.intersects(defender)) {
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    /**
     * ダメージを計算する
     */
    private void calcDamage() {
        // 対戦不能なら処理を中断
        if (!isFightable()) {
            return;
        }

        attackerDamage = 0;
        defenderDamage = attacker.getAttackAbility(defender) - defender.getDefensiveAbility(attacker);
        defenderDamage = Math.max(0, defenderDamage);
    }

    /**
     * 対戦可能なら対戦を行う
     */
    private void fight() {
        if (isFightable()) {
            attacker.fight(this);
            defender.fight(this);
        }
    }

    public BaseActor getAttacker() {
        return attacker;
    }

    public BaseActor getDefender() {
        return defender;
    }

    public int getAttackerDamage() {
        return attackerDamage;
    }

    public int getDefenderDamage() {
        return defenderDamage;
    }
}
