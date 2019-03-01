package rotmg.ui.tooltip;

import alde.flash.utils.Vector;
import alde.flash.utils.consumer.EventConsumer;
import alde.flash.utils.consumer.SignalConsumer;
import flash.display.CapsStyle;
import flash.display.DisplayObject;
import flash.display.GraphicsPath;
import flash.display.GraphicsSolidFill;
import flash.display.GraphicsStroke;
import flash.display.IGraphicsData;
import flash.display.JointStyle;
import flash.display.LineScaleMode;
import flash.display.Sprite;
import flash.events.Event;
import flash.events.MouseEvent;
import rotmg.ui.view.SignalWaiter;
import rotmg.util.GraphicsUtil;
import spark.filters.DropShadowFilter;

public class ToolTip extends Sprite {

	protected final SignalWaiter waiter = new SignalWaiter();
	public int contentWidth;
	public int contentHeight;
	private int background;
	private double backgroundAlpha;
	private int outline;
	private double outlineAlpha;
	private boolean _followMouse;
	private boolean forcePositionLeft = false;
	private boolean forcePositionRight = false;
	private DisplayObject targetObj;
	private GraphicsSolidFill backgroundFill;
	private GraphicsSolidFill outlineFill;
	private GraphicsStroke lineStyle;
	private GraphicsPath path;
	private Vector<IGraphicsData> graphicsData;

	public ToolTip(int param1, double param2, int param3, double param4, boolean param5) {
		super();
		this.backgroundFill = new GraphicsSolidFill(0, 1);
		this.outlineFill = new GraphicsSolidFill(16777215, 1);
		this.lineStyle = new GraphicsStroke(1, false, LineScaleMode.NORMAL, CapsStyle.NONE, JointStyle.ROUND, 3, this.outlineFill);
		this.path = new GraphicsPath(new Vector<Integer>(), new Vector<Double>());

		this.graphicsData = new Vector<>(this.lineStyle, this.backgroundFill, this.path, GraphicsUtil.END_FILL, GraphicsUtil.END_STROKE);
		this.background = param1;
		this.backgroundAlpha = param2;
		this.outline = param3;
		this.outlineAlpha = param4;
		this._followMouse = param5;
		this.mouseEnabled = false;
		this.mouseChildren = false;
		this.filters = new Vector<>(new DropShadowFilter(0, 0, 0, 1, 16, 16));
		this.addEventListener(Event.ADDED_TO_STAGE, new EventConsumer<>(this::onAddedToStage));
		this.addEventListener(Event.REMOVED_FROM_STAGE, new EventConsumer<>(this::onRemovedFromStage));
		this.waiter.complete.add(new SignalConsumer<>(this::alignUIAndDraw));
	}

	public void alignUIAndDraw() {
		this.alignUI();
		this.draw();
		this.position();
	}

	protected void alignUI() {
	}

	public void attachToTarget(DisplayObject param1) {
		if (param1 != null) {
			this.targetObj = param1;
			this.targetObj.addEventListener(MouseEvent.ROLL_OUT, new EventConsumer<>(this::onLeaveTarget));
		}
	}

	public void detachFromTarget() {
		if (this.targetObj != null) {
			this.targetObj.removeEventListener(MouseEvent.ROLL_OUT, new EventConsumer<>(this::onLeaveTarget));
			if (this.parent != null) {
				this.parent.removeChild(this);
			}
			this.targetObj = null;
		}
	}

	public void forcePostionLeft() {
		this.forcePositionLeft = true;
		this.forcePositionRight = false;
	}

	public void forcePostionRight() {
		this.forcePositionRight = true;
		this.forcePositionLeft = false;
	}

	private void onLeaveTarget(MouseEvent param1) {
		this.detachFromTarget();
	}

	private void onAddedToStage(Event param1) {
		if (this.waiter.isEmpty()) {
			this.draw();
		}
		if (this._followMouse) {
			this.position();
			this.addEventListener(Event.ENTER_FRAME, new EventConsumer<>(this::onEnterFrame));
		}
	}

	private void onRemovedFromStage(Event param1) {
		if (this._followMouse) {
			this.removeEventListener(Event.ENTER_FRAME, new EventConsumer<>(this::onEnterFrame));
		}
	}

	private void onEnterFrame(Event param1) {
		this.position();
	}

	protected void position() {
		if (this.stage == null) {
			return;
		}
		if ((!this.forcePositionLeft && (this.stage.mouseX < (this.stage.stageWidth / 2))) || this.forcePositionRight) {
			this.x = this.stage.mouseX + 12;
		} else {
			this.x = this.stage.mouseX - this.width - 1;
		}
		if (this.x < 12) {
			this.x = 12;
		}
		if ((!this.forcePositionLeft && (this.stage.mouseY < (this.stage.stageHeight / 3))) || this.forcePositionRight) {
			this.y = this.stage.mouseY + 12;
		} else {
			this.y = this.stage.mouseY - this.height - 1;
		}
		if (this.y < 12) {
			this.y = 12;
		}
	}

	public void draw() {
		this.backgroundFill.color = this.background;
		this.backgroundFill.alpha = this.backgroundAlpha;
		this.outlineFill.color = this.outline;
		this.outlineFill.alpha = this.outlineAlpha;
		this.graphics.clear();
		this.contentWidth = this.width;
		this.contentHeight = this.height;
		GraphicsUtil.clearPath(this.path);
		GraphicsUtil.drawCutEdgeRect(-6, -6, this.contentWidth + 12, this.contentHeight + 12, 4, new Vector<>(1, 1, 1, 1), this.path);
		this.graphics.drawGraphicsData(this.graphicsData);
	}


}
