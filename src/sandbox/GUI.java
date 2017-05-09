package sandbox;

import java.awt.Dimension;

import javax.swing.JFrame;

/**
 * Minden megjelenitesert felelos osztaly, ez fogja ossze a paneleket
 * @author Long
 *
 */
public class GUI extends JFrame {
	/**ALtalanos neves meretadatok*/
	private static final long serialVersionUID = 1L;
	protected static String FRAMENAME = "Michael Bay's TRAINZ";
	protected static int FRAMEWIDTH;
	protected static int FRAMEHEIGHT;

	/**A menuhoz es a jatekhoz kotott valtozok*/
	private static GameGUI gameGui;
	private static MenuGUI menuGui;
	
	
	/**Konstruktor**/
	public GUI() {
		gameGui = null;
		menuGui = null;
	}

	/**Beallitjuk a jelenlegi nezere a jatek nezetet**/
	protected void setGameView(int width, int height, int TileInterval) {
		gameGui = new GameGUI();
		gameGui.init(width, height, TileInterval);

		FRAMEWIDTH = GameGUI.BOARDWIDTH * GameGUI.TILEWIDTH;
		FRAMEHEIGHT = GameGUI.BOARDHEIGHT * GameGUI.TILEHEIGHT;
		
		this.setTitle(FRAMENAME);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);

		this.setVisible(true);
		this.setSize(new Dimension(0,0));
		gameGui.setPreferredSize(new Dimension(FRAMEWIDTH, FRAMEHEIGHT));
		this.add(gameGui);
		this.pack();
		this.toFront();
		this.requestFocus();
	}

	/**Lekerjuk a jatek nezetetre mutato pointert**/
	protected GameGUI getGameView() {
		return gameGui;
	}

	/**Amenyiben a jatek veget érte**/
	public boolean isGameClosed() {
		return gameGui == null;
	}

	/**Beallitjuk a jelenlegi nezere a menu nezetet**/
	protected void setMenuView() {
		/*Ha valamit eddig nem zartunk le akkor most megtesszuk*/
		if (gameGui != null) {
			gameGui.stopRender();
			gameGui = null;
		}
		if (menuGui != null) {
			menuGui = null;
		}
		
		/*init*/
		menuGui=new MenuGUI();
			
		FRAMEWIDTH = MenuGUI.WIDTH;
		FRAMEHEIGHT = MenuGUI.HEIGHT;
		this.setTitle(FRAMENAME);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);

		this.setVisible(true);
		menuGui.setPreferredSize(new Dimension(FRAMEWIDTH, FRAMEHEIGHT));
		this.add(menuGui);
		this.pack();
		this.toFront();
		this.requestFocus();
	}
	/**Lekerjuk a menu nezetetre mutato pointert**/
	protected MenuGUI getMenuView() {
		return menuGui;
	}
	/**Amenyiben a meu bezarult**/
	public boolean isMenuClosed() {
		return menuGui == null;
	}

	/*Fo szalunk*/
	public static void main(String[] args) {
		GameController myGui= new GameController();
		myGui.showMenu();
		menuGui.startRender();
		return;
	}
}
