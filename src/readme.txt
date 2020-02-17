Steps :

Series : Consecutive presence of 1's i.e. gold treasure.
Coordinate : Row and Column of any element in the array.

1) Code asks if user want to enter the values OR read them from a file.
2) Enter Y if you want to enter the values.
3) Otherwise change the file location as per your directory structure. Sample files are provided.
4) In both cases user is expected to enter number of rows / number of lines in file for convenience
5) input array is generated from either file/user inputs.
Lets say input array is :
1,0,0,1
1,0,1
0,0,1,0,1
1,0,1,0,1
1,0,1,1

6) Another array is calculated from the input array using dynamic programming to generate series of 1's.
That is stored in variable arr and calculated in method getGoldTreasure. arr in the above case will be:
1 0 0 1 0
2 0 1 0 0
0 0 2 0 1
1 0 3 0 2
2 0 4 5 0

and recordSet is used to store the starting coordinate of the series. This is basically index of all 1's.
recordSet keys in this case is :


7) After this printResultSet is called to print the final output. printResultSet will store one more hashmap to
store series length as output needs to be sorted in increasing order of series length. This is countResult.

Post this after sorting countResult. both has maps are printed.






