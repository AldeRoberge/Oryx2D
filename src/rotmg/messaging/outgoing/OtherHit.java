package rotmg.messaging.outgoing;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import flash.consumer.MessageConsumer;

public class OtherHit extends OutgoingMessage {

    public int time;
    public int bulletId;
    public int objectId;
    public int targetId;

    public OtherHit(int id, MessageConsumer callback) {
        super(id, callback);
    }

    @Override
    public void parseFromInput(DataInput in) throws IOException {
        this.time = in.readInt();
        this.bulletId = in.readByte();
        this.objectId = in.readInt();
        this.targetId = in.readInt();
    }

    @Override
    public void writeToOutput(DataOutput out) throws IOException {
        out.writeInt(this.time);
        out.writeByte(this.bulletId);
        out.writeInt(this.objectId);
        out.writeInt(this.targetId);
    }

}
