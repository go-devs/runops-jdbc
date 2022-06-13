package ninja.ebanx.runops.api;

import org.json.JSONObject;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.Flow;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ApiCall implements Answer<HttpResponse<String>> {
    private final HttpResponse<String> response;

    private HttpRequest request;

    public HttpRequest getRequest() {
        return request;
    }

    public static ApiCall createApiCall(int statusCode, String body) {
        @SuppressWarnings("unchecked")
        HttpResponse<String> stringHttpResponse = (HttpResponse<String>) mock(HttpResponse.class);

        when(stringHttpResponse.statusCode()).thenReturn(statusCode);
        when(stringHttpResponse.body()).thenReturn(body);
        return new ApiCall(stringHttpResponse);
    }

    public ApiCall(HttpResponse<String> rsp) {
        response = rsp;
    }

    @Override
    public HttpResponse<String> answer(InvocationOnMock invocationOnMock) {
        request = invocationOnMock.getArgument(0);
        return response;
    }

    public String getRequestBody() {
        var sub = HttpResponse.BodySubscribers.ofString(StandardCharsets.UTF_8);
        request.bodyPublisher().orElse(HttpRequest.BodyPublishers.noBody()).subscribe(new StringSubscriber(sub));
        return sub.getBody().toCompletableFuture().join();
    }

    public JSONObject getRequestBodyAsJson() {
        return new JSONObject(getRequestBody());
    }

    record StringSubscriber(HttpResponse.BodySubscriber<String> wrapped) implements Flow.Subscriber<ByteBuffer> {
        @Override
        public void onSubscribe(Flow.Subscription subscription) {
            wrapped.onSubscribe(subscription);
        }

        @Override
        public void onNext(ByteBuffer item) {
            wrapped.onNext(List.of(item));
        }

        @Override
        public void onError(Throwable throwable) {
            wrapped.onError(throwable);
        }

        @Override
        public void onComplete() {
            wrapped.onComplete();
        }
    }
}
