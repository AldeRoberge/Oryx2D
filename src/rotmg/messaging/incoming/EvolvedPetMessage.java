package rotmg.messaging.incoming;

import flash.consumer.MessageConsumer;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class EvolvedPetMessage extends IncomingMessage {

    int petID;
    int initialSkin;
    int finalSkin;

    public EvolvedPetMessage(int id, MessageConsumer callback) {
        super(id, callback);
    }

    @Override
    public void parseFromInput(DataInput in) throws IOException {
        this.petID = in.readInt();
        this.initialSkin = in.readInt();
        this.finalSkin = in.readInt();
    }

    @Override
    public void writeToOutput(DataOutput out) throws IOException {
        out.writeInt(this.petID);
        out.writeInt(this.initialSkin);
        out.writeInt(this.finalSkin);
    }

}
