import numpy as np
from numba import njit, prange

@njit
def initialize_board():
    return np.zeros(9, dtype=np.int8)

@njit
def make_move(board, square, player):
    board[square] = player

@njit
def get_empty_squares(board):
    return np.where(board == 0)[0]

@njit
def check_winner(board, player):
    win_positions = np.array([
        [0, 1, 2],  # Rows
        [3, 4, 5],
        [6, 7, 8],
        [0, 3, 6],  # Columns
        [1, 4, 7],
        [2, 5, 8],
        [0, 4, 8],  # Diagonals
        [2, 4, 6]
    ], dtype=np.int8)
    
    for i in range(8):
        pos0 = win_positions[i, 0]
        pos1 = win_positions[i, 1]
        pos2 = win_positions[i, 2]
        if board[pos0] == player and board[pos1] == player and board[pos2] == player:
            return True
    return False

@njit
def negamax(board, player, alpha, beta):
    if check_winner(board, -player):
        return -1  # Opponent has won

    empty = get_empty_squares(board)
    if len(empty) == 0:
        return 0  # Tie

    max_score = -np.inf

    for idx in empty:
        board[idx] = player
        score = -negamax(board, -player, -beta, -alpha)
        board[idx] = 0

        if score > max_score:
            max_score = score

        if max_score > alpha:
            alpha = max_score

        if alpha >= beta:
            break  # Alpha-beta pruning

    return max_score

@njit
def ai_player_move(board, player):
    best_score = -np.inf
    best_move = -1
    empty = get_empty_squares(board)

    for idx in empty:
        board[idx] = player
        score = -negamax(board, -player, -np.inf, np.inf)
        board[idx] = 0

        if score > best_score:
            best_score = score
            best_move = idx

    return best_move

@njit
def random_player_move(board):
    empty = get_empty_squares(board)
    idx = np.random.randint(0, len(empty))
    return empty[idx]

@njit
def play_single_game(player1_type, player2_type):
    board = initialize_board()
    current_player = 1  # 'X' starts
    while True:
        if current_player == 1:
            if player1_type == 1:
                move = ai_player_move(board, current_player)
            else:
                move = random_player_move(board)
        else:
            if player2_type == 1:
                move = ai_player_move(board, current_player)
            else:
                move = random_player_move(board)

        make_move(board, move, current_player)

        if check_winner(board, current_player):
            return current_player
        elif np.all(board != 0):
            return 0  # Tie

        current_player *= -1  # Switch player

@njit(parallel=True)
def simulate_games(num_games, player1_type, player2_type):
    results = np.zeros(num_games, dtype=np.int8)
    for i in prange(num_games):
        results[i] = play_single_game(player1_type, player2_type)
    return results
