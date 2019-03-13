package mcjty.gearswap.setup;

import mcjty.gearswap.ConfigSetup;
import mcjty.gearswap.GearSwap;
import mcjty.gearswap.blocks.ModBlocks;
import mcjty.gearswap.items.ModItems;
import mcjty.gearswap.network.GearSwapPacketHandler;
import mcjty.lib.compat.MainCompatHandler;
import mcjty.lib.setup.DefaultModSetup;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import org.apache.logging.log4j.Level;

public class ModSetup extends DefaultModSetup {

    public static boolean baubles = false;

    @Override
    public void preInit(FMLPreInitializationEvent e) {
        super.preInit(e);

        NetworkRegistry.INSTANCE.registerGuiHandler(GearSwap.instance, new GuiProxy());

        GearSwapPacketHandler.registerMessages("gearswapper");

        ModBlocks.init();
        ModItems.init();
    }

    @Override
    protected void setupModCompat() {
        MainCompatHandler.registerWaila();
        MainCompatHandler.registerTOP();

        baubles = Loader.isModLoaded("baubles");
        if (baubles) {
            if (ConfigSetup.supportBaubles) {
                GearSwap.setup.getLogger().log(Level.INFO, "Gear Swapper Detected Baubles: enabling support");
            } else {
                GearSwap.setup.getLogger().log(Level.INFO, "Gear Swapper Detected Baubles but it is disabled in config anyway: disabling support");
                baubles = false;
            }
        }
    }

    @Override
    protected void setupConfig() {
        ConfigSetup.init();
    }

    @Override
    public void createTabs() {

    }

    @Override
    public void postInit(FMLPostInitializationEvent e) {
        super.postInit(e);
        ConfigSetup.postInit();
    }

}
