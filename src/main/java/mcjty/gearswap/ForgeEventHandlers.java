package mcjty.gearswap;

import mcjty.gearswap.blocks.GearSwapperTE;
import mcjty.gearswap.blocks.ModBlocks;
import mcjty.gearswap.items.ModItems;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ForgeEventHandlers {

    @SubscribeEvent
    public void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().register(ModBlocks.glassGearSwapperBlock);
        event.getRegistry().register(ModBlocks.ironGearSwapperBlock);
        event.getRegistry().register(ModBlocks.lapisGearSwapperBlock);
        event.getRegistry().register(ModBlocks.stoneGearSwapperBlock);
        event.getRegistry().register(ModBlocks.woodenGearSwapperBlock);
        if (ModBlocks.moddedGearSwapperBlock != null) {
            event.getRegistry().register(ModBlocks.moddedGearSwapperBlock);
        }
        GameRegistry.registerTileEntity(GearSwapperTE.class, GearSwap.MODID + "_gearSwapper");
    }

    @SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().register(ModItems.forceEmptyItem);

        event.getRegistry().register(new ItemBlock(ModBlocks.glassGearSwapperBlock).setRegistryName(ModBlocks.glassGearSwapperBlock.getRegistryName()));
        event.getRegistry().register(new ItemBlock(ModBlocks.ironGearSwapperBlock).setRegistryName(ModBlocks.ironGearSwapperBlock.getRegistryName()));
        event.getRegistry().register(new ItemBlock(ModBlocks.lapisGearSwapperBlock).setRegistryName(ModBlocks.lapisGearSwapperBlock.getRegistryName()));
        event.getRegistry().register(new ItemBlock(ModBlocks.stoneGearSwapperBlock).setRegistryName(ModBlocks.stoneGearSwapperBlock.getRegistryName()));
        event.getRegistry().register(new ItemBlock(ModBlocks.woodenGearSwapperBlock).setRegistryName(ModBlocks.woodenGearSwapperBlock.getRegistryName()));
        if (ModBlocks.moddedGearSwapperBlock != null) {
            event.getRegistry().register(new ItemBlock(ModBlocks.moddedGearSwapperBlock).setRegistryName(ModBlocks.moddedGearSwapperBlock.getRegistryName()));
        }
    }
}
