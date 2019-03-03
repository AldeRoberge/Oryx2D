package rotmg.maploading.signals;

import utils.osflash.signals.Signal;

public class HideMapLoadingSignal extends Signal {

    static HideMapLoadingSignal instance;

    public static HideMapLoadingSignal getInstance() {
        if (instance == null) {
            instance = new HideMapLoadingSignal();
        }
        return instance;
    }

}

