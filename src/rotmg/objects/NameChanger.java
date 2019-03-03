package rotmg.objects;

import utils.flash.XML;
import rotmg.GameSprite;
import rotmg.ui.panels.Panel;
import rotmg.view.NameChangerPanel;

public class NameChanger extends GameObject implements IInteractiveObject {

    public int rankRequired = 0;

    public NameChanger(XML param1) {
        super(param1);
        this.isInteractive = true;
    }

    public void setRankRequired(int param1) {
        this.rankRequired = param1;
    }

    @Override
    public Panel getPanel(GameSprite param1) {
        return new NameChangerPanel(param1, this.rankRequired);
    }
}
