package mcjty.gearswap.blocks;

import mcjty.gearswap.Config;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModBlocks {
    public static GearSwapperBlock woodenGearSwapperBlock;
    public static GearSwapperBlock ironGearSwapperBlock;
    public static GearSwapperBlock lapisGearSwapperBlock;
    public static GearSwapperBlock stoneGearSwapperBlock;
    public static GearSwapperBlock moddedGearSwapperBlock;
    public static GearSwapperGlassBlock glassGearSwapperBlock;

    public static void init() {
        woodenGearSwapperBlock = new GearSwapperBlock(Material.WOOD, "gearSwapperWood");
        ironGearSwapperBlock = new GearSwapperBlock(Material.IRON, "gearSwapperIron");
        lapisGearSwapperBlock = new GearSwapperBlock(Material.ROCK, "gearSwapperLapis");
        stoneGearSwapperBlock = new GearSwapperBlock(Material.ROCK, "gearSwapperStone");
        glassGearSwapperBlock = new GearSwapperGlassBlock(Material.GLASS, "gearSwapperGlass");

        if (!Config.customBlockName.isEmpty()) {
            Block b = Block.REGISTRY.getObject(new ResourceLocation(Config.customBlockName));
            if (b != null) {
                moddedGearSwapperBlock = new GearSwapperBlock(Material.ROCK, "gearSwapperModded");
            }
        }
        GameRegistry.registerTileEntity(GearSwapperTE.class, "gearSwapper");
    }

    @SideOnly(Side.CLIENT)
    public static void initModels() {
        woodenGearSwapperBlock.initModel();
        ironGearSwapperBlock.initModel();
        lapisGearSwapperBlock.initModel();
        stoneGearSwapperBlock.initModel();
        glassGearSwapperBlock.initModel();
        if (!Config.customBlockName.isEmpty()) {
            Block b = Block.REGISTRY.getObject(new ResourceLocation(Config.customBlockName));
            if (b != null) {
                ItemModelMesher mesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
                mesher.register(Item.getItemFromBlock(moddedGearSwapperBlock), 0, new ModelResourceLocation(b.getRegistryName(), "inventory"));

//                moddedGearSwapperBlock.initModel();
            }
        }
    }
}
