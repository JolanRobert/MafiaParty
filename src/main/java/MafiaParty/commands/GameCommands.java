package MafiaParty.commands;

import MafiaParty.game.MFPlayer;
import MafiaParty.game.Square;
import MafiaParty.game.Star;
import MafiaParty.managers.AnswerManager;
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

    private ArrayList<String> nonOpauthorizedLabels = new ArrayList<>(Arrays.asList("roll", "gold", "star", "square", "boo"));
    private ArrayList<String> opAuthorizedLabels = new ArrayList<>(Arrays.asList("host", "music"));

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (!(sender instanceof Player)) return true; //CONSOLE
        if (!label.equalsIgnoreCase("mp")) return true;

        Player player = (Player)sender;

        if (args.length == 0)
        {
            player.sendMessage(
                    ChatColor.YELLOW+"Commandes :\n"+
                    ChatColor.GOLD+"/mp roll"+ChatColor.GREEN+" -> Lance un dé\n"+
                    ChatColor.GOLD+"/mp gold"+ChatColor.GREEN+" -> Gagne/Perd de l'or\n"+
                    ChatColor.GOLD+"/mp star"+ChatColor.GREEN+" -> Gagne/Perd une étoile\n"+
                    ChatColor.GOLD+"/mp square"+ChatColor.GREEN+" -> Déclenche l'effet d'une case\n"+
                    ChatColor.GOLD+"/mp boo"+ChatColor.GREEN+" -> Vole un adversaire");

            return true;
        }

        if (nonOpauthorizedLabels.contains(args[0])) return nonOpCommand(player, args);
        if (opAuthorizedLabels.contains(args[0])) return opCommand(player, args);

        return true;
    }

    public boolean nonOpCommand(Player player, String[] args) {
        MFPlayer mfPlayer = PlayerManager.getInstance().getMFPlayer(player);

        if (args[0].equalsIgnoreCase("roll") && args.length == 1)
        {
            GameManager.getInstance().commandRoll(player.getName());
        }
        else if (args[0].equalsIgnoreCase("gold") && args.length == 2) {
            try {
                GameManager.getInstance().commandGold(mfPlayer, Integer.parseInt(args[1]));
            } catch(NumberFormatException e) {
                return cancel(player, "/mp gold <amount>");
            }
        }
        else if (args[0].equalsIgnoreCase("star") && args.length == 2) {
            try {
                GameManager.getInstance().commandStar(mfPlayer, Star.valueOf(args[1]));
            } catch(IllegalArgumentException e) {
                return cancel(player, "/mp star <color>");
            }
        }
        else if (args[0].equalsIgnoreCase("square") && args.length == 2) {
            try {
                GameManager.getInstance().commandSquare(mfPlayer, Square.valueOf(args[1]));
            } catch (IllegalArgumentException e) {
                return cancel(player, "/mp square <color>");
            }
        }
        else if (args[0].equalsIgnoreCase("boo") && args.length >= 3) {
            try {
                MFPlayer mfTarget = PlayerManager.getInstance().getMFPlayer(Bukkit.getPlayer(args[1]));
                if (args[2].equalsIgnoreCase("Gold")) {
                    GameManager.getInstance().commandBoo(mfPlayer, mfTarget, "Gold", null);
                }
                else {
                    GameManager.getInstance().commandBoo(mfPlayer, mfTarget, "Star", Star.valueOf(args[3]));
                }
            } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
                return cancel(player, "/mp boo <player> <gold|star> |StarColor|");
            }
        }

        return true;
    }

    public boolean opCommand(Player player, String[] args) {
        if (!player.isOp()) return true;
        MFPlayer mfPlayer = PlayerManager.getInstance().getMFPlayer(player);

        if (args[0].equalsIgnoreCase("host") && args.length == 1) {
            if (mfPlayer != null) {
                Bukkit.broadcastMessage(player.getName()+ChatColor.YELLOW+" > Host");
                PlayerManager.getInstance().removePlayer(player);
            }
            else {
                Bukkit.broadcastMessage(player.getName()+ChatColor.YELLOW+" > Player");
                PlayerManager.getInstance().addPlayer(player);
            }
        }
        else if (args[0].equalsIgnoreCase("music") && args.length == 2) {
            if (args[1].equalsIgnoreCase("start")) {
                Bukkit.broadcastMessage(ChatColor.YELLOW+"== Début de la musique ==");
                AnswerManager.getInstance().setActive(true);
            }
            else if (args[1].equalsIgnoreCase("stop")) {
                Bukkit.broadcastMessage(ChatColor.YELLOW+"== Fin de la musique ==");
                AnswerManager.getInstance().setActive(false);
                AnswerManager.getInstance().printAnswers();
            }
            else {
                cancel(player, "/mp music <start|stop>");
            }
        }

        return true;
    }

    private boolean cancel(Player player, String cmdFormat) {
        player.sendMessage(ChatColor.RED+cmdFormat);
        return true;
    }
}
