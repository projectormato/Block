
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * Write a description of class Ball here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Ball extends BaseActor {

    int speed = 3;
    // 現在触れているActors
    LinkedList<Set<BaseActor>> touchingActorsList = new LinkedList<>();
    final int historyCount = 5; // 何故かballをturnした直後はイベントが発生しない問題を回避するためです。
    boolean isActorsListInitialized = false;

    // 次に進む方向を示すベクトル
    // ただし、両方ともに0であれば方向を変えずに進む。
    double nextTurnVectorX, nextTurnVectorY;

    public Ball() {
        for (int i = 0; i < historyCount; i++) {
            touchingActorsList.add(new HashSet<>());
        }
    }

    @Override
    public void tick() {
        if (getActorStatus() != ActorStatus.ALIVE) {
            return;
        }

        super.tick();

        if (nextTurnVectorY != 0 || nextTurnVectorX != 0) {
            // ベクトルの方向に向きを変える
            setRotation((int) Math.toDegrees(Math.atan2(nextTurnVectorY, nextTurnVectorX)));
        }

        // リストを回転させる
        touchingActorsList.addFirst(touchingActorsList.removeLast());
        // 次のターンに備えて、初期化する
        touchingActorsList.getFirst().clear();
        nextTurnVectorX = 0;
        nextTurnVectorY = 0;

        move(speed);

        if (isAtEdge()) {
            ((PlayWorld) getWorld()).lose();
            return;
        }
    }

    @Override
    public int getAttackAbility(BaseActor defender) {
        if (defender instanceof CursorBarrier) {
            return 0;
        }
        return super.getAttackAbility(defender);
    }

    @Override
    public void fight(Damage damage) {
        super.fight(damage);

        // Ballは常に攻撃側
        BaseActor defender = (BaseActor) damage.getDefender();
        if (defender instanceof Goal) {
            ((PlayWorld) getWorld()).win();
        } else {
            if (!isActorsListInitialized) {
                isActorsListInitialized = true;
                // 初回は、CursorBarrierとの衝突判定を行わない
                for (Object barrier : getWorld().getObjects(CursorBarrier.class)) {
                    touchingActorsList.getFirst().add((CursorBarrier) barrier);
                }
                return;
            }

            touchingActorsList.getFirst().add(defender);
            // オブジェクトの内部にめり込んでいた場合は、何度もfight()を呼び出されることがある。
            // その場合、最初の一回だけを実行し、それ以降はballから離れるまで無視する。
            for (Set<BaseActor> touchingActors : touchingActorsList) {
                // 0番目の要素は無視
                if (touchingActorsList.getFirst() == touchingActors) {
                    continue;
                }

                if (touchingActors.contains(defender)) {
                    return;
                }
            }
            //  BallからDefenderへのベクトル
            double dx, dy;
            if (defender instanceof CursorBarrier) {
                // カーソルのバリアに衝突したときの反射角を調節するため、dx, dyを距離を調節する

                /*
                バリアの画像について
                画像のサイズ: 71, 179
                曲率半径: 78
                画像の中心から、曲率円の中心へのオフセット: -69, -2
                 */
                double rotateAngle = Math.toRadians(defender.getRotation());
                // 現在のの角度ににおける、曲率園のオフセットを求める
                double curvatureDx = -69 * Math.cos(rotateAngle) - (-2) * Math.sin(rotateAngle);
                double curvatureDy = -69 * Math.sin(rotateAngle) + (-2) * Math.cos(rotateAngle);
                // ballから曲率円の中心へのオフセット
                dx = (defender.getX() + curvatureDx) - getX();
                dy = (defender.getY() + curvatureDy) - getY();
            } else {
                dx = defender.getX() - getX();
                dy = defender.getY() - getY();
            }

            // BallからDefenderの中心への距離
            double len = Math.sqrt(dx * dx + dy * dy);

            // Vector F
            // Ballの運動方向と平行な単位ベクトル
            double vectorFx, vectorFy;
            vectorFx = Math.cos(Math.toRadians(getRotation()));
            vectorFy = Math.sin(Math.toRadians(getRotation()));

            // Vector N
            // 衝突面に対する法線ベクトルの単位ベクトル
            // 便宜上、法線はBallの中心とDefenderの中心を結んだ直線とする
            double vectorNx, vectorNy;
            vectorNx = -dx / len;
            vectorNy = -dy / len;

            // 衝突面に平行なベクトルを求める際に使用する係数
            // a = -F * N
            // ベクトル(F + a * N)は衝突面に並行
            double a;
            a = (-vectorFx) * vectorNx + (-vectorFy) * vectorNy;

            // 反射ベクトル
            // R = F + 2 * a * N
            double vectorRx, vectorRy;
            vectorRx = vectorFx + 2 * a * vectorNx;
            vectorRy = vectorFy + 2 * a * vectorNy;

            nextTurnVectorX += vectorRx;
            nextTurnVectorY += vectorRy;
        }
    }
}
