package mcjty.gearswap.network;

import io.netty.buffer.ByteBuf;
import mcjty.gearswap.blocks.GearSwapperTE;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketRememberSetup implements IMessage, IMessageHandler<PacketRememberSetup, IMessage> {
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

    public PacketRememberSetup() {
    }

    public PacketRememberSetup(BlockPos pos, int index) {
        this.pos = pos;
        this.index = index;
    }

    @Override
    public IMessage onMessage(PacketRememberSetup message, MessageContext ctx) {
        EntityPlayerMP playerEntity = ctx.getServerHandler().playerEntity;
        TileEntity te = playerEntity.worldObj.getTileEntity(message.pos);
        if (te instanceof GearSwapperTE) {
            GearSwapperTE gearSwapperTE = (GearSwapperTE) te;
            gearSwapperTE.rememberSetup(message.index, playerEntity);
            playerEntity.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.YELLOW + "Remembered current hotbar and armor"));
        }
        return null;
    }
}
