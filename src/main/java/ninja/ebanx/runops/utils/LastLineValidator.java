package ninja.ebanx.runops.utils;

import com.opencsv.exceptions.CsvValidationException;
import com.opencsv.validators.LineValidator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LastLineValidator implements LineValidator {
    private final Pattern pattern;

    public LastLineValidator() {
        pattern = Pattern.compile("^(\\(\\d+ rows\\))$");
    }

    @Override
    public boolean isValid(String s) {
        if (s == null) {
            return false;
        }
        Matcher matcher = pattern.matcher(s);
        return !matcher.find();
    }

    @Override
    public void validate(String s) throws CsvValidationException {
        if (!isValid(s)) {
            throw new CsvValidationException("row count line");
        }
    }
}
