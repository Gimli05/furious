package sandbox;


import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.JPanel;

/**
 * A jatek nezet megjeleniteseert es kezeleseert felelos osztaly Panel
 * @author Long
 *
 */
public class GameGUI extends JPanel {
	private static final long serialVersionUID = 1L;
	/**Fut a a jatek**/
	private static boolean running;
	
	/**Blokkok merete es blokkok altal leirt palyameret**/
	protected static int TILEWIDTH = 40;
	protected static int TILEHEIGHT = 40;
	protected static int BOARDWIDTH;
	protected static int BOARDHEIGHT;
	
	/**Sajat pointer**/
	private static GameGUI gameGui;

	/**Egy blokkon eltöltött ideo**/
	protected static int TILEINTERVAL
	/**Forrasok helye**/;
	protected static String imageURL = "raw/";
	/**Blokkon beluli leptetesek ideje*/
	protected static long renderTime = 50;
	protected static int hopTime = 100;

	/**MEgejelenito szal**/
	private static Thread renderThread;
	/**Valtozo blokkokat konyvelo terkep**/
	private static boolean changeMap[][];
	/**Kattintasok tarolasa**/
	private static String clickLog = "";
	/**A sinhalozatot abrazolo tarolo**/
	private static Tile baseTileMap[][];

	/**Kis bugfix**/
	private static int frameFix = 5; 
	private static boolean paintTrain=false; 
	
	/**MEgjelenitendo vonatokat tarolo tartolo**/
	private static ArrayList<TrainView> trainContainer;

	/**Kattintas figyelo**/
	private static MouseListener MyMouseListener;
	
	/**Animaciokat kezelo Manager**/
	private static AnimManager animManager;

	/**Konstruktor**/
	public GameGUI() {
		gameGui=this;
	}

