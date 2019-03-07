package rotmg.messaging.data;

import flash.IData;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class TradeItem implements IData {

    int item;
    int slotType;
    boolean tradeable;
    boolean included;

    @Override
    public void parseFromInput(DataInput in) throws IOException {
        this.item = in.readInt();
        this.slotType = in.readInt();
        this.tradeable = in.readBoolean();
        this.included = in.readBoolean();
    }

    @Override
    public void writeToOutput(DataOutput out) throws IOException {
        out.writeInt(this.item);
        out.writeInt(this.slotType);
        out.writeBoolean(this.tradeable);
        out.writeBoolean(this.included);
    }

}
