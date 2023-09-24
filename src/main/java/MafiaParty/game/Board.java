package MafiaParty.game;

import MafiaParty.enums.EStar;
import MafiaParty.items.MFItem;
import MafiaParty.managers.GameManager;
import MafiaParty.managers.InventoryManager;
import MafiaParty.managers.PlayerManager;
import MafiaParty.managers.Shop;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;

import java.util.*;

public class Board
{
    private Random rdm = new Random();

    public void squareBlue(MFPlayer mfPlayer) {
        Bukkit.broadcastMessage(ChatColor.AQUA+"■ "+mfPlayer.getName()+ChatColor.AQUA+" gagne 3 pièces");
        mfPlayer.gainGold(3);

        GameManager.getInstance().playSound(Sound.ENTITY_CAT_AMBIENT);
    }

    public void squareRed(MFPlayer mfPlayer) {
        Bukkit.broadcastMessage(ChatColor.RED+"■ "+mfPlayer.getName()+ChatColor.RED+" perd 3 pièces");
        mfPlayer.loseGold(3);

        GameManager.getInstance().playSound(Sound.ENTITY_WOLF_WHINE);
    }

    public void squareGreen(MFPlayer mfPlayer) {
        int val = rdm.nextInt(1,7);

        if (val == 1) {
            Bukkit.broadcastMessage(ChatColor.GREEN+"■ "+mfPlayer.getName()+ChatColor.GREEN+" gagne 5 pièces");
            mfPlayer.gainGold(5);
        }

        else if (val == 2) {
            Bukkit.broadcastMessage(ChatColor.GREEN+"■ "+mfPlayer.getName()+ChatColor.GREEN+" gagne 7 pièces");
            mfPlayer.gainGold(7);
        }

        else if (val == 3) {
            Bukkit.broadcastMessage(ChatColor.GREEN+"■ "+mfPlayer.getName()+ChatColor.GREEN+" gagne 10 pièces");
            mfPlayer.gainGold(10);
        }

        else if (val == 4) {
            int goldAmount = 0;
            for (MFPlayer p : PlayerManager.getInstance().getMFPlayers()) {
                if (mfPlayer == p) continue;
                goldAmount += p.loseGold(2);
            }
            Bukkit.broadcastMessage(ChatColor.GREEN+"■ "+mfPlayer.getName()+ChatColor.GREEN+" vole 2 pièces à tout ses adversaires ("+goldAmount+"g)");
            mfPlayer.gainGold(goldAmount);
        }

        else if (val == 5) {
            MFItem mfDiceItem = Shop.getInstance().getRandomDiceMFItem();
            Bukkit.broadcastMessage(ChatColor.GREEN+"■ "+mfPlayer.getName()+ChatColor.GREEN+" obtient un objet dé aléatoire "+ChatColor.AQUA+"("+mfDiceItem.getName()+")");
            mfPlayer.addItem(mfDiceItem);
        }

        else {
            Bukkit.broadcastMessage(ChatColor.GREEN+"■ La prochaine étoile de "+ mfPlayer.getName()+ChatColor.GREEN+" sera à moitié prix !");
            mfPlayer.setStarDiscount(true);
        }

        GameManager.getInstance().playSound(Sound.ENTITY_VILLAGER_YES);
    }

    public void squareOrange(MFPlayer mfPlayer) {
        MFItem mfItem = Shop.getInstance().getRandomMFItem();
        Bukkit.broadcastMessage(ChatColor.GOLD+"■ "+mfPlayer.getName()+ChatColor.GOLD+" obtient un objet aléatoire "+ChatColor.AQUA+"("+mfItem.getName()+")");
        mfPlayer.addItem(mfItem);

        GameManager.getInstance().playSound(Sound.ENTITY_AXOLOTL_SPLASH);
    }

    public void squarePink() {
        MFPlayer[] mfPlayers = PlayerManager.getInstance().getMFPlayerDuo();
        if (mfPlayers == null) return;
        MFPlayer mfp1 = mfPlayers[0];
        MFPlayer mfp2 = mfPlayers[1];

        GameManager.getInstance().playSound(Sound.ENTITY_WITCH_CELEBRATE);

        int val = rdm.nextInt(1,6);
        if (val == 1) {
            Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE+"■ "+mfp1.getName()+ChatColor.LIGHT_PURPLE+" et "+mfp2.getName()+ChatColor.LIGHT_PURPLE+
                    " échangent leurs "+ChatColor.YELLOW+"pièces");
            mfp1.swapGold(mfp2);
        }

