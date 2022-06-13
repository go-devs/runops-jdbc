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

import static org.junit.jupiter.api.Assertions.assertEquals;

class RunopsStatementTest extends Mockito {
    @Test
    void execute() throws SQLException, IOException, InterruptedException {
        // Arrange
        HttpClient httpClient = mock(HttpClient.class);
        var call1 = ApiCall.createApiCall(200, "{\"name\":\"target\"}");
        var call2 = ApiCall.createApiCall(201, "{\"id\": 123, \"task_logs\": \"\"}");
        when(httpClient.send(any(HttpRequest.class), ArgumentMatchers.<HttpResponse.BodyHandler<String>>any()))
                .thenAnswer(call1)
                .thenAnswer(call2);
        var roClient = new RunopsApiClient(httpClient);
        // Act
        try (var st = new RunopsStatement(roClient, "target", Logger.getAnonymousLogger())) {
            st.execute("select * from merchant.merchants");
        }
        // Assert
        var requestBody = call2.getRequestBodyAsJson();
        assertEquals("target", requestBody.getString("target"));
        assertEquals("select * from merchant.merchants", requestBody.getString("script"));
    }
}