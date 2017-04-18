package sandbox;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * A játékmenet vezérléséért felel. Betölti a játékos által kiválasztott pályát fájlból, felépíti a pályához tartozó sín hálózatot és 
 * vezérli melyik EnterPoint-on milyen ütemezéssel hány darab vonat érkezzen meg. Ez az osztály felel a vonatok sebességének meghatározásáért, 
 * és a játék kimenetelének eldöntéséért és ennek lekezeléséért. Felelõs továbbá annak felügyeléséért, hogy csak két alagútszáj lehessen egyszerre aktív.
 */
public class GameController {
	
	/**
	 * Megadja, hogy éppen folyamatban van-e játék. True, ha igen, false ha nem.
	 */
	private static Boolean isTheGameRunning;
	
	/**
	 * A síneket tároló arraylist. A pálya betöltése után ebben tároljuk el a teljes sínhálózatot
	 */
	private static ArrayList<Rail> railCollection;
	
	/**
	 * A vonatokat tároló kollekció. A játék futása során az adott pályához tartozó ütemezés szerint adagoljuk 
	 * a pályához tartozó hosszúságú vonatokat a tárolóba a tároló addTrain(Train) metódusával.
	 */
	private static TrainCollection trainCollection;
	
	/**
	 * Az aktív alagútszályak számát tárolja. Segítségével, ha két aktív alagútszály van, akkor létrejöhet az alagút közöttük.
	 */
	private static int activeEntranceCounter;
	
