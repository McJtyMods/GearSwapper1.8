package mcjty.gearswap.blocks;

import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

public class GearSwapperItemBlock extends ItemBlock {
    private final GearSwapperBlock gearSwapperBlock;

    public GearSwapperItemBlock(Block block) {
        super(block);
        gearSwapperBlock = (GearSwapperBlock) block;
    }

    @Override
    public void addInformation(ItemStack itemStack, World player, List list, ITooltipFlag whatIsThis) {
        super.addInformation(itemStack, player, list, whatIsThis);
        gearSwapperBlock.addInformation(itemStack, player, list, whatIsThis);
    }

}
