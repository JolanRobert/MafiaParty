package MafiaParty.items;

import MafiaParty.enums.EItem;
import MafiaParty.game.MFPlayer;
import MafiaParty.managers.GameManager;
import MafiaParty.utils.ItemEditor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.ChatPaginator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MFItem implements Comparable<MFItem>
{
    private EItem itemEnum;
    private ItemStack item;
    private int price;

    protected Random rdm = new Random();

    public MFItem(EItem itemEnum, String displayName, String lore, int price) {
        this.itemEnum = itemEnum;
        this.item = new ItemStack(Material.NETHER_STAR);
        ItemEditor.setDisplayName(item, ChatColor.YELLOW+displayName);
        List<String> pagedLore = new ArrayList<>();
        ChatPaginator.ChatPage cp = ChatPaginator.paginate(lore, 1, 40, 99);
        for (String line : cp.getLines()) {
            pagedLore.add(ChatColor.AQUA+line);
        }
        ItemEditor.setLore(item, pagedLore);

        this.price = price;
    }

    public void use(MFPlayer mfPlayer) {
        Bukkit.broadcastMessage(mfPlayer.getName()+ ChatColor.GREEN+" utilise "+getName());
        mfPlayer.removeItem(this);
        GameManager.getInstance().playSound(Sound.ENTITY_BLAZE_SHOOT);
    }

    public EItem getEItem() {return this.itemEnum;}
    public ItemStack getItem() {return this.item;}
    public int getPrice() {return this.price;}

    public String getName() {
        return ChatColor.AQUA+ChatColor.stripColor(item.getItemMeta().getDisplayName());
    }

    @Override
    public int compareTo(MFItem other)
    {
        if (price > other.price) return 1;
        else if (price < other.price) return -1;
        return 0;
    }
}
