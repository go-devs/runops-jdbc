package ninja.ebanx.runops.utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.regex.Pattern;

public class HandleResult {
    private static final Pattern pattern = Pattern.compile("^QUERY PLAN\\n((.|\\n)*)\\(1 row\\)$", Pattern.DOTALL);

    public static Reader tidyQueryPlan(Reader reader) throws IOException {
        var raw = readerToString(reader);
        var matcher = pattern.matcher(raw);
        var flatPlan = matcher.replaceAll(matchResult -> (new JSONArray(matchResult.group(1))).toString() );
        return new StringReader("QUERY PLAN\n" + flatPlan.replaceAll("\"", "\"\""));
    }

    public static String readerToString(Reader reader) throws IOException {
        int valChar;
        StringBuilder stringBuilder = new StringBuilder();
        while ((valChar = reader.read()) != -1) {
            stringBuilder.append((char) valChar);
        }
        return stringBuilder.toString();
    }
}
