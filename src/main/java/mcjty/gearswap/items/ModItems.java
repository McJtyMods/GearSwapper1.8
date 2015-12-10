package mcjty.gearswap.items;

import mcjty.gearswap.GearSwap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModItems {
    public static ForceEmptyItem forceEmptyItem;

    public static void init() {
        forceEmptyItem = new ForceEmptyItem();
    }

    @SideOnly(Side.CLIENT)
    public static void initModels() {
        ItemModelMesher mesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
        mesher.register(forceEmptyItem, 0, new ModelResourceLocation(GearSwap.MODID + ":" + forceEmptyItem.getUnlocalizedName().substring(5), "inventory"));
    }
}
