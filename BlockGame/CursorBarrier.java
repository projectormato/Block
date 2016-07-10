
public class CursorBarrier extends BaseActor {

    private int k = 5;

    public CursorBarrier(Ball ball) {
    }

    @Override
    public int getAttackAbility(BaseActor defender) {
        return 0;
    }

    @Override
    public void fight(Damage damage) {
        super.fight(damage);

        if (damage.getAttacker() instanceof Ball) {
            Ball ball = (Ball) damage.getAttacker();
            // ボールを反射させる
            double dx, dy;
            double dangle;          // バリアからみた時のボールの方角
            double incidenceAngle;  // 入射角
            double reflectionAngle; // 反射角

            // 裏側から衝突したボールには処理を行わない
            if (Math.abs(ball.getRotation() - getRotation()) <= 90) {
                return;
            }

            dx = ball.getX() - getX();
            dy = ball.getY() - getY();
            dangle = normalizeRelativeAngle(Math.toDegrees(Math.atan2(dy, dx)) - getRotation());

            incidenceAngle = 180 + ball.getRotation() - getRotation();
            reflectionAngle = -incidenceAngle;
            // 衝突地点に応じて、反射角を調整
            reflectionAngle += dangle / k;

            ball.setRotation(getRotation() + (int) reflectionAngle);
        }
    }

    /**
     * 角度(degree)を正規化する
     *
     * @param angle (degree)
     * @return -180 ~ 180 (degree)
     */
    private double normalizeRelativeAngle(double angle) {
        angle %= 360;
        if (angle < 0) {
            angle = 360 + angle;
        }
        if (180 < angle) {
            angle = -360 + angle;
        }
        return angle;
    }
}
