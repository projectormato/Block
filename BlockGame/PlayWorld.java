
import greenfoot.*;

public class PlayWorld extends BaseWorld {

    private PlayWorldStatus worldStatus;
    private String stageName;
    private GreenfootSound bgm;

    public PlayWorld() {
        worldStatus = PlayWorldStatus.WAITING;
        if (Config.getBoolean("enableBGM")) {
            bgm = Config.getSound(this.getClass(), "bgm");
            bgm.play();
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

        MessageBox msgbox = new MessageBox(stageName + "-win", getWidth(), getHeight() / 3);
        msgbox.addListner(new EventListener() {
            @Override
            public void onMouseClicked(MouseInfo mouse) {
                // TODO: 遷移先のステージをサブクラスから指定できるようにする
                Greenfoot.setWorld(new StartWorld());
            }
        });
        addObject(msgbox, getWidth() / 2, getHeight() / 2);
    }

    public void lose() {
        setWorldStatus(PlayWorldStatus.STAGE_END_MSG);

        MessageBox msgbox = new MessageBox(stageName + "-lose", getWidth(), getHeight() / 3);
        msgbox.addListner(new EventListener() {
            @Override
            public void onMouseClicked(MouseInfo mouse) {
                // TODO: 遷移先のステージをサブクラスから指定できるようにする
                Greenfoot.setWorld(new StartWorld());
            }
        });
        addObject(msgbox, getWidth() / 2, getHeight() / 2);
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
        }
    }
}
