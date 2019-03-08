package rotmg.messaging.outgoing;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import flash.consumer.MessageConsumer;
import rotmg.messaging.data.MoveRecord;
import rotmg.messaging.data.WorldPosData;

public class Move extends OutgoingMessage {

    public int tickId;
    public int time;
    public WorldPosData newPosition;
    public List<MoveRecord> records;

    public Move(int param1, MessageConsumer param2) {
        super(param1, param2);
        this.newPosition = new WorldPosData();
        this.records = new ArrayList<>();
    }

    @Override
    public void parseFromInput(DataInput in) throws IOException {
        this.tickId = in.readInt();
        this.time = in.readInt();
        this.newPosition.parseFromInput(in);
        for (int i = 0; i < in.readShort(); i++) {
            MoveRecord record = new MoveRecord();
            record.parseFromInput(in);
            this.records.add(record);
        }
    }

    @Override
    public void writeToOutput(DataOutput out) throws IOException {
        out.writeInt(this.tickId);
        out.writeInt(this.time);
        this.newPosition.writeToOutput(out);
        out.writeShort(this.records.size());
        for (MoveRecord record : this.records) {
            record.writeToOutput(out);
        }
    }

}
