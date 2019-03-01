package rotmg.ui;

import alde.flash.utils.consumer.EventConsumer;
import flash.events.MouseEvent;
import rotmg.text.view.stringBuilder.LineBuilder;

public class TextButtonBase extends BackgroundFilledText {

    public TextButtonBase(int param1) {
        super(param1);
    }

    protected void initText() {
        this.centerTextAndDrawButton();
        this.draw();
        this.addEventListener(MouseEvent.MOUSE_OVER, new EventConsumer<>(this::onMouseOver));
        this.addEventListener(MouseEvent.ROLL_OUT, new EventConsumer<>(this::onRollOut));
    }

    public void setText(String param1) {
        this.text.setStringBuilder(new LineBuilder().setParams(param1));
    }

    public void setEnabled(boolean param1) {
        if (param1 == this.mouseEnabled) {
            return;
        }
        this.mouseEnabled = param1;
        this.graphicsData.put(0, !!param1 ? this.enabledFill : this.disabledFill);
        this.draw();
    }

    private void onMouseOver(MouseEvent param1) {
        this.enabledFill.color = 16768133;
        this.draw();
    }

    private void onRollOut(MouseEvent param1) {
        this.enabledFill.color = 16777215;
        this.draw();
    }

    private void draw() {
        this.graphics.clear();
        this.graphics.drawGraphicsData(this.graphicsData);
    }
}
