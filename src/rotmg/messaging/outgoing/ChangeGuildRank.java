package rotmg.messaging.outgoing;

import alde.flash.utils.consumer.MessageConsumer;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class ChangeGuildRank extends OutgoingMessage {

    public String name;
    public int guildRank;

    public ChangeGuildRank(int id, MessageConsumer callback) {
        super(id, callback);
    }

    @Override
    public void parseFromInput(DataInput in) throws IOException {
        this.name = in.readUTF();
        this.guildRank = in.readInt();
    }

    @Override
    public void writeToOutput(DataOutput out) throws IOException {
        out.writeUTF(this.name);
        out.writeInt(this.guildRank);
    }

}
