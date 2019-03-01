package rotmg.objects;

import alde.flash.utils.XML;
import rotmg.GameSprite;
import rotmg.ui.panels.Panel;
import rotmg.view.MoneyChangerPanel;

public class MoneyChanger extends GameObject implements IInteractiveObject {

	public MoneyChanger(XML param1) {
		super(param1);
		this.isInteractive = true;
	}

	@Override
	public Panel getPanel(GameSprite param1) {
		return new MoneyChangerPanel(param1);
	}
}
