import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException, IOException {
        if (args.length == 2 && args[0].equals("login")) {
            LoginUtility.runopsLogin(args[1]);
            return;
        }
        usageSample();
    }

    private static void usageSample() throws SQLException {
        var connString = "jdbc:runops://read-akkad-production";

        try (var conn = DriverManager.getConnection(connString); var st = conn.createStatement()) {
            st.execute("explain (format json) select * from merchant.merchants limit 10");
            var rs = st.getResultSet();
            while(rs.next()) {
                System.out.println(rs.getString(1));
            }
        }
    }
}
