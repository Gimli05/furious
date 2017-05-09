package sandbox;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
/**
 * Egyetlen animacio amit egy fajlbol toltunk be es kepkockakra bontunk, poziciohoz kotunk.
 * Betoltes utan egy hatso szal cserelgeti az epp megjelenitendo kepet
 * Ezzel oldjuk meg hogy egy mozgokep tobbszor is lejatszhato legyen
 * @author Long
 *
 */

public class Animation {
	/*A blokk szereinti es a telnyleges pozicipja*/
	private int tileX;
	private int tileY;
	private int posX;
	private int posY;
	/*Animacio tipusa (Neve)*/
	private String type;

	/*Szelesseg magassag es hogy hány kepkockabol al, koztuk hany szazad az idokulonbeg*/
	private int width;
	private int height;
	private int frameCount;
	private int delay;

	/*A beolvasott kepunk es amelyi kkockat kirajzoljuk*/
	private Image img;
	private Image out;

	/*Ismetlodo kep e es hogy ha nem akkor az utolso kepkockanal megallitjuk e*/
	boolean continuous = false;
	boolean freeze = false;
	
	/*Kezdest es veget jelez*/
	boolean started = false;
	boolean ended = false;

	
	/**Tipusos konstruktor**/
	public Animation(int X, int Y, String t) {
		/*Animaciotol fuggone meghatarozuk a zedo adatokat*/
		switch (t) {
		case "Passengers":
			try {
				img = ImageIO.read(new File(GameGUI.imageURL + t + ".png"));
			} catch (IOException e) {
				e.printStackTrace();
			}

			posX = (int) (GameGUI.TILEWIDTH * 0.8);
			posY = (int) (GameGUI.TILEHEIGHT * 0.2);
			frameCount = 2;
			delay = 400;
			continuous = true;
			freeze = false;
			break;
		case "321GO":
			try {
				img = ImageIO.read(new File(GameGUI.imageURL + t + ".png"));
			} catch (IOException e) {
				e.printStackTrace();
			}

			posX = (int) (GameGUI.TILEWIDTH * 0.5);
			posY = (int) (GameGUI.TILEHEIGHT * 0.5);
			frameCount = 20;
			delay = 60;
			continuous = false;
			freeze = false;
			break;
		case "Win":
			try {
				img = ImageIO.read(new File(GameGUI.imageURL + t + ".png"));
			} catch (IOException e) {
				e.printStackTrace();
			}

			posX = (int) (GameGUI.TILEWIDTH * 0.5);
			posY = (int) (GameGUI.TILEHEIGHT * 0.5);
			frameCount = 8;
			delay = 70;
			continuous = true;
			freeze = false;
			break;
		case "Lose":
			try {
				img = ImageIO.read(new File(GameGUI.imageURL + t + ".png"));
			} catch (IOException e) {
				e.printStackTrace();
			}

			posX = (int) (GameGUI.TILEWIDTH * 0.5);
			posY = (int) (GameGUI.TILEHEIGHT * 0.5);
			frameCount = 14;
			delay = 70;
			continuous = false;
			freeze = true;
			break;
		case "Epic":
			try {
				img = ImageIO.read(new File(GameGUI.imageURL + t + ".png"));
			} catch (IOException e) {
				e.printStackTrace();
			}

			posX = (int) (GameGUI.TILEWIDTH * 0.5);
			posY = (int) (GameGUI.TILEHEIGHT * 0.5);
			frameCount = 14;
			delay = 70;
			continuous = false;
			freeze = true;
			break;

		case "Arrive":
			try {
				img = ImageIO.read(new File(GameGUI.imageURL + t + ".png"));
			} catch (IOException e) {
				e.printStackTrace();
			}

			posX = (int) (GameGUI.TILEWIDTH * 0.5);
			posY = (int) (GameGUI.TILEHEIGHT * 0.0);
			frameCount = 6;
			delay = 50;
			continuous = false;
			freeze = false;
			break;

		case "Michael":
			try {
				img = ImageIO.read(new File(GameGUI.imageURL + t + ".png"));
			} catch (IOException e) {
				e.printStackTrace();
			}

			posX = (int) (GameGUI.TILEWIDTH * 0.5);
			posY = (int) (GameGUI.TILEHEIGHT * 0.0);
			frameCount = 8;
			delay = 50;
			continuous = false;
			freeze = false;
			break;
		}
		if (img == null)

		{
			try {
				started = true;
				ended = true;
				this.finalize();
			} catch (Throwable e) {
				e.printStackTrace();
			}
			return;
		}
		/*Amit lehet kiszamounk hozza*/
		type = t;
		tileX = X;
		tileY = Y;
		width = img.getWidth(null) / frameCount;
		height = img.getHeight(null);
		posX += X * GameGUI.TILEWIDTH - width / 2;
		posY += Y * GameGUI.TILEHEIGHT - height / 2;
	}

