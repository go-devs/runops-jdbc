package ninja.ebanx.runops;

import ninja.ebanx.runops.api.RunopsApiClient;
import org.json.JSONObject;

import java.io.*;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RunopsStatement implements Statement {

    private final JSONObject target;
    private final Logger logger;

    private final RunopsApiClient clientApi;
    private Reader plainResult;
    private int taskId;
    private int maxRows;

    public RunopsStatement(String target, Logger logger) throws SQLException {
        this.logger = logger;

        clientApi = RunopsApiClient.create();
        try {
            this.target = clientApi.getTarget(target);
            logger.log(Level.INFO, "ready for statements at " + this.target.get("name"));
        } catch (IOException e) {
            throw new SQLException("invalid target " + target, e);
        }
    }

    @Override
    public ResultSet executeQuery(String sql) throws SQLException {
        execute(sql);
        return getResultSet();
    }

    @Override
    public int executeUpdate(String sql) throws SQLException {
        throw new SQLFeatureNotSupportedException("INSERT, UPDATE, DELETE or DDL statements are not supported");
    }

    @Override
    public void close() throws SQLException {
        try {
            plainResult.close();
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public int getMaxFieldSize() {
        return 0;
    }

    @Override
    public void setMaxFieldSize(int max) {
        throw new UnsupportedOperationException("setMaxFieldSize not supported");
    }

    @Override
    public int getMaxRows() {
        return maxRows;
    }

    @Override
    public void setMaxRows(int max) throws SQLException {
        if (max < 0)
            throw new SQLException("max rows must be >= 0");
        maxRows = max;
    }

    @Override
    public void setEscapeProcessing(boolean enable) throws SQLException {
        throw new UnsupportedOperationException("setEscapeProcessing not supported");
    }

    @Override
    public int getQueryTimeout() throws SQLException {
        return 0;
    }

    @Override
    public void setQueryTimeout(int seconds) throws SQLException {

    }

    @Override
    public void cancel() throws SQLException {
        if (taskId == 0) {
            return;
        }
        try {
            clientApi.killTask(taskId);
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        throw new UnsupportedOperationException("getWarnings not supported");
    }

    @Override
    public void clearWarnings() throws SQLException {

    }

    @Override
    public void setCursorName(String name) {
        //noop
    }

    @Override
    public boolean execute(String sql) throws SQLException {
        try {
            executeTask(sql);
            return true;
        } catch (IOException | InterruptedException e) {
            throw new SQLException(e);
        }
    }

    private void executeTask(String sql) throws IOException, InterruptedException {
        var t = clientApi.createTask(target.getString("name"), sql);
        taskId = t.getInt("id");
        logger.info("task " + taskId + " created");

        if (t.getString("task_logs").startsWith("https://")) {
            var taskLog = clientApi.getTaskLogs(taskId);
            plainResult = clientApi.getTaskLogsData(taskLog.getString("logs_url"));
        } else {
            plainResult = new StringReader(t.getString("task_logs"));
        }
    }

    @Override
    public ResultSet getResultSet() throws SQLException {
        return new RunopsResultSet(plainResult);
    }

    @Override
    public int getUpdateCount() throws SQLException {
        return 0;
    }

    @Override
    public boolean getMoreResults() throws SQLException {
        return false;
    }

    @Override
    public void setFetchDirection(int direction) throws SQLException {

    }

    @Override
    public int getFetchDirection() throws SQLException {
        return 0;
    }

    @Override
    public void setFetchSize(int rows) throws SQLException {

    }

    @Override
    public int getFetchSize() throws SQLException {
        return 0;
    }

    @Override
    public int getResultSetConcurrency() throws SQLException {
        return 0;
    }

    @Override
    public int getResultSetType() throws SQLException {
        return 0;
    }

    @Override
    public void addBatch(String sql) throws SQLException {

    }

    @Override
    public void clearBatch() throws SQLException {

    }

    @Override
    public int[] executeBatch() throws SQLException {
        return new int[0];
    }

    @Override
    public Connection getConnection() throws SQLException {
        throw new UnsupportedOperationException("getConnection not supported");
    }

    @Override
    public boolean getMoreResults(int current) throws SQLException {
        return false;
    }

    @Override
    public ResultSet getGeneratedKeys() throws SQLException {
        throw new UnsupportedOperationException("getGeneratedKeys not supported");
    }

    @Override
    public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
        return 0;
    }

    @Override
    public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
        return 0;
    }

    @Override
    public int executeUpdate(String sql, String[] columnNames) throws SQLException {
        return 0;
    }

    @Override
    public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
        return false;
    }

    @Override
    public boolean execute(String sql, int[] columnIndexes) throws SQLException {
        return false;
    }

    @Override
    public boolean execute(String sql, String[] columnNames) throws SQLException {
        return false;
    }

    @Override
    public int getResultSetHoldability() throws SQLException {
        return 0;
    }

    @Override
    public boolean isClosed() throws SQLException {
        return false;
    }

    @Override
    public void setPoolable(boolean poolable) throws SQLException {

    }

    @Override
    public boolean isPoolable() throws SQLException {
        return false;
    }

    @Override
    public void closeOnCompletion() throws SQLException {

    }

    @Override
    public boolean isCloseOnCompletion() throws SQLException {
        return false;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        throw new UnsupportedOperationException("unwrap not supported");
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }
}
