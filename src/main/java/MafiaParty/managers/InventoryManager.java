package MafiaParty.managers;

import MafiaParty.enums.EItem;
import MafiaParty.enums.EStar;
import MafiaParty.game.MFPlayer;
import MafiaParty.items.MFItem;
import MafiaParty.utils.ItemEditor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class InventoryManager
{
    private static InventoryManager instance;
    public static InventoryManager getInstance() {
        if (instance == null) instance = new InventoryManager();
        return instance;
    }

    private ItemStack backButton;

    private Inventory invShop;
    private Inventory invPlayerMenu;
    private Inventory invBoard;
    private Inventory invStar;
    private Inventory invItemTarget;

    public InventoryManager() {
        backButton = new ItemStack(Material.BARRIER);
        ItemEditor.setDisplayName(backButton, ChatColor.RED+"Retour");

        createShopInventory();
        createPlayerMenuInventory();
        createBoardInventory();
    }

    private void createShopInventory() {
        invShop = Bukkit.createInventory(null, 9, ChatColor.BLACK+"Magasin");
        for (MFItem mfItem : Shop.getInstance().getAllItems()) {
            ItemStack newItem = mfItem.getItem().clone();
            ItemEditor.setDisplayName(newItem, newItem.getItemMeta().getDisplayName()+ChatColor.GOLD+" ("+mfItem.getPrice()+"g)");
            invShop.addItem(newItem);
        }
        invShop.setItem(8, backButton);
    }

    private void createPlayerMenuInventory() {
        invPlayerMenu = Bukkit.createInventory(null, 9, ChatColor.BLACK+"Actions");
        ItemStack dice = getCustomHead("https://textures.minecraft.net/texture/7881cc2747ba72cbcb06c3cc331742cd9de271a5bbffd0ecb14f1c6a8b69bc9e");
        ItemEditor.setDisplayName(dice, ChatColor.YELLOW+"Dé standard");
        invPlayerMenu.addItem(dice);

        ItemStack item = new ItemStack(Material.CHEST);
        ItemEditor.setDisplayName(item, ChatColor.YELLOW+"Objets");
        invPlayerMenu.addItem(item);

        ItemStack square = new ItemStack(Material.GOLD_BLOCK);
        ItemEditor.setDisplayName(square, ChatColor.YELLOW+"Plateau");
        invPlayerMenu.addItem(square);

        ItemStack endTurn = new ItemStack(Material.ENDER_EYE);
        ItemEditor.setDisplayName(endTurn, ChatColor.YELLOW+"Fin du tour");
        invPlayerMenu.setItem(7, endTurn);

        invPlayerMenu.setItem(8, backButton);
    }

    private void createBoardInventory() {
        invBoard = Bukkit.createInventory(null, 18, ChatColor.BLACK+"Plateau");

        ItemStack blue = new ItemStack(Material.LIGHT_BLUE_WOOL);
        ItemEditor.setDisplayName(blue, ChatColor.AQUA+"Bleu");
        invBoard.addItem(blue);

        ItemStack red = new ItemStack(Material.RED_WOOL);
        ItemEditor.setDisplayName(red, ChatColor.RED+"Rouge");
        invBoard.addItem(red);

        ItemStack green = new ItemStack(Material.GREEN_WOOL);
        ItemEditor.setDisplayName(green, ChatColor.GREEN+"Chance");
        invBoard.addItem(green);

        ItemStack orange = new ItemStack(Material.ORANGE_WOOL);
        ItemEditor.setDisplayName(orange, ChatColor.GOLD+"Objet");
        invBoard.addItem(orange);

        ItemStack pink = new ItemStack(Material.PINK_WOOL);
        ItemEditor.setDisplayName(pink, ChatColor.LIGHT_PURPLE+"Miracle");
        invBoard.addItem(pink);

        ItemStack black = new ItemStack(Material.PURPLE_WOOL);
        ItemEditor.setDisplayName(black, ChatColor.DARK_PURPLE+"Catastrophe");
        invBoard.addItem(black);

        ItemStack white = new ItemStack(Material.WHITE_WOOL);
        ItemEditor.setDisplayName(white, ChatColor.WHITE+"Magasin");
        invBoard.addItem(white);

        ItemStack gray = new ItemStack(Material.GRAY_WOOL);
        ItemEditor.setDisplayName(gray, ChatColor.GRAY+"Boo");
        invBoard.addItem(gray);

        ItemStack yellow = new ItemStack(Material.YELLOW_WOOL);
        ItemEditor.setDisplayName(yellow, ChatColor.YELLOW+"Étoile");
        invBoard.addItem(yellow);

        invBoard.setItem(17, backButton);
    }

    public Inventory createStarInventory(MFPlayer mfPlayer) {
        invStar = Bukkit.createInventory(null, 9, ChatColor.BLACK+"Étoile");
        int starCost = mfPlayer.hasStarDiscount() ? 10 : 20;

        if (!mfPlayer.hasStar(EStar.Blue)) {
            ItemStack blue = new ItemStack(Material.LIGHT_BLUE_DYE);
            ItemEditor.setDisplayName(blue, ChatColor.AQUA+"Bleue"+ChatColor.GOLD+" ("+starCost+"g)");
            invStar.addItem(blue);
        }

        if (!mfPlayer.hasStar(EStar.Red)) {
            ItemStack red = new ItemStack(Material.RED_DYE);
            ItemEditor.setDisplayName(red, ChatColor.RED+"Rouge"+ChatColor.GOLD+" ("+starCost+"g)");
            invStar.addItem(red);
        }

        if (!mfPlayer.hasStar(EStar.Green)) {
            ItemStack green = new ItemStack(Material.LIME_DYE);
            ItemEditor.setDisplayName(green, ChatColor.GREEN+"Verte"+ChatColor.GOLD+" ("+starCost+"g)");
            invStar.addItem(green);
        }

        if (!mfPlayer.hasStar(EStar.Yellow)) {
            ItemStack yellow = new ItemStack(Material.YELLOW_DYE);
            ItemEditor.setDisplayName(yellow, ChatColor.YELLOW+"Jaune"+ChatColor.GOLD+" ("+starCost+"g)");
            invStar.addItem(yellow);
        }

        invStar.setItem(8, backButton);
        return invStar;
    }

    public Inventory createItemTargetInventory(MFPlayer mfPlayer, EItem eItem) {
        List<MFPlayer> players = PlayerManager.getInstance().getMFPlayers();
        int invSize = players.size() % 9 == 0 ? players.size() : players.size() + (9 - (players.size() % 9));
        invItemTarget = Bukkit.createInventory(null, invSize, "Cible");
        if (eItem == EItem.DeMaudit) {
            for (MFPlayer mfp : players) {
                if (mfp.isCursed()) continue;
                ItemStack pHead = getCustomHead(mfp.getPlayer());
                ItemEditor.setDisplayName(pHead, mfp.getName());
                invItemTarget.addItem(pHead);
            }
        }
        else if (eItem == EItem.Duel) {
            for (MFPlayer mfp : players) {
                if (mfp == mfPlayer) continue;
                if (mfp.isDuelled()) continue;
                ItemStack pHead = getCustomHead(mfp.getPlayer());
                ItemEditor.setDisplayName(pHead, mfp.getName());
                invItemTarget.addItem(pHead);
            }
        }
        else if (eItem == EItem.Kidnapping) {
            for (MFPlayer mfp : players) {
                if (mfp == mfPlayer) continue;
                ItemStack pHead = getCustomHead(mfp.getPlayer());
                ItemEditor.setDisplayName(pHead, mfp.getName());
                invItemTarget.addItem(pHead);
            }
        }
        else if (eItem == EItem.Planification) {
            for (MFPlayer mfp : players) {
                if (mfp == mfPlayer) continue;
                if (mfp.getItems().size() == 0) continue;
                ItemStack pHead = getCustomHead(mfp.getPlayer());
                ItemEditor.setDisplayName(pHead, mfp.getName());
                List<String> lore = new ArrayList<>();
                for (ItemStack item : mfp.getItemInventory().getContents()) {
                    if (item.getType() == Material.BARRIER) continue;
                    lore.add(ChatColor.AQUA+ChatColor.stripColor(item.getItemMeta().getDisplayName())+ChatColor.GOLD+" (x"+item.getAmount()+")");
                }
                ItemEditor.setLore(pHead, lore);
                invItemTarget.addItem(pHead);
            }
        }

        invItemTarget.setItem(invItemTarget.getSize()-1, getBackButton());
        return invItemTarget;
    }

    public ItemStack getCustomHead(Player player) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        meta.setOwningPlayer(player);
        head.setItemMeta(meta);
        return head;
    }

    public ItemStack getCustomHead(String url) {
        PlayerProfile profile = Bukkit.createPlayerProfile("92864445-51c5-4c3b-9039-517c9927d1b4"); // Get a new player profile
        PlayerTextures textures = profile.getTextures();
        URL urlObject;
        try {
            urlObject = new URL(url);
        } catch (MalformedURLException exception) {
            throw new RuntimeException("Invalid URL", exception);
        }
        textures.setSkin(urlObject);
        profile.setTextures(textures);
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        meta.setOwnerProfile(profile);
        head.setItemMeta(meta);
        return head;
    }

    public ItemStack getBackButton() {return this.backButton;}
    public Inventory getShopInventory() {return this.invShop;}
    public Inventory getPlayerMenuInventory() {return this.invPlayerMenu;}
    public Inventory getBoardInventory() {return this.invBoard;}
    public Inventory getStarInventory() {return this.invStar;}
    public Inventory getItemTargetInventory() {return this.invItemTarget;}
}
