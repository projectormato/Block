
import java.util.List;

final public class Damage {

    private BaseActor attacker, defender;
    private int attackerDamage, defenderDamage;

    /**
     * 攻撃が成立するパターン
     */
    // 表記ルール
    // {攻撃側, 防御側}
    // 攻撃側 -> 防御側
    final public Class[][] fightPattern = {
        // Ball -> *
        {Ball.class, Block.class},
        {Ball.class, Goal.class},
        {Ball.class, Wall.class},
        {Ball.class, Ball.class},
        {Ball.class, Cursor.class},
        {Ball.class, CursorBarrier.class},
        // * -> Cursor
        {Wall.class, Cursor.class},
        {Block.class, Cursor.class},
        {Goal.class, Cursor.class},};

    private Damage(BaseActor attacker, BaseActor defender) {
        this.attacker = attacker;
        this.defender = defender;

        calcDamage();
    }

    /**
     * 一つのattackerから、それぞれのdefenderへ攻撃するDamageオブジェクトを作成して返す。
     *
     * @param attacker
     * @param defenders
     * @return
     */
    private static Damage[] createDamages(BaseActor attacker, List<BaseActor> defenders) {
        Damage[] arr = new Damage[defenders.size()];
        for (int i = 0; i < defenders.size(); i++) {
            arr[i] = new Damage(attacker, defenders.get(i));
        }
        return arr;
    }

    /**
     * 複数のActorに対して一斉攻撃する。
     *
     * @param attacker
     * @param defenders
     */
    public static void fights(BaseActor attacker, List<BaseActor> defenders) {
        for (Damage damage : createDamages(attacker, defenders)) {
            BaseActor defender = damage.getDefender();

            // 対戦可能なパターンで、なおかつオブジェクトが隣接している場合
            if (damage.isFightable()) {
                attacker.fight(damage);
                defender.fight(damage);
            }
        }
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

        for (Class[] pattern : fightPattern) {
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
