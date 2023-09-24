package MafiaParty.listeners;

import MafiaParty.enums.EStar;
import MafiaParty.game.Board;
import MafiaParty.game.Boo;
import MafiaParty.game.MFPlayer;
import MafiaParty.items.*;
import MafiaParty.managers.GameManager;
import MafiaParty.managers.InventoryManager;
import MafiaParty.managers.PlayerManager;
import MafiaParty.managers.Shop;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryListener implements Listener
{
    private Board board = new Board();

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) return;
        if (event.getClickedInventory().getItem(event.getSlot()) == null) return;

        Player player = (Player)event.getWhoClicked();
        MFPlayer mfPlayer = PlayerManager.getInstance().getMFPlayer(player);
        Inventory inv = event.getInventory();
        ItemStack clickedItem = event.getClickedInventory().getItem(event.getSlot());
        Material mat = clickedItem.getType();
        String itemName = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName());

        if (mat == Material.BARRIER && itemName.equalsIgnoreCase("Retour")) {
            mfPlayer.closeInventory(false);
            return;
        }

        InventoryManager invM = InventoryManager.getInstance();
        Boo boo = Boo.getInstance();

        if (inv == invM.getPlayerMenuInventory()) {
            if (mat == Material.PLAYER_HEAD && itemName.equalsIgnoreCase("Dé standard")) {
                mfPlayer.roll();
                mfPlayer.closeInventory(true);
            }
            else if (mat == Material.CHEST && itemName.equalsIgnoreCase("Objets")) {
                mfPlayer.openInventory(mfPlayer.getItemInventory());
            }
            else if (mat == Material.GOLD_BLOCK && itemName.equalsIgnoreCase("Plateau")) {
                mfPlayer.openInventory(invM.getBoardInventory());
            }
            else if (mat == Material.ENDER_EYE && itemName.equalsIgnoreCase("Fin du tour")) {
                GameManager.getInstance().nextTurn();
                mfPlayer.closeInventory(true);
            }

            event.setCancelled(true);
        }

        else if (inv == invM.getBoardInventory()) {
            if (mat == Material.LIGHT_BLUE_WOOL) board.squareBlue(mfPlayer);
            else if (mat == Material.RED_WOOL) board.squareRed(mfPlayer);
            else if (mat == Material.GREEN_WOOL) board.squareGreen(mfPlayer);
            else if (mat == Material.ORANGE_WOOL) board.squareOrange(mfPlayer);
            else if (mat == Material.PINK_WOOL) board.squarePink();
            else if (mat == Material.PURPLE_WOOL) board.squarePurple(mfPlayer);
            else if (mat == Material.WHITE_WOOL) {
                board.squareWhite(mfPlayer);
                return;
            }
            else if (mat == Material.GRAY_WOOL) {
                board.squareGray(mfPlayer);
                return;
            }
            else if (mat == Material.YELLOW_WOOL) {
                board.squareYellow(mfPlayer);
                return;
            }
            mfPlayer.closeInventory(true);
        }

        else if (inv == mfPlayer.getItemInventory()) {
            MFItem mfItem = Shop.getInstance().getMFItem(clickedItem);
            if (mfItem instanceof DeMaudit || mfItem instanceof Duel || mfItem instanceof Kidnapping || mfItem instanceof Planification) {
                mfPlayer.openInventory(InventoryManager.getInstance().createItemTargetInventory(mfPlayer, mfItem.getEItem()));
                mfPlayer.setCurrentUsingItem(mfItem);
            }
            else {
                mfItem.use(mfPlayer);
                mfPlayer.closeInventory(true);
            }
        }

        else if (inv == InventoryManager.getInstance().getItemTargetInventory()) {
            Player pTarget = Bukkit.getPlayer(ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName()));
            MFPlayer mfTarget = PlayerManager.getInstance().getMFPlayer(pTarget);
            MFItem itemUsed = mfPlayer.getCurrentUsingItem();
            if (itemUsed instanceof DeMaudit) ((DeMaudit)itemUsed).use(mfPlayer, mfTarget);
            else if (itemUsed instanceof Duel) ((Duel)itemUsed).use(mfPlayer, mfTarget);
            else if (itemUsed instanceof Kidnapping) ((Kidnapping)itemUsed).use(mfPlayer, mfTarget);
            else if (itemUsed instanceof Planification) ((Planification)itemUsed).use(mfPlayer, mfTarget);
            mfPlayer.closeInventory(true);
        }

        else if (inv == invM.getStarInventory()) {
            event.setCancelled(true);

            EStar eStar = null;
            int starCost = mfPlayer.hasStarDiscount() ? 10 : 20;

            if (mfPlayer.getGold() < starCost) {
                mfPlayer.sendMessage(ChatColor.RED+"Pas assez de pièces pour acheter une étoile");
                mfPlayer.getPlayer().playSound(mfPlayer.getLocation(), Sound.ENTITY_VILLAGER_NO, Integer.MAX_VALUE,1);
                return;
            }

            if (mat == Material.LIGHT_BLUE_DYE) eStar = EStar.Blue;
            else if (mat == Material.RED_DYE) eStar = EStar.Red;
            else if (mat == Material.LIME_DYE) eStar = EStar.Green;
            else if (mat == Material.YELLOW_DYE) eStar = EStar.Yellow;
            mfPlayer.gainStar(eStar);
            mfPlayer.loseGold(starCost);
            mfPlayer.setStarDiscount(false);
            Bukkit.broadcastMessage(ChatColor.YELLOW+"■ "+mfPlayer.getName()+ChatColor.YELLOW+" gagne"+eStar.getColor()+" ★"+ChatColor.RED+" ("+starCost+"g)");
            GameManager.getInstance().playSound(Sound.UI_TOAST_CHALLENGE_COMPLETE, 2);
            mfPlayer.closeInventory(true);
        }

        else if (inv == invM.getShopInventory()) {
            Shop.getInstance().buyItem(clickedItem, mfPlayer);
            event.setCancelled(true);
        }

        else if (inv == boo.getBooTargetInventory()) {
            Player pTarget = Bukkit.getPlayer(ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName()));
            MFPlayer mfTarget = PlayerManager.getInstance().getMFPlayer(pTarget);
            boo.setTarget(mfTarget);
            mfPlayer.openInventory(boo.getBooChoiceInventory());
        }

        else if (inv == boo.getBooChoiceInventory()) {
            boo.processChoice(mfPlayer, clickedItem);
            event.setCancelled(true);
        }
    }
}
