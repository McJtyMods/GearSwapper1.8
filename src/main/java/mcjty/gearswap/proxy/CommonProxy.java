package mcjty.gearswap.proxy;

import mcjty.gearswap.Config;
import mcjty.gearswap.ForgeEventHandlers;
import mcjty.gearswap.GearSwap;
import mcjty.gearswap.ModCrafting;
import mcjty.gearswap.blocks.ModBlocks;
import mcjty.gearswap.items.ModItems;
import mcjty.gearswap.network.PacketHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import org.apache.logging.log4j.Level;

public abstract class CommonProxy {

    private Configuration mainConfig;

    public void preInit(FMLPreInitializationEvent e) {
        MinecraftForge.EVENT_BUS.register(new ForgeEventHandlers());

        mainConfig = GearSwap.config;
        readMainConfig();

        PacketHandler.registerMessages("gearswapper");
    }

    private void readMainConfig() {
        Configuration cfg = mainConfig;
        try {
            cfg.load();
            cfg.addCustomCategoryComment(Config.CATEGORY_GEARSWAP, "General configuration");
            cfg.addCustomCategoryComment(Config.CATEGORY_RULES, "Rules that help decide if two items are considered equal. Every rule contains a list of tags that have to match");
            Config.init(cfg);
        } catch (Exception e1) {
            GearSwap.logger.log(Level.ERROR, "Problem loading config file!", e1);
        } finally {
            if (mainConfig.hasChanged()) {
                mainConfig.save();
            }
        }
    }

    public void init(FMLInitializationEvent e) {
        NetworkRegistry.INSTANCE.registerGuiHandler(GearSwap.instance, new GuiProxy());
    }

    public void postInit(FMLPostInitializationEvent e) {
        if (mainConfig.hasChanged()) {
            mainConfig.save();
        }
        mainConfig = null;

        ModCrafting.init();
    }

}
