package rotmg.messaging.data;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import alde.flash.utils.IData;

public class MoveRecord implements IData {

	public int time;
	public double x;
	public double y;

	public MoveRecord() {
	}

	public MoveRecord(int time, double x, double y) {
		super();
		this.time = time;
		this.x = x;
		this.y = y;
	}

	@Override
	public void parseFromInput(DataInput in) throws IOException {
		this.time = in.readInt();
		this.x = in.readDouble();
		this.y = in.readDouble();

	}

	@Override
	public void writeToOutput(DataOutput out) throws IOException {
		out.writeInt(this.time);
		out.writeDouble(this.x);
		out.writeDouble(this.y);
	}

}
