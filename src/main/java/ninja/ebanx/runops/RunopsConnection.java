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

    public String getTarget() {
        return target;
    }

    public String getConfig() {
        return config;
    }
    public RunopsConnection(String url, Properties info, Logger logger) {
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
    public CallableStatement prepareCall(String sql) throws SQLException {
        throw new UnsupportedOperationException("prepareCall not supported");
    }

    @Override
    public String nativeSQL(String sql) {
        return sql;
    }

    @Override
    public void setAutoCommit(boolean autoCommit) throws SQLException {

    }

    @Override
    public boolean getAutoCommit() throws SQLException {
        return false;
    }

    @Override
    public void commit() throws SQLException {

    }

    @Override
    public void rollback() throws SQLException {

    }

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
    public void setReadOnly(boolean readOnly) throws SQLException {

    }

    @Override
    public boolean isReadOnly() throws SQLException {
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
    public void setTransactionIsolation(int level) throws SQLException {

    }

    @Override
    public int getTransactionIsolation() throws SQLException {
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
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        throw new UnsupportedOperationException("prepareCall not supported");
    }

    @Override
    public Map<String, Class<?>> getTypeMap() throws SQLException {
        throw new UnsupportedOperationException("getTypeMap not supported");
    }

    @Override
    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {

    }

    @Override
    public void setHoldability(int holdability) throws SQLException {

    }

    @Override
    public int getHoldability() {
        return ResultSet.HOLD_CURSORS_OVER_COMMIT;
    }

    @Override
    public Savepoint setSavepoint() throws SQLException {
        throw new UnsupportedOperationException("setSavepoint not supported");
    }

    @Override
    public Savepoint setSavepoint(String name) throws SQLException {
        throw new UnsupportedOperationException("setSavepoint not supported");
    }

    @Override
    public void rollback(Savepoint savepoint) throws SQLException {

    }

    @Override
    public void releaseSavepoint(Savepoint savepoint) throws SQLException {

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
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
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
    public Clob createClob() throws SQLException {
        throw new UnsupportedOperationException("createClob not supported");
    }

    @Override
    public Blob createBlob() throws SQLException {
        throw new UnsupportedOperationException("createBlob not supported");
    }

    @Override
    public NClob createNClob() throws SQLException {
        throw new UnsupportedOperationException("createNClob not supported");
    }

    @Override
    public SQLXML createSQLXML() throws SQLException {
        throw new UnsupportedOperationException("createSQLXML not supported");
    }

    @Override
    public boolean isValid(int timeout) throws SQLException {
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
    public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
        throw new UnsupportedOperationException("createArrayOf not supported");
    }

    @Override
    public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
        throw new UnsupportedOperationException("createStruct not supported");
    }

    @Override
    public void setSchema(String schema) throws SQLException {

    }

    @Override
    public String getSchema() throws SQLException {
        throw new UnsupportedOperationException("getSchema not supported");
    }

    @Override
    public void abort(Executor executor) throws SQLException {

    }

    @Override
    public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {

    }

    @Override
    public int getNetworkTimeout() throws SQLException {
        return 0;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        throw new UnsupportedOperationException("unwrap not supported");
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

    public String getURL() {
        return "";
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
