package rotmg.maploading.signals;

import utils.osflash.signals.Signal;

public class ChangeMapSignal extends Signal {

    static ChangeMapSignal instance;

    public static ChangeMapSignal getInstance() {
        if (instance == null) {
            instance = new ChangeMapSignal();
        }
        return instance;
    }

}
