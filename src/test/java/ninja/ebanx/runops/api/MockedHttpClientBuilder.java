package ninja.ebanx.runops.api;

import org.mockito.ArgumentMatchers;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MockedHttpClientBuilder {
    private List<ApiCall> calls;

    public MockedHttpClientBuilder() {
        calls = new ArrayList<>();
    }

    public MockedHttpClientBuilder addResponse(int statusCode, String body) {
        calls.add(ApiCall.createApiCall(statusCode, body));
        return this;
    }

    public MockedHttpClientBuilder withTarget(String name, String type) {
        this.addResponse(200, "{\"name\":\"" + name + "\", \"type\":\"" + type + "\"}");
        return this;
    }

    public HttpClient build() throws IOException, InterruptedException {
        HttpClient httpClient = mock(HttpClient.class);
        var answers = when(httpClient.send(any(HttpRequest.class), ArgumentMatchers.<HttpResponse.BodyHandler<String>>any()));
        for(var call: calls) {
            answers.thenAnswer(call);
        }
        RunopsApiClient.DEFAULT_CLIENT = () -> httpClient;
        return httpClient;
    }
}
