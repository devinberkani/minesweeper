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
//        userGameBoard.printGameBoard();

        while(!gameBoardKey.isGameOver()) {
            gameBoardKey.printGameBoard();
            userGameBoard.printGameBoard();
//            System.out.println("remaining mine indices: " + gameBoardKey.getMineIndices());
//            System.out.println("remaining all user indices " + gameBoardKey.getAllUserIndices());
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
                // convert game boards to multi dimensional arrays
                char[][] multiGameKey = convertToMultiDimensionalArray(gameBoardKey.getGameBoard());
                char[][] multiUser = convertToMultiDimensionalArray(userGameBoard.getGameBoard());
                floodFill(multiGameKey, multiUser, coordinateOne - 1, coordinateTwo - 1);
                // change game boards back to single dimensional arrays
                gameBoardKey.setGameBoard(convertToSingleDimensionalArray(multiGameKey));
                userGameBoard.setGameBoard(convertToSingleDimensionalArray(multiUser));
            } else {
                userGameBoard.updateGameBoardWithFreeCoordinates(gameBoardKey, convertedCoordinate);
            }
        } else {
            gameBoardKey.updateGameBoardWithMineCoordinates(convertedCoordinate);
            userGameBoard.updateGameBoardWithMineCoordinates(convertedCoordinate);
            gameBoardKey.setGameOver(gameBoardKey.getAllUserIndices().size() == 0);
        }
    }

    private void floodFill(char[][] multiGameKey, char[][] multiUser, int coordinateOne, int coordinateTwo) {
        char oldVal = '.';
        char newVal = '/';

        if (coordinateOne < 0 || coordinateOne >= 9 || coordinateTwo < 0 || coordinateTwo >= 9) {
            return;
        }

        // make sure the current position equals the old value
        if (multiGameKey[coordinateTwo][coordinateOne] != oldVal) {
            // YOU NEED TO FIX THIS -- LOOK AT ERROR IN HYPERSKILL SUBMISSION
            multiUser[coordinateTwo][coordinateOne] = multiGameKey[coordinateTwo][coordinateOne];
            return;
        }

        multiGameKey[coordinateTwo][coordinateOne] = newVal;
        multiUser[coordinateTwo][coordinateOne] = newVal;

        floodFill(multiGameKey, multiUser, coordinateOne - 1, coordinateTwo - 1); // 4 4
        floodFill(multiGameKey, multiUser, coordinateOne - 1, coordinateTwo); // 4 5
        floodFill(multiGameKey, multiUser, coordinateOne - 1, coordinateTwo + 1); // 4 6
        floodFill(multiGameKey, multiUser, coordinateOne, coordinateTwo - 1); // 5 4
        floodFill(multiGameKey, multiUser, coordinateOne, coordinateTwo + 1); // 5 6
        floodFill(multiGameKey, multiUser, coordinateOne + 1, coordinateTwo - 1); // 6 4
        floodFill(multiGameKey, multiUser, coordinateOne + 1, coordinateTwo); // 6 5
        floodFill(multiGameKey, multiUser, coordinateOne + 1, coordinateTwo + 1); // 6 6
    }

    private char[][] convertToMultiDimensionalArray(char[] gameBoard) {

        char[][] convertedGameBoard = new char[gameBoardKey.gameBoardWidth][gameBoardKey.gameBoardHeight];

        int index = 0;
        for (int i = 0; i < convertedGameBoard.length; i++) {
            for (int j = 0; j < convertedGameBoard[i].length; j++) {
                convertedGameBoard[i][j] = gameBoard[index];
                index++;
            }
        }

        return convertedGameBoard;
    }

    private char[] convertToSingleDimensionalArray(char[][] gameBoard) {

        char[] convertedGameBoard = new char[gameBoardKey.gameBoardHeight * gameBoardKey.gameBoardWidth];

        int index = 0;
        for (int i = 0; i < gameBoard.length; i++) {
            for (int j = 0; j < gameBoard[i].length; j++) {
                convertedGameBoard[index] = gameBoard[i][j];
                index++;
            }
        }

        return convertedGameBoard;
    }

    private boolean checkForWinner() {

        for (int i = 0; i < userGameBoard.getGameBoard().length; i++) {
            if (!gameBoardKey.getMineIndices().contains(i)) {
                if (userGameBoard.getGameBoard()[i] == '.') {
                    return false;
                }
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
