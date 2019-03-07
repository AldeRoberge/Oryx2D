package rotmg.messaging.data;

import flash.IData;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class ObjectData implements IData {

    public short objectType;
    public ObjectStatusData status;

    public ObjectData() {
        this.status = new ObjectStatusData();
    }

    @Override
    public void parseFromInput(DataInput in) throws IOException {
        this.objectType = in.readShort();
        this.status.parseFromInput(in);
    }

    @Override
    public void writeToOutput(DataOutput out) throws IOException {

        out.writeShort(this.objectType);
        this.status.writeToOutput(out);
    }

}
