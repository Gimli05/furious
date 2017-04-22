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

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GUI extends JPanel {
	private static final long serialVersionUID = 1L;
	private static JFrame frame;
	private static GUI gui;
	private static GameController gc;

	private static String FRAMENAME = "Michael Bay's TRAINZ";
	private static int FRAMEWIDTH;
	private static int FRAMEHEIGHT;
	private static int TILEWIDTH = 40;
	private static int TILEHEIGHT = 40;
	private static int BOARDWIDTH;
	private static int BOARDHEIGHT;

	private static String imageURL = "raw/";
	private static Thread renderThread;

	private static boolean changeMap[][];
	private static String clickLog = "";
	private static Tile baseTileMap[][];
	private static Tile detailsTileMap[][];

	private static MouseListener MyMouseListener;

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
	}

	@Override
	public void paintComponent(Graphics g) {
		for (int x = 0; x < BOARDWIDTH; x++) {
			for (int y = 0; y < BOARDHEIGHT; y++) {
				if (baseTileMap[x][y] != null && changeMap[x][y]) {
					g.drawImage(baseTileMap[x][y].img, x * TILEWIDTH, y * TILEHEIGHT, null);
					changeMap[x][y] = false;
				}
			}
		}
	}

	public void startRender() {
		renderThread = new Thread() {
			@Override
			public void run() {

				while (true) {
					try {
						if (gui != null)
							gui.repaint();
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
		buildBaseMap();
		renderThread.start();
	}

	public void setBaseTileMap(int x, int y, String object) {
		baseTileMap[x][y] = new Tile(object);
		changeMap[x][y] = true;
	}

	public void buildBaseMap() {
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

	public static void writeClickLog(int x, int y, boolean btn) {
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
						System.out.println(type + variant + active + state);
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

}
