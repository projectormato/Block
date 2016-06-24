
import greenfoot.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * 全てのWorldクラスの親クラス
 */
public class BaseWorld extends World implements EventHandler {

    final static boolean ENABLE_LOGGING = false;
    final static String LOG_FILE = "../blockGame.log";

    protected Logger logger;

    private List<String> listenKeys = new ArrayList<>();
    private Map<String, Boolean> lastKeyStatusMap = new HashMap<>();

    public BaseWorld() {
        // Create a new world with 1200x800 cells with a cell size of 1x1 pixels.
        super(1200, 800, 1);
    }

    /**
     * 管理しているActorとWorldに対してイベントを通知する。
     * このメソッドはオーバーライドできません。サブクラスは、適切なイベントハンドラメソッドをオーバーライドして、目的とする機能を実装してください。
     */
    @Override
    final public void act() {
        MouseInfo mouse = Greenfoot.getMouseInfo();

        // マウスイベントを送信
        if (mouse != null) {
            List<EventHandler> handlers = new ArrayList<>();
            BaseActor frontActor;

            // 最前面のオブジェクトは真っ先に処理
            frontActor = (BaseActor) mouse.getActor();
            if (frontActor != null) {
                handlers.add(frontActor);
            }

            // それ以外のオブジェクトは適当な順番で処理
            List actors = getObjectsAt(mouse.getX(), mouse.getY(), BaseActor.class);
            if (actors != null) {
                actors.remove(frontActor);
                handlers.addAll(handlers);
            }

            // 一番最後にWorldにメッセージを送る
            handlers.add(this);

            // メッセージ送信
            for (EventHandler handler : handlers) {
                assert handler != null;
                dispatchMouseEventHandler(handler, mouse, handler == frontActor);
            }
        }

        // キーイベントを送信
        for (Object handler : getObjects(null)) {
            dispatchKeyEventHandler((EventHandler) handler);
        }
        dispatchKeyEventHandler(this);
    }

    /**
     * 指定した一つのハンドラに対して、イベントの処理を委託する。
     *
     * @param handler
     * @param mouse
     * @param isFront
     */
    private void dispatchMouseEventHandler(EventHandler handler, MouseInfo mouse, boolean isFront) {
        assert handler != null;

        if (mouse != null) {
            if (handler instanceof BaseActor) {
                // objに重なっているか？
                handler.onMouseIn(mouse);
                handler.onMouseOut(mouse);
            }

            // 重なっているかにかかわらず
            handler.onMouseDown(mouse);
            handler.onMouseUp(mouse);
            handler.onMouseClicked(mouse);

            handler.onMouseMoved(mouse);
            handler.onMouseDragged(mouse);
            handler.onMouseDragging(mouse);
        }
    }

    private void dispatchKeyEventHandler(EventHandler handler) {
        for (String key : handler.getListenKeys()) {
            boolean status = Greenfoot.isKeyDown(key);
            boolean lastStatus = handler.getLastKeyStatus(key);

            if (lastStatus == status) {
                handler.onKeyHolding(key);
            } else if (lastStatus == false) {
                // false -> true
                handler.onKeyDown(key);
            } else {
                // true -> false
                handler.onKeyUp(key);
            }

            handler.setLastKeyStatus(key, status);
        }
    }

    @Override
    public void addObject(Actor actor, int x, int y) {
        assert actor != null;
        assert logger != null;
        assert actor instanceof BaseActor;

        ((BaseActor) actor).setLogger(logger);
        super.addObject(actor, x, y);
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
}
