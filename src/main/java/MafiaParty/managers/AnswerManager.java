package MafiaParty.managers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AnswerManager
{
    private static AnswerManager instance;
    public static AnswerManager getInstance() {
        if (instance == null) instance = new AnswerManager();
        return instance;
    }

    private class PlayerAnswer implements Comparable<PlayerAnswer> {
        private Player player;
        private String msg;
        private float time;

        public PlayerAnswer(Player player, String msg, float time) {
            this.player = player;
            this.msg = msg;
            this.time = time;
        }

        @Override
        public int compareTo(PlayerAnswer other)
        {
            if (time < other.time) return -1;
            else if (time > other.time) return 1;
            return 0;
        }
    }

    private List<PlayerAnswer> answers = new ArrayList<>();
    private boolean isActive = false;
    private float startTime;

    private static final DecimalFormat df = new DecimalFormat("0.000");

    public void addMessage(Player player, String msg) {
        if (!isActive) return;

        if (!hasPlayer(player)) {
            PlayerAnswer pa = new PlayerAnswer(player, msg, getTime());
            answers.add(pa);

            if (answers.size() == PlayerManager.getInstance().Players.size()) {
                Bukkit.broadcastMessage(ChatColor.GREEN+"Tous les joueurs ont répondu !");
                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP,Integer.MAX_VALUE,1);
                }
            }
            return;
        }

        for (PlayerAnswer pa : answers) {
            if (pa.player == player) {
                pa.msg = msg;
                pa.time = getTime();
                break;
            }
        }
    }

    private boolean hasPlayer(Player player) {
        for (PlayerAnswer pa : answers) {
            if (pa.player == player) return true;
        }
        return false;
    }

    public float getTime() {
        float elapsedTime = System.nanoTime()-startTime;
        double elapsedTimeInSecond = (double) elapsedTime / 1_000_000_000;
        return Float.parseFloat(df.format(elapsedTimeInSecond));
    }

    public void printAnswers() {
        Collections.sort(answers);

        for (PlayerAnswer pa : answers) {
            String msg = ChatColor.GOLD+"["+pa.player.getName()+" | "+pa.time+"s] "+ChatColor.GREEN+pa.msg;
            Bukkit.broadcastMessage(msg);
        }
    }

    public void setActive(boolean state) {
        if (isActive == state) return;
        isActive = state;

        if (isActive) {
            answers.clear();
            startTime = System.nanoTime();
        }
    }

    public boolean isActive() {return isActive;}
}
