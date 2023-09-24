package MafiaParty.items;

import MafiaParty.enums.EItem;
import MafiaParty.game.MFPlayer;

public class DeToutOuRien extends MFItem
{
    public DeToutOuRien()
    {
        super(EItem.DeToutOuRien, "Dé Tout ou Rien", "Lance un dé 0/7", 4);
    }

    public void use(MFPlayer mfPlayer)
    {
        super.use(mfPlayer);
        int result = rdm.nextInt(0, 2) == 0 ? 0 : 7;
        mfPlayer.roll(result);
    }
}
