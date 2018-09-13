package mcjty.gearswap;

import mcjty.gearswap.compat.MainCompatHandler;
import mcjty.gearswap.proxy.CommonProxy;
import mcjty.lib.base.ModBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import java.io.File;

@Mod(modid = GearSwap.MODID, name="Gear Swapper",
        dependencies =
                "required-after:mcjtylib_ng@[" + GearSwap.MIN_MCJTYLIB_VER + ",);" +
                "after:forge@[" + GearSwap.MIN_FORGE11_VER + ",)",
        acceptedMinecraftVersions = "[1.12,1.13)",
        version = GearSwap.VERSION)
public class GearSwap implements ModBase {
    public static final String MODID = "gearswap";
    public static final String VERSION = "1.6.1";
    public static final String MIN_FORGE11_VER = "13.19.0.2176";
    public static final String MIN_MCJTYLIB_VER = "3.0.0";

    @SidedProxy(clientSide="mcjty.gearswap.proxy.ClientProxy", serverSide="mcjty.gearswap.proxy.ServerProxy")
    public static CommonProxy proxy;

    @Mod.Instance("gearswap")
    public static GearSwap instance;
    public static Logger logger;
    public static File mainConfigDir;
    public static File modConfigDir;
    public static Configuration config;

    public static int GUI_GEARSWAP = 0;

    public static boolean baubles = false;


    /**
     * Run before anything else. Read your config, create blocks, items, etc, and
     * register them with the GameRegistry.
     */
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        logger = e.getModLog();
        mainConfigDir = e.getModConfigurationDirectory();
        modConfigDir = new File(mainConfigDir.getPath());
        config = new Configuration(new File(modConfigDir, "gearswap.cfg"));
        proxy.preInit(e);

        MainCompatHandler.registerWaila();
        MainCompatHandler.registerTOP();
    }

    /**
     * Do your mod setup. Build whatever data structures you care about. Register recipes.
     */
    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        proxy.init(e);
    }

    /**
     * Handle interaction with other mods, complete your setup based on this.
     */
    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        proxy.postInit(e);

        baubles = Loader.isModLoaded("Baubles");
        if (baubles) {
            if (Config.supportBaubles) {
                logger.log(Level.INFO, "Gear Swapper Detected Baubles: enabling support");
            } else {
                logger.log(Level.INFO, "Gear Swapper Detected Baubles but it is disabled in config anyway: disabling support");
                baubles = false;
            }
        }
    }

    @Override
    public String getModId() {
        return GearSwap.MODID;
    }

    @Override
    public void openManual(EntityPlayer player, int bookindex, String page) {
        // @todo
    }
}
