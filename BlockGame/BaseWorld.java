
import greenfoot.*;
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
        MouseInfo mouse = Greenfoot.getMouseInfo();
        BaseActor frontActor = (BaseActor) mouse.getActor();

        if (frontActor != null) {
            dispatchEventHandler((EventHandler) frontActor, mouse, true);
        }
        for (Actor backActor : getObjectsAt(mouse.getX(), mouse.getY(), BaseActor.class)) {
            if (frontActor == backActor) {
                continue;
            }
            dispatchEventHandler((EventHandler) backActor, mouse, false);
        }
        dispatchEventHandler((EventHandler) this, mouse, frontActor == null);
    }

    private void dispatchEventHandler(EventHandler handler, MouseInfo mouse, boolean isFront) {
        assert handler != null;
        assert mouse != null;

        if (handler instanceof BaseActor) {
            // objに重なっているか？
            handler.onMouseIn();
            handler.onMouseOut();
        }

        // 重なっているかにかかわらず
        handler.onMouseDown();
        handler.onMouseUp();
        handler.onMouseClicked();

        handler.onMouseMoved();
        handler.onMouseDragged();
        handler.onMouseDragging();
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
    public void onMouseIn() {
    }

    @Override
    public void onMouseOut() {
    }

    @Override
    public void onMouseMoved() {
    }

    @Override
    public void onMouseDown() {
    }

    @Override
    public void onMouseHolding() {
    }

    @Override
    public void onMouseUp() {
    }

    @Override
    public void onMouseClicked() {
    }

    @Override
    public void onMouseDragging() {
    }

    @Override
    public void onMouseDragged() {
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
