package MafiaParty.commands;

import MafiaParty.enums.EItem;
import MafiaParty.game.MFPlayer;
import MafiaParty.enums.EStar;
import MafiaParty.managers.AnswerManager;
import MafiaParty.managers.CommandManager;
import MafiaParty.managers.GameManager;
import MafiaParty.managers.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;

public class GameCommands implements CommandExecutor
{
    private static GameCommands instance;
    public static GameCommands getInstance() {
        if (instance == null) instance = new GameCommands();
        return instance;
    }

    private ArrayList<String> opAuthorizedLabels = new ArrayList<>(Arrays.asList("help", "host", "music", "gold", "star", "item", "turn", "duel"));

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (!(sender instanceof Player)) return true; //CONSOLE
        if (!label.equalsIgnoreCase("mp")) return true;

        Player player = (Player)sender;

        if (opAuthorizedLabels.contains(args[0])) return opCommand(player, args);

        return true;
    }

    public boolean opCommand(Player player, String[] args) {
        if (!player.isOp()) return true;
        MFPlayer mfPlayer = PlayerManager.getInstance().getMFPlayer(player);

        if (args[0].equalsIgnoreCase("help") && args.length == 1) {
            player.sendMessage(
                    ChatColor.YELLOW+"Commandes :\n"+
                    ChatColor.GOLD+"/mp"+ChatColor.GREEN+" host\n"+
                    ChatColor.GOLD+"/mp"+ChatColor.GREEN+" turn\n"+
                    ChatColor.GOLD+"/mp"+ChatColor.GREEN+" music <start|stop>\n"+
                    ChatColor.GOLD+"/mp"+ChatColor.GREEN+" gold <player> <amount>\n"+
                    ChatColor.GOLD+"/mp"+ChatColor.GREEN+" star <player> <starColor>\n"+
                    ChatColor.GOLD+"/mp"+ChatColor.GREEN+" item <player> <itemName>\n"+
                    ChatColor.GOLD+"/mp"+ChatColor.GREEN+" duel <p1> <p2> <pWinner>");
        }

        else if (args[0].equalsIgnoreCase("host") && args.length == 1) {
            if (mfPlayer != null) {
                PlayerManager.getInstance().removePlayer(player);
                Bukkit.broadcastMessage(ChatColor.YELLOW+player.getName()+" > Host");
            }
            else {
                PlayerManager.getInstance().addPlayer(player);
                Bukkit.broadcastMessage(ChatColor.YELLOW+player.getName()+" > Player");
            }
        }
        else if (args[0].equalsIgnoreCase("turn") && args.length >= 2) {
            if (args[1].equalsIgnoreCase("randomize")) {
                GameManager.getInstance().randomizeTurnOrder();
            }
            else if (args[1].equalsIgnoreCase("force")) {
                MFPlayer mfTarget = PlayerManager.getInstance().getMFPlayer(Bukkit.getPlayer(args[2]));
                GameManager.getInstance().forceTurn(mfTarget);
            }
        }
        else if (args[0].equalsIgnoreCase("music") && args.length == 2) {
            if (args[1].equalsIgnoreCase("start")) {
                AnswerManager.getInstance().setActive(true);
                Bukkit.broadcastMessage(ChatColor.YELLOW+"== Début de la musique ==");
            }
            else if (args[1].equalsIgnoreCase("stop")) {
                Bukkit.broadcastMessage(ChatColor.YELLOW+"== Fin de la musique ==");
                AnswerManager.getInstance().setActive(false);
                AnswerManager.getInstance().printAnswers();
                AnswerManager.getInstance().resolve(mfPlayer);
            }
        }
        else if (args[0].equalsIgnoreCase("gold") && args.length == 3) {
            MFPlayer mfTarget = PlayerManager.getInstance().getMFPlayer(Bukkit.getPlayer(args[1]));
            try {
                CommandManager.getInstance().commandGold(mfTarget, Integer.parseInt(args[2]));
            } catch (NumberFormatException e) {
                return true;
            }
        }
        else if (args[0].equalsIgnoreCase("star") && args.length == 3) {
            MFPlayer mfTarget = PlayerManager.getInstance().getMFPlayer(Bukkit.getPlayer(args[1]));
            try {
                CommandManager.getInstance().commandStar(mfTarget, EStar.valueOf(args[2]));
            } catch (IllegalArgumentException e) {
                return true;
            }
        }
        else if (args[0].equalsIgnoreCase("item") && args.length == 4) {
            MFPlayer mfTarget = PlayerManager.getInstance().getMFPlayer(Bukkit.getPlayer(args[1]));
            try {
                CommandManager.getInstance().commandItem(mfTarget, args[2], EItem.valueOf(args[3]));
            } catch (IllegalArgumentException e) {
                return true;
            }
        }
        else if (args[0].equalsIgnoreCase("duel") && args.length == 4) {
            MFPlayer p1 = PlayerManager.getInstance().getMFPlayer(Bukkit.getPlayer(args[1]));
            MFPlayer p2 = PlayerManager.getInstance().getMFPlayer(Bukkit.getPlayer(args[2]));
            if (p1 == null || p2 == null) return true;
            if (p1.getDueller() != p2 || p2.getDueller() != p1) return true;
            MFPlayer pWinner = PlayerManager.getInstance().getMFPlayer(Bukkit.getPlayer(args[3]));
            if (p1 != pWinner && p2 != pWinner) return true;
            MFPlayer pLoser = p1 == pWinner ? p2 : p1;

            int stealAmount = pWinner.stealGold(pLoser, 7);
            pWinner.setDueller(null);
            pLoser.setDueller(null);
            Bukkit.broadcastMessage(pWinner.getName()+ChatColor.GREEN+" (+"+stealAmount+"g) gagne son duel contre "+pLoser.getName()+ChatColor.RED+" (-"+stealAmount+"g)");
        }

        return true;
    }

    private boolean cancel(Player player, String cmdFormat) {
        player.sendMessage(ChatColor.RED+cmdFormat);
        return true;
    }
}
