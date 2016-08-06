
import greenfoot.MouseInfo;

/**
 * <p>EventHandlerを実装したクラスに対して、オブジェクト単位でイベントハンドラを追加するためのクラス。</p>
 *
 * 使用方法:<pre>
 * cursor.addListner(new EventListener() {
 *     &#064;Override
 *     public void tick() {
 *         super.tick();
 *         // write to here.
 *     }
 * });
 * </pre>
 *
 * @author yuuki0xff
 */
public class EventListener{
    public void tick(){}
    public void onAlived(){}
    public void onDisabled(){}
    public void onDied(){}
    public void onRemoved(){}

    public void onMouseIn(MouseInfo mouse){}
    public void onMouseOut(MouseInfo mouse){}
    public void onMouseMoved(MouseInfo mouse){}
    
    public void onMouseDown(MouseInfo mouse){}
    public void onMouseHolding(MouseInfo mouse){}
    public void onMouseUp(MouseInfo mouse){}
    public void onMouseClicked(MouseInfo mouse){}
    public void onMouseDragging(MouseInfo mouse){}
    public void onMouseDragged(MouseInfo mouse){}
    
    public void onKeyDown(String key){}
    public void onKeyHolding(String key){}
    public void onKeyUp(String key){}
}
