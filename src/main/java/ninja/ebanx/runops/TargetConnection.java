package ninja.ebanx.runops;

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

    static TargetConnection of(JSONObject target, RunopsConnection connection) throws SQLException {
        return switch (target.getString("type")) {
            case "postgres" -> new PgTarget(target, connection);
            case "mysql" -> throw new SQLException("target type not implemented yet");
            default -> throw new SQLException("target type not supported");
        };
    }
}
