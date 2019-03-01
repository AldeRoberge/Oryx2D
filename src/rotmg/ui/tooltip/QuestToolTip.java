package rotmg.ui.tooltip;

import alde.flash.utils.Vector;
import rotmg.objects.GameObject;
import rotmg.text.view.stringBuilder.LineBuilder;
import rotmg.text.view.stringBuilder.TextFieldDisplayConcrete;
import rotmg.ui.GameObjectListItem;
import rotmg.util.TextKey;
import spark.filters.DropShadowFilter;

public class QuestToolTip extends ToolTip {

    public GameObjectListItem enemyGOLI;
    private TextFieldDisplayConcrete text;

    public QuestToolTip(GameObject param1) {
        super(6036765, 1, 16549442, 1, false);
        this.text = new TextFieldDisplayConcrete().setSize(22).setColor(16549442).setBold(true);
        this.text.setStringBuilder(new LineBuilder().setParams(TextKey.QUEST_TOOLTIP_QUEST));
        this.text.filters = new Vector<>(new DropShadowFilter(0, 0, 0));
        this.text.x = 0;
        this.text.y = 0;
        this.waiter.push(this.text.textChanged);
        this.addChild(this.text);
        this.enemyGOLI = new GameObjectListItem(11776947, true, param1);
        this.enemyGOLI.x = 0;
        this.enemyGOLI.y = 32;
        this.waiter.push(this.enemyGOLI.textReady);
        this.addChild(this.enemyGOLI);
        this.filters = new Vector<>();
    }
}
