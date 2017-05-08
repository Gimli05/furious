package sandbox;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Animation {
	private int tileX;
	private int tileY;
	private int posX;
	private int posY;
	private String type;

	private int width;
	private int height;
	private int frameCount;
	private int delay;

	private Image img;
	private Image out;

	boolean continuous = false;
	boolean freeze = false;
	boolean started = false;
	boolean ended = false;

	public Animation(int X, int Y, String t) {
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
		// Kép megvan
		type = t;
		tileX = X;
		tileY = Y;
		width = img.getWidth(null) / frameCount;
		height = img.getHeight(null);
		posX += X * GameGUI.TILEWIDTH - width / 2;
		posY += Y * GameGUI.TILEHEIGHT - height / 2;
	}

	private void setCurrentFrame(int i) {
		if (i < 0 || i >= frameCount)
			return;
		out = ((BufferedImage) img).getSubimage(i * width, 0, width, height);
	}

	public void setUpdatedTiles(boolean[][] changeMap) {

		int x0 = ((posX - width) - (posX - width) % GameGUI.TILEWIDTH) / GameGUI.TILEWIDTH;
		int y0 = ((posY - height) - (posY - height) % GameGUI.TILEHEIGHT) / GameGUI.TILEHEIGHT;
		int xn = ((posX + width) - (posX + width) % GameGUI.TILEWIDTH) / GameGUI.TILEWIDTH;
		int yn = ((posY + height) - (posY + height) % GameGUI.TILEHEIGHT) / GameGUI.TILEHEIGHT;

		for (int x = x0; x <= xn; x++) {
			for (int y = y0; y <= yn; y++) {
				if (x < GameGUI.BOARDWIDTH && y < GameGUI.BOARDHEIGHT && x >= 0 && y >= 0)
					changeMap[x][y] = true;
			}
		}
	}

	public boolean started() {
		return started;
	}

	public boolean ended() {
		return ended;
	}

	public void endLoop() {
		continuous = false;
	}

	public void start() {
		started = true;
		new Thread() {
			@Override
			public void run() {
				for (int i = 0; i < frameCount || continuous; i++) {
					setCurrentFrame(i);
					if (i == frameCount - 1 && continuous)
						i = -1; // Loop

					try {
						Thread.sleep(delay);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				while (freeze)
					setCurrentFrame(frameCount - 1);
				ended = true;
			}
		}.start();
	}

	public void clear() throws Throwable {
		this.finalize();
	}

	public void endFreeze() {
		freeze = false;
	}

	public int getX() {
		return tileX;
	}

	public int getY() {
		return tileY;
	}

	public String getType() {
		return type;
	}

	public void draw(Graphics g) {

		((Graphics2D) g).setComposite(AlphaComposite.SrcOver.derive(1.0f));

		if (out != null)
			g.drawImage(out, posX, posY, null);
	}
}
