package rotmg.messaging.data;

import flash.IData;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class ObjectStatusData implements IData {

    public int objectId;
    public WorldPosData pos = new WorldPosData();
    public StatData[] stats = new StatData[0];

    @Override
    public void parseFromInput(DataInput in) throws IOException {

        this.objectId = in.readInt();
        this.pos.parseFromInput(in);

        this.stats = new StatData[in.readShort()];
        for (int i = 0; i < this.stats.length; i++) {
            StatData statData = new StatData();
            statData.parseFromInput(in);
            this.stats[i] = statData;
        }
    }

    @Override
    public void writeToOutput(DataOutput out) throws IOException {

        out.writeInt(this.objectId);
        this.pos.writeToOutput(out);
        out.writeShort(this.stats.length);
        for (StatData statData : this.stats) {
            statData.writeToOutput(out);
        }
    }

}
