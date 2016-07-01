
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
        // * -> Cursor
        {Wall.class, Cursor.class},
        {Block.class, Cursor.class},
        {Goal.class, Cursor.class},};

    public Damage(BaseActor attacker, BaseActor defender) {
        this.attacker = attacker;
        this.defender = defender;

        calcDamage();
    }

    /**
     * 攻撃が成立するかを返す
     *
     * @return
     */
    private boolean isFightable() {
        for (Class[] pattern : fightPattern) {
            Class attackerClass = pattern[0], defenderClass = pattern[1];
            if (attackerClass.isInstance(attacker) && defenderClass.isInstance(defender)) {
                return true;
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
