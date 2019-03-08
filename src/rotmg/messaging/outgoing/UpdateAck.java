package rotmg.messaging.outgoing;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import flash.consumer.MessageConsumer;

public class UpdateAck extends OutgoingMessage {

    /**
     * The client maps this to a Message (empty Packet)
     * This class is meant as a replacement of an empty packet
     */

    public UpdateAck(int id, MessageConsumer callback) {
        super(id, callback);
    }

    @Override
    public void parseFromInput(DataInput in) throws IOException {

    }

    @Override
    public void writeToOutput(DataOutput out) throws IOException {

    }

}
