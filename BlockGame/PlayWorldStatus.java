
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
}
