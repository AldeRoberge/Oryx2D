package rotmg.messaging.outgoing;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import alde.flash.utils.consumer.MessageConsumer;
import rotmg.messaging.data.SlotObjectData;

public class InvDrop extends OutgoingMessage {

	public SlotObjectData slotObject;

	public InvDrop(int id, MessageConsumer callback) {
		super(id, callback);
		this.slotObject = new SlotObjectData();
	}

	@Override
	public void parseFromInput(DataInput in) throws IOException {
		this.slotObject.parseFromInput(in);
	}

	@Override
	public void writeToOutput(DataOutput out) throws IOException {
		this.slotObject.writeToOutput(out);
	}

}
