package ninja.ebanx.runops.api;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
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
    void listTargets() throws IOException {
        // Arrange
        HttpClient httpClient = mock(HttpClient.class);
        ApiCall apiCall = arrangeApiCall(200, new StringEntity("[]"));
        when(httpClient.execute(any(HttpUriRequest.class))).thenAnswer(apiCall);
        // Act
        var cli = new RunopsApiClient(httpClient);
        var targets = cli.listTargets();
        // Assert
        assertEquals("GET", apiCall.request.getMethod());
        assertEquals("/v1/targets", apiCall.request.getURI().getPath());
        assertEquals("xpto", apiCall.request.getFirstHeader(HttpHeaders.AUTHORIZATION).getValue());
        assertEquals(ContentType.APPLICATION_JSON.getMimeType(),
                apiCall.request.getFirstHeader(HttpHeaders.CONTENT_TYPE).getValue());
        assertEquals(0, targets.length());
    }

    @Test
    void getTarget() throws IOException {
        // Arrange
        HttpClient httpClient = mock(HttpClient.class);
        ApiCall apiCall = arrangeApiCall(200, new StringEntity("{}"));
        when(httpClient.execute(any(HttpUriRequest.class))).thenAnswer(apiCall);
        // Act
        var cli = new RunopsApiClient(httpClient);
        var target = cli.getTarget("read-database");
        // Assert
        assertEquals("GET", apiCall.request.getMethod());
        assertEquals("/v1/targets/read-database", apiCall.request.getURI().getPath());
        assertEquals("xpto", apiCall.request.getFirstHeader(HttpHeaders.AUTHORIZATION).getValue());
        assertEquals(ContentType.APPLICATION_JSON.getMimeType(),
                apiCall.request.getFirstHeader(HttpHeaders.CONTENT_TYPE).getValue());
        assertEquals("{}", target.toString());
    }

    @Test
    void listTasks() throws IOException {
        // Arrange
        HttpClient httpClient = mock(HttpClient.class);
        ApiCall apiCall = arrangeApiCall(200, new StringEntity("[]"));
        when(httpClient.execute(any(HttpUriRequest.class))).thenAnswer(apiCall);
        // Act
        var cli = new RunopsApiClient(httpClient);
        var tasks = cli.listTasks();
        // Assert
        assertEquals("GET", apiCall.request.getMethod());
        assertEquals("/v1/tasks", apiCall.request.getURI().getPath());
        assertEquals("xpto", apiCall.request.getFirstHeader(HttpHeaders.AUTHORIZATION).getValue());
        assertEquals(ContentType.APPLICATION_JSON.getMimeType(),
                apiCall.request.getFirstHeader(HttpHeaders.CONTENT_TYPE).getValue());
        assertEquals(0, tasks.length());
    }

    @Test
    void createTask() throws IOException {
        // Arrange
        HttpClient httpClient = mock(HttpClient.class);
        ApiCall apiCall = arrangeApiCall(201, new StringEntity("{}"));
        when(httpClient.execute(any(HttpUriRequest.class))).thenAnswer(apiCall);
        // Act
        var cli = new RunopsApiClient(httpClient);
        var task = cli.createTask("target", "do something");
        // Assert
        assertEquals("POST", apiCall.request.getMethod());
        assertEquals("/v1/tasks", apiCall.request.getURI().getPath());
        assertEquals("xpto", apiCall.request.getFirstHeader(HttpHeaders.AUTHORIZATION).getValue());
        assertEquals("{\"script\":\"do something\",\"target\":\"target\"}",
                new String(((HttpEntityEnclosingRequestBase)apiCall.request).getEntity().getContent().readAllBytes()));
        assertEquals("{}", task.toString());
    }

    @Test
    void getTask() throws IOException {
        // Arrange
        HttpClient httpClient = mock(HttpClient.class);
        ApiCall apiCall = arrangeApiCall(200, new StringEntity("{}"));
        when(httpClient.execute(any(HttpUriRequest.class))).thenAnswer(apiCall);
        // Act
        var cli = new RunopsApiClient(httpClient);
        var task = cli.getTask(1234);
        // Assert
        assertEquals("GET", apiCall.request.getMethod());
        assertEquals("/v1/tasks/1234", apiCall.request.getURI().getPath());
        assertEquals("xpto", apiCall.request.getFirstHeader(HttpHeaders.AUTHORIZATION).getValue());
        assertEquals(ContentType.APPLICATION_JSON.getMimeType(),
                apiCall.request.getFirstHeader(HttpHeaders.CONTENT_TYPE).getValue());
        assertEquals("{}", task.toString());
    }

    @Test
    void getTaskLogs() throws IOException {
        // Arrange
        HttpClient httpClient = mock(HttpClient.class);
        ApiCall apiCall = arrangeApiCall(200, new StringEntity("{}"));
        when(httpClient.execute(any(HttpUriRequest.class))).thenAnswer(apiCall);
        // Act
        var cli = new RunopsApiClient(httpClient);
        var task = cli.getTaskLogs(789);
        // Assert
        assertEquals("GET", apiCall.request.getMethod());
        assertEquals("/v1/tasks/789/logs", apiCall.request.getURI().getPath());
        assertEquals("xpto", apiCall.request.getFirstHeader(HttpHeaders.AUTHORIZATION).getValue());
        assertEquals(ContentType.APPLICATION_JSON.getMimeType(),
                apiCall.request.getFirstHeader(HttpHeaders.CONTENT_TYPE).getValue());
        assertEquals("{}", task.toString());
    }

    private ApiCall arrangeApiCall(int statusCode, HttpEntity entity) {
        HttpResponse httpResponse = mock(HttpResponse.class);
        StatusLine statusLine = mock(StatusLine.class);

        when(statusLine.getStatusCode()).thenReturn(statusCode);
        when(httpResponse.getStatusLine()).thenReturn(statusLine);
        when(httpResponse.getEntity()).thenReturn(entity);
        return new ApiCall(httpResponse);
    }

    private static class ApiCall implements Answer<HttpResponse> {

        private final HttpResponse response;
        private HttpUriRequest request;

        public ApiCall(HttpResponse rsp) {
            response = rsp;
        }

        @Override
        public HttpResponse answer(InvocationOnMock invocationOnMock) {
            request = invocationOnMock.getArgument(0);
            return response;
        }
    }
}