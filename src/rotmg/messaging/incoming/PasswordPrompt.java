package rotmg.messaging.incoming;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import alde.flash.utils.consumer.MessageConsumer;

public class PasswordPrompt extends IncomingMessage {

	public final int SIGN_IN = 2;
	public final int SEND_EMAIL_AND_SIGN_IN = 3;
	public final int REGISTER = 4;
	private int cleanPasswordStatus;

	public PasswordPrompt(int id, MessageConsumer callback) {
		super(id, callback);
	}

	@Override
	public void parseFromInput(DataInput in) throws IOException {
		this.cleanPasswordStatus = in.readInt();
	}

	@Override
	public void writeToOutput(DataOutput out) throws IOException {
		out.writeInt(this.cleanPasswordStatus);
	}

}
