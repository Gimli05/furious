package sandbox;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;

import javax.swing.ImageIcon;


class TrainBlock {
	protected static int TRAINBLOCKWIDTH = 40;
	protected static int TRAINBLOCKHEIGHT = 40;
	
	
	private String type;
	// Elözö indexek
	private int currentMapX, currentMapY;
	private int lastMapX, lastMapY;
	private int nextMapX, nextMapY;
	// Konkrét hely
	private int posX, posY;

	// Forgatási adatok
	private boolean isTurning;
	private double angle;
	private double startAngle;
	private int rotateDir;
	private double circleStartAngle;
	private int correctedX, correctedY;
	
	// Raw Data
	private Image img;
	
	//Változás könyvelés
	
	private String changeLog="";
	
	
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
		angle = 0;
		isTurning = false;

		// Raw Data
		switch (type) {
		case "E":
			img = new ImageIcon(GUI.imageURL + "Engine.png").getImage();
			break;
		case "R":
			img = new ImageIcon(GUI.imageURL + "RCab.png").getImage();
			break;
		case "G":
			img = new ImageIcon(GUI.imageURL + "GCab.png").getImage();
			break;
		case "B":
			img = new ImageIcon(GUI.imageURL + "BCab.png").getImage();
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
		double percent = ((double) currentTime / GUI.TILEINTERVAL);
		// Idöfüggö számítás
		if (!isTurning) {
			int direction = (angle == 180 || angle == 270) ? -1 : 1;
			// Az irány nem változik
			if (angle == 90) {
				// Függölegesen halad
				posX = (int) Math.floor((currentMapX) * GUI.TILEWIDTH + GUI.TILEWIDTH / 2);
				posY = (int) Math.floor((currentMapY) * GUI.TILEHEIGHT + direction * GUI.TILEHEIGHT * percent);
			} else if (angle == 270) {
				// Függölegesen halad
				posX = (int) Math.floor((currentMapX) * GUI.TILEWIDTH + GUI.TILEWIDTH / 2);
				posY = (int) Math.floor((currentMapY + 1) * GUI.TILEHEIGHT + direction * GUI.TILEHEIGHT * percent);
			} else if (angle == 180) {
				// Vizszintesen halad
				posX = (int) Math.floor((currentMapX + 1) * GUI.TILEWIDTH + direction * percent * GUI.TILEWIDTH);
				posY = (int) Math.floor((currentMapY) * GUI.TILEHEIGHT + GUI.TILEHEIGHT / 2);
			} else if (angle == 0) {
				posX = (int) Math.floor((currentMapX) * GUI.TILEWIDTH + direction * percent * GUI.TILEWIDTH);
				posY = (int) Math.floor((currentMapY) * GUI.TILEHEIGHT + GUI.TILEHEIGHT / 2);
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
			double range = GUI.TILEWIDTH / 2;
			int circleX = (int) (range * Math.cos(Math.toRadians(circleStartAngle + delta)));
			int circleY = (int) (range * Math.sin(Math.toRadians(circleStartAngle + delta)));

			posX = (int) Math.floor((currentMapX + correctedX) * GUI.TILEWIDTH + circleX);
			posY = (int) Math.floor((currentMapY + correctedY) * GUI.TILEHEIGHT + circleY);

		}

		// Update
		//Figyelünk hogy eggyel több vesszö van!!
		if (currentMapX >= 0 && currentMapY >= 0) {
			changeLog=changeLog+","+currentMapX+","+currentMapY;
			

			if (lastMapX >= 0 && lastMapY >= 0) {
				changeLog=changeLog+","+lastMapX+","+lastMapY;
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
	
	public double getStartAngle(){
		return startAngle;
	}
	
	public String getChangeLog(){
		String log = changeLog;
		changeLog="";
		return log;
	}
}


