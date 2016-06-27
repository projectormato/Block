
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
    private int mouseStatus;
    private EventListener listener;

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
        List<EventHandler> handlers = new ArrayList<>();
        BaseActor frontActor = null;

        // 最前面のオブジェクトは真っ先に処理
        if (mouse != null) {
            frontActor = (BaseActor) mouse.getActor();
            if (frontActor != null) {
                handlers.add(frontActor);
            }

        }

        // それ以外のオブジェクトは適当な順番で処理
        List actors = getObjects(BaseActor.class);
        if (actors != null) {
            if (frontActor != null) {
                actors.remove(frontActor);
            }
            handlers.addAll(actors);
        }

        // 一番最後にWorldにメッセージを送る
        handlers.add(this);

        // マウスイベントを送信
        dispatchMouseEventHandler(handlers, mouse);

        // キーイベントを送信
        for (Object handler : getObjects(null)) {
            dispatchKeyEventHandler((EventHandler) handler);
        }
        dispatchKeyEventHandler(this);

        // actorとworldへtickイベントを送信
        for (Object handler : getObjects(BaseActor.class)) {
            ((BaseActor) handler).tick();
        }
        tick();
    }

    private void dispatchMouseEventHandler(List<EventHandler> handlers, MouseInfo mouse) {
        List objs = null;
        if (mouse != null) {
            objs = getObjectsAt(mouse.getX(), mouse.getY(), null);
        }

        for (EventHandler handler : handlers) {
            boolean isHovered = handler.getLastMouseStatus(EventHandler.MOUSE_HOVERED);
            boolean isHovering = false;

            if (handler instanceof Actor) {
                if (objs != null) {
                    isHovering = objs.contains(handler);
                }
            } else if (handler instanceof World) {
                isHovering = mouse != null;
            } else {
                throw new AssertionError();
            }

            // onMouseIn, onMouseOut
            if (isHovering && !isHovered) {
                System.out.println(handler + ": mouse in");
                handler.onMouseIn(mouse);
            } else if (!isHovering && isHovered) {
                System.out.println(handler + ": mouse out");
                handler.onMouseOut(mouse);
            }
            handler.setLastMouseStatus(EventHandler.MOUSE_HOVERED, isHovering);

            // onMouseMoved
            if (mouse != null) {
                handler.onMouseMoved(mouse);
            }

            boolean isPressed = handler.getLastMouseStatus(EventHandler.MOUSE_PRESSED);
            boolean isPressing = isPressed;
            if (Greenfoot.mousePressed(handler)) {
                isPressing = true;
            } else if (Greenfoot.mouseClicked(handler) || Greenfoot.mouseDragEnded(handler)) {
                isPressing = false;
            }

            // onMouseDown, onMouseHolding, onMouseUp, onMouseClicked
            if (isPressing && !isPressed) {
                System.out.println(handler + ": mouse down");
                handler.onMouseDown(mouse);
            } else if (isPressing && isPressed) {
                System.out.println(handler + ": mouse holding");
                handler.onMouseHolding(mouse);
            } else if (!isPressing && isPressed) {
                System.out.println(handler + ": mouse up");
                handler.onMouseUp(mouse);

                if (Greenfoot.mouseClicked(handler) && !Greenfoot.mouseDragEnded(handler)) {
                    System.out.println(handler + ": mouse clicked");
                    handler.onMouseClicked(mouse);
                }
            }
            handler.setLastMouseStatus(EventHandler.MOUSE_PRESSED, isPressing);

            boolean isDragged = handler.getLastMouseStatus(EventHandler.MOUSE_DRAGGED);
            boolean isDragging = isDragged;
            if (Greenfoot.mouseDragged(handler)) {
                isDragging = true;
            } else if (Greenfoot.mouseDragEnded(handler)) {
                isDragging = false;
            }

            // onMouseDragging, onMouseDragged
            if (isDragging && !isDragged) {
                System.out.println(handler + ": mouse drag started");
            } else if (isDragging && isDragged) {
                System.out.println(handler + ": mouse dragging");
                handler.onMouseDragging(mouse);
            } else if (!isDragging && isDragged) {
                System.out.println(handler + ": mouse drag ended");
                handler.onMouseDragged(mouse);
            }
            handler.setLastMouseStatus(EventHandler.MOUSE_DRAGGED, isDragging);

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
    public void addListner(EventListener listener) {
        this.listener = listener;
    }

    @Override
    public void tick() {
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
