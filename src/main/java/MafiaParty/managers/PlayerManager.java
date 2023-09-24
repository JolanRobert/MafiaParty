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

    private List<MFPlayer> players = new ArrayList<>();
    private Random rdm = new Random();

    public void addPlayer(Player player) {
        players.add(new MFPlayer(player));
        ScoreboardManager.getInstance().updateScoreboard();
    }

    public void removePlayer(Player player) {
        for (MFPlayer mfPlayer : players) {
            if (mfPlayer.getPlayer() == player) {
                players.remove(mfPlayer);
                break;
            }
        }
        ScoreboardManager.getInstance().updateScoreboard();
    }

    public List<MFPlayer> getMFPlayers() {return this.players;}

    public MFPlayer getMFPlayer(Player player) {
        for (MFPlayer mfPlayer : players) {
            if (mfPlayer.getPlayer() == player) return mfPlayer;
        }
        return null;
    }

    public MFPlayer[] getMFPlayerDuo() {
        MFPlayer[] result = new MFPlayer[2];
        List<MFPlayer> tmp = new ArrayList<>(players);
        if (tmp.size() < 2) return null;

        for (int i = 0; i < result.length; i++) {
            MFPlayer p = tmp.get(rdm.nextInt(tmp.size()));
            result[i] = p;
            tmp.remove(p);
        }

        return result;
    }
}
