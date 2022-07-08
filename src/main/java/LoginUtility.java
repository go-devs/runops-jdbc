import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import ninja.ebanx.runops.api.RunopsApiClient;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

public final class LoginUtility {
    public static void runopsLogin(String email) throws IOException {
        startHttpServer();
        RunopsApiClient rac = RunopsApiClient.create();
        String url = rac.loginUrl(email);
        openDefaultBrowser(url);
    }

    private static void startHttpServer() throws IOException {
        HttpServer httpServer = HttpServer.create(new InetSocketAddress(3000), 0);
        httpServer.createContext("/callback").setHandler(LoginUtility::handlerCallback);
        httpServer.start();
    }

    private static void handlerCallback(HttpExchange exchange) throws IOException {
        Map<String, String> query = splitQuery(exchange.getRequestURI().getQuery());
        String token = query.get("token");
        handleJwt(token);
        String response = "Runops JDBC Driver Utility: login succeeded. You can close this tab now.";
        exchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
        exchange.getHttpContext().getServer().stop(1);
    }

    private static Map<String, String> splitQuery(String query) {
        Map<String, String> queryPairs = new LinkedHashMap<>();
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            queryPairs.put(
                    URLDecoder.decode(pair.substring(0, idx), StandardCharsets.UTF_8),
                    URLDecoder.decode(pair.substring(idx + 1), StandardCharsets.UTF_8));
        }
        return queryPairs;
    }

    private static void handleJwt(String token) {
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String[] chunks = token.split("\\.");
        JSONObject payload = new JSONObject(new String(decoder.decode(chunks[1])));
        System.out.println("Paylaod received:" + payload);
        LocalDateTime exp = Instant.ofEpochSecond(payload.getLong("exp")).atZone(ZoneId.systemDefault()).toLocalDateTime();
        System.out.println("Expires in:" + exp);
        saveRunopsConfigFile(token);
    }

    private static void saveRunopsConfigFile(String token) {
        Path fileName = Path.of(System.getProperty("user.home"),"/.runops/config");
        try {
            Files.createDirectories(fileName.getParent());
            Files.writeString(fileName,"Bearer " + token);
        } catch (IOException e) {
            System.out.println("Error when try to save the token. Reason: " + e.getMessage());
        }
    }

    private static void openDefaultBrowser(String url) throws IOException {
        System.out.println("Trying to open your browser now. If it fails, use the link below to continue:");
        System.out.println(url);
        Runtime rt = Runtime.getRuntime();
        rt.exec(new String[]{"open", url});
    }
}
