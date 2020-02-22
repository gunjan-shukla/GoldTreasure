const path = require('path');
const prompt = require('prompt-sync')();
const HashMap = require('hashmap');
const fs = require('fs');
const csv = require('csv'); // FYI : Specific version of this node module is used.
const obj = csv();

const goldTreasure = {
    readUserInput: function readUserInput(scanner) {
        scanner.question('Enter number of rows for the Jagged array.  ', (enteredVal) => {
            scanner.close();
            var numOfRows = Number.parseInt(enteredVal);
            if (isNaN(numOfRows) || numOfRows < 1) {
                console.log("INVALID Input");
                process.exit(1);
            }
            var input = new Array(numOfRows);
            var maxColumn = -1;
            console.log("Enter values comma separated for each row for the Jagged array.");
            var temp;
            for (let i = 0; i < numOfRows; i++) {
                var temp = prompt(`Values for Row ${i} : `);
                var strArray = temp.split(",");
                var intArray = new Array(strArray.length);
                for (let j = 0; j < strArray.length; j++) {
                    intArray[j] = Number.parseInt(strArray[j]);
                }
                input[i] = intArray;
                if (strArray.length > maxColumn) {
                    maxColumn = strArray.length;
                }
            }
            //printArray method can be uncommented to check the intermediate input.
            //goldTreasure.printArray(input);
            goldTreasure.getGoldTreasure(input, maxColumn);
        });
    },
    readInputFile: function readInputFile(filePath, scanner) {
        var input;
        var maxColumn = -1;
        obj.from.path(filePath).to.array(function(data) {
            input = new Array(data.length);
            for (var index = 0; index < data.length; index++) {
                input[index] = new Array();

                if (maxColumn < data[index].length)
                    maxColumn = data[index].length;
                for (let j = 0; j < data[index].length; j++) {
                    input[index].push(Number.parseInt(data[index][j]));
                }
                if (index == data.length - 1) {
                    //printArray method can be uncommented to check the intermediate input.
                    //goldTreasure.printArray(input);
                    goldTreasure.getGoldTreasure(input, maxColumn);
                }
            }
        });
    },
    printArray: function printArray(arr) {
        for (let i = 0; i < arr.length; i++) {
            var temp = '';
            for (let j = 0; j < arr[i].length; j++)
                temp += (arr[i][j] + " ");
            console.log(temp);
        }
    },
    // This below method reads the input array and using dynamic programming create another array.
    getGoldTreasure: function getGoldTreasure(input, maxColumn) {
        // Result set to store the coordinates the array which will participate in the result.
        // Key of the hash map is starting index of any series. Format is : "row*column"
        var resultSet = new HashMap();

        // Initializing the zeroth coordinates because all the calculations will happen leaving that coordinate.
        if (input[0][0] == 1) {
            resultSet.set(0 + "*" + 0, new Array());
        }

        // Temporary array to store the series. This can be optimized to store within the same array.
        var arr = new Array(input.length);

        for (let index = 0; index < arr.length; index++) {
            arr[index] = new Array();
            //Initializing the intemediate array with -1 which will be considered as invalid input later.
            for (let j = 0; j < maxColumn; j++) {
                arr[index].push(-1);
            }
        }

        for (let i = 0; i < input.length; i++) {
            for (let j = 0; j < input[i].length; j++) {
                // If any input is invalid i.e a single coordinate that can participate in 
                //multiple series OR any other input from 0  or 1
                if (!goldTreasure.checkCorrectInput(input, i, j)) {
                    console.log("INVALID Input");
                    process.exit(1);
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
                        if (goldTreasure.checkIfRecordExists(i - 1, j, input) && input[i - 1][j] > 0) {
                            arr[i][j] = arr[i - 1][j] + 1;
                        } else if (goldTreasure.checkIfRecordExists(i, j - 1, input) && input[i][j - 1] > 0) {
                            arr[i][j] = arr[i][j - 1] + 1;
                        } else {
                            //Otherwise if element before is 0 then in that case temp array coordinate will be same as input
                            resultSet.set(i + "*" + j, new Array());
                            arr[i][j] = input[i][j];
                        }
                    }
                }
            }
        }
        //printArray method can be uncommented to check the intermediate input.
        //goldTreasure.printArray(arr);
        goldTreasure.printResultSet(arr, resultSet);

    },
    // The below method will check if the element is present as the array is jagged or can have invalid numbers
    checkIfRecordExists: function checkIfRecordExists(row, column, input) {
        let bRet = true;
        //if Row and Column are negative.
        if (row < 0 || column < 0)
            bRet = false;
        //if Input is not defined.
        else if (!input)
            bRet = false;
        //if row is not present in the input
        else if (input && row >= input.length)
            bRet = false;
        // if Row is present but the column is not
        else if (input && input[row] && row < input.length && column >= input[row].length)
            bRet = false;
        // if row and columns are present but value is invalid which is -1
        else if (input && input[row] && row < input.length && column < input[row].length && input[row][column] && input[row][column] == -1)
            bRet = false;
        else
            bRet = true;
        return bRet;
    },
    // Method to check correct input.
    checkCorrectInput: function checkCorrectInput(input, row, column) {
        // If record is not in 0 or 1 then the input is invalid.
        if (goldTreasure.checkIfRecordExists(row, column, input) &&
            input[row][column] != 0 &&
            input[row][column] != 1)
            return false;
        if (input[row][column] == 0) // If value @ given coordinate is 0 then input is valid.
            return true;
        else if (input[row][column] == 1) { // if value @ given coordinate is 1 then need to check if the next elements
            // on the right and bottom should not be 1
            var isColumnNextPresent = goldTreasure.checkIfRecordExists(row, column + 1, input);

            var isRowNextPresent = goldTreasure.checkIfRecordExists(row + 1, column, input);

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
    },
    // This method will check the temp array to print all the series.
    printResultSet: function printResultSet(arr, resultSet) {
        // Below array is to store length of all the series.
        // This is to sort the array based on count
        var countResult = new Array();
        resultSet.forEach(function(value, key) {
            var tokens = key.split("*");
            var currentRow = Number.parseInt(tokens[0]);
            var currentColumn = Number.parseInt(tokens[1]);

            let count = 0;
            //While loop is to create series.
            while (arr[currentRow][currentColumn] > 0) {
                value.push("{" + currentRow + "," + currentColumn + "}");
                count++;
                if (goldTreasure.checkIfRecordExists(currentRow + 1, currentColumn, arr) && arr[currentRow + 1][currentColumn] > 0) {
                    currentRow++;
                } else if (goldTreasure.checkIfRecordExists(currentRow, currentColumn + 1, arr) && arr[currentRow][currentColumn + 1] > 0) {
                    currentColumn++;
                } else
                    break;
            }
            goldTreasure.insertCountValues(countResult, { 'key': key, 'count': count });
        });

        var output1 = '[';
        var output2 = '[';
        for (let index = 0; index < countResult.length; index++) {
            const element = countResult[index];
            output1 += element['count'].toString();
            output1 += ","
            let key = element['key'];
            for (let j = 0; j < resultSet.get(key).length; j++) {
                const item = resultSet.get(element['key'])[j];
                output2 += item;
                output2 += ","
            }
            output2 += "\n";
        }
        output1 = output1.slice(0, -1);
        output1 += "]"
        output2 = output2.slice(0, -2);
        output2 += "]"
        console.log(output1);
        console.log(output2);
        process.exit(1);
    },
    insertCountValues: function insertCountValues(countArray, currentItem) {
        countArray.push(currentItem);
        countArray.sort(function(a, b) {
            return a.count - b.count;
        });
        return countArray;
    }
};

module.exports = {
    readUserInput: goldTreasure.readUserInput,
    readInputFile: goldTreasure.readInputFile
};