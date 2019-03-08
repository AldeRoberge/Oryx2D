package rotmg.signals;

import rotmg.model.UseBuyPotionVO;
import utils.osflash.signals.Signal;

public class UseBuyPotionSignal extends Signal<UseBuyPotionVO> {

    private static UseBuyPotionSignal instance;

    public static UseBuyPotionSignal getInstance() {
        if (instance == null) {
            instance = new UseBuyPotionSignal();
        }
        return instance;
    }

}
