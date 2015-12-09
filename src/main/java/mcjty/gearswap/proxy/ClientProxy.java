package mcjty.gearswap.proxy;

import mcjty.gearswap.ModRenderers;
import mcjty.gearswap.blocks.GearSwapperBlock;
import mcjty.gearswap.blocks.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.ISmartBlockModel;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;
import java.util.Map;

public class ClientProxy extends CommonProxy {

    @Override
    public void preInit(FMLPreInitializationEvent e) {
        super.preInit(e);
        MinecraftForge.EVENT_BUS.register(this);
        ModBlocks.initModels();

    }

    @Override
    public void init(FMLInitializationEvent e) {
        super.init(e);
        ModRenderers.init();
//        MinecraftForge.EVENT_BUS.register(this);
//        FMLCommonHandler.instance().bus().register(new KeyInputHandler());
//        KeyBindings.init();
    }

    @Override
    public void postInit(FMLPostInitializationEvent e) {
        super.postInit(e);
    }

    @SubscribeEvent
    public void onModelBakeEvent(ModelBakeEvent event) {
        for (Map.Entry<String, Block> entry : GearSwapperBlock.nameToMimicingBlock.entrySet()) {
            event.modelRegistry.putObject(new ModelResourceLocation(entry.getKey()),
                    new ISmartBlockModel() {
                        private IBakedModel model;

                        @Override
                        public IBakedModel handleBlockState(IBlockState state) {
                            if (model == null) {
                                model = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getModelForState(entry.getValue().getDefaultState());
                                ;
                            }
                            return model;
                        }

                        @Override
                        public List<BakedQuad> getFaceQuads(EnumFacing f) {
                            return model.getFaceQuads(f);
                        }

                        @Override
                        public List<BakedQuad> getGeneralQuads() {
                            return model.getGeneralQuads();
                        }

                        @Override
                        public boolean isAmbientOcclusion() {
                            return model.isAmbientOcclusion();
                        }

                        @Override
                        public boolean isGui3d() {
                            return model.isGui3d();
                        }

                        @Override
                        public boolean isBuiltInRenderer() {
                            return model.isBuiltInRenderer();
                        }

                        @Override
                        public TextureAtlasSprite getParticleTexture() {
                            return model.getParticleTexture();
                        }

                        @Override
                        public ItemCameraTransforms getItemCameraTransforms() {
                            return model.getItemCameraTransforms();
                        }
                    });

        }

    }
}
