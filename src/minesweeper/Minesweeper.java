package minesweeper;

import java.util.Random;
import java.util.Scanner;

public class Minesweeper {

    private static final int GRID_SIZE = 10;
    private static final int NUM_MINES = 10;
    private static final char MINE_CHAR = 'X';
    private static final char HIDDEN_CHAR = '■';
    private static final char REVEALED_CHAR = ' ';
    
    private static char[][] grid;
    private static boolean[][] revealed;
    private static int remainingCells;

    public static void main(String[] args) {
        grid = new char[GRID_SIZE][GRID_SIZE];
        revealed = new boolean[GRID_SIZE][GRID_SIZE];
        remainingCells = GRID_SIZE * GRID_SIZE - NUM_MINES;
        printInstructions();
        initializeGrid();
        placeMines();
        printGrid();

        Scanner scanner = new Scanner(System.in);
        while (remainingCells > 0) {
            System.out.print("Enter the row : ");
            int row = scanner.nextInt();
            System.out.print("Enter the column : ");
            int col = scanner.nextInt();
            scanner.nextLine(); 
            if (isValidCoordinate(row, col) && !revealed[row][col]) {
                revealCell(row, col);
                printGrid();

                if (grid[row][col] == MINE_CHAR) {
                    System.out.println("Boom! You hit a mine. Game Over!");
                    break;
                } else if (remainingCells == 0) {
                    System.out.println("Congratulations! You won!");
                    break;
                }
            } else {
                System.out.println("Invalid input or already revealed cell. Try again.");
            }
        }

        scanner.close();
    }

    
    private static void printInstructions() {
        System.out.println("\n Welcome to Minesweeper! \n \n" +
                "1. To start playing, enter the row and column numbers of the cell you want to reveal.   \n" +
                "2. Use numbers 0 to 9 to specify rows and columns on the game board.\n" +
                "3. For instance, input '4' for the row and '3' for the column to uncover the cell in the fourth row and third column.\n" +
                "4. Be careful not to reveal any mines (X) as that will result in losing the game.\n" +
                "5. Win the game by successfully revealing all non-mine cells on the board.\n \n" +
                "Enjoy playing Minesweeper!");
    }

    private static void initializeGrid() {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                grid[i][j] = HIDDEN_CHAR;
                revealed[i][j] = false;
            }
        }
    }

    private static void placeMines() {
        Random random = new Random();
        int minesPlaced = 0;

        while (minesPlaced < NUM_MINES) {
            int row = random.nextInt(GRID_SIZE);
            int col = random.nextInt(GRID_SIZE);

            if (grid[row][col] != MINE_CHAR) {
                grid[row][col] = MINE_CHAR;
                minesPlaced++;
            }
        }
    }

    private static boolean isValidCoordinate(int row, int col) {
        return row >= 0 && row < GRID_SIZE && col >= 0 && col < GRID_SIZE;
    }

    private static void revealCell(int row, int col) {
        if (isValidCoordinate(row, col) && !revealed[row][col]) {
            revealed[row][col] = true;

            if (grid[row][col] != MINE_CHAR) {
                remainingCells--;

                int numSurroundingMines = countSurroundingMines(row, col);
                grid[row][col] = (char) ('0' + numSurroundingMines);

                if (numSurroundingMines == 0) {
                    // Auto-reveal neighboring cells if the current cell has no surrounding mines
                    for (int i = row - 1; i <= row + 1; i++) {
                        for (int j = col - 1; j <= col + 1; j++) {
                            revealCell(i, j);
                        }
                    }
                }
            }
        }
    }

    private static int countSurroundingMines(int row, int col) {
        int count = 0;

        for (int i = row - 1; i <= row + 1; i++) {
            for (int j = col - 1; j <= col + 1; j++) {
                if (isValidCoordinate(i, j) && grid[i][j] == MINE_CHAR) {
                    count++;
                }
            }
        }

        return count;
    }

    private static void printGrid() {
        System.out.println("\n  0 1 2 3 4 5 6 7 8 9");
        for (int i = 0; i < GRID_SIZE; i++) {
            System.out.print(i + " ");
            for (int j = 0; j < GRID_SIZE; j++) {
                char cell = revealed[i][j] ? grid[i][j] : HIDDEN_CHAR;
                System.out.print(cell + " ");
            }
            System.out.println();
        }
    }
    
}