package flash.geom;

public class Point {
    public double x;
    public double y;

    public Point(double x, double y) {
    }

    public Point() {

    }

    public Point clone() {
        return new Point(this.x, this.y);
    }
}
