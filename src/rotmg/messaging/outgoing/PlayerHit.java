package rotmg.messaging.outgoing;

import flash.consumer.MessageConsumer;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class PlayerHit extends OutgoingMessage {

    public int bulletId;
    public int objectId;

    public PlayerHit(int id, MessageConsumer callback) {
        super(id, callback);
    }

    @Override
    public void parseFromInput(DataInput in) throws IOException {
        this.bulletId = in.readUnsignedByte();
        this.objectId = in.readInt();
    }

    @Override
    public void writeToOutput(DataOutput out) throws IOException {
        out.writeByte(this.bulletId);
        out.writeInt(this.objectId);
    }

}
