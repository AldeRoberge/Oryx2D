package rotmg.messaging.incoming;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import flash.consumer.MessageConsumer;

public class Notification extends IncomingMessage {

    public int objectId;
    public String message;
    public int color;

    public Notification(int id, MessageConsumer callback) {
        super(id, callback);
    }

    @Override
    public void parseFromInput(DataInput in) throws IOException {
        this.objectId = in.readInt();
        this.message = in.readUTF();
        this.color = in.readInt();
    }

    @Override
    public void writeToOutput(DataOutput out) throws IOException {
        out.writeInt(this.objectId);
        out.writeUTF(this.message);
        out.writeInt(this.color);
    }

}
