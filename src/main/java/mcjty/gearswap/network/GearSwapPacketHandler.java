package mcjty.gearswap.network;

import mcjty.gearswap.GearSwap;
import mcjty.lib.network.PacketHandler;
import mcjty.lib.thirteen.ChannelBuilder;
import mcjty.lib.thirteen.SimpleChannel;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

public class GearSwapPacketHandler {
    public static SimpleNetworkWrapper INSTANCE;

    public static void registerMessages(String name) {
        SimpleChannel net = ChannelBuilder
                .named(new ResourceLocation(GearSwap.MODID, name))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net.getNetwork();

        // Server side
        net.registerMessageServer(id(), PacketToggleMode.class, PacketToggleMode::toBytes, PacketToggleMode::new, PacketToggleMode::handle);
        net.registerMessageServer(id(), PacketRememberSetup.class, PacketRememberSetup::toBytes, PacketRememberSetup::new, PacketRememberSetup::handle);
    }

    private static int id() {
        return PacketHandler.nextPacketID();
    }
}
