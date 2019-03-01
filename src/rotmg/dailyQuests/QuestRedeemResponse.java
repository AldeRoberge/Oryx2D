package rotmg.dailyQuests;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import alde.flash.utils.consumer.MessageConsumer;
import rotmg.messaging.incoming.IncomingMessage;

public class QuestRedeemResponse extends IncomingMessage {

	private boolean ok;
	private String message;

	public QuestRedeemResponse(int id, MessageConsumer callback) {
		super(id, callback);
	}

	@Override
	public void parseFromInput(DataInput in) throws IOException {
		this.ok = in.readBoolean();
		this.message = in.readUTF();
	}

	@Override
	public void writeToOutput(DataOutput out) throws IOException {
		out.writeBoolean(this.ok);
		out.writeUTF(this.message);
	}

}
