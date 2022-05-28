package ninja.ebanx.runops;

import org.junit.jupiter.api.Test;

import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class RunopsDriverTest {

    @Test
    void acceptsURL() throws ClassNotFoundException, SQLException {
        Class.forName("ninja.ebanx.runops.RunopsDriver");
        var drv = DriverManager.getDriver("jdbc:runops://xpto");
        assertInstanceOf(RunopsDriver.class, drv);
    }

    @Test
    void getPropertyInfo() {
    }
}