package rotmg.messaging.incoming.arena;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import flash.consumer.MessageConsumer;
import rotmg.messaging.outgoing.OutgoingMessage;

public class ArenaDeath extends OutgoingMessage {

    private int cost;

    public ArenaDeath(int id, MessageConsumer callback) {
        super(id, callback);
    }

    @Override
    public void parseFromInput(DataInput in) throws IOException {
        this.cost = in.readInt();
    }

    @Override
    public void writeToOutput(DataOutput out) throws IOException {
        out.writeInt(this.cost);
    }

}
