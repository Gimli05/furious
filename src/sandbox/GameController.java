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
		System.out.println("Class: GameController\t Method: Constructor\t Param: -");
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
		System.out.println("Class: GameController\t Method: startNewGame\t Param: -");
		buildFromFile("level.txt");
	}
	
	
	/**
	 * Értesíti a játékost, hogy nyert, és leállítja a játékot.
	 */
	public static void winEvent(){
		System.out.println("Class: GameController\t Method: winEvent\t Param: -\t Gyozelem.");
		//TODO: kitolteni
	}
	
	
	/**
	 * Értesíti a játékost, hogy vesztett, és leállítja a játékot.
	 */
	public static void loseEvent(){
		System.out.println("Class: GameController\t Method: loseEvent\t Param: -\t Veres�g.");
		//TODO: kitolteni
	}
	
	private void buildFromFile(String filename) throws IOException{
		System.out.println("Class: GameController\t Method: buildFromFile\t Param: filename\t Bet�lt�s.");
		isTheGameRunning=false;
		railCollection.clear();
		trainCollection.clear();
		
		
		/**Létrehozzuk az olvasót**/
		String in;
		String[] line;
		BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
		line=br.readLine().split(";");
		
		/**Kiolvassuk a pálya méretét**/
		
		int x = Integer.parseInt(line[0]);
		int y = Integer.parseInt(line[1]);
		Rail[][] tempMap = new Rail[x][y];
		String[][] tempView = new String[x][y];
		
				
		/** Létrehozzuk a blokosított pályavázlatot**/
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
		
		/**Összekötjük az elemeket**/
		
		for(int i=0;i<x;i++){
			for(int j=0;j<y;j++){
				/**Szomszédok átnézése**/
				ArrayList<Rail> tmp = new ArrayList<Rail>();
				if(i-1>0 && tempView[i-1][j]!=null) tmp.add(tempMap[i-1][j]); //NY
				if(i+1<x && tempView[i+1][j]!=null) tmp.add(tempMap[i+1][j]); //K
				if(j-1>0 && tempView[i][j-1]!=null) tmp.add(tempMap[i][j-1]); //É
				if(j+1<x && tempView[i][j+1]!=null) tmp.add(tempMap[i][j+1]); //D
				
				if(i-1>0 && j-1>0 && tempView[i-1][j]!=null) tmp.add(tempMap[i-1][j-1]); //ÉNY
				if(i+1<x && j-1>0 && tempView[i+1][j]!=null) tmp.add(tempMap[i+1][j-1]); //ÉK
				if(j-1>0 && j+1<y && tempView[i][j-1]!=null) tmp.add(tempMap[i-1][j+1]); //DNy
				if(j+1<x && j+1<y && tempView[i][j+1]!=null) tmp.add(tempMap[i+1][j+1]); //DK
				
				/**Véglegesítés**/
				
				tempMap[i][j].setNeighbourRails(tmp);
			}
		}
		
		/**Bepakoljuk a collectionba**/
		
		for(int i=0;i<x;i++){
			for(int j=0;j<y;j++){
				if(tempMap[i][j]!=null)railCollection.add(tempMap[i][j]);
			}
		}
		
		/**Profit**/
		isTheGameRunning=true;
	}
	
	 /**
     * A map-ben minden egyes mező egy betűvel van megadva.
     * Mindegy egyes betű egy bizonyos cellatípusnak felel meg.
     * Alábbi függvény a megkapott betűnek megfelelő síntípust hoz létre, és visszaadja mely bekerül a railCollection-be.
     *
     * @param mapChar   A map-ban szereplő karakter, mely egy síntípust takar.
     * @return  A létrehozott sín.
     */
    private Rail elementReader(String mapChar){
    	System.out.println("Class: GameController\t Method: elementReader\t Param: mapChar\t Dek�dol�s a f�jlb�l.");
        switch(mapChar){
        case "E":
            return new EnterPoint(); /* Létrehozunk egy új enterPointot. */
           
        case "R":
            return new Rail(); /* Létrehozunk egy új Railt. */
           
        case "S":
            return new Switch(); /* Létrehozunk egy új Switchet. */
           
        case "U":
            return new TunnelEntrance(); /* Létrehozunk egy új TunnelEntrance-t. */
           
        case "1":
            return new TrainStation(Color.RED); /* Létrehozunk egy új TrainStation-t, mely piros színű lesz. */
           
        case "2":
            return new TrainStation(Color.GREEN); /* Létrehozunk egy új TrainStation-t, mely piros színű lesz. */
           
        case "3":
            return new TrainStation(Color.BLUE); /* Létrehozunk egy új TrainStation-t, mely piros színű lesz. */
           
        default:
            return null; /* Ez a lehetőség soha nem futhat le, mivel mi készítjük a map-ot. */
        }
    }
}
