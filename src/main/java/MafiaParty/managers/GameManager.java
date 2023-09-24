package MafiaParty.managers;

import MafiaParty.game.MFPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.*;

public class GameManager
{
    private static GameManager instance;
    public static GameManager getInstance() {
        if (instance == null) instance = new GameManager();
        return instance;
    }

    private Queue<MFPlayer> turnOrder = new LinkedList<>();
    private MFPlayer currentPlayer;

    public void randomizeTurnOrder() {
        currentPlayer = null;
        List<MFPlayer> players = new ArrayList<>(PlayerManager.getInstance().getMFPlayers());
        Collections.shuffle(players);
        StringBuilder msg = new StringBuilder(ChatColor.GOLD + "= Ordre du tour =\n");
        for (MFPlayer mfp : players) {
            msg.append(mfp.getName()+ChatColor.GREEN+" > ");
            mfp.endTurn();
            turnOrder.add(mfp);
        }

        Bukkit.broadcastMessage(msg.substring(0,msg.length()-3));
        nextTurn();
    }

    public void forceTurn(MFPlayer mfPlayer) {
        if (turnOrder == null) return;

        while (turnOrder.peek() != mfPlayer) {
            turnOrder.add(turnOrder.remove());
            currentPlayer.endTurn();
        }

        currentPlayer = turnOrder.peek();
        Bukkit.broadcastMessage(ChatColor.RED+"= Début forcé du tour de "+currentPlayer.getName()+ChatColor.RED+" =");
        currentPlayer.beginTurn();
    }

    public void nextTurn() {
        if (currentPlayer != null) {
            Bukkit.broadcastMessage(ChatColor.GOLD+"= Fin du tour de "+currentPlayer.getName()+ChatColor.GOLD+" =");
            currentPlayer.endTurn();
            turnOrder.add(turnOrder.remove());
        }

        currentPlayer = turnOrder.peek();
        Bukkit.broadcastMessage(ChatColor.GOLD+"= Début du tour de "+currentPlayer.getName()+ChatColor.GOLD+" =");
        currentPlayer.beginTurn();
    }

    public void playSound(Sound sound) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.playSound(player.getLocation(), sound,Integer.MAX_VALUE,1);
        }
    }

    public void playSound(Sound sound, float pitch) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.playSound(player.getLocation(), sound,Integer.MAX_VALUE,pitch);
        }
    }

    public MFPlayer getCurrentPlayer() {return this.currentPlayer;}
}