	/**Kezdo inicializalasok**/
	public void init(int width, int height, int TileInterval) {
		BOARDWIDTH = width;
		BOARDHEIGHT = height;
		TILEINTERVAL=TileInterval;	
		baseTileMap = new Tile[BOARDWIDTH][BOARDHEIGHT];
		changeMap = new boolean[BOARDWIDTH][BOARDHEIGHT];
		
		/*A teljes palyat akarjuk amjd frissiteni also korben*/
		for (int i = 0; i < BOARDWIDTH; i++)
			for (int j = 0; j < BOARDHEIGHT; j++) {
				changeMap[i][j] = false;
				baseTileMap[i][j] = null;
			}

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
				/*Ha kattintunk logoljuk es grissitjuk a kattintott helyet*/
				writeClickLog((int) Math.floor(e.getX() / TILEWIDTH), (int) Math.floor(e.getY() / TILEHEIGHT),
						e.getButton() == MouseEvent.BUTTON3);
				changeMap[(int) Math.floor(e.getX() / TILEWIDTH)][(int) Math.floor(e.getY() / TILEHEIGHT)]=true;
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}
		};

		/*Hozzaaadjuk a figyelot es a meretet*/
		this.addMouseListener(MyMouseListener);
		this.setPreferredSize(new Dimension(GUI.FRAMEWIDTH, GUI.FRAMEHEIGHT));
		

		trainContainer = new ArrayList<TrainView>();
		animManager=new AnimManager();
	}

	/**Kirajzolas**/
	@Override
	public void paintComponent(Graphics g) {	
		
		for (int x = 0; x < BOARDWIDTH; x++) {
			for (int y = 0; y < BOARDHEIGHT; y++) {
				/*FrameLoadFix*/
				if(frameFix>0){
					changeMap[x][y]=true;
					frameFix++;
				}
				/*Ha az adott meno valtozott frissitjuk*/
				if (baseTileMap[x][y] != null && changeMap[x][y]) {
					g.drawImage(baseTileMap[x][y].getImage(), x * TILEWIDTH, y * TILEHEIGHT, null);
				}
			}
		}
		
		/*Amennyiben mar rajzolunk vonatokat kirajzoljuk oket**/
		if(paintTrain)
		for (TrainView train : trainContainer) {
			train.draw(g);
		}
		
		/*Ha van animacios manager akkor az is rajzoljon*/
		if(animManager!=null)animManager.draw(g);

		/*Minden frissitest elvegeztunk, nullazzuk a mapet*/
		for (int x = 0; x < BOARDWIDTH; x++) {
			for (int y = 0; y < BOARDHEIGHT; y++) {
				changeMap[x][y] = false;
			}
		}
	}

	/**INdituk a rajzolast**/
	public void startRender() {
		running=true;
		renderThread = new mainGameRenderThread();
		/*Felepitjuk a palyat*/
		buildBaseMap();
		/*Inditjuk*/
		renderThread.start();
	}
	
	/**Leallitjuk arajzolast**/
	public void stopRender() {
		running=false;
		if(animManager!=null)animManager.endAllAnimation();
		SoundManager.stopBackgroundMusic();
		SoundManager.stopTrainSound();
	}

	
	/**Az adott helyen levo palyeelemet meghatarozzuk**/
	public void setBaseTileMap(int x, int y, String object) {
		baseTileMap[x][y] = new Tile(object);
		changeMap[x][y] = true;
	}

	/**Adott helyen levo palyaelemet lekerdezzuk*/
	public Tile getBaseTileMap(int x, int y) {
		return baseTileMap[x][y];
	}

	/**Minde palya elemet ellenorizzuk, jeloljuk ha sarok, es mindent beforgatjuk**/
	private void buildBaseMap() {
		for (int y = 0; y < BOARDHEIGHT; y++) {
			for (int x = 0; x < BOARDWIDTH; x++) {
				baseTileMap[x][y].markIfCorner(baseTileMap, x, y);
				baseTileMap[x][y].setTileAngle(baseTileMap, x, y);
			}
		}
	}

	/**Ha kattintunk logoluk**/
	private static void writeClickLog(int x, int y, boolean btn) {
		if (clickLog == "") {
			clickLog += x + "," + y + "," + (btn == false ? 0 : 1);
		} else {
			clickLog += ";" + x + "," + y + "," + (btn == false ? 0 : 1);
		}

	}

	/**Lekerdezzuk a kattintasokat**/
	public String getClickLog() {
		String ret = clickLog;
		clickLog = "";
		return ret;
	}
	
	/**Alagutszajat kapcsolunk be**/
	public void activateTunnel(int x, int y){
		if(baseTileMap[x][y].getType().equals("U")){
			baseTileMap[x][y].activate(baseTileMap, x, y);
			changeMap[x][y]=true;
		}
	}
	
	/**Alagutszajat kapcsolunk ki**/
	public void deactivateTunnel(int x, int y){
		if(baseTileMap[x][y].getType().equals("U")){
			baseTileMap[x][y].deactivate(baseTileMap, x, y);
			changeMap[x][y]=true;
		}
	}
	
	/**Alagutszajon vagy válto alasson kapcsolunk**/
	public void switchState(int x, int y){
		baseTileMap[x][y].switchState(baseTileMap, x, y);
		changeMap[x][y]=true;
	}
	
	/**Konkret pozicioba allitunk**/
	public void switchState(int x, int y, boolean s){
		/*Váltani fogunk rajta ezért negáljuk*/
		baseTileMap[x][y].setState(!s);
		baseTileMap[x][y].switchState(baseTileMap, x, y);
		changeMap[x][y]=true;
	}
	
	/**Egy idoegysegget leptetunk a vonatokon**/
	public void updateTime(int time) {
		for (TrainView train : trainContainer) {
			train.updateTime(time);
		}
	}

	/**Hozzaadunk egy uj vonatot**/
	public void addTrain(String colorString, int cX, int cY, int nX, int nY) {
		ArrayList<String> colors = new ArrayList<String>();
		String col[] = colorString.split("");
		/*Hozzaadjuk a szineket**/
		for (String c : col) {
			colors.add(c);
		}
		int pX = 0;
		int pY = 0;
		double rot = 0;
		/*Az elozo es a jelnelegi pozicio alapjan megadjuk a kovetkezot es az elforgatast*/
		if (cX == nX) {
			pX = nX;
			if (cY > nY) {
				/* fentröl le**/
				pY = cY + 1;
				rot = 270;
			} else {
				/* lentröl fel*/
				pY = cY - 1;
				rot = 90;
			}
		} else if (cY == nY) {
			pY = nY;
			if (cX > nX) {
				/* jobbrol balra*/

				pX = cX + 1;
				rot = 180;
			} else {
				/* balrol jobbra*/
				pX = cX - 1;
				rot = 0;
			}
		}
		/*Letrehozzuk es hozzaadjuk a vonatot*/
		TrainView newTrain = new TrainView(colors);
		newTrain.setPos(pX, pY, cX, cY, nX, nY);
		newTrain.setAngle(rot);
		trainContainer.add(newTrain);
	}

	/**Minden vonatot leptetunk az daott x,y koordinatakba es lathatosagot is allitunk**/
	public void moveAllTrain(String coordsString) {
		if (coordsString.length() < 6)
			return;
		String[] coords = coordsString.split(",");
		for (int idx = 1; idx < coords.length; idx += 3) {
			/*Poziio leptetes*/
			trainContainer.get(idx / 3).updatePos(Integer.parseInt(coords[idx]), Integer.parseInt(coords[idx + 1]));
			/*Lathatosag allitas*/
			trainContainer.get(idx/3).setVisibility( Integer.parseInt(coords[idx + 2]));
		}
	}

	/**Vagonok lathatosagat alitjuk at**/
	public void setCabStates(String states){
		if (states.length() < 1)
			return;		
		String[] trainStates = states.split(",");
		for (int idx = 0; idx < trainStates.length; idx ++) {
			trainContainer.get(idx).setCabsState(trainStates[idx]);
		}
	}
	
	/**Minden valtozast elvegzunk**/
	private static void readAllChangeLog() {	
		for (TrainView train : trainContainer) {
			readChangeLog(train.getChangeLog());
		}
		
	}
	
	/**Minden valtozast jelunk a mpa-nek**/
	private static void readChangeLog(String log) {
		if (log.length() < 4)
			return;
		String[] coords = log.split(",");
		for (int i = 1; i < coords.length; i += 2) {
			try{
			changeMap[Integer.parseInt(coords[i])][Integer.parseInt(coords[i + 1])] = true;
			}catch(IndexOutOfBoundsException e){};
			}
	}

	/**Elinditjuk a vonat kirajzolassat**/
	public void paintTrain(){
		paintTrain=true;
	}
	
	/**Uj animacio hozzadasa*/
	public static void addAnimation(int x, int y, String type){
		/*Ha csak felszallo jelzes kell*/
		if(type.equals("4") || type.equals("5") ||type.equals("6")) type="Passengers";
		animManager.addAnimation(x, y, type);
	}

	/**Animacio eltavolitasa**/
	public static void removeAnimation(int x, int y, String type){
		animManager.removeAnimation(x, y, type);
	}
	
	/**Minden animacio megallitasa**/
	public static void endAllAnimation(){
		animManager.endAllAnimation();
	}
	
	/**A algutszaj utan kovetkezo leptetesi pont meghatarozasa (animacio miatt kell)**/
	public Tunnel getFirstTunnelPart(TunnelEntrance e){
		int x = e.getX();
		int y = e.getY();
		int cx=0;
		int cy=0;
		
		if (x == 0) {
		} else if (x == GameGUI.BOARDWIDTH - 1) {
		} else {
			if (y == 0) {
			} else if (y == GameGUI.BOARDHEIGHT - 1) {
			} else {			
				/*/ Szemben*/
				if (!baseTileMap[x][y - 1].getType().equals("x") && !baseTileMap[x][y + 1].getType().equals("x")) {
					cy=0;
					cx=1;
				}
				if (!baseTileMap[x - 1][y].getType().equals("x") && !baseTileMap[x + 1][y].getType().equals("x")) {
					cy=-1;
					cx=0;
				}
				/* Jobb*/

				
				if (!baseTileMap[x + 1][y].getType().equals("x") && !baseTileMap[x][y + 1].getType().equals("x")) {
					cy=0;
					cx=-1;
				}				

				if (!baseTileMap[x - 1][y].getType().equals("x") && !baseTileMap[x][y - 1].getType().equals("x")) {
					cy=0;
					cx=1;
				}
				
				/* Bal*/
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
	
	
	/**Fo kirajzolo szal inditasa**/
	private static class mainGameRenderThread extends Thread {
		@Override
		public void run() {
			while (running) {
				try {
					/*Elolvassuk es elvegezzuk az valtoztatasokat, animaciokat frissitunk*/
						readAllChangeLog();
						animManager.setUpdatedTiles(changeMap);
						animManager.updateAnimations();
						gameGui.repaint();

					Thread.sleep(renderTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	

}
