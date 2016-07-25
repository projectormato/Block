
/**
 * 耐久性の高いBlock。通常のボールを何度も当てないと消滅しない。
 *
 * @author yuuki
 */
public class StrongBlock extends Block {

    /**
     * n回当てないと壊れないブロック
     *
     * @param n 当てる回数
     */
    public StrongBlock(int n) {
        setMaxHp(n * 100);
        setHp(n * 100);
    }

    /**
     * 5回当てないと壊れないブロック
     */
    public StrongBlock() {
        this(5);
    }

    @Override
    public void fight(Damage damage) {
        super.fight(damage);
    }
}
