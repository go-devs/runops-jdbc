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
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

class RunopsResultSetTest extends Mockito {

    @Test
    void getString() throws IOException, InterruptedException, SQLException {
        // Arrange
        HttpClient httpClient = mock(HttpClient.class);
        var call1 = ApiCall.createApiCall(200, "{\"name\":\"xpto\"}");
        var call2 = ApiCall.createApiCall(201, """
                {
                    "description":null,
                    "redact":"none",
                    "task_logs":"id\\tname\\n8b4779eb-0fdd-4c32-aab0-c657bea108c6\\tAldeia\\nb3f1f293-8bea-4090-bdca-3fee86020197\\tAthletico Paranaense\\n(2 rows)\\n",
                    "type":"postgres",
                    "created":"2022\\/06\\/12 02:28",
                    "status":"success",
                    "id":118828,
                    "reviews":[],
                    "elapsed_time_ms":161,
                    "target":"read-akkad-production"
                }
                """);
        when(httpClient.send(any(HttpRequest.class), ArgumentMatchers.<HttpResponse.BodyHandler<String>>any()))
                .thenAnswer(call1)
                .thenAnswer(call2);
        var roClient = new RunopsApiClient(httpClient);
        // Act
        try (var stmt = new RunopsStatement(roClient, "xpto", Logger.getAnonymousLogger())) {
            var rst = stmt.executeQuery("select * from information_schema.columns");
            while (rst.next()) {
                // Assert
                assertEquals(rst.getString(1), rst.getString("id"));
                assertEquals(rst.getString(2), rst.getString("name"));
            }
        }
    }
}