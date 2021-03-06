package mcjty.gearswap.blocks;

import mcjty.gearswap.ConfigSetup;
import mcjty.gearswap.items.ModItems;
import mcjty.gearswap.setup.ModSetup;
import mcjty.gearswap.varia.InventoryHelper;
import mcjty.gearswap.varia.ItemStackList;
import mcjty.gearswap.varia.Tools;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.wrapper.InvWrapper;

import javax.annotation.Nullable;

public class GearSwapperTE extends TileEntity implements ISidedInventory {

    // First 4 slots are the ghost slots for the front icons
    // Next there are 4 times 9+4+1 slots for the remembered states.
    // Then there are 16 slots general inventory.
    // Then we optionally have 4*4 slots for baubles.
    private ItemStackList stacks = ItemStackList.create(4 + 4*(9+4+1) + 16 + 4*4 + 4);

    public static final int SLOT_GHOST = 4;
    public static final int SLOT_BUFFER = SLOT_GHOST + 4*(9+4+1);
    public static final int SLOT_BAUBLES = SLOT_BUFFER + 16;
    public static final int SLOT_GHOSTSHIELD = SLOT_BAUBLES + 4*4;

    public static final int MODE_PLAYERINV = 0;
    public static final int MODE_LOCALINV = 1;
    public static final int MODE_REMOTEINV = 2;

