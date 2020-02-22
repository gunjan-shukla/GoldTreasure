Steps :

Series : Consecutive presence of 1's i.e. gold treasure.
Coordinate : Row and Column of any element in the array.

1) Code asks if user want to enter the values OR read them from a file.
2) Enter Y if you want to enter the values. In this case user is expected to enter number of rows.
3) Enter N and then file name from the samples already added in the directory structure. New Sample files can be added here only.
4) input array is generated from either file/user inputs.
Lets say input array is :
1,0,0,1
1,0,1
0,0,1,0,1
1,0,1,0,1
1,0,1,1

5) Another array is calculated from the input array using dynamic programming to generate series of 1's.
That is stored in variable arr and calculated in method getGoldTreasure. arr in the above case will be:
1 0 0 1 -1
2 0 1 -1 -1
0 0 2 0 1
1 0 3 0 2
2 0 4 5 -1

and recordSet is used to store the starting coordinate of the series. This is basically index of all 1's.
recordSet keys in this case is :

6) After this printResultSet is called to print the final output. printResultSet will store one more array to
store series length as output needs to be sorted in increasing order of series length. This is countResult.

Sorting countResult will help creating the desired output.

Improvement : 
1) In place of hashmap module simple json object could be used as json object properties are also unique.
2) Insertion of -1 to make jagged a normal 2D array can be avoided.


