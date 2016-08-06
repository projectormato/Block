
/**
 * PlayWorldのステータス
 *
 * @author yuuki0xff
 */
public enum PlayWorldStatus {
    /**
     * ステージの概要をMessageBoxで表示している状態
     */
    STAGE_START_MSG,
    /**
     * マウスのクリックを待機している状態。クリックするとボールの発射をプレイを開始する。
     */
    WAITING,
    /**
     * ゲームをプレイ中
     */
    PLAYING,
    /**
     * ゲーム終了後のメッセージや結果表示
     */
    STAGE_END_MSG,
    /**
     * アニメーションの終了を待機中。次の状態に遷移するときは、必ずこの状態を経由して遷移する。
     */
    ANIMATION_WAIT,
}
