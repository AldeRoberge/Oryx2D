package rotmg.signals;

import utils.osflash.signals.Signal;

public class ShowProTipSignal extends Signal {

    public static ShowProTipSignal instance;

    public static ShowProTipSignal getInstance() {
        if (instance == null) {
            instance = new ShowProTipSignal();
        }
        return instance;
    }
}
