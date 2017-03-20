package sandbox;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * A játékmenet vezérléséért felel. Betölti a játékos által kiválasztott pályát fájlból, felépíti a pályához tartozó sín hálózatot és 
 * vezérli melyik EnterPoint-on milyen ütemezéssel hány darab vonat érkezzen meg. Ez az osztály felel a vonatok sebességének meghatározásáért, 
 * és a játék kimenetelének eldöntéséért és ennek lekezeléséért. Felelõs továbbá annak felügyeléséért, hogy csak két alagútszáj lehessen egyszerre aktív.
 */
public class GameController {
	private Boolean isTheGameRunning; /* Megadja, hogy éppen folyamatban van-e játék. True, ha igen, false ha nem. */
	
	private ArrayList<Rail> railCollection; /* A síneket tároló arraylist. A pálya betöltése után ebben tároljuk el a teljes sínhálózatot */
	
	private TrainCollection trainCollection; /* A vonatokat tároló kollekció. A játék futása során az adott pályához tartozó ütemezés szerint adagoljuk 
											  * a pályához tartozó hosszúságú vonatokat a tárolóba a tároló addTrain(Train) metódusával.*/
	
	
	/**
	 * A GameController konstruktora.
	 */
	public GameController(){
		isTheGameRunning = false;
		railCollection=new ArrayList<Rail>();
		trainCollection = new TrainCollection();
	}
	
	
	/**
	 * Új játék indítására szolgáló függvény. Megjeleníti a játékosnak a választható pályák listáját. 
	 * Miután a játékos kiválasztotta melyik pályán akar játszani, betölti a pályához tartozó sínhálózatot és vonat ütemezést. 
	 * Ezután elindítja a vonatok léptetéséért felelõs szálat.
	 */
	public void startNewGame(){
		//TODO: kitolteni
	}
	
	
	/**
	 * Értesíti a játékost, hogy nyert, és leállítja a játékot.
	 */
	public static void winEvent(){
		//TODO: kitolteni
	}
	
	
	/**
	 * Értesíti a játékost, hogy vesztett, és leállítja a játékot.
	 */
	public static void loseEvent(){
		//TODO: kitolteni
	}
	
	private void buildFromFile() throws IOException{
		isTheGameRunning=false;
		railCollection.clear();
		trainCollection.clear();
		
		
		/*Létrehozzuk az olvasót*/
		String in;
		String[] line;
		BufferedReader br = new BufferedReader(new FileReader(new File("level.txt")));
		line=br.readLine().split(";");
		
		/*Kiolvassuk a pálya méretét*/
		
		int x = Integer.parseInt(line[0]);
		int y = Integer.parseInt(line[1]);
		Rail[][] tempMap = new Rail[x][y];
		String[][] tempView = new String[x][y];
		
				
		/* Létrehozzuk a blokosított pályavázlatot*/
		x=0;
		y=0;
		while((in=br.readLine())!=null){
			line=in.split("");
			for(String s: line){
				tempMap[x][y] = elementReader(s);
				tempView[x][y]=s;
				x++;
			}
			y++;
		}
		
		/*Összekötjük az elemeket*/
		
		for(int i=0;i<x;i++){
			for(int j=0;j<y;j++){
				/*Szomszédok átnézése*/
				ArrayList<Rail> tmp = new ArrayList<Rail>();
				if(i-1>0 && tempView[i-1][j]!=null) tmp.add(tempMap[i-1][j]); //NY
				if(i+1<x && tempView[i+1][j]!=null) tmp.add(tempMap[i+1][j]); //K
				if(j-1>0 && tempView[i][j-1]!=null) tmp.add(tempMap[i][j-1]); //É
				if(j+1<x && tempView[i][j+1]!=null) tmp.add(tempMap[i][j+1]); //D
				
				if(i-1>0 && j-1>0 && tempView[i-1][j]!=null) tmp.add(tempMap[i-1][j-1]); //ÉNY
				if(i+1<x && j-1>0 && tempView[i+1][j]!=null) tmp.add(tempMap[i+1][j-1]); //ÉK
				if(j-1>0 && j+1<y && tempView[i][j-1]!=null) tmp.add(tempMap[i-1][j+1]); //DNy
				if(j+1<x && j+1<y && tempView[i][j+1]!=null) tmp.add(tempMap[i+1][j+1]); //DK
				
				/*Véglegesítés*/
				
				tempMap[i][j].setNeighbourRails(tmp);
			}
		}
		
		/*Bepakoljuk a collectionba*/
		
		for(int i=0;i<x;i++){
			for(int j=0;j<y;j++){
				if(tempMap[i][j]!=null)railCollection.add(tempMap[i][j]);
			}
		}
		
		/*Profit*/
	}
	
	private Rail elementReader(String s){
		switch(s){
		case "E":
			return new EnterPoint(new ArrayList<Rail>());
		case "R":
			return new Rail(new ArrayList<Rail>());
		case "S":
			return new Switch(new ArrayList<Rail>());
		case "U":
			return new TunnelEntrance(new ArrayList<Rail>());
		case "1":
			return new TrainStation(new ArrayList<Rail>(),Color.RED);
		case "2":
			return new TrainStation(new ArrayList<Rail>(),Color.GREEN);
		case "3":
			return new TrainStation(new ArrayList<Rail>(), Color.BLUE);
		default:
			return null;
		}
	}
}
