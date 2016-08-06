
/**
 * エネルギー弾(Ball)を何度当てても壊れない最強の障害物。
 *
 * @author yuuki0xff
 */
public class Wall extends BaseActor {

    @Override
    public int getDefensiveAbility(BaseActor attacker) {
        return 1000;
    }
}
