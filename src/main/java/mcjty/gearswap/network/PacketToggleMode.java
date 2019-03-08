package mcjty.gearswap.network;

import io.netty.buffer.ByteBuf;
import mcjty.gearswap.blocks.GearSwapperTE;
import mcjty.lib.thirteen.Context;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.function.Supplier;

public class PacketToggleMode implements IMessage {
    private BlockPos pos;
    private int index;


    @Override
    public void fromBytes(ByteBuf buf) {
        pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
        index = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(pos.getX());
        buf.writeInt(pos.getY());
        buf.writeInt(pos.getZ());
        buf.writeInt(index);
    }

    public PacketToggleMode() {
    }

    public PacketToggleMode(ByteBuf buf) {
        fromBytes(buf);
    }

    public PacketToggleMode(BlockPos pos, int index) {
        this.pos = pos;
        this.index = index;
    }

    public void handle(Supplier<Context> supplier) {
        Context ctx = supplier.get();
        ctx.enqueueWork(() -> {
            EntityPlayerMP playerEntity = ctx.getSender();
            TileEntity te = playerEntity.getEntityWorld().getTileEntity(pos);
            if (te instanceof GearSwapperTE) {
                GearSwapperTE gearSwapperTE = (GearSwapperTE) te;
                gearSwapperTE.toggelExportMode(index);
            }
        });
        ctx.setPacketHandled(true);
    }
}
