package rotmg.core.signals;

import utils.osflash.signals.Signal;

public class InvalidateDataSignal extends Signal {

    private static InvalidateDataSignal instance;

    public static InvalidateDataSignal getInstance() {
        if (instance == null) {
            instance = new InvalidateDataSignal();
        }
        return instance;
    }

}
