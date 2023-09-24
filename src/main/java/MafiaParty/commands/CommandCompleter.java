package MafiaParty.commands;

import MafiaParty.enums.EItem;
import MafiaParty.game.MFPlayer;
import MafiaParty.enums.EStar;
import MafiaParty.managers.PlayerManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CommandCompleter implements TabCompleter
{
    private static CommandCompleter instance;
    public static CommandCompleter getInstance() {
        if (instance == null) instance = new CommandCompleter();
        return instance;
    }

    private List<String> result = new ArrayList<String>();

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        result.clear();

        if (!label.equalsIgnoreCase("mp")) return result;

        Player player = (Player)sender;

        if (player.isOp()) opCommands(args);

        return result;
    }

    public void opCommands(String[] args) {
        if (args.length == 1)
        {
            if (IsValid("help",args[0])) result.add("help");
            if (IsValid("host",args[0])) result.add("host");
            if (IsValid("music",args[0])) result.add("music");
            if (IsValid("gold",args[0])) result.add("gold");
            if (IsValid("star",args[0])) result.add("star");
            if (IsValid("item",args[0])) result.add("item");
            if (IsValid("turn",args[0])) result.add("turn");
            if (IsValid("duel",args[0])) result.add("duel");
        }

        else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("music")) {
                if (IsValid("start",args[1])) result.add("start");
                if (IsValid("stop",args[1])) result.add("stop");
            }
            else if (args[0].equalsIgnoreCase("gold") || args[0].equalsIgnoreCase("star")
                    || args[0].equalsIgnoreCase("item") || args[0].equalsIgnoreCase("duel")) {
                result.addAll(getPlayerNames());
            }
            else if (args[0].equalsIgnoreCase("turn")) {
                if (IsValid("randomize",args[1])) result.add("randomize");
                if (IsValid("force",args[1])) result.add("force");
            }
        }
        else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("star")) {
                for (EStar star : EStar.values()) {
                    result.add(star.toString());
                }
            }
            else if (args[0].equalsIgnoreCase("item")) {
                if (IsValid("add",args[2])) result.add("add");
                if (IsValid("remove",args[2])) result.add("remove");
            }
            else if (args[0].equalsIgnoreCase("turn") || args[0].equalsIgnoreCase("duel")) {
                result.addAll(getPlayerNames());
            }
        }
        else if (args.length == 4) {
            if (args[0].equalsIgnoreCase("item")) {
                for (EItem item : EItem.values()) {
                    result.add(item.toString());
                }
            }
            else if (args[0].equalsIgnoreCase("duel")) {
                result.addAll(getPlayerNames());
            }
        }
    }

    private boolean IsValid(String command, String playerInput) {
        return command.startsWith(playerInput.toLowerCase());
    }

    private List<String> getPlayerNames()
    {
        List<String> names = new ArrayList<>();
        for (MFPlayer mfPlayer : PlayerManager.getInstance().getMFPlayers())
        {
            names.add(mfPlayer.getPlayer().getName());
        }
        return names;
    }
}
