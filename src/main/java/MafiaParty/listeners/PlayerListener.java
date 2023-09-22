package MafiaParty.listeners;

import MafiaParty.game.MFPlayer;
import MafiaParty.managers.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
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
        if (item.getType() != Material.NETHER_STAR) return;

        mfPlayer.useItem(item);

        String itemName = item.getItemMeta().getDisplayName();
        if (itemName.contains("RDV sous le bureau")) {
            Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE+mfPlayer.getName()+" choisit le résultat de son dé");
        }
        else if (itemName.contains("Téléphone du bossó")) {
            Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE+mfPlayer.getName()+" échange sa position avec le joueur de son choix");
        }
        else if (itemName.contains("DU-DUEL")) {
            Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE+mfPlayer.getName()+" défi le joueur de son choix pour la prochaine musique");
        }
        else if (itemName.contains("Another One")) {
            Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE+mfPlayer.getName()+" lance un dé supplémentaire pour ce tour");
        }

        event.setCancelled(true);
    }
}
