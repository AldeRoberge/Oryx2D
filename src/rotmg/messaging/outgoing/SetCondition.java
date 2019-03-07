package rotmg.messaging.outgoing;

import flash.consumer.MessageConsumer;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class SetCondition extends OutgoingMessage {

    public int conditionEffect;
    public double conditionDuration;

    public SetCondition(int id, MessageConsumer callback) {
        super(id, callback);
    }

    @Override
    public void parseFromInput(DataInput in) throws IOException {
        this.conditionEffect = in.readUnsignedByte();
        this.conditionDuration = in.readFloat();
    }

    @Override
    public void writeToOutput(DataOutput out) throws IOException {
        out.writeByte(this.conditionEffect);
        out.writeDouble(this.conditionDuration);
    }

}
