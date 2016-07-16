
import greenfoot.*;

public class PlayWorld extends BaseWorld {

    private PlayWorldStatus worldStatus;
    private String stageName;
    private GreenfootSound bgm;
    private MessageBox msgbox;
    private BlinkMessageBox click2startMsgbox;
    private Overlay overlay; // click2startMsgboxの背景

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

    public void win() {
        setWorldStatus(PlayWorldStatus.STAGE_END_MSG);

        if (stageName != null) {
            MessageBox msgbox = new MessageBox(stageName + "-win", getWidth(), getHeight() / 3);
            msgbox.addListner(new EventListener() {
                @Override
                public void onMouseClicked(MouseInfo mouse) {
                    changeWorld("next");
                }
            });
            addObject(msgbox, getWidth() / 2, getHeight() / 2);
        } else {
            // TODO: 遷移先のステージをサブクラスから指定できるようにする
            changeWorld("next");
        }
    }

    public void lose() {
        setWorldStatus(PlayWorldStatus.STAGE_END_MSG);

        if (stageName != null) {
            MessageBox msgbox = new MessageBox(stageName + "-lose", getWidth(), getHeight() / 3);
            msgbox.addListner(new EventListener() {
                @Override
                public void onMouseClicked(MouseInfo mouse) {
                    changeWorld("replay");
                }
            });
            addObject(msgbox, getWidth() / 2, getHeight() / 2);
        } else {
            changeWorld("title");
        }
    }

    public PlayWorldStatus getWorldStatus() {
        return worldStatus;
    }

    public void setWorldStatus(PlayWorldStatus worldStatus) {
        this.worldStatus = worldStatus;
        onChangeStatus();
    }

    public void onChangeStatus() {
        switch (getWorldStatus()) {
            case STAGE_END_MSG:
                if (bgm != null) {
                    bgm.stop();
                    bgm = null;
                }
                break;
            case WAITING:
                removeObject(msgbox);
                // overlayはステージ開始時のメッセージの背景を流用
                addObject(click2startMsgbox, getWidth() / 2, getHeight() / 2);
                break;
            case PLAYING:
                removeObject(overlay);
                removeObject(click2startMsgbox);
                overlay = null;
                click2startMsgbox = null;
                break;
        }
    }

    @Override
    public void addDisabledObject(Actor actor, int x, int y) {
        super.addDisabledObject(actor, x, y);
    }
}
