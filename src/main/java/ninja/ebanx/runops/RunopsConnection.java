package ninja.ebanx.runops;

import ninja.ebanx.runops.postgres.PgDatabaseMetaData;
import ninja.ebanx.runops.postgres.ServerVersion;
import ninja.ebanx.runops.postgres.TypeInfo;

import java.net.URI;
import java.sql.*;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.logging.Logger;

public class RunopsConnection implements Connection {
    private final String target;
    private final String config;
    private final Logger logger;
    private final ServerVersion version;
    private TypeInfo typeInfo;
    private String catalog;
    private final String url;

    public String getTarget() {
        return target;
    }

    public String getConfig() {
        return config;
    }

    public Logger getLogger() {
        return logger;
    }

    public RunopsConnection(String url, Properties info, Logger logger) {
        this.url = url;
        URI uri = URI.create(url.substring(5));
        this.target = uri.getHost();
        this.config = info.getProperty("config");
        this.logger = logger;
        logger.info("connection created");
        // TODO get current version
        version = ServerVersion.v10;
    }

    @Override
    public Statement createStatement() throws SQLException {
        return new RunopsStatement(target, logger);
    }

    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return new RunopsPreparedStatement(sql, target, logger);
    }

    @Override
    public CallableStatement prepareCall(String sql) {
        throw new UnsupportedOperationException("prepareCall not supported");
    }

    @Override
    public String nativeSQL(String sql) {
        return sql;
    }

    @Override
    public void setAutoCommit(boolean autoCommit) {}

    @Override
    public boolean getAutoCommit() {
        return false;
    }

    @Override
    public void commit() {}

    @Override
    public void rollback() {}

    @Override
    public void close() {
        logger.info("connection closed");
    }

    @Override
    public boolean isClosed() {
        return false;
    }

    @Override
    public DatabaseMetaData getMetaData() {
        return new PgDatabaseMetaData(this);
    }

    @Override
    public void setReadOnly(boolean readOnly) {}

    @Override
    public boolean isReadOnly() {
        return true;
    }

    @Override
    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    @Override
    public String getCatalog() {
        return catalog;
    }

    @Override
    public void setTransactionIsolation(int level) {}

    @Override
    public int getTransactionIsolation() {
        return Connection.TRANSACTION_NONE;
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        return null;
    }

    @Override
    public void clearWarnings() throws SQLException {

    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        if (resultSetType != ResultSet.TYPE_FORWARD_ONLY)
            throw new SQLException("resultSetType %d not supported".formatted(resultSetType));
        if (resultSetConcurrency != ResultSet.CONCUR_READ_ONLY)
            throw new SQLException("resultSetConcurrency %d not supported".formatted(resultSetType));
        return createStatement();
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        if (resultSetType != ResultSet.TYPE_FORWARD_ONLY)
            throw new SQLException("resultSetType %d not supported".formatted(resultSetType));
        if (resultSetConcurrency != ResultSet.CONCUR_READ_ONLY)
            throw new SQLException("resultSetConcurrency %d not supported".formatted(resultSetType));
        return prepareStatement(sql);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) {
        throw new UnsupportedOperationException("prepareCall not supported yet");
    }

    @Override
    public Map<String, Class<?>> getTypeMap() {
        throw new UnsupportedOperationException("getTypeMap not supported");
    }

    @Override
    public void setTypeMap(Map<String, Class<?>> map) {
        throw new UnsupportedOperationException("setTypeMap not supported");
    }

    @Override
    public void setHoldability(int holdability) {}

    @Override
    public int getHoldability() {
        return ResultSet.HOLD_CURSORS_OVER_COMMIT;
    }

    @Override
    public Savepoint setSavepoint() throws SQLException {
        throw new SQLFeatureNotSupportedException("setSavepoint not supported");
    }

    @Override
    public Savepoint setSavepoint(String name) throws SQLException {
        throw new SQLFeatureNotSupportedException("setSavepoint(string) not supported");
    }

    @Override
    public void rollback(Savepoint savepoint) {}

    @Override
    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        throw new SQLFeatureNotSupportedException("releaseSavepoint not supported");
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        if (resultSetHoldability != ResultSet.HOLD_CURSORS_OVER_COMMIT)
            throw new SQLException("resultSetType %d not supported".formatted(resultSetType));
        return createStatement(resultSetType, resultSetConcurrency);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        throw new SQLFeatureNotSupportedException("prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) not supported");
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) {
        throw new UnsupportedOperationException("prepareCall not supported");
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
        throw new SQLFeatureNotSupportedException("prepareStatement(String sql, int autoGeneratedKeys) not supported");
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
        throw new SQLFeatureNotSupportedException("prepareStatement(String sql, int[] columnIndexes) not supported");
    }

    @Override
    public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
        throw new SQLFeatureNotSupportedException("prepareStatement(String sql, String[] columnNames) not supported");
    }

    @Override
    public Clob createClob() {
        throw new UnsupportedOperationException("createClob not supported");
    }

    @Override
    public Blob createBlob() {
        throw new UnsupportedOperationException("createBlob not supported");
    }

    @Override
    public NClob createNClob() {
        throw new UnsupportedOperationException("createNClob not supported");
    }

    @Override
    public SQLXML createSQLXML() {
        throw new UnsupportedOperationException("createSQLXML not supported");
    }

    @Override
    public boolean isValid(int timeout) {
        return true;
    }

    @Override
    public void setClientInfo(String name, String value) throws SQLClientInfoException {

    }

    @Override
    public void setClientInfo(Properties properties) throws SQLClientInfoException {

    }

    @Override
    public String getClientInfo(String name) throws SQLException {
        throw new UnsupportedOperationException("getClientInfo not supported");
    }

    @Override
    public Properties getClientInfo() throws SQLException {
        throw new UnsupportedOperationException("getClientInfo not supported");
    }

    @Override
    public Array createArrayOf(String typeName, Object[] elements) {
        throw new UnsupportedOperationException("createArrayOf not supported");
    }

    @Override
    public Struct createStruct(String typeName, Object[] attributes) {
        throw new UnsupportedOperationException("createStruct not supported");
    }

    @Override
    public void setSchema(String schema) {}

    @Override
    public String getSchema() {
        return null;
    }

    @Override
    public void abort(Executor executor) throws SQLException {

    }

    @Override
    public void setNetworkTimeout(Executor executor, int milliseconds) {}

    @Override
    public int getNetworkTimeout() {
        return 0;
    }

    @Override
    public <T> T unwrap(Class<T> iface) {
        throw new UnsupportedOperationException("unwrap not supported");
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) {
        return false;
    }

    public String getURL() {
        return url;
    }

    public String getUserName() {
        return "";
    }

    public boolean haveMinimumServerVersion(ServerVersion ver) {
        return version.getVersionNum() >= ver.getVersionNum();
    }

    public boolean getHideUnprivilegedObjects() {
        return false;
    }

    public TypeInfo getTypeInfo() {
        if (typeInfo == null) {
            typeInfo = new TypeInfo(this, Integer.MAX_VALUE);
        }
        return typeInfo;
    }
}
