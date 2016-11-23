package mcjty.gearswap.blocks;

import mcjty.gearswap.GearSwap;
import mcjty.gearswap.compat.top.TOPInfoProvider;
import mcjty.gearswap.compat.waila.WailaInfoProvider;
import mcjty.gearswap.network.PacketHandler;
import mcjty.gearswap.network.PacketRememberSetup;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcjty.lib.compat.CompatBlock;
import mcjty.lib.tools.ChatTools;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public class GearSwapperBlock extends CompatBlock implements ITileEntityProvider, WailaInfoProvider, TOPInfoProvider {
    public static final PropertyDirection FACING = PropertyDirection.create("facing");

    public GearSwapperBlock(Material material, String blockName) {
        super(material);
        setUnlocalizedName(blockName);
        setRegistryName(blockName);
        setHardness(2.0f);
        setHarvestLevel("pickaxe", 0);
        setCreativeTab(CreativeTabs.MISC);
        setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
        GameRegistry.register(this);
        GameRegistry.register(new ItemBlock(this), getRegistryName());
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }

    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return new GearSwapperTE();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, EntityPlayer player, List<String> list, boolean whatIsThis) {
        list.add("This block can remember four different sets of tools, weapons");
        list.add("and armor and allows you to quickly switch between them.");
        list.add("Sneak-left-click to store current hotbar+armor in slot.");
        list.add("Right-click on slot to restore hotbar+armor.");
        list.add("Right-click on bottom to open GUI.");
    }


    public static int getSlot(RayTraceResult mouseOver, World world) {
        return getSlot(world, mouseOver.getBlockPos(), mouseOver.sideHit, mouseOver.hitVec);
    }

    public static int getSlot(World world, BlockPos blockPos, EnumFacing sideHit, Vec3d hitVec) {
        int x = blockPos.getX();
        int y = blockPos.getY();
        int z = blockPos.getZ();
        EnumFacing k = getOrientation(world, blockPos);
        if (sideHit == k) {
            float sx = (float) (hitVec.xCoord - x);
            float sy = (float) (hitVec.yCoord - y);
            float sz = (float) (hitVec.zCoord - z);
            return calculateHitIndex(sx, sy, sz, k);
        } else {
            return -1;
        }
    }

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
        int index = getSlot(world, data.getPos(), data.getSideHit(), data.getHitVec());
        if (index == -1) {
            probeInfo.text("Right-click to access GUI");
        } else {
            probeInfo.text(TextFormatting.YELLOW + "Sneak-left-click:" + TextFormatting.WHITE + " store current setup in slot");
            probeInfo.text(TextFormatting.YELLOW + "Right-click:" + TextFormatting.WHITE + " restore current setup from slot");
        }
    }

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        RayTraceResult mouseOver = accessor.getMOP();
        int index = getSlot(mouseOver, accessor.getWorld());
        if (index == -1) {
            currenttip.add("Right-click to access GUI");
        } else {
            currenttip.add("Sneak-left-click: store current setup in slot");
            currenttip.add("Right-click: restore current setup from slot");
        }
        return currenttip;
    }


    @Override
    public void onBlockClicked(World world, BlockPos pos, EntityPlayer player) {
        if (world.isRemote && player.isSneaking()) {
            // On client. We find out what part of the block was hit and send that to the server.
            RayTraceResult mouseOver = Minecraft.getMinecraft().objectMouseOver;
            int index = getSlot(mouseOver, world);
            if (index >= 0) {
                PacketHandler.INSTANCE.sendToServer(new PacketRememberSetup(pos, index));
            }
        }
    }

    @Override
    protected boolean clOnBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float sx, float sy, float sz) {
        if (!world.isRemote) {
            EnumFacing k = getOrientation(world, pos);
            if (side == k) {
                TileEntity tileEntity = world.getTileEntity(pos);
                if (tileEntity instanceof GearSwapperTE) {
                    GearSwapperTE gearSwapperTE = (GearSwapperTE) tileEntity;
                    int index = calculateHitIndex(sx, sy, sz, k);

                    if (index == -1) {
                        player.openGui(GearSwap.instance, GearSwap.GUI_GEARSWAP, world, pos.getX(), pos.getY(), pos.getZ());
                        return true;
                    }

                    gearSwapperTE.restoreSetup(index, player);
                    ChatTools.addChatMessage(player, new TextComponentString(TextFormatting.YELLOW + "Restored hotbar and armor"));
                }
            } else {
                player.openGui(GearSwap.instance, GearSwap.GUI_GEARSWAP, world, pos.getX(), pos.getY(), pos.getZ());
            }
        }
        return true;
    }

    private static int calculateHitIndex(float sx, float sy, float sz, EnumFacing k) {
        int index = -1;
        switch (k) {
            case DOWN:
                if (sz < .13) {
                    return -1;
                }
                index = (sx > .5 ? 1 : 0) + (sz < .54 ? 2 : 0);
                break;
            case UP:
                if (sz > 1-.13) {
                    return -1;
                }
                index = (sx > .5 ? 1 : 0) + (sz > .54 ? 2 : 0);
                break;
            case NORTH:
                if (sy < .13) {
                    return -1;
                }
                index = (sx < .5 ? 1 : 0) + (sy < .54 ? 2 : 0);
                break;
            case SOUTH:
                if (sy < .13) {
                    return -1;
                }
                index = (sx > .5 ? 1 : 0) + (sy < .54 ? 2 : 0);
                break;
            case WEST:
                if (sy < .13) {
                    return -1;
                }
                index = (sz > .5 ? 1 : 0) + (sy < .54 ? 2 : 0);
                break;
            case EAST:
                if (sy < .13) {
                    return -1;
                }
                index = (sz < .5 ? 1 : 0) + (sy < .54 ? 2 : 0);
                break;
        }
        return index;
    }


    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entityLivingBase, ItemStack itemStack) {
        world.setBlockState(pos, state.withProperty(FACING, getFacingFromEntity(pos, entityLivingBase)), 2);

        NBTTagCompound tagCompound = itemStack.getTagCompound();
        if (tagCompound != null) {
            TileEntity te = world.getTileEntity(pos);
            if (te instanceof GearSwapperTE) {
                ((GearSwapperTE)te).readRestorableFromNBT(tagCompound);
            }
        }
    }

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        TileEntity tileEntity = world.getTileEntity(pos);

        if (tileEntity instanceof GearSwapperTE) {
            ItemStack stack = new ItemStack(Item.getItemFromBlock(this));
            NBTTagCompound tagCompound = new NBTTagCompound();
            ((GearSwapperTE)tileEntity).writeRestorableToNBT(tagCompound);

            stack.setTagCompound(tagCompound);
            List<ItemStack> result = new ArrayList<ItemStack>();
            result.add(stack);
            return result;
        } else {
            return super.getDrops(world, pos, state, fortune);
        }
    }

    @Override
    public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
        if (willHarvest) return true; // If it will harvest, delay deletion of the block until after getDrops
        return super.removedByPlayer(state, world, pos, player, willHarvest);
    }

    @Override
    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te, ItemStack stack) {
        super.harvestBlock(worldIn, player, pos, state, te, stack);
        worldIn.setBlockToAir(pos);
    }


    private static EnumFacing getOrientation(IBlockAccess world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        return state.getValue(FACING);
    }

    public static EnumFacing getFacingFromEntity(BlockPos clickedBlock, EntityLivingBase entityIn) {
        if (MathHelper.abs((float) entityIn.posX - clickedBlock.getX()) < 2.0F && MathHelper.abs((float) entityIn.posZ - clickedBlock.getZ()) < 2.0F) {
            double d0 = entityIn.posY + (double) entityIn.getEyeHeight();

            if (d0 - (double) clickedBlock.getY() > 2.0D) {
                return EnumFacing.UP;
            }

            if ((double) clickedBlock.getY() - d0 > 0.0D) {
                return EnumFacing.DOWN;
            }
        }

        return entityIn.getHorizontalFacing().getOpposite();
    }



    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }


    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(FACING, getFacing(meta));
    }

    public static EnumFacing getFacing(int meta) {
        int i = meta & 7;
        return i > 5 ? null : EnumFacing.getFront(i);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getIndex();
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING);
    }


}
