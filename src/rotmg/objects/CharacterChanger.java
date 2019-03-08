package rotmg.objects;

import flash.XML;

public class CharacterChanger extends GameObject implements IInteractiveObject {

    public CharacterChanger(XML param1) {
        super(param1);
        this.isInteractive = true;
    }


}

