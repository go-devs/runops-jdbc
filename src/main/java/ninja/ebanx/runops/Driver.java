package ninja.ebanx.runops;

import java.sql.*;
import java.util.Objects;
import java.util.Properties;
import java.util.logging.Logger;

public class Driver implements java.sql.Driver {

    private static final String URI_PREFIX = "jdbc:runops";
    private static final Logger logger = Logger.getLogger("RUNOPS-JDBC");

    static {
        try {
            java.sql.DriverManager.registerDriver(new Driver());
        } catch (SQLException ex) {
            throw new RuntimeException("Can't register driver!");
        }
    }

    @Override
    public Connection connect(String url, Properties info) throws SQLException {
        if (!this.acceptsURL(url)) {
            throw new SQLException("invalid url");
        }
        return new RunopsConnection(url, info, this.getParentLogger());
    }

    @Override
    public boolean acceptsURL(String url) {
        return url.startsWith(URI_PREFIX);
    }

    @Override
    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
        if (Objects.equals(info.getProperty("config"), "")) {
            throw new SQLException("invalid config file");
        }
        return new DriverPropertyInfo[] {
                new DriverPropertyInfo("config", "~/.runops/config")
        };
    }

    @Override
    public int getMajorVersion() {
        return Version.MAJOR_VERSION;
    }

    @Override
    public int getMinorVersion() {
        return Version.MINOR_VERSION;
    }

    @Override
    public boolean jdbcCompliant() {
        return false;
    }

    @Override
    public Logger getParentLogger() {
        return Driver.logger;
    }

    public static class Version {
        public static final String DRIVER_NAME = "Runops JDBC Driver";
        public static final int MAJOR_VERSION = 0;
        public static final int MINOR_VERSION = 1;

        public static String driverVersion() {
            return String.format("%d.%d", MAJOR_VERSION, MINOR_VERSION);
        }
    }
}
