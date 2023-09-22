package MafiaParty.commands;

import MafiaParty.game.MFPlayer;
import MafiaParty.game.Square;
import MafiaParty.game.Star;
import MafiaParty.managers.PlayerManager;
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
    private List<String> op = new ArrayList<String>();
    private List<String> nonOp = new ArrayList<String>();

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        result.clear();

        if (!label.equalsIgnoreCase("mp")) return result;

        Player player = (Player)sender;

        result.addAll(nonOpCommands(args));
        if (player.isOp()) result.addAll(opCommands(args));

        return result;
    }

    public List<String> nonOpCommands(String[] args) {
        nonOp.clear();

        if (args.length == 1)
        {
            if (IsValid("boo",args[0])) result.add("boo");
            if (IsValid("gold",args[0])) result.add("gold");
            if (IsValid("roll",args[0])) result.add("roll");
            if (IsValid("square",args[0])) result.add("square");
            if (IsValid("star",args[0])) result.add("star");
        }

        else if (args.length == 2)
        {
            if (args[0].equalsIgnoreCase("boo")) result.addAll(getPlayerNames());
            else if (args[0].equalsIgnoreCase("square"))
            {
                for (Square square : Square.values())
                {
                    result.add(square.toString());
                }
            }
            else if (args[0].equalsIgnoreCase("star"))
            {
                for (Star star : Star.values())
                {
                    result.add(star.toString());
                }
            }
        }

        else if (args.length == 3)
        {
            if (args[0].equalsIgnoreCase("boo"))
            {
                if (IsValid("gold",args[2])) result.add("gold");
                if (IsValid("star",args[2])) result.add("star");
            }
        }

        else if (args.length == 4)
        {
            if (args[0].equalsIgnoreCase("boo") && args[2].equalsIgnoreCase("star"))
            {
                for (Star star : Star.values())
                {
                    result.add(star.toString());
                }
            }
        }

        return nonOp;
    }

    public List<String> opCommands(String[] args) {
        op.clear();

        if (args.length == 1)
        {
            if (IsValid("host",args[0])) result.add("host");
            if (IsValid("music",args[0])) result.add("music");
        }

        else if (args.length == 2)
        {
            if (args[0].equalsIgnoreCase("music")) {
                if (IsValid("start",args[1])) result.add("start");
                if (IsValid("stop",args[1])) result.add("stop");
            }
        }

        return op;
    }

    private boolean IsValid(String command, String playerInput) {
        return command.startsWith(playerInput.toLowerCase());
    }

    private List<String> getPlayerNames()
    {
        List<String> names = new ArrayList<>();
        for (MFPlayer mfPlayer : PlayerManager.getInstance().Players)
        {
            names.add(mfPlayer.getName());
        }
        return names;
    }
}
