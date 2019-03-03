package rotmg.messaging.incoming;

import utils.flash.consumer.MessageConsumer;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class PlaySound extends IncomingMessage {

    public int ownerId;
    public int soundId;

    public PlaySound(int id, MessageConsumer callback) {
        super(id, callback);
    }

    @Override
    public void parseFromInput(DataInput in) throws IOException {
        this.ownerId = in.readInt();
        this.soundId = in.readUnsignedByte();
    }

    @Override
    public void writeToOutput(DataOutput out) throws IOException {
        out.writeInt(this.ownerId);
        out.writeByte(this.soundId);
    }

}
