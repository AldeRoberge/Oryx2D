package rotmg.objects;

import flash.XML;

public class MoneyChanger extends GameObject implements IInteractiveObject {

    public MoneyChanger(XML param1) {
        super(param1);
        this.isInteractive = true;
    }

}
