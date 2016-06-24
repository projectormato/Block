
import greenfoot.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

/**
 * 全てのActorの親クラス
 */
public class BaseActor extends Actor implements EventHandler {

    protected Logger logger;
    private List<String> listenKeys = new ArrayList<>();
    private Map<String, Boolean> lastKeyStatusMap = new HashMap<>();

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
    public void onMouseIn(MouseInfo mouse) {
    }

    @Override
    public void onMouseOut(MouseInfo mouse) {
    }

    @Override
    public void onMouseMoved(MouseInfo mouse) {
    }

    @Override
    public void onMouseDown(MouseInfo mouse) {
    }

    @Override
    public void onMouseHolding(MouseInfo mouse) {
    }

    @Override
    public void onMouseUp(MouseInfo mouse) {
    }

    @Override
    public void onMouseClicked(MouseInfo mouse) {
    }

    @Override
    public void onMouseDragging(MouseInfo mouse) {
    }

    @Override
    public void onMouseDragged(MouseInfo mouse) {
    }

    @Override
    public void onKeyDown(String key) {
    }

    @Override
    public void onKeyHolding(String key) {
    }

    @Override
    public void onKeyUp(String key) {
    }

    @Override
    public void setListenKeys(List<String> keys) {
        Set<String> keySet = lastKeyStatusMap.keySet();
        for (String key : keys) {
            if (!keySet.contains(key)) {
                lastKeyStatusMap.put(key, false);
            }
        }
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
        return lastKeyStatusMap.get(key);
    }
}
