package ninja.ebanx.runops;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class RunopsDriverTest {

    private static final String CONNECTION_STRING = "jdbc:runops://xpto";

    @Test
    void acceptsURL() throws SQLException {
        var drv = DriverManager.getDriver(CONNECTION_STRING);
        assertInstanceOf(RunopsDriver.class, drv);
    }

    @Test
    void getPropertyInfo() throws SQLException, IOException {
        // Arrange
        Path path = Files.createTempFile("testFile", ".txt");
        Driver drv = DriverManager.getDriver(CONNECTION_STRING);
        var p = new Properties();
        p.setProperty("config", path.toString());

        // Act
        DriverPropertyInfo[] dpi = drv.getPropertyInfo(CONNECTION_STRING, p);

        // Assert
        assertEquals(dpi[0].name, "config");
        assertEquals(dpi[0].value, "~/.runops/config");
    }

    @Test
    void connect() throws SQLException {
        // Arrange
        // Act
        var conn = DriverManager.getConnection(CONNECTION_STRING);
        // Assert
        assertInstanceOf(RunopsConnection.class, conn);
    }
}