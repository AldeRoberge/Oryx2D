package rotmg.signals;

import utils.osflash.signals.Signal;

public class ExitGameSignal extends Signal {

    private static ExitGameSignal instance;

    public static ExitGameSignal getInstance() {
        if (instance == null) {
            instance = new ExitGameSignal();
        }
        return instance;
    }

}
