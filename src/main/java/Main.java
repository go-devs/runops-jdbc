import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        var connString = "jdbc:runops://read-akkad-production";

        try (var con = DriverManager.getConnection(connString); var st = con.createStatement()) {
            st.execute("select version()");

        }
    }
}
