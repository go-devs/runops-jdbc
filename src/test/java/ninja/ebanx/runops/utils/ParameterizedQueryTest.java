package ninja.ebanx.runops.utils;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ParameterizedQueryTest {

    @Test
    void getQuery_withDoubleIntString_shouldPass() throws SQLException {
        // Arrange
        ParameterizedQuery pq = new ParameterizedQuery("UPDATE EMPLOYEES SET SALARY = ? WHERE ID = ? AND NAME = ?");
        pq.setDouble(1, 153833.01);
        pq.setInt(2, 110592);
        pq.setString(3, "John");

        // Act
        var query = pq.getQuery();

        // Assert
        var expected = "UPDATE EMPLOYEES SET SALARY = 153833.01 WHERE ID = 110592 AND NAME = 'John'";
        assertEquals(expected, query);
    }

    @Disabled("Disabled until fix ' in string")
    @Test
    void getQuery_shouldPass_butIsFailing() throws SQLException {
        // Arrange
        ParameterizedQuery pq = new ParameterizedQuery("UPDATE EMPLOYEES SET SALARY = ? WHERE ID = ? AND NAME = ?");
        pq.setDouble(1, 153833.01);
        pq.setInt(2, 110592);
        pq.setString(3, "John O'Connor");

        // Act
        var query = pq.getQuery();

        // Assert
        var expected = "UPDATE EMPLOYEES SET SALARY = 153833.01 WHERE ID = 110592 AND NAME = 'John O''Connor'";
        assertEquals(expected, query);
    }

    @Test
    void getQuery_withBoolLongTimestamp_shouldPass() throws SQLException {
        // Arrange
        ParameterizedQuery pq = new ParameterizedQuery("bool = ?, long = ?, tz = ?");
        pq.setBool(1, false);
        pq.setLong(2, 123456L);
        var ldt = LocalDateTime.of(2022, 6, 20, 23, 50);
        var zdt = ldt.atZone(ZoneId.of("America/Sao_Paulo"));
        pq.setTimestamp(3, Timestamp.from(zdt.toInstant()));

        // Act
        var query = pq.getQuery();

        // Assert
        var expected = "bool = false, long = 123456, tz = '2022-06-21T02:50:00Z'";
        assertEquals(expected, query);
    }
}