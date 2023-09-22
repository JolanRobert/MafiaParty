package MafiaParty;

import MafiaParty.commands.CommandCompleter;
import MafiaParty.commands.GameCommands;
import MafiaParty.listeners.ChatListener;
import MafiaParty.listeners.ConnexionListener;
import MafiaParty.listeners.InventoryListener;
import MafiaParty.listeners.PlayerListener;
import MafiaParty.managers.*;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    private static Main instance;
    public static Main getInstance() {return instance;}

    @Override
    public void onEnable() {
        instance = this;

        this.configWorld();
        this.registerListeners();
        this.registerCommands();

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.setScoreboard(ScoreboardManager.getInstance().getScoreboard());
            PlayerManager.getInstance().addPlayer(player);
        }

        ScoreboardManager.getInstance().updateScoreboard();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void configWorld() {
        World world = this.getServer().getWorld("world");
        world.setDifficulty(Difficulty.PEACEFUL);
        world.setGameRule(GameRule.DO_FIRE_TICK, false);
        world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
    }

    public void registerListeners() {
        PluginManager pm = this.getServer().getPluginManager();
        pm.registerEvents(new ChatListener(), this);
        pm.registerEvents(new ConnexionListener(), this);
        pm.registerEvents(new InventoryListener(), this);
        pm.registerEvents(new PlayerListener(), this);
    }

    private void registerCommands() {
        this.getCommand("mp").setExecutor(GameCommands.getInstance());
        this.getCommand("mp").setTabCompleter(CommandCompleter.getInstance());
    }
}
