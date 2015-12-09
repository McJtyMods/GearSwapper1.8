package mcjty.gearswap.proxy;

import mcjty.gearswap.GearSwap;
import mcjty.gearswap.blocks.GearSwapperContainer;
import mcjty.gearswap.blocks.GearSwapperTE;
import mcjty.gearswap.blocks.GuiGearSwapper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiProxy implements IGuiHandler {
    @Override
    public Object getServerGuiElement(int guiid, EntityPlayer entityPlayer, World world, int x, int y, int z) {
        if (guiid == GearSwap.GUI_GEARSWAP) {
            TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
            if (te instanceof GearSwapperTE) {
                return new GearSwapperContainer(entityPlayer, (GearSwapperTE) te);
            }
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int guiid, EntityPlayer entityPlayer, World world, int x, int y, int z) {
        TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
        if (te instanceof GearSwapperTE) {
            GearSwapperTE gearSwapperTE = (GearSwapperTE) te;
            return new GuiGearSwapper(gearSwapperTE, new GearSwapperContainer(entityPlayer, gearSwapperTE));
        }
        return null;
    }
}
