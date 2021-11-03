package com.asanlua;

import com.google.common.base.Splitter;

import java.io.BufferedWriter;
import java.io.IOException;

public class Utils {

    private static final String CSV_SEPARATOR = ",";

    public static void addLineToFile(String value, BufferedWriter bw) throws IOException {
        bw.write(value);
        bw.newLine();
    }

    public static float getCsvValue(String csv, int position) throws Exception {
        Iterable<String> csvElements = Splitter.on(CSV_SEPARATOR)
                .split(csv);
        int i = 0;
        for (String s : csvElements) {
            if (i == position) {
                return Float.valueOf(s);
            }
            i++;
        }
        throw new Exception("Could not parse CSV value");
    }

    public static void printElapsedTime(String message, long startTime) {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(message);
        strBuilder.append(" ");
        strBuilder.append(System.currentTimeMillis() - startTime);
        strBuilder.append(" ms");
        System.out.println(strBuilder.toString());
    }

    public static boolean arePercentileLimitsValid(float lowerLimit, float upperLimit) {
        if (lowerLimit < 0 || lowerLimit > 1.0 || upperLimit < 0 || upperLimit > 1.0 || lowerLimit > upperLimit) {
            return false;
        }
        return true;
    }
}
