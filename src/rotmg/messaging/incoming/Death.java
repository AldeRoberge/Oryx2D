package rotmg.messaging.incoming;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import flash.consumer.MessageConsumer;

public class Death extends IncomingMessage {

    public String killedBy;
    public int zombieId;
    public int zombieType;
    public boolean isZombie;
    private String accountId;
    private int charId;

    public Death(int id, MessageConsumer callback) {
        super(id, callback);
    }

    @Override
    public void parseFromInput(DataInput in) throws IOException {
        this.accountId = in.readUTF();
        this.charId = in.readInt();
        this.killedBy = in.readUTF();
        this.zombieId = in.readInt();
        this.zombieType = in.readInt();
        this.isZombie = this.zombieType != -1;
    }

    @Override
    public void writeToOutput(DataOutput out) throws IOException {
        out.writeUTF(this.accountId);
        out.writeInt(this.charId);
        out.writeUTF(this.killedBy);
        out.writeInt(this.zombieId);
        out.writeInt(this.zombieType);
    }

}
