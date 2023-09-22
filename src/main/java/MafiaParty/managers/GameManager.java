package MafiaParty.managers;

import MafiaParty.game.MFPlayer;
import MafiaParty.game.Square;
import MafiaParty.game.Star;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Random;

public class GameManager
{
    private static GameManager instance;
    public static GameManager getInstance() {
        if (instance == null) instance = new GameManager();
        return instance;
    }

    private Random rdm = new Random();

    public void commandRoll(String playerName) {
        int roll = rdm.nextInt(1,11);

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP,Integer.MAX_VALUE,2);
        }

        Bukkit.broadcastMessage(ChatColor.GREEN+playerName+" a fait un "+ChatColor.GOLD+roll);
    }

    public void commandGold(MFPlayer mfPlayer, int amount) {
        if (amount < 0) {
            int lostAmount = mfPlayer.loseGold(amount);
            Bukkit.broadcastMessage(ChatColor.RED+mfPlayer.getName()+" perd "+lostAmount+"g");
        }
        else {
            mfPlayer.gainGold(amount);
            Bukkit.broadcastMessage(ChatColor.GREEN+mfPlayer.getName()+" gagne "+amount+"g");
        }
    }

    public void commandStar(MFPlayer mfPlayer, Star star) {
        if (mfPlayer.hasStar(star)) {
            mfPlayer.loseStar(star);
            Bukkit.broadcastMessage(ChatColor.RED+mfPlayer.getName()+" perd "+getStarColor(star)+"★");
        }
        else {
            mfPlayer.gainStar(star);
            Bukkit.broadcastMessage(ChatColor.GREEN+mfPlayer.getName()+" gagne "+getStarColor(star)+"★");
        }
    }

    private ChatColor getStarColor(Star star) {
        if (star == Star.Blue) return ChatColor.AQUA;
        if (star == Star.Red) return ChatColor.RED;
        if (star == Star.Green) return ChatColor.GREEN;
        return ChatColor.YELLOW;
    }

    public void commandSquare(MFPlayer mfPlayer, Square square) {
        switch (square) {
            case Blue:
                BoardManager.getInstance().squareBlue(mfPlayer);
                break;
            case Red:
                BoardManager.getInstance().squareRed(mfPlayer);
                break;
            case Green:
                BoardManager.getInstance().squareGreen(mfPlayer);
                break;
            case Purple:
                BoardManager.getInstance().squarePurple(mfPlayer);
                break;
        }
    }

    public void commandBoo(MFPlayer mfPlayer, MFPlayer mfTarget, String action, Star star) {
        if (mfPlayer == mfTarget) {
            mfPlayer.Player.sendMessage(ChatColor.RED+"Vous ne pouvez pas vous voler vous-même, idiot !");
            return;
        }

        if (action.equalsIgnoreCase("Gold")) {
            int stealAmount = mfPlayer.stealGold(mfTarget, rdm.nextInt(2,5));
            Bukkit.broadcastMessage(ChatColor.GRAY+"■ "+mfPlayer.getName()+" vole "+stealAmount+" pièces à "+mfTarget.getName());
        }
        else if (action.equalsIgnoreCase("Star")) {
            if (mfPlayer.GoldAmount < 25) {
                mfPlayer.Player.sendMessage(ChatColor.RED+"Pas assez de Gold (20g) !");
                return;
            }
            if (mfPlayer.stealStar(mfTarget, star)) {
                mfPlayer.loseGold(25);
                Bukkit.broadcastMessage(ChatColor.GRAY+"■ "+mfPlayer.getName()+" vole "+getStarColor(star)+"★"+ChatColor.GRAY+" à "+mfTarget.getName());
            }
        }
    }
}
