package mcjty.gearswap.setup;


import mcjty.gearswap.GearSwap;
import mcjty.gearswap.blocks.GearSwapperTE;
import mcjty.gearswap.blocks.ModBlocks;
import mcjty.gearswap.items.ModItems;
import mcjty.lib.datafix.fixes.TileEntityNamespace;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.datafix.FixTypes;
import net.minecraftforge.common.util.ModFixs;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber
public class Registration {

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        ModFixs modFixs = FMLCommonHandler.instance().getDataFixer().init(GearSwap.MODID, 1);
        Map<String, String> oldToNewIdMap = new HashMap<>();

        event.getRegistry().register(ModBlocks.glassGearSwapperBlock);
        event.getRegistry().register(ModBlocks.ironGearSwapperBlock);
        event.getRegistry().register(ModBlocks.lapisGearSwapperBlock);
        event.getRegistry().register(ModBlocks.stoneGearSwapperBlock);
        event.getRegistry().register(ModBlocks.woodenGearSwapperBlock);
        if (ModBlocks.moddedGearSwapperBlock != null) {
            event.getRegistry().register(ModBlocks.moddedGearSwapperBlock);
        }
        GameRegistry.registerTileEntity(GearSwapperTE.class, GearSwap.MODID + ":gearswapper");

        // We used to accidentally register TEs with names like "minecraft:gearswap_gearswapper" instead of "gearswap:gearswapper".
        // Set up a DataFixer to map these incorrect names to the correct ones, so that we don't break old saved games.
        // @todo Remove all this if we ever break saved-game compatibility.
        oldToNewIdMap.put(GearSwap.MODID + "_gearSwapper", GearSwap.MODID + ":gearswapper");
        oldToNewIdMap.put("minecraft:" + GearSwap.MODID + "_gearswapper", GearSwap.MODID + ":gearswapper");
        modFixs.registerFix(FixTypes.BLOCK_ENTITY, new TileEntityNamespace(oldToNewIdMap, 1));
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
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
