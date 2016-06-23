
import greenfoot.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * 全てのWorldクラスの親クラス
 */
public class BaseWorld extends World implements EventHandler {

    final static boolean ENABLE_LOGGING = false;
    final static String LOG_FILE = "../blockGame.log";

    protected Logger logger;

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
        List<EventHandler> handlers = new ArrayList<>();
        MouseInfo mouse = Greenfoot.getMouseInfo();
        BaseActor frontActor = null;

        // 最前面のオブジェクトは真っ先に処理
        if (mouse != null) {
            frontActor = (BaseActor) mouse.getActor();
        }
        if (frontActor != null) {
            handlers.add(frontActor);
        }

        // それ以外のオブジェクトは適当な順番で処理
        if (mouse != null) {
            List actors = getObjectsAt(mouse.getX(), mouse.getY(), BaseActor.class);
            if (actors != null) {
                actors.remove(frontActor);
                handlers.addAll(handlers);
            }
        }

        // 一番最後にWorldにメッセージを送る
        handlers.add(this);

        // メッセージ送信
        for (EventHandler handler : handlers) {
            assert handler != null;
            // if(handler == null)continue;
            dispatchEventHandler(handler, mouse, handler == frontActor);
        }
    }

    /**
     * 指定した一つのハンドラに対して、イベントの処理を委託する。
     *
     * @param handler
     * @param mouse
     * @param isFront
     */
    private void dispatchEventHandler(EventHandler handler, MouseInfo mouse, boolean isFront) {
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

        // TODO: key周りのイベントを呼び出す
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
    public void onKeyDown() {
    }

    @Override
    public void onKeyHolding() {
    }

    @Override
    public void onKeyUp() {
    }

}
