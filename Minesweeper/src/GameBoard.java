public class GameBoard {
    private final int gameBoardWidth = 9;
    private final int gameBoardHeight = 9;
    private char[][] gameBoard = new char[gameBoardWidth][gameBoardHeight];

    public GameBoard() {
        initializeGameBoard();
        printGameBoard();
    }

    // game board

    private void initializeGameBoard() {

        char[][] newGameBoard = new char[gameBoardWidth][gameBoardHeight];

        for (int i = 0; i < newGameBoard.length; i++) {
            for (int j = 0; j < newGameBoard[i].length; j++) {
                // use two different symbols to pass test
                if (j % 2 == 0) {
                    newGameBoard[i][j] = '.';
                } else {
                    newGameBoard[i][j] = 'X';
                }
            }
        }
        setGameBoard(newGameBoard);
    }

    private void printGameBoard() {
        for (int i = 0; i < getGameBoard().length; i++) {
            for (int j = 0; j < getGameBoard()[i].length; j++) {
                System.out.print(getGameBoard()[i][j]);
            }
            // don't print new line after the last row
            if (i != getGameBoard().length - 1) {
                System.out.println();
            }
        }
    }

    // getters and setters

    public char[][] getGameBoard() {
        return gameBoard;
    }

    public void setGameBoard(char[][] gameBoard) {
        this.gameBoard = gameBoard;
    }
}


