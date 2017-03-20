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
		System.out.println("Class: GameController\t Method: loseEvent\t Param: -\t Vereseg");
		//TODO: kitolteni
	}
	
	/**
	 * Ebben a részben egy elöre kitöltött szöveges fájlból ovlasunk majd be karaktereket,
	 * ami alapján automatikusan felépitjük a pályánkat. A fájl kötött formátumú, ha ezt 
	 * nem tartjuk a program hibval zárulna.
	 * A szövegfáj formai követelményei:
	 * 	* Az elsö sor a pálya szélessége és magassága lesz, ;-vel elválasztva (pl.: 12;7)
	 *  * Ezután az elementReadeben leírtak alapján töltjük ki afájlunkat
	 *  * Fontos hogy minden sorban pontosan annyi karakter legyen, amennyit meghatároztunk kezdetben
	 *  * Nincs ellenörizve, hogy van e belépési pontunk, de az akadálytalan futás érdekében ajánlott.
	 *  * Az egymás mellett lévö sinek szomszédosnak lesznek véve, így egy legalább egy mezönyi helyet
	 *    kell hagyni köztük, hogy jól építse fel.
	 *    pl.: ERRR
	 *    	   xRxR
	 *  	   xRRR
	 * Ha ezeket tartjuk, várhatóan jó eredményt kapunk
	 * 
	 * A gyakorlati müködés:
	 * 	* Kezdetben létrehozunk egy olvasót, amivel soronként végignézzük a fájlt
	 * 	* Az elsö sorát kiolvassuk és létrehozunk 2 tömböt: tempMap-ot ami konkrét sineket tárol
	 * 	  és a tempView-t ami a kiolvasott karaktereket.
	 * 	* A kiolvasott karakterektöl függöen az elementReader visszaadja amegfelelö tipusú Rail-t
	 *  * Végigolvassuk a fájlt és kitöltüjük a két tömböt
	 *  
	 *  * Ezután sorban végigjárjuk a tempView mezöit és ha Rail, akkor megnézzük hogy a 8 szomszédjából melyik létezö Rail
	 *  * Ha a 8 szomszédos mezön van Rail akkor azt felvesszük az aktuális mezö szomszédai közé
	 *  * Végül hozzáadjuk a szomszédokat a tempMap megfelelö Rail-jéhez
	 *  * Ügyelünk arra hogy az ellenörzött 8 mezö ne lógjon le a pályáról
	 *  
	 *  * Végül bejárjuk a tempMap-t és az összes létezö Railt hozzáaduk a railCollectionhoz
	 *  
	 * Ekkorra minden szomszédosság fel van építve és minden mezö megjelenik a collection-ban.
	 * A metódus visszatér.
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
		System.out.println("Létrehozott pályaelemek száma: "+railCollection.size());
		isTheGameRunning=true;
		System.out.println("A játék elindult");
	}
	
	 /**
     * A map-ben minden egyes mezõ egy betûvel van megadva.
     * Mindegy egyes betû egy bizonyos cellatípusnak felel meg.
     * Alábbi függvény a megkapott betûnek megfelelõ síntípust hoz létre, és visszaadja mely bekerül a railCollection-be.
     *
     * @param mapChar   A map-ban szereplõ karakter, mely egy síntípust takar.
     * @return  A létrehozott sín.
     */
    private Rail elementReader(String mapChar){
    	System.out.println("Class: GameController\t Method: elementReader\t Param: mapChar\t Dek------------?????--------a fajlbol");
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
            return new TrainStation(Color.RED); /* Létrehozunk egy új TrainStation-t, mely piros színû lesz. */
           
        case "2":
            return new TrainStation(Color.GREEN); /* Létrehozunk egy új TrainStation-t, mely piros színû lesz. */
           
        case "3":
            return new TrainStation(Color.BLUE); /* Létrehozunk egy új TrainStation-t, mely piros színû lesz. */
           
        default:
            return null; /* Ez a lehetõség akkor fut le ha nem ismert betü van a szövegünben, mely ilyenkor egy üres mezö lesz */
        }
    }
}
