package MafiaParty.game;

import MafiaParty.enums.EStar;
import MafiaParty.items.MFItem;
import MafiaParty.managers.GameManager;
import MafiaParty.managers.InventoryManager;
import MafiaParty.managers.ScoreboardManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class MFPlayer implements Comparable<MFPlayer>
{
    private Player player;
    private int gold;
    private Map<EStar, Boolean> stars;

    private Inventory invItem;
    private List<MFItem> items = new ArrayList<>();
    private MFItem currentUsingItem;

    private boolean hasTurn;
    private boolean starDiscount;
    private MFPlayer curser;
    private MFPlayer dueller;

    private Stack<Inventory> inventories = new Stack<>();

    private Random rdm = new Random();

    public MFPlayer(Player player) {
        this.player = player;

        gold = 5;
        stars = new HashMap<>()
        {{
            put(EStar.Blue, false);
            put(EStar.Red, false);
            put(EStar.Green, false);
            put(EStar.Yellow, false);
        }};

        invItem = Bukkit.createInventory(null, 9, ChatColor.BLACK+"Objets");
        prepare();
    }

    public void prepare() {
        player.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, PotionEffect.INFINITE_DURATION, 0, true, false));
        player.setInvulnerable(true);
        player.getInventory().clear();

        player.getInventory().addItem(GameItem.getPlayerMenu());
    }

    public void beginTurn() {
        hasTurn = true;
        currentUsingItem = null;
    }

    public void endTurn() {
        hasTurn = false;
        currentUsingItem = null;
    }

    public void gainStar(EStar star) {
        stars.put(star, true);
        ScoreboardManager.getInstance().updateScoreboard();
    }

    public void loseStar(EStar star) {
        stars.put(star, false);
        ScoreboardManager.getInstance().updateScoreboard();
    }

    public boolean stealStar(MFPlayer mfTarget, EStar star) {
        if (hasStar(star) || !mfTarget.hasStar(star)) return false;

        mfTarget.loseStar(star);
        gainStar(star);
        ScoreboardManager.getInstance().updateScoreboard();
        return true;
    }

    public void swapStars(MFPlayer mfTarget) {
        Map<EStar, Boolean> tmp = stars;
        stars = mfTarget.getStars();
        mfTarget.setStars(tmp);
        ScoreboardManager.getInstance().updateScoreboard();
    }

    public boolean hasStar(EStar star) {
        return stars.get(star);
    }

    public void gainGold(int amount) {
        if (amount < 0) return;

        gold += amount;
        ScoreboardManager.getInstance().updateScoreboard();
    }

    public int loseGold(int amount) {
        int lostAmount = amount;
        gold -= amount;
        if (gold < 0) {
            lostAmount += gold;
            gold = 0;
        }

        ScoreboardManager.getInstance().updateScoreboard();
        return lostAmount;
    }

    public int stealGold(MFPlayer mfTarget, int amount) {
        int stealAmount = mfTarget.loseGold(amount);
        gainGold(stealAmount);
        ScoreboardManager.getInstance().updateScoreboard();

        return stealAmount;
    }

    public void swapGold(MFPlayer mfTarget) {
        int tmp = gold;
        gold = mfTarget.getGold();
        mfTarget.setGold(tmp);
        ScoreboardManager.getInstance().updateScoreboard();
    }

    public void addItem(MFItem mfItem) {
        items.add(mfItem);
    }

    public void removeItem(MFItem mfItem) {
        items.remove(mfItem);
    }

    public void removeAllItems() {
        items.clear();
    }

    public void swapItems(MFPlayer mfTarget) {
        List<MFItem> tmp = new ArrayList<>(getItems());
        items = new ArrayList<>(mfTarget.getItems());
        mfTarget.setItems(tmp);
    }

    public MFItem getRandomItem() {
        return items.get(rdm.nextInt(items.size()));
    }

    public Inventory getItemInventory() {
        invItem.clear();
        Map<MFItem, Integer> itemAmountMap = new HashMap<>();
        for (MFItem mfItem : items) {
            if (itemAmountMap.containsKey(mfItem)) itemAmountMap.put(mfItem, itemAmountMap.get(mfItem)+1);
            else itemAmountMap.put(mfItem,1);
        }

        for (Map.Entry<MFItem, Integer> entry : itemAmountMap.entrySet()) {
            ItemStack item = entry.getKey().getItem();
            item.setAmount(entry.getValue());
            invItem.addItem(item);
        }
        invItem.setItem(8, InventoryManager.getInstance().getBackButton());
        return invItem;
    }

    public void openInventory(Inventory inv) {
        //Player manually closed his inventory
        if (inventories.contains(inv)) inventories.clear();
        inventories.push(inv);
        player.openInventory(inventories.peek());
    }

    public void closeInventory(boolean all) {
        if (all) {
            inventories.clear();
            player.closeInventory();
        }
        else {
            inventories.pop();
            if (inventories.size() > 0) player.openInventory(inventories.peek());
            else player.closeInventory();
        }
    }

    public void manualCloseInventory() {
        inventories.clear();
    }

    public String getScore() {
        return ChatColor.AQUA+(stars.get(EStar.Blue) ? "★" : "☆")+
                ChatColor.RED+(stars.get(EStar.Red) ? "★" : "☆")+
                ChatColor.GREEN+(stars.get(EStar.Green) ? "★" : "☆")+
                ChatColor.YELLOW+(stars.get(EStar.Yellow) ? "★" : "☆")+
                ChatColor.GOLD+" "+gold +"g "+
                ChatColor.WHITE+player.getName();
    }

    public void roll() {
        int result = rdm.nextInt(1,7);
        if (curser == null) roll(result);
        else {
            int cursedResult = result-2;
            if (cursedResult < 0) cursedResult = 0;
            Bukkit.broadcastMessage(getName()+ChatColor.GREEN+" a fait un "+ChatColor.YELLOW+cursedResult+ChatColor.RED+" ("+result+"-2)");
            int lostGold = loseGold(cursedResult);
            curser.gainGold(lostGold);
            Bukkit.broadcastMessage(ChatColor.RED+"La malédiction prend effet, "+ curser.getName()+ChatColor.RED+" récupère "+lostGold+" pièces de "+getName());
            curser = null;
        }

        GameManager.getInstance().playSound(Sound.ENTITY_EXPERIENCE_ORB_PICKUP);
    }

    public void roll(int result) {
        Bukkit.broadcastMessage(getName()+ChatColor.GREEN+" a fait un "+ChatColor.YELLOW+result);
    }

    public int getStarAmount() {
        int amount = 0;
        for (Map.Entry<EStar, Boolean> entry : stars.entrySet()) {
            if (entry.getValue()) amount++;
        }
        return amount;
    }

    public Player getPlayer() {return this.player;}
    public int getGold() {return this.gold;}
    public Map<EStar, Boolean> getStars() {return this.stars;}
    public List<MFItem> getItems() {return this.items;}
    public MFItem getCurrentUsingItem() {return this.currentUsingItem;}
    public MFPlayer getDueller() {return this.dueller;}

    public boolean hasStarDiscount() {return this.starDiscount;}
    public boolean hasTurn() {return this.hasTurn;}
    public boolean isCursed() {return this.curser != null;}
    public boolean isDuelled() {return this.dueller != null;}

    public String getName() {
        return ChatColor.YELLOW+player.getName();
    }
    public Location getLocation() {return player.getLocation();}
    public void sendMessage(String msg) {player.sendMessage(msg);}
    public void teleport(Location loc) {player.teleport(loc);}

    public void setGold(int gold)
    {
        this.gold = gold;
    }
    public void setStars(Map<EStar, Boolean> stars) {this.stars = stars;}
    public void setStarDiscount(boolean state) {this.starDiscount = state;}
    public void setCurser(MFPlayer mfPlayer) {this.curser = mfPlayer;}
    public void setDueller(MFPlayer mfPlayer) {this.dueller = mfPlayer;}
    public void setItems(List<MFItem> items) {this.items = items;}
    public void setCurrentUsingItem(MFItem mfItem) {this.currentUsingItem = mfItem;}

    @Override
    public int compareTo(MFPlayer other)
    {
        if (getStarAmount() > other.getStarAmount()) return 1;
        if (getStarAmount() < other.getStarAmount()) return -1;
        return Integer.compare(gold, other.getGold());
    }
}
