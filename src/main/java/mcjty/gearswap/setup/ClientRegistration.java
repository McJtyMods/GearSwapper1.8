package mcjty.gearswap.setup;


import mcjty.gearswap.ConfigSetup;
import mcjty.gearswap.blocks.GearSwapperTESR;
import mcjty.gearswap.blocks.ModBlocks;
import mcjty.gearswap.items.ModItems;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientRegistration {

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        ModBlocks.glassGearSwapperBlock.initModel();
        ModBlocks.ironGearSwapperBlock.initModel();
        ModBlocks.lapisGearSwapperBlock.initModel();
        ModBlocks.stoneGearSwapperBlock.initModel();
        ModBlocks.woodenGearSwapperBlock.initModel();
        ModItems.forceEmptyItem.initModel();

        if (!ConfigSetup.customBlockName.isEmpty()) {
            Block b = Block.REGISTRY.getObject(new ResourceLocation(ConfigSetup.customBlockName));
            if (b != null) {
                ItemModelMesher mesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
                mesher.register(Item.getItemFromBlock(ModBlocks.moddedGearSwapperBlock), 0, new ModelResourceLocation(b.getRegistryName(), "inventory"));
            }
        }

        GearSwapperTESR.register();
    }

}
