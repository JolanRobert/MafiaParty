package MafiaParty.managers;

import MafiaParty.game.MFPlayer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
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
    private long startTime;

    private static final DecimalFormat df = new DecimalFormat("0.00");

    public void addMessage(Player player, String msg) {
        if (!isActive) return;

        if (!hasPlayer(player)) {
            PlayerAnswer pa = new PlayerAnswer(player, msg, getTime());
            answers.add(pa);

            if (answers.size() == PlayerManager.getInstance().getMFPlayers().size()) {
                Bukkit.broadcastMessage(ChatColor.GREEN+"Tous les joueurs ont répondu !");
                GameManager.getInstance().playSound(Sound.ENTITY_EXPERIENCE_ORB_PICKUP);
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
        long elapsedTime = System.nanoTime()-startTime;
        double elapsedTimeInSecond = (double) elapsedTime / 1_000_000_000;
        return Float.parseFloat(df.format(elapsedTimeInSecond));
    }

    public void printAnswers() {
        Collections.sort(answers);

        for (PlayerAnswer pa : answers) {
            String msg = ChatColor.GOLD+"["+ChatColor.YELLOW+pa.player.getName()+ChatColor.GOLD+" | "+ChatColor.YELLOW+pa.time+"s"+ChatColor.GOLD+"] "+ChatColor.GREEN+pa.msg;
            Bukkit.broadcastMessage(msg);
        }
    }

    public void resolve(MFPlayer mfPlayer) {
        mfPlayer.sendMessage(ChatColor.AQUA+"> Attribution des pièces <");
        int[] rewards = new int[] {10,8,4};
        for (PlayerAnswer pa : answers) {
            TextComponent msg = new TextComponent(ChatColor.YELLOW + pa.player.getName());
            for (Integer r : rewards) {
                TextComponent click = new TextComponent(ChatColor.GOLD+" [+"+r+"g]");
                click.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mp gold "+pa.player.getName()+" "+r));
                click.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.GOLD+"Click")));
                msg.addExtra(click);
            }
            mfPlayer.getPlayer().spigot().sendMessage(msg);
        }

        mfPlayer.sendMessage(ChatColor.AQUA+"\n> Résolution des duels <");
        List<MFPlayer> checkedPlayers = new ArrayList<>();
        for (MFPlayer mfp : PlayerManager.getInstance().getMFPlayers()) {
            if (!mfp.isDuelled() || checkedPlayers.contains(mfp)) continue;
            MFPlayer mfDueller = mfp.getDueller();
            TextComponent p1 = new TextComponent(mfp.getName());
            p1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mp duel "+mfp.getPlayer().getName()+" "+mfDueller.getPlayer().getName()+" "+mfp.getPlayer().getName()));
            p1.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.YELLOW+"Click")));
            TextComponent p2 = new TextComponent(mfDueller.getName());
            p2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mp duel "+mfp.getPlayer().getName()+" "+mfDueller.getPlayer().getName()+" "+mfDueller.getPlayer().getName()));
            p2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.YELLOW+"Click")));
            p1.addExtra(" vs ");
            p1.addExtra(p2);
            mfPlayer.getPlayer().spigot().sendMessage(p1);

            checkedPlayers.add(mfp);
            checkedPlayers.add(mfDueller);
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
