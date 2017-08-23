package mcjty.gearswap.blocks;

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
        if (amount < current.getCount()) {
            current = current.copy();
            int amount1 = -amount;
            currentStacks[index].grow(amount1);
            ItemStack result;
            if (amount <= 0) {
                current.setCount(0);
                result = ItemStack.EMPTY;
            } else {
                current.setCount(amount);
                result = current;
            }
            current = result;
        } else {
            currentStacks[index] = ItemStack.EMPTY;
        }
        return current;
    }
}
