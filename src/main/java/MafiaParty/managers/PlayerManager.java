package MafiaParty.managers;

import MafiaParty.game.MFPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PlayerManager
{
    private static PlayerManager instance;
    public static PlayerManager getInstance() {
        if (instance == null) instance = new PlayerManager();
        return instance;
    }

    private Random rdm = new Random();
    public List<MFPlayer> Players = new ArrayList<>();

    public void addPlayer(Player player) {
        Players.add(new MFPlayer(player));
        ScoreboardManager.getInstance().updateScoreboard();
    }

    public void removePlayer(Player player) {
        for (MFPlayer mfPlayer : Players) {
            if (mfPlayer.Player == player) {
                Players.remove(mfPlayer);
                break;
            }
        }
        ScoreboardManager.getInstance().updateScoreboard();
    }

    public MFPlayer getMFPlayer(Player player) {
        for (MFPlayer mfPlayer : Players) {
            if (mfPlayer.Player == player) return mfPlayer;
        }
        return null;
    }

    public MFPlayer getRandomMFPlayer(MFPlayer exclude) {
        List<MFPlayer> tmp = new ArrayList<>(Players);
        tmp.remove(exclude);
        return tmp.get(rdm.nextInt(tmp.size()));
    }
}
