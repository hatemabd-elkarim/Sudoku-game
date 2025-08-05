package com.mycompany.sudokuapp;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class BoardGenerator {

    int [][] board = new int [9][9];
    int cellsToRemove;
    BoardSolver solver = new BoardSolver();
    Level gameLevel;
    Random rand = new Random();
    
    
    public BoardGenerator(Level gameLevel) 
    {
        switch(gameLevel)
        {
            case Easy: 
            {
                cellsToRemove = 36;
                break;
            }
            case Medium:
            {
                cellsToRemove = 41;
                break;
            }    
            case Hard:
            {
                cellsToRemove = 46;
                break;
            }
            case Expert:
            {
                cellsToRemove = 56;
                break;
            }
        }
        
        fillBoard();
        solver.solution = copyBoard(board);
        removeCells(cellsToRemove);
    }
    
    void fillBoard() 
    {
        for (int row = 0; row < 9; row++)
            for (int col = 0; col < 9; col++)
                board[row][col] = 0;

        fillCell(0, 0);
    }
    
    boolean fillCell(int row, int col)
    {
        if (row == 9)   return true; // complete board
        if (col == 9)   return fillCell(row+1,0); // complete row
        
        List<Integer> rowNumbers = new ArrayList<>();
        for (int i = 1; i <= 9; i++)    rowNumbers.add(i); // fill the numbers from 1 to 9
        Collections.shuffle(rowNumbers); // randomize the order of numbers
        
        for (int num : rowNumbers)
        {
            if (isSafe(row, col, num))
            {
                board[row][col] = num; // initial change
                if (fillCell(row, col+1))   return true; // try to fill next cells
                board[row][col] = 0; // undo change
            }
            
        }
     return false;   
    }
    
    boolean isSafe(int row, int col, int num)
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
            if (num == this.board[i][col])  return false; 
        }
        
        for (int j = 0; j < 9; j++) // check that the number is not repeated within the same row
        {
            if (num == this.board[row][j])  return false;
        }
        
        return true;
    }
    
    
    void removeCells (int cellsToRemove)
    {
        while (cellsToRemove > 0)
        {
            // choose random cell to remove
            int row = rand.nextInt(9);
            int col = rand.nextInt(9);
            
            if (board[row][col] == 0)   continue;   // the cell is already empty
            
            int backup = board[row][col];
            board[row][col] = 0;
            
            int[][] copy = copyBoard(board);
            if (solver.countSolutions(copy) != 1)
            {
                board[row][col] = backup;
            }
            else
            {
                cellsToRemove--;
            }
        }
    }
    
    int[][] copyBoard(int[][] original) {
        int[][] copy = new int[9][9];
        for (int i = 0; i < 9; i++)
            System.arraycopy(original[i], 0, copy[i], 0, 9);
        return copy;
    }
}
