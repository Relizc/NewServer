
import java.util.UUID;

import net.itsrelizc.players.locales.Locale;
import net.itsrelizc.players.locales.Locale.Language;
import net.itsrelizc.string.StringUtils;

public class UUIDConverter {

	public static UUID forceUUIDv4(UUID original) {
        long msb = original.getMostSignificantBits();
        long lsb = original.getLeastSignificantBits();

        // Clear version bits (bits 12-15 of MSB)
        msb &= ~(0xF000L);       // clear version bits
        msb |=  (0x4000L);       // set version to 4 (0100)

        return new UUID(msb, lsb);
    }
	
	

    public static void main(String[] args) {
    	//Locale.load_all();
    	String ans = StringUtils.wrapWithColor("§f微型航天工程的奇迹，适用于射弹作战。最初的灵感源自飞机上的全尺寸飞行控制单元 (FCU)，这个小型动力装置可以计算实时轨迹修正、风向调整以及飞行过程中的目标跟踪。", Language.ZH_CN);
    	
    	System.out.println(ans);
    }
}
