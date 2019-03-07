package rotmg.messaging.incoming;

import flash.consumer.MessageConsumer;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NameResult extends IncomingMessage {

    private boolean success;
    private String errorText;

    public NameResult(int id, MessageConsumer callback) {
        super(id, callback);
    }

    @Override
    public void parseFromInput(DataInput in) throws IOException {
        this.success = in.readBoolean();
        this.errorText = in.readUTF();
    }

    @Override
    public void writeToOutput(DataOutput out) throws IOException {
        out.writeBoolean(this.success);
        out.writeUTF(this.errorText);
    }

}
