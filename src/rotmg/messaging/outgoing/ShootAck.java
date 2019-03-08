package rotmg.messaging.outgoing;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import flash.consumer.MessageConsumer;

public class ShootAck extends OutgoingMessage {

    public int time;

    public ShootAck(int id, MessageConsumer callback) {
        super(id, callback);
    }

    @Override
    public void parseFromInput(DataInput in) throws IOException {
        this.time = in.readInt();
    }

    @Override
    public void writeToOutput(DataOutput out) throws IOException {
        out.writeInt(this.time);
    }

}
