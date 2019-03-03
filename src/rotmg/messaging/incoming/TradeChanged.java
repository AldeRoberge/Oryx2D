package rotmg.messaging.incoming;

import utils.flash.consumer.MessageConsumer;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class TradeChanged extends IncomingMessage {

    private boolean[] offers = new boolean[0];

    public TradeChanged(int id, MessageConsumer callback) {
        super(id, callback);
    }

    @Override
    public void parseFromInput(DataInput in) throws IOException {
        this.offers = new boolean[in.readShort()];
        for (int i = 0; i < this.offers.length; i++) {
            this.offers[i] = in.readBoolean();
        }
    }

    @Override
    public void writeToOutput(DataOutput out) throws IOException {
        out.writeShort(this.offers.length);
        for (boolean b : this.offers) {
            out.writeBoolean(b);
        }
    }

}
