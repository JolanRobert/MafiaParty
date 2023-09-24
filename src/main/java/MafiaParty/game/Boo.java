package MafiaParty.game;

import MafiaParty.enums.EStar;
import MafiaParty.managers.GameManager;
import MafiaParty.managers.InventoryManager;
import MafiaParty.managers.PlayerManager;
import MafiaParty.utils.ItemEditor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class Boo
{
    private static Boo instance;
    public static Boo getInstance() {
        if (instance == null) instance = new Boo();
        return instance;
    }

    private Inventory invBooTarget;
    private Inventory invBooChoice;

    private MFPlayer mfTarget;

    private Random rdm = new Random();

    public Boo() {
        createBooChoiceInventory();
    }

    private void createBooChoiceInventory() {
        invBooChoice = Bukkit.createInventory(null, 9, "Voler ?");

        ItemStack gold = new ItemStack(Material.GOLD_INGOT);
        ItemEditor.setDisplayName(gold, ChatColor.YELLOW+"Pièces "+ChatColor.GOLD+"(Gratuit)");
        invBooChoice.addItem(gold);

        ItemStack star = new ItemStack(Material.NETHER_STAR);
        ItemEditor.setDisplayName(star, ChatColor.YELLOW+"Étoile "+ChatColor.GOLD+"(30g)");
        invBooChoice.addItem(star);

        invBooChoice.setItem(8, InventoryManager.getInstance().getBackButton());
    }

    public Inventory createBooTargetInventory(MFPlayer mfPlayer) {
        List<MFPlayer> players = PlayerManager.getInstance().getMFPlayers();
        int invSize = players.size() % 9 == 0 ? players.size() : players.size() + (9 - (players.size() % 9));
        invBooTarget = Bukkit.createInventory(null, invSize, "Cible");

        for (MFPlayer mfp : players) {
            if (mfp == mfPlayer) continue;
            ItemStack pHead = InventoryManager.getInstance().getCustomHead(mfp.getPlayer());
            ItemEditor.setDisplayName(pHead, mfp.getName());
            invBooTarget.addItem(pHead);
        }

        invBooTarget.setItem(invBooTarget.getSize()-1, InventoryManager.getInstance().getBackButton());
        return invBooTarget;
    }

    public void setTarget(MFPlayer mfTarget) {
        this.mfTarget = mfTarget;
    }

    public void processChoice(MFPlayer mfPlayer, ItemStack item) {
        if (item.getType() == Material.GOLD_INGOT) {
            int stealAmount = mfPlayer.stealGold(mfTarget, rdm.nextInt(4,7));
            Bukkit.broadcastMessage(ChatColor.GRAY+"■ "+mfPlayer.getName()+ChatColor.GRAY+" vole "+stealAmount+" pièces à "+mfTarget.getName());
            GameManager.getInstance().playSound(Sound.ENTITY_GHAST_HURT);
            mfPlayer.closeInventory(true);
        }
        else if (item.getType() == Material.NETHER_STAR) {
            if (mfPlayer.getGold() < 30) {
                mfPlayer.sendMessage(ChatColor.RED+"Pas assez de pièces pour voler une étoile");
                mfPlayer.getPlayer().playSound(mfPlayer.getLocation(), Sound.ENTITY_VILLAGER_NO, Integer.MAX_VALUE,1);
                return;
            }
            List<EStar> stars = new ArrayList<>(Arrays.asList(EStar.values()));
            Collections.shuffle(stars);
            for (EStar eStar : stars) {
                if (mfPlayer.stealStar(mfTarget, eStar)) {
                    mfPlayer.loseGold(30);
                    Bukkit.broadcastMessage(ChatColor.GRAY+"■ "+mfPlayer.getName()+ChatColor.GRAY+" vole"+eStar.getColor()+" ★"+ChatColor.GRAY+" à "+mfTarget.getName()+ChatColor.RED+" (30g)");
                    GameManager.getInstance().playSound(Sound.ENTITY_GHAST_HURT);
                    mfPlayer.closeInventory(true);
                    return;
                }
            }
            mfPlayer.sendMessage(ChatColor.RED+"Impossible de voler une étoile à ce joueur");
            mfPlayer.getPlayer().playSound(mfPlayer.getLocation(), Sound.ENTITY_VILLAGER_NO, Integer.MAX_VALUE,1);
        }
    }

    public Inventory getBooTargetInventory() {return this.invBooTarget;}
    public Inventory getBooChoiceInventory() {return this.invBooChoice;}
}
