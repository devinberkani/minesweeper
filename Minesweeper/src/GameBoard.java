import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class GameBoard {
    private static final Scanner scanner = new Scanner(System.in);
    private boolean isGameOver;
    protected final int gameBoardWidth = 9;
    protected final int gameBoardHeight = 9;
    private char[] gameBoard = new char[gameBoardWidth * gameBoardHeight];
    private ArrayList<Integer> mineIndices = new ArrayList<>();

    private ArrayList<Integer> allUserIndices = new ArrayList<>();

    public GameBoard() {
    }

    // game board

    protected void initializeGameBoard() {

        char[] newGameBoard = new char[gameBoardWidth * gameBoardHeight];

        Arrays.fill(newGameBoard, '.');

        for (int index : getMineIndices()) {
            newGameBoard[index] = 'X';
        }

        setGameBoard(newGameBoard);
    }

    protected void printGameBoard() {

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

    protected boolean userSteppedOnMine(int convertedCoordinate) {

        for (int mineIndex: getMineIndices()) {
            if (convertedCoordinate == mineIndex) {
                return true;
            }
        }
        return false;
    }

    protected boolean checkUserInputs(int convertedCoordinate, String freeOrMine) {

        // check for index out of bounds
        if (convertedCoordinate < 0 || convertedCoordinate >= getGameBoard().length) {
            System.out.println("You have entered an invalid coordinate. Please try again:");
            return false;
        // check for "free" or "mine" as String input
        } else if (!freeOrMine.equalsIgnoreCase("free") && !freeOrMine.equalsIgnoreCase("mine")) {
            System.out.println("You must enter either \"free\" or \"mine\" as input. Please try again:");
            return false;
        }
        return true;
    }

    protected void updateGameBoardWithFreeCoordinates(GameBoard gameBoardKey, int convertedCoordinate) {
        getGameBoard()[convertedCoordinate] = gameBoardKey.getGameBoard()[convertedCoordinate];
    }

    protected void updateGameBoardWithMineCoordinates(int convertedCoordinate) {
        char[] updatedGameBoard = new char[gameBoardWidth * gameBoardHeight];

        for (int i = 0; i < updatedGameBoard.length; i++) {
            if (i == convertedCoordinate) {
                if (getGameBoard()[i] == '*') {
                    updatedGameBoard[i] = '.';
                    if (getAllUserIndices().contains(convertedCoordinate)) {
                        getAllUserIndices().remove(Integer.valueOf(convertedCoordinate));
                    } else {
                        getAllUserIndices().add(convertedCoordinate);
                    }

                } else {
                    updatedGameBoard[i] = '*';
                    if (getAllUserIndices().contains(convertedCoordinate)) {
                        getAllUserIndices().remove(Integer.valueOf(convertedCoordinate));
                    } else {
                        getAllUserIndices().add(convertedCoordinate);
                    }
                }
            } else {
                updatedGameBoard[i] = getGameBoard()[i];
            }
        }

        setGameBoard(updatedGameBoard);
    }

    // get user input for mines

    protected int getNumberOfMines() {
        System.out.println("How many mines do you want on the field?");

        return scanner.nextInt();
    }

    protected void generateMines(int numOfMines, int firstUserCoordinate) {
        Random random = new Random();
        int upperBound = gameBoardWidth * gameBoardHeight;

        ArrayList<Integer> newMineIndices = new ArrayList<>();
        ArrayList<Integer> allUserIndices = new ArrayList<>();

        while (newMineIndices.size() != numOfMines) {
            int currentRandomNumber = random.nextInt(upperBound);
            while (currentRandomNumber == firstUserCoordinate) {
                currentRandomNumber = random.nextInt(upperBound);
            }
            if (!newMineIndices.contains(currentRandomNumber)) {
                newMineIndices.add(currentRandomNumber);
                allUserIndices.add(currentRandomNumber);
            }
        }

        setMineIndices(newMineIndices);
        setAllUserIndices(allUserIndices);
    }

    protected void updateGameBoardWithMines() {
        char[] updatedGameBoard = new char[gameBoardWidth * gameBoardHeight];

        for (int i = 0; i < updatedGameBoard.length; i++) {
            if (getGameBoard()[i] == 'X') {
                updatedGameBoard[i] = 'X';
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


    protected boolean isGameOver() {
        return isGameOver;
    }

    protected void setGameOver(boolean gameOver) {
        isGameOver = gameOver;
    }

    protected char[] getGameBoard() {
        return gameBoard;
    }

    protected void setGameBoard(char[] gameBoard) {
        this.gameBoard = gameBoard;
    }

    protected ArrayList<Integer> getMineIndices() {
        return mineIndices;
    }

    private void setMineIndices(ArrayList<Integer> mineIndices) {
        this.mineIndices = mineIndices;
    }

    protected ArrayList<Integer> getAllUserIndices() {
        return allUserIndices;
    }

    protected void setAllUserIndices(ArrayList<Integer> allUserIndices) {
        this.allUserIndices = allUserIndices;
    }
}