package rotmg.messaging.outgoing.arena;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import alde.flash.utils.consumer.MessageConsumer;
import rotmg.messaging.outgoing.OutgoingMessage;

public class EnterArena extends OutgoingMessage {

	private int currency;

	public EnterArena(int id, MessageConsumer callback) {
		super(id, callback);
	}

	@Override
	public void parseFromInput(DataInput in) throws IOException {
		this.currency = in.readInt();
	}

	@Override
	public void writeToOutput(DataOutput out) throws IOException {
		out.writeInt(this.currency);
	}

}
