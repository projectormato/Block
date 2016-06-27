
import greenfoot.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * 全てのActorの親クラス
 */
public class BaseActor extends Actor implements EventHandler {

    protected Logger logger;
    private List<String> listenKeys = new ArrayList<>();
    private Map<String, Boolean> lastKeyStatusMap = new HashMap<>();
    private int mouseStatus;
    private EventListener listener;

    /**
     * 何もしない。このメソッドはオーバーライドできません。BaseWorldクラスから呼び出されるイベントハンドラ内で必要な動作を実装してください。
     */
    final public void act() {
    }

    protected void setImage() {
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void addListner(EventListener listener) {
        this.listener = listener;
    }

    @Override
    public void onMouseIn(MouseInfo mouse) {
        if (listener != null) {
            listener.onMouseIn();
        }
    }

    @Override
    public void onMouseOut(MouseInfo mouse) {
        if (listener != null) {
            listener.onMouseOut();
        }
    }

    @Override
    public void onMouseMoved(MouseInfo mouse) {
        if (listener != null) {
            listener.onMouseMoved();
        }
    }

    @Override
    public void onMouseDown(MouseInfo mouse) {
        if (listener != null) {
            listener.onMouseDown();
        }
    }

    @Override
    public void onMouseHolding(MouseInfo mouse) {
        if (listener != null) {
            listener.onMouseHolding();
        }
    }

    @Override
    public void onMouseUp(MouseInfo mouse) {
        if (listener != null) {
            listener.onMouseUp();
        }
    }

    @Override
    public void onMouseClicked(MouseInfo mouse) {
        if (listener != null) {
            listener.onMouseClicked();
        }
    }

    @Override
    public void onMouseDragging(MouseInfo mouse) {
        if (listener != null) {
            listener.onMouseDragging();
        }
    }

    @Override
    public void onMouseDragged(MouseInfo mouse) {
        if (listener != null) {
            listener.onMouseDragged();
        }
    }

    @Override
    public void onKeyDown(String key) {
        if (listener != null) {
            listener.onKeyDown();
        }
    }

    @Override
    public void onKeyHolding(String key) {
        if (listener != null) {
            listener.onKeyHolding();
        }
    }

    @Override
    public void onKeyUp(String key) {
        if (listener != null) {
            listener.onKeyUp();
        }
    }

    @Override
    public void setListenKeys(List<String> keys) {
        this.listenKeys = keys;
    }

    @Override
    public List<String> getListenKeys() {
        return listenKeys;
    }

    @Override
    public void setLastKeyStatus(String key, boolean isKeyDown) {
        lastKeyStatusMap.put(key, isKeyDown);
    }

    @Override
    public boolean getLastKeyStatus(String key) {
        try {
            return lastKeyStatusMap.get(key);
        } catch (NullPointerException e) {
            return false;
        }
    }

    @Override
    public void setLastMouseStatus(int type, boolean flag) {
        assert type != 0;

        int mask = Integer.MAX_VALUE & type;
        if (flag) {
            // set flag
            mouseStatus = mouseStatus | mask;
        } else {
            // unset flag
            mouseStatus = mouseStatus & (~mask);
        }
    }

    @Override
    public boolean getLastMouseStatus(int type) {
        assert type != 0;

        int mask = Integer.MAX_VALUE & type;
        return (mouseStatus & mask) != 0;
    }
}
