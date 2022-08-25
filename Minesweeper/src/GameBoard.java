import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class GameBoard {
    private final int gameBoardWidth = 9;
    private final int gameBoardHeight = 9;
    private char[] gameBoard = new char[gameBoardWidth * gameBoardHeight];
    private ArrayList<Integer> mineIndices = new ArrayList<>();

    // get [userInput] random numbers between 0 and [gameBoardWidth * gameBoardHeight]

    public GameBoard() {
        getUserInput();
        initializeGameBoard();
        printGameBoard();
    }

    // game board

    private void initializeGameBoard() {

        char[] newGameBoard = new char[gameBoardWidth * gameBoardHeight];

        Arrays.fill(newGameBoard, '.');

        for (int index : getMineIndices()) {
            newGameBoard[index] = 'X';
        }

        setGameBoard(newGameBoard);
    }

    private void printGameBoard() {

        int rowTracker = 0;
        for (int i = 0; i < getGameBoard().length; i++) {
            rowTracker++;
            System.out.print(getGameBoard()[i]);
            // print new line for all but the last row
            if (rowTracker % 9 == 0 && i != getGameBoard().length - 1) {
                System.out.println();
            }
        }
    }

    // get user input for mines

    private void getUserInput() {
        System.out.println("How many mines do you want on the field?");

        Scanner scanner = new Scanner(System.in);

        int numOfMines = scanner.nextInt();

        generateMines(numOfMines);
    }

    private void generateMines(int numOfMines) {
        Random random = new Random();
        int upperBound = gameBoardWidth * gameBoardHeight;

        ArrayList<Integer> newMineIndices = new ArrayList<>();

        while (newMineIndices.size() != numOfMines) {
            int currentRandomNumber = random.nextInt(upperBound);
            if (!newMineIndices.contains(currentRandomNumber)) {
                newMineIndices.add(currentRandomNumber);
            }
        }

        setMineIndices(newMineIndices);
    }

    // getters and setters

    private char[] getGameBoard() {
        return gameBoard;
    }

    private void setGameBoard(char[] gameBoard) {
        this.gameBoard = gameBoard;
    }

    private ArrayList<Integer> getMineIndices() {
        return mineIndices;
    }

    private void setMineIndices(ArrayList<Integer> mineIndices) {
        this.mineIndices = mineIndices;
    }
}


