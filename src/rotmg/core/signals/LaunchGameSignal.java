package rotmg.core.signals;

import utils.osflash.signals.Signal;

public class LaunchGameSignal extends Signal {

    private static LaunchGameSignal instance;

    public static LaunchGameSignal getInstance() {
        if (instance == null) {
            instance = new LaunchGameSignal();
        }
        return instance;
    }

}
