package ninja.ebanx.runops.postgres;

public class ByteConverter {
    private static final int SHORT_BYTES = 2;
    private static final int LONG_BYTES = 4;
    public static int bytesToInt(byte []bytes) {
        if ( bytes.length == 1 ) {
            return bytes[0];
        }
        if ( bytes.length == SHORT_BYTES ) {
            return int2(bytes, 0);
        }
        if ( bytes.length == LONG_BYTES ) {
            return int4(bytes, 0);
        } else {
            throw new IllegalArgumentException("Argument bytes is empty");
        }
    }

    public static short int2(byte[] bytes, int idx) {
        return (short) (((bytes[idx] & 255) << 8) + ((bytes[idx + 1] & 255)));
    }

    public static int int4(byte[] bytes, int idx) {
        return
                ((bytes[idx] & 255) << 24)
                        + ((bytes[idx + 1] & 255) << 16)
                        + ((bytes[idx + 2] & 255) << 8)
                        + ((bytes[idx + 3] & 255));
    }
}
