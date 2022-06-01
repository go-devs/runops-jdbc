package ninja.ebanx.runops.api;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

public class RunopsApiClient {
    private static final String BASE_URI = "https://api.runops.io/v1";
    private final HttpClient client;

    public RunopsApiClient(HttpClient httpClient) {
        client = httpClient;
    }

    public static RunopsApiClient create() {
        var cli = HttpClientBuilder.create().build();
        return new RunopsApiClient(cli);
    }

    public JSONArray listTargets() throws IOException {
        var req = (HttpUriRequest) createRequest(HttpGet.METHOD_NAME, "/targets");
        var rsp = client.execute(req);
        return new JSONArray(EntityUtils.toString(rsp.getEntity()));
    }

    public JSONObject getTarget(String name) throws IOException {
        var req = (HttpUriRequest) createRequest(HttpGet.METHOD_NAME, "/targets/" + name);
        var rsp = execute(req);
        return new JSONObject(EntityUtils.toString(rsp.getEntity()));
    }

    public JSONArray listTasks() throws IOException {
        var rsp = execute(createRequest(HttpGet.METHOD_NAME, "/tasks"));
        return new JSONArray(EntityUtils.toString(rsp.getEntity()));
    }

    public JSONObject createTask(String target, String script) throws IOException {
        return createTask(target, script, null, null);
    }

    public JSONObject createTask(String target, String script, String message, String taskType) throws IOException {
        var task = new JSONObject();
        task.put("target", target);
        task.put("script", script);
        task.put("message", message);
        task.put("type", taskType);
        var entity = new StringEntity(
                task.toString(),
                ContentType.APPLICATION_JSON
        );
        var rsp = execute(createRequest(HttpPost.METHOD_NAME, "/tasks", entity));
        return new JSONObject(EntityUtils.toString(rsp.getEntity()));
    }

    public JSONObject getTask(int id) throws IOException {
        var rsp = execute(createRequest("GET", "/tasks/" + id));
        return new JSONObject(EntityUtils.toString(rsp.getEntity()));
    }

    public JSONObject getTaskLogs(int id) throws IOException {
        var rsp = execute(createRequest("GET", "/tasks/" + id + "/logs"));
        return new JSONObject(EntityUtils.toString(rsp.getEntity()));
    }

    public InputStreamReader getTaskLogsData(String uri) throws IOException {
        var req = new HttpGet(uri);
        var rsp = client.execute(req);
        return new InputStreamReader(rsp.getEntity().getContent());
    }

    private HttpResponse execute(HttpRequest request) {
        try {
            return client.execute((HttpUriRequest) request);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private HttpRequest createRequest(String method, String resource) {
        return createRequest(method, resource, null);
    }

    private HttpRequest createRequest(String method, String resource, HttpEntity entity) {
        return RequestBuilder.create(method)
                .addHeader(HttpHeaders.AUTHORIZATION, AuthorizationResolver.resolve())
                .addHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType())
                .addHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType())
                .setUri(BASE_URI + resource)
                .setEntity(entity)
                .build();
    }
}
