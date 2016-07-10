
import greenfoot.*;
import java.awt.Color;
import java.awt.Font;

public class PlayWorld extends BaseWorld {

    private PlayWorldStatus worldStatus;
    private String stageName;
    private GreenfootSound bgm;

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

        MessageBox msgbox = new MessageBox(stageName + "-start", getWidth(), getHeight() / 3);
        msgbox.addListner(new EventListener() {
            @Override
            public void onMouseClicked(MouseInfo mouse) {
                setWorldStatus(PlayWorldStatus.WAITING);
                removeObject(msgbox);
            }
        });
        addObject(msgbox, getWidth() / 2, getHeight() / 2);
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
                    // TODO: 遷移先のステージをサブクラスから指定できるようにする
                    Greenfoot.setWorld(new StartWorld());
                }
            });
            addObject(msgbox, getWidth() / 2, getHeight() / 2);
        } else {
            // TODO: 遷移先のステージをサブクラスから指定できるようにする
            Greenfoot.setWorld(new StartWorld());
        }
    }

    public void lose() {
        setWorldStatus(PlayWorldStatus.STAGE_END_MSG);

        if (stageName != null) {
            MessageBox msgbox = new MessageBox(stageName + "-lose", getWidth(), getHeight() / 3);
            msgbox.addListner(new EventListener() {
                @Override
                public void onMouseClicked(MouseInfo mouse) {
                    // TODO: 遷移先のステージをサブクラスから指定できるようにする
                    Greenfoot.setWorld(new StartWorld());
                }
            });
            addObject(msgbox, getWidth() / 2, getHeight() / 2);
        } else {
            // TODO: 遷移先のステージをサブクラスから指定できるようにする
            Greenfoot.setWorld(new StartWorld());
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
                BlinkMessageBox click2startMsgbox = new BlinkMessageBox("click-to-start", getWidth(), getHeight());
                click2startMsgbox.setColor(Color.WHITE, new Color(0x55000000, true));
                click2startMsgbox.setFont(new Font("SansSerif", Font.PLAIN, 30));
                // クリックしたらゲームスタート
                click2startMsgbox.addListner(new EventListener() {
                    @Override
                    public void onMouseClicked(MouseInfo mouse) {
                        switch (getWorldStatus()) {
                            case WAITING:
                                removeObject(click2startMsgbox);
                                setWorldStatus(PlayWorldStatus.PLAYING);
                                break;
                        }
                    }
                });
                addObject(click2startMsgbox, getWidth() / 2, getHeight() / 2);
                break;
        }
    }

    @Override
    public void addDisabledObject(Actor actor, int x, int y) {
        super.addDisabledObject(actor, x, y);
    }
}
