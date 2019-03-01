package rotmg.account.core.signals;

import org.osflash.signals.Signal;

public class CharListDataSignal extends Signal {

	CharListDataSignal instance;

	public CharListDataSignal getInstance() {
		if (this.instance == null) {
			this.instance = new CharListDataSignal();
		}

		return this.instance;
	}

}
