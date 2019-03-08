package rotmg.focus.control;

import utils.osflash.signals.Signal;

public class UpdateGroundTileSignal extends Signal {

    static UpdateGroundTileSignal instance;

    public static UpdateGroundTileSignal getInstance() {
        if (instance == null) {
            instance = new UpdateGroundTileSignal();
        }
        return instance;
    }

}
