package MafiaParty.listeners;

import MafiaParty.game.MFPlayer;
import MafiaParty.managers.InventoryManager;
import MafiaParty.managers.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerListener implements Listener
{
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        MFPlayer mfPlayer = PlayerManager.getInstance().getMFPlayer(player);
        if (mfPlayer == null) return;

        ItemStack item = player.getInventory().getItemInMainHand();
        Material mat = item.getType();
        if (mat != Material.NETHER_STAR) return;

        if (!mfPlayer.hasTurn()) {
            mfPlayer.sendMessage(ChatColor.RED+"Ce n'est pas votre tour !");
            return;
        }

        String itemName = ChatColor.stripColor(item.getItemMeta().getDisplayName());

        if (itemName.equalsIgnoreCase("Menu")) {
            mfPlayer.openInventory(InventoryManager.getInstance().getPlayerMenuInventory());
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void OnPlayerDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        if (player.getGameMode() == GameMode.CREATIVE) return;
        event.setCancelled(true);
    }
}
