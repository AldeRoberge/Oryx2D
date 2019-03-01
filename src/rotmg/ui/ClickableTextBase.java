package rotmg.ui;

import alde.flash.utils.Vector;
import alde.flash.utils.consumer.EventConsumer;
import flash.display.Sprite;
import flash.events.MouseEvent;
import rotmg.sound.SoundEffectLibrary;
import rotmg.text.view.stringBuilder.LineBuilder;
import rotmg.text.view.stringBuilder.TextFieldDisplayConcrete;
import spark.filters.DropShadowFilter;

public class ClickableTextBase extends Sprite {

	public TextFieldDisplayConcrete text;

	public int defaultColor = 16777215;

	public ClickableTextBase(int param1, boolean param2, String param3) {
		super();
		this.text = this.makeText().setSize(param1).setColor(16777215);
		this.text.setBold(param2);
		this.text.setStringBuilder(new LineBuilder().setParams(param3));
		this.addChild(this.text);
		this.text.filters = new Vector<>(new DropShadowFilter(0, 0, 0));
		this.addEventListener(MouseEvent.MOUSE_OVER, new EventConsumer<>(this::onMouseOver));
		this.addEventListener(MouseEvent.MOUSE_OUT, new EventConsumer<>(this::onMouseOut));
		this.addEventListener(MouseEvent.CLICK, new EventConsumer<>(this::onMouseClick));
	}

	public void removeOnHoverEvents() {
		this.removeEventListener(MouseEvent.MOUSE_OVER, new EventConsumer<>(this::onMouseOver));
		this.removeEventListener(MouseEvent.MOUSE_OUT, new EventConsumer<>(this::onMouseOut));
	}

	protected TextFieldDisplayConcrete makeText() {
		return new TextFieldDisplayConcrete();
	}

	public void setAutoSize(String param1) {
		this.text.setAutoSize(param1);
	}

	public void makeStatic(String param1) {
		this.text.setStringBuilder(new LineBuilder().setParams(param1));
		this.setDefaultColor(11776947);
		this.mouseEnabled = false;
		this.mouseChildren = false;
	}

	public void setColor(int param1) {
		this.text.setColor(param1);
	}

	public void setDefaultColor(int param1) {
		this.defaultColor = param1;
		this.setColor(this.defaultColor);
	}

	private void onMouseOver(MouseEvent param1) {
		this.setColor(16768133);
	}

	private void onMouseOut(MouseEvent param1) {
		this.setColor(this.defaultColor);
	}

	private void onMouseClick(MouseEvent param1) {
		SoundEffectLibrary.play("button_click");
	}


}
