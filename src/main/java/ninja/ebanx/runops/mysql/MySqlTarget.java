package ninja.ebanx.runops.mysql;

import ninja.ebanx.runops.QueryExecutor;
import ninja.ebanx.runops.RunopsConnection;
import ninja.ebanx.runops.TargetConnection;
import ninja.ebanx.runops.api.RunopsApiClient;
import ninja.ebanx.runops.utils.DefaultQueryExecutor;
import org.json.JSONObject;

import java.sql.DatabaseMetaData;

public class MySqlTarget implements TargetConnection {
    private final JSONObject target;
    private final RunopsConnection connection;

    public MySqlTarget(JSONObject target, RunopsConnection connection) {
        this.target = target;
        this.connection = connection;
    }

    @Override
    public JSONObject getTarget() {
        return target;
    }

    @Override
    public DatabaseMetaData getMetaData() {
        return null;
    }

    @Override
    public QueryExecutor getQueryExecutor(RunopsApiClient client) {
        return new DefaultQueryExecutor(client, this);
    }
}
