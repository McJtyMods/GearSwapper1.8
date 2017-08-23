package mcjty.gearswap.network;

import io.netty.buffer.ByteBuf;
import mcjty.gearswap.blocks.GearSwapperTE;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketRememberSetup implements IMessage {
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

    public static class Handler implements IMessageHandler<PacketRememberSetup, IMessage> {
        @Override
        public IMessage onMessage(PacketRememberSetup message, MessageContext ctx) {
            FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
            return null;
        }

        private void handle(PacketRememberSetup message, MessageContext ctx) {
            EntityPlayerMP playerEntity = ctx.getServerHandler().player;
            TileEntity te = playerEntity.getEntityWorld().getTileEntity(message.pos);
            if (te instanceof GearSwapperTE) {
                GearSwapperTE gearSwapperTE = (GearSwapperTE) te;
                gearSwapperTE.rememberSetup(message.index, playerEntity);
                ITextComponent component = new TextComponentString(TextFormatting.YELLOW + "Remembered current hotbar and armor");
                if (playerEntity instanceof EntityPlayer) {
                    ((EntityPlayer) playerEntity).sendStatusMessage(component, false);
                } else {
                    playerEntity.sendMessage(component);
                }
            }
        }
    }
}
