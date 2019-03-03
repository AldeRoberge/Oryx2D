package rotmg.messaging.outgoing;

import utils.flash.consumer.MessageConsumer;
import rotmg.messaging.data.SlotObjectData;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class InvDrop extends OutgoingMessage {

    public SlotObjectData slotObject;

    public InvDrop(int id, MessageConsumer callback) {
        super(id, callback);
        this.slotObject = new SlotObjectData();
    }

    @Override
    public void parseFromInput(DataInput in) throws IOException {
        this.slotObject.parseFromInput(in);
    }

    @Override
    public void writeToOutput(DataOutput out) throws IOException {
        this.slotObject.writeToOutput(out);
    }

}
