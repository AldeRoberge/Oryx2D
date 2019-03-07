package rotmg.messaging.outgoing;

import flash.consumer.MessageConsumer;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class CreateGuild extends OutgoingMessage {

    public String name;

    public CreateGuild(int id, MessageConsumer callback) {
        super(id, callback);
    }

    @Override
    public void parseFromInput(DataInput in) throws IOException {
        this.name = in.readUTF();
    }

    @Override
    public void writeToOutput(DataOutput out) throws IOException {
        out.writeUTF(this.name);
    }

}
