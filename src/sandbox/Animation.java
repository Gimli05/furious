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
	boolean started = false;
	boolean ended = false;

	public Animation(int X, int Y, String t){	
		switch (t) {
		case "Passengers":
			try {
				img = ImageIO.read(new File(GUI.imageURL + t + ".png"));
			} catch (IOException e) {e.printStackTrace();}
			
			posX = (int) (GUI.TILEWIDTH * 0.8);
			posY = (int) (GUI.TILEHEIGHT * 0.2);
			frameCount = 2;
			delay = 400;
			continuous = true;
			break;
		}

		// Kép megvan
		type=t;
		tileX=X;
		tileY=Y;
		width = img.getWidth(null) / frameCount;
		height = img.getHeight(null);
		posX += X * GUI.TILEWIDTH - width / 2;
		posY += Y * GUI.TILEHEIGHT - height / 2;
	}

	private void setCurrentFrame(int i) {
		if (i < 0 || i >= frameCount)
			return;
		out = ((BufferedImage) img).getSubimage(i * width, 0, width, height);
	}
	
	public void setUpdatedTiles(boolean[][] changeMap){
		int x = (posX - posX%GUI.TILEWIDTH)/GUI.TILEWIDTH;
		int y = (posY - posY%GUI.TILEHEIGHT)/GUI.TILEHEIGHT;
		
		changeMap[x][y]=true;		
		if(x<GUI.BOARDWIDTH-1)changeMap[x+1][y]=true;
		if(x<GUI.BOARDHEIGHT-1)changeMap[x][y+1]=true;
		if(x<GUI.BOARDWIDTH-1 && x<GUI.BOARDWIDTH-1)changeMap[x+1][y+1]=true;
	}
	
	public boolean started() {
		return started;
	}

	public boolean ended() {
		return ended;
	}

	public void endLoop(){
		continuous=false;
	}
	
	public void start() {
		started = true;
		new Thread() {
			@Override
			public void run() {
				if (continuous) {
					for (int i = 0; i < frameCount && continuous; i++) {
						setCurrentFrame(i);
						if (i == frameCount - 1 && continuous)
							i = -1; // Loop

						try {
							Thread.sleep(delay);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					ended = true;
				}
			}
		}.start();
	}

	public void clear() throws Throwable {
		this.finalize();
	}

	public int getX(){
		return tileX;
	}
	
	public int getY(){
		return tileY;
	}
	
	public String getType(){
		return type;
	}
	
	public void draw(Graphics g) {
		
		
		((Graphics2D)g).setComposite(AlphaComposite.SrcOver.derive(1.0f));

		if (out!= null)
			g.drawImage(out, posX, posY, null);
	}
}
