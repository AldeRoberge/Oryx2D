package rotmg.messaging.outgoing;

import flash.consumer.MessageConsumer;
import rotmg.messaging.data.WorldPosData;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class AoeAck extends OutgoingMessage {

    public int time;
    public WorldPosData position;

    public AoeAck(int id, MessageConsumer callback) {
        super(id, callback);
        this.position = new WorldPosData();
    }

    @Override
    public void parseFromInput(DataInput in) throws IOException {
        this.time = in.readInt();
        this.position.parseFromInput(in);
    }

    @Override
    public void writeToOutput(DataOutput out) throws IOException {
        out.writeInt(this.time);
        this.position.writeToOutput(out);
    }

}
