package mcjty.gearswap.varia;

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
        int itemsToPlace = result.getCount();

        if (result.isStackable()) {
            while (itemsToPlace > 0 && (k < stop)) {
                itemstack1 = inventory.getStackInSlot(k);

                if (isItemStackConsideredEqual(result, itemstack1) && (sidedInventory == null || sidedInventory.canInsertItem(k, result, side))) {
                    int l = itemstack1.getCount() + itemsToPlace;

                    if (l <= result.getMaxStackSize()) {
                        if (undo != null) {
                            // Only put on undo map if the key is not already present.
                            if (!undo.containsKey(k)) {
                                undo.put(k, itemstack1.copy());
                            }
                        }
                        itemsToPlace = 0;
                        ItemStack result1;
                        if (l <= 0) {
                            itemstack1.setCount(0);
                            result1 = ItemStack.EMPTY;
                        } else {
                            itemstack1.setCount(l);
                            result1 = itemstack1;
                        }
                        inventory.markDirty();
                    } else if (itemstack1.getCount() < result.getMaxStackSize()) {
                        if (undo != null) {
                            if (!undo.containsKey(k)) {
                                undo.put(k, itemstack1.copy());
                            }
                        }
                        itemsToPlace -= result.getMaxStackSize() - itemstack1.getCount();
                        int amount = result.getMaxStackSize();
                        ItemStack result1;
                        if (amount <= 0) {
                            itemstack1.setCount(0);
                            result1 = ItemStack.EMPTY;
                        } else {
                            itemstack1.setCount(amount);
                            result1 = itemstack1;
                        }
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

                if (itemstack1.isEmpty() && (sidedInventory == null || sidedInventory.canInsertItem(k, result, side))) {
                    if (undo != null) {
                        if (!undo.containsKey(k)) {
                            undo.put(k, ItemStack.EMPTY);
                        }
                    }
                    ItemStack copy = result.copy();
                    ItemStack result1;
                    if (itemsToPlace <= 0) {
                        copy.setCount(0);
                        result1 = ItemStack.EMPTY;
                    } else {
                        copy.setCount(itemsToPlace);
                        result1 = copy;
                    }
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
        return !itemstack1.isEmpty() && itemstack1.getItem() == result.getItem() && (!result.getHasSubtypes() || result.getItemDamage() == itemstack1.getItemDamage()) && ItemStack.areItemStackTagsEqual(result, itemstack1);
    }
}
