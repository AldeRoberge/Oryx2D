package rotmg.objects;

import flash.XML;

public class MysteryBoxGround extends GameObject implements IInteractiveObject {

    public MysteryBoxGround(XML param1) {
        super(param1);
        this.isInteractive = true;
    }


}
