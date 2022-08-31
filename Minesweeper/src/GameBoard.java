import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class GameBoard {
    private static final Scanner scanner = new Scanner(System.in);
    private boolean isGameOver;
    private final int gameBoardWidth = 9;
    private final int gameBoardHeight = 9;
    private char[] gameBoard = new char[gameBoardWidth * gameBoardHeight];
    private char[][] userGameBoard = new char[gameBoardWidth][gameBoardHeight];
    private ArrayList<Integer> mineIndices = new ArrayList<>();

    // YOU ARE HERE **********
    // user needs to be able to type mine or free as a part of input
    // input should then modify only the user game board
    // implement flood fill algorithm
    // implement game over logic (if user finds all mines or if user steps on a mine)


    public GameBoard() {
        getNumberOfMines();
        initializeGameBoards();
        updateGameBoardWithMines();
        printGameBoard();
        printUserGameBoard();

        while(!isGameOver) {
//            System.out.println(getMineIndices());
            getUserCoordinates();
            printGameBoard();
            printUserGameBoard();
            setGameOver(getMineIndices().size() == 0);
        }

        // game won message
        System.out.println("Congratulations! You found all the mines!");
    }

    // user game board

    private void initializeUserGameBoard() {
        char[][] newGameBoard = new char[gameBoardWidth][gameBoardHeight];

        for (char[] chars : newGameBoard) {
            Arrays.fill(chars, '.');
        }

        setUserGameBoard(newGameBoard);
    }

    private void printUserGameBoard() {
        // print top of game board

        System.out.println(" |123456789|");
        System.out.println("-|---------|");
        String borderSymbol = "|";

        int rowNumber = 1;

        for (int i = 0; i < getUserGameBoard().length; i++) {
            System.out.print(rowNumber);
            System.out.print(borderSymbol);
            rowNumber++;
            for (int j = 0; j < getUserGameBoard()[i].length; j++) {
                System.out.print(getUserGameBoard()[i][j]);
            }
            System.out.println(borderSymbol);
        }

        // print bottom of game board
        System.out.println("-|---------|");
    }

    // game board

    private void initializeGameBoards() {

        initializeUserGameBoard();

        char[] newGameBoard = new char[gameBoardWidth * gameBoardHeight];

        Arrays.fill(newGameBoard, '.');

        for (int index : getMineIndices()) {
            newGameBoard[index] = 'X';
        }

        setGameBoard(newGameBoard);
    }

    private void printGameBoard() {

        // print top of game board

        System.out.println(" |123456789|");
        System.out.println("-|---------|");
        String borderSymbol = "|";

        int rowNumber = 1;
        int endOfRowTracker = 0;
        for (int i = 0; i < getGameBoard().length; i++) {

            // print start of each row
            if (i % gameBoardWidth == 0) {
                System.out.print(rowNumber);
                System.out.print(borderSymbol);
                rowNumber++;
            }

            // print game board
            System.out.print(getGameBoard()[i]);

            // print end of each row
            endOfRowTracker++;
            if (endOfRowTracker % gameBoardWidth == 0) {
                System.out.println(borderSymbol);
            }
        }

        // print bottom of game board
        System.out.println("-|---------|");
    }

    // get user input for coordinates

    private void getUserCoordinates() {

        boolean coordinatesValid = false;

        while (!coordinatesValid) {
            System.out.print("Set/delete mines marks (x and y coordinates): ");

            int coordinateOne = scanner.nextInt();
            int coordinateTwo = scanner.nextInt();

            int convertedCoordinate = gameBoardWidth * (coordinateTwo - 1) + (coordinateOne - 1);
            char location = getGameBoard()[convertedCoordinate];

            if (location == '*' || location == '.') {
                coordinatesValid = true;
                updateGameBoardWithCoordinates(convertedCoordinate);
            } else {
                System.out.println("There is a number here!");
            }
        }

    }

    private void updateGameBoardWithCoordinates(int coordinate) {
        char[] updatedGameBoard = new char[gameBoardWidth * gameBoardHeight];

        for (int i = 0; i < updatedGameBoard.length; i++) {
            if (i == coordinate) {
                if (getGameBoard()[i] == '*') {
                    updatedGameBoard[i] = '.';
                    if (getMineIndices().contains(coordinate)) {
                        getMineIndices().remove(Integer.valueOf(coordinate));
                    } else {
                        getMineIndices().add(coordinate);
                    }

                } else {
                    updatedGameBoard[i] = '*';
                    if (getMineIndices().contains(coordinate)) {
                        getMineIndices().remove(Integer.valueOf(coordinate));
                    } else {
                        getMineIndices().add(coordinate);
                    }
                }
            } else {
                updatedGameBoard[i] = getGameBoard()[i];
            }
        }

        setGameBoard(updatedGameBoard);
    }

    // get user input for mines

    private void getNumberOfMines() {
        System.out.println("How many mines do you want on the field?");

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

    private void updateGameBoardWithMines() {
        char[] updatedGameBoard = new char[gameBoardWidth * gameBoardHeight];

        for (int i = 0; i < updatedGameBoard.length; i++) {
            if (getGameBoard()[i] == 'X') {
                updatedGameBoard[i] = '.';
            } else {
                int numOfMines = getNumberOfMines(i);
                updatedGameBoard[i] = numOfMines == 0 ? getGameBoard()[i] : (char) (numOfMines + '0'); // converts int to char
            }
        }

        setGameBoard(updatedGameBoard);
    }

    // calculate number of mines around each empty cell
    private int getNumberOfMines(int currentIndex) {
        // check if index is corner - check 3 cells
        boolean isCorner = isCorner(currentIndex);

        // check if index is side - check 5 cells
        boolean isSide = isSide(currentIndex);

        // else is middle - check 8 cells

        String exactLocation = findLocation(isCorner, isSide, currentIndex);

        return minesAroundLocation(exactLocation, currentIndex);
    }

    private int minesAroundLocation(String exactLocation, int currentIndex) {

        int mineCount = 0;

        // corners
        if (exactLocation.equals("topleft")) {
            if (getGameBoard()[1] == 'X') {
                mineCount++;
            }
            if (getGameBoard()[gameBoardWidth] == 'X') {
                mineCount++;
            }
            if (getGameBoard()[gameBoardWidth + 1] == 'X') {
                mineCount++;
            }
        } else if (exactLocation.equals("topright")) {
            if (getGameBoard()[gameBoardWidth - 2] == 'X') {
                mineCount++;
            }
            if (getGameBoard()[gameBoardWidth + gameBoardWidth - 2] == 'X') {
                mineCount++;
            }
            if (getGameBoard()[gameBoardWidth + gameBoardWidth - 1] == 'X') {
                mineCount++;
            }
        } else if (exactLocation.equals("bottomleft")) {
            if (getGameBoard()[getGameBoard().length - (gameBoardWidth * 2)] == 'X') {
                mineCount++;
            }
            if (getGameBoard()[getGameBoard().length - (gameBoardWidth * 2) + 1] == 'X') {
                mineCount++;
            }
            if (getGameBoard()[(getGameBoard().length - gameBoardWidth) + 1] == 'X') {
                mineCount++;
            }
        } else if (exactLocation.equals("bottomright")) {
            if (getGameBoard()[getGameBoard().length - 2] == 'X') {
                mineCount++;
            }
            if (getGameBoard()[getGameBoard().length - (gameBoardWidth + 2)] == 'X') {
                mineCount++;
            }
            if (getGameBoard()[getGameBoard().length - (gameBoardWidth + 1)] == 'X') {
                mineCount++;
            }
        // sides
        } else if (exactLocation.equals("leftside")) {
            if (getGameBoard()[currentIndex - gameBoardWidth] == 'X') {
                mineCount++;
            }
            if (getGameBoard()[(currentIndex - gameBoardWidth) + 1] == 'X') {
                mineCount++;
            }
            if (getGameBoard()[currentIndex + 1] == 'X') {
                mineCount++;
            }
            if (getGameBoard()[currentIndex + gameBoardWidth] == 'X') {
                mineCount++;
            }
            if (getGameBoard()[(currentIndex + gameBoardWidth) + 1] == 'X') {
                mineCount++;
            }
        } else if (exactLocation.equals("rightside")) {
            if (getGameBoard()[currentIndex - gameBoardWidth] == 'X') {
                mineCount++;
            }
            if (getGameBoard()[currentIndex - (gameBoardWidth + 1)] == 'X') {
                mineCount++;
            }
            if (getGameBoard()[currentIndex - 1] == 'X') {
                mineCount++;
            }
            if (getGameBoard()[(currentIndex + gameBoardWidth) - 1] == 'X') {
                mineCount++;
            }
            if (getGameBoard()[currentIndex + gameBoardWidth] == 'X') {
                mineCount++;
            }
        } else if (exactLocation.equals("topside")) {
            if (getGameBoard()[currentIndex - 1] == 'X') {
                mineCount++;
            }
            if (getGameBoard()[(currentIndex + gameBoardWidth) - 1] == 'X') {
                mineCount++;
            }
            if (getGameBoard()[currentIndex + gameBoardWidth] == 'X') {
                mineCount++;
            }
            if (getGameBoard()[(currentIndex + gameBoardWidth) + 1] == 'X') {
                mineCount++;
            }
            if (getGameBoard()[currentIndex + 1] == 'X') {
                mineCount++;
            }
        } else if (exactLocation.equals("bottomside")) {
            if (getGameBoard()[currentIndex - 1] == 'X') {
                mineCount++;
            }
            if (getGameBoard()[(currentIndex - gameBoardWidth) - 1] == 'X') {
                mineCount++;
            }
            if (getGameBoard()[currentIndex - gameBoardWidth] == 'X') {
                mineCount++;
            }
            if (getGameBoard()[(currentIndex - gameBoardWidth) + 1] == 'X') {
                mineCount++;
            }
            if (getGameBoard()[currentIndex + 1] == 'X') {
                mineCount++;
            }
        // middle
        } else if (exactLocation.equals("middle")) {
            if (getGameBoard()[currentIndex - 1] == 'X') {
                mineCount++;
            }
            if (getGameBoard()[(currentIndex - gameBoardWidth) - 1] == 'X') {
                mineCount++;
            }
            if (getGameBoard()[currentIndex - gameBoardWidth] == 'X') {
                mineCount++;
            }
            if (getGameBoard()[(currentIndex - gameBoardWidth) + 1] == 'X') {
                mineCount++;
            }
            if (getGameBoard()[currentIndex + 1] == 'X') {
                mineCount++;
            }
            if (getGameBoard()[(currentIndex + gameBoardWidth) + 1] == 'X') {
                mineCount++;
            }
            if (getGameBoard()[currentIndex + gameBoardWidth] == 'X') {
                mineCount++;
            }
            if (getGameBoard()[(currentIndex + gameBoardWidth) - 1] == 'X') {
                mineCount++;
            }
        }
        return mineCount;
    }

    private String findLocation(boolean isCorner, boolean isSide, int currentIndex) {

        // corner locations
        if (isCorner) {
            // top left corner
            if (currentIndex == 0) {
                return "topleft";
            // top right corner
            } else if (currentIndex == gameBoardWidth - 1) {
                return "topright";
            // bottom left corner
            } else if (currentIndex == getGameBoard().length - gameBoardWidth) {
                return "bottomleft";
            // bottom right corner
            } else if (currentIndex == getGameBoard().length - 1) {
                return "bottomright";
            }
        // side locations
        } else if (isSide) {
            // left side
            if (currentIndex % gameBoardWidth == 0) {
                return "leftside";
            // right side
            } else if ((currentIndex - (gameBoardWidth - 1)) % gameBoardWidth == 0) {
                return "rightside";
            // top side
            } else if (currentIndex > 0 && currentIndex < gameBoardWidth) {
                return "topside";
            // bottom side
            } else if (currentIndex > getGameBoard().length - gameBoardWidth && currentIndex < getGameBoard().length - 1) {
                return "bottomside";
            }
        }
        return "middle";
    }

    private boolean isCorner(int currentIndex) {
        // top left corner
        if (currentIndex == 0) {
            return true;
        // top right corner
        } else if (currentIndex == gameBoardWidth - 1) {
            return true;
        // bottom left corner
        } else if (currentIndex == getGameBoard().length - gameBoardWidth) {
            return true;
        // bottom right corner
        } else if (currentIndex == getGameBoard().length - 1) {
            return true;
        }
        return false;
    }

    private boolean isSide(int currentIndex) {

        // return false no matter what if index is a corner
        if (isCorner(currentIndex)) {
            return false;
        }

        // left side
        if (currentIndex % gameBoardWidth == 0) {
            return true;
        // right side
        } else if ((currentIndex - (gameBoardWidth - 1)) % gameBoardWidth == 0) {
            return true;
        // top side
        } else if (currentIndex > 0 && currentIndex < gameBoardWidth) {
            return true;
        // bottom side
        } else if (currentIndex > getGameBoard().length - gameBoardWidth && currentIndex < getGameBoard().length - 1) {
            return true;
        }
        return false;
    }

    // getters and setters


    public boolean isGameOver() {
        return isGameOver;
    }

    public void setGameOver(boolean gameOver) {
        isGameOver = gameOver;
    }

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

    public char[][] getUserGameBoard() {
        return userGameBoard;
    }

    public void setUserGameBoard(char[][] userGameBoard) {
        this.userGameBoard = userGameBoard;
    }
}