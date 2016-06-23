
import greenfoot.*;
import java.util.logging.Logger;

/**
 * 全てのActorの親クラス
 */
public class BaseActor extends Actor implements EventHandler {

    protected Logger logger;

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
    public void onKeyDown() {
    }

    @Override
    public void onKeyHolding() {
    }

    @Override
    public void onKeyUp() {
    }

}
