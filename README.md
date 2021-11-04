
# percentilefilter
Parse a CSV file and get the entries between a lower and upper percentile limit. 

# Introduction
The goal of this project is to be able to return all the trips over 0.9 percentile in distance travelled for any of the CSV files found in the [NYC “Yellow Taxi” Trips Data](https://www1.nyc.gov/site/tlc/about/tlc-trip-record-data.page).

As an extension of the original problem, the solution is able to parse any CSV file and get the entries between a lower and upper percentile limit, just passing the position of the field in the CSV. As an important remark: it is assumed that the values to be parsed are floating-point numbers. Of course, it would be easier and faster with integers.

# Quick guide
You can find an already compiled jar in the files dir, so you can use it directly. To solve the original question, you can try:

    java -jar percentilefilter.jar -i yellow_tripdata_2021-01.csv -o output.csv -l 0.9 -p 4

The options of the jar are the following:


- -h,--help                  
 Print help. Example of usage: `java -jar percentilefilter.jar -i yellow_tripdata_2021-01.csv -o output.csv -l 0.9 -p 4`
 - -i,--inputFileName arg    
 Input file name (.csv). Mandatory. Example: input.csv
 - -l,--lower arg            
 Lower percentile limit (decimal), between 0 and 1.0. Optional. Default value: 0. Example: 0.1
 - -o,--outputFileName arg   
 Output file name (.csv). Mandatory. Example: output.csv.
 - -p,--position arg         
 Position of the value to be parsed in the csv file. Default value: 0. Example: 4
 - -u,--upper arg           
 Upper percentile limit (decimal), between 0 and 1.0. Optional. Default value: 1.0. Example: 0.9

Notice that the default percentile limits (0 and 1.0) produce an ordered version of the input file. 

In the folder files, you can find input and output file examples, with easier files, to verify the validity of the solution. 
- Parse a list of numbers to get an ordered output file:
	- Without percentile limits, the result is simply an ordered file of the input.
	- `java -jar percentilefilter.jar -i ../files/input-numbers.csv -o ../files/output-numbers-ordered.csv`
- Parse a list of numbers to get the 0.9 upper percentile entries:
	- `java -jar percentilefilter.jar -i ../files/input-numbers.csv -o ../files/output-numbers-percentile09.csv -l 0.9`
- We can try with a 20 lines example of NYC Yellow Tripdata:
	- Ordered list:
		- `java -jar percentilefilter.jar -i ../files/trip_data_20_lines.csv -o ../files/trip_data_20_lines-ordered.csv  -p 4`
	- Upper 0.9 percentile:
		- `java -jar percentilefilter.jar -i ../files/trip_data_20_lines.csv -o ../files/trip_data_20_lines-upper09percentile.csv  -p 4 -l 0.9`

# Build instructions
As for any Java-based solution, you need JRE and Maven to build the project: 
`mvn clean package`

# Design 
The solution is made upon two assumptions:
- As the files are around 100 MB, we can process them in memory directly. For files in the order of TB, this approach could be wrong.
- Performance is more important than memory consumption. 

There are three main steps to accomplish the task:
- Read the file and insert the lines into a data structure, a list in this case.
- Order the data structure (a list).
- Print the result, selecting the values between the percentile limits. 

The process always prints the time invested in each step, and we can see that the third one is almost negligible compared with the other two. In the final version, for a 120 MB file, the first step takes around 1000 ms, 400 ms the second, and less than 100 ms the third one. 

Thus, the major improvements should be done reading and creating the list and ordering the list. 

- For reading and creating the list, the process of reading the file itself is negligible. But we need to parse the CSV to be able to get the results. This could be done typically with a Java split. But the usage of Guava Splitter gave better results.
- Also, instead of creating a list of CSV lines, so that we would need to parse each line again in order to compare and order the list, a simple object has been created, extracting the field to be compared and saving the complete CSV to be printed later. Thus, it is only needed to parse de CSV once. 
- Once we have the list of objects containing the field to be compared and the CSV to be printed, we sort the list with the Java Collections sort methods. Of course, we need an implementation of the Comparator. In this case, we are comparing floats. It would be much faster if we could compare integer values. This could be a way of improvement, maybe extending the object with a new field for the decimal part, and making only integer comparisons. 


# Next steps
- Build a solution based on disk to be able to handle files on the order of terabytes.
- Improve the ordering of the list using threads. To do so, we could try to handle several lists, to add values in each one based on the value itself. For example, values below 2 go to the first list, and above 2, to the second one. Then we could use two threads to order both lists, and then merge them. This could be also made configurable, providing a list of intermediate values to fill several lists, order them in parallel, and merge. 
- As mentioned before, Guava Splitter is better than the String.split() method, but maybe a regex, with a pattern already compiled, would perform better. 
