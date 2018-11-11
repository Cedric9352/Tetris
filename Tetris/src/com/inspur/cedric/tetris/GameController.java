package com.inspur.cedric.tetris;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.inspur.cedric.tetris.GameModel.GameState;

public class GameController extends JFrame{
	/**
	 * default UID
	 */
	private static final long serialVersionUID = 1L;
	// indicates which model this controller sends events to
	private GameModel gameModel;
	private GameView gameView;
	/**
	 * constructor
	 */
	public GameController(GameView gameView, GameModel gameModel) {
		super("Tetris");
		this.setVisible(true);
		this.setSize(543, 595);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.gameView = gameView;
		this.gameModel = gameModel;
		this.add(gameView);
	}
	/**
	 * game entrance
	 */
	public void init() {
		// initialize keyListener
		KeyListener keyListener = new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				synchronized (gameModel) {
					int keyCode = e.getKeyCode();
					switch (keyCode) {
					case KeyEvent.VK_P:
						if(gameModel.getGameState() == GameState.RUNNING) {
							gameModel.setGameState(GameState.PAUSING);
						}
						break;
					case KeyEvent.VK_C:
						if(gameModel.getGameState() == GameState.PAUSING) {
							gameModel.setGameState(GameState.RUNNING);
						}
						break;
					case KeyEvent.VK_S:
						gameModel.reset();
						gameModel.setGameState(GameState.RUNNING);
						break;
					case KeyEvent.VK_Q:
						System.exit(0);
					default:
						if(gameModel.getGameState() == GameState.RUNNING) {
							switch(keyCode) {
							case KeyEvent.VK_UP:
									gameModel.rotate();
								break;
							case KeyEvent.VK_DOWN:
									gameModel.moveDown();
								break;
							case KeyEvent.VK_LEFT:
									gameModel.moveLeft();
								break;
							case KeyEvent.VK_RIGHT:
									gameModel.moveRight();
								break;
							case KeyEvent.VK_SPACE:
									gameModel.moveDownToBottom();
								break;
							}
							break;
						}
					}
					gameView.repaint();
				}
			}
		};
		this.addKeyListener(keyListener);
		this.requestFocus();
		// start auto down thread
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				synchronized (gameModel) {
					if(gameModel.getGameState() == GameState.RUNNING) {
						gameModel.moveDown();
						SwingUtilities.invokeLater(new Runnable() {
							@Override
							public void run() {
								gameView.repaint();
							}
						});
					}
				}
			}
		}, 0, 1000);
	}
	public static void run() {
		GameModel gameModel = new GameModel();
		GameView gameView = new GameView(gameModel);
		GameController gameController = new GameController(gameView, gameModel);
		gameModel.setGameState(GameState.RUNNING);
		gameController.init();
	}
	public static void main(String[] args) {
		GameController.run();
	}
}