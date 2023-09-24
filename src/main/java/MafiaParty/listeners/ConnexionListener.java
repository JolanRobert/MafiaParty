package MafiaParty.listeners;

import MafiaParty.managers.PlayerManager;
import MafiaParty.managers.ScoreboardManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ConnexionListener implements Listener
{
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        event.setJoinMessage(ChatColor.DARK_GRAY+"["+ChatColor.GREEN+"+"+ChatColor.DARK_GRAY+"] "+ChatColor.GRAY+player.getName());

        player.setScoreboard(ScoreboardManager.getInstance().getScoreboard());
        PlayerManager.getInstance().addPlayer(player);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        event.setQuitMessage(ChatColor.DARK_GRAY+"["+ChatColor.RED+"-"+ChatColor.DARK_GRAY+"] "+ChatColor.GRAY+player.getName());

        PlayerManager.getInstance().removePlayer(player);
    }
}
