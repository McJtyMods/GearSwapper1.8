package mcjty.gearswap.blocks;

import mcjty.lib.tools.ItemStackTools;
import net.minecraft.item.ItemStack;

class OriginalStackSource implements Source {
    private final ItemStack[] currentStacks;

    public OriginalStackSource(ItemStack[] currentStacks) {
        this.currentStacks = currentStacks;
    }

    @Override
    public int getStackCount() {
        return currentStacks.length;
    }

    @Override
    public ItemStack getStack(int index) {
        return currentStacks[index];
    }

    @Override
    public ItemStack extractAmount(int index, int amount) {
        ItemStack current = currentStacks[index];
        if (amount < ItemStackTools.getStackSize(current)) {
            current = current.copy();
            ItemStackTools.incStackSize(currentStacks[index], -amount);
            current = ItemStackTools.setStackSize(current, amount);
        } else {
            currentStacks[index] = ItemStackTools.getEmptyStack();
        }
        return current;
    }
}