	/**
	 * Ebben tároljuk, melyik pályát indítottuk el legutoljára.
	 */
	private static int lastPlayedMapNumber;
	
	
	/**
	 * A GameController konstruktora.
	 */
	public GameController(){
		System.out.println("Class: GameController\t Object: GameController@STATIC\t Method: Constructor\t ");/* Kiíratás a Szkeleton vezérlésének */
		isTheGameRunning = false; /* Alapból nem fut a játék */
		railCollection=new ArrayList<Rail>(); /* Új listát hozunk létre */
		trainCollection = new TrainCollection(); /* új kollekciót hozunk létre. */
	}
	
	
	/**
	 * Új játék indítására szolgáló függvény. Megjeleníti a játékosnak a választható pályák listáját. 
	 * Miután a játékos kiválasztotta melyik pályán akar játszani, betölti a pályához tartozó sínhálózatot és vonat ütemezést. 
	 * Ezután elindítja a vonatok léptetéséért felelõs szálat.
	 * 
	 * @param mapNumber	a betöltendõ pálya sorszáma
	 */
	public static void startNewGame(int mapNumber){
		System.out.println("Class: GameController\t Object: Object: GameController@STATIC\t Method: startNewGame\t Param: "+mapNumber); /* Kiíratás a Szkeleton vezérlésének */
		
		String mapName = new String("maps/map" + mapNumber + ".txt"); /* mivel több pálya is lehet, ezért dinamikusan állítjuk össze a betöltendõ nevet. */
		try {
			buildFromFile(mapName); /* Megpróbáljuk a fájlból felépíteni a pályát */
			isTheGameRunning=true; /*Elinditjuka játkot*/
			System.out.println("\nClass: GameController\t Object: GameController@STATIC\t A játék elindult\n"); /* Kiíratás a Szkeleton vezérlésének */
			lastPlayedMapNumber = mapNumber; /* Elmentjük melyik pályát töltöttük be utoljára. */
		} catch (IOException e) {
			System.err.println("\nClass: GameController\t Object: GameController@STATIC\t HIBA A PÁLYA BETÖLTÉSE KÖZBEN\n"); /* Ha véletlen nem lehet betölteni a pályát, akkor küldünk egy értesítst errõl */
		}
	}
	
	
	/**
	 * Értesíti a játékost, hogy nyert, és leállítja a játékot.
	 */
	public static void winEvent(){
		isTheGameRunning = false; /* Leállítjuk a játékot. Ez majd a GUI-t futtató threadnél lesz fontos */
		System.out.println("Class: GameController\t Object: GameController@STATIC\t Method: winEvent\t Gyozelem."); /* Kiíratás a Szkeleton vezérlésének */
		if(lastPlayedMapNumber == 1){ /* Ha az elsõ pálya volt az amit elõbb játszottunk, akkor betöltjük a másodikat. */
			System.out.println("Class: GameController\t Object: GameController@STATIC\t Van még új pálya, így az következik."); /* Kiíratás a Szkeleton vezérlésének */
			System.out.println("\n\nÚJ JÁTÉK KEZDÕDIK \n\n"); /* Kiíratás a Szkeleton vezérlésének */
			startNewGame(2); /* Elindul az új játék */
		} else {
			System.out.println("Class: GameController\t Object: GameController@STATIC\t Nincs tobb palya, jatek vege."); /* Kiíratás a Szkeleton vezérlésének */
		}
	}
	
	
	/**
	 * Értesíti a játékost, hogy vesztett, és leállítja a játékot.
	 */
	public static void loseEvent(){
		isTheGameRunning = false; /* Leállítjuk a játékot */
		System.out.println("Class: GameController\t Object: GameController@STATIC\t Method: loseEvent\t Vereseg"); /* Kiíratás a Szkeleton vezérlésének */
		System.out.println("Class: GameController\t Object: GameController@STATIC\t Michael Bay Effekt"); /* Kiíratás a Szkeleton vezérlésének */
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
	 *  @param	filename	a beolvasandó file neve
	 *  
	 */
	
	private static void buildFromFile(String filename) throws IOException{
		System.out.println("Class: GameController\t Object: GameController@STATIC\t Method: buildFromFile\t Param: "+filename+"\t Betoltes."); /* Kiíratás a Szkeleton vezérlésének */
		
		/*Kezdetben megállítjk a játékot és töröljük azelözö listákat*/
		isTheGameRunning=false;
		railCollection.clear();
		trainCollection.clear();
		System.out.println("Class: GameController\t Object: GameController@STATIC\t Method: railCollection.clear"); /* Kiíratás a Szkeleton vezérlésének */
		
		
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
				if(tempMap[x][y]!=null){
					tempMap[x][y].setX(x);
					tempMap[x][y].setY(y);
				}

				tempView[x][y]=s; /*Mentjük a vázlatát*/		
				x++;
			}
			x=0;
			y++;
		}
		
		
		System.out.println("\nClass: GameController\t Object: GameController@STATIC\t Sínek közötti kapcsolatok létrehozása."); /* Kiíratás a Szkeleton vezérlésének */
		for(int i=0;i<width;i++){ /*Végignézzük a pályát szélességben...*/
			for(int j=0;j<height;j++){	/*... és magasságban*/
				if(tempMap[i][j]!=null){		/*Ha az aktuális elem nem üres, akkor lehetnek szomszédai*/		
					ArrayList<Rail> tmp = new ArrayList<Rail>(); /*új "szomszéd tároló"*/
					
					
					
					if(i-1>=0 && tempMap[i-1][j]!=null) tmp.add(tempMap[i-1][j]); /*Ha balra lévö mezö a pálya része és Rail akkor a szomszédja*/
					if(i+1<width && tempMap[i+1][j]!=null) tmp.add(tempMap[i+1][j]);/*Ha jobbra lévö mezö a pálya része és Rail akkor a szomszédja*/
					if(j-1>=0 && tempMap[i][j-1]!=null) tmp.add(tempMap[i][j-1]);/*Ha felette lévö mezö a pálya része és Rail akkor a szomszédja*/
					if(j+1<height && tempMap[i][j+1]!=null) tmp.add(tempMap[i][j+1]);/*Ha alatta lévö mezö a pálya része és Rail akkor a szomszédja*/
					
					/*Itt a sorrend számít, mert az XRail az elsö kettöt és a második kettöt kapcsolja párba*/
					
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
            
        case "X":
        	System.out.println("Beolvasott elem: XRail");
            return new XRail(); /* Létrehozunk egy új XRail-t. */
           
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
     * 
     * @return Ha mindegyik vonat kiürült, akkor igazzal térünk vissza, egyébként hamissal.
     */ 
    private boolean hasTheGameEnded(){
    	System.out.println("Class: GameController\t Object: GameController@STATIC\t Method: hasTheGameEnded\t Vonatok uressegenek ellenorzese"); /* Kiíratás a Szkeleton vezérlésének */
    	Boolean isAllEmpty = trainCollection.isAllEmpty(); /* Ha mindegyik vonat kiürült, akkor igazzal térünk vissza, egyébként hamissal. */
    	System.out.println("Class: GameController\t Object: GameController@STATIC\t Returned: "+isAllEmpty); /* Kiíratás a Szkeleton vezérlésének */
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
		for(int i=0; i < tunnelEntranceCounterToBeActivated; i++){ /* Annyi alagútszájat keresünk, amennyit paraméterül megadtak */
			Boolean notActivatedTunnelEntranceFound = false; 
			while(!notActivatedTunnelEntranceFound){ /* amíg nem találunk egy nem aktivált alagútszájat */
				for(Rail oneRail:railCollection){
					if(oneRail.getClass() == TunnelEntrance.class){  /* megnézzük TunnelEntrance-e a sín. */
						TunnelEntrance oneTunnel = (TunnelEntrance) oneRail;  /* Ha igen, akkor átkasztoljuk */
						if(!oneTunnel.checkIfActivated()){ /* Ha még nincs aktiválva */
							oneTunnel.activate(); /* Aktiváljuk, és megnöveljük az aktív alagútszájak számát eggyel */
							activeEntranceCounter++;
							notActivatedTunnelEntranceFound = true;
							break; /* Break, kezdõdik a következõ keresésése, ha erre szükség van. */
						}
					}
				}
			}
			
		}
		System.out.println("Class: GameController\t Object: GameController@STATIC\t Aktív alagútszájak száma: "+activeEntranceCounter);/* Kiíratás a Szkeleton vezérlésének */
		if(activeEntranceCounter == 2){ /* Ha két aktív alagútszájunk van, akkor kezdeményezzük az alagút létrehozását. */
			System.out.println("\nClass: GameController\t Object: GameController@STATIC\t Alagút létrehozása");/* Kiíratás a Szkeleton vezérlésének */
			
			/*  Disclaimer: Az alábbi kódot Long csinálta, szóval God have mercy on your soul, if you want to understand this. */
			Rail entrance1, entrance2;
			entrance1 = null;
			entrance2 = null;
			
			
			
			/*Kikeressük a kettöt*/
			for(Rail rail:railCollection){
				if(rail.getClass() == TunnelEntrance.class){
					if(entrance1==null){
						entrance1=rail;
					}else{
						entrance2=rail;
					}
				}
			}
			/*Segédváltozók*/
			int e1X=entrance1.getX();
			int e1Y=entrance1.getY();
			int e2X=entrance2.getX();
			int e2Y=entrance2.getY();
			
			ArrayList<Rail> newTunnels= new ArrayList<Rail>();
			Tunnel tmp;
			
			/*Az elsõ része a belépési pont lesz*/
			newTunnels.add(entrance1);
			
			/*Viszintes tengelyen vizsgáljuk*/
			/*Ha az elsö alagut jobbrább van*/
			
			if(e1X>e2X){										
				while(e1X>e2X){									/*Amig nem érünk egy szintre a kijárattal*/
					if(e1X-1==e2X && e1Y==e2Y){ 				/*Pont tõle jobbra van a kijárat*/
						newTunnels.add(entrance2);				/*Bekötjük a kijárathoz*/
						e1X--;
					}else{ 										/*Ha ez még csak egy alagut*/
						e1X--; 									/*Közelebb hozzuk*/
						tmp=new Tunnel();						/*Létrehozzuk és beállítjuk az adatait*/
						tmp.setX(e1X);
						tmp.setY(e1Y);
						newTunnels.add(tmp); 					/*Felépitjük*/
					}
				}
			}
			/*Ha az elsö alagut balrább van*/
			else if(e1X<e2X){
				while(e1X<e2X){									/*Amig nem érünk egy szintre a kijárattal*/
					if(e1X+1==e2X && e1Y==e2Y){				/*Pont tõle balrara van a kijárat*/
						newTunnels.add(entrance2);				/*Bekötjük a kijárathoz*/
						e1X++;
					}else{ 										/*Ha ez még csak egy alagut*/
						e1X++; 									/*Közelebb hozzuk*/
						tmp=new Tunnel();						/*Létrehozzuk és beállítjuk az adatait*/
						tmp.setX(e1X);
						tmp.setY(e1Y);
						newTunnels.add(tmp); 					/*Felépitjük*/
					}
				}
			}
			
			/*Függöleges tengelyen vizsgáljuk*/
			/*Ha az elsö alagut feljebb van*/
			if(e1Y>e2Y){										
				while(e1Y>e2Y){									/*Amig nem érünk egy szintre a kijárattal*/
					if(e1Y-1==e2Y && e1X==e2X){ 				/*Pont felette van a kijárat*/
						newTunnels.add(entrance2);				/*Bekötjük a kijárathoz*/
						e1Y--;
					}else{ 										/*Ha ez még csak egy alagut*/
						e1Y--; 									/*Közelebb hozzuk*/
						tmp=new Tunnel();						/*Létrehozzuk és beállítjuk az adatait*/
						tmp.setX(e1X);
						tmp.setY(e1Y);
						newTunnels.add(tmp); 					/*Felépitjük*/
					}
				}
			}
			/*Ha az elsö alagut lejjebb van*/
			else if(e1Y<e2Y){
				while(e1Y<e2Y){									/*Amig nem érünk egy szintre a kijárattal*/
					if(e1Y+1==e2Y && e1X==e2X){ 				/*Pont alatta van a kijárat*/
						newTunnels.add(entrance2);				/*Bekötjük a kijárathoz*/
						e1Y++;
					}else{ 										/*Ha ez még csak egy alagut*/
						e1Y++;									/*Közelebb hozzuk*/
						tmp=new Tunnel();						/*Létrehozzuk és beállítjuk az adatait*/
						tmp.setX(e1X);
						tmp.setY(e1Y);
						newTunnels.add(tmp); 					/*Felépitjük*/
					}
				}
			}
			
			/*Minden alagutelemet felvettünk*/			
			/*Összekötjük öket*/
			for(int i=0;i<newTunnels.size();i++){
				if(i-1>=0)newTunnels.get(i).addNeighbourRail(newTunnels.get(i-1));
				if(i+1<newTunnels.size())newTunnels.get(i).addNeighbourRail(newTunnels.get(i+1));
				if(i>0&&i<newTunnels.size()-1)railCollection.add(newTunnels.get(i));
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
				
		Boolean activatedTunnelEntranceFound = false;
		while(!activatedTunnelEntranceFound){ /* amíg nem találunk egy aktivált  */
			for(Rail oneRail:railCollection){
				if(oneRail.getClass() == TunnelEntrance.class){   /* Megkeressük a TunnelEntrancet */
					TunnelEntrance oneTunnel = (TunnelEntrance) oneRail; /* átkasztoljuk */
					if(oneTunnel.checkIfActivated()){ /* Ellenõrizzük, hogy aktiválva van-e */
						oneTunnel.deActivate(); /* Deaktiváljuk, mely során kitörlõdik a szomszédlistájából a referencia az alagútra */
						activatedTunnelEntranceFound = true;
						break;
					}
				}
			}
		}
		
		if(activeEntranceCounter == 2){ /* Ha két aktív TunnelEntrance volt */
			System.out.println("\nClass: GameController\t Object: GameController@STATIC\t Alagút lerombolása"); /* Kiíratás a Szkeleton vezérlésének */
			
			Boolean railCollectionIsFreeOfTunnels = false; /* A railCollectionben még vannak Tunnelek. Mivel deaktiváltunk egy alagútszájat, az eddigi alagutat meg kell szûntetni. */
			
			while(!railCollectionIsFreeOfTunnels){ /* Amíg nem lesz Tunnel mentes a railCollection */
				railCollectionIsFreeOfTunnels = true; /* Feltesszük, hogy üres, ha nem volt az akkor ezt a for-ban átírjuk. */
				Rail railToGetDeleted = null; /* Ezt akarjuk majd törölni */
				
				for(Rail rail:railCollection){ /* Végigmegyünk az összes sínen */
					if(rail.getClass() == Tunnel.class){ /* Ha találtunk egy tunnelt, azt megjelöljük mint törlendõ sín és beállítjuk, hogy még nem volt tiszta a railCollection */
						railToGetDeleted = rail;
						railCollectionIsFreeOfTunnels = false;
					}
				}
				
				if(!railCollectionIsFreeOfTunnels){ /* Ha talátunk törlendõ elemet akkor azt itt most ki is töröljük. */
					railCollection.remove(railToGetDeleted); /* Kitöröljük */
				}
			}
			
		}
		
		activeEntranceCounter--; /* eggyel kevesebb aktív alagútszáj lett. */
		System.out.println("Class: GameController\t Object: GameController@STATIC\t Aktív alagútszájak száma: "+activeEntranceCounter); /* Kiíratás a Szkeleton vezérlésének */
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
		while(!switchFound){ /* Amíg nem találunk egy switch-et */
			for(Rail oneRail:railCollection){
				if(oneRail.getClass() == Switch.class){  /* végignézzük melyik a switch */
					Switch oneSwitch = (Switch) oneRail;  /* átkasztoljuk */
					oneSwitch.switchRail(); /* változtatunk az állapotán egyet */
					switchFound = true;
					break; /* ha szükséges keresünk egy következõt. */ 
					
				}
			}
		}
	}
	
	/**
	 * Váltót lehet vele állítani, meg alagutat építeni.
	 * Ha a paraméterül kapott helyen lévõ elemrõl kideríti, hogy alagútszáj-e; ha igen, akkor aktiválva van-e.
	 * Ha igen, deaktiválja, ha nem , aktiválja. Ha ez alagút építést/bontást von maga után, akkor megteszi.
	 * Ezzel egyidõben átállítja az alagútszáj váltóját is (aktiválás -> be az alagútba, deaktiválás -> alaphelyzetbe).
	 * Ha a kattintott elem váltó, akkor átállítja.
	 * Jelenleg nincs TELJESEN kész, a grafikus részben ezt fel kell majd iratkoztatni a kattintásra.
	 * Jelenleg a kapott koordináta így nem a kattintásé, hanem a konkrét elemé amire kattintani akarunk.
	 * Ha mondjuk a cellák 20*20 pixelesek lesznek és ide az érkezik, hogy 33, 21, akkor ebbõl még le kell hozni, hogy ez az 1-1-es indexû cella.
	 * Ha ez megvan onnantól viszont ez már jó kell hogy legyen
	 * 
	 * @param X	Kattintás X koordinátája
	 * @param Y	Kattintás Y koordinátája
	 */
	
	public void clickHandler(int X, int Y){
		for(Rail rail:railCollection){
			if(rail.getX() == X && rail.getY() == Y){  /* Megkeressük a kattintott elemet */
				try {
					TunnelEntrance thisEntrance = (TunnelEntrance)rail;			/* megpróbáljuk átkasztolni */
					thisEntrance.switchRail();					/* ha sikerült, átállítjuk */
					
					if (thisEntrance.checkIfActivated()) {
						thisEntrance.deActivate();
						activeEntranceCounter--;
						if (activeEntranceCounter == 1) {				/* bontani kell */
							for (Rail rail2 : railCollection) {			/* kikeressük a másik nyitott alagútszájat */
								try {
									TunnelEntrance otherEntrance = (TunnelEntrance)rail2;
									if (otherEntrance.checkIfActivated() == false) {
										continue;						/* ha zárt, folytatjuk a keresést */
									}
									
									/* +  +  +  +  Innentõl Long kódja a skeletonTesterbõl +  +  +  +  */
									
									System.out.println("\nClass: GameController\t Object: GameController@STATIC\t Alagút lerombolása"); /* Kiíratás a Szkeleton vezérlésének */
									
									Boolean railCollectionIsFreeOfTunnels = false; /* A railCollectionben még vannak Tunnelek. Mivel deaktiváltunk egy alagútszájat, az eddigi alagutat meg kell szûntetni. */
									
									while(!railCollectionIsFreeOfTunnels){ /* Amíg nem lesz Tunnel mentes a railCollection */
										railCollectionIsFreeOfTunnels = true; /* Feltesszük, hogy üres, ha nem volt az akkor ezt a for-ban átírjuk. */
										Rail railToGetDeleted = null; /* Ezt akarjuk majd törölni */
										
										for(Rail rail3:railCollection){ /* Végigmegyünk az összes sínen */
											if(rail3.getClass() == Tunnel.class){ /* Ha találtunk egy tunnelt, azt megjelöljük mint törlendõ sín és beállítjuk, hogy még nem volt tiszta a railCollection */
												railToGetDeleted = rail3;
												railCollectionIsFreeOfTunnels = false;
											}
										}
										
										if(!railCollectionIsFreeOfTunnels){ /* Ha talátunk törlendõ elemet akkor azt itt most ki is töröljük. */
											railCollection.remove(railToGetDeleted); /* Kitöröljük */
										}
									}
									break;
									
								} catch (Exception e) {
									// TODO: handle exception
								}
							}
						}
					} 
					
					else {
						thisEntrance.activate();
						activeEntranceCounter++;
						if (activeEntranceCounter == 2) {				/* építeni kell */
							for (Rail rail2 : railCollection) {			/* kikeressük a másik nyitott alagútszájat */
								try {
									TunnelEntrance otherEntrance = (TunnelEntrance)rail2;
									if (thisEntrance.equals(otherEntrance) || otherEntrance.checkIfActivated() == false) {
										continue;						/* ha ugyanazt az alagútszájat találtuk meg megint, vagy zárt, folytatjuk a keresést */
									}
									
									/* +  +  +  +  Innentõl Long kódja a skeletonTesterbõl +  +  +  +  */
									
									/*Segédváltozók*/
									int e1X=thisEntrance.getX();
									int e1Y=thisEntrance.getY();
									int e2X=otherEntrance.getX();
									int e2Y=otherEntrance.getY();
									
									ArrayList<Rail> newTunnels= new ArrayList<Rail>();
									Tunnel tmp;
									
									/*Az elsõ része a belépési pont lesz*/
									newTunnels.add(thisEntrance);
									
									/*Viszintes tengelyen vizsgáljuk*/
									/*Ha az elsö alagut jobbrább van*/
									
									if(e1X>e2X){										
										while(e1X>e2X){									/*Amig nem érünk egy szintre a kijárattal*/
											if(e1X-1==e2X && e1Y==e2Y){ 				/*Pont tõle jobbra van a kijárat*/
												newTunnels.add(otherEntrance);				/*Bekötjük a kijárathoz*/
												e1X--;
											}else{ 										/*Ha ez még csak egy alagut*/
												e1X--; 									/*Közelebb hozzuk*/
												tmp=new Tunnel();						/*Létrehozzuk és beállítjuk az adatait*/
												tmp.setX(e1X);
												tmp.setY(e1Y);
												newTunnels.add(tmp); 					/*Felépitjük*/
											}
										}
									}
									/*Ha az elsö alagut balrább van*/
									else if(e1X<e2X){
										while(e1X<e2X){									/*Amig nem érünk egy szintre a kijárattal*/
											if(e1X+1==e2X && e1Y==e2Y){				/*Pont tõle balrara van a kijárat*/
												newTunnels.add(otherEntrance);				/*Bekötjük a kijárathoz*/
												e1X++;
											}else{ 										/*Ha ez még csak egy alagut*/
												e1X++; 									/*Közelebb hozzuk*/
												tmp=new Tunnel();						/*Létrehozzuk és beállítjuk az adatait*/
												tmp.setX(e1X);
												tmp.setY(e1Y);
												newTunnels.add(tmp); 					/*Felépitjük*/
											}
										}
									}
									
									/*Függöleges tengelyen vizsgáljuk*/
									/*Ha az elsö alagut feljebb van*/
									if(e1Y>e2Y){										
										while(e1Y>e2Y){									/*Amig nem érünk egy szintre a kijárattal*/
											if(e1Y-1==e2Y && e1X==e2X){ 				/*Pont felette van a kijárat*/
												newTunnels.add(otherEntrance);				/*Bekötjük a kijárathoz*/
												e1Y--;
											}else{ 										/*Ha ez még csak egy alagut*/
												e1Y--; 									/*Közelebb hozzuk*/
												tmp=new Tunnel();						/*Létrehozzuk és beállítjuk az adatait*/
												tmp.setX(e1X);
												tmp.setY(e1Y);
												newTunnels.add(tmp); 					/*Felépitjük*/
											}
										}
									}
									/*Ha az elsö alagut lejjebb van*/
									else if(e1Y<e2Y){
										while(e1Y<e2Y){									/*Amig nem érünk egy szintre a kijárattal*/
											if(e1Y+1==e2Y && e1X==e2X){ 				/*Pont alatta van a kijárat*/
												newTunnels.add(otherEntrance);				/*Bekötjük a kijárathoz*/
												e1Y++;
											}else{ 										/*Ha ez még csak egy alagut*/
												e1Y++;									/*Közelebb hozzuk*/
												tmp=new Tunnel();						/*Létrehozzuk és beállítjuk az adatait*/
												tmp.setX(e1X);
												tmp.setY(e1Y);
												newTunnels.add(tmp); 					/*Felépitjük*/
											}
										}
									}
									
									/*Minden alagutelemet felvettünk*/			
									/*Összekötjük öket*/
									for(int i=0;i<newTunnels.size();i++){
										if(i-1>=0)newTunnels.get(i).addNeighbourRail(newTunnels.get(i-1));
										if(i+1<newTunnels.size())newTunnels.get(i).addNeighbourRail(newTunnels.get(i+1));
										if(i>0&&i<newTunnels.size()-1)railCollection.add(newTunnels.get(i));
									}
									
									break;
								} catch (Exception e) {
									// TODO: handle exception
								}
							}
						}
					}
					rail = thisEntrance;
					break;								/* Itt ki kell breakelni mert egy szívdobbanás alatt átkonvertálná switchre a köv. try-ban */
				} catch (Exception e) {
					System.out.println("A kattintott elem nem tunnelEntrance");
				}
				
				/*  Megnézzük switchre kattintott e a játékos és ha igen akkor mit kell tenni */
				try {
					Switch sw = (Switch)rail;			/* megpróbáljuk átkasztolni */
					sw.switchRail();					/* ha sikerült, átállítjuk */
					rail = sw;
				} catch (Exception e) {
					System.out.println("A kattintott elem nem switch");
				}
				break;
			}
		}
	}
	
	
	/**
	 * Kizárólag a teszteléshez létrehozott metódus.
	 * A pályán megkeresi az elsõ tunnelEntrancet amit talál és beállítja a kívánt állapotra.
	 * 
	 * A getClass fv csak azért kell bele, mert a végsõ verzióban a GUI-n történõ kattintásból egybõl meg fogjuk tudni az eventet kiváltó objektum id-jét,
	 * GUI hiányában azonban erre nincs lehetõségünk. Ha nagyon szükséges, meg tudjuk oldani enélkül is (de az ocsmányabb lenne) ezért ennél maradtunk.
	 */
	public void skeletonTesterSwitchATunnelEntrance(){
		Boolean tunnelEntranceFound = false;
		while(!tunnelEntranceFound){ /* Keresünk egy TunnelEntrance-t */
			for(Rail oneRail:railCollection){
				if(oneRail.getClass() == TunnelEntrance.class){  /* végignézzük melyik a TunnelEntrance */
					TunnelEntrance oneTunnelEntrance = (TunnelEntrance) oneRail; /* átkasztoljuk */
					oneTunnelEntrance.switchRail();/* változtatunk az állapotán egyet */
					tunnelEntranceFound = true;
					break;/* ha szükséges keresünk egy következõt. */ 
					
				}
			}
		}
	}
	
	
	/**
	 * Kizárólag a teszteléshez létrehozott metódus
	 * A Main-tõl megadott paraméterekkel létrehoz egy vonatot,
	 * 
	 * @param cabinColors	A kabinok színének listája amibõl a vonat felépül
	 */
	public void skeletonTesterAddNewTrain(ArrayList<Color> cabinColors){
		
		System.out.println("\nClass: GameController\t Object: GameController@STATIC\t Új vonat hozzáadása");/* Kiíratás a Szkeleton vezérlésének */
		Train testTrain = new Train(cabinColors); /* A megadott paraméterekkel létrehozunk egy új vonatot */
		
		EnterPoint enterPoint = null;
		
		Boolean enterPointFound = false; /* Kikeressük melyik a railCollection-ben az EnterPoint és azt eltároljuk, hogy utána beállítsuk elsõ sínként a vonatnak. */
		while(!enterPointFound){
			for(Rail oneRail:railCollection){
				if(oneRail.getClass() == EnterPoint.class){ 
					enterPoint = (EnterPoint) oneRail;	
					enterPointFound = true;
				}
			}
		}
		
		testTrain.setNextRail(enterPoint); /* Beállítjuk elsõ sínnek a belépési pontot */
		trainCollection.addNewTrain(testTrain); /* Hozzáadjuk a trainCollection-be az új vonatot. */
	}
	
	/**
	 * Kizárólag a teszteléshez létrehozott metódus
	 * Lépteti a vonatokat egyszer.
	 */
	public void skeletonTesterMakeTrainsMove(){
		System.out.println("\nClass: GameController\t Object: GameController@STATIC\t Ütközéshez szükséges vonat hossz számlálók csökkentése");/* Kiíratás a Szkeleton vezérlésének */
		for(Rail oneRail: railCollection){ /* Minden eggyes sínnek csökkentjük eggyel a rajta még áthaladó kabinok számát, mivel lép egyet minden vonat. */
			oneRail.lowerTrainLenghtCounter();
		}
		
		System.out.println("\nClass: GameController\t Object: GameController@STATIC\t Vonatok léptetése"); /* Kiíratás a Szkeleton vezérlésének */
		trainCollection.moveAllTrains(); /* Minden egyes vonatot léptetünk eggyel. Késõbb ezt egy thread fogja csinálni, jelen szkeletonban elég ennyi. */
	}
	
	/**
	 * Kizárólag a teszteléshez létrehozott metódus
	 * 
	 * Kiiratjuk a felépített pálya adatait egy fájla hogy késöbb elelnörizhessük öket
	 * @param mapNumber	a betöltendõ pálya száma
	 * @param name ez lesz a létrehozott fájl neve
	 * @throws IOException 
	 */
	public void createMapTestFile(int mapNumber, String name) throws IOException{
		PrintWriter writer = new PrintWriter(name+".txt", "UTF-8"); /*Kell egy TXT íro*/
		
		String[] splitresult; 							/*Egyetlen sor felbontása*/
		String result; 									/*Egy sor egy részletét tárolja*/
		for(Rail rail:railCollection){         	 		/*Megnézünk minden sint*/
			splitresult=rail.toString().split("@");		/*Felmbontjuk az objekumot a pointere mentén*/
			result=rail.getClass().getSimpleName();		/*Elkérjük az objektum osztályát*/
			
			
			/*Ha állomásunk volt, annak szine is van, akkor a színre is szükségünk lesz*/
			if(rail.getClass().getSimpleName().toString().trim().equals("TrainStation")){ 
				Color c =((TrainStation)rail).getColor();
				if (c.equals(Color.RED)) result+= "1";			/*Ha piros volt, a neve után írunk egy 1 est*/
				if (c.equals(Color.GREEN)) result+= "2";		/*Ha zöld volt, a neve után írunk egy 2 est*/
				if (c.equals(Color.BLUE)) result+= "3";			/*Ha kék volt, a neve után írunk egy 3 mast*/
			}
			
			
			/*Egyebe füzzük az adatokat a következö séma szerint:*/
			/*név [x,y] (pointer) {szomszéd1, szomszéd2,...} */
			result+=" ["+ rail.getX() +","+rail.getY()+"]"+
					" ("+ splitresult[1] +")";
			result+=" {";
			int comacounter=rail.getNeighbourRails().size() -1; /*Elemszám -1 vesszöt kell letennünk majd a fájlba*/
			for(Rail neighbour:rail.getNeighbourRails()){
				splitresult=neighbour.toString().split("@");
				result+=splitresult[1];
				if(comacounter>0){ /*Számoljuk hogy hány vesszöt tettünk le a szomszdok utána*/
					comacounter--;
					result+=",";
				}
			}
			result+="}"; /*Lezárjuk*/
			writer.println(result); /*Kiiratjuk a fájlba*/
		}
		writer.close(); /*Bezárjuk az olvasást*/
		System.out.println("Save Done");
		MapCreationTest.main("maps/map" + mapNumber + ".txt", name+".txt"); /*Indítjuk a tesztet*/
	}
	/**
	 * A száékezelés tesztelésére írt egyszerü program, fix idöközönként generál egy történést.
	 * 
	 */
	public void runThreadTest(){
		Thread testThread = new Thread(){ 	/*Új Thread hgoy a háttérben fusson*/
			ArrayList<Color> testColors; 	/*Vonat kocsiszíneit tárolja*/
			Train testTrain;				/*Egy teszt vonat*/
			EnterPoint enterPoint;			/*Egy véletlenül talált belépési pont*/
			
			public void run() {				/*Megírjuk az esemnyeket*/
				try {
					testColors=new ArrayList<Color>();  /*Inicializáljuk a listát*/
					testColors.add(Color.BLUE);			/*Színt adunk hozzá*/
					testTrain = new Train(testColors);	/*A szinekkel inicializálunk egy vonatot*/
		        
					for(Rail oneRail:railCollection){	/*Kikeresünk egy belépésíi pontot*/
						if(oneRail.getClass() == EnterPoint.class){ 
							enterPoint = (EnterPoint) oneRail;	
						}
					}
					
					if(enterPoint==null){ /*Amennyiben null az objektumunk, nem volt belépési pont a pályán*/
						System.out.println("Nincs belépési pont a pályán");
						return;		/*Ilyenkor vége a futásnak*/
					}
					
					/*A belépési pontra rakjuk a vonatunk*/
					testTrain.setNextRail(enterPoint); 	
					trainCollection.addNewTrain(testTrain);
					
					
					/*16 léptetést hajtunk végre*/
					for(int i=0;i<16;i++){
						for(Rail oneRail: railCollection){ /* Minden eggyes sínnek csökkentjük eggyel a rajta még áthaladó kabinok számát, mivel lép egyet minden vonat. */
							oneRail.lowerTrainLenghtCounter();
						}
						trainCollection.moveAllTrains();				
						drawToConsole();	/*Közben frissítjuk a nézetet*/
						Thread.sleep(1000);
					}

					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		};
		
		testThread.start(); /*Indítuk a szálat*/
	}
	
	/**
	 * kirajzolja a pályát a konzolra
	 */
	public void drawToConsole(){
		
		int maxX = 0;		/* mi a legnagyobb x index ( = pályaszélesség - 1) */
		int maxY = 0;		/* mi a legnagyobb y index ( = pályamagasság - 1) */
		for (Rail rail : railCollection) {
			if (rail.getY() > maxY) {
				maxY = rail.getY();
			}
			if (rail.getX() > maxX) {
				maxX = rail.getX();
			}
		}
		
		Rail[][] mapToDraw = new Rail[maxY + 1][maxX + 1];	/* sajnos a railcollectionben nem sorfojtonosan egymás
														után vannak az elemek. Kell egy map helyette */
		for (Rail rail : railCollection) {
			mapToDraw[rail.getY()][rail.getX()] = rail;	/*feltöltjük a mapet */
		}
		
		char[][] charMap = new char[maxY + 1][maxX + 1]; /* ebben vannak a konkrét karakterek amiket ki fogunk írni */
		for (char[] cs : charMap) {
			for (char c : cs) {
				c = ' '; 								/* kezdetben minden legyen üres, majd felül lesznek írva elemekkel */	
			}
		}
		
		
		for (int line = 0; line < maxY + 1; line++) {	/* charmap feltöltése kiírandó betûkkel a mapToDraw alapján */
			for (int col = 0; col < maxX + 1; col++) {
				if (mapToDraw[line][col] == null) {		/* Ha egy cella üres a mapToDraw-ban, akkor az a charMap-ben ' ' marad */
					continue;
				}
				switch (mapToDraw[line][col].getClass().getSimpleName()) {
				case "Rail":
					charMap[line][col] = 'R';
					break;
				case "EnterPoint":
					charMap[line][col] = 'E';
					break;
				case "Switch":
					charMap[line][col] = 'S';
					break;
				case "Tunnel":
					charMap[line][col] = 'T';
					break;
				case "TunnelEntrance":
					charMap[line][col] = 'U';
					break;
				case "XRail":
					charMap[line][col] = 'X';
					break;
				case "TrainStation":
					TrainStation ts = (TrainStation)mapToDraw[line][col];
					if (ts.getColor() == Color.RED) {
						charMap[line][col] = '1';
					}
					if (ts.getColor() == Color.GREEN){
						charMap[line][col] = '2';
					}
					if (ts.getColor() == Color.BLUE){
						charMap[line][col] = '3';
					}

				default:
					charMap[line][col] = ' ';
					break;
				}
			}
		}
		
		/*------------------- Idáig csak a statikus pálya kiírása volt, innetõl jön a vonat berakása -------------------*/
		
		for (int line = 0; line < maxY + 1; line++) {	/* foglalt sínt átírjuk 'V'-re (V mint Vonat) Sajnos a síneknek fogalmuk sincs pontosan mi lépett rájuk */
			for (int col = 0; col < maxX + 1; col++) {
				if (mapToDraw[line][col] == null) {
					continue;
				}
				if (mapToDraw[line][col].checkIfOccupied()) {
					if (charMap[line][col] != 'T') {		/* Alagútban nem látszódhat a vonat. Ha a betû eredetilet 'T' volt, meghagyjuk */
						charMap[line][col] = 'V';
					}
				}
			}
		}
		
		
		for (char[] cs : charMap) {			/* maga a konzolra írás */
			for (char c : cs) {
				System.out.print(c);
			}
			System.out.println();
		}
	}

}
