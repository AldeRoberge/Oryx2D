package rotmg.core.signals;

import utils.osflash.signals.Signal;

public class SetLoadingMessageSignal extends Signal<String> {

    private static SetLoadingMessageSignal instance;

    public static SetLoadingMessageSignal getInstance() {
        if (instance == null) {
            instance = new SetLoadingMessageSignal();
        }
        return instance;
    }

}
