package rotmg.messaging.data;

import alde.flash.utils.IData;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Formelly known as 'Location' in RealmRelay 1.0
 */

public class WorldPosData implements IData {

    // - x = up
    // + x = down
    //
    // - y = left
    // + y = right

    public double x;
    public double y;

    public WorldPosData() {
        this.x = 0;
        this.y = 0;
    }

    public WorldPosData(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public WorldPosData(WorldPosData loc) {
        this.x = loc.x;
        this.y = loc.y;
    }

    @Override
    public WorldPosData clone() {
        return new WorldPosData(this.x, this.y);
    }

    public double distanceSquaredTo(WorldPosData location) {
        double dx = location.x - this.x;
        double dy = location.y - this.y;
        return (dx * dx) + (dy * dy);
    }

    public double distanceTo(WorldPosData location) {
        return Math.sqrt(this.distanceSquaredTo(location));
    }

    @Override
    public void parseFromInput(DataInput in) throws IOException {
        this.x = in.readFloat();
        this.y = in.readFloat();
    }

    @Override
    public String toString() {
        return this.x + " " + this.y;
    }

    @Override
    public void writeToOutput(DataOutput out) throws IOException {
        out.writeFloat((float) this.x);
        out.writeFloat((float) this.y);
    }

}
