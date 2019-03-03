package rotmg.messaging.incoming;

import utils.flash.consumer.MessageConsumer;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class VerifyEmail extends IncomingMessage {

    public VerifyEmail(int id, MessageConsumer callback) {
        super(id, callback);
    }

    @Override
    public void parseFromInput(DataInput in) throws IOException {
    }

    @Override
    public void writeToOutput(DataOutput out) throws IOException {
    }
}