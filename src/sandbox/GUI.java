package sandbox;

import java.awt.AlphaComposite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GUI extends JPanel {
	private static final long serialVersionUID = 1L;
	private static JFrame frame;
	private static GUI gui;
	private static boolean running;
	
	protected static String FRAMENAME = "Michael Bay's TRAINZ";
	protected static int FRAMEWIDTH;
	protected static int FRAMEHEIGHT;
	protected static int TILEWIDTH = 40;
	protected static int TILEHEIGHT = 40;
	protected static int BOARDWIDTH;
	protected static int BOARDHEIGHT;

	protected static int TILEINTERVAL;
	protected static String imageURL = "raw/";
	protected static long renderTime = 50;
	protected static int hopTime = 100;

	private static Thread renderThread;
	private static boolean changeMap[][];
	private static String clickLog = "";
	private static Tile baseTileMap[][];

	
	
	private static ArrayList<TrainView> trainContainer;

	private static MouseListener MyMouseListener;
	
	private static Animation anim;
	private static AnimManager animManager;

	// TFix
	
	private static boolean paintTrain=false; 

	public GUI() {
		baseTileMap = new Tile[BOARDWIDTH][BOARDHEIGHT];
		changeMap = new boolean[BOARDWIDTH][BOARDHEIGHT];
		for (int i = 0; i < BOARDWIDTH; i++)
			for (int j = 0; j < BOARDHEIGHT; j++) {
				changeMap[i][j] = false;
				baseTileMap[i][j] = null;
			}
	}

	public void init(int width, int height, int TileInterval) {
		BOARDWIDTH = width;
		BOARDHEIGHT = height;
		FRAMEWIDTH = BOARDWIDTH * TILEWIDTH;
		FRAMEHEIGHT = BOARDHEIGHT * TILEHEIGHT;
		TILEINTERVAL=TileInterval;

		frame = new JFrame(FRAMENAME);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setResizable(false);
		gui = new GUI();

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
				writeClickLog((int) Math.floor(e.getX() / TILEWIDTH), (int) Math.floor(e.getY() / TILEHEIGHT),
						e.getButton() == MouseEvent.BUTTON3);
				changeMap[(int) Math.floor(e.getX() / TILEWIDTH)][(int) Math.floor(e.getY() / TILEHEIGHT)]=true;
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}
		};

		gui.addMouseListener(MyMouseListener);
		gui.setPreferredSize(new Dimension(FRAMEWIDTH, FRAMEHEIGHT));
		frame.add(gui);
		frame.pack();

		frame.toFront();
		frame.requestFocus();

		trainContainer = new ArrayList<TrainView>();
		animManager=new AnimManager();
	}

	@Override
	public void paintComponent(Graphics g) {
		// BaseDraw
		
		for (int x = 0; x < BOARDWIDTH; x++) {
			for (int y = 0; y < BOARDHEIGHT; y++) {
				if (baseTileMap[x][y] != null && changeMap[x][y]) {
					g.drawImage(baseTileMap[x][y].getImage(), x * TILEWIDTH, y * TILEHEIGHT, null);
				}
			}
		}
		
		if(paintTrain)
		for (TrainView train : trainContainer) {
			train.draw(g);
		}
		
		
		animManager.draw(g);

		for (int x = 0; x < BOARDWIDTH; x++) {
			for (int y = 0; y < BOARDHEIGHT; y++) {
				changeMap[x][y] = false;
			}
		}
	}

	public void startRender() {
		running=true;
		renderThread = new mainRenderThread();
		buildBaseMap();
		renderThread.start();
	}
	
	public void stoptRender() {
		running=false;		
	}

	
	public void setBaseTileMap(int x, int y, String object) {
		baseTileMap[x][y] = new Tile(object);
		changeMap[x][y] = true;
	}

	public Tile getBaseTileMap(int x, int y) {
		return baseTileMap[x][y];
	}

	private void buildBaseMap() {
		for (int y = 0; y < BOARDHEIGHT; y++) {
			for (int x = 0; x < BOARDWIDTH; x++) {
				baseTileMap[x][y].markIfCorner(baseTileMap, x, y);
				baseTileMap[x][y].setTileAngle(baseTileMap, x, y);
			}
		}
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
	
	public void activateTunnel(int x, int y){
		if(baseTileMap[x][y].getType().equals("U")){
			baseTileMap[x][y].activate(baseTileMap, x, y);
			changeMap[x][y]=true;
		}
	}
	
	public void deactivateTunnel(int x, int y){
		if(baseTileMap[x][y].getType().equals("U")){
			baseTileMap[x][y].deactivate(baseTileMap, x, y);
			changeMap[x][y]=true;
		}
	}
	
	public void switchState(int x, int y){
		baseTileMap[x][y].switchState(baseTileMap, x, y);
		changeMap[x][y]=true;
	}
	
	// Train
	
	public void updateTime(int time) {
		for (TrainView train : trainContainer) {
			train.updateTime(time);
		}
	}

	public void addTrain(String colorString, int cX, int cY, int nX, int nY) {
		ArrayList<String> colors = new ArrayList<String>();
		String col[] = colorString.split("");
		for (String c : col) {
			colors.add(c);
		}
		int pX = 0;
		int pY = 0;
		double rot = 0;
		if (cX == nX) {
			pX = nX;
			if (cY > nY) {
				// fentröl le
				pY = cY + 1;
				rot = 90;
			} else {
				// lentröl fel
				pY = cY - 1;
				rot = 270;
			}
		} else if (cY == nY) {
			pY = nY;
			if (cX > nX) {
				// jobbrol balra

				pX = cX + 1;
				rot = 180;
			} else {
				// balrol jobbra
				pX = cX - 1;
				rot = 0;
			}
		}
		TrainView newTrain = new TrainView(colors);
		newTrain.setPos(pX, pY, cX, cY, nX, nY);
		newTrain.setAngle(rot);
		trainContainer.add(newTrain);
	}

	public void moveAllTrain(String coordsString) {
		if (coordsString.length() < 6)
			return;
		String[] coords = coordsString.split(",");
		for (int idx = 1; idx < coords.length; idx += 3) {
			trainContainer.get(idx / 3).updatePos(Integer.parseInt(coords[idx]), Integer.parseInt(coords[idx + 1]));
			trainContainer.get(idx/3).setVisibility( Integer.parseInt(coords[idx + 2]));
		}
	}

	public void setCabStates(String states){
		if (states.length() < 1)
			return;		
		String[] trainStates = states.split(",");
		for (int idx = 0; idx < trainStates.length; idx ++) {
			trainContainer.get(idx).setCabsState(trainStates[idx]);
		}
	}
	
	private static void readAllChangeLog() {	
		for (TrainView train : trainContainer) {
			readChangeLog(train.getChangeLog());
		}
		
		if(anim!=null)anim.setUpdatedTiles(changeMap);
	}
	
	private static void readChangeLog(String log) {
		if (log.length() < 4)
			return;
		String[] coords = log.split(",");
		// Figyelünk az elsö vesszöre
		for (int i = 1; i < coords.length; i += 2) {
			changeMap[Integer.parseInt(coords[i])][Integer.parseInt(coords[i + 1])] = true;
		}
	}

	public void paintTrain(){
		paintTrain=true;
	}
	//Animation
	
	public static void addAnimation(int x, int y, String type){
		if(type.equals("4") || type.equals("5") ||type.equals("6")) type="Passengers";
		animManager.addAnimation(x, y, type);
	}

	public static void removeAnimation(int x, int y, String type){
		animManager.removeAnimation(x, y, type);
	}
	// Render
	
	public static void endAllAnimation(){
		animManager.endAllAnimation();
	}
	
	public Tunnel getFirstTunnelPart(TunnelEntrance e){
		int x = e.getX();
		int y = e.getY();
		int cx=0;
		int cy=0;
		
		if (x == 0) {
		} else if (x == GUI.BOARDWIDTH - 1) {
		} else {
			if (y == 0) {
			} else if (y == GUI.BOARDHEIGHT - 1) {
			} else {			
				// Szemben
				if (!baseTileMap[x][y - 1].getType().equals("x") && !baseTileMap[x][y + 1].getType().equals("x")) {
					cy=0;
					cx=1;
				}
				if (!baseTileMap[x - 1][y].getType().equals("x") && !baseTileMap[x + 1][y].getType().equals("x")) {
					cy=-1;
					cx=0;
				}
				// Jobb

				
				if (!baseTileMap[x + 1][y].getType().equals("x") && !baseTileMap[x][y + 1].getType().equals("x")) {
					cy=0;
					cx=-1;
				}				

				if (!baseTileMap[x - 1][y].getType().equals("x") && !baseTileMap[x][y - 1].getType().equals("x")) {
					cy=0;
					cx=1;
				}
				
				// Bal
				if (!baseTileMap[x + 1][y].getType().equals("x") && !baseTileMap[x][y - 1].getType().equals("x")) {
					cy=0;
					cx=-1;
				
				}
				if (!baseTileMap[x - 1][y].getType().equals("x") && !baseTileMap[x][y + 1].getType().equals("x")) {
					cy=0;
					cx=1;
				}
			}
		}
		
		Tunnel newTunnel = new Tunnel();
		System.out.println("Prev:" +e.getX()+","+e.getY()+"   Next:"+(e.getX()+cx)+","+(e.getY()+cy));
		newTunnel.setX(e.getX() + cx);
		newTunnel.setY(e.getY() + cy);;
		return newTunnel;
	}

	private static class mainRenderThread extends Thread {
		@Override
		public void run() {
			while (running) {
				try {
					if (gui != null) {
						readAllChangeLog();
						animManager.setUpdatedTiles(changeMap);
						animManager.updateAnimations();
						gui.repaint();
					}

					Thread.sleep(renderTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	

}
