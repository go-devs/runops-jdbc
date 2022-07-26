package ninja.ebanx.runops;

import ninja.ebanx.runops.api.ApiCall;
import ninja.ebanx.runops.api.RunopsApiClient;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

class RunopsPreparedStatementTest extends Mockito {

    @Test
    void executeQuery() throws IOException, InterruptedException, SQLException {
        // Arrange
        HttpClient httpClient = mock(HttpClient.class);
        var call1 = ApiCall.createApiCall(200, "{\"name\":\"target\",\"type\":\"postgres\"}");
        var call2 = ApiCall.createApiCall(201, "{\"id\": 123, \"task_logs\": \"\",\"status\":\"success\"}");
        var call3 = ApiCall.createApiCall(201, "{\"id\": 124, \"task_logs\": \"id\\tname\\n2020\\tAldeia\\n\",\"status\":\"success\"}");
        when(httpClient.send(any(HttpRequest.class), ArgumentMatchers.<HttpResponse.BodyHandler<String>>any()))
                .thenAnswer(call1)
                .thenAnswer(call2)
                .thenAnswer(call3);
        var roClient = new RunopsApiClient(httpClient);
        // Act
        String query = "select * from some_table where id = ?";
        try (var st = new RunopsPreparedStatement(query, roClient, TargetConnection.of(roClient.getTarget("target"), null), Logger.getAnonymousLogger())) {
            st.setInt(1, 1010);
            st.execute();
            st.setInt(1, 2020);
            st.executeQuery();
        }
        // Assert
        assertEquals("select * from some_table where id = 1010", call2.getRequestBodyAsJson().get("script"));
        assertEquals("select * from some_table where id = 2020", call3.getRequestBodyAsJson().get("script"));
    }

    @Test
    void executeUpdate_ShouldFail() throws SQLException, IOException, InterruptedException {
        // Arrange
        HttpClient httpClient = mock(HttpClient.class);
        var call1 = ApiCall.createApiCall(200, "{\"name\":\"target\",\"type\":\"postgres\"}");
        when(httpClient.send(any(HttpRequest.class), ArgumentMatchers.<HttpResponse.BodyHandler<String>>any()))
                .thenAnswer(call1);
        var roClient = new RunopsApiClient(httpClient);
        // Act
        String sql = "update some_table set column = 123 where id = 1";
        try (var st = new RunopsPreparedStatement(sql, roClient, TargetConnection.of(roClient.getTarget("target"), null), Logger.getAnonymousLogger())) {
            assertThrows(
                    SQLFeatureNotSupportedException.class,
                    st::executeUpdate,
                    "executeUpdate() not allowed"
            );
        }
    }

    @Test
    void inheritedExecute_ShouldFail() throws SQLException, IOException, InterruptedException {
        // Arrange
        HttpClient httpClient = mock(HttpClient.class);
        var call1 = ApiCall.createApiCall(200, "{\"name\":\"target\",\"type\":\"postgres\"}");
        when(httpClient.send(any(HttpRequest.class), ArgumentMatchers.<HttpResponse.BodyHandler<String>>any()))
                .thenAnswer(call1);
        var roClient = new RunopsApiClient(httpClient);
        // Act
        try (var st = new RunopsPreparedStatement("", roClient, TargetConnection.of(roClient.getTarget("target"), null), Logger.getAnonymousLogger())) {
            String sql = "select * from merchant.merchants";
            assertThrows(
                    SQLFeatureNotSupportedException.class,
                    () -> st.execute(sql),
                    "execute(sql) can't be executed"
            );
            assertThrows(
                    SQLFeatureNotSupportedException.class,
                    () -> st.executeQuery(sql),
                    "executeQuery(sql) can't be executed"
            );
        }
        // Assert
    }
}