package mcjty.gearswap.varia;

//import codechicken.lib.inventory.InventoryUtils;

import mcjty.lib.inventory.CompatInventory;
import mcjty.lib.inventory.InventoryTools;
import mcjty.lib.tools.ItemStackTools;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;

/**
 * This code is copied from EnderIO. Thanks!
 * It is used to solve some problems with the baubles api (the
 * inventory misbehaving).
 */
public class ShadowInventory implements CompatInventory {
    private final ItemStack[] items;
    private final IInventory master;

    public ShadowInventory(IInventory master) {
        this.master = master;
        items = new ItemStack[master.getSizeInventory()];
        for (int i = 0; i < master.getSizeInventory(); i++) {
            items[i] = master.getStackInSlot(i);
        }
    }

    @Override
    public int getSizeInventory() {
        return master.getSizeInventory();
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return items[index];
    }

    @Override
    public ItemStack decrStackSize(int index, int amount) {
        return decrStackSize(this, index, amount);
    }

    public static ItemStack decrStackSize(IInventory inv, int slot, int size) {
        ItemStack item = inv.getStackInSlot(slot);
        if(ItemStackTools.isValid(item)) {
            if(ItemStackTools.getStackSize(item) <= size) {
                inv.setInventorySlotContents(slot, ItemStackTools.getEmptyStack());
                inv.markDirty();
                return item;
            } else {
                ItemStack itemstack1 = item.splitStack(size);
                if(ItemStackTools.isEmpty(item)) {
                    inv.setInventorySlotContents(slot, ItemStackTools.getEmptyStack());
                } else {
                    inv.setInventorySlotContents(slot, item);
                }

                inv.markDirty();
                return itemstack1;
            }
        } else {
            return null;
        }
    }


    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        items[index] = stack;
    }

    @Override
    public int getInventoryStackLimit() {
        return master.getInventoryStackLimit();
    }

    @Override
    public void markDirty() {
    }

    @Override
    public boolean isUsable(EntityPlayer player) {
        return InventoryTools.isUsable(player, master);
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return master.isItemValidForSlot(index, stack);
    }

    @Override
    public void clear() {
        master.clear();
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        ItemStack stack = items[index];
        items[index] = null;
        return stack;
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
}