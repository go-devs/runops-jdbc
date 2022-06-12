package ninja.ebanx.runops;

import org.junit.jupiter.api.Test;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class RunopsConnectionTest {
    @Test
    void targetAndConfig() throws SQLException {
        // Arrange
        Properties info = new Properties();
        info.setProperty("config", "~/.runops/config");
        var url = "jdbc:runops://target-name";
        // Act
        var conn = DriverManager.getConnection(url, info);
        // Assert
        assertEquals(((RunopsConnection)conn).getTarget(), "target-name");
        assertEquals(((RunopsConnection)conn).getConfig(), "~/.runops/config");
    }

    @Test
    void createStatement() {
    }
}