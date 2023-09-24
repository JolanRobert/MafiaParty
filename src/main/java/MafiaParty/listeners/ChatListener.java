package MafiaParty.listeners;

import MafiaParty.game.MFPlayer;
import MafiaParty.managers.AnswerManager;
import MafiaParty.managers.PlayerManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener
{
    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        MFPlayer mfPlayer = PlayerManager.getInstance().getMFPlayer(player);
        String msg = event.getMessage();

        if (mfPlayer == null) return;
        AnswerManager am = AnswerManager.getInstance();
        if (!am.isActive()) return;

        player.sendMessage(ChatColor.GOLD+"["+ChatColor.YELLOW+player.getName()+ChatColor.GOLD+" | "+ChatColor.YELLOW+am.getTime()+"s"+ChatColor.GOLD+"] "+ChatColor.GREEN+msg);

        am.addMessage(player, msg);
        event.setCancelled(true);
    }
}
