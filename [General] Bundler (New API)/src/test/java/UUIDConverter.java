
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

    public static void main(String[] args) {
        UUID original = UUID.fromString("56b89759-c07a-1a8e-a42d-376be0077451");
        UUID modified = forceUUIDv4(original);

        System.out.println("Original : " + original + " (v" + original.version() + ")");
        System.out.println("Modified : " + modified + " (v" + modified.version() + ")");
    }
}
