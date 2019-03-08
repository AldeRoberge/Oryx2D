package rotmg.messaging.outgoing;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import flash.consumer.MessageConsumer;

public class CancelTrade extends OutgoingMessage {

    public CancelTrade(int id, MessageConsumer callback) {
        super(id, callback);
    }

    @Override
    public void parseFromInput(DataInput in) throws IOException {

    }

    @Override
    public void writeToOutput(DataOutput out) throws IOException {

    }

}
