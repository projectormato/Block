
import greenfoot.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * GreenfootのWorldにイベントハンドラ等を実装したクラス。全てのWorldはこのクラスのサブクラスにすること。
 * イベントハンドラの呼び出し、及び背景画像の変更などを行う。
 *
 * @author yuuki0xff
 */
public abstract class BaseWorld extends World implements EventHandler {

    private List<String> listenKeys = new ArrayList<>();
    private Map<String, Boolean> lastKeyStatusMap = new HashMap<>();
    private int mouseStatus;
    private EventListener listener;

    public BaseWorld() {
        // Create a new world.
        super(Config.WORLD_WIDTH, Config.WORLD_HEIGHT, 1);
        setPaintOrder(MessageBox.class, Overlay.class, BaseActor.class);
        try {
            setBackground(Config.getImage(this, "bg"));
        } catch (IllegalArgumentException e) {
            // 無視
        }
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

        // 衝突判定をする。衝突していた者同士には、fightメソッドが実行される。
        List<BaseActor> aliveActors = getObjects(null, ActorStatus.ALIVE);
        Damage.fightsAll(aliveActors);

        // actorとworldへtickイベントを送信
        for (Object handler : getObjects(BaseActor.class)) {
            ((BaseActor) handler).tick();
        }
        tick();

        // 削除すべきオブジェクトを消す
        for (Object handler : getObjects(BaseActor.class)) {
            if (((BaseActor) handler).getActorStatus() == ActorStatus.REMOVED) {
                removeObject((BaseActor) handler);
            }
        }
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
                handler.onMouseIn(mouse);
            } else if (!isHovering && isHovered) {
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
                handler.onMouseDown(mouse);
            } else if (isPressing && isPressed) {
                handler.onMouseHolding(mouse);
            } else if (!isPressing && isPressed) {
                handler.onMouseUp(mouse);

                if (Greenfoot.mouseClicked(handler) && !Greenfoot.mouseDragEnded(handler)) {
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
            } else if (isDragging && isDragged) {
                handler.onMouseDragging(mouse);
            } else if (!isDragging && isDragged) {
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

    /**
     * 指定したステータスのActorを返す
     *
     * @param cls このクラスのインスタンス。nullなら全てのActorが対象になる
     * @param status Actorの状態
     * @return 条件を満たしているActorのリスト
     */
    public List<BaseActor> getObjects(Class cls, ActorStatus status) {
        List<BaseActor> actors = new ArrayList<>();

        for (Object tmpActor : getObjects(null)) {
            BaseActor actor = (BaseActor) tmpActor;
            if (actor.getActorStatus() != status) {
                continue;
            }

            actors.add(actor);
        }

        return actors;
    }

    @Override
    public void addObject(Actor actor, int x, int y) {
        assert actor != null;
        assert actor instanceof BaseActor;

        super.addObject(actor, x, y);
    }

    public void addDisabledObject(Actor actor, int x, int y) {
        ((BaseActor) actor).setActorStatus(ActorStatus.DISABLED);
        addObject(actor, x, y);
    }

    /**
     * Worldを指定した遷移先へ切り替える
     *
     * @param key 遷移先
     */
    public void changeWorld(String key) {
        try {
            World nextWorld = (World) Config.getNextWorld(this.getClass(), key).newInstance();
            Greenfoot.setWorld(nextWorld);
        } catch (InstantiationException | IllegalAccessException e) {
            // e.printStackTrace(System.out);
            Greenfoot.stop();
        }
    }

    @Override
    public void addListner(EventListener listener) {
        this.listener = listener;
    }

    @Override
    public void tick() {
    }

    @Override
    public void onAlived() {
        if (listener != null) {
            listener.onAlived();

        }
    }

    @Override
    public void onDisabled() {
        if (listener != null) {
            listener.onDisabled();

        }
    }

    @Override
    public void onDied() {
        if (listener != null) {
            listener.onDied();

        }
    }

    @Override
    public void onRemoved() {
        if (listener != null) {
            listener.onRemoved();

        }
    }

    @Override
    public void onMouseIn(MouseInfo mouse) {
        if (listener != null) {
            listener.onMouseIn(mouse);
        }
    }

    @Override
    public void onMouseOut(MouseInfo mouse) {
        if (listener != null) {
            listener.onMouseOut(mouse);
        }
    }

    @Override
    public void onMouseMoved(MouseInfo mouse) {
        if (listener != null) {
            listener.onMouseMoved(mouse);
        }
    }

    @Override
    public void onMouseDown(MouseInfo mouse) {
        if (listener != null) {
            listener.onMouseDown(mouse);
        }
    }

    @Override
    public void onMouseHolding(MouseInfo mouse) {
        if (listener != null) {
            listener.onMouseHolding(mouse);
        }
    }

    @Override
    public void onMouseUp(MouseInfo mouse) {
        if (listener != null) {
            listener.onMouseUp(mouse);
        }
    }

    @Override
    public void onMouseClicked(MouseInfo mouse) {
        if (listener != null) {
            listener.onMouseClicked(mouse);
        }
    }

    @Override
    public void onMouseDragging(MouseInfo mouse) {
        if (listener != null) {
            listener.onMouseDragging(mouse);
        }
    }

    @Override
    public void onMouseDragged(MouseInfo mouse) {
        if (listener != null) {
            listener.onMouseDragged(mouse);
        }
    }

    @Override
    public void onKeyDown(String key) {
        if (listener != null) {
            listener.onKeyDown(key);
        }
    }

    @Override
    public void onKeyHolding(String key) {
        if (listener != null) {
            listener.onKeyHolding(key);
        }
    }

    @Override
    public void onKeyUp(String key) {
        if (listener != null) {
            listener.onKeyUp(key);
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
