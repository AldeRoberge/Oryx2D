package rotmg.messaging.incoming.pets;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import flash.consumer.MessageConsumer;
import rotmg.messaging.outgoing.OutgoingMessage;

public class Hatch_PetMessage extends OutgoingMessage {

    private String petName;
    private int petSkinId;

    public Hatch_PetMessage(int id, MessageConsumer callback) {
        super(id, callback);
    }

    @Override
    public void parseFromInput(DataInput in) throws IOException {
        this.petName = in.readUTF();
        this.petSkinId = in.readInt();
    }

    @Override
    public void writeToOutput(DataOutput out) throws IOException {
        out.writeUTF(this.petName);
        out.writeInt(this.petSkinId);

    }

}
