package rotmg.messaging.incoming;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import alde.flash.utils.consumer.MessageConsumer;
import rotmg.messaging.data.ObjectStatusData;

public class NewTick extends IncomingMessage {

	public int tickId;
	public int tickTime;
	public ObjectStatusData[] statuses = new ObjectStatusData[0];

	public NewTick(int id, MessageConsumer callback) {
		super(id, callback);
	}

	@Override
	public void parseFromInput(DataInput in) throws IOException {
		this.tickId = in.readInt();
		this.tickTime = in.readInt();
		this.statuses = new ObjectStatusData[in.readShort()];
		for (int i = 0; i < this.statuses.length; i++) {
			ObjectStatusData ObjectStatusData = new ObjectStatusData();
			ObjectStatusData.parseFromInput(in);
			this.statuses[i] = ObjectStatusData;
		}
	}

	@Override
	public void writeToOutput(DataOutput out) throws IOException {
		out.writeInt(this.tickId);
		out.writeInt(this.tickTime);
		out.writeShort(this.statuses.length);
		for (ObjectStatusData ObjectStatusData : this.statuses) {
			ObjectStatusData.writeToOutput(out);
		}
	}

}
