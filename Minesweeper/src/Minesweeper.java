import java.util.Scanner;

public class Minesweeper {
    private static final Scanner scanner = new Scanner(System.in);
    private final GameBoard gameBoardKey = new GameBoard();
    private final GameBoard userGameBoard = new GameBoard();
    private boolean steppedOnMine;

    boolean firstTurn = true;
    public Minesweeper() {

        // initialize game board key
        int numOfMines = gameBoardKey.getNumberOfMines();
        gameBoardKey.initializeGameBoard();

        // initialize user game board
        userGameBoard.initializeGameBoard();
        userGameBoard.printGameBoard();

        while(!gameBoardKey.isGameOver()) {
            gameBoardKey.printGameBoard();
            userGameBoard.printGameBoard();
            System.out.println(gameBoardKey.getMineIndices());
            getUserInput(numOfMines);
            if (checkForWinner()) {
                gameBoardKey.setGameOver(true);
            }
        }

        // game over message

        if (isSteppedOnMine()) {
            printLosingGameBoard();
            System.out.println("You stepped on a mine and failed!");
        } else {
            userGameBoard.printGameBoard();
            System.out.println("Congratulations! You found all the mines!");
        }
    }


    // get user input for coordinates

    private void getUserInput(int numOfMines) {

        boolean coordinatesValid = false;

        int convertedCoordinate = 0;
        String freeOrMine = "";

        // initialize variables for flood fill algorithm (filling game board with empty spaces)
        int coordinateOne = 0;
        int coordinateTwo = 0;

        while (!coordinatesValid) {
            System.out.print("Set/delete mines marks (x and y coordinates): ");

            coordinateOne = scanner.nextInt();
            coordinateTwo = scanner.nextInt();
            freeOrMine = scanner.next();

            convertedCoordinate = gameBoardKey.gameBoardWidth * (coordinateTwo - 1) + (coordinateOne - 1);

            coordinatesValid = gameBoardKey.checkUserInputs(convertedCoordinate, freeOrMine);
        }

        // make sure the first choice is never a mine

        if (firstTurn) {
            gameBoardKey.generateMines(numOfMines, convertedCoordinate);
            gameBoardKey.initializeGameBoard(); // important - this makes sure X's are added where mines are
            gameBoardKey.updateGameBoardWithMines();
            firstTurn = false;
        }

        char location = gameBoardKey.getGameBoard()[convertedCoordinate];

        if (freeOrMine.equalsIgnoreCase("free")) {
            // check if user stepped on mine
            if (gameBoardKey.userSteppedOnMine(convertedCoordinate)) {
                setSteppedOnMine(true);
                gameBoardKey.setGameOver(true);
                // check if location is a number or not
            } else if (location == '.') {
                floodFill(coordinateOne, coordinateTwo);
            } else {
                userGameBoard.updateGameBoardWithFreeCoordinates(gameBoardKey, convertedCoordinate);
            }
        } else {
            gameBoardKey.updateGameBoardWithMineCoordinates(convertedCoordinate);
            userGameBoard.updateGameBoardWithMineCoordinates(convertedCoordinate);
            gameBoardKey.setGameOver(gameBoardKey.getMineIndices().size() == 0);
        }
    }

    private void floodFill(int coordinateOne, int coordinateTwo) {
        System.out.println("in flood fill");
        char oldValOne = '.';
        char oldValTwo = 'X';
        char newVal = '/';

        int convertedCoordinate = gameBoardKey.gameBoardWidth * (coordinateTwo - 1) + (coordinateOne - 1);

        System.out.println("converted coordinate is " + convertedCoordinate);

        // make sure coordinate is in bounds
        if (convertedCoordinate < 0 || convertedCoordinate >= gameBoardKey.getGameBoard().length) {
            return;
        }

        // make sure the current position equals the old value
        if (gameBoardKey.getGameBoard()[convertedCoordinate] != oldValOne) {
//            userGameBoard.getGameBoard()[convertedCoordinate] = gameBoardKey.getGameBoard()[convertedCoordinate];
            return;
        }

        gameBoardKey.getGameBoard()[convertedCoordinate] = newVal;
        userGameBoard.getGameBoard()[convertedCoordinate] = newVal;

        floodFill(coordinateOne + 1, coordinateTwo);
        floodFill(coordinateOne - 1, coordinateTwo);
        floodFill(coordinateOne, coordinateTwo + 1);
        floodFill(coordinateOne, coordinateTwo - 1);

    }

    private boolean checkForWinner() {

        for (int mineIndex : gameBoardKey.getMineIndices()) {
            if (gameBoardKey.getGameBoard()[mineIndex] != '.') {
                return false;
            }
        }

        for (int i = 0; i < gameBoardKey.getGameBoard().length; i++) {
            if (gameBoardKey.getGameBoard()[i] == '.') {
                return false;
            }
        }

        return true;
    }

    private void printLosingGameBoard() {

        for (int mineIndex : gameBoardKey.getMineIndices()) {
            userGameBoard.getGameBoard()[mineIndex] = 'X';
        }

        userGameBoard.printGameBoard();
    }

    // getters and setters


    public boolean isSteppedOnMine() {
        return steppedOnMine;
    }

    public void setSteppedOnMine(boolean steppedOnMine) {
        this.steppedOnMine = steppedOnMine;
    }
}
