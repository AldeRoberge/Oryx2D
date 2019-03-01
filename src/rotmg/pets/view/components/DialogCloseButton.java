package rotmg.pets.view.components;

import org.osflash.signals.Signal;

import alde.flash.utils.consumer.EventConsumer;
import flash.display.DisplayObject;
import flash.display.Sprite;
import flash.events.MouseEvent;

public class DialogCloseButton extends Sprite {

	//public static Class CloseButtonAsset = DialogCloseButton_CloseButtonAsset;

	//public static Class CloseButtonLargeAsset = DialogCloseButton_CloseButtonLargeAsset;

	public final Signal clicked = new Signal();

	public final Signal closeClicked = new Signal();

	public boolean disabled = false;

	public DialogCloseButton(double param1) {
		super();
		DisplayObject loc2 = null;
		if (param1 < 0) {
			//addChild(new CloseButtonAsset());
		} else {
			//loc2 = new CloseButtonLargeAsset();
			//addChild(new CloseButtonLargeAsset());
			this.scaleX = this.scaleX * param1;
			this.scaleY = this.scaleY * param1;
		}
		//buttonMode = true;
		this.addEventListener(MouseEvent.CLICK, new EventConsumer<>(this::onClicked));
	}

	public void setDisabled(boolean param1) {
		this.disabled = param1;
		if (param1) {
			this.removeEventListener(MouseEvent.CLICK, new EventConsumer<>(this::onClicked));
		} else {
			this.addEventListener(MouseEvent.CLICK, new EventConsumer<>(this::onClicked));
		}
	}

	public void disableLegacyCloseBehavior() {
		this.disabled = true;
		this.removeEventListener(MouseEvent.CLICK, new EventConsumer<>(this::onClicked));
	}

	private void onClicked(MouseEvent param1) {
		if (!this.disabled) {
			this.removeEventListener(MouseEvent.CLICK, new EventConsumer<>(this::onClicked));
			this.closeClicked.dispatch();
			this.clicked.dispatch();
		}
	}
}
