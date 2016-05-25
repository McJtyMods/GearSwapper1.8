package mcjty.gearswap;

import mcjty.gearswap.blocks.ModBlocks;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModCrafting {
    public static void init() {
        ItemStack lapisStack = new ItemStack(Items.DYE, 1, 4);

        GameRegistry.addRecipe(new ItemStack(ModBlocks.woodenGearSwapperBlock), "pCp", "pcp", "ppp", 'p', Blocks.PLANKS, 'C', Items.COMPARATOR, 'c', Blocks.CHEST);
        GameRegistry.addRecipe(new ItemStack(ModBlocks.ironGearSwapperBlock), "pCp", "pcp", "ppp", 'p', Items.IRON_INGOT, 'C', Items.COMPARATOR, 'c', Blocks.CHEST);
        GameRegistry.addRecipe(new ItemStack(ModBlocks.lapisGearSwapperBlock), "pCp", "pcp", "ppp", 'p', lapisStack, 'C', Items.COMPARATOR, 'c', Blocks.CHEST);
        GameRegistry.addRecipe(new ItemStack(ModBlocks.stoneGearSwapperBlock), "pCp", "pcp", "ppp", 'p', Blocks.STONE, 'C', Items.COMPARATOR, 'c', Blocks.CHEST);
        GameRegistry.addRecipe(new ItemStack(ModBlocks.glassGearSwapperBlock), "pCp", "pcp", "ppp", 'p', Blocks.GLASS, 'C', Items.COMPARATOR, 'c', Blocks.CHEST);
        if (ModBlocks.moddedGearSwapperBlock != null) {
            GameRegistry.addRecipe(new ItemStack(ModBlocks.moddedGearSwapperBlock), "pCp", "pcp", "ppp", 'p', Items.REDSTONE, 'C', Items.COMPARATOR, 'c', Blocks.CHEST);
        }
    }
}
