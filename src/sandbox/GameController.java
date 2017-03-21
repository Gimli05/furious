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
	private static Boolean isTheGameRunning; /* Megadja, hogy éppen folyamatban van-e játék. True, ha igen, false ha nem. */
	
	private static ArrayList<Rail> railCollection; /* A síneket tároló arraylist. A pálya betöltése után ebben tároljuk el a teljes sínhálózatot */
	
	private static TrainCollection trainCollection; /* A vonatokat tároló kollekció. A játék futása során az adott pályához tartozó ütemezés szerint adagoljuk 
											  * a pályához tartozó hosszúságú vonatokat a tárolóba a tároló addTrain(Train) metódusával.*/
	
	private static int activeEntranceCounter; /* Az aktív alagútszályak számát tárolja. Segítségével, ha két aktív alagútszály van, akkor létrejöhet az alagút közöttük. */
	private static int lastPlayedMapNumber;
	
	
	/**
	 * A GameController konstruktora.
	 */
	public GameController(){
		System.out.println("Class: GameController\t Object: GameController@STATIC\t Method: Constructor\t ");
		isTheGameRunning = false;
		railCollection=new ArrayList<Rail>();
		trainCollection = new TrainCollection();
	}
	
	
	/**
	 * Új játék indítására szolgáló függvény. Megjeleníti a játékosnak a választható pályák listáját. 
	 * Miután a játékos kiválasztotta melyik pályán akar játszani, betölti a pályához tartozó sínhálózatot és vonat ütemezést. 
	 * Ezután elindítja a vonatok léptetéséért felelõs szálat.
	 */
	public static void startNewGame(int mapNumber){
		System.out.println("Class: GameController\t Object: Object: GameController@STATIC\t Method: startNewGame\t Param: "+mapNumber);
		
		
		String mapName = new String("maps/map" + mapNumber + ".txt");
		try {
			buildFromFile(mapName);
			isTheGameRunning=true; /*Elinditjuka játkot*/
			System.out.println("\nClass: GameController\t Object: GameController@STATIC\t A játék elindult\n");
			lastPlayedMapNumber = mapNumber;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Értesíti a játékost, hogy nyert, és leállítja a játékot.
	 */
	public static void winEvent(){
		isTheGameRunning = false;
		System.out.println("Class: GameController\t Object: GameController@STATIC\t Method: winEvent\t Gyozelem.");
		if(lastPlayedMapNumber == 1){
			System.out.println("Class: GameController\t Object: GameController@STATIC\t Van még új pálya, így az következik.");
			System.out.println("\n\nÚJ JÁTÉK KEZDÕDIK \n\n");
			startNewGame(2);
		} else {
			System.out.println("Class: GameController\t Object: GameController@STATIC\t Nincs tobb palya, jatek vege.");
		}
	}
	
	
	/**
	 * Értesíti a játékost, hogy vesztett, és leállítja a játékot.
	 */
	public static void loseEvent(){
		isTheGameRunning = false;
		System.out.println("Class: GameController\t Object: GameController@STATIC\t Method: loseEvent\t Vereseg");
		System.out.println("Class: GameController\t Object: GameController@STATIC\t Michael Bay Effekt");
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
	
	private static void buildFromFile(String filename) throws IOException{
		System.out.println("Class: GameController\t Object: GameController@STATIC\t Method: buildFromFile\t Param: "+filename+"\t Betoltes.");
		
		/*Kezdetben megállítjk a játékot és töröljük azelözö listákat*/
		isTheGameRunning=false;
		railCollection.clear();
		trainCollection.clear();
		System.out.println("Class: GameController\t Object: GameController@STATIC\t Method: railCollection.clear");
		
		
		String in; /*Egy beolvasott sort tárol, ebbe olvasunk*/
		String[] line; /*Feltördeljük az elözöleg olvasott sort*/
		
		
		BufferedReader brMap = new BufferedReader(new FileReader(new File(filename))); /*Térkép File olvasó*/
		
		String tunnelFilename=filename.substring(0,filename.length()-4)+"tunnelmap.txt";
		line=brMap.readLine().split(";"); /*Kiolvassuk a pálya méretét*/
		
		
		int width = Integer.parseInt(line[0]); /*Pálya szélessége*/
		int height = Integer.parseInt(line[1]); /*Pálya magassága*/
		Rail[][] tempMap = new Rail[width][height]; /*Pályaelem tároló mátrix*/
		String[][] tempView = new String[width][height]; /*Pályaelem leíró mátrix*/
		

		int x=0; /*Segédváltozó szélességhez*/
		int y=0; /*Segédváltpzó magassághoz*/
		while((in=brMap.readLine())!=null){ /*Összes maradék sort kiolvassuk*/
			line=in.split("");	
			for(String s: line){ /*Minden karaktert megnézünk*/
				tempMap[x][y] = elementReader(s); /*Létrehozzuk a típust*/
				tempView[x][y]=s; /*Mentjük a vázlatát*/		
				x++;
			}
			x=0;
			y++;
		}
		
		
		System.out.println("\nClass: GameController\t Object: GameController@STATIC\t Sínek közötti kapcsolatok létrehozása.");
		for(int i=0;i<width;i++){ /*Végignézzük a pályát szélességben...*/
			for(int j=0;j<height;j++){	/*... és magasságban*/
				if(tempMap[i][j]!=null){		/*Ha az aktuális elem nem üres, akkor lehetnek szomszédai*/		
					ArrayList<Rail> tmp = new ArrayList<Rail>(); /*új "szomszéd tároló"*/
					if(i-1>0 && tempMap[i-1][j]!=null) tmp.add(tempMap[i-1][j]); /*Ha balra lévö mezö a pálya része és Rail akkor a szomszédja*/
					if(i+1<width && tempMap[i+1][j]!=null) tmp.add(tempMap[i+1][j]);/*Ha jobbra lévö mezö a pálya része és Rail akkor a szomszédja*/
					if(j-1>0 && tempMap[i][j-1]!=null) tmp.add(tempMap[i][j-1]);/*Ha felette lévö mezö a pálya része és Rail akkor a szomszédja*/
					if(j+1<height && tempMap[i][j+1]!=null) tmp.add(tempMap[i][j+1]);/*Ha alatta lévö mezö a pálya része és Rail akkor a szomszédja*/
					
					tempMap[i][j].setNeighbourRails(tmp); /*Hozzáadjuk az újonnan felvett szomszédokat*/
				}
			}
		}
		
		for(int i=0;i<width;i++){ /*Bejárjuk a táblát*/
			for(int j=0;j<height;j++){
				if(tempMap[i][j]!=null)railCollection.add(tempMap[i][j]); /*Ha van Rail tipus, akkor a kollekciónk része kell hogy legyen, felvesszük.*/
				}
		}

		brMap.close();
		System.out.println("Class: GameController\t Object: GameController@STATIC\t Létrehozott pályaelemek száma: "+railCollection.size()); /*Megnézzük hogy változott e valam*/
		isTheGameRunning=true; /*Elinditjuka játkot*/
		System.out.println("\nClass: GameController\t Object: GameController@STATIC\t A játék elindult\n");
	}
	

	
	 /**
     * A map-ben minden egyes mezõ egy betûvel van megadva.
     * Mindegy egyes betû egy bizonyos cellatípusnak felel meg.
     * Alábbi függvény a megkapott betûnek megfelelõ síntípust hoz létre, és visszaadja mely bekerül a railCollection-be.
     *
     * @param mapChar   A map-ban szereplõ karakter, mely egy síntípust takar.
     * @return  A létrehozott sín.
     */
    private static Rail elementReader(String mapChar){
    	System.out.println("\nClass: GameController\t Object: GameController@STATIC\t Method: elementReader\t Param: "+mapChar+"\t Elem dekodolasa fajlbol"); /* A jobb olvashatóság érdekében ezelõtt egy új sor van. */
        switch(mapChar){
        case "E":
        	System.out.println("Beolvasott elem: EnterPoint");
            return new EnterPoint(); /* Létrehozunk egy új enterPointot. */
           
        case "R":
        	System.out.println("Beolvasott elem: Rail");
            return new Rail(); /* Létrehozunk egy új Railt. */
           
        case "S":
        	System.out.println("Beolvasott elem: Switch");
            return new Switch(); /* Létrehozunk egy új Switchet. */
           
        case "U":
        	System.out.println("Beolvasott elem: TunnelEntrance");
            return new TunnelEntrance(); /* Létrehozunk egy új TunnelEntrance-t. */
           
        case "1":
        	System.out.println("Beolvasott elem: Red TrainStation");
            return new TrainStation(Color.RED); /* Létrehozunk egy új TrainStation-t, mely piros színû lesz. */
           
        case "2":
        	System.out.println("Beolvasott elem: Green TrainStation");
            return new TrainStation(Color.GREEN); /* Létrehozunk egy új TrainStation-t, mely zöld színû lesz. */
           
        case "3":
        	System.out.println("Beolvasott elem: Blue TrainStation");
            return new TrainStation(Color.BLUE); /* Létrehozunk egy új TrainStation-t, mely kék színû lesz. */
           
        default:
        	System.out.println("Beolvasott elem: ures");
            return null; /* Ez a lehetõség akkor fut le ha nem ismert betü van a szövegünben, mely ilyenkor egy üres mezö lesz */
        }
    }
    
    /**
     * Meg kell nézni minden léptetés után hogy a vonatok kiürültek e
     */ 
    private boolean hasTheGameEnded(){
    	System.out.println("Class: GameController\t Object: GameController@STATIC\t Method: hasTheGameEnded\t Vonatok uressegenek ellenorzese");
    	Boolean isAllEmpty = trainCollection.isAllEmpty();
    	System.out.println("Class: GameController\t Object: GameController@STATIC\t Returned: "+isAllEmpty);
    	return isAllEmpty;
    }
   
	
	/**
	 * Kizárólag a teszteléshez létrehozott metódus.
	 * Annyi alagútszályat tud aktiválni, ahányat megadunk neki. (ennek mennyiségét nem ellenõrzi, hiszen azt a Main megfelelõ része már megtette.
	 * 
	 * A getClass fv csak azért kell bele, mert a végsõ verzióban a GUI-n történõ kattintásból egybõl meg fogjuk tudni az eventet kiváltó objektum id-jét,
	 * GUI hiányában azonban erre nincs lehetõségünk. Ha nagyon szükséges, meg tudjuk oldani enélkül is (de az ocsmányabb lenne) ezért ennél maradtunk.
	 * 
	 * @param tunnelEntranceCounterToBeActivated	Hány alagútszályat akarunk aktiválni.
	 */
	public void skeletonTesterActivateTunnelEntrance(int tunnelEntranceCounterToBeActivated){
		for(int i=0; i < tunnelEntranceCounterToBeActivated; i++){
			Boolean notActivatedTunnelEntranceFound = false;
			while(!notActivatedTunnelEntranceFound){
				for(Rail oneRail:railCollection){
					if(oneRail.getClass() == TunnelEntrance.class){  /*  Az ilyenért lehet kibasznak tbh.. :D */
						TunnelEntrance oneTunnel = (TunnelEntrance) oneRail; /* Ezért meg fõleg. */
						if(!oneTunnel.checkIfActivated()){
							oneTunnel.activate();
							activeEntranceCounter++;
							notActivatedTunnelEntranceFound = true;
							break;
						}
					}
				}
			}
			
		}
		System.out.println("Class: GameController\t Object: GameController@STATIC\t Aktív alagútszájak száma: "+activeEntranceCounter);
		
		if(activeEntranceCounter == 2){
			System.out.println("\nClass: GameController\t Object: GameController@STATIC\t Alagút létrehozása");
			Rail entrance1, entrance2;
			
			/*Kikeressük a kettöt*/
			for(Rail rail:railCollection){
				if(rail.getClass() == TunnelEntrance.class){
					if(enrance1==null){
						entrance1=rail
					}else{
						entrance2=rail;
					}
				}
			}
			
			/*Összekötjük*/
			int e1X=entrance1.getX();
			int e1Y=entrance1.getY();
			int e2X=entrance2.getX();
			int e2Y=entrance2.getY();
			
			Rail lemarado = entrance1;
			Rail next;
			
			/*X közelit*/
			if(e1X>e2X){
				while(e1X>e2X){
					e1X--;
					ArrayList<Rail> neighbours = new ArrayList<Rail>();
					neighbours.add(lemarado);				
					
					if(e1X-1==e2X && e1Y==e2Y){
						e1X--;
						neighbours.add(entrance2);
					}else{
						next=new Tunnel(e1X-1,e1Y);
						neighbours.add(next);
					}
					
					Tunnel tmp = new Tunnel(e1X,e1Y);
					tmp.setNeighbourRails(neighbours);
					
					railCollection.add(tmp);
					if(e1X==e2X && e1Y==e2Y)entrance2.addNeighbourRail(tmp);				
					if(entrance1==lemarado)entrance1.addNeighbourRail(tmp);
					
					lemarado=tmp;
				}
			}else if(e1X<e2X){
				while(e1X<e2X){
					e1X++;
					ArrayList<Rail> neighbours = new ArrayList<Rail>();
					neighbours.add(lemarado);				
					
					if(e1X+1==e2X && e1Y==e2Y){
						e1X++;
						neighbours.add(entrance2);
					}else{
						next=new Tunnel(e1X+1,e1Y);
						neighbours.add(next);
					}
					
					Tunnel tmp = new Tunnel(e1X,e1Y);
					tmp.setNeighbourRails(neighbours);
					
					railCollection.add(tmp);
					if(e1X==e2X && e1Y==e2Y)entrance2.addNeighbourRail(tmp);				
					if(entrance1==lemarado)entrance1.addNeighbourRail(tmp);
					
					lemarado=tmp;
				}
			}
			
			/*Y közelit*/
			if(e1Y>e2Y){
				while(e1Y>e2Y){
					e1Y--;
					ArrayList<Rail> neighbours = new ArrayList<Rail>();
					neighbours.add(lemarado);				
					
					if(e1Y+1==e2Y && e1X==e2X){
						e1X--;
						neighbours.add(entrance2);
					}else{
						next=new Tunnel(e1X,e1Y-1);
						neighbours.add(next);
					}
					
					Tunnel tmp = new Tunnel(e1X,e1Y);
					tmp.setNeighbourRails(neighbours);
					
					railCollection.add(tmp);
					if(e1X==e2X && e1Y==e2Y)entrance2.addNeighbourRail(tmp);				
					if(entrance1==lemarado)entrance1.addNeighbourRail(tmp);
					
					lemarado=tmp;
				}
			}else if(e1Y<eY){
				while(e1Y<e2Y){
					e1Y++;
					ArrayList<Rail> neighbours = new ArrayList<Rail>();
					neighbours.add(lemarado);				
					
					if(e1Y+1==e2Y && e1X==e2X){
						e1Y++;
						neighbours.add(entrance2);
					}else{
						next=new Tunnel(e1X,e1Y+1);
						neighbours.add(next);
					}
					
					Tunnel tmp = new Tunnel(e1X,e1Y);
					tmp.setNeighbourRails(neighbours);
					
					railCollection.add(tmp);
					if(e1X==e2X && e1Y==e2Y)entrance2.addNeighbourRail(tmp);				
					if(entrance1==lemarado)entrance1.addNeighbourRail(tmp);
					
					lemarado=tmp;
				}
			}
		}
	}
	
	
	/**
	 * Kizárólag a teszteléshez létrehozott metódus.
	 * Ellenõrzi hány aktív alagútszáj van. Ha kettõ, akkor lebontja az alagutat, és deaktivál egyet.
	 * 
	 * A getClass fv csak azért kell bele, mert a végsõ verzióban a GUI-n történõ kattintásból egybõl meg fogjuk tudni az eventet kiváltó objektum id-jét,
	 * GUI hiányában azonban erre nincs lehetõségünk. Ha nagyon szükséges, meg tudjuk oldani enélkül is (de az ocsmányabb lenne) ezért ennél maradtunk.
	 */
	public void skeletonTesterDeActivateATunnelEntrance(){
		if(activeEntranceCounter == 2){
			System.out.println("\nClass: GameController\t Object: GameController@STATIC\t Alagút lerombolása");
			//TODO implementálni
		}
		
		Boolean activatedTunnelEntranceFound = false;
		while(!activatedTunnelEntranceFound){
			for(Rail oneRail:railCollection){
				if(oneRail.getClass() == TunnelEntrance.class){  /*  Az ilyenért lehet kibasznak tbh.. :D */
					TunnelEntrance oneTunnel = (TunnelEntrance) oneRail; /* Ezért meg fõleg. */
					if(oneTunnel.checkIfActivated()){
						oneTunnel.deActivate();
						activeEntranceCounter--;
						activatedTunnelEntranceFound = true;
						break;
					}
				}
			}
		}
		System.out.println("Class: GameController\t Object: GameController@STATIC\t Aktív alagútszájak száma: "+activeEntranceCounter);
	}
	
	
	/**
	 * Kizárólag a teszteléshez létrehozott metódus.
	 * A pályán megkeresi az elsõ váltót amit talál és beállítja a kívánt állapotra.
	 * 
	 * A getClass fv csak azért kell bele, mert a végsõ verzióban a GUI-n történõ kattintásból egybõl meg fogjuk tudni az eventet kiváltó objektum id-jét,
	 * GUI hiányában azonban erre nincs lehetõségünk. Ha nagyon szükséges, meg tudjuk oldani enélkül is (de az ocsmányabb lenne) ezért ennél maradtunk.
	 */
	public void skeletonTesterSwitchASwitch(){
		Boolean switchFound = false;
		while(!switchFound){
			for(Rail oneRail:railCollection){
				if(oneRail.getClass() == Switch.class){  /*  Az ilyenért lehet kibasznak tbh.. :D */
					Switch oneSwitch = (Switch) oneRail; /* Ezért meg fõleg. */
					oneSwitch.switchRail();
					switchFound = true;
					break;
					
				}
			}
		}
	}
	
	
	/**
	 * Kizárólag a teszteléshez létrehozott metódus
	 * A Main-tõl megadott paraméterekkel létrehoz egy vonatot,
	 * 
	 * @param cabinColors
	 */
	public void skeletonTesterAddNewTrain(ArrayList<Color> cabinColors){
		
		System.out.println("\nClass: GameController\t Object: GameController@STATIC\t Új vonat hozzáadása");
		Train testTrain = new Train(cabinColors);
		
		EnterPoint enterPoint = null;
		
		Boolean enterPointFound = false;
		while(!enterPointFound){
			for(Rail oneRail:railCollection){
				if(oneRail.getClass() == EnterPoint.class){  /*  Az ilyenért lehet kibasznak tbh.. :D */
					enterPoint = (EnterPoint) oneRail;	
					enterPointFound = true;
				}
			}
		}
		
		
		testTrain.setNextRail(enterPoint);
		
		trainCollection.addNewTrain(testTrain);
	}

}
