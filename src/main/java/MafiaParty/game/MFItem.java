package MafiaParty.game;

import org.bukkit.inventory.ItemStack;

public class MFItem
{
    public ItemStack Item;
    public int Price;

    public MFItem(ItemStack item, int price) {
        this.Item = item;
        this.Price = price;
    }
}
