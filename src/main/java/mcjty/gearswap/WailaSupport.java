package mcjty.gearswap;

public class WailaSupport {}
//implements IWailaDataProvider {
//
//    public static final WailaSupport INSTANCE = new WailaSupport();
//
//    private WailaSupport() {}
//
//    public static void load(IWailaRegistrar registrar) {
//        registrar.registerHeadProvider(INSTANCE, GearSwapperBlock.class);
//        registrar.registerBodyProvider(INSTANCE, GearSwapperBlock.class);
//        registrar.registerTailProvider(INSTANCE, GearSwapperBlock.class);
//    }
//
//    @Override
//    public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, int x, int y, int z) {
//        return tag;
//    }
//
//    @Override
//    public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
//        return null;
//    }
//
//    @Override
//    public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
//        return currenttip;
//    }
//
//    @Override
//    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
//        Block block = accessor.getBlock();
//        if (block instanceof GearSwapperBlock) {
//            return ((GearSwapperBlock) block).getWailaBody(itemStack, currenttip, accessor, config);
//        }
//        return currenttip;
//    }
//
//    @Override
//    public List<String> getWailaTail(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
//        return currenttip;
//    }
//}