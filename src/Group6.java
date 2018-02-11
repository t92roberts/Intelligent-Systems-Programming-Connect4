public class Group6 implements IGameLogic {
    private int x = 0;
    private int y = 0;
    private int playerID;
    private boolean hasMoved = false;

    public Group6() {
        //TODO Write your implementation for this method
    }

    public void initializeGame(int x, int y, int playerID) {
        this.x = x;
        this.y = y;
        this.playerID = playerID;
        //TODO Write your implementation for this method
    }

    public Winner gameFinished() {
        int firstEmptyCol = MinimaxUtilties.firstNonFullColumn(gameBoard); // check if there is a column open
        if (firstEmptyCol == -1) return Winner.TIE; // no columns available
        else {
            Minimax mm = new Minimax();
            int terminalTest = mm.terminalTest(gameBoard);

            if (terminalTest == 1) return Winner.PLAYER1;
            else if (terminalTest == 2) return Winner.PLAYER2;
            else return Winner.NOT_FINISHED;
        }
    }


    public void insertCoin(int column, int player) {

    }

    public int decideNextMove() {
        String colour;

        if (playerID == 1) colour = "Blue";
        else colour = "Red";

        System.out.println(colour + " is thinking...");

        Minimax mm = new Minimax();

        int[] move = mm.minimax(FourConnectGUI.get, 8, playerID);

        if (move[1] == 0) {
            System.out.println("No player advantage, placing in central column\n");
        } else {
            System.out.println(colour + " chose - Column: " + move[0] + ", Utility: " + move[1] + ", Depth: " + move[2] + "\n");
        }

        return move[0];
    }
}