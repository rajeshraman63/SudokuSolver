package com.rrb.sudokusolver;

public class SudokuSolver {

    public  boolean usedInRow(int[][] grid, int n, int row, int num) {
        for (int col = 0; col < n; col++) {
            if (grid[row][col] == num)
                return true;
        }
        return false;
    }

    public  boolean usedInCol(int[][] grid, int n, int col, int num) {

        for (int row = 0; row < n; row++) {
            if (grid[row][col] == num)
                return true;
        }
        return false;
    }

/* Returns a boolean which indicates whether an assigned entry
within the specified 3x3 box matches the given number. */

    public  boolean usedInBox(int[][] grid, int n, int boxStartRow, int boxStartCol, int num) {

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (grid[row + boxStartRow][col + boxStartCol] == num)
                    return true;
            }
        }
        return false;
    }

    public  boolean isSafe(int[][] grid, int n, int row, int col, int num) {

        return !usedInRow(grid, n, row, num) && !usedInCol(grid, n, col, num) && !usedInBox(grid, n, row - row % 3, col - col % 3, num) && grid[row][col] == 0;
    }


    public  Cell findUnassignedLocation(int[][] grid, int n, int row, int col) {
        Cell cell;
        for (row = 0; row < n; row++) {
            for (col = 0; col < n; col++) {
                if (grid[row][col] == 0) {
                    cell = new Cell(row, col);
                    return cell;
                }
            }
        }
        return null;
    }


    public static  boolean presentInRow(int [][]grid,int n,int num,int row,int col){
        for(int j=0;j<n;j++){
            if(j != col && grid[row][j] == num){
                return true;
            }
        }
        return false;
    }

    public static  boolean presentInCol(int [][]grid,int n,int num,int row,int col){
        for(int i=0;i<n;i++){
            if(i != row && grid[i][col] == num){
                return true;
            }
        }
        return false;
    }

    public static  boolean presentInBox(int [][]grid,int n,int num,int row,int col,int boxStartRow,int boxStartCol){
        for(int i=0;i<3;i++) {
            for (int j = 0; j < 3; j++) {
                int tempRow = i + boxStartRow;
                int tempCol = j + boxStartCol;
                if (tempRow != row && tempCol != col &&  grid[tempRow][tempCol] == num)
                    return true;
            }
        }
        return false;
    }

    public static boolean invalidInput(int [][]grid,int n){
        for(int i=0;i<n;i++){
            for(int j=0;j<n;j++){
                int num = grid[i][j];
                if(num == 0)
                    continue;
                if(presentInRow(grid,n,num,i,j)){
                    return true;
                }
                if(presentInCol(grid,n,num,i,j)){
                    return true;
                }
                if(presentInBox(grid,n,num,i,j,i-i%3,j-j%3)){
                    return true;
                }
            }
        }

        return false;
    }

    public String solveSudoku(int [][]grid,int n){
        if(invalidInput(grid,n)){
            System.out.println("Invalid Input");
//            return false;
            return "Invalid Input";
        }

        if(solveSudokuUtil(grid,n)){
            return "Exists";
        }else{
            return "Doesn't Exist";
        }
    }


    public  boolean solveSudokuUtil(int[][] grid,int n) {

        int row = 0, col = 0;

//        finding the location with initial value as zero
        Cell cell = findUnassignedLocation(grid, n, row, col);

        if (cell == null) {
            // since there are no left over spaces for filling a new element in the cell
            // the sudoku has been solved now
            return true;
        }

        row = cell.row;
        col = cell.col;

        // filling the soduko cell with the numbers from 1 to 9

        for (int num = 1; num <= 9; num++) {

            if (isSafe(grid, n, row, col, num)) {
                grid[row][col] = num;
                if (solveSudokuUtil(grid, n))
                    return true;

                grid[row][col] = 0;   // backtracking
            }
        }

        return false;
    }



}
