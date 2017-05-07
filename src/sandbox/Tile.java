package sandbox;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class Tile {
	private Image img;
	private String type;
	private boolean state;
	private boolean active;
	private int variant;

	public Tile(String t) {
		if(t.equals("4"))t="1";
		if(t.equals("5"))t="2";
		if(t.equals("6"))t="3";
		type = t.toString().trim().substring(0, 1);
		variant = 1;
		active = false;
		state = false;

		if (type.contains("S")) {
			img = loadImage(type + "0").getImage();
		} else if (type.contains("U")) {
			img = loadImage(type + variant + "0" + "0").getImage();
		} else
			img = loadImage(type).getImage();

	}

	public void rotation(double d) {
		img = rotateImage(img, d);
	}

	public void setType(String t) {
		type = t.toString().trim().substring(0, 1);
		img = loadImage(type).getImage();
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

	public Image getImage() {
		return img;
	}

	public void activate(Tile[][] baseTileMap, int x, int y) {
		active = true;
		state=false;
		img = loadImage(type + variant + (active == false ? 0 : 1) + (state == false ? 0 : 1)).getImage();
		setTileAngle(baseTileMap, x, y);
	}

	public void deactivate(Tile[][] baseTileMap, int x, int y) {
		active = false;
		state=false;
		img = loadImage(type + variant + (active == false ? 0 : 1) + (state == false ? 0 : 1)).getImage();
		setTileAngle(baseTileMap, x, y);
	}

	public void switchState(Tile[][] baseTileMap, int x, int y) {
		if(type.equals("1") ||type.equals("2")|| type.equals("3")){
			
			
		}else if (type.equals("U") && active) {
			state = !state;
			img = loadImage(type + variant + (active == false ? 0 : 1) + (state == false ? 0 : 1)).getImage();
			setTileAngle(baseTileMap, x, y);

		} else if (type.equals("S")) {
			state = !state;
			img = loadImage(type + (state == false ? 0 : 1)).getImage();
			setTileAngle(baseTileMap, x, y);
		}
	}

	public void updateImage() {
		if (type.equals("U"))
			img = loadImage(type + variant + (active == false ? 0 : 1) + (state == false ? 0 : 1)).getImage();
		else
			img = loadImage(type + (state == false ? 0 : 1)).getImage();
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

	private ImageIcon loadImage(String object) {
		switch (object) {
		case ("x"):
			return new ImageIcon(GameGUI.imageURL + "Grass.png");
		case ("R"):
			return new ImageIcon(GameGUI.imageURL + "Rail.png");
		case ("X"):
			return new ImageIcon(GameGUI.imageURL + "Xrail.png");
		case ("B"):
			return new ImageIcon(GameGUI.imageURL + "BRail.png");
		case ("S0"):
			return new ImageIcon(GameGUI.imageURL + "Switch0.png");
		case ("S1"):
			return new ImageIcon(GameGUI.imageURL + "Switch1.png");
		case ("U000"):
			return new ImageIcon(GameGUI.imageURL + "Tunnel000.png");
		case ("U010"):
			return new ImageIcon(GameGUI.imageURL + "Tunnel010.png");
		case ("U011"):
			return new ImageIcon(GameGUI.imageURL + "Tunnel011.png");
		case ("U100"):
			return new ImageIcon(GameGUI.imageURL + "Tunnel100.png");
		case ("U110"):
			return new ImageIcon(GameGUI.imageURL + "Tunnel110.png");
		case ("U111"):
			return new ImageIcon(GameGUI.imageURL + "Tunnel111.png");
		case ("U200"):
			return new ImageIcon(GameGUI.imageURL + "Tunnel200.png");
		case ("U210"):
			return new ImageIcon(GameGUI.imageURL + "Tunnel210.png");
		case ("U211"):
			return new ImageIcon(GameGUI.imageURL + "Tunnel211.png");
		case ("E"):
			return new ImageIcon(GameGUI.imageURL + "EnterPoint.png");
		case ("1"):
			return new ImageIcon(GameGUI.imageURL + "Station1.png");
		case ("2"):
			return new ImageIcon(GameGUI.imageURL + "Station2.png");
		case ("3"):
			return new ImageIcon(GameGUI.imageURL + "Station3.png");
		default:
			return new ImageIcon(GameGUI.imageURL + "Grass.png");
		}
	}

	public void setTileAngle(Tile[][] baseTileMap, int x, int y) {
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
				baseTileMap[x][y].rotation(90);
			} else if (x == GameGUI.BOARDWIDTH - 1) {
				baseTileMap[x][y].rotation(-90);
			} else {
				if (y == 0) {
					baseTileMap[x][y].rotation(180);
				} else if (y == GameGUI.BOARDHEIGHT - 1) {
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
			setTunnelVariantAndAngle(baseTileMap, x, y);
			break;
		default:
			break;
		}
	}

	public void markIfCorner(Tile[][] baseTileMap, int x, int y) {
		if (!baseTileMap[x][y].getType().equals("R"))
			return;
		if (x == 0 || x == GameGUI.BOARDWIDTH - 1) {
			if (y == 0) {
				baseTileMap[x][y].setType("B");
			} else if (y == GameGUI.BOARDHEIGHT - 1) {
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
			} else if (y == GameGUI.BOARDHEIGHT - 1) {
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

	public void setTunnelVariantAndAngle(Tile[][] baseTileMap, int x, int y) {
		if (x == 0) {
			baseTileMap[x][y].rotation(-90);
		} else if (x == GameGUI.BOARDWIDTH - 1) {
			baseTileMap[x][y].rotation(90);
		} else {
			if (y == 0) {
				baseTileMap[x][y].rotation(180);
			} else if (y == GameGUI.BOARDHEIGHT - 1) {
				baseTileMap[x][y].rotation(0);
			} else {
				
				// Bal
				
				
				// Szemben
				if (!baseTileMap[x][y - 1].getType().equals("x") && !baseTileMap[x][y + 1].getType().equals("x")) {
					baseTileMap[x][y].setVariant(0);
					baseTileMap[x][y].updateImage();
					baseTileMap[x][y].rotation(90);
				}
				if (!baseTileMap[x - 1][y].getType().equals("x") && !baseTileMap[x + 1][y].getType().equals("x")) {
					baseTileMap[x][y].setVariant(0);
					baseTileMap[x][y].updateImage();
					baseTileMap[x][y].rotation(0);
				}
				// Jobb

				
				if (!baseTileMap[x + 1][y].getType().equals("x") && !baseTileMap[x][y + 1].getType().equals("x")) {
					baseTileMap[x][y].setVariant(1);
					baseTileMap[x][y].updateImage();
					baseTileMap[x][y].rotation(180);
				}				

				if (!baseTileMap[x - 1][y].getType().equals("x") && !baseTileMap[x][y - 1].getType().equals("x")) {
					baseTileMap[x][y].setVariant(1);
					baseTileMap[x][y].updateImage();
					baseTileMap[x][y].rotation(0);
				}
				
				// Bal
				if (!baseTileMap[x + 1][y].getType().equals("x") && !baseTileMap[x][y - 1].getType().equals("x")) {
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

}
