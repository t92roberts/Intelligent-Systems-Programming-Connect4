/**
 * Created by Tom on 15/03/2017.
 */
public class MinimaxUtilties {

    static int firstNonFullColumn(int[][] gameBoard) {
        for (int col = 0; col < gameBoard.length; col++) {
            if (emptyRow(gameBoard[col]) > -1) {
                return col;
            }
        }

        return -1; // only reached if there are no empty columns
    }

    static int freeCentralColumn(int[][] gameBoard) {
        int boardCentre;

        if (gameBoard.length % 2 == 0) { // even number of columns
            boardCentre = gameBoard.length / 2;
        } else { // odd number of columns
            boardCentre = (gameBoard.length - 1) / 2;
        }

        for (int offset = 0; offset < boardCentre; offset++) {
            int leftCol = emptyRow(gameBoard[boardCentre - offset]);
            int rightCol = emptyRow(gameBoard[boardCentre + offset]);

            if (leftCol > -1) return boardCentre - offset;
            else if (rightCol > -1) return boardCentre + offset;
        }

        return firstNonFullColumn(gameBoard);
    }

    static int emptyRow(int[] column) {
        for (int row = column.length - 1; row >= 0; row--) {  // check from the bottom of the column upwards
            if (column[row] == 0) {
                return row;
            }
        }

        return -1; // only reached if there are no empty rows
    }

    static int[][] copyBoard(int[][] originalBoard) {
        int[][] newBoard = new int[originalBoard.length][];

        for (int i = 0; i < originalBoard.length; i++) {
            newBoard[i] = new int[originalBoard[i].length];

            for (int j = 0; j < originalBoard[i].length; j++) {
                newBoard[i][j] = originalBoard[i][j];
            }
        }

        return newBoard;
    }

    static int chain3Count(int[][] gameBoard, int playerID) {
        int chain3Count = 0;

        // check for vertical chain (searching downwards)
        for (int col = 0; col < gameBoard.length; col++) {
            for (int row = 0; row < gameBoard[col].length - 2; row++) {
                if (gameBoard[col][row] == playerID &&
                        gameBoard[col][row + 1] == playerID &&
                        gameBoard[col][row + 2] == playerID) {
                    chain3Count++;
                }
            }
        }

        // check for horizontal chain (searching to the right)
        for (int row = 0; row < gameBoard[0].length; row++) {
            for (int col = 0; col < gameBoard.length - 2; col++) {
                if (gameBoard[col][row] == playerID &&
                        gameBoard[col + 1][row] == playerID &&
                        gameBoard[col + 2][row] == playerID) {
                    chain3Count++;
                }
            }
        }

        // check for diagonal chain (searching down + right)
        for (int col = 0; col < gameBoard.length - 2; col++) {
            for (int row = 0; row < gameBoard[col].length - 2; row++) {
                if (gameBoard[col][row] == playerID &&
                        gameBoard[col + 1][row + 1] == playerID &&
                        gameBoard[col + 2][row + 2] == playerID) {
                    chain3Count++;
                }
            }
        }

        // check for diagonal chain (searching up + right)
        for (int col = 0; col < gameBoard.length - 2; col++) {
            for (int row = 3; row < gameBoard[col].length; row++) {
                if (gameBoard[col][row] == playerID &&
                        gameBoard[col + 1][row - 1] == playerID &&
                        gameBoard[col + 2][row - 2] == playerID) {
                    chain3Count++;
                }
            }
        }

        return chain3Count;
    }

    static int chain2Count(int[][] gameBoard, int playerID) {
        int chain2Count = 0;

        // check for vertical chain (searching downwards)
        for (int col = 0; col < gameBoard.length; col++) {
            for (int row = 0; row < gameBoard[col].length - 1; row++) {
                if (gameBoard[col][row] == playerID && gameBoard[col][row + 1] == playerID) {
                    chain2Count++;
                }
            }
        }

        // check for horizontal chain (searching to the right)
        for (int row = 0; row < gameBoard[0].length; row++) {
            for (int col = 0; col < gameBoard.length - 1; col++) {
                if (gameBoard[col][row] == playerID && gameBoard[col + 1][row] == playerID) {
                    chain2Count++;
                }
            }
        }

        // check for diagonal chain (searching down + right)
        for (int col = 0; col < gameBoard.length - 1; col++) {
            for (int row = 0; row < gameBoard[col].length - 1; row++) {
                if (gameBoard[col][row] == playerID && gameBoard[col + 1][row + 1] == playerID) {
                    chain2Count++;
                }
            }
        }

        // check for diagonal chain (searching up + right)
        for (int col = 0; col < gameBoard.length - 1; col++) {
            for (int row = 3; row < gameBoard[col].length; row++) {
                if (gameBoard[col][row] == playerID && gameBoard[col + 1][row - 1] == playerID) {
                    chain2Count++;
                }
            }
        }

        return chain2Count;
    }
}
