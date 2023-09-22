package MafiaParty.managers;

import MafiaParty.game.MFItem;
import MafiaParty.utils.ItemEditor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryManager
{
    private static InventoryManager instance;
    public static InventoryManager getInstance() {
        if (instance == null) instance = new InventoryManager();
        return instance;
    }

    private Inventory invShop;

    public InventoryManager() {
        this.createShopInventory();
    }

    public void createShopInventory() {
        this.invShop = Bukkit.createInventory(null, 9, ""+ChatColor.BLACK+ChatColor.BOLD+"Magasin");

        for (MFItem mfItem : Shop.getInstance().getItems()) {
            ItemStack newItem = mfItem.Item.clone();
            ItemEditor.setDisplayName(newItem, newItem.getItemMeta().getDisplayName()+ChatColor.YELLOW+" ("+mfItem.Price+"g)");
            this.invShop.addItem(newItem);
        }

        ItemStack close = new ItemStack(Material.BARRIER);
        ItemEditor.setDisplayName(close, ""+ChatColor.RED+ChatColor.BOLD+"Fermer");
        this.invShop.setItem(8, close);
    }

    public Inventory getShopInventory() {return this.invShop;}
}
