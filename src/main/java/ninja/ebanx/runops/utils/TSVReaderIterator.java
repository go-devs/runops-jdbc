package ninja.ebanx.runops.utils;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;

public class TSVReaderIterator implements Iterator<String[]>, Closeable {
    private final CSVReader reader;
    private final Iterator<String[]> iterator;

    public TSVReaderIterator(Reader in) {
        var csvParser = new CSVParserBuilder().withSeparator('\t').build();
        reader = new CSVReaderBuilder(in)
                .withCSVParser(csvParser)
                .withSkipLines(1)
                .withLineValidator(new LastLineValidator())
                .build();
        iterator = reader.iterator();
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public String[] next() {
        return iterator.next();
    }

    @Override
    public void close() throws IOException {
        reader.close();
    }
}
