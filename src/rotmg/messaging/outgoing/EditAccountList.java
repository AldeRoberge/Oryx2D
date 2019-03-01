package rotmg.messaging.outgoing;

import alde.flash.utils.consumer.MessageConsumer;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class EditAccountList extends OutgoingMessage {

    public int accountListId;
    public boolean add;
    public int objectId;

    public EditAccountList(int id, MessageConsumer callback) {
        super(id, callback);
    }

    @Override
    public void parseFromInput(DataInput in) throws IOException {
        this.accountListId = in.readInt();
        this.add = in.readBoolean();
        this.objectId = in.readInt();
    }

    @Override
    public void writeToOutput(DataOutput out) throws IOException {
        out.writeInt(this.accountListId);
        out.writeBoolean(this.add);
        out.writeInt(this.objectId);
    }

}
