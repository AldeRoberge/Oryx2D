package rotmg.signals;

import rotmg.model.AddSpeechBalloonVO;
import utils.osflash.signals.Signal;

public class AddSpeechBalloonSignal extends Signal<AddSpeechBalloonVO> {

    private static AddSpeechBalloonSignal instance;

    public static AddSpeechBalloonSignal getInstance() {
        if (instance == null) {
            instance = new AddSpeechBalloonSignal();
        }
        return instance;
    }

}
