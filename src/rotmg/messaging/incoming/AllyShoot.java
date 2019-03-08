package rotmg.messaging.incoming;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import flash.consumer.MessageConsumer;

public class AllyShoot extends IncomingMessage {

    public int bulletId;
    public int ownerId;
    public short containerType;
    public double angle;

    public AllyShoot(int id, MessageConsumer callback) {
        super(id, callback);
    }

    @Override
    public void parseFromInput(DataInput in) throws IOException {
        this.bulletId = in.readUnsignedByte();
        this.ownerId = in.readInt();
        this.containerType = in.readShort();
        this.angle = in.readFloat();
    }

    @Override
    public void writeToOutput(DataOutput out) throws IOException {
        out.writeByte(this.bulletId);
        out.writeInt(this.ownerId);
        out.writeShort(this.containerType);
        out.writeDouble(this.angle);
    }

}
