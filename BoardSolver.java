package com.mycompany.sudokuapp;

public class BoardSolver {
    int count;
    int[][] solution = new int [9][9];
    
    public int countSolutions(int board[][])
    {
        count = 0;
        solve(board);
        return count;
    }
    
    public void solve(int board[][])
    {
        for (int row = 0; row < 9; row++)
        {
            for (int col = 0; col < 9; col++)
            {
                if (board[row][col] == 0)
                {
                    for (int num = 1; num <= 9; num++)
                    {
                        if (isSafe(board, row, col, num))
                        {
                            board[row][col] = num;
                            solve(board); // try to fill the rest of cells upon the current change
                            board[row][col] = 0; // reset and try another change
                        }
                    }
                    return; // can't fill all of cells
                }
            }
        }
        count++;
    }
    
    boolean isSafe(int board[][] ,int row, int col, int num)
    {
        int boxStartRow = row - row%3;
        int boxStartCol = col - col%3;
        
        for (int i = 0; i < 3; i++) // check that the number is not repeated within its 3*3 box
        {
            for (int j = 0; j < 3; j++) 
            {
                if (board[boxStartRow + i][boxStartCol + j] == num)
                    return false;
            }
        }
        
        for (int i = 0; i < 9; i++) // check that the number is not repeated within the same column
        {
            if (num == board[i][col])  return false; 
        }
        
        for (int j = 0; j < 9; j++) // check that the number is not repeated within the same row
        {
            if (num == board[row][j])  return false;
        }
        
        return true;
    }
}
