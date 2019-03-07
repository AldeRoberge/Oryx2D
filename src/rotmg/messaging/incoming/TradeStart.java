package rotmg.messaging.incoming;

import flash.consumer.MessageConsumer;
import rotmg.messaging.data.TradeItem;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class TradeStart extends IncomingMessage {

    private TradeItem[] myItems;
    private String yourName;
    private TradeItem[] yourItems;

    public TradeStart(int id, MessageConsumer callback) {
        super(id, callback);
        this.myItems = new TradeItem[0];
        this.yourItems = new TradeItem[0];
    }

    @Override
    public void parseFromInput(DataInput in) throws IOException {
        this.myItems = new TradeItem[in.readShort()];
        for (int i = 0; i < this.myItems.length; i++) {
            TradeItem item = new TradeItem();
            item.parseFromInput(in);
            this.myItems[i] = item;
        }
        this.yourName = in.readUTF();
        this.yourItems = new TradeItem[in.readShort()];
        for (int i = 0; i < this.yourItems.length; i++) {
            TradeItem item = new TradeItem();
            item.parseFromInput(in);
            this.yourItems[i] = item;
        }
    }

    @Override
    public void writeToOutput(DataOutput out) throws IOException {
        out.writeShort(this.myItems.length);
        for (TradeItem item : this.myItems) {
            item.writeToOutput(out);
        }
        out.writeUTF(this.yourName);
        out.writeShort(this.yourItems.length);
        for (TradeItem item : this.yourItems) {
            item.writeToOutput(out);
        }
    }

}
