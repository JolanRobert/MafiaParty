package MafiaParty.managers;

import MafiaParty.game.MFItem;
import MafiaParty.game.MFPlayer;
import MafiaParty.utils.ItemEditor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.ChatPaginator;

import java.util.*;

public class Shop
{
    private static Shop instance;
    public static Shop getInstance() {
        if (instance == null) instance = new Shop();
        return instance;
    }

    public Shop() {
        addItem("RDV sous le bureau", "Pipe le dé, le maître du jeu est consentant.", 10);
        addItem("Téléphone du bossó", "Appelle la mafia pour kidnapper un adversaire et échanger ta place avec.", 7);
        addItem("DU-DUEL", "Choisis un adversaire, le premier de vous deux qui trouve la prochaine musique oblige son adversaire à lui donner 4 pièces (peut s'activer après annonce du thème).", 7);
        addItem("Another One", "Lance un dé supplémentaire (+5 pièces en cas de double !).", 5);
    }

    private List<MFItem> items = new ArrayList<>();

    private void addItem(String displayName, String lore, int price) {
        ItemStack item = new ItemStack(Material.NETHER_STAR);
        ItemEditor.setDisplayName(item, ChatColor.GOLD+displayName);
        List<String> pagedLore = new ArrayList<>();
        ChatPaginator.ChatPage cp = ChatPaginator.paginate(lore, 1, 40, 99);
        for (String line : cp.getLines()) {
            pagedLore.add(ChatColor.RED+line);
        }
        ItemEditor.setLore(item, pagedLore);
        items.add(new MFItem(item, price));
    }

    public void buyItem(ItemStack item, MFPlayer mfPlayer) {
        for (MFItem mfItem : items) {
            if (!item.getItemMeta().getDisplayName().startsWith(mfItem.Item.getItemMeta().getDisplayName())) continue;
            if (mfPlayer.GoldAmount < mfItem.Price) {
                mfPlayer.Player.playSound(mfPlayer.getLocation(), Sound.ENTITY_VILLAGER_NO, Integer.MAX_VALUE,1);
                break;
            }

            mfPlayer.addItem(mfItem.Item);
            mfPlayer.loseGold(mfItem.Price);
            break;
        }
    }

    public ItemStack getRandomItem() {
        List<MFItem> tmp = new ArrayList<>(items);
        Random rdm = new Random();
        return tmp.get(rdm.nextInt(items.size())).Item;
    }

    public List<MFItem> getItems() {
        return this.items;
    }
}
