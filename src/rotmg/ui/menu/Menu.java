package rotmg.ui.menu;

import alde.flash.utils.Vector;
import alde.flash.utils.consumer.EventConsumer;
import flash.display.*;
import flash.events.Event;
import flash.events.MouseEvent;
import flash.geom.Rectangle;
import rotmg.ui.view.UnFocusAble;
import rotmg.util.GraphicsUtil;
import rotmg.util.RectangleUtil;
import spark.filters.DropShadowFilter;

public class Menu extends Sprite implements UnFocusAble {

    protected int yOffset;
    private GraphicsSolidFill backgroundFill;
    private GraphicsSolidFill outlineFill;
    private GraphicsStroke lineStyle;
    private GraphicsPath path;
    private Vector<IGraphicsData> graphicsData;
    private int background;
    private int outline;

    public Menu(int param1, int param2) {
        super();
        this.backgroundFill = new GraphicsSolidFill(0, 1);
        this.outlineFill = new GraphicsSolidFill(0, 1);
        this.lineStyle = new GraphicsStroke(1, false, LineScaleMode.NORMAL, CapsStyle.NONE, JointStyle.ROUND, 3, this.outlineFill);
        this.path = new GraphicsPath(new Vector<Integer>(), new Vector<Double>());
        this.graphicsData = new Vector<>(this.lineStyle, this.backgroundFill, this.path, GraphicsUtil.END_FILL, GraphicsUtil.END_STROKE);
        this.background = param1;
        this.outline = param2;
        this.yOffset = 40;
        this.filters = new Vector<>(new DropShadowFilter(0, 0, 0, 1, 16, 16));
        this.addEventListener(Event.ADDED_TO_STAGE, new EventConsumer<>(this::onAddedToStage));
        this.addEventListener(Event.REMOVED_FROM_STAGE, new EventConsumer<>(this::onRemovedFromStage));
    }

    protected void addOption(MenuOption param1) {
        param1.x = 8;
        param1.y = this.yOffset;
        this.addChild(param1);
        this.yOffset = this.yOffset + 28;
    }

    protected void onAddedToStage(Event param1) {
        this.draw();
        this.position();
        this.addEventListener(Event.ENTER_FRAME, new EventConsumer<>(this::onEnterFrame));
        this.addEventListener(MouseEvent.ROLL_OUT, new EventConsumer<>(this::onRollOut));
    }

    protected void onRemovedFromStage(Event param1) {
        this.removeEventListener(Event.ENTER_FRAME, new EventConsumer<>(this::onEnterFrame));
        this.removeEventListener(MouseEvent.ROLL_OUT, new EventConsumer<>(this::onRollOut));
    }

    protected void onEnterFrame(Event param1) {
        if (this.stage == null) {
            return;
        }
        Rectangle loc2 = this.getRect(this.stage);
        double loc3 = RectangleUtil.pointDist(loc2, this.stage.mouseX, this.stage.mouseY);
        if (loc3 > 40) {
            this.remove();
        }
    }


    private void position() {
        if (this.stage == null) {
            return;
        }
        if (this.stage.mouseX < (this.stage.stageWidth / 2)) {
            this.x = this.stage.mouseX + 12;
        } else {
            this.x = this.stage.mouseX - this.width - 1;
        }
        if (this.x < 12) {
            this.x = 12;
        }
        if (this.stage.mouseY < (this.stage.stageHeight / 3)) {
            this.y = this.stage.mouseY + 12;
        } else {
            this.y = this.stage.mouseY - this.height - 1;
        }
        if (this.y < 12) {
            this.y = 12;
        }
    }

    protected void onRollOut(Event param1) {
        this.remove();
    }

    public void remove() {
        if (this.parent != null) {
            this.parent.removeChild(this);
        }
    }

    protected void draw() {
        this.backgroundFill.color = this.background;
        this.outlineFill.color = this.outline;
        this.graphics.clear();
        GraphicsUtil.clearPath(this.path);
        GraphicsUtil.drawCutEdgeRect(-6, -6, Math.max(154, this.width + 12), this.height + 12, 4, new Vector<>(1, 1, 1, 1), this.path);
        this.graphics.drawGraphicsData(this.graphicsData);
    }
}
