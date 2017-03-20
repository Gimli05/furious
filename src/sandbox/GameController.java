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
		System.out.println("Class: GameController\t Method: Constructor\t Param: -");
		isTheGameRunning = false;
		railCollection=new ArrayList<Rail>();
		trainCollection = new TrainCollection();
	}
	
	
	/**
	 * �j j�t�k ind�t�s�ra szolg�l� f�ggv�ny. Megjelen�ti a j�t�kosnak a v�laszthat� p�ly�k list�j�t. 
	 * Miut�n a j�t�kos kiv�lasztotta melyik p�ly�n akar j�tszani, bet�lti a p�ly�hoz tartoz� s�nh�l�zatot �s vonat �temez�st. 
	 * Ezut�n elind�tja a vonatok l�ptet�s��rt felel�s sz�lat.
	 */
	public void startNewGame(int mapNumber){
		System.out.println("Class: GameController\t Method: startNewGame\t Param: -");
		
		String mapName = new String("../../maps/map" + mapNumber + ".txt");
		try {
			buildFromFile(mapName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * �rtes�ti a j�t�kost, hogy nyert, �s le�ll�tja a j�t�kot.
	 */
	public static void winEvent(){
		System.out.println("Class: GameController\t Method: winEvent\t Param: -\t Gyozelem.");
		//TODO: kitolteni
	}
	
	
	/**
	 * �rtes�ti a j�t�kost, hogy vesztett, �s le�ll�tja a j�t�kot.
	 */
	public static void loseEvent(){
		System.out.println("Class: GameController\t Method: loseEvent\t Param: -\t Vereseg");
		//TODO: kitolteni
	}
	
	private void buildFromFile(String filename) throws IOException{
		System.out.println("Class: GameController\t Method: buildFromFile\t Param: filename\t Betoltes.");
		isTheGameRunning=false;
		railCollection.clear();
		trainCollection.clear();
		
		
		/**L�trehozzuk az olvas�t**/
		String in;
		String[] line;
		BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
		line=br.readLine().split(";");
		
		/**Kiolvassuk a p�lya m�ret�t**/
		
		int x = Integer.parseInt(line[0]);
		int y = Integer.parseInt(line[1]);
		Rail[][] tempMap = new Rail[x][y];
		String[][] tempView = new String[x][y];
		
				
		/** L�trehozzuk a blokos�tott p�lyav�zlatot**/
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
		
		/**�sszek�tj�k az elemeket**/
		
		for(int i=0;i<x;i++){
			for(int j=0;j<y;j++){
				/**Szomsz�dok �tn�z�se**/
				ArrayList<Rail> tmp = new ArrayList<Rail>();
				if(i-1>0 && tempView[i-1][j]!=null) tmp.add(tempMap[i-1][j]); //NY
				if(i+1<x && tempView[i+1][j]!=null) tmp.add(tempMap[i+1][j]); //K
				if(j-1>0 && tempView[i][j-1]!=null) tmp.add(tempMap[i][j-1]); //�
				if(j+1<x && tempView[i][j+1]!=null) tmp.add(tempMap[i][j+1]); //D
				
				if(i-1>0 && j-1>0 && tempView[i-1][j]!=null) tmp.add(tempMap[i-1][j-1]); //�NY
				if(i+1<x && j-1>0 && tempView[i+1][j]!=null) tmp.add(tempMap[i+1][j-1]); //�K
				if(j-1>0 && j+1<y && tempView[i][j-1]!=null) tmp.add(tempMap[i-1][j+1]); //DNy
				if(j+1<x && j+1<y && tempView[i][j+1]!=null) tmp.add(tempMap[i+1][j+1]); //DK
				
				/**V�gleges�t�s**/
				
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
     * A map-ben minden egyes mez� egy bet�vel van megadva.
     * Mindegy egyes bet� egy bizonyos cellat�pusnak felel meg.
     * Al�bbi f�ggv�ny a megkapott bet�nek megfelel� s�nt�pust hoz l�tre, �s visszaadja mely beker�l a railCollection-be.
     *
     * @param mapChar   A map-ban szerepl� karakter, mely egy s�nt�pust takar.
     * @return  A l�trehozott s�n.
     */
    private Rail elementReader(String mapChar){
    	System.out.println("Class: GameController\t Method: elementReader\t Param: mapChar\t Dek------------?????--------a fajlbol");
        switch(mapChar){
        case "E":
            return new EnterPoint(); /* L�trehozunk egy �j enterPointot. */
           
        case "R":
            return new Rail(); /* L�trehozunk egy �j Railt. */
           
        case "S":
            return new Switch(); /* L�trehozunk egy �j Switchet. */
           
        case "U":
            return new TunnelEntrance(); /* L�trehozunk egy �j TunnelEntrance-t. */
           
        case "1":
            return new TrainStation(Color.RED); /* L�trehozunk egy �j TrainStation-t, mely piros sz�n� lesz. */
           
        case "2":
            return new TrainStation(Color.GREEN); /* L�trehozunk egy �j TrainStation-t, mely piros sz�n� lesz. */
           
        case "3":
            return new TrainStation(Color.BLUE); /* L�trehozunk egy �j TrainStation-t, mely piros sz�n� lesz. */
           
        default:
            return null; /* Ez a lehet�s�g soha nem futhat le, mivel mi k�sz�tj�k a map-ot. */
        }
    }
}
