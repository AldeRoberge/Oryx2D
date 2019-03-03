package rotmg.messaging.incoming;

import utils.flash.consumer.MessageConsumer;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class AccountList extends IncomingMessage {

    public int accountListId;
    public String[] accountIds;
    private int lockAction = -1;

    public AccountList(int param1, MessageConsumer param2) {
        super(param1, param2);
        this.accountIds = new String[0];
    }

    @Override
    public void parseFromInput(DataInput in) throws IOException {
        this.accountListId = in.readInt();
        this.accountIds = new String[in.readShort()];
        for (int i = 0; i < this.accountIds.length; i++) {
            this.accountIds[i] = in.readUTF();
        }
        this.lockAction = in.readInt();
    }

    @Override
    public void writeToOutput(DataOutput out) throws IOException {
        out.writeInt(this.accountListId);
        out.writeShort(this.accountIds.length);
        for (String accountId : this.accountIds) {
            out.writeUTF(accountId);
        }
        out.writeInt(this.lockAction);
    }

}
