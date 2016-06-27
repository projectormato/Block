
import greenfoot.*;
import java.util.List;

interface EventHandler{
    void addListner(EventListener listener);

    void tick();

    void onMouseIn(MouseInfo mouse);
    void onMouseOut(MouseInfo mouse);
    void onMouseMoved(MouseInfo mouse);
    
    void onMouseDown(MouseInfo mouse);
    void onMouseHolding(MouseInfo mouse);
    void onMouseUp(MouseInfo mouse);
    void onMouseClicked(MouseInfo mouse);
    void onMouseDragging(MouseInfo mouse);
    void onMouseDragged(MouseInfo mouse);
    
    void onKeyDown(String key);
    void onKeyHolding(String key);
    void onKeyUp(String key);

    void setListenKeys(List<String> keys);
    List<String> getListenKeys();
    void setLastKeyStatus(String key, boolean isKeyDown);
    boolean getLastKeyStatus(String key);

    final static int MOUSE_HOVERED = 1;
    final static int MOUSE_PRESSED = 2;
    final static int MOUSE_DRAGGED = 4;
    void setLastMouseStatus(int type, boolean flag);
    boolean getLastMouseStatus(int type);
}
