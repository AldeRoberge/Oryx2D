package rotmg.messaging.incoming;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import alde.flash.utils.consumer.MessageConsumer;
import rotmg.messaging.data.WorldPosData;

public class EnemyShoot extends IncomingMessage {

	public int bulletId;
	public int ownerId;
	public int bulletType;
	public WorldPosData startingPos;
	public double angle;
	public short damage;
	public int numShots;
	public double angleInc;

	public EnemyShoot(int id, MessageConsumer callback) {
		super(id, callback);
		this.startingPos = new WorldPosData();
	}

	@Override
	public void parseFromInput(DataInput in) throws IOException {
		this.bulletId = in.readUnsignedByte();
		this.ownerId = in.readInt();
		this.bulletType = in.readUnsignedByte();
		this.startingPos.parseFromInput(in);
		this.angle = in.readFloat();
		this.damage = in.readShort();

		try {
			this.numShots = in.readUnsignedByte();
			this.angleInc = in.readFloat();
		} catch (IOException e) {
			this.numShots = 1;
			this.angleInc = 0;
		}
	}

	@Override
	public void writeToOutput(DataOutput out) throws IOException {
		out.writeByte(this.bulletId);
		out.writeInt(this.ownerId);
		out.writeByte(this.bulletType);
		this.startingPos.writeToOutput(out);
		out.writeDouble(this.angle);
		out.writeShort(this.damage);
		if (this.numShots != 1) {
			out.writeByte(this.numShots);
			out.writeDouble(this.angleInc);
		}
	}

}
