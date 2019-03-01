package rotmg.messaging.outgoing.arena;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import alde.flash.utils.consumer.MessageConsumer;
import rotmg.messaging.data.SlotObjectData;
import rotmg.messaging.outgoing.OutgoingMessage;

public class QuestRedeem extends OutgoingMessage {

	String questID;
	SlotObjectData[] slots;
	int item;

	public QuestRedeem(int id, MessageConsumer callback) {
		super(id, callback);
	}

	@Override
	public void parseFromInput(DataInput in) throws IOException {
		this.questID = in.readUTF();
		this.item = in.readInt();
		this.slots = new SlotObjectData[in.readShort()];
		for (int i = 0; i < this.slots.length; i++) {
			SlotObjectData s = new SlotObjectData();
			s.parseFromInput(in);
			this.slots[i] = s;
		}

	}

	@Override
	public void writeToOutput(DataOutput out) throws IOException {
		out.writeUTF(this.questID);
		out.writeInt(this.item);
		out.writeShort(this.slots.length);

		for (SlotObjectData slot : this.slots) {
			slot.writeToOutput(out);
		}
	}

}
