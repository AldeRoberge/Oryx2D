package rotmg.core.signals;

import utils.flash.XML;
import utils.osflash.signals.Signal;

public class AppInitDataReceivedSignal extends Signal<XML> {

    private static AppInitDataReceivedSignal instance;

    public static AppInitDataReceivedSignal getInstance() {
        if (instance == null) {
            instance = new AppInitDataReceivedSignal();
        }
        return instance;
    }

}
