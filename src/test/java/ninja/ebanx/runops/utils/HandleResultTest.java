package ninja.ebanx.runops.utils;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;

class HandleResultTest {

    @Test
    void tidyQueryPlan() throws IOException {
        // Arrange
        var sr = new StringReader("""
                QUERY PLAN
                [
                  {
                    "Plan": {
                      "Node Type": "Seq Scan",
                      "Parallel Aware": false,
                      "Relation Name": "merchants",
                      "Alias": "merchants",
                      "Startup Cost": 0.00,
                      "Total Cost": 103.00,
                      "Plan Rows": 4200,
                      "Plan Width": 83,
                      "Actual Startup Time": 0.006,
                      "Actual Total Time": 0.331,
                      "Actual Rows": 4200,
                      "Actual Loops": 1
                    },
                    "Planning Time": 0.230,
                    "Triggers": [
                    ],
                    "Execution Time": 0.508
                  }
                ]
                (1 row)
                """);

        // Act
        var result = HandleResult.tidyQueryPlan(sr);
        // Assert
        var expected = new JSONArray("[{\"Plan\":{\"Node Type\":\"Seq Scan\",\"Parallel Aware\":false,\"Relation Name\":\"merchants\",\"Alias\":\"merchants\",\"Startup Cost\":0,\"Total Cost\":103,\"Plan Rows\":4200,\"Plan Width\":83,\"Actual Startup Time\":0.006,\"Actual Total Time\":0.331,\"Actual Rows\":4200,\"Actual Loops\":1},\"Planning Time\":0.23,\"Triggers\":[],\"Execution Time\":0.508}]");
//        var j = new JSONObject("");
//        j.equals()
        assertEquals("QUERY PLAN\n" + expected.toString().replaceAll("\"", "\"\"") + "\n", HandleResult.readerToString(result));
    }
}