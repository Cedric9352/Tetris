package com.inspur.cedric.tetris;

import java.awt.image.BufferedImage;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Random;

public abstract class GameShape {
	// indicates pivot of shape
	protected Block pivot;
	// indicates blocks that a shape contains
	protected Block[] blocks;
	// indicates shape type
	protected ShapeType shapeType;
	// random
	protected static Random random;
	static {
		random = new Random(42);
	}
	// constructor
	protected GameShape() {
		blocks = new Block[4];
	}
	/**
	 * action interface of GameShape
	 */
	public void moveDown() {
		for(Block block : blocks) {
			block.moveDown();
		}
	}
	public void moveLeft() {
		for(Block block : blocks) {
			block.moveLeft();
		}
	}
	public void moveRight() {
		for(Block block : blocks) {
			block.moveRight();
		}
	}
	// CORE method of program
	// x' = (x-x_p)cos(a) - (y-y_p)*sin(a) + x_p
	// y' = (x-x_p)sin(a) + (y-y_p)*cos(a) + y_p
	public void rotate() {
		if(shapeType != ShapeType.O) {
			for(Block block : blocks) {
				if(block != pivot) {
					int row = -(block.getCol()-pivot.getCol())+pivot.getRow();
					int col = block.getRow()-pivot.getRow()+pivot.getCol();
					block.setRow(row);
					block.setCol(col);
				}
			}
		}
	}
	/**
	 * get instance of shape
	 */
	public static GameShape createInstance(Class<? extends GameShape> clazz) {
		GameShape gameShape = null;
		try {
			Constructor<? extends GameShape> constructor = clazz.getDeclaredConstructor();
			constructor.setAccessible(true);
			gameShape = constructor.newInstance();
		} catch(InvocationTargetException | NoSuchMethodException | IllegalAccessException | InstantiationException e) {
			throw new RuntimeException("cannot create blocks");
		}
		return gameShape;
	}
	/**
	 * choose a shape randomly
	 */
	public static GameShape randomOne() {
		ShapeType shapeType = ShapeType.randomShape();
		GameShape gameShape = null;
		switch (shapeType) {
		case O:
			gameShape = createInstance(ShapeO.class);
			break;
		case I:
			gameShape = createInstance(ShapeI.class);
			for(int i = 0; i < random.nextInt(2); i++) {
				gameShape.rotate();
			}
			break;
		case T:
			gameShape = createInstance(ShapeT.class);
			for(int i = 0; i < random.nextInt(4); i++) {
				gameShape.rotate();
			}
			break;
		case J:
			gameShape = createInstance(ShapeJ.class);
			for(int i = 0; i < random.nextInt(4); i++) {
				gameShape.rotate();
			}
			break;
		case L:
			gameShape = createInstance(ShapeL.class);
			for(int i = 0; i < random.nextInt(4); i++) {
				gameShape.rotate();
			}
			break;
		case Z:
			gameShape = createInstance(ShapeZ.class);
			for(int i = 0; i < random.nextInt(4); i++) {
				gameShape.rotate();
			}
			break;
		case S:
			gameShape = createInstance(ShapeS.class);
			for(int i = 0; i < random.nextInt(4); i++) {
				gameShape.rotate();
			}
			break;
		default:
			break;
		}
		return gameShape;
	}
	/**
	 * toString override
	 */
	public String toString() {
		return Arrays.toString(blocks);
	}
	/**********************************************************/
	public static class Block {
		// indicates which row the block is
		private int row;
		// indicates which column the block is
		private int col;
		// what block looks like
		private BufferedImage blockImage;
		/**
		 * constructor
		 */
		public Block(int row, int col, BufferedImage blockImage) {
			this.row = row;
			this.col = col;
			this.blockImage = blockImage;
		}
		public Block(Block block) {
			this.row = block.row;
			this.col = block.col;
			this.blockImage = block.blockImage;
		}
		/**
		 * GETTER & SETTER: row, column, blockImage
		 */
		public int getRow() { return row; }
		public void setRow(int row) { this.row = row; }
		public int getCol() { return col; }
		public void setCol(int col) { this.col = col; }
		public BufferedImage getBlockImage() { return blockImage; }
		public void setBlockImage(BufferedImage blockImage) { this.blockImage = blockImage; }
		/**
		 * action of block
		 */
		public void moveDown() { ++row; }
		public void moveLeft() { --col; }
		public void moveRight() { ++col; }
		/**
		 * toString override
		 */
		@Override
		public String toString() {
			StringBuilder sBuilder = new StringBuilder();
			sBuilder.append("block: ").append("(").append(row).append(", ").append(col).append(")");
			return sBuilder.toString();
		}
	}
	/**********************************************************/
	private static class ShapeO extends GameShape {
		/**
		 * constructor
		 */
		private ShapeO() {
			super();
			blocks[0] = new Block(-1, 4, GameView.Image.O);
			blocks[1] = new Block(-1, 5, GameView.Image.O);
			blocks[2] = new Block(0, 4, GameView.Image.O);
			blocks[3] = new Block(0, 5, GameView.Image.O);
			pivot = null;
			shapeType = ShapeType.O;
		}
	}
	/**********************************************************/
	private static class ShapeI extends GameShape {
		/**
		 * constructor
		 */
		private ShapeI() {
			super();
			blocks[0] = new Block(-1, 3, GameView.Image.I);
			blocks[1] = new Block(-1, 4, GameView.Image.I);
			blocks[2] = new Block(-1, 5, GameView.Image.I);
			blocks[3] = new Block(-1, 6, GameView.Image.I);
			pivot = blocks[1];
			shapeType = ShapeType.I;
		}
	}
	/**********************************************************/
	private static class ShapeT extends GameShape {
		/**
		 * constructor
		 */
		private ShapeT() {
			super();
			blocks[0] = new Block(-1, 3, GameView.Image.T);
			blocks[1] = new Block(-1, 4, GameView.Image.T);
			blocks[2] = new Block(-1, 5, GameView.Image.T);
			blocks[3] = new Block(0, 4, GameView.Image.T);
			pivot = blocks[1];
			shapeType = ShapeType.T;
		}
	}
	/**********************************************************/
	private static class ShapeJ extends GameShape {
		/**
		 * constructor
		 */
		private ShapeJ() {
			super();
			blocks[0] = new Block(-1, 3, GameView.Image.J);
			blocks[1] = new Block(-1, 4, GameView.Image.J);
			blocks[2] = new Block(-1, 5, GameView.Image.J);
			blocks[3] = new Block(0, 5, GameView.Image.J);
			pivot = blocks[1];
			shapeType = ShapeType.J;
		}
	}
	/**********************************************************/
	private static class ShapeL extends GameShape {
		/**
		 * constructor
		 */
		private ShapeL() {
			super();
			blocks[0] = new Block(-1, 3, GameView.Image.L);
			blocks[1] = new Block(-1, 4, GameView.Image.L);
			blocks[2] = new Block(-1, 5, GameView.Image.L);
			blocks[3] = new Block(0, 3, GameView.Image.L);
			pivot = blocks[1];
			shapeType = ShapeType.L;
		}
	}
	/**********************************************************/
	private static class ShapeZ extends GameShape {
		/**
		 * constructor
		 */
		private ShapeZ() {
			super();
			blocks[0] = new Block(-1, 3, GameView.Image.Z);
			blocks[1] = new Block(-1, 4, GameView.Image.Z);
			blocks[2] = new Block(0, 4, GameView.Image.Z);
			blocks[3] = new Block(0, 5, GameView.Image.Z);
			pivot = blocks[1];
			shapeType = ShapeType.Z;
		}
	}
	/**********************************************************/
	private static class ShapeS extends GameShape {
		/**
		 * constructor
		 */
		private ShapeS() {
			super();
			blocks[0] = new Block(-1, 5, GameView.Image.S);
			blocks[1] = new Block(-1, 4, GameView.Image.S);
			blocks[2] = new Block(0, 4, GameView.Image.S);
			blocks[3] = new Block(0, 3, GameView.Image.S);
			pivot = blocks[1];
			shapeType = ShapeType.S;
		}
	}
	/**********************************************************/
	public static enum ShapeType {
		O, I, T, J, L, Z, S;
		public static ShapeType randomShape() {
			return values()[random.nextInt(7)];
		}
	}
}