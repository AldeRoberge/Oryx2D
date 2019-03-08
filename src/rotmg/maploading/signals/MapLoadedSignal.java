package rotmg.maploading.signals;

import rotmg.messaging.incoming.MapInfo;
import utils.osflash.signals.Signal;

public class MapLoadedSignal extends Signal<MapInfo> {

    static MapLoadedSignal instance;

    public static MapLoadedSignal getInstance() {
        if (instance == null) {
            instance = new MapLoadedSignal();
        }
        return instance;
    }

}

