
/**
 * BaseActorのステータス。
 *
 * @author yuuki0xff
 */
public enum ActorStatus {
    /**
     * 活性状態で、通常の操作が行える状態。
     */
    ALIVE,
    /**
     * オブジェクトが無効化された状態。衝突判定などは行わない。
     */
    DISABLED,
    /**
     * 死んだ状態。この状態になったら、必要な処理を終えた後REMOVED状態に遷移するべきである。
     */
    DIED,
    /**
     * 削除待機中の状態。この状態になったら、1フレーム以内にBaseActorクラスにより削除される。
     */
    REMOVED,
};
