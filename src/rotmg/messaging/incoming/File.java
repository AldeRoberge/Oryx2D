package rotmg.messaging.incoming;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import flash.consumer.MessageConsumer;

public class File extends IncomingMessage {

    private String filename;
    private byte[] bytes = new byte[0];

    public File(int id, MessageConsumer callback) {
        super(id, callback);
    }

    @Override
    public void parseFromInput(DataInput in) throws IOException {
        this.filename = in.readUTF();
        this.bytes = new byte[in.readInt()];
        in.readFully(this.bytes);
    }

    @Override
    public void writeToOutput(DataOutput out) throws IOException {
        out.writeUTF(this.filename);
        out.writeInt(this.bytes.length);
        out.write(this.bytes);
    }

}
