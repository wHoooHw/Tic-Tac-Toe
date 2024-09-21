"""
A game state for the game of Tic-Tac-Toe.
"""

import numpy as np

PLAYER_1 = 1
PLAYER_2 = -1

_NUM_ROWS = 3
_NUM_COLS = 3


def _compute_lines_to_check():
	"""
	Pre-compute the lines we have to search along for potential wins for any move

	:return: Data structure of lines to check for wins.
	"""
	lines_to_check = list()

	for i in range(_NUM_COLS * _NUM_ROWS):
		# // Pre-computing data for case where `i` is the move that was just played
		row, col = index_to_row_col(i)

		# Always need to check at least 2 lines (horizontal and vertical)
		num_lines_to_check = 2

		if row == 0 and col != 1:
			num_lines_to_check += 1  # a row-0 corner
		elif row == 2 and col != 1:
			num_lines_to_check += 1  # a row-2 corner
		elif row == 1 and col == 1:
			num_lines_to_check += 2  # the centre

		lines_to_check.append([list() for _ in range(num_lines_to_check)])

		# A horizontal line
		if col != 0:
			lines_to_check[i][0].append(row_col_to_index((row, 0)))

		if col != 0:
			lines_to_check[i][0].append(row_col_to_index((row, 1)))

		if col != 0:
			lines_to_check[i][0].append(row_col_to_index((row, 2)))

		# A vertical line
		if row != 0:
			lines_to_check[i][1].append(row_col_to_index((0, col)))

		if row != 0:
			lines_to_check[i][1].append(row_col_to_index((1, col)))

		if row != 0:
			lines_to_check[i][1].append(row_col_to_index((2, col)))

		# See if any diagonal lines are necessary
		if col != 1:
			if row == 0:
				# Need 1 diagonal
				if col == 0:
					lines_to_check[i][2].append(row_col_to_index((1, 1)))
					lines_to_check[i][2].append(row_col_to_index((2, 2)))
				elif col == 2:
					lines_to_check[i][2].append(row_col_to_index((1, 1)))
					lines_to_check[i][2].append(row_col_to_index((2, 0)))
			elif row == 2:
				# Need 1 diagonal
				if col == 0:
					lines_to_check[i][2].append(row_col_to_index((1, 1)))
					lines_to_check[i][2].append(row_col_to_index((0, 2)))
				elif col == 2:
					lines_to_check[i][2].append(row_col_to_index((0, 0)))
					lines_to_check[i][2].append(row_col_to_index((1, 1)))
		elif row == 1:
			# Centre, so need to do both diagonals
			lines_to_check[i][2].append(row_col_to_index((0, 0)))
			lines_to_check[i][2].append(row_col_to_index((2, 2)))

			lines_to_check[i][3].append(row_col_to_index((0, 2)))
			lines_to_check[i][3].append(row_col_to_index((2, 0)))

		return lines_to_check


# Indexed into by a single 0-based index.
# Gives all lines we would have to check for potential wins from that cell outwards.
_LINES_TO_CHECK = _compute_lines_to_check()


class GameState:

	def __init__(self):
		self.board = np.zeros(_NUM_ROWS * _NUM_COLS, dtype=int)
		self.legal_moves = list(range(9))
		self.current_player = PLAYER_1
		self.winner = 0


def index_to_row_col(index: int) -> tuple[int, int]:
	"""
	:param index: A 0-based index.
	:return: (row, column) corresponding to the given index.
	"""
	return index // _NUM_COLS, index % _NUM_COLS


def row_col_to_index(row_col: tuple[int, int]) -> int:
	"""
	:param row_col: (row, column)
	:return: A 0-based index corresponding to the given row and column.
	"""
	return (row_col[0] * _NUM_COLS) + row_col[1]
