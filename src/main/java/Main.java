import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        var connString = "jdbc:runops://read-akkad-production";

        try (var conn = DriverManager.getConnection(connString); var st = conn.createStatement()) {
            st.execute("select * from merchant.merchants");
            var rs = st.getResultSet();
            while(rs.next()) {
                System.out.println(rs.getString(1) + "\t" + rs.getString(2));
            }
        }
    }
}
