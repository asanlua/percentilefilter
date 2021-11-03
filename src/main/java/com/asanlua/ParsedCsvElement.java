package com.asanlua;

public class ParsedCsvElement {

    private final float value;
    private final String csv;

    public ParsedCsvElement(String csv, int position) throws Exception {
        this.csv = csv;
        this.value = Utils.getCsvValue(csv, position);
    }

    public float getValue() {
        return value;
    }

    public String getCsv() {
        return csv;
    }
}
