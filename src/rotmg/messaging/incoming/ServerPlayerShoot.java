package rotmg.messaging.incoming;

import utils.flash.consumer.MessageConsumer;
import rotmg.messaging.data.WorldPosData;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class ServerPlayerShoot extends IncomingMessage {

    public int bulletId;
    public int ownerId;
    public int containerType;
    public WorldPosData startingPos;
    public double angle;
    public short damage;

    public ServerPlayerShoot(int id, MessageConsumer callback) {
        super(id, callback);
        this.startingPos = new WorldPosData();
    }

    @Override
    public void parseFromInput(DataInput in) throws IOException {
        this.bulletId = in.readUnsignedByte();
        this.ownerId = in.readInt();
        this.containerType = in.readInt();
        this.startingPos.parseFromInput(in);
        this.angle = in.readFloat();
        this.damage = in.readShort();
    }

    @Override
    public void writeToOutput(DataOutput out) throws IOException {
        out.writeByte(this.bulletId);
        out.writeInt(this.ownerId);
        out.writeInt(this.containerType);
        this.startingPos.writeToOutput(out);
        out.writeDouble(this.angle);
        out.writeShort(this.damage);
    }

}
