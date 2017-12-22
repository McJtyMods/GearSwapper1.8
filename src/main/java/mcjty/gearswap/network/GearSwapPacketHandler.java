package mcjty.gearswap.network;

import mcjty.lib.network.PacketHandler;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class GearSwapPacketHandler {
    public static SimpleNetworkWrapper INSTANCE;

    public static void registerMessages(SimpleNetworkWrapper network) {
        INSTANCE = network;

        // Server side
        INSTANCE.registerMessage(PacketToggleMode.Handler.class, PacketToggleMode.class, PacketHandler.nextID(), Side.SERVER);
        INSTANCE.registerMessage(PacketRememberSetup.Handler.class, PacketRememberSetup.class, PacketHandler.nextID(), Side.SERVER);
    }
}
