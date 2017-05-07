package sandbox;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class MenuGUI extends JPanel {
	public static int WIDTH = 800;
	public static int HEIGHT = 475;

	private static MenuGUI menuGui;
	private static long renderTime = 50;
	private static Thread renderThread;

	private static boolean running;

	private static MouseListener MyMouseListener;
	private static String clickLog;
	
	private static boolean inLevelSelect;

	public MenuGUI() {
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

		this.addMouseListener(MyMouseListener);
		this.setPreferredSize(new Dimension(GUI.FRAMEWIDTH, GUI.FRAMEHEIGHT));
		
	}

	public void drawMenu(Graphics g){
		String filename = "Menu.png";
		if(inLevelSelect)filename="Levels.png";
		try {
			g.drawImage(ImageIO.read(new File(GameGUI.imageURL+filename)), 0, 0, null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void stopRender() {
		running = false;
	}
	
	public void startRender() {
		running = true;
		renderThread = new mainMenuRenderThread();
		renderThread.start();
	}
	
	private static void writeClickLog(int x, int y, boolean btn) {
		if (clickLog == "") {
			clickLog += x + "," + y + "," + (btn == false ? 0 : 1);
		} else {
			clickLog += ";" + x + "," + y + "," + (btn == false ? 0 : 1);
		}

	}

	public String getClickLog() {
		String ret = clickLog;
		clickLog = "";
		return ret;
	}

	public void paintComponent(Graphics g) {
		drawMenu(g);
	}

	private static class mainMenuRenderThread extends Thread {
		@Override
		public void run() {
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
	
	public int getClickedItem(int x, int y) {
		if(!inLevelSelect){
			if(x>=240 && x<552){
				if(y>40 && y<90)return 1; //Start
				if(y>125 && y<180)return 2; //Select
				if(y>215 && y<265)return 3; //Exit
			}
		}else{
			//LevelSelect
		}
		return 0;
	}

}