	/**Beallitjuk a kimentere az adott indexu kepkockat**/
	private void setCurrentFrame(int i) {
		if (i < 0 || i >= frameCount)
			return;
		out = ((BufferedImage) img).getSubimage(i * width, 0, width, height);
	}

	/**Kiszamolja hogy melyik blokkokat kell majd frissitenunk a kepen, amiket az animaco eltakart**/
	public void setUpdatedTiles(boolean[][] changeMap) {
		/*Jobb also es bal felso sakrot kiszamoljuk*/
		int x0 = ((posX - width) - (posX - width) % GameGUI.TILEWIDTH) / GameGUI.TILEWIDTH;
		int y0 = ((posY - height) - (posY - height) % GameGUI.TILEHEIGHT) / GameGUI.TILEHEIGHT;
		int xn = ((posX + width) - (posX + width) % GameGUI.TILEWIDTH) / GameGUI.TILEWIDTH;
		int yn = ((posY + height) - (posY + height) % GameGUI.TILEHEIGHT) / GameGUI.TILEHEIGHT;

		/*Mindegyik, a ket pont koze seo blokkra jeloljuk hogy frissiteni kell*/
		for (int x = x0; x <= xn; x++) {
			for (int y = y0; y <= yn; y++) {
				if (x < GameGUI.BOARDWIDTH && y < GameGUI.BOARDHEIGHT && x >= 0 && y >= 0)
					changeMap[x][y] = true;
			}
		}
	}

	/**Elkezdodott e az animacio**/
	public boolean started() {
		return started;
	}

	/**Befejezeodott e az animacio***/
	public boolean ended() {
		return ended;
	}

	/**Zarjuk le az ismetles**/
	public void endLoop() {
		continuous = false;
	}

	/**Animacio inditasa es kezelese egy szallal**/
	public void start() {
		started = true;
		new Thread() {
			@Override
			public void run() {
				/*Ha folyamatos vagy meg nem ert veget akkor folytatjuk*/
				for (int i = 0; i < frameCount || continuous; i++) {
					setCurrentFrame(i);
					if (i == frameCount - 1 && continuous)
						i = -1;
						/*Amennyiben folyamatos az utolso frame utan az eso jon*/
					try {
						Thread.sleep(delay);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				while (freeze)
					/*Ha az utolso kepkocka befagy akkor amig nem jelzik ezt a kepet mutatjuk*/
					setCurrentFrame(frameCount - 1);
				ended = true;
			}
		}.start();
	}
	
	/**Hozza tartozo forrast felszabaditunk**/
	public void clear() throws Throwable {
		this.finalize();
	}

	/**Megallitjuk a fagyasztast**/
	public void endFreeze() {
		freeze = false;
	}

	/**X Blokk-koordinataa lekerse**/
	public int getX() {
		return tileX;
	}
	/**Y Blokk-koordinataa lekerse**/
	public int getY() {
		return tileY;
	}

	/**Lekerdezzuk a tipusat**/
	public String getType() {
		return type;
	}

	/**Kirajzoljuk a megadott vaszonra**/
	public void draw(Graphics g) {

		((Graphics2D) g).setComposite(AlphaComposite.SrcOver.derive(1.0f));

		if (out != null)
			g.drawImage(out, posX, posY, null);
	}
}
