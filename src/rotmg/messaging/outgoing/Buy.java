package rotmg.messaging.outgoing;

import utils.flash.consumer.MessageConsumer;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;


public class Buy extends OutgoingMessage {

    public int objectId;
    public int quantity;

    public Buy(int id, MessageConsumer callback) {
        super(id, callback);
    }

    @Override
    public void parseFromInput(DataInput in) throws IOException {
        this.objectId = in.readInt();
        this.quantity = in.readInt();
    }

    @Override
    public void writeToOutput(DataOutput out) throws IOException {
        out.writeInt(this.objectId);
        out.writeInt(this.quantity);
    }

}
