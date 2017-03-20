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
	
	/**
	 * Ebben a r�szben egy el�re kit�lt�tt sz�veges f�jlb�l ovlasunk majd be karaktereket,
	 * ami alapj�n automatikusan fel�pitj�k a p�ly�nkat. A f�jl k�t�tt form�tum�, ha ezt 
	 * nem tartjuk a program hibval z�rulna.
	 * A sz�vegf�j formai k�vetelm�nyei:
	 * 	* Az els� sor a p�lya sz�less�ge �s magass�ga lesz, ;-vel elv�lasztva (pl.: 12;7)
	 *  * Ezut�n az elementReadeben le�rtak alapj�n t�ltj�k ki af�jlunkat
	 *  * Fontos hogy minden sorban pontosan annyi karakter legyen, amennyit meghat�roztunk kezdetben
	 *  * Nincs ellen�rizve, hogy van e bel�p�si pontunk, de az akad�lytalan fut�s �rdek�ben aj�nlott.
	 *  * Az egym�s mellett l�v� sinek szomsz�dosnak lesznek v�ve, �gy egy legal�bb egy mez�nyi helyet
	 *    kell hagyni k�zt�k, hogy j�l �p�tse fel.
	 *    pl.: ERRR
	 *    	   xRxR
	 *  	   xRRR
	 * Ha ezeket tartjuk, v�rhat�an j� eredm�nyt kapunk
	 * 
	 * A gyakorlati m�k�d�s:
	 * 	* Kezdetben l�trehozunk egy olvas�t, amivel soronk�nt v�gign�zz�k a f�jlt
	 * 	* Az els� sor�t kiolvassuk �s l�trehozunk 2 t�mb�t: tempMap-ot ami konkr�t sineket t�rol
	 * 	  �s a tempView-t ami a kiolvasott karaktereket.
	 * 	* A kiolvasott karakterekt�l f�gg�en az elementReader visszaadja amegfelel� tipus� Rail-t
	 *  * V�gigolvassuk a f�jlt �s kit�lt�j�k a k�t t�mb�t
	 *  
	 *  * Ezut�n sorban v�gigj�rjuk a tempView mez�it �s ha Rail, akkor megn�zz�k hogy a 8 szomsz�dj�b�l melyik l�tez� Rail
	 *  * Ha a 8 szomsz�dos mez�n van Rail akkor azt felvessz�k az aktu�lis mez� szomsz�dai k�z�
	 *  * V�g�l hozz�adjuk a szomsz�dokat a tempMap megfelel� Rail-j�hez
	 *  * �gyel�nk arra hogy az ellen�rz�tt 8 mez� ne l�gjon le a p�ly�r�l
	 *  
	 *  * V�g�l bej�rjuk a tempMap-t �s az �sszes l�tez� Railt hozz�aduk a railCollectionhoz
	 *  
	 * Ekkorra minden szomsz�doss�g fel van �p�tve �s minden mez� megjelenik a collection-ban.
	 * A met�dus visszat�r.
	 *  
	 */
	
	private void buildFromFile(String filename) throws IOException{
		System.out.println("Class: GameController\t Method: buildFromFile\t Param: filename\t Betoltes.");
		isTheGameRunning=false;
		railCollection.clear();
		trainCollection.clear();
		
		
		String in;
		String[] line;
		BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
		line=br.readLine().split(";");
		
		
		int width = Integer.parseInt(line[0]);
		int height = Integer.parseInt(line[1]);
		Rail[][] tempMap = new Rail[width][height];
		String[][] tempView = new String[width][height];
		
				

		int x=0;
		int y=0;
		while((in=br.readLine())!=null){
			line=in.split("");
			for(String s: line){
				tempMap[x][y] = elementReader(s);
				tempView[x][y]=s;
				x++;
			}
			x=0;
			y++;
		}
		
		
		for(int i=0;i<width;i++){
			for(int j=0;j<height;j++){	
				if(tempMap[i][j]!=null){				
					ArrayList<Rail> tmp = new ArrayList<Rail>();
					if(i-1>0 && tempView[i-1][j]!=null) tmp.add(tempMap[i-1][j]);
					if(i+1<width && tempView[i+1][j]!=null) tmp.add(tempMap[i+1][j]);
					if(j-1>0 && tempView[i][j-1]!=null) tmp.add(tempMap[i][j-1]);
					if(j+1<height && tempView[i][j+1]!=null) tmp.add(tempMap[i][j+1]);
				
					if(i-1>0 && j-1>0 && tempView[i-1][j-1]!=null) tmp.add(tempMap[i-1][j-1]);
					if(i+1<width && j-1>0 && tempView[i+1][j-1]!=null) tmp.add(tempMap[i+1][j-1]);
					if(i-1>0 && j+1<height && tempView[i-1][j+1]!=null) tmp.add(tempMap[i-1][j+1]);
					if(i+1<width && j+1<height && tempView[i+1][j+1]!=null) tmp.add(tempMap[i+1][j+1]);
								
					tempMap[i][j].setNeighbourRails(tmp);
				}
			}
		}
		
		
		for(int i=0;i<width;i++){
			for(int j=0;j<height;j++){
				if(tempMap[i][j]!=null)railCollection.add(tempMap[i][j]);
			}
		}
		System.out.println("L�trehozott p�lyaelemek sz�ma: "+railCollection.size());
		isTheGameRunning=true;
		System.out.println("A j�t�k elindult");
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
            return null; /* Ez a lehet�s�g akkor fut le ha nem ismert bet� van a sz�veg�nben, mely ilyenkor egy �res mez� lesz */
        }
    }
}
