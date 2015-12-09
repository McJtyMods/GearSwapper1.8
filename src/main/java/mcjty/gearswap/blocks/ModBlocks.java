package mcjty.gearswap.blocks;

import mcjty.gearswap.Config;
import net.minecraft.block.Block;
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
    public static GearSwapperBlock glassGearSwapperBlock;

    public static void init() {
        woodenGearSwapperBlock = new GearSwapperBlock(Material.wood, "gearSwapperWood");
        GameRegistry.registerBlock(woodenGearSwapperBlock, "gearSwapperWood");

        ironGearSwapperBlock = new GearSwapperBlock(Material.iron, "gearSwapperIron");
        GameRegistry.registerBlock(ironGearSwapperBlock, "gearSwapperIron");

        lapisGearSwapperBlock = new GearSwapperBlock(Material.rock, "gearSwapperLapis");
        GameRegistry.registerBlock(lapisGearSwapperBlock, "gearSwapperLapis");

        stoneGearSwapperBlock = new GearSwapperBlock(Material.rock, "gearSwapperStone");
        GameRegistry.registerBlock(stoneGearSwapperBlock, "gearSwapperStone");

        glassGearSwapperBlock = new GearSwapperBlock(Material.glass, "gearSwapperGlass");
        GameRegistry.registerBlock(glassGearSwapperBlock, "gearSwapperGlass");

        if (!Config.moddedTextureName.isEmpty()) {
            moddedGearSwapperBlock = new GearSwapperBlock(Material.rock, "gearSwapperModded");
            GameRegistry.registerBlock(moddedGearSwapperBlock, "gearSwapperModded");
        }

        GameRegistry.registerTileEntity(GearSwapperTE.class, "gearSwapper");
    }

    @SideOnly(Side.CLIENT)
    public static void initModels() {
        woodenGearSwapperBlock.registerModel(Blocks.planks);
        ironGearSwapperBlock.registerModel(Blocks.iron_block);
        lapisGearSwapperBlock.registerModel(Blocks.lapis_block);
        stoneGearSwapperBlock.registerModel(Blocks.stone);
        glassGearSwapperBlock.registerModel(Blocks.glass);
    }
}
