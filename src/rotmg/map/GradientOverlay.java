package rotmg.map;

import alde.flash.utils.Vector;
import flash.airglobal.Shape;
import flash.display.GradientType;
import flash.display.GraphicsGradientFill;
import flash.display.GraphicsPath;
import flash.display.IGraphicsData;
import rotmg.util.GraphicsUtil;

public class GradientOverlay extends Shape {

    private final GraphicsGradientFill gradientFill = new GraphicsGradientFill(GradientType.LINEAR, new Vector<>(0, 0), new Vector<>(0.0, 1.0), new Vector<>(0, 255), GraphicsUtil.getGradientMatrix(10, 600));

    private final GraphicsPath gradientPath = GraphicsUtil.getRectPath(0, 0, 10, 600);

    private Vector<IGraphicsData> gradientGraphicsData;

    public GradientOverlay() {
        super();
        this.gradientGraphicsData = new Vector<>(this.gradientFill, this.gradientPath, GraphicsUtil.END_FILL);
        this.graphics.drawGraphicsData(this.gradientGraphicsData);
        this.visible = false;
    }

}
