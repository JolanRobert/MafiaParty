package MafiaParty.managers;

import MafiaParty.enums.EItem;
import MafiaParty.enums.EStar;
import MafiaParty.game.MFPlayer;
import MafiaParty.items.MFItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.Random;

public class CommandManager
{
    private static CommandManager instance;
    public static CommandManager getInstance() {
        if (instance == null) instance = new CommandManager();
        return instance;
    }

    private Random rdm = new Random();

    public void commandGold(MFPlayer mfPlayer, int amount) {
        if (amount < 0) {
            int lostAmount = mfPlayer.loseGold(-amount);
            Bukkit.broadcastMessage(mfPlayer.getName()+ChatColor.RED+" perd "+lostAmount+"g");
        }
        else {
            mfPlayer.gainGold(amount);
            Bukkit.broadcastMessage(mfPlayer.getName()+ChatColor.GREEN+" gagne "+amount+"g");
        }
    }

    public void commandStar(MFPlayer mfPlayer, EStar star) {
        if (mfPlayer.hasStar(star)) {
            mfPlayer.loseStar(star);
            Bukkit.broadcastMessage(mfPlayer.getName()+ChatColor.RED+" perd "+star.getColor()+"★");
        }
        else {
            mfPlayer.gainStar(star);
            Bukkit.broadcastMessage(mfPlayer.getName()+ChatColor.GREEN+" gagne "+star.getColor()+"★");
        }
    }

    public void commandItem(MFPlayer mfPlayer, String action, EItem eItem) {
        MFItem mfItem = Shop.getInstance().getMFItem(eItem);
        if (action.equalsIgnoreCase("add")) {
            mfPlayer.addItem(mfItem);
            Bukkit.broadcastMessage(mfPlayer.getName()+ChatColor.GREEN+" gagne "+mfItem.getName());
        }
        else if (action.equalsIgnoreCase("remove")) {
            mfPlayer.removeItem(mfItem);
            Bukkit.broadcastMessage(mfPlayer.getName()+ChatColor.RED+" perd "+mfItem.getName());
        }
    }
}
