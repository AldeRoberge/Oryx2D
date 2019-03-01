package rotmg.messaging.incoming;

import alde.flash.utils.consumer.MessageConsumer;
import rotmg.messaging.data.WorldPosData;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class Aoe extends IncomingMessage {

    public WorldPosData pos;
    public double radius;
    private int damage;
    private int effect;
    private double duration;
    private int origType;
    private int color;

    public Aoe(int id, MessageConsumer callback) {
        super(id, callback);
        this.pos = new WorldPosData();
    }

    @Override
    public void parseFromInput(DataInput in) throws IOException {
        this.pos.parseFromInput(in);
        this.radius = in.readFloat();
        this.damage = in.readUnsignedShort();
        this.effect = in.readUnsignedByte();
        this.duration = in.readFloat();
        this.origType = in.readUnsignedShort();
        this.color = in.readInt();
    }

    @Override
    public void writeToOutput(DataOutput out) throws IOException {
        this.pos.writeToOutput(out);
        out.writeDouble(this.radius);
        out.writeShort(this.damage);
        out.writeByte(this.effect);
        out.writeDouble(this.duration);
        out.writeShort(this.origType);
        out.writeInt(this.color);
    }

}
