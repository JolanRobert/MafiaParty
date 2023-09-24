package MafiaParty.items;

import MafiaParty.enums.EItem;
import MafiaParty.game.MFPlayer;

public class DeRapide extends MFItem
{
    public DeRapide()
    {
        super(EItem.DeRapide, "Dé Rapide", "Lance un dé 4/5/6", 4);
    }

    public void use(MFPlayer mfPlayer)
    {
        super.use(mfPlayer);
        int result = rdm.nextInt(4,6);
        mfPlayer.roll(result);
    }
}
