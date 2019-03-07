package rotmg.messaging.incoming.arena;

import flash.consumer.MessageConsumer;
import rotmg.messaging.outgoing.OutgoingMessage;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class ImminentArenaWave extends OutgoingMessage {

    private int currentRuntime;

    public ImminentArenaWave(int id, MessageConsumer callback) {
        super(id, callback);
    }

    @Override
    public void parseFromInput(DataInput in) throws IOException {
        this.currentRuntime = in.readInt();
    }

    @Override
    public void writeToOutput(DataOutput out) throws IOException {
        out.writeInt(this.currentRuntime);
    }

}
