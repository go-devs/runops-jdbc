package ninja.ebanx.runops.utils;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BlackHole {
    public static void close(@Nullable Connection con) {
        try {
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            /* ignore for now */
        }
    }

    public static void close(@Nullable Statement s) {
        try {
            if (s != null) {
                s.close();
            }
        } catch (SQLException e) {
            /* ignore for now */
        }
    }

    public static void close(@Nullable ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            /* ignore for now */
        }
    }
}