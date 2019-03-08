package rotmg.messaging.outgoing;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import flash.consumer.MessageConsumer;
import rotmg.messaging.data.SlotObjectData;
import rotmg.messaging.data.WorldPosData;

public class InvSwap extends OutgoingMessage {

    public int time;
    public WorldPosData position;
    public SlotObjectData slotObject1;
    public SlotObjectData slotObject2;

    public InvSwap(int id, MessageConsumer callback) {
        super(id, callback);
        this.position = new WorldPosData();
        this.slotObject1 = new SlotObjectData();
        this.slotObject2 = new SlotObjectData();
    }

    @Override
    public void parseFromInput(DataInput in) throws IOException {
        this.time = in.readInt();
        this.position.parseFromInput(in);
        this.slotObject1.parseFromInput(in);
        this.slotObject2.parseFromInput(in);
    }

    @Override
    public void writeToOutput(DataOutput out) throws IOException {
        out.writeInt(this.time);
        this.position.writeToOutput(out);
        this.slotObject1.writeToOutput(out);
        this.slotObject2.writeToOutput(out);
    }

}
