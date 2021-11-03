package com.asanlua;

import org.apache.commons.cli.*;

public class PercentileFilterConfiguration {

    private String inputFileName;
    private String outputFileName;
    private float lowerPercentileLimit;
    private float upperPercentileLimit;
    private int csvPosition;

    public static PercentileFilterConfiguration getPercentileParserConfiguration(String[] args) throws Exception {

        String inputFileName;
        String outputFileName;
        float lowerPercentileLimit;
        float upperPercentileLimit;
        int csvPosition = 0;

        CommandLineParser parser  = new DefaultParser();
        Options options = getOptions();
        CommandLine       cmdLine = parser.parse(options, args);

        if (!cmdLine.hasOption("i")) {
            new HelpFormatter().printHelp(PercentileFilterConfiguration.class.getCanonicalName(), options );
            throw new Exception("Error: Input file name is mandatory.");
        }
        inputFileName = cmdLine.getOptionValue("i");

        if (!cmdLine.hasOption("o")) {
            new HelpFormatter().printHelp(PercentileFilterConfiguration.class.getCanonicalName(), options );
            throw new Exception("Error: Output file name is mandatory.");
        }
        outputFileName = cmdLine.getOptionValue("o");

        if (cmdLine.hasOption("p")) {
            csvPosition = Integer.valueOf(cmdLine.getOptionValue("p"));
        }

        if (cmdLine.hasOption("l")) {
            lowerPercentileLimit = Float.valueOf(cmdLine.getOptionValue("l"));
        } else {
            lowerPercentileLimit = 0;
        }

        if (cmdLine.hasOption("u")) {
            upperPercentileLimit = Float.valueOf(cmdLine.getOptionValue("u"));
        } else {
            upperPercentileLimit = 1;
        }

        if (!Utils.arePercentileLimitsValid(lowerPercentileLimit, upperPercentileLimit)) {
            new HelpFormatter().printHelp(PercentileFilterConfiguration.class.getCanonicalName(), options );
            throw new Exception("Invalid percentile limits");
        }

        if (cmdLine.hasOption("h")){
            new HelpFormatter().printHelp(PercentileFilterConfiguration.class.getCanonicalName(), options );
            throw new Exception("Help command pressed");
        }

        return new PercentileFilterConfiguration(inputFileName, outputFileName, lowerPercentileLimit, upperPercentileLimit, csvPosition);
    }

    private static Options getOptions() {
        Options options = new Options();
        options.addOption("i", "inputFileName", true, "Input file name (.csv). Mandatory. Example: input.csv");
        options.addOption("o", "outputFileName", true, "Output file name (.csv). Mandatory. Example: output.csv.");
        options.addOption("l", "lower", true, "Lower percentile limit (decimal), between 0 and 1.0. Optional. Default value: 0. Example: 0.1");
        options.addOption("u", "upper", true, "Upper percentile limit (decimal), between 0 and 1.0. Optional. Default value: 1.0. Example: 0.9");
        options.addOption("p", "position", true, "Position of the value to be parsed in the csv file. Default value: 0. Example: 4");
        options.addOption("h", "help", false,"Print help. Example of usage: java -jar percentilefilter.jar -i yellow_tripdata_2021-01.csv -o output.csv -l 0.9 -p 4");
        return options;
    }

    private PercentileFilterConfiguration(String inputFileName, String outputFileName, float lowerPercentileLimit, float upperPercentileLimit, int csvPosition) {
        this.inputFileName = inputFileName;
        this.outputFileName = outputFileName;
        this.lowerPercentileLimit = lowerPercentileLimit;
        this.upperPercentileLimit = upperPercentileLimit;
        this.csvPosition = csvPosition;
    }

    public String getInputFileName() {
        return inputFileName;
    }

    public String getOutputFileName() {
        return outputFileName;
    }

    public float getLowerPercentileLimit() {
        return lowerPercentileLimit;
    }

    public float getUpperPercentileLimit() {
        return upperPercentileLimit;
    }

    public int getCsvPosition() {
        return csvPosition;
    }

    @Override
    public String toString() {
        return "PercentileFilterConfiguration{" +
                "inputFileName='" + inputFileName + '\'' +
                ", outputFileName='" + outputFileName + '\'' +
                ", lowerPercentileLimit=" + lowerPercentileLimit +
                ", upperPercentileLimit=" + upperPercentileLimit +
                ", csvPosition=" + csvPosition +
                '}';
    }
}
