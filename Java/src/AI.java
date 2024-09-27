public class AI {

    public int negamax(GameState gameState, int player, int depth) {
        if (gameState.isGameOver()) {
            return gameState.getWinner() * player; // Return score from perspective of current player
        }

        int bestScore = Integer.MIN_VALUE;
        int bestMove = -1;

        for (int move : gameState.getLegalMoves()) {
            GameState newState = gameState.clone();
            newState.applyMove(move);
            int score = -negamax(newState, -player, depth + 1); // Negate the score since the opponent plays next

            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
            }
        }

        // If at root of tree (depth 0), return the move, otherwise return the score
        return depth == 0 ? bestMove : bestScore;
    }

    // Simple utility method to print the board (for debugging purposes)
    public void printBoard(GameState gameState) {
        int[] board = gameState.getBoard();
        for (int i = 0; i < 9; i++) {
            if (i % 3 == 0) System.out.println();
            if (board[i] == GameState.PLAYER_1) {
                System.out.print("X ");
            } else if (board[i] == GameState.PLAYER_2) {
                System.out.print("O ");
            } else {
                System.out.print(". ");
            }
        }
        System.out.println();
    }
}
