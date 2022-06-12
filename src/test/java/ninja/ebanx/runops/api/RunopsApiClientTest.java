package ninja.ebanx.runops.api;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

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
        ApiCall apiCall = ApiCall.createApiCall(200, "[]");
        when(httpClient.send(any(HttpRequest.class), ArgumentMatchers.<HttpResponse.BodyHandler<String>>any())).thenAnswer(apiCall);
        // Act
        var cli = new RunopsApiClient(httpClient);
        var targets = cli.listTargets();
        // Assert
        assertRequest("GET", "/v1/targets", apiCall.getRequest());
        assertEquals(0, targets.length());
    }

    @Test
    void getTarget() throws IOException, InterruptedException {
        // Arrange
        HttpClient httpClient = mock(HttpClient.class);
        ApiCall apiCall = ApiCall.createApiCall(200, "{}");
        when(httpClient.send(any(HttpRequest.class), ArgumentMatchers.<HttpResponse.BodyHandler<String>>any())).thenAnswer(apiCall);
        // Act
        var cli = new RunopsApiClient(httpClient);
        var target = cli.getTarget("read-database");
        // Assert
        assertRequest("GET", "/v1/targets/read-database", apiCall.getRequest());
        assertEquals("{}", target.toString());
    }

    @Test
    void listTasks() throws IOException, InterruptedException {
        // Arrange
        HttpClient httpClient = mock(HttpClient.class);
        ApiCall apiCall = ApiCall.createApiCall(200, "[]");
        when(httpClient.send(any(HttpRequest.class), ArgumentMatchers.<HttpResponse.BodyHandler<String>>any())).thenAnswer(apiCall);
        // Act
        var cli = new RunopsApiClient(httpClient);
        var tasks = cli.listTasks();
        // Assert
        assertRequest("GET", "/v1/tasks", apiCall.getRequest());
        assertEquals(0, tasks.length());
    }

    @Test
    void createTask() throws IOException, InterruptedException {
        // Arrange
        HttpClient httpClient = mock(HttpClient.class);
        ApiCall apiCall = ApiCall.createApiCall(201, "{}");
        when(httpClient.send(any(HttpRequest.class), ArgumentMatchers.<HttpResponse.BodyHandler<String>>any())).thenAnswer(apiCall);
        // Act
        var cli = new RunopsApiClient(httpClient);
        var task = cli.createTask("target", "do something");
        // Assert
        assertRequest("POST", "/v1/tasks", apiCall.getRequest());
        assertEquals("{}", task.toString());
    }

    @Test
    void getTask() throws IOException, InterruptedException {
        // Arrange
        HttpClient httpClient = mock(HttpClient.class);
        ApiCall apiCall = ApiCall.createApiCall(200, "{}");
        when(httpClient.send(any(HttpRequest.class), ArgumentMatchers.<HttpResponse.BodyHandler<String>>any())).thenAnswer(apiCall);
        // Act
        var cli = new RunopsApiClient(httpClient);
        var task = cli.getTask(1234);
        // Assert
        assertRequest("GET", "/v1/tasks/1234", apiCall.getRequest());
        assertEquals("{}", task.toString());
    }

    @Test
    void getTaskLogs() throws IOException, InterruptedException {
        // Arrange
        HttpClient httpClient = mock(HttpClient.class);
        ApiCall apiCall = ApiCall.createApiCall(200, "{}");
        when(httpClient.send(any(HttpRequest.class), ArgumentMatchers.<HttpResponse.BodyHandler<String>>any())).thenAnswer(apiCall);
        // Act
        var cli = new RunopsApiClient(httpClient);
        var task = cli.getTaskLogs(789);
        // Assert
        assertRequest("GET", "/v1/tasks/789/logs", apiCall.getRequest());
        assertEquals("{}", task.toString());
    }

    @Test
    void killTask() throws IOException, InterruptedException {
        // Arrange
        HttpClient httpClient = mock(HttpClient.class);
        ApiCall apiCall = ApiCall.createApiCall(202, "");
        when(httpClient.send(any(HttpRequest.class), ArgumentMatchers.<HttpResponse.BodyHandler<String>>any())).thenAnswer(apiCall);
        // Act
        var cli = new RunopsApiClient(httpClient);
        cli.killTask(567);
        // Assert
        assertRequest("POST", "/v1/tasks/567/kill", apiCall.getRequest());
    }

    private static void assertRequest(String method, String path, HttpRequest request) {
        assertEquals(method, request.method());
        assertEquals(path, request.uri().getPath());
        assertEquals("xpto", request.headers().firstValue("Authorization").orElse(""));
        assertEquals("application/json", request.headers().firstValue("Content-Type").orElse(""));
    }
}