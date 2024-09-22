#!/usr/bin/env python

"""
Runnable script. By default, plays a game where moves are selected uniformly
at random. Students may modify this to implement AI-driven decision-making.
"""

import random
import tic_tac_toe

from tic_tac_toe import GameState

if __name__=="__main__":
    game_state = GameState()

    num_moves = 0
    while not game_state.is_game_over():
        legal_moves = game_state.legal_moves
        move_to_play = random.choice(legal_moves)
        game_state.apply_move(move_to_play)
        num_moves += 1

    print(f"Game ended after {num_moves} moves.")
    if game_state.winner == tic_tac_toe.PLAYER_1:
        print("The winner was: Player 1.")
    elif game_state.winner == tic_tac_toe.PLAYER_2:
        print("The winner was: Player 2.")
    else:
        print("The winner was: noone.")
