package mcjty.gearswap.items;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModItems {
    public static ForceEmptyItem forceEmptyItem;

    public static void init() {
        forceEmptyItem = new ForceEmptyItem();
    }

    @SideOnly(Side.CLIENT)
    public static void initModels() {
        ModelLoader.setCustomModelResourceLocation(forceEmptyItem, 0, new ModelResourceLocation(forceEmptyItem.getRegistryName(), "inventory"));
    }
}
