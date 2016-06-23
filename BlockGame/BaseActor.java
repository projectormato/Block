
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
