import numpy as np
import matplotlib.pyplot as plt
import time
from tic_tac_toe import simulate_games

if __name__ == '__main__':
    NUM_GAMES = 1000  # Increased for demonstration

    # --- AI vs Random ---
    start_time = time.time()
    results_ai_vs_random = simulate_games(NUM_GAMES, 1, 0)
    total_time = time.time() - start_time

    x_wins = np.count_nonzero(results_ai_vs_random == 1)
    o_wins = np.count_nonzero(results_ai_vs_random == -1)
    ties = np.count_nonzero(results_ai_vs_random == 0)

    print(f"AI vs Random Results after {NUM_GAMES} games:")
    print({'X wins': x_wins, 'O wins': o_wins, 'Ties': ties})
    print(f"Total time: {total_time:.4f} seconds")
    print(f"Average game time: {total_time / NUM_GAMES:.6f} seconds")

    # --- AI vs AI ---
    start_time = time.time()
    results_ai_vs_ai = simulate_games(NUM_GAMES, 1, 1)
    total_time = time.time() - start_time

    x_wins_ai = np.count_nonzero(results_ai_vs_ai == 1)
    o_wins_ai = np.count_nonzero(results_ai_vs_ai == -1)
    ties_ai = np.count_nonzero(results_ai_vs_ai == 0)

    print(f"\nAI vs AI Results after {NUM_GAMES} games:")
    print({'X wins': x_wins_ai, 'O wins': o_wins_ai, 'Ties': ties_ai})
    print(f"Total time: {total_time:.4f} seconds")
    print(f"Average game time: {total_time / NUM_GAMES:.6f} seconds")

    # --- Visualizations ---
    plt.figure(figsize=(12, 5))

    # AI vs Random
    plt.subplot(1, 2, 1)
    labels = ['X wins', 'O wins', 'Ties']
    counts = [x_wins, o_wins, ties]
    plt.bar(labels, counts, color=['green', 'red', 'blue'])
    plt.title('AI vs Random Results')
    plt.xlabel('Outcome')
    plt.ylabel('Number of Games')

    # AI vs AI
    plt.subplot(1, 2, 2)
    labels_ai = ['X wins', 'O wins', 'Ties']
    counts_ai = [x_wins_ai, o_wins_ai, ties_ai]
    plt.bar(labels_ai, counts_ai, color=['green', 'red', 'blue'])
    plt.title('AI vs AI Results')
    plt.xlabel('Outcome')
    plt.ylabel('Number of Games')

    plt.tight_layout()
    plt.show()
