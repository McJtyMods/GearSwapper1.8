package mcjty.gearswap.blocks;

import mcjty.gearswap.ConfigSetup;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.ResourceLocation;

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

        if (!ConfigSetup.customBlockName.isEmpty()) {
            Block b = Block.REGISTRY.getObject(new ResourceLocation(ConfigSetup.customBlockName));
            if (b != null) {
                moddedGearSwapperBlock = new GearSwapperBlock(Material.ROCK, "gearSwapperModded");
            }
        }
    }
}
