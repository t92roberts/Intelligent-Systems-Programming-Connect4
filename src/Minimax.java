import java.util.ArrayList;

/**
 * Created by Tom on 15/03/2017.
 */
public class Minimax {
    private int maxDepth;

    public int[] minimax(int[][] initialState, int depth, int playerID) {
        maxDepth = depth;

        int[] initialAlpha = new int[]{MinimaxUtilties.freeCentralColumn(initialState), Integer.MIN_VALUE, 0}; // initialise with the central-most empty column and a utility
        int[] initialBeta = new int[]{MinimaxUtilties.freeCentralColumn(initialState), Integer.MAX_VALUE, 0};

        if (playerID == 1) {
            // returns [game board column number, utility of choosing that column)
            int[] move = maxValue(initialState, initialAlpha, initialBeta, depth);

            if (move[1] == 0) { // minimax found no advantage to either player
                return new int[]{MinimaxUtilties.freeCentralColumn(initialState), 0};
            }
            else { // minimax found a favourable move
                return move; // return column number
            }
        } else if (playerID == 2) {
            int[] move = minValue(initialState, initialAlpha, initialBeta, depth);

            if (move[1] == 0) {
                return new int[]{MinimaxUtilties.freeCentralColumn(initialState), 0};
            }
            else {
                return move;
            }
        }

        return new int[]{MinimaxUtilties.freeCentralColumn(initialState), 0}; // return the first free column as a fail-safe
    }

    private int[] maxValue(int[][] currentState, int[] alpha, int[] beta, int depth) {
        // initialise the 'best move' as the central-most free column, and a highly negative utility
        int[] bestMove = new int[]{MinimaxUtilties.freeCentralColumn(currentState), Integer.MIN_VALUE, 0};

        if (terminalTest(currentState) != 0) {
            return new int[]{0, utility(currentState), maxDepth - depth};
        }

        if (depth < 1){
            return new int[]{0, utility(currentState), maxDepth};
        }

        if (terminalTest(currentState) == 0 && depth > 0) {
            // find all possible actions for this player from the current game state
            ArrayList<Integer> possibleActions = actions(currentState, 1);

            // check if each possible action is better than the best action so far
            for (int i = 0; i < possibleActions.size(); i++) {
                int column = possibleActions.get(i);
                int[][] resultOfAction = result(currentState, column, 1);

                // returns [column that was chosen, utility of choosing it]
                int[] currentResult = new int[]{column, minValue(resultOfAction, alpha, beta, depth - 1)[1], maxDepth - depth};

                // found a new best action
                // new move is better, or equal to best move but has shallower depth
                if (currentResult[1] > bestMove[1] || (currentResult[1] == bestMove[1] && currentResult[2] < bestMove[2])) {
                    bestMove = currentResult;
                }

                if (bestMove[1] > alpha[1] || (bestMove[1] == alpha[1] && bestMove[2] < alpha[2])) alpha = bestMove; // new best action is better than the current alpha action

                if (alpha[1] >= beta[1]) return alpha; // beta cut-off
            }
        }

        return bestMove;
    }

    private int[] minValue(int[][] currentState, int[] alpha, int[] beta, int depth) {
        // initialise the 'best move' as the central-most free column, and a highly positive utility
        int[] bestMove = new int[]{MinimaxUtilties.freeCentralColumn(currentState), Integer.MAX_VALUE, 0};

        if (terminalTest(currentState) != 0) {
            return new int[]{0, utility(currentState), maxDepth - depth};
        }

        if (depth < 1){
            return new int[]{0, utility(currentState), maxDepth};
        }

        if (terminalTest(currentState) == 0 && depth > 0) {
            ArrayList<Integer> possibleActions = actions(currentState, 2);

            for (int i = 0; i < possibleActions.size(); i++) {
                int column = possibleActions.get(i);
                int[][] resultOfAction = result(currentState, column, 2);

                int[] currentResult = new int[]{column, maxValue(resultOfAction, alpha, beta, depth - 1)[1], maxDepth - depth};

                if (currentResult[1] < bestMove[1] || (currentResult[1] == bestMove[1] && currentResult[2] < bestMove[2])) {
                    bestMove = currentResult;
                }

                if (bestMove[1] < beta[1] || (bestMove[1] == beta[1] && bestMove[2] < beta[2])) beta = bestMove;

                if (alpha[1] >= beta[1]) return beta;
            }
        }

        return bestMove;
    }

