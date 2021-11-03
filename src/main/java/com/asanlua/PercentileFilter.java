package com.asanlua;

import static com.asanlua.Utils.*;

/**
 * Hello world!
 *
 */
public class PercentileFilter
{
    public static void main( String[] args )
    {
        long startTime = System.currentTimeMillis();
        try {
            PercentileFilterConfiguration pcConfig = PercentileFilterConfiguration.getPercentileParserConfiguration(args);
            System.out.println(pcConfig);
            MemoryPercentileFilter.writeFilteredResult(pcConfig);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        printElapsedTime("Total process time:", startTime);
    }






}
