package ninja.ebanx.runops.utils;

import ninja.ebanx.runops.QueryExecutor;
import ninja.ebanx.runops.TargetConnection;
import ninja.ebanx.runops.api.RunopsApiClient;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.sql.SQLException;
import java.util.Objects;

public class DefaultQueryExecutor implements QueryExecutor {
    private final RunopsApiClient clientApi;
    private final TargetConnection target;
    protected Reader data;
    private JSONObject task;

    public DefaultQueryExecutor(RunopsApiClient client, TargetConnection target) {
        this.clientApi = client;
        this.target = target;
    }

    @Override
    public Reader execute(String sql) throws SQLException {
        try {
            executeTask(sql);
            return data;
        } catch (IOException | InterruptedException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public int getTaskId() {
        return task.getInt("id");
    }

    @Override
    public String getTaskStatus() {
        return task.getString("status");
    }

    protected void executeTask(String sql) throws IOException, InterruptedException, SQLException {
        task = clientApi.createTask(target.getName(), sql);
        if (!Objects.equals(getTaskStatus(), "success")) {
            throw new SQLException(task.getString("task_logs"));
        }
        if (task.getString("task_logs").startsWith("https://")) {
            var taskLog = clientApi.getTaskLogs(this.getTaskId());
            data = clientApi.getTaskLogsData(taskLog.getString("logs_url"));
        } else {
            data = new StringReader(task.getString("task_logs"));
        }
    }
}
