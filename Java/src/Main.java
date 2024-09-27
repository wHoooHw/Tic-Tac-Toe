// import java.util.List;
// import java.util.Scanner;
// import java.util.concurrent.ThreadLocalRandom;

// /**
//  * Main class. By default, plays a game where moves are selected uniformly
//  * at random. Students may modify this to implement AI-driven decision-making.
//  * 
//  * @author Dennis Soemers
//  */
// public class Main {
    
//     public static void main(final String[] args) {
//         final GameState gameState = new GameState();
//         final AI ai = new AI();
//         int numMoves = 0; 		
//         int moveToPlay;	
//         Scanner scanner = new Scanner(System.in);

//         System.out.println("Choose a combination to play:");
//         System.out.println("1. Random vs Random");
//         System.out.println("2. Negamax AI vs Negamax AI");
//         System.out.println("3. Negamax AI vs Random");
//         int choice = scanner.nextInt();
// 		scanner.close();

//         switch (choice) {
//             case 1:
//                 // MARK: Random vs Random
//                 while (!gameState.isGameOver()) {
//                     final List<Integer> legalMoves = gameState.getLegalMoves();
//                     moveToPlay = legalMoves.get(ThreadLocalRandom.current().nextInt(legalMoves.size())).intValue();
//                     gameState.applyMove(moveToPlay);
//                     ++numMoves;
// 					ai.printBoard(gameState);
//                 }
//                 break;
//             case 2:
//                 // MARK: Negamax AI vs Negamax AI
//                 while (!gameState.isGameOver()) {            
//                     if (gameState.getCurrentPlayer() == GameState.PLAYER_1) {                
//                         moveToPlay = ai.negamax(gameState, GameState.PLAYER_1, 0);
//                     } else {                
//                         moveToPlay = ai.negamax(gameState, GameState.PLAYER_2, 0);
//                     }
//                     gameState.applyMove(moveToPlay);
//                     ++numMoves;
//                     ai.printBoard(gameState);
//                 }
//                 break;
//             case 3:
//                 // MARK: Negamax AI vs Random
//                 while (!gameState.isGameOver()) {			
//                     final List<Integer> legalMoves = gameState.getLegalMoves();

//                     if (gameState.getCurrentPlayer() == GameState.PLAYER_1) {                
//                         moveToPlay = ai.negamax(gameState, GameState.PLAYER_1, 0);
//                     } else {                
//                         moveToPlay = legalMoves.get(ThreadLocalRandom.current().nextInt(legalMoves.size())).intValue();
//                     }
//                     gameState.applyMove(moveToPlay);
//                     ++numMoves;
//                     ai.printBoard(gameState);
//                 }
//                 break;
//             default:
//                 System.out.println("Invalid choice. Exiting.");
//                 return;
//         }

//         // MARK: Prints results
//         System.out.println("Game ended after " + numMoves + " moves.");
//         if (gameState.getWinner() == GameState.PLAYER_1)
//             System.out.println("The winner was: Player1.");
//         else if (gameState.getWinner() == GameState.PLAYER_2)
//             System.out.println("The winner was: Player2.");
//         else
//             System.out.println("It's a draw");
//     }
// }







import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import java.util.*;
import java.util.concurrent.*;

public class Main extends ApplicationFrame {

    public Main(String title) {
        super(title);
    }

    public static void main(String[] args) {
        AI ai = new AI();
        final int numGames = 1000;
        final int numThreads = Runtime.getRuntime().availableProcessors();  // Use all available CPU cores

        // First: Run AI vs Random simulations with timing
        System.out.println("Running AI vs Random simulations...");
        long startTime = System.nanoTime();
        runSimulations(numGames, ai, 1, "AI vs Random Results", numThreads);
        long endTime = System.nanoTime();
        double totalTimeAIvsRandom = (endTime - startTime) / 1e9;
        System.out.printf("AI vs Random took %.4f seconds\n", totalTimeAIvsRandom);

        // Second: Run AI vs AI simulations with timing
        System.out.println("Running AI vs AI simulations...");
        startTime = System.nanoTime();
        runSimulations(numGames, ai, 2, "AI vs AI Results", numThreads);
        endTime = System.nanoTime();
        double totalTimeAIvsAI = (endTime - startTime) / 1e9;
        System.out.printf("AI vs AI took %.4f seconds\n", totalTimeAIvsAI);
    }

    /**
     * Runs simulations of Tic-Tac-Toe games in parallel and displays the results in a chart.
     */
    public static void runSimulations(int numGames, AI ai, int mode, String chartTitle, int numThreads) {
        int player1Wins = 0;
        int player2Wins = 0;
        int draws = 0;

        // Set up a thread pool to run simulations in parallel
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        List<Future<Integer>> results;

        // Submit game simulations as tasks to be executed in parallel
        try {
            List<Callable<Integer>> tasks = new ArrayList<>();
            for (int i = 0; i < numGames; i++) {
                tasks.add(() -> playSingleGame(ai, new GameState(), mode));
            }
            results = executor.invokeAll(tasks);  // Run all tasks in parallel

            // Process results from each game
            for (Future<Integer> result : results) {
                int gameResult = result.get();  // Get the result of the game
                if (gameResult == GameState.PLAYER_1) {
                    player1Wins++;
                } else if (gameResult == GameState.PLAYER_2) {
                    player2Wins++;
                } else {
                    draws++;
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }

        // Print Results
        System.out.println("\nResults after " + numGames + " games (" + chartTitle + "):");
        System.out.println("Player 1 (AI) Wins: " + player1Wins);
        System.out.println("Player 2 Wins: " + player2Wins);
        System.out.println("Draws: " + draws);

        // Create bar chart to visualize results
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(player1Wins, "Wins", "Player 1 (AI)");
        dataset.addValue(player2Wins, "Wins", "Player 2");
        dataset.addValue(draws, "Draws", "Draws");

        JFreeChart barChart = ChartFactory.createBarChart(
                chartTitle,         // Chart title
                "Outcome",          // Domain axis label
                "Number of Wins",   // Range axis label
                dataset,            // Data
                PlotOrientation.VERTICAL,
                true,               // Include legend
                true,               // Tooltips
                false               // URLs
        );

        // Display chart in a window
        ChartPanel chartPanel = new ChartPanel(barChart);
        chartPanel.setPreferredSize(new java.awt.Dimension(800, 600));
        ApplicationFrame chartWindow = new ApplicationFrame(chartTitle);
        chartWindow.setContentPane(chartPanel);
        chartWindow.pack();
        RefineryUtilities.centerFrameOnScreen(chartWindow);
        chartWindow.setVisible(true);
    }

    /**
     * Plays a single game of Tic-Tac-Toe.
     */
    public static int playSingleGame(AI ai, GameState gameState, int mode) {
        int moveToPlay;

        // Keep playing until the game is over
        while (!gameState.isGameOver()) {
            List<Integer> legalMoves = gameState.getLegalMoves();
            if (gameState.getCurrentPlayer() == GameState.PLAYER_1) {
                moveToPlay = ai.negamax(gameState, GameState.PLAYER_1, 0);
            } else {
                if (mode == 1) { // Random player for mode 1 (AI vs Random)
                    moveToPlay = legalMoves.get(ThreadLocalRandom.current().nextInt(legalMoves.size()));
                } else { // AI vs AI for mode 2
                    moveToPlay = ai.negamax(gameState, GameState.PLAYER_2, 0);
                }
            }
            gameState.applyMove(moveToPlay);
        }
        return gameState.getWinner();  // Returns winner or 0 for a draw
    }
}
