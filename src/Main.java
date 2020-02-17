import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Press Y if you want to enter input array and N to use array from csv file.");

        String inputString = scanner.next();
        if (inputString.equals("Y") || inputString.equals("y")) {
            //The below option reads user input for jagged array and provide results
            readUserInput(scanner);
        } else if (inputString.equals("N") || inputString.equals("n")){
            // Reading Input from a hard coded file please change file Name. Input files can be chosen from sample available
            readInputFile("/Users/shuklag/IdeaProjects/ImpactAnalytics/src/valid1.csv", scanner);
        } else {
            System.out.println("Wrong input provided");
            return;
        }
    }

    public static void readInputFile(String csvFile, Scanner scanner) {
        try {
            System.out.println("Enter number of rows present in File.");
            // The above statement in written for convenience, Could be read from file only.
            int numOfRows = scanner.nextInt();
            int[][] input = new int[numOfRows][];

            File file = new File(csvFile);
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line = "";
            int tempRow = 0;
            int maxColumn = -1;

            while ((line = br.readLine()) != null) {
                String[] strArray = line.split(",");
                int[] intArray = new int[strArray.length];
                for (int j = 0; j < strArray.length; j++) {
                    intArray[j] = Integer.parseInt(strArray[j]);
                }
                input[tempRow] = intArray;
                if (strArray.length > maxColumn) {
                    maxColumn = strArray.length;
                }
                tempRow++;
            }
            getGoldTreasure(input, maxColumn);
            br.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }


    static void readUserInput(Scanner scanner) {
        System.out.println("Enter number of rows for the Jagged array.");
        int numOfRows = scanner.nextInt();
        int[][] input = new int[numOfRows][];

        int maxColumn = -1;
        System.out.println("Enter values comma separated for each row for the Jagged array.");
        String temp;
        for (int i = 0; i < numOfRows; i++) {
            temp = scanner.next();
            String[] strArray = temp.split(",");
            int[] intArray = new int[strArray.length];
            for (int j = 0; j < strArray.length; j++) {
                intArray[j] = Integer.parseInt(strArray[j]);
            }
            input[i] = intArray;
            if (strArray.length > maxColumn) {
                maxColumn = strArray.length;
            }
        }
        getGoldTreasure(input, maxColumn);
    }

    // This below method reads the input array and using dynamic programming create another array.
    static void getGoldTreasure(int[][] input, int maxCol) {
        // Result set to store the coordinates the array which will participate in the result.
        // Key if the hash map is starting index of any series. This is actually coordinate of 1.
        HashMap<Coordinate, ArrayList<String>> resultSet = new HashMap<Coordinate, ArrayList<String>>();

        // Initializing the zeroth coordinates because all the calculations will happen leaving that coordinate.
        if (input[0][0] == 1) {
            resultSet.put(new Coordinate(0, 0), new ArrayList<String>());
        }

        // Temporary array to store the series. This can be optimized to store within the same array.
        int[][] arr = new int[input.length][maxCol];

        for (int i = 0; i < input.length; i++) {
            for (int j = 0; j < input[i].length; j++) {
                // If any input is invalid i.e a single coordinate that can participate in multiple series OR any other input from 0  or 1
                if (!checkCorrectInput(input, i, j)) {
                    System.out.println("Invalid Input");
                    return;
                } else {
                    if ((i == 0 && j == 0) || input[i][j] == 0) {
                        // In this logic I am excluding the zeroth coordinate.
                        // If value at that coordinate is non zero then only that coordinate will pe part of the series.
                        // So excluding everything else
                        arr[i][j] = input[i][j];
                    } else {
                        // Logic is to check if the number if non zero i.e 1
                        // and the element on top if it i.e (row-1, col) is more than zero then the current element will be part of series.
                        // and the element on before if it i.e (row, col-1) is more than zero then the current element will be part of series.
                        if (checkIfRecordExists(i - 1, j, input) && input[i - 1][j] > 0) {
                            arr[i][j] = arr[i - 1][j] + 1;
                        } else if (checkIfRecordExists(i, j - 1, input) && input[i][j - 1] > 0) {
                            arr[i][j] = arr[i][j - 1] + 1;
                        } else {
                            //Otherwise if element before is 0 then in that case temp array coordinate will be same as input
                            resultSet.put(new Coordinate(i, j), new ArrayList<String>());
                            arr[i][j] = input[i][j];
                        }

                    }
                }
            }
        }
        //below commented code can be uncommented to check the intermediate input.
        //printArray(arr);
        printResultSet(arr, resultSet);
    }

    // The below method will check if the element is present as the array is jagged.
    static boolean checkIfRecordExists(int row, int column, int[][] input) {
        boolean bRet = false;
        if (row > -1 && row < input.length) {
            int[] tempRow = input[row];
            if (column > -1 && column < tempRow.length)
                bRet = true;
        }
        return bRet;
    }

    static void printArray(int[][] arr) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++)
                System.out.print(arr[i][j] + " ");
            System.out.println();
        }
    }

    // This method will check the temp array to print all the series.
    static void printResultSet(int[][] arr, HashMap<Coordinate, ArrayList<String>> resultSet) {
        // Below hash map is to store length of all the series.
        // This is to sort the hash map based on value as the output series needs to
        // be printed the increasing order of length in any series
        HashMap<Coordinate, Integer> countResult = new HashMap<Coordinate, Integer>();
        for (Map.Entry<Coordinate, ArrayList<String>> entry : resultSet.entrySet()) {
            int currentRow = ((Coordinate) entry.getKey()).getRow();
            int currentColumn = ((Coordinate) entry.getKey()).getColumn();

            int count = 0;
            //While loop is to create series.
            while (arr[currentRow][currentColumn] > 0) {
                entry.getValue().add("{" + currentRow + "," + currentColumn + "}");
                count++;
                if (checkIfRecordExists(currentRow + 1, currentColumn, arr) && arr[currentRow + 1][currentColumn] > 0) {
                    currentRow++;
                } else if (checkIfRecordExists(currentRow, currentColumn + 1, arr) && arr[currentRow][currentColumn + 1] > 0) {
                    currentColumn++;
                } else
                    break;
            }
            countResult.put(entry.getKey(), count);
        }

        HashMap<Coordinate, Integer> newHashMap = sortByValue(countResult);

        StringBuilder output1 = new StringBuilder();

        StringBuilder output2 = new StringBuilder();

        output1.append(newHashMap.values());

        for (Map.Entry<Coordinate, Integer> item : newHashMap.entrySet()) {
            ArrayList<String> current = resultSet.get(item.getKey());
            output2.append(current);
            output2.append(",");
        }

        System.out.println(output1);
        System.out.println(output2);
    }

    // Method to sort hashmap with values.
    // This code can further be improvised to check if two values are same then consider
    // the one with lower value of row first.
    public static HashMap<Coordinate, Integer> sortByValue(HashMap<Coordinate, Integer> hash) {
        List<Map.Entry<Coordinate, Integer>> list =
                new LinkedList<Map.Entry<Coordinate, Integer>>(hash.entrySet());

        Collections.sort(list, new Comparator<Map.Entry<Coordinate, Integer>>() {
            public int compare(Map.Entry<Coordinate, Integer> o1,
                               Map.Entry<Coordinate, Integer> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        HashMap<Coordinate, Integer> temp = new LinkedHashMap<Coordinate, Integer>();
        for (Map.Entry<Coordinate, Integer> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        //Returns a new hasmap sorted on Values.
        return temp;
    }

    // Method to check correct input.
    static boolean checkCorrectInput(int[][] input, int row, int column) {
        // If record is not in 0 or 1 then the input is invalid.
        if (checkIfRecordExists(row, column, input)
                && input[row][column] != 0
                && input[row][column] != 1)
            return false;
        if (input[row][column] == 0) // If value @ given coordinate is 0 then input is valid.
            return true;
        else if (input[row][column] == 1) { // if value @ given coordinate is 1 then need to check if the next elements
            // on the right and bottom should not be 1
            boolean isColumnNextPresent = checkIfRecordExists(row, column + 1, input);

            boolean isRowNextPresent = checkIfRecordExists(row + 1, column, input);

            // Case to check if both right and bottom records are present and both the 1 , that that is a case of invalid input.
            if (isColumnNextPresent && input[row][column + 1] == 1 && isRowNextPresent && input[row + 1][column] == 1)
                return false;
                // if bottom element is present and right is not , then that is a valid input
            else if (!isColumnNextPresent && isRowNextPresent)
                return true;
                // if right element is present and bottom is not , then that is a valid input
            else if (!isRowNextPresent && isColumnNextPresent)
                return true;
            else // (!isColumnNextPresent && !isRowNextPresent) // If both bottom and right elements are not present.
                return true;
        } else
            return true;
    }
}
