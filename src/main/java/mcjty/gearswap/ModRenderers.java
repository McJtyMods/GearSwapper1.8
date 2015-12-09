package mcjty.gearswap;

import mcjty.gearswap.blocks.GearSwapperTE;
import mcjty.gearswap.blocks.GearSwapperTESR;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public final class ModRenderers {

    public static void init() {
        ClientRegistry.bindTileEntitySpecialRenderer(GearSwapperTE.class, new GearSwapperTESR());
    }
}
