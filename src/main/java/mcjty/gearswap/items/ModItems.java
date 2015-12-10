package mcjty.gearswap.items;

import mcjty.gearswap.GearSwap;
import net.minecraft.client.Minecraft;
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
        RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
        renderItem.getItemModelMesher().register(forceEmptyItem, 0, new ModelResourceLocation(GearSwap.MODID + ":emptyItem", "inventory"));
    }
}
