package mcjty.gearswap.blocks;

import mcjty.lib.tools.ItemStackTools;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

class ExternalInventorySource implements Source {
    private final IInventory otherInventory;
    private final EnumFacing direction;

    public ExternalInventorySource(IInventory otherInventory, EnumFacing direction) {
        this.otherInventory = otherInventory;
        this.direction = direction;
    }

    @Override
    public int getStackCount() {
        return otherInventory.getSizeInventory();
    }

    @Override
    public ItemStack getStack(int index) {
        ItemStack current = otherInventory.getStackInSlot(index);
        final ISidedInventory sidedInventory;
        if (otherInventory instanceof ISidedInventory) {
            sidedInventory = (ISidedInventory) otherInventory;
        } else {
            sidedInventory = null;
        }
        if (sidedInventory != null && current != null) {
            if (!sidedInventory.canExtractItem(index, current, direction.getOpposite())) {
                return null;
            }
        }
        return current;
    }

    @Override
    public ItemStack extractAmount(int index, int amount) {
        ItemStack current = otherInventory.getStackInSlot(index);
        if (amount < ItemStackTools.getStackSize(current)) {
            current = otherInventory.decrStackSize(index, amount);
        } else {
            otherInventory.setInventorySlotContents(index, ItemStackTools.getEmptyStack());
        }
        return current;
    }
}
