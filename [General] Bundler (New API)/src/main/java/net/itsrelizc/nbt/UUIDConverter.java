package net.itsrelizc.nbt;

import java.util.UUID;

public class UUIDConverter {

	public static UUID forceUUIDv4(UUID original) {
        long msb = original.getMostSignificantBits();
        long lsb = original.getLeastSignificantBits();

        // Clear version bits (bits 12-15 of MSB)
        msb &= ~(0xF000L);       // clear version bits
        msb |=  (0x4000L);       // set version to 4 (0100)

        return new UUID(msb, lsb);
    }
    
    public static UUID forceUUIDv2(UUID original) {
        long msb = original.getMostSignificantBits();
        long lsb = original.getLeastSignificantBits();

        // Clear version bits (bits 12-15 of MSB)
        msb &= ~(0xF000L);       // Clear version bits
        msb |=  (0x2000L);       // Set version to 2 (0010)

        return new UUID(msb, lsb);
    }

}
