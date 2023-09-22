package MafiaParty.game;

import MafiaParty.managers.ScoreboardManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class MFPlayer implements Comparable<MFPlayer>
{
    public Player Player;
    public int GoldAmount;

    private Map<Star, Boolean> stars;

    public MFPlayer(Player player) {
        Player = player;

        stars = new HashMap<Star, Boolean>() {{
            put(Star.Blue, false);
            put(Star.Red, false);
            put(Star.Green, false);
            put(Star.Yellow, false);
        }};

        GoldAmount =  3;
    }

    public void gainStar(Star star) {
        stars.put(star, true);
        ScoreboardManager.getInstance().updateScoreboard();
    }

    public void loseStar(Star star) {
        stars.put(star, false);
        ScoreboardManager.getInstance().updateScoreboard();
    }

    public boolean stealStar(MFPlayer mfTarget, Star star) {
        if (hasStar(star)) {
            Player.sendMessage(ChatColor.RED+"Cette étoile est déjà en votre possesion");
            return false;
        }
        if (!mfTarget.hasStar(star)) {
            Player.sendMessage(ChatColor.RED+"Ce joueur ne possède pas cette étoile");
            return false;
        }

        mfTarget.loseStar(star);
        gainStar(star);
        ScoreboardManager.getInstance().updateScoreboard();
        return true;
    }

    public boolean hasStar(Star star) {
        return stars.get(star);
    }

    public void gainGold(int amount) {
        GoldAmount += amount;
        ScoreboardManager.getInstance().updateScoreboard();
    }

    public int loseGold(int amount) {
        amount = Math.abs(amount);

        int lostAmount = amount;
        GoldAmount -= amount;
        if (GoldAmount < 0) {
            lostAmount += GoldAmount;
            GoldAmount = 0;
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

    public void addItem(ItemStack item) {
        Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE+getName()+" obtient "+item.getItemMeta().getDisplayName());
        Player.getInventory().addItem(item);
    }

    public void useItem(ItemStack item) {
        Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE+getName()+" utilise "+item.getItemMeta().getDisplayName());
        item.setAmount(item.getAmount()-1);
    }

    public String getScore() {
        return Player.getName()+" "+
                ChatColor.AQUA+(stars.get(Star.Blue) ? "★" : "☆")+
                ChatColor.RED+(stars.get(Star.Red) ? "★" : "☆")+
                ChatColor.GREEN+(stars.get(Star.Green) ? "★" : "☆")+
                ChatColor.YELLOW+(stars.get(Star.Yellow) ? "★" : "☆")+
                ChatColor.GOLD+" "+GoldAmount +"g";
    }

    public int getStarAmount() {
        int amount = 0;
        for (Map.Entry<Star, Boolean> entry : stars.entrySet()) {
            if (entry.getValue()) amount++;
        }
        return amount;
    }

    public String getName() {
        return Player.getName();
    }

    public Location getLocation() {return Player.getLocation();}

    @Override
    public int compareTo(MFPlayer other)
    {
        if (getStarAmount() > other.getStarAmount()) return 1;
        if (getStarAmount() < other.getStarAmount()) return -1;
        return Integer.compare(GoldAmount, other.GoldAmount);
    }
}
