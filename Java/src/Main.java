import java.util.List;
import java.util.Scanner;
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
        final AI ai = new AI();
        int numMoves = 0; 		
        int moveToPlay;	
        Scanner scanner = new Scanner(System.in);

        System.out.println("Choose a combination to play:");
        System.out.println("1. Random vs Random");
        System.out.println("2. Negamax AI vs Negamax AI");
        System.out.println("3. Negamax AI vs Random");
        int choice = scanner.nextInt();
		scanner.close();

        switch (choice) {
            case 1:
                // MARK: Random vs Random
                while (!gameState.isGameOver()) {
                    final List<Integer> legalMoves = gameState.getLegalMoves();
                    moveToPlay = legalMoves.get(ThreadLocalRandom.current().nextInt(legalMoves.size())).intValue();
                    gameState.applyMove(moveToPlay);
                    ++numMoves;
					ai.printBoard(gameState);
                }
                break;
            case 2:
                // MARK: Negamax AI vs Negamax AI
                while (!gameState.isGameOver()) {            
                    if (gameState.getCurrentPlayer() == GameState.PLAYER_1) {                
                        moveToPlay = ai.negamax(gameState, GameState.PLAYER_1, 0);
                    } else {                
                        moveToPlay = ai.negamax(gameState, GameState.PLAYER_2, 0);
                    }
                    gameState.applyMove(moveToPlay);
                    ++numMoves;
                    ai.printBoard(gameState);
                }
                break;
            case 3:
                // MARK: Negamax AI vs Random
                while (!gameState.isGameOver()) {			
                    final List<Integer> legalMoves = gameState.getLegalMoves();

                    if (gameState.getCurrentPlayer() == GameState.PLAYER_1) {                
                        moveToPlay = ai.negamax(gameState, GameState.PLAYER_1, 0);
                    } else {                
                        moveToPlay = legalMoves.get(ThreadLocalRandom.current().nextInt(legalMoves.size())).intValue();
                    }
                    gameState.applyMove(moveToPlay);
                    ++numMoves;
                    ai.printBoard(gameState);
                }
                break;
            default:
                System.out.println("Invalid choice. Exiting.");
                return;
        }

        // AMRK: Prints results
        System.out.println("Game ended after " + numMoves + " moves.");
        if (gameState.getWinner() == GameState.PLAYER_1)
            System.out.println("The winner was: MR_Negamax_AI.");
        else if (gameState.getWinner() == GameState.PLAYER_2)
            System.out.println("The winner was: MR_Random.");
        else
            System.out.println("It's a draw");
    }
}