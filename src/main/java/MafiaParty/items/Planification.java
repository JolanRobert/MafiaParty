package MafiaParty.items;

import MafiaParty.enums.EItem;
import MafiaParty.game.MFPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class Planification extends MFItem
{
    public Planification()
    {
        super(EItem.Planification, "Planification", "Vole un objet aléatoire à un joueur, vous pouvez réutiliser un objet pendant ce tour", 6);
    }

    public void use(MFPlayer mfPlayer, MFPlayer mfTarget)
    {
        super.use(mfPlayer);
        MFItem stolenItem = mfTarget.getRandomItem();
        mfPlayer.addItem(stolenItem);
        mfTarget.removeItem(stolenItem);
        Bukkit.broadcastMessage(mfPlayer.getName()+ ChatColor.RED+" vole "+stolenItem.getName()+ChatColor.RED+" à "+mfTarget.getName());
    }
}
