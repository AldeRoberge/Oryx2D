package rotmg.messaging.incoming.pets;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import flash.consumer.MessageConsumer;
import rotmg.messaging.outgoing.OutgoingMessage;

public class DeletePetMessage extends OutgoingMessage {

    int petID;

    public DeletePetMessage(int id, MessageConsumer callback) {
        super(id, callback);
    }

    @Override
    public void parseFromInput(DataInput in) throws IOException {
        this.petID = in.readInt();
    }

    @Override
    public void writeToOutput(DataOutput out) throws IOException {
        out.writeInt(this.petID);
    }

}
