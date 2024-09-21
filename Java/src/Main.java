import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Main class. By default, plays a game where moves are selected uniformly
 * at random. Students may modify this to implement AI-driven decision-making.
 * 
 * @author Dennis Soemers
 */
public class Main {
	
	public static void main(final String[] args) {
		final GameState gameState = new GameState();
		
		int numMoves = 0;
		while (!gameState.isGameOver()) {
			final List<Integer> legalMoves = gameState.getLegalMoves();
			final int moveToPlay = legalMoves.get(
					ThreadLocalRandom.current().nextInt(legalMoves.size())).intValue();
			gameState.applyMove(moveToPlay);
			++numMoves;
		}
		
		System.out.println("Game ended after " + numMoves + " moves.");
		if (gameState.getWinner() == GameState.PLAYER_1)
			System.out.println("The winner was: Player 1.");
		else if (gameState.getWinner() == GameState.PLAYER_2)
			System.out.println("The winner was: Player 2.");
		else
			System.out.println("The winner was: noone.");
	}

}
