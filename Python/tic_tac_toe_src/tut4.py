# import random
# import tic_tac_toe
# from tic_tac_toe import GameState

# # Negamax implementation
# def negamax(game_state: GameState, player: int, depth: int = 0) -> int:
#     if game_state.is_game_over():
#         if game_state.winner != 0:
#             return game_state.winner * player  # Positive if current player won, negative if opponent won
#         else:
#             return 0  # Draw
    
#     best_score = float('-inf')
#     best_move = None
    
#     for move in game_state.legal_moves:
#         game_state.apply_move(move)
#         score = -negamax(game_state, -player, depth + 1)
#         game_state.undo_move(move)

#         if score > best_score:
#             best_score = score
#             best_move = move

#     return best_move if depth == 0 else best_score

# # Function to draw the board in a readable format
# def print_board(game_state: GameState):
#     board = game_state.board
#     symbols = {tic_tac_toe.PLAYER_1: 'X', tic_tac_toe.PLAYER_2: 'O', 0: '.'}

#     # Convert the board into symbols and format for display
#     for row in range(3):
#         row_symbols = [symbols[board[row * 3 + col]] for col in range(3)]
#         print(" | ".join(row_symbols))
#         if row < 2:
#             print("--+---+--")
#     print()  # Add an extra newline for spacing

# # Function to play a game between two strategies
# def play_game(player1_strategy, player2_strategy):
#     game_state = GameState()
#     num_moves = 0

#     while not game_state.is_game_over():
#         print(f"Move {num_moves + 1}: Player {game_state.current_player}")
        
#         if game_state.current_player == tic_tac_toe.PLAYER_1:
#             move_to_play = player1_strategy(game_state)
#         else:
#             move_to_play = player2_strategy(game_state)

#         game_state.apply_move(move_to_play)
        
#         # Print the current state of the board
#         print_board(game_state)
        
#         num_moves += 1

#     print(f"Game ended after {num_moves} moves.")
#     if game_state.winner == tic_tac_toe.PLAYER_1:
#         print("The winner was: Player 1 (X).")
#     elif game_state.winner == tic_tac_toe.PLAYER_2:
#         print("The winner was: Player 2 (O).")
#     else:
#         print("It's a draw!")

# # Strategy functions
# def random_strategy(game_state: GameState):
#     return random.choice(game_state.legal_moves)

# def negamax_strategy(game_state: GameState):
#     return negamax(game_state, game_state.current_player)

# if __name__ == "__main__":
#     print("1. Random vs Random")
#     play_game(random_strategy, random_strategy)
    
#     print("\n2. Negamax vs Negamax")
#     play_game(negamax_strategy, negamax_strategy)
    
#     print("\n3. Random vs Negamax")
#     play_game(random_strategy, negamax_strategy)




import numpy as np
import random
from tic_tac_toe import GameState, PLAYER_1, PLAYER_2

# Negamax algorithm with depth to favor faster draws
def negamax(game_state: GameState, player: int, depth: int = 0) -> int:
    # Check if the game is over
    if game_state.is_game_over():
        if game_state.winner == 0:
            return 0  # Draw
        else:
            return (game_state.winner * player)  # Positive if the current player won, negative if lost

    best_score = -np.inf
    best_move = None
    
    for move in game_state.legal_moves:
        game_state.apply_move(move)
        
        # Call Negamax recursively and consider depth (deeper draws should be avoided)
        score = -negamax(game_state, -player, depth + 1)
        game_state.undo_move(move)

        # Prioritize better scores, but also draw if no winning move is found
        if score > best_score:
            best_score = score
            best_move = move

    # At depth 0, return the best move; otherwise return the score
    return best_move if depth == 0 else best_score

# Function to play a game between two strategies
def play_game(player1_strategy, player2_strategy):
    game_state = GameState()

    while not game_state.is_game_over():
        if game_state.current_player == PLAYER_1:
            move_to_play = player1_strategy(game_state)
        else:
            move_to_play = player2_strategy(game_state)

        game_state.apply_move(move_to_play)

    return game_state.winner

# Strategy functions
def random_strategy(game_state: GameState):
    return random.choice(game_state.legal_moves)

def negamax_strategy(game_state: GameState):
    return negamax(game_state, game_state.current_player)
