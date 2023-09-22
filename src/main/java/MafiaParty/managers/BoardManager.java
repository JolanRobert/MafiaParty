package MafiaParty.managers;

import MafiaParty.game.MFPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Random;

public class BoardManager
{
    private static BoardManager instance;
    public static BoardManager getInstance() {
        if (instance == null) instance = new BoardManager();
        return instance;
    }

    private Random rdm = new Random();

    public void squareBlue(MFPlayer mfPlayer) {
        Bukkit.broadcastMessage(ChatColor.AQUA+"■ "+mfPlayer.getName()+" gagne 2 pièces");
        mfPlayer.gainGold(2);
    }

    public void squareRed(MFPlayer mfPlayer) {
        int val = rdm.nextInt(1,6);
        if (val == 1) {
            Bukkit.broadcastMessage(ChatColor.RED+"■ Toutes les pièces sont réparties équitablement entre les joueurs");

            List<MFPlayer> players = PlayerManager.getInstance().Players;
            int totalGold = 0;
            for (MFPlayer p : players) {
                totalGold += p.GoldAmount;
            }
            int splitGold = totalGold/players.size();
            for (MFPlayer p : players) {
                if (p.GoldAmount > splitGold) {
                    int lostAmount = p.GoldAmount-splitGold;
                    p.loseGold(lostAmount);
                    Bukkit.broadcastMessage(ChatColor.RED+p.getName()+" perd "+lostAmount+"g");
                }
                else {
                    int gainAmount = splitGold-p.GoldAmount;
                    p.gainGold(gainAmount);
                    Bukkit.broadcastMessage(ChatColor.GREEN+p.getName()+" gagne "+gainAmount+"g");
                }
            }
        }

        else if (val == 2) {
            Bukkit.broadcastMessage(ChatColor.RED+"■ "+mfPlayer.getName()+" perd la moitié de ses pièces");
            int lostAmount = mfPlayer.GoldAmount/2;
            mfPlayer.loseGold(lostAmount);
            Bukkit.broadcastMessage(ChatColor.RED+mfPlayer.getName()+" perd "+lostAmount+"g");
        }

        else if (val == 3) {
            Bukkit.broadcastMessage(ChatColor.RED+"■ "+mfPlayer.getName()+" retourne à la case départ");
        }

        else if (val == 4) {
            Bukkit.broadcastMessage(ChatColor.RED+"■ "+mfPlayer.getName()+" aura seulement 15s d'écoute à la prochaine musique");
        }

        else {
            Bukkit.broadcastMessage(ChatColor.RED+"■ "+mfPlayer.getName()+" donne une pièce à tous les autres joueurs");

            List<MFPlayer> players = PlayerManager.getInstance().Players;
            for (MFPlayer p : players) {
                if (mfPlayer == p) {
                    p.loseGold(players.size()-1);
                    Bukkit.broadcastMessage(ChatColor.RED+p.getName()+" perd "+(players.size()-1)+"g");
                }
                else {
                    p.gainGold(1);
                    Bukkit.broadcastMessage(ChatColor.GREEN+p.getName()+" gagne 1g");
                }
            }
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO,Integer.MAX_VALUE,1);
        }
    }

    public void squareGreen(MFPlayer mfPlayer) {
        int val = rdm.nextInt(1,6);

        if (val == 1) {
            Bukkit.broadcastMessage(ChatColor.GREEN+"■ "+mfPlayer.getName()+" gagne 4 pièces");
            mfPlayer.gainGold(4);
        }

        else if (val == 2) {
            Bukkit.broadcastMessage(ChatColor.GREEN+"■ "+mfPlayer.getName()+" gagne un objet aléatoire");
            mfPlayer.addItem(Shop.getInstance().getRandomItem());
        }

        else if (val == 3) {
            Bukkit.broadcastMessage(ChatColor.GREEN+"■ "+mfPlayer.getName()+" peut rejouer");
        }

        else if (val == 4) {
            MFPlayer other = PlayerManager.getInstance().getRandomMFPlayer(mfPlayer);
            Location loc = mfPlayer.getLocation();
            mfPlayer.Player.teleport(other.getLocation());
            other.Player.teleport(loc);

            Bukkit.broadcastMessage(ChatColor.GREEN+"■ "+mfPlayer.getName()+" échange sa position avec "+other.getName());
        }

        else {
            Bukkit.broadcastMessage(ChatColor.GREEN+"■ "+mfPlayer.getName()+" peut choisir le prochain thème");
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_YES,Integer.MAX_VALUE,1);
        }
    }

    public void squarePurple(MFPlayer mfPlayer) {
        Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE+"■ "+mfPlayer.getName()+" accède au magasin");
        mfPlayer.Player.openInventory(InventoryManager.getInstance().getShopInventory());
    }
}
