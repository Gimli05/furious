package sandbox;

import java.awt.Dimension;

import javax.swing.JFrame;

public class GUI extends JFrame {
	private static final long serialVersionUID = 1L;
	protected static String FRAMENAME = "Michael Bay's TRAINZ";
	protected static int FRAMEWIDTH;
	protected static int FRAMEHEIGHT;

	private static GameGUI gameGui;
	private static MenuGUI menuGui;
	
	public GUI() {
		gameGui = null;
		menuGui = null;
	}

	//Game
	protected void setGameView(int width, int height, int TileInterval) {
		gameGui = new GameGUI();
		gameGui.init(width, height, TileInterval);

		FRAMEWIDTH = GameGUI.BOARDWIDTH * GameGUI.TILEWIDTH;
		FRAMEHEIGHT = GameGUI.BOARDHEIGHT * GameGUI.TILEHEIGHT;
		this.setTitle(FRAMENAME);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);

		this.setVisible(true);
		this.setPreferredSize(new Dimension(FRAMEWIDTH, FRAMEHEIGHT));
		this.add(gameGui);
		this.pack();
		this.toFront();
		this.requestFocus();
	}

	protected GameGUI getGameView() {
		return gameGui;
	}

	public boolean isGameClosed() {
		return gameGui == null;
	}

	//Menu
	protected void setMenuView() {
		if (gameGui != null) {
			gameGui.stopRender();
			gameGui = null;
		}
		if (menuGui != null) {
			menuGui = null;
		}
		
		menuGui=new MenuGUI();
			
		FRAMEWIDTH = MenuGUI.WIDTH;
		FRAMEHEIGHT = MenuGUI.HEIGHT;
		this.setTitle(FRAMENAME);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);

		this.setVisible(true);
		this.setPreferredSize(new Dimension(FRAMEWIDTH, FRAMEHEIGHT));
		this.add(menuGui);
		this.pack();
		this.toFront();
		this.requestFocus();
	}

	protected MenuGUI getMenuView() {
		return menuGui;
	}

	public boolean isMenuClosed() {
		return menuGui == null;
	}

	public static void main(String[] args) {
		GameController myGui= new GameController();
		myGui.showMenu();
		return;
	}
}
