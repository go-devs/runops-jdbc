package ninja.ebanx.runops;

import java.io.Reader;
import java.sql.SQLException;

public interface QueryExecutor {
    Reader execute(String sql) throws SQLException;
    int getTaskId();
    String getTaskStatus();
}
