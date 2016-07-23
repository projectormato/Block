
import greenfoot.*;

public class PlayWorld extends BaseWorld {

    private PlayWorldStatus worldStatus;
    private String stageName;
    private GreenfootSound bgm;
    private MessageBox msgbox;
    private BlinkMessageBox click2startMsgbox;
    private Overlay overlay; // click2startMsgboxの背景

    private boolean isWin = false;
    private PlayWorldStatus nextWorldStatus = null;

    public PlayWorld() {
        worldStatus = PlayWorldStatus.WAITING;
        if (Config.getBoolean("enableBGM")) {
            bgm = Config.getSound(this.getClass(), "bgm");
            bgm.playLoop();
        }
        prepare();
    }

    public PlayWorld(String stageName) {
        this();
        this.stageName = stageName;
        worldStatus = PlayWorldStatus.STAGE_START_MSG;

        //ステージ開始時のメッセージ
        msgbox = new MessageBox(stageName + "-start", getWidth(), getHeight() / 3);
        msgbox.addListner(new EventListener() {
            @Override
            public void onMouseClicked(MouseInfo mouse) {
                setWorldStatus(PlayWorldStatus.WAITING);
                removeObject(msgbox);
            }
        });
        addObject(msgbox, getWidth() / 2, getHeight() / 2);

        // メッセージボックスとclick2startの背景
        overlay = new Overlay(getWidth(), getHeight(), Config.OVERLAY_COLOR);
        addObject(overlay, getWidth() / 2, getHeight() / 2);

        // ゲーム開始待機状態に表示させるメッセージ
        click2startMsgbox = new BlinkMessageBox("click-to-start",
                Config.BLINK_MSGBOX_WIDTH, Config.BLINK_MSGBOX_HEIGHT);
        click2startMsgbox.setColor(Config.BLINK_MSGBOX_FONT_COLOR, Config.BLINK_MSGBOX_BG_COLOR);
        click2startMsgbox.setFont(Config.BLINK_MSGBOX_FONT);
        click2startMsgbox.createImageCache();

        // クリックしたらゲームスタート
        EventListener listener = new EventListener() {
            @Override
            public void onMouseClicked(MouseInfo mouse) {
                switch (getWorldStatus()) {
                    case STAGE_START_MSG:
                        setWorldStatus(PlayWorldStatus.WAITING);
                        break;
                    case WAITING:
                        setWorldStatus(PlayWorldStatus.PLAYING);
                        break;
                }
            }
        };
        overlay.addListner(listener);
        click2startMsgbox.addListner(listener);
    }

    private void prepare() {
    }

    @Override
    public void tick() {
        super.tick();
        // 全てのアニメーションが終了していれば、次の状態に遷移
        if (getWorldStatus() == PlayWorldStatus.ANIMATION_WAIT && isAnimationEnded()) {
            changeNextWorldStatus();
        }
    }

    public void win() {
        isWin = true;
        setWorldStatus(PlayWorldStatus.STAGE_END_MSG);
    }

    public void lose() {
        isWin = false;
        setWorldStatus(PlayWorldStatus.STAGE_END_MSG);
    }

    public PlayWorldStatus getWorldStatus() {
        return worldStatus;
    }

    public void setWorldStatus(PlayWorldStatus worldStatus) {
        if (preChangeStatus(worldStatus)) {
            // ANIMATION_WAITを経由して
            this.worldStatus = PlayWorldStatus.ANIMATION_WAIT;
            nextWorldStatus = worldStatus;
            onChangeStatus();
        }
    }

    /**
     * ANIMATION_WAITから、次の状態へ遷移する
     */
    private void changeNextWorldStatus() {
        assert worldStatus == PlayWorldStatus.ANIMATION_WAIT;
        worldStatus = nextWorldStatus;
        nextWorldStatus = null;
        onChangeStatus();
    }

    /**
     * ステータスが切り替わる直前に実行される。ステータスの切り替えを許可するか否かを判定し、返す
     *
     * @param newStatus 切替後のステータス
     * @return ステータスを切り替え可能ならtrue、切り替え不能ならfalse
     */
    public boolean preChangeStatus(PlayWorldStatus newStatus) {
        if (getWorldStatus() == PlayWorldStatus.WAITING && newStatus == PlayWorldStatus.PLAYING) {
            // カーソルとブロックが衝突している状態なら、プレイを開始しない
            for (Object cursor : getObjects(Cursor.class)) {
                if (((BaseActor) cursor).getIntersectingObjects(Block.class).size() > 0) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * ステータスを切り替えた直後に実行される。
     */
    public void onChangeStatus() {
        switch (getWorldStatus()) {
            case WAITING:
                removeObject(msgbox);
                // overlayはステージ開始時のメッセージの背景を流用
                addObject(click2startMsgbox, getWidth() / 2, getHeight() / 2);
                break;
            case PLAYING:
                removeObject(overlay);
                removeObject(click2startMsgbox);
                // overlayはステージ終了時にも使うので、残しておく
                click2startMsgbox = null;
                break;
            case STAGE_END_MSG:
                if (bgm != null) {
                    bgm.stop();
                    bgm = null;
                }

                String msgPrefix = isWin ? "-win" : "-lose";
                String nextWorld = isWin ? "next" : "replay";
                String defaultNextWorld = isWin ? "next" : "title";

                // ステージ終了時のメッセージを表示
                if (stageName != null) {
                    msgbox = new MessageBox(stageName + msgPrefix, getWidth(), getHeight() / 3);
                    EventListener listener = new EventListener() {
                        @Override
                        public void onMouseClicked(MouseInfo mouse) {
                            changeWorld(nextWorld);
                        }
                    };
                    msgbox.addListner(listener);
                    overlay.addListner(listener);
                    addObject(msgbox, getWidth() / 2, getHeight() / 2);
                    addObject(overlay, getWidth() / 2, getHeight() / 2);
                } else {
                    changeWorld(nextWorld);
                }
                break;
        }
    }

    @Override
    public void addDisabledObject(Actor actor, int x, int y) {
        super.addDisabledObject(actor, x, y);
    }

    /**
     * 全てのアニメーションが終了しているかを返す
     *
     * @return アニメーションが終了していたらtrue
     */
    private boolean isAnimationEnded() {
        for (Object actor : getObjects(AnimationActor.class)) {
            if (actor instanceof NoWaitActor == false) {
                // アニメーションは終了していない
                return false;
            }
        }
        return true;
    }
}
