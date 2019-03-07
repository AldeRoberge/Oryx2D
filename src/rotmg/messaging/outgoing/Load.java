package rotmg.messaging.outgoing;

import flash.consumer.MessageConsumer;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class Load extends OutgoingMessage {

    public int charId;
    public boolean isFromArena;

    public Load(int id, MessageConsumer callback) {
        super(id, callback);
    }

    @Override
    public void parseFromInput(DataInput in) throws IOException {
        this.charId = in.readInt();
        this.isFromArena = in.readBoolean();
    }

    @Override
    public void writeToOutput(DataOutput out) throws IOException {
        out.writeInt(this.charId);
        out.writeBoolean(this.isFromArena);
    }

    @Override
    public String toString() {
        return "Load{" +
                "charId=" + this.charId +
                ", isFromArena=" + this.isFromArena +
                '}';
    }
}
