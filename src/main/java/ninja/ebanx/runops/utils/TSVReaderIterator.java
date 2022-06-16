package ninja.ebanx.runops.utils;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TSVReaderIterator implements Iterator<String[]>, Closeable {
    private final CSVReader reader;
    private final Iterator<String[]> iterator;
    private final String[] header;
    private final Pattern pattern;

    public String[] getHeader() {
        return header;
    }

    public TSVReaderIterator(Reader in) {
        var csvParser = new CSVParserBuilder().withSeparator('\t').build();
        reader = new CSVReaderBuilder(in)
                .withCSVParser(csvParser)
                .build();
        iterator = reader.iterator();
        header = iterator.next();
        pattern = Pattern.compile("^(\\(\\d+ rows\\))$");
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public String[] next() {
        var nxt = iterator.next();
        if (!isValid(nxt[0])) {
            nxt = iterator.next();
        }
        return nxt;
    }

    @Override
    public void close() throws IOException {
        reader.close();
    }

    public boolean isValid(String s) {
        if (s == null) {
            return false;
        }
        Matcher matcher = pattern.matcher(s);
        return !matcher.find();
    }
}
