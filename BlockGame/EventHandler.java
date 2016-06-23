
import greenfoot.*;

interface EventHandler{
    void onMouseIn(MouseInfo mouse);
    void onMouseOut(MouseInfo mouse);
    void onMouseMoved(MouseInfo mouse);
    
    void onMouseDown(MouseInfo mouse);
    void onMouseHolding(MouseInfo mouse);
    void onMouseUp(MouseInfo mouse);
    void onMouseClicked(MouseInfo mouse);
    void onMouseDragging(MouseInfo mouse);
    void onMouseDragged(MouseInfo mouse);
    
    void onKeyDown();
    void onKeyHolding();
    void onKeyUp();
}
