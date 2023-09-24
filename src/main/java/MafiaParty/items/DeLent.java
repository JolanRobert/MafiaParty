package MafiaParty.items;

import MafiaParty.enums.EItem;
import MafiaParty.game.MFPlayer;

public class DeLent extends MFItem
{
    public DeLent()
    {
        super(EItem.DeLent, "Dé Lent", "Lance un dé 1/2/3", 4);
    }

    public void use(MFPlayer mfPlayer)
    {
        super.use(mfPlayer);
        int result = rdm.nextInt(1,4);
        mfPlayer.roll(result);
    }
}
