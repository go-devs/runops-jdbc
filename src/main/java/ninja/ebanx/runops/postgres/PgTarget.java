package ninja.ebanx.runops.postgres;

import ninja.ebanx.runops.RunopsConnection;
import ninja.ebanx.runops.TargetConnection;
import org.json.JSONObject;

import java.sql.DatabaseMetaData;

public class PgTarget implements TargetConnection {
    private final JSONObject target;
    private final RunopsConnection connection;
    private TypeInfo typeInfo;
    private final ServerVersion version;

    public PgTarget(JSONObject target, RunopsConnection connection) {
        this.target = target;
        this.connection = connection;
        // TODO get current version
        version = ServerVersion.v10;
    }

    @Override
    public JSONObject getTarget() {
        return target;
    }

    @Override
    public DatabaseMetaData getMetaData() {
        return new PgDatabaseMetaData(this.connection);
    }

    public String getURL() {
        return "jdbc:runops://" + target.getString("name") + "/" + connection.getCatalog();
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
            typeInfo = new TypeInfo(this.connection, Integer.MAX_VALUE);
        }
        return typeInfo;
    }
}
