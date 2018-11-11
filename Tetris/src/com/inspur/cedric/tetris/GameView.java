package com.inspur.cedric.tetris;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import com.inspur.cedric.tetris.GameShape.Block;

public class GameView extends JPanel {
	/**
	 * default UID
	 */
	private static final long serialVersionUID = 1L;
	// indicates the size of block on image
	private static final int BLOCK_SIZE = 26;
	private static final String[] FLAGS = { "[P]ause", "[C]ontinue]", "[S]tart"};
	private static final String GAMEOVER = "game over";
	// indicates which model this view been instructed
	private GameModel gameModel;
	/**
	 * constructor
	 */
	public GameView(GameModel gameModel) {
		this.gameModel = gameModel;
	}
	/**
	 * paint override
	 */
	@Override
	public void paint(Graphics graphics) {
		graphics.drawImage(Image.background, 0, 0, this);
		graphics.translate(15, 15);
		paintBoard(graphics);
		paintCurrentShape(graphics);
		paintNextShape(graphics);
		paintState(graphics);
		paintScore(graphics);
	}
	/**
	 * action of view
	 */
	public void paintCurrentShape(Graphics graphics) {
		Block[] blocks = gameModel.getCurrentShape().blocks;
		for (Block block : blocks) {
			
			int x = block.getCol() * BLOCK_SIZE;
			int y = block.getRow() * BLOCK_SIZE;
			//  block that within board range can be displayed
			if(x >= 0 && x < GameModel.WIDTH * BLOCK_SIZE && y >= 0 && y <= GameModel.HEIGHT * BLOCK_SIZE) {
				graphics.drawImage(block.getBlockImage(), x, y, null);
			}
		}
	}
	public void paintNextShape(Graphics graphics) {
		Block[] blocks = gameModel.getNextShape().blocks;
		for (Block block : blocks) {
			int x = block.getCol() * BLOCK_SIZE + 270;
			int y = 0;
			if(gameModel.getNextShape().shapeType == GameShape.ShapeType.I) {
				y = block.getRow() * BLOCK_SIZE + 78;	
			} else {
				y = block.getRow() * BLOCK_SIZE + 60;
			}
			graphics.drawImage(block.getBlockImage(), x, y, null);
		}
	}
	public void paintBoard(Graphics a) {
		Block[][] board = gameModel.getBoard();
		for (int i = 0; i < GameModel.HEIGHT; i++) {
			for (int j = 0; j < GameModel.WIDTH; j++) {
				int x = j * BLOCK_SIZE;
				int y = i * BLOCK_SIZE;
				Block block = board[i][j];
				if (block == null) {
					a.drawRect(x, y, BLOCK_SIZE, BLOCK_SIZE);
				} else {
					a.drawImage(block.getBlockImage(), x, y, null);
				}
			}
		}
	}
	public void paintScore(Graphics graphics) {
		graphics.setFont(new Font(Font.MONOSPACED, Font.BOLD, 26));
		graphics.drawString("SCORES: " + gameModel.getTotalScores(), 285, 160);
	}
	private void paintState(Graphics graphics) {
		graphics.setFont(new Font(Font.MONOSPACED, Font.BOLD, 26));
		switch (gameModel.getGameState()) {
		case RUNNING:
			graphics.drawString(FLAGS[0], 285, 270);
			break;
		case PAUSING:
			graphics.drawString(FLAGS[1], 285, 270);
			break;
		case OVER:
			graphics.drawString(GAMEOVER, 285, 215);
			graphics.drawString(FLAGS[2], 285, 270);
			break;
		default:
			break;
		}
	}
	/**********************************************************/
	public static class Image {
		// image of background
		public static BufferedImage background; 
		// image of game-over
		public static BufferedImage gameOver;
		// image of shape
		public static BufferedImage O;
		public static BufferedImage I;
		public static BufferedImage T;
		public static BufferedImage J;
		public static BufferedImage L;
		public static BufferedImage Z;
		public static BufferedImage S;
		// initialize image
		static {
			try {
				String classPath = GameView.class.getResource("").getPath();
				String sourceBasePath = classPath.replace("bin", "src") + "resources/";
				background = ImageIO.read(new File(sourceBasePath + "background.png"));
				gameOver= ImageIO.read(new File(sourceBasePath + "game-over.png"));
				O = ImageIO.read(new File(sourceBasePath + "O.png"));
				I = ImageIO.read(new File(sourceBasePath + "I.png"));
				T = ImageIO.read(new File(sourceBasePath + "T.png"));
				J = ImageIO.read(new File(sourceBasePath + "L.png"));
				L = ImageIO.read(new File(sourceBasePath + "J.png"));
				Z = ImageIO.read(new File(sourceBasePath + "Z.png"));
				S = ImageIO.read(new File(sourceBasePath + "S.png"));
			} catch (IOException e) {
				throw new RuntimeException("cannot load image resources");
			}
		}
	}
}