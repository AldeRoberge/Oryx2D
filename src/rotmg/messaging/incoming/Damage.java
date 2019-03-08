package rotmg.messaging.incoming;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import flash.consumer.MessageConsumer;

public class Damage extends IncomingMessage {

    public int targetId;
    public int[] effects = new int[0];
    public int damageAmount;
    public boolean kill;
    public boolean armorPierce;
    public int bulletId;
    public int objectId;

    public Damage(int id, MessageConsumer callback) {
        super(id, callback);
    }

    @Override
    public void parseFromInput(DataInput in) throws IOException {
        this.targetId = in.readInt();
        this.effects = new int[in.readUnsignedByte()];
        for (int i = 0; i < this.effects.length; i++) {
            this.effects[i] = in.readUnsignedByte();
        }
        this.damageAmount = in.readUnsignedShort();
        this.kill = in.readBoolean();
        this.armorPierce = in.readBoolean();
        this.bulletId = in.readUnsignedByte();
        this.objectId = in.readInt();
    }

    @Override
    public void writeToOutput(DataOutput out) throws IOException {
        out.writeInt(this.targetId);
        out.writeByte(this.effects.length);
        for (int effect : this.effects) {
            out.writeByte(effect);
        }
        out.writeShort(this.damageAmount);
        out.writeBoolean(this.kill);
        out.writeBoolean(this.armorPierce);
        out.writeByte(this.bulletId);
        out.writeInt(this.objectId);
    }

}
