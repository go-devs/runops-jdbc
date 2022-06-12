package ninja.ebanx.runops.api;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

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
}
