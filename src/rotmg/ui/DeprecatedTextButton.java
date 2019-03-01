package rotmg.ui;

import org.osflash.signals.Signal;

import alde.flash.utils.consumer.SignalConsumer;
import rotmg.text.view.stringBuilder.LineBuilder;
import rotmg.text.view.stringBuilder.StaticStringBuilder;

public class DeprecatedTextButton extends TextButtonBase {

	public final Signal textChanged = new Signal();

	public DeprecatedTextButton(int param1, String param2) {
		this(param1, param2, 0);
	}

	public DeprecatedTextButton(int param1, String param2, int param3) {
		this(param1, param2, param3, false);
	}

	public DeprecatedTextButton(int param1, String param2, int param3, boolean param4) {
		super(param3);
		this.addText(param1);
		if (param4) {
			this.text.setStringBuilder(new StaticStringBuilder(param2));
		} else {
			this.text.setStringBuilder(new LineBuilder().setParams(param2));
		}
		this.text.textChanged.addOnce(new SignalConsumer<>(this::onTextChanged));
	}

	protected void onTextChanged() {
		this.initText();
		this.textChanged.dispatch();
	}
}

