package MafiaParty.game;

import MafiaParty.utils.ItemEditor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class GameItem
{
    private static ItemStack playerMenu;

    private static void createPlayerMenu() {
        playerMenu = new ItemStack(Material.NETHER_STAR);
        ItemEditor.setDisplayName(playerMenu, ChatColor.YELLOW+"Menu");
    }

    public static ItemStack getPlayerMenu() {
        if (playerMenu == null) createPlayerMenu();
        return playerMenu;
    }
}
