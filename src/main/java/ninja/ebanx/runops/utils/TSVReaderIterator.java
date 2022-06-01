package ninja.ebanx.runops.utils;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.List;

public class TSVReaderIterator implements Iterator<List<String>>, Closeable {
    private final BufferedReader reader;
    private List<String> cachedLine;

    public TSVReaderIterator(Reader in) {
        reader = new BufferedReader(in);
        cachedLine = readLine();
    }

    @Override
    public boolean hasNext() {
        return !cachedLine.isEmpty();
    }

    @Override
    public List<String> next() {
        var currentLine = cachedLine;
        cachedLine = readLine();
        return currentLine;
    }

    private List<String> readLine() {
        try {
            var line = reader.readLine();
            if (line == null) {
                return List.of();
            }
            return List.of(line.split("\t"));
        } catch (IOException e) {
            return List.of();
        }
    }

    @Override
    public void close() throws IOException {
        reader.close();
    }
}
