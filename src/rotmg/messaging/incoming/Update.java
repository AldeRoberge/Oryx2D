package rotmg.messaging.incoming;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import alde.flash.utils.consumer.MessageConsumer;
import rotmg.messaging.data.GroundTileData;
import rotmg.messaging.data.ObjectData;

public class Update extends IncomingMessage {

	public GroundTileData[] tiles;
	public ObjectData[] newObjs;
	public int[] drops;

	public Update(int id, MessageConsumer callback) {
		super(id, callback);
		this.tiles = new GroundTileData[0];
		this.newObjs = new ObjectData[0];
		this.drops = new int[0];
	}

	@Override
	public void parseFromInput(DataInput in) throws IOException {

		this.tiles = new GroundTileData[in.readShort()];
		for (int i = 0; i < this.tiles.length; i++) {
			GroundTileData tile = new GroundTileData();
			tile.parseFromInput(in);
			this.tiles[i] = tile;
		}

		this.newObjs = new ObjectData[in.readShort()];
		for (int i = 0; i < this.newObjs.length; i++) {
			ObjectData entity = new ObjectData();
			entity.parseFromInput(in);
			this.newObjs[i] = entity;
		}

		try {
			this.drops = new int[in.readShort()];
			for (int i = 0; i < this.drops.length; i++) {
				this.drops[i] = in.readInt();
			}
		} catch (Exception e) {
		}

	}

	@Override
	public void writeToOutput(DataOutput out) throws IOException {
		out.writeShort(this.tiles.length);
		for (GroundTileData tile : this.tiles) {
			tile.writeToOutput(out);
		}
		out.writeShort(this.newObjs.length);
		for (ObjectData obj : this.newObjs) {
			obj.writeToOutput(out);
		}
		out.writeShort(this.drops.length);
		for (int drop : this.drops) {
			out.writeInt(drop);
		}
	}

}
