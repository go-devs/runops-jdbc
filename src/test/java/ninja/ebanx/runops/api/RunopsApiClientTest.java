package ninja.ebanx.runops.api;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.junit.jupiter.api.Assertions.*;

class RunopsApiClientTest extends Mockito {

    @BeforeAll
    static void initAll() {
        System.setProperty("RUNOPS_JWT", "xpto");
    }

    @Test
    void listTargets() throws IOException, InterruptedException {
        // Arrange
        HttpClient httpClient = mock(HttpClient.class);
        ApiCall apiCall = arrangeApiCall(200, "[]");
        when(httpClient.send(any(HttpRequest.class), ArgumentMatchers.<HttpResponse.BodyHandler<String>>any())).thenAnswer(apiCall);
        // Act
        var cli = new RunopsApiClient(httpClient);
        var targets = cli.listTargets();
        // Assert
        assertRequest("GET", "/v1/targets", apiCall.request);
        assertEquals(0, targets.length());
    }

    @Test
    void getTarget() throws IOException, InterruptedException {
        // Arrange
        HttpClient httpClient = mock(HttpClient.class);
        ApiCall apiCall = arrangeApiCall(200, "{}");
        when(httpClient.send(any(HttpRequest.class), ArgumentMatchers.<HttpResponse.BodyHandler<String>>any())).thenAnswer(apiCall);
        // Act
        var cli = new RunopsApiClient(httpClient);
        var target = cli.getTarget("read-database");
        // Assert
        assertRequest("GET", "/v1/targets/read-database", apiCall.request);
        assertEquals("{}", target.toString());
    }

    @Test
    void listTasks() throws IOException, InterruptedException {
        // Arrange
        HttpClient httpClient = mock(HttpClient.class);
        ApiCall apiCall = arrangeApiCall(200, "[]");
        when(httpClient.send(any(HttpRequest.class), ArgumentMatchers.<HttpResponse.BodyHandler<String>>any())).thenAnswer(apiCall);
        // Act
        var cli = new RunopsApiClient(httpClient);
        var tasks = cli.listTasks();
        // Assert
        assertRequest("GET", "/v1/tasks", apiCall.request);
        assertEquals(0, tasks.length());
    }

    @Test
    void createTask() throws IOException, InterruptedException {
        // Arrange
        HttpClient httpClient = mock(HttpClient.class);
        ApiCall apiCall = arrangeApiCall(201, "{}");
        when(httpClient.send(any(HttpRequest.class), ArgumentMatchers.<HttpResponse.BodyHandler<String>>any())).thenAnswer(apiCall);
        // Act
        var cli = new RunopsApiClient(httpClient);
        var task = cli.createTask("target", "do something");
        // Assert
        assertRequest("POST", "/v1/tasks", apiCall.request);
        assertEquals("{}", task.toString());
    }

    @Test
    void getTask() throws IOException, InterruptedException {
        // Arrange
        HttpClient httpClient = mock(HttpClient.class);
        ApiCall apiCall = arrangeApiCall(200, "{}");
        when(httpClient.send(any(HttpRequest.class), ArgumentMatchers.<HttpResponse.BodyHandler<String>>any())).thenAnswer(apiCall);
        // Act
        var cli = new RunopsApiClient(httpClient);
        var task = cli.getTask(1234);
        // Assert
        assertRequest("GET", "/v1/tasks/1234", apiCall.request);
        assertEquals("{}", task.toString());
    }

    @Test
    void getTaskLogs() throws IOException, InterruptedException {
        // Arrange
        HttpClient httpClient = mock(HttpClient.class);
        ApiCall apiCall = arrangeApiCall(200, "{}");
        when(httpClient.send(any(HttpRequest.class), ArgumentMatchers.<HttpResponse.BodyHandler<String>>any())).thenAnswer(apiCall);
        // Act
        var cli = new RunopsApiClient(httpClient);
        var task = cli.getTaskLogs(789);
        // Assert
        assertRequest("GET", "/v1/tasks/789/logs", apiCall.request);
        assertEquals("{}", task.toString());
    }

    @Test
    void killTask() throws IOException, InterruptedException {
        // Arrange
        HttpClient httpClient = mock(HttpClient.class);
        ApiCall apiCall = arrangeApiCall(202, "");
        when(httpClient.send(any(HttpRequest.class), ArgumentMatchers.<HttpResponse.BodyHandler<String>>any())).thenAnswer(apiCall);
        // Act
        var cli = new RunopsApiClient(httpClient);
        cli.killTask(567);
        // Assert
        assertRequest("POST", "/v1/tasks/567/kill", apiCall.request);
    }

    private ApiCall arrangeApiCall(int statusCode, String body) {
        @SuppressWarnings("unchecked")
        HttpResponse<String> stringHttpResponse = (HttpResponse<String>) mock(HttpResponse.class);

        when(stringHttpResponse.statusCode()).thenReturn(statusCode);
        when(stringHttpResponse.body()).thenReturn(body);
        return new ApiCall(stringHttpResponse);
    }

    private static void assertRequest(String method, String path, HttpRequest request) {
        assertEquals(method, request.method());
        assertEquals(path, request.uri().getPath());
        assertEquals("xpto", request.headers().firstValue("Authorization").orElse(""));
        assertEquals("application/json", request.headers().firstValue("Content-Type").orElse(""));
    }

    private static class ApiCall implements Answer<HttpResponse<String>> {

        private final HttpResponse<String> response;
        private HttpRequest request;

        public ApiCall(HttpResponse<String> rsp) {
            response = rsp;
        }

        @Override
        public HttpResponse<String> answer(InvocationOnMock invocationOnMock) {
            request = invocationOnMock.getArgument(0);
            return response;
        }
    }
}