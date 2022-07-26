package ninja.ebanx.runops;

import ninja.ebanx.runops.api.RunopsApiClient;
import ninja.ebanx.runops.mysql.MySqlTarget;
import ninja.ebanx.runops.postgres.PgTarget;
import org.json.JSONObject;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;

public interface TargetConnection {
    JSONObject getTarget();

    default String getName() {
        return getTarget().getString("name");
    }

    default String getType() {
        return getTarget().getString("type");
    }

    DatabaseMetaData getMetaData();

    QueryExecutor getQueryExecutor(RunopsApiClient client);

    static TargetConnection of(JSONObject target, RunopsConnection connection) throws SQLException {
        return switch (target.getString("type")) {
            case "postgres" -> new PgTarget(target, connection);
            case "mysql" -> new MySqlTarget(target, connection);
            default -> throw new SQLException("target type not supported");
        };
    }
}
