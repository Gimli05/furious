package sandbox;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * A j�t�kmenet vez�rl�s��rt felel. Bet�lti a j�t�kos �ltal kiv�lasztott p�ly�t f�jlb�l, fel�p�ti a p�ly�hoz tartoz� s�n h�l�zatot �s 
 * vez�rli melyik EnterPoint-on milyen �temez�ssel h�ny darab vonat �rkezzen meg. Ez az oszt�ly felel a vonatok sebess�g�nek meghat�roz�s��rt, 
 * �s a j�t�k kimenetel�nek eld�nt�s��rt �s ennek lekezel�s��rt. Felel�s tov�bb� annak fel�gyel�s��rt, hogy csak k�t alag�tsz�j lehessen egyszerre akt�v.
 */
public class GameController {
	private Boolean isTheGameRunning; /* Megadja, hogy �ppen folyamatban van-e j�t�k. True, ha igen, false ha nem. */
	
	private ArrayList<Rail> railCollection; /* A s�neket t�rol� arraylist. A p�lya bet�lt�se ut�n ebben t�roljuk el a teljes s�nh�l�zatot */
	
	private TrainCollection trainCollection; /* A vonatokat t�rol� kollekci�. A j�t�k fut�sa sor�n az adott p�ly�hoz tartoz� �temez�s szerint adagoljuk 
											  * a p�ly�hoz tartoz� hossz�s�g� vonatokat a t�rol�ba a t�rol� addTrain(Train) met�dus�val.*/
	
	
	/**
	 * A GameController konstruktora.
	 */
	public GameController(){
		isTheGameRunning = false;
		railCollection=new ArrayList<Rail>();
		trainCollection = new TrainCollection();
	}
	
	
	/**
	 * �j j�t�k ind�t�s�ra szolg�l� f�ggv�ny. Megjelen�ti a j�t�kosnak a v�laszthat� p�ly�k list�j�t. 
	 * Miut�n a j�t�kos kiv�lasztotta melyik p�ly�n akar j�tszani, bet�lti a p�ly�hoz tartoz� s�nh�l�zatot �s vonat �temez�st. 
	 * Ezut�n elind�tja a vonatok l�ptet�s��rt felel�s sz�lat.
	 */
	public void startNewGame(){
		//TODO: kitolteni
	}
	
	
	/**
	 * �rtes�ti a j�t�kost, hogy nyert, �s le�ll�tja a j�t�kot.
	 */
	public static void winEvent(){
		//TODO: kitolteni
	}
	
	
	/**
	 * �rtes�ti a j�t�kost, hogy vesztett, �s le�ll�tja a j�t�kot.
	 */
	public static void loseEvent(){
		//TODO: kitolteni
	}
	
	private void buildFromFile() throws IOException{
		isTheGameRunning=false;
		railCollection.clear();
		trainCollection.clear();
		
		
		/*L�trehozzuk az olvas�t*/
		String in;
		String[] line;
		BufferedReader br = new BufferedReader(new FileReader(new File("level.txt")));
		line=br.readLine().split(";");
		
		/*Kiolvassuk a p�lya m�ret�t*/
		
		int x = Integer.parseInt(line[0]);
		int y = Integer.parseInt(line[1]);
		Rail[][] tempMap = new Rail[x][y];
		String[][] tempView = new String[x][y];
		
				
		/* L�trehozzuk a blokos�tott p�lyav�zlatot*/
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
		
		/*�sszek�tj�k az elemeket*/
		
		for(int i=0;i<x;i++){
			for(int j=0;j<y;j++){
				/*Szomsz�dok �tn�z�se*/
				ArrayList<Rail> tmp = new ArrayList<Rail>();
				if(i-1>0 && tempView[i-1][j]!=null) tmp.add(tempMap[i-1][j]); //NY
				if(i+1<x && tempView[i+1][j]!=null) tmp.add(tempMap[i+1][j]); //K
				if(j-1>0 && tempView[i][j-1]!=null) tmp.add(tempMap[i][j-1]); //�
				if(j+1<x && tempView[i][j+1]!=null) tmp.add(tempMap[i][j+1]); //D
				
				if(i-1>0 && j-1>0 && tempView[i-1][j]!=null) tmp.add(tempMap[i-1][j-1]); //�NY
				if(i+1<x && j-1>0 && tempView[i+1][j]!=null) tmp.add(tempMap[i+1][j-1]); //�K
				if(j-1>0 && j+1<y && tempView[i][j-1]!=null) tmp.add(tempMap[i-1][j+1]); //DNy
				if(j+1<x && j+1<y && tempView[i][j+1]!=null) tmp.add(tempMap[i+1][j+1]); //DK
				
				/*V�gleges�t�s*/
				
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
