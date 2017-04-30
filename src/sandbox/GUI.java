package sandbox;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GUI extends JPanel {
	private static final long serialVersionUID = 1L;
	private static JFrame frame;
	private static GUI gui;

	private static String FRAMENAME = "Michael Bay's TRAINZ";
	private static int FRAMEWIDTH;
	private static int FRAMEHEIGHT;
	private static int TILEWIDTH = 40;
	private static int TILEHEIGHT = 40;
	private static int BOARDWIDTH;
	private static int BOARDHEIGHT;

	private static int TILEINTERVAL = 1000;
	private static int TRAINBLOCKWIDTH = 40;
	private static int TRAINBLOCKHEIGHT = 40;

	private static String imageURL = "raw/";
	private static Thread renderThread;
	private static long renderTime = 20;
	private static int time;
	private static int hopTime = 100;

	private static int tilePositionCount = (TILEINTERVAL / hopTime) - 1;

	private static boolean changeMap[][];
	private static String clickLog = "";
	private static Tile baseTileMap[][];

	private static TrainBlock testBlock = new TrainBlock("E");
	private static ArrayList<String> testColors;
	//private static TrainView testTrain;

	private static MouseListener MyMouseListener;

	// Test
	private static int testCnt = 0;
	private static int testNX = 0;
	private static int testNY = 0;
	private static int fix = 0;

	public GUI() {
		baseTileMap = new Tile[BOARDWIDTH][BOARDHEIGHT];
		changeMap = new boolean[BOARDWIDTH][BOARDHEIGHT];
		for (int i = 0; i < BOARDWIDTH; i++)
			for (int j = 0; j < BOARDHEIGHT; j++) {
				changeMap[i][j] = false;
				baseTileMap[i][j] = null;
			}
	}

	public void init(int width, int height) {
		BOARDWIDTH = width;
		BOARDHEIGHT = height;
		FRAMEWIDTH = BOARDWIDTH * TILEWIDTH;
		FRAMEHEIGHT = BOARDHEIGHT * TILEHEIGHT;

		time = 0;

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
		
		//LONG TESZTEL
		testColors=new ArrayList<String>();
		testColors.add("E");
		testColors.add("R");
		testColors.add("G");
		testColors.add("B");
		
		//testTrain=new TrainView(testColors);
	}

	@Override
	public void paintComponent(Graphics g) {
		// BaseDraw

		// RANDOM FIX
		if (fix < 2) {
			changeMap[0][0] = true;
			fix++;
		}

		for (int x = 0; x < BOARDWIDTH; x++) {
			for (int y = 0; y < BOARDHEIGHT; y++) {
				if (baseTileMap[x][y] != null && changeMap[x][y]) {
					g.drawImage(baseTileMap[x][y].img, x * TILEWIDTH, y * TILEHEIGHT, null);
				}
			}
		}
		testBlock.draw(g);
		//testTrain.draw(g);
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

	private void buildBaseMap() {
		for (int y = 0; y < BOARDHEIGHT; y++) {
			for (int x = 0; x < BOARDWIDTH; x++) {

				markIfCorner(x, y);
				setTileAngle(x, y);
			}
		}
	}

	private void markIfCorner(int x, int y) {
		if (!baseTileMap[x][y].getType().equals("R"))
			return;
		if (x == 0 || x == BOARDWIDTH - 1) {
			if (y == 0) {
				baseTileMap[x][y].setType("B");
			} else if (y == BOARDHEIGHT - 1) {
				baseTileMap[x][y].setType("B");
			} else {
				if ((baseTileMap[x][y - 1].getType().equals("x") && !baseTileMap[x][y + 1].getType().equals("x"))
						|| (baseTileMap[x][y + 1].getType().equals("x")
								&& !baseTileMap[x][y - 1].getType().equals("x"))) {
					baseTileMap[x][y].setType("B");
				}
			}
		} else {
			if (y == 0) {
				if (!baseTileMap[x][y + 1].getType().equals("x")
						|| (baseTileMap[x + 1][y].getType().equals("x") && !baseTileMap[x - 1][y].getType().equals("x"))
						|| (baseTileMap[x - 1][y].getType().equals("x")
								&& !baseTileMap[x + 1][y].getType().equals("x"))) {
					baseTileMap[x][y].setType("B");
				}
			} else if (y == BOARDHEIGHT - 1) {
				if (!baseTileMap[x][y - 1].getType().equals("x")
						|| (baseTileMap[x + 1][y].getType().equals("x") && !baseTileMap[x - 1][y].getType().equals("x"))
						|| (baseTileMap[x - 1][y].getType().equals("x")
								&& !baseTileMap[x + 1][y].getType().equals("x"))) {
					baseTileMap[x][y].setType("B");
				}
			} else {
				if ((baseTileMap[x][y - 1].getType().equals("x") && !baseTileMap[x][y + 1].getType().equals("x"))
						|| (baseTileMap[x][y + 1].getType().equals("x") && !baseTileMap[x][y - 1].getType().equals("x"))
						|| (baseTileMap[x + 1][y].getType().equals("x") && !baseTileMap[x - 1][y].getType().equals("x"))
						|| (baseTileMap[x - 1][y].getType().equals("x")
								&& !baseTileMap[x + 1][y].getType().equals("x"))) {
					baseTileMap[x][y].setType("B");
				}
			}
		}
	}

	private static void setTunnelVariantAndAngle(int x, int y) {
		if (x == 0) {
			baseTileMap[x][y].rotation(-90);
		} else if (x == BOARDWIDTH - 1) {
			baseTileMap[x][y].rotation(90);
		} else {
			if (y == 0) {
				baseTileMap[x][y].rotation(180);
			} else if (y == BOARDHEIGHT - 1) {
				baseTileMap[x][y].rotation(0);
			} else {
				// Szemben
				if (!baseTileMap[x][y - 1].getType().equals("x") && !baseTileMap[x][y + 1].getType().equals("x")) {
					baseTileMap[x][y].setVariant(0);
					baseTileMap[x][y].rotation(90);
				}
				if (!baseTileMap[x - 1][y].getType().equals("x") && !baseTileMap[x + 1][y].getType().equals("x")) {
					baseTileMap[x][y].setVariant(0);
					baseTileMap[x][y].rotation(0);
				}
				// Jobb

				if (!baseTileMap[x + 1][y].getType().equals("x") && !baseTileMap[x][y - 1].getType().equals("x")) {
					baseTileMap[x][y].setVariant(2);
					baseTileMap[x][y].updateImage();
					baseTileMap[x][y].rotation(0);
				}
				if (!baseTileMap[x + 1][y].getType().equals("x") && !baseTileMap[x][y + 1].getType().equals("x")) {
					baseTileMap[x][y].setVariant(2);
					baseTileMap[x][y].updateImage();
					baseTileMap[x][y].rotation(180);
				}
				// Bal

				if (!baseTileMap[x - 1][y].getType().equals("x") && !baseTileMap[x][y - 1].getType().equals("x")) {
					baseTileMap[x][y].setVariant(2);
					baseTileMap[x][y].updateImage();
					baseTileMap[x][y].rotation(0);
				}
				if (!baseTileMap[x - 1][y].getType().equals("x") && !baseTileMap[x][y + 1].getType().equals("x")) {
					baseTileMap[x][y].setVariant(2);
					baseTileMap[x][y].updateImage();
					baseTileMap[x][y].rotation(180);
				}

			}
		}
	}

	private static Image rotateImage(Image img, double angle) {
		BufferedImage rotateImage = null;
		try {
			BufferedImage bufferedImage = new BufferedImage(img.getWidth(null), img.getHeight(null),
					BufferedImage.TYPE_INT_RGB);
			Graphics g = bufferedImage.createGraphics();
			g.drawImage(img, 0, 0, null);
			g.dispose();
			ImageIO.write(bufferedImage, "png", new File("a.png"));

			rotateImage = new BufferedImage(img.getHeight(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
			AffineTransform a90 = AffineTransform.getRotateInstance(Math.toRadians(angle), img.getHeight(null) / 2,
					img.getWidth(null) / 2);
			AffineTransformOp op90 = new AffineTransformOp(a90, AffineTransformOp.TYPE_BILINEAR);
			op90.filter(bufferedImage, rotateImage);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rotateImage;
	}

	private static void setTileAngle(int x, int y) {
		switch (baseTileMap[x][y].getType()) {
		case "R":
			if (y > 0) {
				if (!baseTileMap[x][y - 1].getType().equals("x"))
					baseTileMap[x][y].rotation(90);
			} else {
				if (!baseTileMap[x][y + 1].getType().equals("x"))
					baseTileMap[x][y].rotation(90);
			}
			break;
		case "1":
			if (y > 0) {
				if (!baseTileMap[x][y - 1].getType().equals("x"))
					baseTileMap[x][y].rotation(90);
			} else {
				if (!baseTileMap[x][y + 1].getType().equals("x"))
					baseTileMap[x][y].rotation(90);
			}
			break;
		case "2":
			if (y > 0) {
				if (!baseTileMap[x][y - 1].getType().equals("x"))
					baseTileMap[x][y].rotation(90);
			} else {
				if (!baseTileMap[x][y + 1].getType().equals("x"))
					baseTileMap[x][y].rotation(90);
			}
			break;
		case "3":
			if (y > 0) {
				if (!baseTileMap[x][y - 1].getType().equals("x"))
					baseTileMap[x][y].rotation(90);
			} else {
				if (!baseTileMap[x][y + 1].getType().equals("x"))
					baseTileMap[x][y].rotation(90);
			}
			break;
		case "X":
			break;

		case "S":
			if (x == 0) {
				baseTileMap[x][y].rotation(-90);
			} else if (x == BOARDWIDTH - 1) {
				baseTileMap[x][y].rotation(90);
			} else {
				if (y == 0) {
					baseTileMap[x][y].rotation(180);
				} else if (y == BOARDHEIGHT - 1) {
					baseTileMap[x][y].rotation(0);
				} else {
					if (baseTileMap[x][y - 1].getType().equals("x"))
						baseTileMap[x][y].rotation(180);
					if (baseTileMap[x][y + 1].getType().equals("x"))
						baseTileMap[x][y].rotation(0);
					if (baseTileMap[x - 1][y].getType().equals("x"))
						baseTileMap[x][y].rotation(90);
					if (baseTileMap[x + 1][y].getType().equals("x"))
						baseTileMap[x][y].rotation(-90);
				}
			}
			break;

		case "B":
			if (x > 0) {
				if (y > 0) {
					if (baseTileMap[x][y - 1].getType().equals("x") && baseTileMap[x - 1][y].getType().equals("x"))
						baseTileMap[x][y].rotation(180);
					if (!(baseTileMap[x][y - 1].getType().equals("x")) && baseTileMap[x - 1][y].getType().equals("x"))
						baseTileMap[x][y].rotation(90);
					if (baseTileMap[x][y - 1].getType().equals("x") && !(baseTileMap[x - 1][y].getType().equals("x")))
						baseTileMap[x][y].rotation(-90);
					if (!(baseTileMap[x][y - 1].getType().equals("x"))
							&& !(baseTileMap[x - 1][y].getType().equals("x")))
						baseTileMap[x][y].rotation(0);
				} else {
					if (baseTileMap[x][y + 1].getType().equals("x") && baseTileMap[x - 1][y].getType().equals("x"))
						baseTileMap[x][y].rotation(90);
					if (!(baseTileMap[x][y + 1].getType().equals("x")) && baseTileMap[x - 1][y].getType().equals("x"))
						baseTileMap[x][y].rotation(180);
					if (baseTileMap[x][y + 1].getType().equals("x") && !(baseTileMap[x - 1][y].getType().equals("x")))
						baseTileMap[x][y].rotation(0);
					if (!(baseTileMap[x][y + 1].getType().equals("x"))
							&& !(baseTileMap[x - 1][y].getType().equals("x")))
						baseTileMap[x][y].rotation(-90);
				}
			} else {
				if (y > 0) {
					if (baseTileMap[x][y - 1].getType().equals("x") && baseTileMap[x + 1][y].getType().equals("x"))
						baseTileMap[x][y].rotation(-90);
					if (!(baseTileMap[x][y - 1].getType().equals("x")) && baseTileMap[x + 1][y].getType().equals("x"))
						baseTileMap[x][y].rotation(0);
					if (baseTileMap[x][y - 1].getType().equals("x") && !(baseTileMap[x + 1][y].getType().equals("x")))
						baseTileMap[x][y].rotation(180);
					if (!(baseTileMap[x][y - 1].getType().equals("x"))
							&& !(baseTileMap[x + 1][y].getType().equals("x")))
						baseTileMap[x][y].rotation(90);
				} else {
					if (baseTileMap[x][y + 1].getType().equals("x") && baseTileMap[x + 1][y].getType().equals("x"))
						baseTileMap[x][y].rotation(0);
					if (!(baseTileMap[x][y + 1].getType().equals("x")) && baseTileMap[x + 1][y].getType().equals("x"))
						baseTileMap[x][y].rotation(90);
					if (baseTileMap[x][y + 1].getType().equals("x") && !(baseTileMap[x + 1][y].getType().equals("x")))
						baseTileMap[x][y].rotation(-90);
					if (!(baseTileMap[x][y + 1].getType().equals("x"))
							&& !(baseTileMap[x + 1][y].getType().equals("x")))
						baseTileMap[x][y].rotation(180);
				}
			}
			break;
		case "U":
			setTunnelVariantAndAngle(x, y);
			break;
		default:
			break;
		}
	}

	private static void callTileClick(int x, int y, boolean btn) {
		baseTileMap[x][y].switchState(x, y, btn);
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
		}else if (cnt == 17) {
			testNX = 0;
			testNY = 5;
		}
		else if (cnt == 18) {
			testNX = 0;
			testNY = 6;
		}
		testBlock.updatePos(testNX, testNY);
		//testTrain.updatePos(testNX, testNY);
	}

	public static class Tile {
		private Image img;
		private String type;
		private boolean interactive;
		private boolean state;
		private boolean active;
		private int variant;

		public Tile(String t) {
			type = t.toString().trim().substring(0, 1);
			variant = 0;
			active = false;
			state = false;
			interactive = false;

			if (type.contains("S")) {
				interactive = true;
				img = getImage(type + "0").getImage();
			} else if (type.contains("U")) {
				interactive = true;
				img = getImage(type + variant + "0" + "0").getImage();
			} else
				img = getImage(type).getImage();

		}

		public void rotation(double d) {
			img = rotateImage(img, d);
		}

		public void setType(String t) {
			type = t.toString().trim().substring(0, 1);
			img = getImage(type).getImage();
			state = false;
			active = false;
		}

		public void setVariant(int v) {
			variant = v;
		}

		public int getVariant() {
			return variant;
		}

		public String getType() {
			return type;
		}

		public void switchState(int x, int y, boolean btn) {
			if (interactive) {
				if (!btn && type.equals("U")) {
					if (!active) {
						active = true;
						changeMap[x][y] = true;
						img = getImage(type + variant + (active == false ? 0 : 1) + (state == false ? 0 : 1))
								.getImage();
						setTileAngle(x, y);

					} else {
						state = !state;
						changeMap[x][y] = true;
						img = getImage(type + variant + (active == false ? 0 : 1) + (state == false ? 0 : 1))
								.getImage();
						setTileAngle(x, y);
					}

				} else if (btn && type.equals("U")) {
					if (active) {
						state = false;
						active = !active;
						changeMap[x][y] = true;
						img = getImage(type + variant + (active == false ? 0 : 1) + (state == false ? 0 : 1))
								.getImage();
						setTileAngle(x, y);
					}
				} else {
					if (!btn) {
						state = !state;
						changeMap[x][y] = true;
						img = getImage(type + (state == false ? 0 : 1)).getImage();
						setTileAngle(x, y);
					}
				}
			}
		}

		public void updateImage() {
			if (type.equals("U"))
				img = getImage(type + variant + (active == false ? 0 : 1) + (state == false ? 0 : 1)).getImage();
			else
				img = getImage(type + (state == false ? 0 : 1)).getImage();
		}

		private ImageIcon getImage(String object) {
			switch (object) {
			case ("x"):
				return new ImageIcon(imageURL + "Grass.png");
			case ("R"):
				return new ImageIcon(imageURL + "Rail.png");
			case ("X"):
				return new ImageIcon(imageURL + "Xrail.png");
			case ("B"):
				return new ImageIcon(imageURL + "BRail.png");
			case ("S0"):
				return new ImageIcon(imageURL + "Switch0.png");
			case ("S1"):
				return new ImageIcon(imageURL + "Switch1.png");
			case ("U000"):
				return new ImageIcon(imageURL + "Tunnel000.png");
			case ("U010"):
				return new ImageIcon(imageURL + "Tunnel010.png");
			case ("U011"):
				return new ImageIcon(imageURL + "Tunnel011.png");
			case ("U100"):
				return new ImageIcon(imageURL + "Tunnel100.png");
			case ("U110"):
				return new ImageIcon(imageURL + "Tunnel110.png");
			case ("U111"):
				return new ImageIcon(imageURL + "Tunnel111.png");
			case ("U200"):
				return new ImageIcon(imageURL + "Tunnel200.png");
			case ("U210"):
				return new ImageIcon(imageURL + "Tunnel210.png");
			case ("U211"):
				return new ImageIcon(imageURL + "Tunnel211.png");
			case ("E"):
				return new ImageIcon(imageURL + "EnterPoint.png");
			case ("1"):
				return new ImageIcon(imageURL + "Station1.png");
			case ("2"):
				return new ImageIcon(imageURL + "Station2.png");
			case ("3"):
				return new ImageIcon(imageURL + "Station3.png");
			default:
				return new ImageIcon(imageURL + "Grass.png");
			}
		}
	}

	// Train

	private static void hopTrains() {
		time = (time + hopTime) % TILEINTERVAL;
		testBlock.updateTime(time);
		//testTrain.updateTime(time);
	}

	public static class TrainBlock {
		public String type;
		// Elözö indexek
		public int currentMapX, currentMapY;
		public int lastMapX, lastMapY;
		public int nextMapX, nextMapY;
		// Konkrét hely
		public int posX, posY;

		// Forgatási adatok
		public boolean isTurning;
		public double angle;
		public double startAngle;
		int rotateDir;
		double circleStartAngle;
		int correctedX, correctedY;
		
		// Raw Data
		public Image img;
		
		
		public TrainBlock(String t) {
			type = t;
			// UI Block & Position Update
			lastMapX = -1;
			lastMapY = -1;
			currentMapX = -1;
			currentMapY = -1;
			nextMapX = -1;
			nextMapY = -1;

			// Position Calculation
			posX = -1;
			posY = -1;
			rotateDir = 0;
			angle = 90;
			isTurning = false;

			// Raw Data
			switch (type) {
			case "E":
				img = new ImageIcon(imageURL + "Engine.png").getImage();
				break;
			case "R":
				img = new ImageIcon(imageURL + "RCab.png").getImage();
				break;
			case "G":
				img = new ImageIcon(imageURL + "GCab.png").getImage();
				break;
			case "B":
				img = new ImageIcon(imageURL + "BCab.png").getImage();
				break;
			}
		}
		
		public void setPos(int lastX, int lastY,int currentX, int currentY, int nextX, int nextY){
			lastMapX=lastX;
			lastMapY=lastY;
			currentMapX=currentX;
			currentMapY=currentY;
			nextMapX=nextX;
			nextMapY=nextY;
		}
		
		public void setAngle(double a){
			angle=a;
		}
		
		public void updatePos(int nextX, int nextY) {
			// Update ha nem csak idöben van változás
			if (nextMapX != nextX || nextMapY != nextY) {
				// Blokk poziciok
				lastMapX = currentMapX;
				lastMapY = currentMapY;
				currentMapX = nextMapX;
				currentMapY = nextMapY;
				nextMapX = nextX;
				nextMapY = nextY;
				
				//Forgatás korrekcio
				if(angle>=-45 && angle<45)angle=0;
				else if(angle>=45 && angle<135)angle=90;			
				else if(angle>=135 && angle<225)angle=180;		
				else if(angle>=225 && angle<315)angle=270;		
				else if(angle>=315 && angle<405)angle=360;
				
				System.out.println("AC:"+angle);
				
				// Forgatás adatai
				rotateDir = 1;
				circleStartAngle = 0;

				// Irányszámítás

				// Nem kanyarodik, mert két koordináta egyezik...
				if (lastMapX == nextMapX || lastMapY == nextMapY) {
					// Elfordulási szög nem változik.. csak léptetünk.
					isTurning = false;
					// Nincs dolgunk..
				} else {
					isTurning = true;
					// Itt fordulunk
					// Esetekre bontjuk egy elöre átbeszelt táblázat alapján,
					// forgatási iránytol függöen

					if (nextMapX < lastMapX && nextMapY < lastMapY) {
						// Megnézzük hogy hol van a forgatás középpontja
						// (jelenlegi blokk felett vagy alatt)
						if (currentMapY > nextMapY) {
							System.out.println("A-");
							rotateDir = 1;
							circleStartAngle = 90;
							correctedX = 1;
							correctedY = 0;
						} else {
							System.out.println("A+");
							rotateDir = -1;
							circleStartAngle = 360;
							correctedX = 0;
							correctedY = 1;
						}

					} else if (nextMapX > lastMapX && nextMapY < lastMapY) {
						if (currentMapY > nextMapY) {
							System.out.println("B+");
							rotateDir = -1;
							circleStartAngle = 90;
							correctedX = 0;
							correctedY = 0;
						} else {
							System.out.println("B-");
							rotateDir = 1;
							circleStartAngle = 180;
							correctedX = 1;
							correctedY = 1;
						}

					} else if (nextMapX < lastMapX && nextMapY > lastMapY) {
						if (currentMapX > nextMapX) {
							System.out.println("C-");
							rotateDir = 1;
							circleStartAngle = 0;
							correctedX = 0;
							correctedY = 0;
						} else {
							System.out.println("C+");
							rotateDir = -1;
							circleStartAngle = 270;
							correctedX = 1;
							correctedY = 1;
						}

					} else if (nextMapX > lastMapX && nextMapY > lastMapY) {
						if (currentMapX < nextMapX) {
							System.out.println("D+");
							rotateDir = -1;
							circleStartAngle = 180;
							correctedX = 1;
							correctedY = 0;
						} else {
							System.out.println("D-");
							rotateDir = 1;
							circleStartAngle = 270;
							correctedX = 0;
							correctedY = 1;
						}
					}
					startAngle=angle;
				}
				// Megvan a forgatási irány és a középpontja, már csak el kell
				// forgatni az idö függvenyeben
			}
		}
		
		public void updateTime(int currentTime){
			double percent = ((double) currentTime / TILEINTERVAL);
			// Idöfüggö számítás
			if (!isTurning) {
				int direction = (angle == 180 || angle == 270) ? -1 : 1;
				// Az irány nem változik
				if (angle == 90) {
					// Függölegesen halad
					posX = (int) Math.floor((currentMapX) * TILEWIDTH + TILEWIDTH / 2);
					posY = (int) Math.floor((currentMapY) * TILEHEIGHT + direction * TILEHEIGHT * percent);
				} else if (angle == 270) {
					// Függölegesen halad
					posX = (int) Math.floor((currentMapX) * TILEWIDTH + TILEWIDTH / 2);
					posY = (int) Math.floor((currentMapY + 1) * TILEHEIGHT + direction * TILEHEIGHT * percent);
				} else if (angle == 180) {
					// Vizszintesen halad
					posX = (int) Math.floor((currentMapX + 1) * TILEWIDTH + direction * percent * TILEWIDTH);
					posY = (int) Math.floor((currentMapY) * TILEHEIGHT + TILEHEIGHT / 2);
				} else if (angle == 0) {
					posX = (int) Math.floor((currentMapX) * TILEWIDTH + direction * percent * TILEWIDTH);
					posY = (int) Math.floor((currentMapY) * TILEHEIGHT + TILEHEIGHT / 2);
				}
			} else {
				// Fordulunk
				// Forgatás közepe és iránya meg van adva, csak forgatunk...
				// Kép forgatás
				// Kép uj irányának számolása
				double delta = rotateDir * 90 * percent;
				angle = startAngle + delta;

				// Intervalluma foglalás
				if (angle < 0)
					angle += 360;
				if (angle > 360)
					angle -= 360;
				
				
				// Pozicio számitás
				// Blokkonbelüli forgatás utáni pozicio
				// Origo középpontu kör + eltolás;
				double range = TILEWIDTH / 2;
				int circleX = (int) (range * Math.cos(Math.toRadians(circleStartAngle + delta)));
				int circleY = (int) (range * Math.sin(Math.toRadians(circleStartAngle + delta)));

				posX = (int) Math.floor((currentMapX + correctedX) * TILEWIDTH + circleX);
				posY = (int) Math.floor((currentMapY + correctedY) * TILEHEIGHT + circleY);

			}

			// Update

			if (currentMapX >= 0 && currentMapY >= 0) {
				changeMap[currentMapX][currentMapY] = true;

				if (lastMapX >= 0 && lastMapY >= 0) {
					changeMap[lastMapX][lastMapY] = true;
				}
			}
		}
		
		public void draw(Graphics g) {
			AffineTransform trans = new AffineTransform();
			trans.translate(posX - TRAINBLOCKWIDTH / 2, posY - TRAINBLOCKHEIGHT / 2);
			trans.rotate(Math.toRadians(angle), TRAINBLOCKWIDTH / 2, TRAINBLOCKHEIGHT / 2);
			Graphics2D g2d = (Graphics2D) g;
			g2d.drawImage(img, trans, null);
		}
		
		public int getNextPosX(){
			return nextMapX;
		}
		
		public int getNextPosY(){
			return nextMapY;
		}
	}
	

	public static class TrainView {
		ArrayList<TrainBlock> train;

		public TrainView(ArrayList<String> list) {
			train=new ArrayList<TrainBlock>();
			for(String type:list){
				train.add(new TrainBlock(type));
			}
		}
	
		public void setAngle(double a){
			train.get(0).setAngle(a);
		}
		
		public void setPos(int lastX, int lastY,int currentX, int currentY, int nextX, int nextY){
			train.get(0).setPos(lastX,lastY,currentX,currentY,nextX,nextY);
		}
			
		public void updatePos(int nextX, int nextY){
			for(int i=train.size()-1;i>0;i--){
				TrainBlock next = train.get(i-1);
				train.get(i).updatePos(next.getNextPosX(), next.getNextPosY());
			}
			train.get(0).updatePos(nextX, nextY);
		}
		
		public void updateTime(int currentTime){
			for(TrainBlock block:train){
				block.updateTime(currentTime);
			}
		}
		
		public void draw(Graphics g){
			for(TrainBlock block:train){
				block.draw(g);
			}
		}
	}

	// Threads
	
	private static class mainRenderThread extends Thread {
		@Override
		public void run() {
			int cntr = 0;
			//testTrain.setPos(0,0,1,0,1,1);
			//testTrain.setAngle(0);
			while (true) {
				try {
					if (gui != null) {
						if (cntr == tilePositionCount) {
							cntr = 0;
							testCnt++;
						} else {
							cntr++;
						}
						doTurnTest(testCnt);
						hopTrains();
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
}
