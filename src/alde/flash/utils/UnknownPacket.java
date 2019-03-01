package alde.flash.utils;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import alde.flash.utils.consumer.MessageConsumer;
import rotmg.messaging.incoming.IncomingMessage;

/**
 * Unused class kept from Realm-Relay
 */
public class UnknownPacket extends IncomingMessage {

	private final List<Byte> bytes = new LinkedList<>();
	private byte id;

	public UnknownPacket(int id, MessageConsumer callback) {
		super(id, callback);
	}

	@Override
	public void parseFromInput(DataInput in) throws IOException {
		try {
			while (true) {
				this.bytes.add(in.readByte());
			}
		} catch (Exception e) {
		}
	}

	public void setId(byte id) {
		this.id = id;
	}

	@Override
	public void writeToOutput(DataOutput out) throws IOException {
		for (Byte b : this.bytes) {
			out.writeByte(b);
		}
	}

}
