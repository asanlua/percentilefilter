package com.asanlua;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static com.asanlua.Utils.addLineToFile;
import static com.asanlua.Utils.printElapsedTime;


public class MemoryPercentileFilter {


    public static void writeFilteredResult(PercentileFilterConfiguration pcConfig) throws Exception {
        List<ParsedCsvElement> orderedNumbers = getOrderedList(pcConfig);
        writeFilteredList(pcConfig, orderedNumbers);

    }

    private static void writeFilteredList(PercentileFilterConfiguration pcConfig, List<ParsedCsvElement> orderedNumbers) throws Exception {
        try {
            long startTime = System.currentTimeMillis();
            File fout = new File(pcConfig.getOutputFileName());
            FileOutputStream fos = new FileOutputStream(fout);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

            int lowerLimit = (int) (pcConfig.getLowerPercentileLimit() * orderedNumbers.size());
            int upperLimit = (int) (pcConfig.getUpperPercentileLimit() * orderedNumbers.size());

            int i = 0;
            Iterator<ParsedCsvElement> iterator = orderedNumbers.iterator();
            while (iterator.hasNext()) {
                if (i < lowerLimit) {
                    i++;
                    iterator.next();
                    continue;
                }
                if (i > upperLimit) {
                    break;
                }
                addLineToFile(iterator.next().getCsv(), bw);
                i++;
            }
            bw.close();
            printElapsedTime("Write file time:", startTime);
        } catch (FileNotFoundException e) {
            throw new Exception("File not found: " + e.getLocalizedMessage());
        } catch (IOException e) {
            throw new Exception("IO Exception: " + e.getLocalizedMessage());
        }
    }


    private static List<ParsedCsvElement> getOrderedList(PercentileFilterConfiguration pcConfig) throws Exception {
        List<ParsedCsvElement> csvList = new ArrayList<>();
        long startTime = System.currentTimeMillis();
        try {
            FileInputStream fstream = new FileInputStream(pcConfig.getInputFileName());
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

            long invalidLines = -1;

            String inputLine;
            while ((inputLine = br.readLine()) != null) {
                try {
                    csvList.add(new ParsedCsvElement(inputLine, pcConfig.getCsvPosition()));
                } catch (Exception e) {
                    invalidLines++;
                }
            }
            fstream.close();

            if (invalidLines > 0) {
                System.out.println("Found a total of " + invalidLines + " invalid lines.");
            }

            printElapsedTime("Read file time:", startTime);
            startTime = System.currentTimeMillis();

            Collections.sort(csvList, new CsvComparator());

            printElapsedTime("Order time:", startTime);
            System.out.println("Processed a total of " + csvList.size() + " valid lines.");

        } catch (IOException e) {
            throw new Exception("IO Exception: " + e.getLocalizedMessage());
        }
        return csvList;

    }
}
