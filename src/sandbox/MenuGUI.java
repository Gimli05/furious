package sandbox;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * A menu megjeleniteseert felelos Jpanel
 * @author Long
 *
 */

public class MenuGUI extends JPanel {
	/**Szelesseg  es magassag pixelben**/
	public static int WIDTH = 800;
	public static int HEIGHT = 475;

	/**Renderelo szal, ket render kozotti ido es sajat pointer**/
	private static MenuGUI menuGui;
	private static long renderTime = 50;
	private static Thread renderThread;

	/**Fut e meg a menu**/
	private static boolean running;

	/**Kattintas figyelo es valtoasokat jegyzo szoveg**/
	private static MouseListener MyMouseListener;
	private static String clickLog;
	
	/**Jelzi hogy ha a manenu meg aktiv**/
	private static boolean inLevelSelect;

	/**Konstruktor**/
	public MenuGUI() {
		/*Ha incializaljuk akkor a menut hasznaéjuk eppen, kezdo adatokat allitunk*/
		running = false;
		inLevelSelect=false;
		menuGui = this;
		
		clickLog = "";

		MyMouseListener = new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent arg0) {
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
				writeClickLog(e.getX(),e.getY(),e.getButton() == MouseEvent.BUTTON3);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}
		};

		/*Megadjuk a kttintas kezelot es a meretet*/
		this.addMouseListener(MyMouseListener);
		this.setPreferredSize(new Dimension(GUI.FRAMEWIDTH, GUI.FRAMEHEIGHT));
		
	}

	/**Kirajzoljuk a menut**/
	public void drawMenu(Graphics g){
		/*Ha a menuben vagyunk akkor a menu kepet rajzoljk ki*/
		String filename = "Menu.png";
		if(inLevelSelect)filename="Levels.png";
		try {
			g.drawImage(ImageIO.read(new File(GameGUI.imageURL+filename)), 0, 0, null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**Megallitjuk a kirahzolast*/
	public void stopRender() {
		SoundManager.stopMenuMusic();
		running = false;
	}
	
	/**Elindtijuk a kirahzolast**/
	public void startRender() {
		running = true;
		renderThread = new mainMenuRenderThread();
		renderThread.start();
		SoundManager.playMenuMusic();
	}
	
	/**A kattintasi gelyeket felirjuk**/
	private static void writeClickLog(int x, int y, boolean btn) {
		if (clickLog == "") {
			clickLog += x + "," + y + "," + (btn == false ? 0 : 1);
		} else {
			clickLog += ";" + x + "," + y + "," + (btn == false ? 0 : 1);
		}

	}

	/**LE lehet kerni a kattintasi helyeket**/
	public String getClickLog() {
		String ret = clickLog;
		clickLog = "";
		return ret;
	}

	/**Kirajzolas**/
	public void paintComponent(Graphics g) {
		drawMenu(g);
	}

	/**Fo rajzolo szal inditasa**/
	private static class mainMenuRenderThread extends Thread {
		@Override
		public void run() {
			/*Amig a menu aktiv csinaljuk*/
			while (running) {
				try {
					menuGui.repaint();
					Thread.sleep(renderTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**Lekerjuk a kattintott elem indexet**/
	public int getClickedItem(int x, int y) 
	{
		/*A poziciokat fix koordinatakhoz kotottuk*/
		if(!inLevelSelect){
			if(x>192 && x<610){
				if(y>40 && y<90)return 1; //Start
				if(y>125 && y<180){
					if(x>195 && x<270)return 21;
					if(x>305 && x<385)return 22;
					if(x>415 && x<495)return 23;
					if(x>530 && x<610)return 24;
				}
				if(y>215 && y<265)return 3; //Exit
			}
		}

		return 0;
	}

}
