package rotmg.messaging.outgoing;

import utils.flash.consumer.MessageConsumer;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class ActivePetUpdateRequest extends OutgoingMessage {

    private int commandtype;
    private int instanceid;

    public ActivePetUpdateRequest(int id, MessageConsumer callback) {
        super(id, callback);
    }

    @Override
    public void parseFromInput(DataInput in) throws IOException {
        this.commandtype = in.readByte();
        this.instanceid = in.readInt();
    }

    @Override
    public void writeToOutput(DataOutput out) throws IOException {
        out.writeInt(this.commandtype);
        out.writeByte(this.instanceid);
    }
}
