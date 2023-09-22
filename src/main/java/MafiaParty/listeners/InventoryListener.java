package MafiaParty.listeners;

import MafiaParty.game.MFPlayer;
import MafiaParty.managers.InventoryManager;
import MafiaParty.managers.PlayerManager;
import MafiaParty.managers.Shop;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class InventoryListener implements Listener
{
    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) return;
        if (event.getClickedInventory().getItem(event.getSlot()) == null) return;

        Player player = (Player)event.getWhoClicked();
        MFPlayer mfPlayer = PlayerManager.getInstance().getMFPlayer(player);
        ItemStack clickedItem = event.getClickedInventory().getItem(event.getSlot());

        //Config Inventory
        if (event.getInventory() == InventoryManager.getInstance().getShopInventory()) {
            Material itemType = clickedItem.getType();

            if (itemType == Material.BARRIER) player.closeInventory();
            else if (itemType == Material.NETHER_STAR) Shop.getInstance().buyItem(clickedItem, mfPlayer);

            event.setCancelled(true);
        }
    }
}
