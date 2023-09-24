package MafiaParty.items;

import MafiaParty.enums.EItem;
import MafiaParty.game.MFPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class DeMaudit extends MFItem
{
    public DeMaudit()
    {
        super(EItem.DeMaudit, "Dé Maudit", "-2 au prochain jet de dé standard d'un joueur, vole autant de pièces que le résultat du lancer", 6);
    }

    public void use(MFPlayer mfPlayer, MFPlayer mfTarget)
    {
        super.use(mfPlayer);
        mfTarget.setCurser(mfPlayer);
        Bukkit.broadcastMessage(mfPlayer.getName()+ ChatColor.RED+" maudit "+mfTarget.getName()+ChatColor.RED+", son prochain jet de dé standard sera réduit et lui fera perdre des pièces");
    }
}
