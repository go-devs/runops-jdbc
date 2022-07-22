package ninja.ebanx.runops;

import ninja.ebanx.runops.api.MockedHttpClientBuilder;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RunopsConnectionTest {
    @Test
    void targetAndConfig() throws SQLException, IOException, InterruptedException {
        // Arrange
        Properties info = new Properties();
        info.setProperty("config", "~/.runops/config");
        var url = "jdbc:runops://target-name";
        new MockedHttpClientBuilder()
                .withTarget("target-name", "postgres")
                .build();
        // Act
        var conn = DriverManager.getConnection(url, info);
        // Assert
        assertEquals(((RunopsConnection) conn).getTargetConnection().getName(), "target-name");
        assertEquals(((RunopsConnection) conn).getConfig(), "~/.runops/config");
    }

    @Test
    void createStatement() {
    }
}