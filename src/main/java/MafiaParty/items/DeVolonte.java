package MafiaParty.items;

import MafiaParty.enums.EItem;
import MafiaParty.game.MFPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class DeVolonte extends MFItem
{
    public DeVolonte()
    {
        super(EItem.DeVolonte, "Dé Volonté", "Choisis le résultat du prochain jet de dé entre 1 et 6", 8);
    }

    public void use(MFPlayer mfPlayer)
    {
        super.use(mfPlayer);
        Bukkit.broadcastMessage(mfPlayer.getName()+ ChatColor.GREEN+" peut choisir le résultat de son dé entre "+ChatColor.YELLOW+"1 et 6");
    }
}
