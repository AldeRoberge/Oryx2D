package rotmg.messaging.incoming;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import flash.consumer.MessageConsumer;

public class Ping extends IncomingMessage {

    public int serial;

    public Ping(int id, MessageConsumer callback) {
        super(id, callback);
    }

    @Override
    public void parseFromInput(DataInput in) throws IOException {
        this.serial = in.readInt();
    }

    @Override
    public void writeToOutput(DataOutput out) throws IOException {
        out.writeInt(this.serial);
    }

}
