package rotmg.tooltips.view;

public class TooltipsView extends Sprite {

    private DisplayObject toolTip;

    public TooltipsView() {
        super();
    }

    public void show(DisplayObject param1) {
        this.hide();
        this.toolTip = param1;
        if (param1 != null) {
            this.addChild(param1);
        }
    }

    public void hide() {
        if ((this.toolTip != null) && (this.toolTip.parent != null)) {
            this.toolTip.parent.removeChild(this.toolTip);
        }
        this.toolTip = null;
    }


}
