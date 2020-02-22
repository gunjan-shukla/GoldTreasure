const readline = require('readline');
const goldTreasure = require('./goldTreasure');
const fs = require('fs');
const path = require('path');
const scanner = readline.createInterface({
    input: process.stdin,
    output: process.stdout
});

scanner.question('Press Y if you want to enter input array and N to use array from csv file:  ', (response) => {
    if (response.toString().toLowerCase() === "y") {
        //The below option reads user input for jagged array and provide results
        readUserInput()
    } else if (response.toString().toLowerCase() === "n") {
        //The below option reads input from a csv file
        readFile();
    } else {
        console.log("Wrong input provided");
        process.exit(1);
    }
});

function readUserInput() {
    goldTreasure.readUserInput(scanner);
}

function readFile() {
    //Sample input file can ne passed as in input.
    scanner.question('Enter file path:  ', (filePath) => {
        let finalPath = path.join(__dirname, filePath);
        goldTreasure.readInputFile(finalPath, scanner);
    });
}