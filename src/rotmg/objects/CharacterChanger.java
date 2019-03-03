package rotmg.objects;

import utils.flash.XML;
import rotmg.GameSprite;
import rotmg.ui.panels.Panel;

public class CharacterChanger extends GameObject implements IInteractiveObject {

    public CharacterChanger(XML param1) {
        super(param1);
        this.isInteractive = true;
    }

    /*
    TODO this is not implemented
     */
    @Override
    public Panel getPanel(GameSprite param1) {
        return new Panel(param1);
        //return new CharacterChangerPanel(param1);
    }
}

