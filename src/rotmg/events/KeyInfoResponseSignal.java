package rotmg.events;

import rotmg.messaging.incoming.KeyInfoResponse;
import utils.osflash.signals.Signal;

public class KeyInfoResponseSignal extends Signal<KeyInfoResponse> {

    static KeyInfoResponseSignal instance;

    public static KeyInfoResponseSignal getInstance() {
        if (instance == null) {
            instance = new KeyInfoResponseSignal();
        }
        return instance;
    }

}
