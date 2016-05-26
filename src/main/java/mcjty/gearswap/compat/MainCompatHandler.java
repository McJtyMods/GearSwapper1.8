package mcjty.gearswap.compat;

import mcjty.gearswap.compat.top.TOPCompatibility;
import mcjty.gearswap.compat.waila.WailaCompatibility;
import net.minecraftforge.fml.common.Loader;

public class MainCompatHandler {

    public static void registerWaila() {
        if (Loader.isModLoaded("Waila")) {
            WailaCompatibility.register();
        }
    }

    public static void registerTOP() {
        if (Loader.isModLoaded("theoneprobe")) {
            TOPCompatibility.register();
        }
    }

}
