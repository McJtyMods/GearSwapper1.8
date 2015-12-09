package mcjty.gearswap.blocks;

import mcjty.gearswap.GearSwap;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GearSwapperTESR extends TileEntitySpecialRenderer {

    private static final ResourceLocation texture = new ResourceLocation(GearSwap.MODID, "textures/blocks/gearSwapperFront.png");

    private static int xx[] = new int[] { 9, 40, 9, 40 };
    private static int yy[] = new int[] { 7, 7, 36, 36 };

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float partialTicks, int destroyStage) {
        GL11.glPushAttrib(GL11.GL_CURRENT_BIT | GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_ENABLE_BIT | GL11.GL_LIGHTING_BIT | GL11.GL_TEXTURE_BIT);

        MovingObjectPosition mouseOver = Minecraft.getMinecraft().objectMouseOver;
        int index;
        if (mouseOver != null && mouseOver.getBlockPos() == tileEntity.getPos()) {
            index = GearSwapperBlock.getSlot(mouseOver, tileEntity.getWorld());
        } else {
            index = -2;
        }

        Block block = tileEntity.getBlockType();

        GL11.glPushMatrix();
        EnumFacing facing = GearSwapperBlock.getFacing(tileEntity.getBlockMetadata());

        GL11.glTranslatef((float) x + 0.5F, (float) y + 0.75F, (float) z + 0.5F);

        if (facing == EnumFacing.UP) {
            GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
            GL11.glTranslatef(0.0F, 0.0F, -0.68F);
        } else if (facing == EnumFacing.DOWN) {
            GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
            GL11.glTranslatef(0.0F, 0.0F, -.184F);
        } else {
            float rotY = 0.0F;
            if (facing == EnumFacing.NORTH) {
                rotY = 180.0F;
            } else if (facing == EnumFacing.WEST) {
                rotY = 90.0F;
            } else if (facing == EnumFacing.EAST) {
                rotY = -90.0F;
            }
            GL11.glRotatef(-rotY, 0.0F, 1.0F, 0.0F);
            GL11.glTranslatef(0.0F, -0.2500F, -0.4375F);
        }

        GearSwapperTE gearSwapperTE = (GearSwapperTE) tileEntity;

        GL11.glTranslatef(0.0F, 0.0F, 0.9F);

        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_DEPTH_TEST);

        renderSlotHilight(index, block == ModBlocks.ironGearSwapperBlock);
        renderSlots(gearSwapperTE);

//        RenderHelper.disableStandardItemLighting();

        GL11.glPopMatrix();

        GL11.glPopAttrib();
    }

    private void renderSlotHilight(int index, boolean darktext) {
        GL11.glPushMatrix();

        GL11.glTranslatef(-0.5F, 0.5F, 0.04F);
        float factor = 2.0f;
        float f3 = 0.0075F;
        GL11.glScalef(f3 * factor, -f3 * factor, f3);
        GL11.glDisable(GL11.GL_LIGHTING);

        for (int i = 0 ; i < 4 ; i++) {
            Gui.drawRect(xx[i] - 4, yy[i] - 4, xx[i] + 22, yy[i] - 3, 0xff222222);
            Gui.drawRect(xx[i] - 4, yy[i] + 21, xx[i] + 22, yy[i] + 22, 0xff222222);
            Gui.drawRect(xx[i] - 4, yy[i] - 4, xx[i] - 3, yy[i] + 22, 0xff222222);
            Gui.drawRect(xx[i] + 21, yy[i] - 4, xx[i] + 22, yy[i] + 22, 0xff222222);
            Gui.drawRect(xx[i] - 3, yy[i] - 3, xx[i] + 21, yy[i] + 21, index == i ? 0x55666666 : 0x55000000);
        }

        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glTranslatef(-0.5F, 0.5F, 0.06F);
        factor = 1.0f;
        GL11.glScalef(f3 * factor, -f3 * factor, f3);
        FontRenderer fontrenderer = this.getFontRenderer();
        if (darktext) {
            fontrenderer.drawString("Settings...", 10, 120, index == -1 ? 0xff000000 : 0xff666666);
        } else {
            fontrenderer.drawString("Settings...", 10, 120, index == -1 ? 0xffffffff : 0xff888888);
        }

        GL11.glPopMatrix();
    }

    private void renderSlots(GearSwapperTE gearSwapperTE) {
        RenderHelper.enableGUIStandardItemLighting();

        float factor = 2.0f;
        float f3 = 0.0075F;
        GL11.glTranslatef(-0.5F, 0.5F, 0.04F);
        GL11.glScalef(f3 * factor, -f3 * factor, 0.0001f);

        RenderItem itemRender = Minecraft.getMinecraft().getRenderItem();

        for (int i = 0 ; i < 4 ; i++) {
            ItemStack stack = gearSwapperTE.getStackInSlot(i);
            if (stack != null) {
                itemRender.renderItemAndEffectIntoGUI(stack, xx[i], yy[i]);
            }
        }
    }

}