    private int exportModes[] = new int[] { MODE_PLAYERINV, MODE_LOCALINV, MODE_REMOTEINV };


    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        readRestorableFromNBT(tagCompound);
    }

    public void readRestorableFromNBT(NBTTagCompound tagCompound) {
        readBufferFromNBT(tagCompound);
        exportModes[0] = tagCompound.getInteger("export0");
        exportModes[1] = tagCompound.getInteger("export1");
        exportModes[2] = tagCompound.getInteger("export2");
    }

    private void readBufferFromNBT(NBTTagCompound tagCompound) {
        NBTTagList bufferTagList = tagCompound.getTagList("Items", Constants.NBT.TAG_COMPOUND);
        for (int i = 0 ; i < bufferTagList.tagCount() ; i++) {
            NBTTagCompound nbtTagCompound = bufferTagList.getCompoundTagAt(i);
            setStackInSlot(i, new ItemStack(nbtTagCompound));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        writeRestorableToNBT(tagCompound);
        return tagCompound;
    }

    public void writeRestorableToNBT(NBTTagCompound tagCompound) {
        writeBufferToNBT(tagCompound);
        tagCompound.setInteger("export0", exportModes[0]);
        tagCompound.setInteger("export1", exportModes[1]);
        tagCompound.setInteger("export2", exportModes[2]);
    }

    private void writeBufferToNBT(NBTTagCompound tagCompound) {
        NBTTagList bufferTagList = new NBTTagList();
        for (int i = 0 ; i < stacks.size() ; i++) {
            ItemStack stack = getStackInSlot(i);
            NBTTagCompound nbtTagCompound = new NBTTagCompound();
            if (!stack.isEmpty()) {
                stack.writeToNBT(nbtTagCompound);
            }
            bufferTagList.appendTag(nbtTagCompound);
        }
        tagCompound.setTag("Items", bufferTagList);
    }

    public int getExportMode(int i) {
        return exportModes[i];
    }

    public void toggelExportMode(int i) {
        exportModes[i]++;
        if (exportModes[i] > MODE_REMOTEINV) {
            exportModes[i] = MODE_PLAYERINV;
        }
        markDirty();
        IBlockState state = getWorld().getBlockState(pos);
        getWorld().notifyBlockUpdate(pos, state, state, 3);
    }

    public void setFaceIconSlot(int index, ItemStack stack) {
        setInventorySlotContents(index, stack);
        IBlockState state = getWorld().getBlockState(pos);
        getWorld().notifyBlockUpdate(pos, state, state, 3);
    }

    // Get total player inventory count. This is 9+4+1 (hotbar+armor+shield) without baubles
    // and 9+4+1+4 with baubles.
    private int getPlayerInventorySize() {
        if (ModSetup.baubles) {
            return 9+4+1+4;
        } else {
            return 9+4+1;
        }
    }

    // Virtual inventory index. From 0 to 9 is hotbar. The four slots after that are armor. The slot after that is shield
    private int getInventoryIndex(int i) {
        if (i < 9) {
            return i;
        } else if (i == 13) {
            return 40;
        } else {
            return (i-9) + 36;
        }
    }

    private ItemStack getStackFromPlayerInventory(int index, EntityPlayer player) {
        if (index < 9+4+1) {
            return player.inventory.getStackInSlot(getInventoryIndex(index));
        } else if (ModSetup.baubles) {
            IInventory baubles = Tools.getBaubles(player);
            if (baubles != null) {
                return baubles.getStackInSlot(index - (9+4+1));
            }
        }
        return ItemStack.EMPTY;
    }

    private void putStackInPlayerInventory(int index, EntityPlayer player, ItemStack stack) {
        if (index < 9+4+1) {
            player.inventory.setInventorySlotContents(getInventoryIndex(index), stack);
        } else if (ModSetup.baubles) {
            IInventory baubles = Tools.getBaubles(player);
            if (baubles != null) {
                baubles.setInventorySlotContents(index - (9+4+1), stack);
            }
        }
    }

    // Get the internal slot where we keep a ghost copy of the player inventory item.
    private int getInternalInventoryIndex(int index, int i) {
        if (i >= 9+4+1) {
            // We have a baubles slot.
            return SLOT_BAUBLES + index * 4 + (i-(9+4+1));
        } else {
            return 4 + index * (9 + 4 + 1) + i;
        }
    }

    public void rememberSetup(int index, EntityPlayer player) {
        setFaceIconSlot(index, player.getHeldItem(EnumHand.MAIN_HAND));

        for (int i = 0 ; i < getPlayerInventorySize() ; i++) {
            ItemStack stack = getStackFromPlayerInventory(i, player);
            if (!stack.isEmpty() && stack.getCount() == 0) {
                // For some weird reason it seems baubles can have a 0 stacksize? (can't happen on 1.11)
                stack = stack.copy();
            }
            setInventorySlotContents(getInternalInventoryIndex(index, i), stack);
        }
    }

    public void restoreSetup(int index, EntityPlayer player) {
        InventoryPlayer inventory = player.inventory;

        // Set aside the current hotbar + armor slots (+ optional baubles slots).
        ItemStack[] currentStacks = new ItemStack[getPlayerInventorySize()];
        for (int i = 0 ; i < getPlayerInventorySize() ; i++) {
            currentStacks[i] = getStackFromPlayerInventory(i, player);
            putStackInPlayerInventory(i, player, new ItemStack(ModItems.forceEmptyItem));
        }

        // Find stacks in all possible sources to replace the current selection
        for (int i = 0 ; i < getPlayerInventorySize() ; i++) {
            ItemStack desiredStack = getStackInSlot(getInternalInventoryIndex(index, i));
            if ((desiredStack.isEmpty()) || desiredStack.getItem() == ModItems.forceEmptyItem) {
                // Either we don't have specific needs for this slot or we want it to be cleared.
                // In both cases we keep the slot empty here.
            } else {
                ItemStack foundStack = findBestMatchingStack(desiredStack, currentStacks, inventory);
                // Can be that we didn't find anything. In any case, we simply put whatever we found in the slot.
                putStackInPlayerInventory(i, player, foundStack);
            }
        }

        // First we check all slots that we don't need to be cleared and we put back the item
        // from currentStacks if that's still there. In all other slots we temporarily set
        // our dummy item so that we don't accidently overwrite that in the next step.
        for (int i = 0 ; i < getPlayerInventorySize() ; i++) {
            ItemStack stack = getStackFromPlayerInventory(i, player);
            if (stack.isEmpty() || stack.getItem() == ModItems.forceEmptyItem) {
                if (!currentStacks[i].isEmpty() && currentStacks[i].getItem() != ModItems.forceEmptyItem) {
                    int internalInventoryIndex = getInternalInventoryIndex(index, i);
                    ItemStack desiredStack = getStackInSlot(internalInventoryIndex);
                    // First check if we don't want to force the slot to be empty
                    if ((desiredStack.isEmpty()) || desiredStack.getItem() != ModItems.forceEmptyItem) {
                        putStackInPlayerInventory(i, player, currentStacks[i]);
                        currentStacks[i] = ItemStack.EMPTY;
                    }
                }
            }
        }

        // All items that we didn't find a place for we need to back somewhere.
        for (int i = 0 ; i < getPlayerInventorySize() ; i++) {
            if (!currentStacks[i].isEmpty()) {
                if (storeItem(inventory, currentStacks[i])) {
                    currentStacks[i] = ItemStack.EMPTY;
                }
            }
        }

        // Now we clear the dummy items from our slots.
        for (int i = 0 ; i < getPlayerInventorySize() ; i++) {
            ItemStack stack = getStackFromPlayerInventory(i, player);
            if (!stack.isEmpty() && stack.getItem() == ModItems.forceEmptyItem) {
                putStackInPlayerInventory(i, player, ItemStack.EMPTY);
            }
        }

        // Now it is possible that some of the items we couldn't place back because the slots in the hotbar
        // were locked. Now that they are unlocked we can try again.
        for (int i = 0 ; i < getPlayerInventorySize() ; i++) {
            if (!currentStacks[i].isEmpty()) {
                if (inventory.addItemStackToInventory(currentStacks[i])) {
                    currentStacks[i] = ItemStack.EMPTY;
                }
            }
        }

        // Finally it is possible that some items could not be placed anywhere.
        for (int i = 0 ; i < getPlayerInventorySize() ; i++) {
            if (!currentStacks[i].isEmpty()) {
                EntityItem entityItem = new EntityItem(getWorld(), pos.getX(), pos.getY(), pos.getZ(), currentStacks[i]);
                getWorld().spawnEntity(entityItem);
            }
        }

        markDirty();
        player.openContainer.detectAndSendChanges();
    }

    private boolean storeItem(InventoryPlayer inventory, ItemStack item) {
        for (int exportMode : exportModes) {
            switch (exportMode) {
                case MODE_PLAYERINV:
                    if (inventory.addItemStackToInventory(item)) {
                        return true;
                    }
                    break;
                case MODE_LOCALINV: {
                    int left = InventoryHelper.mergeItemStack(this, item, SLOT_BUFFER, SLOT_BUFFER + 16, null);
                    if (left == 0) {
                        return true;
                    }
                    ItemStack result;
                    if (left <= 0) {
                        item.setCount(0);
                        result = ItemStack.EMPTY;
                    } else {
                        item.setCount(left);
                        result = item;
                    }
                    break;
                }
                case MODE_REMOTEINV:
                    for (EnumFacing facing : EnumFacing.values()) {
                        TileEntity te = getWorld().getTileEntity(pos.offset(facing));
                        if (te != null && te.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing.getOpposite())) {
                            IItemHandler capability = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing.getOpposite());
                            ItemStack stack = ItemHandlerHelper.insertItem(capability, item, false);
                            if (stack.isEmpty()) {
                                return true;
                            }
                            item = stack;

                        } else if (te instanceof IInventory) {
                            IInventory otherInventory = (IInventory) te;
                            int left = InventoryHelper.mergeItemStackSafe(otherInventory, facing.getOpposite(), item, 0, otherInventory.getSizeInventory(), null);
                            if (left == 0) {
                                return true;
                            }
                            ItemStack result;
                            if (left <= 0) {
                                item.setCount(0);
                                result = ItemStack.EMPTY;
                            } else {
                                item.setCount(left);
                                result = item;
                            }
                        }
                    }
                    break;
            }
        }
        return false;
    }

    private ItemStack findBestMatchingStack(ItemStack desired, ItemStack[] currentStacks, InventoryPlayer inventoryPlayer) {
        ItemStack bestSoFar = ItemStack.EMPTY;
        desired = desired.copy();

        while (!desired.isEmpty()) {
            ItemStack stack = findBestMatchingStackWithScore(desired, currentStacks, inventoryPlayer, bestSoFar);
            if (stack.isEmpty()) {
                return bestSoFar;
            }
            if (!bestSoFar.isEmpty()) {
                bestSoFar.grow(stack.getCount());
            } else {
                bestSoFar = stack;
            }
            int amount = -stack.getCount();
            desired.grow(amount);
        }
        return bestSoFar;
    }

    private class BestScore {
        public int score = -1;
        public Source source = null;
        public int index = -1;
    }

    /**
     * Find the best matching item. If 'bestMatch' is already given then we already found one before and so we
     * now need an exact match for remaining items.
     * @param desired
     * @param source
     * @param bestScore
     * @param bestMatch
     */
    private void findBestMatchingStackWithScore(ItemStack desired, Source source, BestScore bestScore, ItemStack bestMatch) {
        for (int i = 0 ; i < source.getStackCount() ; i++) {
            ItemStack current = source.getStack(i);
            if (!bestMatch.isEmpty() && !current.isEmpty()) {
                if (!bestMatch.isItemEqual(current)) {
                    continue;
                }
                if (!ItemStack.areItemStackTagsEqual(bestMatch, current)) {
                    continue;
                }
            }
            int score = calculateMatchingScore(desired, current);
            if (score > bestScore.score) {
                bestScore.score = score;
                bestScore.source = source;
                bestScore.index = i;
            }
        }
    }

    private ItemStack findBestMatchingStackWithScore(final ItemStack desired, final ItemStack[] currentStacks, final InventoryPlayer inventoryPlayer, ItemStack bestMatch) {
        final BestScore bestScore = new BestScore();
        findBestMatchingStackWithScore(desired, new OriginalStackSource(currentStacks), bestScore, bestMatch);
        findBestMatchingStackWithScore(desired, new PlayerSource(inventoryPlayer), bestScore, bestMatch);
        findBestMatchingStackWithScore(desired, new InternalSource(this), bestScore, bestMatch);

        // Check external inventories.
        for (EnumFacing facing : EnumFacing.values()) {
            TileEntity te = getWorld().getTileEntity(pos.offset(facing));
            if (te instanceof IInventory) {
                findBestMatchingStackWithScore(desired, new ExternalInventorySource((IInventory) te, facing), bestScore, bestMatch);
            }
        }

        if (bestScore.source != null) {
            return bestScore.source.extractAmount(bestScore.index, desired.getCount());
        }
        return ItemStack.EMPTY;
    }



    private int calculateMatchingScore(ItemStack desired, ItemStack current) {
        if (current.isEmpty()) {
            return -1;
        }

        if (ItemStack.areItemStackTagsEqual(desired, current) && desired.isItemEqual(current)) {
            return 1000;
        }

        if (desired.getItem().equals(current.getItem()) && desired.getTagCompound() != null && current.getTagCompound() != null) {
            if (itemsMatchForSpecificTags(desired, current)) {
                return 700;
            }
        }

        if (desired.isItemEqual(current)) {
            return 500;
        }

        if (desired.getItem().equals(current.getItem())) {
            return 200;
        }
        return -1;
    }

    private boolean itemsMatchForSpecificTags(ItemStack desired, ItemStack current) {
        if (ConfigSetup.tagsThatHaveToMatch.containsKey(desired.getUnlocalizedName())) {
            String[] tags = ConfigSetup.tagsThatHaveToMatch.get(desired.getUnlocalizedName());
            boolean ok = true;
            for (String tag : tags) {
                NBTBase tag1 = desired.getTagCompound().getTag(tag);
                NBTBase tag2 = current.getTagCompound().getTag(tag);
                if (tag1 == null && tag2 != null) {
                    ok = false;
                    break;
                } else if (tag1 != null && tag2 == null) {
                    ok = false;
                    break;
                } else if (tag1 != null && !tag1.equals(tag2)) {
                    ok = false;
                    break;
                }
            }
            if (ok) {
                return true;
            }
        }
        return false;
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return writeToNBT(new NBTTagCompound());
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound nbtTag = new NBTTagCompound();
        this.writeToNBT(nbtTag);
        return new SPacketUpdateTileEntity(pos, 1, nbtTag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
        readFromNBT(packet.getNbtCompound());
    }

    // IInventory

    @Override
    public int getSizeInventory() {
        return stacks.size();
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        if (index >= stacks.size()) {
            return ItemStack.EMPTY;
        }

        return stacks.get(index);
    }

    public boolean isGhostSlot(int index) {
        return (index >= 0 && index < SLOT_BUFFER) || (ModSetup.baubles && index >= SLOT_BAUBLES && index < SLOT_BAUBLES+16);
    }

    public boolean isIconSlot(int index) {
        return index >= 0 && index < SLOT_GHOST;
    }

    public boolean isBufferSlot(int index) { return index >= SLOT_BUFFER && index < SLOT_BUFFER + 16; }

    @Override
    public ItemStack decrStackSize(int index, int amount) {
        if (index >= stacks.size()) {
            return ItemStack.EMPTY;
        }

        if (isGhostSlot(index)) {
            ItemStack old = stacks.get(index);
            stacks.set(index, ItemStack.EMPTY);
            if (old.isEmpty()) {
                return ItemStack.EMPTY;
            }
            if (0 <= 0) {
                old.setCount(0);
                return ItemStack.EMPTY;
            } else {
                old.setCount(0);
                return old;
            }
        } else {
            if (!stacks.get(index).isEmpty()) {
                if (stacks.get(index).getCount() <= amount) {
                    ItemStack old = stacks.get(index);
                    stacks.set(index, ItemStack.EMPTY);
                    markDirty();
                    return old;
                }
                ItemStack its = stacks.get(index).splitStack(amount);
                if (stacks.get(index).isEmpty()) {
                    stacks.set(index, ItemStack.EMPTY);
                }
                markDirty();
                return its;
            }
            return ItemStack.EMPTY;
        }
    }

    public void setStackInSlot(int index, ItemStack stack) {
        if (index >= stacks.size()) {
            return;
        }
        stacks.set(index, stack);
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        if (index >= stacks.size()) {
            return;
        }

        if (isGhostSlot(index)) {
            if (!stack.isEmpty()) {
                stacks.set(index, stack.copy());
            } else {
                stacks.set(index, ItemStack.EMPTY);
            }
        } else {
            stacks.set(index, stack);
            if (!stack.isEmpty() && stack.getCount() > getInventoryStackLimit()) {
                int amount = getInventoryStackLimit();
                ItemStack result;
                if (amount <= 0) {
                    stack.setCount(0);
                    result = ItemStack.EMPTY;
                } else {
                    stack.setCount(amount);
                    result = stack;
                }
            }
        }
        markDirty();
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return true;
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return !isGhostSlot(index);
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
        return index >= SLOT_BUFFER;
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        return new int[] {
                SLOT_BUFFER, SLOT_BUFFER+1, SLOT_BUFFER+2, SLOT_BUFFER+3,
                SLOT_BUFFER+4, SLOT_BUFFER+5, SLOT_BUFFER+6, SLOT_BUFFER+7,
                SLOT_BUFFER+8, SLOT_BUFFER+9, SLOT_BUFFER+10, SLOT_BUFFER+11,
                SLOT_BUFFER+12, SLOT_BUFFER+13, SLOT_BUFFER+14, SLOT_BUFFER+15
        };
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        return index >= SLOT_BUFFER;
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public void openInventory(EntityPlayer player) {

    }

    @Override
    public void closeInventory(EntityPlayer player) {

    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {

    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {

    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public ITextComponent getDisplayName() {
        return null;
    }

    IItemHandler invHandler = new InvWrapper(this);

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return true;
        }
        return super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, net.minecraft.util.EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(invHandler);
        }
        return super.getCapability(capability, facing);
    }
}