    private ArrayList<Integer> actions(int[][] gameBoard, int player) {
        ArrayList<Integer> actions = new ArrayList<>();

        for (int col = 0; col < gameBoard.length; col++) {
            int emptyRow = MinimaxUtilties.emptyRow(gameBoard[col]);

            if (emptyRow >= 0) {
                actions.add(col);
            }
        }

        return actions;
    }

    private int[][] result(int[][] gameBoard, int col, int player) {
        int[][] newBoard = MinimaxUtilties.copyBoard(gameBoard);

        int emptyRow = MinimaxUtilties.emptyRow(newBoard[col]);

        if (emptyRow >= 0) {
            newBoard[col][emptyRow] = player;
        }

        return newBoard;
    }

    private int utility(int[][] gameBoard) {
        int chain3Weight = 100;
        int chain2Weight = 10;

        // check if a player has won
        if (terminalTest(gameBoard) == 1) return 1000000; // player 1 has won
        if (terminalTest(gameBoard) == 2) return -1000000; // player 2 has won

        // player 1's utility
        int chain3Score = MinimaxUtilties.chain3Count(gameBoard, 1) * chain3Weight;
        int chain2Score = MinimaxUtilties.chain2Count(gameBoard, 1) * chain2Weight;
        int player1Score = chain3Score + chain2Score;

        // player 2's utility
        chain3Score = MinimaxUtilties.chain3Count(gameBoard, 2) * chain3Weight;
        chain2Score = MinimaxUtilties.chain2Count(gameBoard, 2) * chain2Weight;
        int player2Score = -1 * (chain3Score + chain2Score);

        return player1Score + player2Score;
    }

    public int terminalTest(int[][] gameBoard) {
        // check for vertical win (searching downwards)
        for (int col = 0; col < gameBoard.length; col++) {
            for (int row = 0; row < gameBoard[col].length - 3; row++) {
                if (gameBoard[col][row] == 1 &&
                        gameBoard[col][row + 1] == 1 &&
                        gameBoard[col][row + 2] == 1 &&
                        gameBoard[col][row + 3] == 1) {
                    return 1;
                } else if (gameBoard[col][row] == 2 &&
                        gameBoard[col][row + 1] == 2 &&
                        gameBoard[col][row + 2] == 2 &&
                        gameBoard[col][row + 3] == 2) {
                    return 2;
                }
            }
        }

        // check for horizontal win (searching to the right)
        for (int row = 0; row < gameBoard[0].length; row++) {
            for (int col = 0; col < gameBoard.length - 3; col++) {
                if (gameBoard[col][row] == 1 &&
                        gameBoard[col + 1][row] == 1 &&
                        gameBoard[col + 2][row] == 1 &&
                        gameBoard[col + 3][row] == 1) {
                    return 1;
                } else if (gameBoard[col][row] == 2 &&
                        gameBoard[col + 1][row] == 2 &&
                        gameBoard[col + 2][row] == 2 &&
                        gameBoard[col + 3][row] == 2) {
                    return 2;
                }
            }
        }

        // check for diagonal win (searching down + right)
        for (int col = 0; col < gameBoard.length - 3; col++) {
            for (int row = 0; row < gameBoard[col].length - 3; row++) {
                if (gameBoard[col][row] == 1 &&
                        gameBoard[col + 1][row + 1] == 1 &&
                        gameBoard[col + 2][row + 2] == 1 &&
                        gameBoard[col + 3][row + 3] == 1) {
                    return 1;
                } else if (gameBoard[col][row] == 2 &&
                        gameBoard[col + 1][row + 1] == 2 &&
                        gameBoard[col + 2][row + 2] == 2 &&
                        gameBoard[col + 3][row + 3] == 2) {
                    return 2;
                }
            }
        }

        // check for diagonal win (searching down + left)
        for (int col = gameBoard.length - 1; col >= 3; col--) {
            for (int row = 0; row < gameBoard[col].length - 3; row++) {
                if (gameBoard[col][row] == 1 &&
                        gameBoard[col - 1][row + 1] == 1 &&
                        gameBoard[col - 2][row + 2] == 1 &&
                        gameBoard[col - 3][row + 3] == 1) {
                    return 1;
                } else if (gameBoard[col][row] == 2 &&
                        gameBoard[col - 1][row + 1] == 2 &&
                        gameBoard[col - 2][row + 2] == 2 &&
                        gameBoard[col - 3][row + 3] == 2) {
                    return 2;
                }
            }
        }

        return 0; // neither player has won
    }
}