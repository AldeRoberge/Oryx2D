package rotmg.maploading.signals;

import utils.osflash.signals.Signal;

public class ShowLoadingViewSignal extends Signal {
    static ShowLoadingViewSignal instance;

    public static ShowLoadingViewSignal getInstance() {
        if (instance == null) {
            instance = new ShowLoadingViewSignal();
        }
        return instance;
    }
}

