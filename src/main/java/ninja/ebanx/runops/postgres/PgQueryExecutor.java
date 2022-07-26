package ninja.ebanx.runops.postgres;

import ninja.ebanx.runops.TargetConnection;
import ninja.ebanx.runops.api.RunopsApiClient;
import ninja.ebanx.runops.utils.DefaultQueryExecutor;
import ninja.ebanx.runops.utils.HandleResult;

import java.io.IOException;
import java.sql.SQLException;

public class PgQueryExecutor extends DefaultQueryExecutor {
    public PgQueryExecutor(RunopsApiClient client, TargetConnection target) {
        super(client, target);
    }

    @Override
    protected void executeTask(String sql) throws IOException, InterruptedException, SQLException {
        boolean shouldFixExplainResult = sql.toUpperCase().startsWith("EXPLAIN (FORMAT JSON");
        super.executeTask(sql);
        if (shouldFixExplainResult) {
            data = HandleResult.tidyQueryPlan(data);
        }
    }
}
