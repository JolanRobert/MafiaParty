package MafiaParty.items;

import MafiaParty.enums.EItem;
import MafiaParty.game.MFPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class Duel extends MFItem
{
    public Duel()
    {
        super(EItem.Duel, "Duel", "Défi un joueur en duel sur la prochaine musique, le perdant donne 7 pièces au vainqueur", 4);
    }

    public void use(MFPlayer mfPlayer, MFPlayer mfTarget)
    {
        super.use(mfPlayer);
        mfPlayer.setDueller(mfTarget);
        mfTarget.setDueller(mfPlayer);
        Bukkit.broadcastMessage(mfPlayer.getName()+ ChatColor.GREEN+" défie "+mfTarget.getName()+ChatColor.GREEN+" sur la prochaine musique ! Le gagnant volera 7 pièces au perdant");
    }
}
