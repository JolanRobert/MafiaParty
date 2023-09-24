package MafiaParty.items;

import MafiaParty.enums.EItem;
import MafiaParty.game.MFPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;

public class Kidnapping extends MFItem
{
    public Kidnapping()
    {
        super(EItem.Kidnapping, "Kidnapping", "Échange de position avec un joueur choisi", 8);
    }

    public void use(MFPlayer mfPlayer, MFPlayer mfTarget)
    {
        super.use(mfPlayer);
        Location tmp = mfPlayer.getLocation();
        mfPlayer.teleport(mfTarget.getLocation());
        mfTarget.teleport(tmp);
        Bukkit.broadcastMessage(mfPlayer.getName()+ ChatColor.GREEN+" échange de position avec "+mfTarget.getName());
    }
}
