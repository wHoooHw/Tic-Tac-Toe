import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A game state for the game of Tic-Tac-Toe.
 * 
 * @author Dennis Soemers
 */
public class GameState {
	
	public static final int PLAYER_1 = 1;
	public static final int PLAYER_2 = -1;
	
	private static final int NUM_ROWS = 3;
	private static final int NUM_COLS = 3;
	
	// Indexed into by a single 0-based index. 
	// Gives all lines we would have to check for potential wins from that cell outwards.
	private static final int[][][] LINES_TO_CHECK;
	
	// The game state variables:
	private final int[] board;
	private final List<Integer> legalMoves;
	private int currentPlayer;
	private int winner;
	
	
	/**
	 * Creates a new initial game state (with empty board).
	 */
	public GameState() {
		board = new int[NUM_ROWS * NUM_COLS];
		
		legalMoves = new ArrayList<Integer>(9);
		for (int i = 0; i < 9; ++i) {
			legalMoves.add(Integer.valueOf(i));
		}
		
		currentPlayer = PLAYER_1;
		winner = 0;
	}
	
	/**
	 * @return An unmodifiable view of the list of legal moves in current game state.
	 */
	public List<Integer> getLegalMoves() {
		return Collections.unmodifiableList(legalMoves);
	}
	
	/**
	 * Modifies the game state by applying the given move.
	 * 
	 * WARNING: for the sake of efficiency, does not implement any checks to avoid
	 * weird stuff happening if this is called on a game state that is already terminal.
	 * 
	 * It also does not check whether given moves are actually legal.
	 * 
	 * @param move A 0-based index representing where we want to place our piece.
	 */
	public void applyMove(final int move) {
		board[move] = currentPlayer;
		Utils.removeSwap(legalMoves, legalMoves.indexOf(Integer.valueOf(move)));
		
		// Check if the mover won
		final int[][] linesToCheck = LINES_TO_CHECK[move];
		for (final int[] line : linesToCheck) {
			boolean win = true;
			
			for (final int cell : line) {
				if (board[cell] != currentPlayer) {
					win = false;
					break;
				}
			}
			
			if (win) {
				winner = currentPlayer;
				break;
			}
		}
		
		// Swap players
		currentPlayer *= -1;
	}
	
	/**
	 * Reverts game state back to the one we were in before given move was played.
	 * 
	 * WARNING: for the sake of efficiency, does not implement any checks to avoid
	 * weird stuff happening if this is called on the initial game state.
	 * 
	 * It also does not check whether the given move was actually played last.
	 * 
	 * @param move The move to be undone.
	 */
	public void undoMove(final int move) {
		board[move] = 0;
		legalMoves.add(Integer.valueOf(move));
		winner = 0;
		currentPlayer *= -1;
	}
	
	/**
	 * @return Did the game end?
	 */
	public boolean isGameOver() {
		return winner != 0 || legalMoves.isEmpty();
	}
	
	/**
	 * @return Which player won? Returns 0 if the game is not yet over,
	 *  but also if it ended in a draw.
	 */
	public int getWinner() {
		return winner;
	}
	
	/**
	 * @param index A 0-based index.
	 * @return [row, column] corresponding to the given index.
	 */
	public static int[] indexToRowCol(final int index) {
		return new int[] {index / NUM_COLS, index % NUM_COLS};
	}
	
	/**
	 * @param rowCol [row, column]
	 * @return A 0-based index corresponding to the given row and column.
	 */
	public static int rowColToIndex(final int[] rowCol) {
		return (rowCol[0] * NUM_COLS) + rowCol[1];
	}
	
	// Pre-compute the lines we have to search along for potential wins for any move
	static {
		LINES_TO_CHECK = new int[NUM_COLS * NUM_ROWS][][];

		for (int i = 0; i < LINES_TO_CHECK.length; ++i) {
			// Pre-computing data for case where `i` is the move that was just played
			final int[] rowCol = indexToRowCol(i);
			final int row = rowCol[0];
			final int col = rowCol[1];

			// Always need to check at least 2 lines (horizontal and vertical)
			int numLinesToCheck = 2;

			if (row == 0 && col != 1)
				++numLinesToCheck;		// a row-0 corner
			else if (row == 2 && col != 1)
				++numLinesToCheck;		// a row-2 corner
			else if (row == 1 && col == 1)
				numLinesToCheck += 2;	// the centre
			
			LINES_TO_CHECK[i] = new int[numLinesToCheck][2];

			// A horizontal line
			int nextIdx = 0;
			if (col != 0)
				LINES_TO_CHECK[i][0][nextIdx++] = rowColToIndex(new int[] {row, 0});

			if (col != 1)
				LINES_TO_CHECK[i][0][nextIdx++] = rowColToIndex(new int[] {row, 1});

			if (col != 2)
				LINES_TO_CHECK[i][0][nextIdx++] = rowColToIndex(new int[] {row, 2});

			// A vertical line
			nextIdx = 0;
			if (row != 0)
				LINES_TO_CHECK[i][1][nextIdx++] = rowColToIndex(new int[] {0, col});

			if (row != 1)
				LINES_TO_CHECK[i][1][nextIdx++] = rowColToIndex(new int[] {1, col});

			if (row != 2)
				LINES_TO_CHECK[i][1][nextIdx++] = rowColToIndex(new int[] {2, col});
			
			// See if any diagonal lines are necessary
			if (col != 1) {
				if (row == 0) {
					// Need 1 diagonal
					if (col == 0) {
						LINES_TO_CHECK[i][2][0] = rowColToIndex(new int[] {1, 1});
						LINES_TO_CHECK[i][2][1] = rowColToIndex(new int[] {2, 2});
					}
					else if (col == 2) {
						LINES_TO_CHECK[i][2][0] = rowColToIndex(new int[] {1, 1});
						LINES_TO_CHECK[i][2][1] = rowColToIndex(new int[] {2, 0});
					}
				}
				else if (row == 2) {
					// Need 1 diagonal
					if (col == 0) {
						LINES_TO_CHECK[i][2][0] = rowColToIndex(new int[] {1, 1});
						LINES_TO_CHECK[i][2][1] = rowColToIndex(new int[] {0, 2});
					}
					else if (col == 2) {
						LINES_TO_CHECK[i][2][0] = rowColToIndex(new int[] {0, 0});
						LINES_TO_CHECK[i][2][1] = rowColToIndex(new int[] {1, 1});
					}
				}
			}
			else if (row == 1) {
				// Centre, so need to do both diagonals
				LINES_TO_CHECK[i][2][0] = rowColToIndex(new int[] {0, 0});
				LINES_TO_CHECK[i][2][1] = rowColToIndex(new int[] {2, 2});
				
				LINES_TO_CHECK[i][3][0] = rowColToIndex(new int[] {0, 2});
				LINES_TO_CHECK[i][3][1] = rowColToIndex(new int[] {2, 0});
			}
		}
	}

}
