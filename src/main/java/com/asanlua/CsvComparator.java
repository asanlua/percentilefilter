package com.asanlua;

import java.util.Comparator;

public class CsvComparator implements Comparator<ParsedCsvElement> {

    @Override
    public int compare(ParsedCsvElement element1, ParsedCsvElement element2) {
        if (element1.getValue() >= element2.getValue()) {
            return 1;
        }
        return -1;
    }
}
