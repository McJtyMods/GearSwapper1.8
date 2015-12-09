package mcjty.gearswap.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GearSwapperGlassBlock extends GearSwapperBlock {
    public GearSwapperGlassBlock(Material material, String blockName) {
        super(material, blockName);
    }

//
//
//    @Override
//    @SideOnly(Side.CLIENT)
//    public int getRenderBlockPass()
//    {
//        return 0;
//    }
//
//    @Override
//    public boolean renderAsNormalBlock()
//    {
//        return false;
//    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean shouldSideBeRendered(IBlockAccess world, BlockPos pos, EnumFacing side) {
        Block block = world.getBlockState(pos).getBlock();

        if (block == this || block == Blocks.glass) {
            return false;
        }

        return super.shouldSideBeRendered(world, pos, side);
    }

}
