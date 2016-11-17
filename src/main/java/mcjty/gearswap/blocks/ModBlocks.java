package mcjty.gearswap.blocks;

import mcjty.gearswap.Config;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModBlocks {
    public static final String GEAR_SWAPPER_WOOD = "gearswapperwood";
    public static final String GEAR_SWAPPER_IRON = "gearswapperiron";
    public static final String GEAR_SWAPPER_LAPIS = "gearswapperlapis";
    public static final String GEAR_SWAPPER_STONE = "gearswapperstone";
    public static final String GEAR_SWAPPER_GLASS = "gearswapperglass";

    public static GearSwapperBlock woodenGearSwapperBlock;
    public static GearSwapperBlock ironGearSwapperBlock;
    public static GearSwapperBlock lapisGearSwapperBlock;
    public static GearSwapperBlock stoneGearSwapperBlock;
    public static GearSwapperBlock moddedGearSwapperBlock;
    public static GearSwapperGlassBlock glassGearSwapperBlock;

    public static void init() {
        woodenGearSwapperBlock = new GearSwapperBlock(Material.WOOD, GEAR_SWAPPER_WOOD);
        ironGearSwapperBlock = new GearSwapperBlock(Material.IRON, GEAR_SWAPPER_IRON);
        lapisGearSwapperBlock = new GearSwapperBlock(Material.ROCK, GEAR_SWAPPER_LAPIS);
        stoneGearSwapperBlock = new GearSwapperBlock(Material.ROCK, GEAR_SWAPPER_STONE);
        glassGearSwapperBlock = new GearSwapperGlassBlock(Material.GLASS, GEAR_SWAPPER_GLASS);

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
        ClientRegistry.bindTileEntitySpecialRenderer(GearSwapperTE.class, new GearSwapperTESR());

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
