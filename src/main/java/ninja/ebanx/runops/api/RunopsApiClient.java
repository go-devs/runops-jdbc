package ninja.ebanx.runops.api;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.function.Supplier;

public class RunopsApiClient {
    public static Supplier<HttpClient> DEFAULT_CLIENT = HttpClient::newHttpClient;
    private static final String BASE_URI = "https://api.runops.io/v1";
    private final HttpClient client;

    public RunopsApiClient(HttpClient httpClient) {
        client = httpClient;
    }

    public static RunopsApiClient create() {
        HttpClient cli = DEFAULT_CLIENT.get();
        return new RunopsApiClient(cli);
    }

    public String loginUrl(String email) throws IOException {
        var req = createRequest("GET", "/login?email=" + email);
        var rsp = execute(req);
        if (rsp.statusCode() != 200) {
            throw new IOException(String.format("wrong status code: %d", rsp.statusCode()));
        }
        var ret = new JSONObject(rsp.body());
        return ret.getString("login_url");
    }

    public JSONArray listTargets() throws IOException, InterruptedException {
        var req = createRequest("GET", "/targets");
        var rsp = client.send(req, BodyHandlers.ofString());
        return new JSONArray(rsp.body());
    }

    public JSONObject getTarget(String name) throws IOException {
        var req = createRequest("GET", "/targets/" + name);
        var rsp = execute(req);
        if (rsp.statusCode() != 200) {
            throw new IOException(String.format("wrong status code: %d", rsp.statusCode()));
        }
        return new JSONObject(rsp.body());
    }

    public JSONArray listTasks() {
        var rsp = execute(createRequest("GET", "/tasks"));
        return new JSONArray(rsp.body());
    }

    public JSONObject createTask(String target, String script) {
        return createTask(target, script, null, null);
    }

    public JSONObject createTask(String target, String script, String message, String taskType) {
        var task = new JSONObject();
        task.put("target", target);
        task.put("script", script);
        task.put("message", message);
        task.put("type", taskType);
        var entity = BodyPublishers.ofString(task.toString());
        var rsp = execute(createRequest("POST", "/tasks", entity));
        return new JSONObject(rsp.body());
    }

    public JSONObject getTask(int id) {
        var rsp = execute(createRequest("GET", "/tasks/" + id));
        return new JSONObject(rsp.body());
    }

    public JSONObject getTaskLogs(int id) {
        var rsp = execute(createRequest("GET", "/tasks/" + id + "/logs"));
        return new JSONObject(rsp.body());
    }

    public InputStreamReader getTaskLogsData(String uri) throws IOException, InterruptedException {
        var req = HttpRequest.newBuilder(URI.create(uri)).build();
        var rsp = client.send(req, BodyHandlers.ofInputStream());
        return new InputStreamReader(rsp.body());
    }

    public void killTask(int id) throws IOException {
        var rsp = execute(createRequest("POST","/tasks/" + id + "/kill"));
        if (rsp.statusCode() != 202) {
            throw new IOException(String.format("wrong status code: %d", rsp.statusCode()));
        }
    }

    private HttpResponse<String> execute(HttpRequest request) {
        try {
            return client.send(request, BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private HttpRequest createRequest(String method, String resource) {
        return createRequest(method, resource, BodyPublishers.noBody());
    }

    private HttpRequest createRequest(String method, String resource, BodyPublisher body) {
        return HttpRequest.newBuilder()
                .method(method, body)
                .header("Authorization", AuthorizationResolver.resolve())
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .uri(URI.create(BASE_URI + resource))
                .build();
    }
}
