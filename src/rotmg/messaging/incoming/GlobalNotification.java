package rotmg.messaging.incoming;

import flash.consumer.MessageConsumer;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class GlobalNotification extends IncomingMessage {

    public int type;
    public String text;

    public GlobalNotification(int id, MessageConsumer callback) {
        super(id, callback);
    }

    @Override
    public void parseFromInput(DataInput in) throws IOException {
        this.type = in.readInt();
        this.text = in.readUTF();
    }

    @Override
    public void writeToOutput(DataOutput out) throws IOException {
        out.writeInt(this.type);
        out.writeUTF(this.text);
    }

}
