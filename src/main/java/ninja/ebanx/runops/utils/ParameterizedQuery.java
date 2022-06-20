package ninja.ebanx.runops.utils;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParameterizedQuery {
    private static final Pattern pattern = Pattern.compile("\\B(\\?+)\\B|\\B(\\?)$");
    private final int count;
    private final Map<Integer, String> parameters;
    private final Matcher matcher;

    public ParameterizedQuery(String query) {
        matcher = pattern.matcher(query);
        count = (int) matcher.results().count();
        this.parameters = new HashMap<>(count);
    }

    public String getQuery() {
        matcher.reset();
        AtomicInteger i = new AtomicInteger();
        return matcher.replaceAll((m) -> parameters.getOrDefault(i.incrementAndGet(), ""));
    }

    public void setString(int index, String value) throws SQLException {
        assertIndex(index);
        parameters.put(index, "'" + value + "'");
    }

    public void setInt(int index, int value) throws SQLException {
        assertIndex(index);
        parameters.put(index, Integer.toString(value));
    }

    public void setDouble(int index, double value) throws SQLException {
        assertIndex(index);
        parameters.put(index, Double.toString(value));

    }

    public void setBool(int index, boolean value) throws SQLException {
        assertIndex(index);
        parameters.put(index, Boolean.toString(value));
    }

    public void setLong(int index, long value) throws SQLException {
        assertIndex(index);
        parameters.put(index, Long.toString(value));
    }

    private void assertIndex(int index) throws SQLException {
        if (!(0 < index && index <= count))
            throw new SQLException("parameter index is out of range");
    }

    public void setTimestamp(int index, Timestamp value) throws SQLException {
        assertIndex(index);
        parameters.put(index, "'" + value.toInstant() + "'");
    }
}
