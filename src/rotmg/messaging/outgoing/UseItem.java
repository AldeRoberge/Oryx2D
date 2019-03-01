package rotmg.messaging.outgoing;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import alde.flash.utils.consumer.MessageConsumer;
import rotmg.messaging.data.SlotObjectData;
import rotmg.messaging.data.WorldPosData;

public class UseItem extends OutgoingMessage {

	public int time;
	public SlotObjectData slotObject;
	public WorldPosData itemUsePos;
	public int useType;

	public UseItem(int id, MessageConsumer callback) {
		super(id, callback);
		this.slotObject = new SlotObjectData();
		this.itemUsePos = new WorldPosData();
	}

	@Override
	public void parseFromInput(DataInput in) throws IOException {
		this.time = in.readInt();
		this.slotObject.parseFromInput(in);
		this.itemUsePos.parseFromInput(in);
		this.useType = in.readUnsignedByte();
	}

	@Override
	public void writeToOutput(DataOutput out) throws IOException {
		out.writeInt(this.time);
		this.slotObject.writeToOutput(out);
		this.itemUsePos.writeToOutput(out);
		out.writeByte(this.useType);
	}

}
