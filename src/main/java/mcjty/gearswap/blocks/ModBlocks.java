package mcjty.gearswap.blocks;

import mcjty.gearswap.Config;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
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
        woodenGearSwapperBlock = new GearSwapperBlock(Material.wood, "gearSwapperWood");
        ironGearSwapperBlock = new GearSwapperBlock(Material.iron, "gearSwapperIron");
        lapisGearSwapperBlock = new GearSwapperBlock(Material.rock, "gearSwapperLapis");
        stoneGearSwapperBlock = new GearSwapperBlock(Material.rock, "gearSwapperStone");
        glassGearSwapperBlock = new GearSwapperGlassBlock(Material.glass, "gearSwapperGlass");

        if (!Config.moddedTextureName.isEmpty()) {
            moddedGearSwapperBlock = new GearSwapperBlock(Material.rock, "gearSwapperModded");
        }

        GameRegistry.registerTileEntity(GearSwapperTE.class, "gearSwapper");
    }

    @SideOnly(Side.CLIENT)
    public static void initMimicingModels() {
        woodenGearSwapperBlock.registerModel(Blocks.planks);
        ironGearSwapperBlock.registerModel(Blocks.iron_block);
        lapisGearSwapperBlock.registerModel(Blocks.lapis_block);
        stoneGearSwapperBlock.registerModel(Blocks.stone);
        glassGearSwapperBlock.registerModel(Blocks.glass);
    }

    @SideOnly(Side.CLIENT)
    public static void initModels() {
        woodenGearSwapperBlock.initModel();
        ironGearSwapperBlock.initModel();
        lapisGearSwapperBlock.initModel();
        stoneGearSwapperBlock.initModel();
        glassGearSwapperBlock.initModel();
    }
}
