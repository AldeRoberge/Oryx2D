package rotmg.focus.control;

import utils.osflash.signals.Signal;

public class SetGameFocusSignal extends Signal<String> {

    static SetGameFocusSignal instance;

    public static SetGameFocusSignal getInstance() {
        if (instance == null) {
            instance = new SetGameFocusSignal();
        }
        return instance;
    }

}
