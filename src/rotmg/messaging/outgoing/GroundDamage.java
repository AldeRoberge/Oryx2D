package rotmg.messaging.outgoing;

import utils.flash.consumer.MessageConsumer;
import rotmg.messaging.data.WorldPosData;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class GroundDamage extends OutgoingMessage {

    public int time;
    public WorldPosData position;

    public GroundDamage(int id, MessageConsumer callback) {
        super(id, callback);
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