        else if (val == 2) {
            Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE+"■ "+mfp1.getName()+ChatColor.LIGHT_PURPLE+" et "+mfp2.getName()+ChatColor.LIGHT_PURPLE+
                    " échangent leurs "+ChatColor.YELLOW+"étoiles");
            mfp1.swapStars(mfp2);
        }

        else if (val == 3) {
            Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE+"■ "+mfp1.getName()+ChatColor.LIGHT_PURPLE+" et "+mfp2.getName()+ChatColor.LIGHT_PURPLE+
                    " échangent leurs "+ChatColor.YELLOW+"pièces et étoiles");
            mfp1.swapGold(mfp2);
            mfp1.swapStars(mfp2);
        }
        else {
            Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE+"■ "+mfp1.getName()+ChatColor.LIGHT_PURPLE+" et "+mfp2.getName()+ChatColor.LIGHT_PURPLE+
                    " échangent leurs "+ChatColor.YELLOW+"objets");
            mfp1.swapItems(mfp2);
        }
    }

    public void squarePurple(MFPlayer mfPlayer) {
        GameManager.getInstance().playSound(Sound.ENTITY_ENDER_DRAGON_GROWL);

        int val = rdm.nextInt(1,7);
        if (val == 1) {
            Bukkit.broadcastMessage(ChatColor.DARK_PURPLE+"■ Toutes les pièces sont réparties équitablement entre les joueurs");

            List<MFPlayer> players = PlayerManager.getInstance().getMFPlayers();
            int totalGold = 0;
            for (MFPlayer p : players) {
                totalGold += p.getGold();
            }
            int splitGold = totalGold/players.size();
            for (MFPlayer p : players) {
                if (p.getGold() > splitGold) {
                    int lostAmount = p.getGold()-splitGold;
                    p.loseGold(lostAmount);
                    Bukkit.broadcastMessage(p.getName()+ChatColor.RED+" perd "+lostAmount+"g");
                }
                else {
                    int gainAmount = splitGold-p.getGold();
                    p.gainGold(gainAmount);
                    Bukkit.broadcastMessage(p.getName()+ChatColor.GREEN+" gagne "+gainAmount+"g");
                }
            }
        }

        else if (val == 2) {
            Bukkit.broadcastMessage(ChatColor.DARK_PURPLE+"■ "+mfPlayer.getName()+ChatColor.DARK_PURPLE+" perd la moitié de ses pièces");
            int amount = mfPlayer.getGold()%2 == 0 ? mfPlayer.getGold()/2 : mfPlayer.getGold()/2+1;
            int lostAmount = mfPlayer.loseGold(amount);
            Bukkit.broadcastMessage(mfPlayer.getName()+ChatColor.RED+" perd "+lostAmount+"g");
        }

        else if (val == 3) {
            Bukkit.broadcastMessage(ChatColor.DARK_PURPLE+"■ "+mfPlayer.getName()+ChatColor.DARK_PURPLE+" perd une étoile");
            int starAmount = mfPlayer.getStarAmount();
            if (starAmount == 0) return;

            int rdmStarIndex = rdm.nextInt(starAmount);
            EStar loseStar = null;
            for (Map.Entry<EStar,Boolean> entry : mfPlayer.getStars().entrySet()) {
                if (!entry.getValue()) continue;
                if (rdmStarIndex == 0) {
                    loseStar = entry.getKey();
                }
                else rdmStarIndex--;
            }
            Bukkit.broadcastMessage(mfPlayer.getName()+ChatColor.RED+" perd "+loseStar.getColor()+"★");
            mfPlayer.loseStar(loseStar);
        }

        else if (val == 4) {
            Bukkit.broadcastMessage(ChatColor.DARK_PURPLE+"■ "+mfPlayer.getName()+ChatColor.DARK_PURPLE+" donne 10 pièces au dernier");
            List<MFPlayer> players = new ArrayList<>(PlayerManager.getInstance().getMFPlayers());
            Collections.sort(players);
            MFPlayer lastPlayer = players.get(0);

            int lostAmount = mfPlayer.loseGold(10);
            Bukkit.broadcastMessage(mfPlayer.getName()+ChatColor.RED+" perd "+lostAmount+"g");
            lastPlayer.gainGold(lostAmount);
            Bukkit.broadcastMessage(lastPlayer.getName()+ChatColor.GREEN+" gagne "+lostAmount+"g");
        }

        else if (val == 5) {
            Bukkit.broadcastMessage(ChatColor.DARK_PURPLE+"■ "+mfPlayer.getName()+ChatColor.DARK_PURPLE+" perd tout ses objets");
            mfPlayer.removeAllItems();
        }

        else {
            Bukkit.broadcastMessage(ChatColor.DARK_PURPLE+"■ Tous les joueurs échangent leurs positions aléatoirement");
            List<Location> locations = new ArrayList<>();
            List<MFPlayer> players = PlayerManager.getInstance().getMFPlayers();
            for (MFPlayer mfp : players) {
                locations.add(mfp.getLocation());
            }
            Collections.shuffle(locations);
            for (int i = 0; i < players.size(); i++) {
                players.get(i).teleport(locations.get(i));
            }
        }
    }

    public void squareWhite(MFPlayer mfPlayer) {
        Bukkit.broadcastMessage(ChatColor.WHITE+"■ "+mfPlayer.getName()+ChatColor.WHITE+" accède au magasin");
        mfPlayer.openInventory(InventoryManager.getInstance().getShopInventory());
    }

    public void squareGray(MFPlayer mfPlayer) {
        mfPlayer.openInventory(Boo.getInstance().createBooTargetInventory(mfPlayer));
    }

    public void squareYellow(MFPlayer mfPlayer) {
        mfPlayer.openInventory(InventoryManager.getInstance().createStarInventory(mfPlayer));
    }
}
