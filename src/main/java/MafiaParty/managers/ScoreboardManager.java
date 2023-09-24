package MafiaParty.managers;

import MafiaParty.game.MFPlayer;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScoreboardManager
{
    private static ScoreboardManager instance;
    public static ScoreboardManager getInstance() {
        if (instance == null) instance = new ScoreboardManager();
        return instance;

    }

    private Scoreboard scoreboard;
    private Objective objective;

    public ScoreboardManager() {
        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        objective = scoreboard.registerNewObjective("game", Criteria.DUMMY, "Game");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    public void updateScoreboard() {
        List<MFPlayer> players = new ArrayList<>(PlayerManager.getInstance().getMFPlayers());
        Collections.sort(players);

        for (String entry : scoreboard.getEntries()) {
            scoreboard.resetScores(entry);
        }

        for (int i = 0; i < players.size(); i++) {
            MFPlayer mfPlayer = players.get(i);
            Score s = objective.getScore(mfPlayer.getScore());
            s.setScore(i);
        }
    }

    public Scoreboard getScoreboard() {
        return scoreboard;
    }
}
