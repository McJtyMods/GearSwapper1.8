package mcjty.gearswap.varia;

import mcjty.lib.tools.ItemStackTools;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

import java.util.Map;

public class InventoryHelper {
    /**
     * Merges provided ItemStack with the first available one in this inventory. It will return the amount
     * of items that could not be merged. Also fills the undo buffer in case you want to undo the operation.
     * This version also checks for ISidedInventory if that's implemented by the inventory
     */
    public static int mergeItemStackSafe(IInventory inventory, EnumFacing side, ItemStack result, int start, int stop, Map<Integer,ItemStack> undo) {
        if (inventory instanceof ISidedInventory) {
            return mergeItemStackInternal(inventory, (ISidedInventory) inventory, side, result, start, stop, undo);
        } else {
            return mergeItemStackInternal(inventory, null, side, result, start, stop, undo);
        }
    }

    /**
     * Merges provided ItemStack with the first available one in this inventory. It will return the amount
     * of items that could not be merged. Also fills the undo buffer in case you want to undo the operation.
     */
    public static int mergeItemStack(IInventory inventory, ItemStack result, int start, int stop, Map<Integer,ItemStack> undo) {
        return mergeItemStackInternal(inventory, null, null, result, start, stop, undo);
    }

    private static int mergeItemStackInternal(IInventory inventory, ISidedInventory sidedInventory, EnumFacing side, ItemStack result, int start, int stop, Map<Integer,ItemStack> undo) {
        int k = start;

        ItemStack itemstack1;
        int itemsToPlace = ItemStackTools.getStackSize(result);

        if (result.isStackable()) {
            while (itemsToPlace > 0 && (k < stop)) {
                itemstack1 = inventory.getStackInSlot(k);

                if (isItemStackConsideredEqual(result, itemstack1) && (sidedInventory == null || sidedInventory.canInsertItem(k, result, side))) {
                    int l = ItemStackTools.getStackSize(itemstack1) + itemsToPlace;

                    if (l <= result.getMaxStackSize()) {
                        if (undo != null) {
                            // Only put on undo map if the key is not already present.
                            if (!undo.containsKey(k)) {
                                undo.put(k, itemstack1.copy());
                            }
                        }
                        itemsToPlace = 0;
                        ItemStackTools.setStackSize(itemstack1, l);
                        inventory.markDirty();
                    } else if (ItemStackTools.getStackSize(itemstack1) < result.getMaxStackSize()) {
                        if (undo != null) {
                            if (!undo.containsKey(k)) {
                                undo.put(k, itemstack1.copy());
                            }
                        }
                        itemsToPlace -= result.getMaxStackSize() - ItemStackTools.getStackSize(itemstack1);
                        ItemStackTools.setStackSize(itemstack1, result.getMaxStackSize());
                        inventory.markDirty();
                    }
                }

                ++k;
            }
        }

        if (itemsToPlace > 0) {
            k = start;

            while (k < stop) {
                itemstack1 = inventory.getStackInSlot(k);

                if (ItemStackTools.isEmpty(itemstack1) && (sidedInventory == null || sidedInventory.canInsertItem(k, result, side))) {
                    if (undo != null) {
                        if (!undo.containsKey(k)) {
                            undo.put(k, ItemStackTools.getEmptyStack());
                        }
                    }
                    ItemStack copy = result.copy();
                    ItemStackTools.setStackSize(copy, itemsToPlace);
                    inventory.setInventorySlotContents(k, copy);
                    inventory.markDirty();
                    itemsToPlace = 0;
                    break;
                }

                ++k;
            }
        }

        return itemsToPlace;
    }

    private static boolean isItemStackConsideredEqual(ItemStack result, ItemStack itemstack1) {
        return ItemStackTools.isValid(itemstack1) && itemstack1.getItem() == result.getItem() && (!result.getHasSubtypes() || result.getItemDamage() == itemstack1.getItemDamage()) && ItemStack.areItemStackTagsEqual(result, itemstack1);
    }
}
