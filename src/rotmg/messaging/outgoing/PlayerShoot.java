package rotmg.messaging.outgoing;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import flash.consumer.MessageConsumer;
import rotmg.messaging.data.WorldPosData;

public class PlayerShoot extends OutgoingMessage {

    public int time;
    public int bulletId;
    public int containerType;
    public WorldPosData startingPos;
    public double angle;

    public PlayerShoot(int id, MessageConsumer callback) {
        super(id, callback);
        this.startingPos = new WorldPosData();
    }

    @Override
    public void parseFromInput(DataInput in) throws IOException {
        this.time = in.readInt();
        this.bulletId = in.readUnsignedByte();
        this.containerType = in.readUnsignedShort();
        this.startingPos.parseFromInput(in);
        this.angle = in.readDouble();
    }

    @Override
    public void writeToOutput(DataOutput out) throws IOException {
        out.writeInt(this.time);
        out.writeByte(this.bulletId);
        out.writeShort(this.containerType);
        this.startingPos.writeToOutput(out);
        out.writeDouble(this.angle);
    }

}
