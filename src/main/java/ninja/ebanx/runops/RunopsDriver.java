package ninja.ebanx.runops;

import java.sql.*;
import java.util.Objects;
import java.util.Properties;
import java.util.logging.Logger;

public class RunopsDriver implements Driver {

    private static final String URI_PREFIX = "jdbc:runops";

    static {
        try {
            java.sql.DriverManager.registerDriver(new RunopsDriver());
        } catch (SQLException ex) {
            throw new RuntimeException("Can't register driver!");
        }
    }

    @Override
    public Connection connect(String url, Properties info) throws SQLException {
        if (!this.acceptsURL(url)) {
            throw new SQLException("invalid url");
        }
        return new RunopsConnection(url, info);
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
        return 0;
    }

    @Override
    public int getMinorVersion() {
        return 0;
    }

    @Override
    public boolean jdbcCompliant() {
        return false;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }
}
