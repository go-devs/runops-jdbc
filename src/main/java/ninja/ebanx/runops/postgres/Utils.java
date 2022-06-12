package ninja.ebanx.runops.postgres;

import org.checkerframework.checker.nullness.qual.PolyNull;

import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

public class Utils {
    public static byte @PolyNull [] encodeString(@PolyNull String str) {
        return str.getBytes(StandardCharsets.UTF_8);
    }

    public static String escapeString(String value) throws SQLException {
        var sbuf = new StringBuilder((value.length() + 10) / 10 * 11);
        for (int i = 0; i < value.length(); ++i) {
            char ch = value.charAt(i);
            if (ch == '\0') {
                throw new SQLException("Zero bytes may not occur in string parameters.", "22023");
            }
            if (ch == '\'') {
                sbuf.append('\'');
            }
            sbuf.append(ch);
        }
        return sbuf.toString();
    }
}
