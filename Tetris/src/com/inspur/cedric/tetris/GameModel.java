package com.inspur.cedric.tetris;

import java.util.Arrays;
import java.util.Comparator;

import com.inspur.cedric.tetris.GameShape.Block;

public class GameModel {
	// width & height
	public static final int WIDTH = 10;
	public static final int HEIGHT = 20;
	// indicates current shape in game
	private GameShape currentShape;
	// indicates next shape in game
	private GameShape nextShape;
	// indicates total score in game
	private int totalScores;
	// indicates state of game
	private GameState gameState;
	// indicates the board which gaming on
	private Block[][] board;
	/**
	 * constructor
	 */
	public GameModel() {
		currentShape = GameShape.randomOne();
		nextShape = GameShape.randomOne();
		totalScores = 0;
		gameState = GameState.READY;
		board = new Block[HEIGHT][WIDTH];
	}
	/**
	 * GETTER & SETTER: currentShape, nextShape, totalScore, gameState
	 */
	public GameShape getCurrentShape() { return currentShape; }
	public void setCurrentShape(GameShape currentShape) { this.currentShape = currentShape; }
	public GameShape getNextShape() { return nextShape; }
	public void setNextShape(GameShape nextShape) { this.nextShape = nextShape; }
	public int getTotalScores() { return totalScores; }
	public void setTotalScores(int totalScores) { this.totalScores = totalScores; }
	public GameState getGameState() { return gameState; }
	public void setGameState(GameState gameState) { this.gameState = gameState; }
	public Block[][] getBoard() { return board; }
	public void setBoard(Block[][] board) { this.board = board; }
	/**
	 * condition
	 */
	private boolean canMoveDown() {
		Block[] blocks = currentShape.blocks;
		for(Block block : blocks) {
			int row = block.getRow(), col = block.getCol();
			if(row == HEIGHT-1 || (row >= -1 && board[row+1][col] != null)) {
				return false;
			}
		}
		return true;
	}
	private boolean isOutOfBounds() {
		Block[] blocks = currentShape.blocks;
		for(Block block : blocks) {
			int row = block.getRow(), col = block.getCol();
			// NOTE: row can be negative, but column cannot
			if(row > HEIGHT-1 || col > WIDTH-1 || col < 0) {
				return true;
			}
		}
		return false;
	}
	private boolean isOverlapping() {
		Block[] blocks = currentShape.blocks;
		for(Block block : blocks) {
			int row = block.getRow(), col = block.getCol();
			if(row >= 0 && col >= 0 && board[row][col] != null) {
				return true;
			}
		}
		return false;
	}
	private boolean isFullLine(int row) {
		for(int col = 0; col < WIDTH; col++) {
			if(board[row][col] == null) {
				return false;
			}
		}
		return true;
	}
	private boolean checkNextShapeBounds() {
		Block[] blocks = nextShape.blocks;
		for(Block block : blocks) {
			int row = block.getRow();
			int col = block.getCol();
			if(row >= 0 && col >= 0 && board[row][col] != null) {
				return true;
			}
		}
		return false;
	}
	private boolean checkCurrentShapeBounds() {
		Block[] blocks = currentShape.blocks;
		for(Block block : blocks) {
			int row = block.getRow();
			if(row < 0) {
				return true;
			}
		}
		return false;
	}
	/**
	 * action
	 */
	public void moveDown() {
		if(canMoveDown()) {
			currentShape.moveDown();
		} else {
			/**
			 * here, if current shape cannot move down, and it is out of bounds(do not consider erasing),
			 * then I think if it has negative-row block, the game is over
			 */
			if(checkCurrentShapeBounds()) {
				gameState = GameState.OVER;
			} else {
				pack();
				eraseLine();
				if(!checkNextShapeBounds()) {
					currentShape = nextShape;
					nextShape = GameShape.randomOne();
				} else {
					gameState = GameState.OVER;
				}
			}
		}
	}
	public void moveDownToBottom() {
		boolean flag = true;
		while(flag) {
			if(canMoveDown()) {
				currentShape.moveDown();
			} else {
				flag = false;
			}
		}
		/**
		 * same above
		 */
		if(checkCurrentShapeBounds()) {
			gameState = GameState.OVER;
		} else {
			pack();
			eraseLine();
			if(!checkNextShapeBounds()) {
				currentShape = nextShape;
				nextShape = GameShape.randomOne();
			} else {
				gameState = GameState.OVER;
			}	
		}
	}
	public void moveLeft() {
		currentShape.moveLeft();
		if(isOutOfBounds() || isOverlapping()) {
			currentShape.moveRight();
		}
	}
	public void moveRight() {
		currentShape.moveRight();
		if(isOutOfBounds() || isOverlapping()) {
			currentShape.moveLeft();
		}
	}
	public void rotate() {
		currentShape.rotate();;
		if(isOutOfBounds() || isOverlapping()) {
			// if cannot rotate, rotate back
			for(int i = 0; i < 3; i++) {
				currentShape.rotate();
			}			
		}
	}
	public void reset() {
		board = new Block[HEIGHT][WIDTH];
		currentShape = GameShape.randomOne();
		nextShape = GameShape.randomOne();
		totalScores = 0;
	}
	public void pack() {
		Block[] blocks = currentShape.blocks;
		for(Block block : blocks) {
			int row = block.getRow();
			int col = block.getCol();
			if(row >= 0 && col >= 0) {
				board[row][col] = new Block(block);
			}
		}
	}
	public void eraseLine() {
		int lines = 0;
		Arrays.sort(currentShape.blocks, new Comparator<Block>() {
			@Override
			public int compare(Block b1, Block b2) {
				return b1.getRow() - b2.getRow();
			}
		});
		// now the first block in blocks is the top one
		Block topBlock = currentShape.blocks[0];
		for(int row = topBlock.getRow(); row < HEIGHT; row++) {
			if(row >= 0 && isFullLine(row)) {
				++lines;
				for(int i = row; i > 0; i--) {
					System.arraycopy(board[i-1], 0, board[i], 0, 10);
				}
				// new line for first line
				for(int col = 0; col < WIDTH; col++) {
					board[0][col] = null;
				}
			}
		}
		totalScores += lines * 20;
	}
	/**********************************************************/
	public enum GameState {
		READY,
		RUNNING,
		PAUSING,
		OVER
	}
}