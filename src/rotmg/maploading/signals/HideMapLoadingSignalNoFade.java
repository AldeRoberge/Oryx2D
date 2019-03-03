package rotmg.maploading.signals;

import utils.osflash.signals.Signal;

public class HideMapLoadingSignalNoFade extends Signal {

    static HideMapLoadingSignalNoFade instance;

    public static HideMapLoadingSignalNoFade getInstance() {
        if (instance == null) {
            instance = new HideMapLoadingSignalNoFade();
        }
        return instance;
    }

}

