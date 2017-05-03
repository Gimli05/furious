package sandbox;

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
	private static int tilePositionCount = (TILEINTERVAL / hopTime)-1;

	private static Thread renderThread;
	private static boolean changeMap[][];
	private static String clickLog = "";
	private static Tile baseTileMap[][];

	private static ArrayList<TrainView> trainContainer;

	private static MouseListener MyMouseListener;

	// Test
	private static int testCnt = 0;
	private static int testNX = 0;
	private static int testNY = 0;
	private static int FrameFix = 0;

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
				callTileClick((int) Math.floor(e.getX() / TILEWIDTH), (int) Math.floor(e.getY() / TILEHEIGHT),
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

		// LONG TESZTEL
		//addTrain("EGBRB", -1, 1, 0, 1);
		// LONG TESZTEL
	}

	@Override
	public void paintComponent(Graphics g) {
		// BaseDraw

		// RANDOM FIX
		if (FrameFix < 2) {
			changeMap[0][0] = true;
			FrameFix++;
		}

		for (int x = 0; x < BOARDWIDTH; x++) {
			for (int y = 0; y < BOARDHEIGHT; y++) {
				if (baseTileMap[x][y] != null && changeMap[x][y]) {
					g.drawImage(baseTileMap[x][y].getImage(), x * TILEWIDTH, y * TILEHEIGHT, null);
				}
			}
		}
		for (TrainView train : trainContainer) {
			train.draw(g);
		}

		for (int x = 0; x < BOARDWIDTH; x++) {
			for (int y = 0; y < BOARDHEIGHT; y++) {
				changeMap[x][y] = false;
			}
		}
	}

	public void startRender() {
		renderThread = new mainRenderThread();
		buildBaseMap();
		renderThread.start();
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

	private static void callTileClick(int x, int y, boolean btn) {
		baseTileMap[x][y].switchState(baseTileMap, x, y, btn);
		writeClickLog(x, y, btn);
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

	public static void doTurnTest(int cnt) {
		if (cnt == 0) {
			testNX = 1;
			testNY = 1;
		} else if (cnt == 1) {
			testNX = 1;
			testNY = 2;
		} else if (cnt == 2) {
			testNX = 1;
			testNY = 3;
		} else if (cnt == 3) {
			testNX = 2;
			testNY = 3;
		} else if (cnt == 4) {
			testNX = 3;
			testNY = 3;
		} else if (cnt == 5) {
			testNX = 3;
			testNY = 2;
		} else if (cnt == 6) {
			testNX = 3;
			testNY = 1;
		} else if (cnt == 7) {
			testNX = 4;
			testNY = 1;
		} else if (cnt == 8) {
			testNX = 4;
			testNY = 2;
		} else if (cnt == 9) {
			testNX = 4;
			testNY = 3;
		} else if (cnt == 10) {
			testNX = 4;
			testNY = 4;
		} else if (cnt == 11) {
			testNX = 4;
			testNY = 5;
		} else if (cnt == 12) {
			testNX = 4;
			testNY = 6;
		} else if (cnt == 13) {
			testNX = 3;
			testNY = 6;
		} else if (cnt == 14) {
			testNX = 3;
			testNY = 5;
		} else if (cnt == 15) {
			testNX = 2;
			testNY = 5;
		} else if (cnt == 16) {
			testNX = 1;
			testNY = 5;
		} else if (cnt == 17) {
			testNX = 0;
			testNY = 5;
		} else if (cnt == 18) {
			testNX = 0;
			testNY = 6;
		}
	}

	// Train

	public void updateTime(int time) {
		//time = (time + hopTime) % TILEINTERVAL;

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
		if (coordsString.length() < 4)
			return;
		String[] coords = coordsString.split(",");
		for (int idx = 1; idx < coords.length; idx += 2) {
			trainContainer.get(idx / 2).updatePos(Integer.parseInt(coords[idx]), Integer.parseInt(coords[idx + 1]));
		}
	}

	private static void readAllChangeLog() {
		for (TrainView train : trainContainer) {
			readChangeLog(train.getChangeLog());
		}
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

	// Render

	private static class mainRenderThread extends Thread {
		@Override
		public void run() {
			int cntr = 0;
			while (true) {
				try {
					if (gui != null) {
						if (cntr == tilePositionCount) {
							cntr = 0;
							testCnt++;
							//moveAllTrain("," + testNX + "," + testNY);

						} else {
							cntr++;

						}
						//doTurnTest(testCnt);
						//updateTime();
						readAllChangeLog();
						gui.repaint();
					}

					Thread.sleep(renderTime);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public void updateTimeInst() {
		// TODO Auto-generated method stub
		
	}
}
