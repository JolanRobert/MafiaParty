package MafiaParty.managers;

import MafiaParty.enums.EItem;
import MafiaParty.game.MFPlayer;
import MafiaParty.items.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Shop
{
    private static Shop instance;
    public static Shop getInstance() {
        if (instance == null) instance = new Shop();
        return instance;
    }

    private List<MFItem> allItems = new ArrayList<>();
    private List<MFItem> diceItems = new ArrayList<>();
    private Random rdm = new Random();

    public Shop() {
        diceItems.add(new DeLent());
        diceItems.add(new DeRapide());
        diceItems.add(new DeToutOuRien());
        diceItems.add(new DeMaudit());
        diceItems.add(new DeVolonte());
        allItems.add(new Duel());
        allItems.add(new Kidnapping());
        allItems.add(new Planification());
        allItems.addAll(diceItems);
        Collections.sort(allItems);
    }

    public void buyItem(ItemStack item, MFPlayer mfPlayer) {
        MFItem mfItem = getMFItem(item);
        if (mfPlayer.getGold() < mfItem.getPrice()) {
            mfPlayer.sendMessage(ChatColor.RED+"Pas assez de pièces pour acheter "+mfItem.getName());
            mfPlayer.getPlayer().playSound(mfPlayer.getLocation(), Sound.ENTITY_VILLAGER_NO, Integer.MAX_VALUE,1);
            return;
        }

        Bukkit.broadcastMessage(mfPlayer.getName()+ChatColor.WHITE+" achète "+mfItem.getName());
        mfPlayer.addItem(mfItem);
        mfPlayer.loseGold(mfItem.getPrice());
        mfPlayer.getPlayer().playSound(mfPlayer.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, Integer.MAX_VALUE,1);
    }

    public MFItem getMFItem(EItem eItem) {
        for (MFItem mfItem : allItems) {
            if (mfItem.getEItem() == eItem) return mfItem;
        }
        return null;
    }

    public MFItem getMFItem(ItemStack item) {
        for (MFItem mfItem : allItems) {
            String itemName = ChatColor.stripColor(item.getItemMeta().getDisplayName());
            String mfItemName = ChatColor.stripColor(mfItem.getName());
            if (itemName.startsWith(mfItemName)) return mfItem;
        }
        return null;
    }

    public MFItem getRandomMFItem() {
        return allItems.get(rdm.nextInt(allItems.size()));
    }

    public MFItem getRandomDiceMFItem() {
        return diceItems.get(rdm.nextInt(diceItems.size()));
    }

    public List<MFItem> getAllItems() {
        return this.allItems;
    }
}
