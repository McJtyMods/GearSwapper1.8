package mcjty.gearswap.varia;

//import baubles.api.BaublesApi;
import mcjty.gearswap.GearSwap;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

public class Tools {

    // This code is copied from EnderIO. Thanks!
    public enum WhoAmI {
        SPCLIENT, MPCLIENT, SPSERVER, MPSERVER, OTHER;

        public static WhoAmI whoAmI(World world) {
            Side side = FMLCommonHandler.instance().getSide();
            if (side == Side.CLIENT) {
                if (Minecraft.getMinecraft().isIntegratedServerRunning()) {
                    if (world.isRemote) {
                        return SPCLIENT;
                    } else {
                        return SPSERVER;
                    }
                } else {
                    return MPCLIENT;
                }
            } else if (side == Side.SERVER) {
                if (MinecraftServer.getServer().isDedicatedServer()) {
                    return MPSERVER;
                } else if (Minecraft.getMinecraft().isIntegratedServerRunning()) {
                    return SPSERVER;
                }
            }
            return OTHER;
        }
    }

    /**
     * Do NOT modify this inventory on the client side of a singleplayer game!
     *
     * Wrap it in a ShadowInventory if you need to.
     */
    public static IInventory getBaubles(EntityPlayer player) {
        return null;
//        return GearSwap.baubles ? BaublesApi.getBaubles(player) : null;
    }
}