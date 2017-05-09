package sandbox;

import java.awt.Color;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * A játékmenet vezérléséért felel. Betölti a játékos által kiválasztott pályát
 * fájlból, felépíti a pályához tartozó sín hálózatot és vezérli melyik
 * EnterPoint-on milyen ütemezéssel hány darab vonat érkezzen meg. Ez az osztály
 * felel a vonatok sebességének meghatározásáért, és a játék kimenetelének
 * eldöntéséért és ennek lekezeléséért. Felelos továbbá annak felügyeléséért,
 * hogy csak két alagútszáj lehessen egyszerre aktív.
 */
public class GameController {

	/**
	 * Megadja, hogy éppen folyamatban van-e játék. True, ha igen, false ha nem.
	 */
	private static Boolean isTheGameRunning;

	/**
	 * A síneket tároló arraylist. A pálya betöltése után ebben tároljuk el a
	 * teljes sínhálózatot
	 */
	private static ArrayList<Rail> railCollection;

	/**
	 * A vonatokat tároló kollekció. A játék futása során az adott pályához
	 * tartozó ütemezés szerint adagoljuk a pályához tartozó hosszúságú
	 * vonatokat a tárolóba a tároló addTrain(Train) metódusával.
	 */
	private static TrainCollection trainCollection;

	/**
	 * Az aktív alagútszályak számát tárolja. Segítségével, ha két aktív
	 * alagútszály van, akkor létrejöhet az alagút közöttük.
	 */
	private static int activeEntranceCounter;

	/**
	 * Ebben tároljuk, melyik pályát indítottuk el legutoljára.
	 */
	private static int lastPlayedMapNumber;

	// LOOOOOOOOOOOOONG
	private static GUI gui;
	private static Thread mainThread;
	private static Thread controllThread;
	private static int STEPTIME = 1000;
	private static boolean fail = false;
	private static boolean stopControll = false;
	private static int speed = 2;
	private static int stepParts = 20;
	private static boolean continueLevels = false;
	private static int levelCounter = 1;

	private static String scheduledTrains;
	private static int trainScheduleTimer;
	// LOOOOOOOOOOOOONG

	/**
	 * A GameController konstruktora.
	 */
	public GameController() {
		System.out.println("Class: GameController\t Object: GameController@STATIC\t Method: Constructor\t ");/*
																												 * Kiíratás
																												 * a
																												 * Szkeleton
																												 * vezérlésének
																												 */
		isTheGameRunning = false; /* Alapból nem fut a játék */
		railCollection = new ArrayList<Rail>(); /* ? j listát hozunk létre */
		trainCollection = new TrainCollection(); /*
													 * új kollekciót hozunk
													 * létre.
													 */

		gui = new GUI();
		;
	}

	/**
	 * Új játék indítására szolgáló függvény. Megjeleníti a játékosnak a
	 * választható pályák listáját. Miután a játékos kiválasztotta melyik pályán
	 * akar játszani, betölti a pályához tartozó sínhálózatot és vonat
	 * ütemezést. Ezután elindítja a vonatok léptetéséért felelos szálat.
	 * 
	 * @param mapNumber
	 *            a betöltendo pálya sorszáma
	 */
	public static void startNewGame(int mapNumber) {
		fail = false;
		scheduledTrains="";
		trainScheduleTimer = 0;
		activeEntranceCounter=0;
		System.out.println(
				"Class: GameController\t Object: Object: GameController@STATIC\t Method: startNewGame\t Param: "
						+ mapNumber); /* Kiíratás a Szkeleton vezérlésének */

		String mapName = new String("maps/map" + mapNumber+ ".txt");
		
		/*mivel több pálya is lehet, ezért dinamikusan állítjuk össze a betöltendo nevet.*/
		try {
			buildFromFile(
					mapName); /* Megpróbáljuk a fájlból felépíteni a pályát */
			isTheGameRunning = true; /* Elinditjuka játkot */

			startMainThread();
			startControllThread();

			System.out.println("\nClass: GameController\t Object: GameController@STATIC\t A játék elindult\n"); /*
																												 * Kiíratás
																												 * a
																												 * Szkeleton
																												 * vezérlésének
																												 */
			lastPlayedMapNumber = mapNumber; /*
												 * Elmentjük melyik pályát
												 * töltöttük be utoljára.
												 */
		} catch (IOException e) {
			System.err.println(
					"\nClass: GameController\t Object: GameController@STATIC\t HIBA A P? LYA BET? LT? SE K? ZBEN\n"); /*
																														 * Ha
																														 * véletlen
																														 * nem
																														 * lehet
																														 * betölteni
																														 * a
																														 * pályát,
																														 * akkor
																														 * küldünk
																														 * egy
																														 * értesítst
																														 * errol
																														 */
		}
	}

	/**
	 * Értesíti a játékost, hogy nyert, és leállítja a játékot.
	 */
	public static void winEvent() {
		SoundManager.stopTrainSound();
		gui.getGameView().addAnimation(GameGUI.BOARDWIDTH / 2, GameGUI.BOARDHEIGHT / 2, "Win");
		SoundManager.playSound("Win");
		isTheGameRunning = false; /*
									 * Leállítjuk a játékot. Ez majd a GUI-t
									 * futtató threadnél lesz fontos
									 */
		System.out.println("Class: GameController\t Object: GameController@STATIC\t Method: winEvent\t Gyozelem."); /*
																													 * Kiíratás
																													 * a
																													 * Szkeleton
																													 * vezérlésének
																													 */
	}

	/**
	 * Értesíti a játékost, hogy vesztett, és leállítja a játékot.
	 */
	public static void loseEvent() {
		SoundManager.stopTrainSound();
		SoundManager.playSound("Lose");
		gui.getGameView().addAnimation(GameGUI.BOARDWIDTH / 2, GameGUI.BOARDHEIGHT / 2, "Lose");
		isTheGameRunning = false; /* Leállítjuk a játékot */
		continueLevels = false;
		System.out.println("Class: GameController\t Object: GameController@STATIC\t Method: loseEvent\t Vereseg"); /*
																													 * Kiíratás
																													 * a
																													 * Szkeleton
																													 * vezérlésének
																													 */
		System.out.println("Class: GameController\t Object: GameController@STATIC\t Michael Bay Effekt"); /*
																											 * Kiíratás
																											 * a
																											 * Szkeleton
																											 * vezérlésének
																											 */
	}

	/**
	 * Ebben a részben egy elöre kitöltött szöveges fájlból ovlasunk majd be
	 * karaktereket, ami alapján automatikusan felépitjük a pályánkat. A fájl
	 * kötött formátumú, ha ezt nem tartjuk a program hibval zárulna. A
	 * szövegfáj formai követelményei: * Az elsö sor a pálya szélessége és
	 * magassága lesz, ;-vel elválasztva (pl.: 12;7) * Ezután az elementReadeben
	 * leírtak alapján töltjük ki afájlunkat * Fontos hogy minden sorban
	 * pontosan annyi karakter legyen, amennyit meghatároztunk kezdetben * Nincs
	 * ellenörizve, hogy van e belépési pontunk, de az akadálytalan futás
	 * érdekében ajánlott. * Az egymás mellett lévö sinek szomszédosnak lesznek
	 * véve, így egy legalább egy mezönyi helyet kell hagyni köztük, hogy jól
	 * építse fel. pl.: ERRR xRxR xRRR Ha ezeket tartjuk, várhatóan jó eredményt
	 * kapunk
	 * 
	 * A gyakorlati müködés: * Kezdetben létrehozunk egy olvasót, amivel
	 * soronként végignézzük a fájlt * Az elsö sorát kiolvassuk és létrehozunk 2
	 * tömböt: tempMap-ot ami konkrét sineket tárol és a tempView-t ami a
	 * kiolvasott karaktereket. * A kiolvasott karakterektöl függöen az
	 * elementReader visszaadja amegfelelö tipusú Rail-t * Végigolvassuk a fájlt
	 * és kitöltüjük a két tömböt
	 * 
	 * * Ezután sorban végigjárjuk a tempView mezöit és ha Rail, akkor megnézzük
	 * hogy a 8 szomszédjából melyik létezö Rail * Ha a 8 szomszédos mezön van
	 * Rail akkor azt felvesszük az aktuális mezö szomszédai közé * Végül
	 * hozzáadjuk a szomszédokat a tempMap megfelelö Rail-jéhez * ? gyelünk arra
	 * hogy az ellenörzött 8 mezö ne lógjon le a pályáról
	 * 
	 * * Végül bejárjuk a tempMap-t és az összes létezö Railt hozzáaduk a
	 * railCollectionhoz
	 * 
	 * Ekkorra minden szomszédosság fel van építve és minden mezö megjelenik a
	 * collection-ban. A metódus visszatér.
	 * 
	 * @param filename
	 *            a beolvasandó file neve
	 * 
	 */

	private static void buildFromFile(String filename) throws IOException {
		System.out.println("Class: GameController\t Object: GameController@STATIC\t Method: buildFromFile\t Param: "
				+ filename
				+ "\t Betoltes."); /* Kiíratás a Szkeleton vezérlésének */

		/* Kezdetben megállítjk a játékot és töröljük azelözö listákat */
		isTheGameRunning = false;
		railCollection.clear();
		trainCollection.clear();
		System.out.println("Class: GameController\t Object: GameController@STATIC\t Method: railCollection.clear"); /*
																													 * Kiíratás
																													 * a
																													 * Szkeleton
																													 * vezérlésének
																													 */

		String in = ""; /* Egy beolvasott sort tárol, ebbe olvasunk */
		String[] line; /* Feltördeljük az elözöleg olvasott sort */

		BufferedReader brMap = new BufferedReader(
				new FileReader(new File(filename))); /* Térkép File olvasó */

		line = brMap.readLine().split(";"); /* Kiolvassuk a pálya méretét */

		int width = Integer.parseInt(line[0]); /* Pálya szélessége */
		int height = Integer.parseInt(line[1]); /* Pálya magassága */
		Rail[][] tempMap = new Rail[width][height]; /*
													 * Pályaelem tároló mátrix
													 */
		String[][] tempView = new String[width][height]; /*
															 * Pályaelem leíró
															 * mátrix
															 */

		gui.setGameView(width, height, STEPTIME);

		int x = 0; /* Segédváltozó szélességhez */
		int y = 0; /* Segédváltpzó magassághoz */
		while (y < height && (in = brMap
				.readLine()) != null) { /* ? sszes maradék sort kiolvassuk */
			line = in.split("");
			for (String s : line) { /* Minden karaktert megnézünk */
				tempMap[x][y] = elementReader(s); /* Létrehozzuk a típust */
				if (tempMap[x][y] != null) {
					tempMap[x][y].setX(x);
					tempMap[x][y].setY(y);
				}

				gui.getGameView().setBaseTileMap(x, y, s);
				gui.getGameView().addAnimation(x, y, s);
				tempView[x][y] = s; /* Mentjük a vázlatát */
				x++;
			}
			x = 0;
			y++;
		}

		/*Beolvasott vonatokat tároljuk*/
		scheduledTrains=brMap.readLine();
		/*Kiiratjuk a vezérlésnek*/
		
 		System.out.println("\nClass: GameController\t Object: GameController@STATIC\t Sínek közötti kapcsolatok létrehozása."); 
 		

		for (int i = 0; i < width; i++) { /*
											 * Végignézzük a pályát
											 * szélességben...
											 */
			for (int j = 0; j < height; j++) { /* ... és magasságban */
				if (tempMap[i][j] != null) { /*
												 * Ha az aktuális elem nem üres,
												 * akkor lehetnek szomszédai
												 */
					ArrayList<Rail> tmp = new ArrayList<Rail>(); /* új "szomszéd tároló" */

					if (i - 1 >= 0 && tempMap[i - 1][j] != null)
						tmp.add(tempMap[i
								- 1][j]); /*
											 * Ha balra lévö mezö a pálya része
											 * és Rail akkor a szomszédja
											 */
					if (i + 1 < width && tempMap[i + 1][j] != null)
						tmp.add(tempMap[i
								+ 1][j]);/*
											 * Ha jobbra lévö mezö a pálya része
											 * és Rail akkor a szomszédja
											 */
					if (j - 1 >= 0 && tempMap[i][j - 1] != null)
						tmp.add(tempMap[i][j
								- 1]);/*
										 * Ha felette lévö mezö a pálya része és
										 * Rail akkor a szomszédja
										 */
					if (j + 1 < height && tempMap[i][j + 1] != null)
						tmp.add(tempMap[i][j
								+ 1]);/*
										 * Ha alatta lévö mezö a pálya része és
										 * Rail akkor a szomszédja
										 */

					/*
					 * Itt a sorrend számít, mert az XRail az elsö kettöt és a
					 * második kettöt kapcsolja párba
					 */

					tempMap[i][j]
							.setNeighbourRails(tmp); /*
														 * Hozzáadjuk az újonnan
														 * felvett szomszédokat
														 */
				}
			}
		}

		for (int i = 0; i < width; i++) { /* Bejárjuk a táblát */
			for (int j = 0; j < height; j++) {
				if (tempMap[i][j] != null)
					railCollection
							.add(tempMap[i][j]); /*
													 * Ha van Rail tipus, akkor
													 * a kollekciónk része kell
													 * hogy legyen, felvesszük.
													 */
			}
		}

		brMap.close();
		System.out.println("Class: GameController\t Object: GameController@STATIC\t Létrehozott pályaelemek száma: "
				+ railCollection.size()); /* Megnézzük hogy változott e valam */
	}

	/**
	 * A map-ben minden egyes mezo egy betuvel van megadva. Mindegy egyes betu
	 * egy bizonyos cellatípusnak felel meg. Alábbi függvény a megkapott betunek
	 * megfelelo síntípust hoz létre, és visszaadja mely bekerül a
	 * railCollection-be.
	 *
	 * @param mapChar
	 *            A map-ban szereplo karakter, mely egy síntípust takar.
	 * @return A létrehozott sín.
	 */
	private static Rail elementReader(String mapChar) {
		System.out.println("\nClass: GameController\t Object: GameController@STATIC\t Method: elementReader\t Param: "
				+ mapChar
				+ "\t Elem dekodolasa fajlbol"); /*
													 * A jobb olvashatóság
													 * érdekében ezelott egy új
													 * sor van.
													 */
		switch (mapChar) {
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
			return new TunnelEntrance(); /*
											 * Létrehozunk egy új
											 * TunnelEntrance-t.
											 */

		case "X":
			System.out.println("Beolvasott elem: XRail");
			return new XRail(); /* Létrehozunk egy új XRail-t. */

		case "1":
			System.out.println("Beolvasott elem: Red TrainStation");
			return new TrainStation(
					Color.RED); /*
								 * Létrehozunk egy új TrainStation-t, mely piros
								 * színu lesz.
								 */

		case "2":
			System.out.println("Beolvasott elem: Green TrainStation");
			return new TrainStation(
					Color.GREEN); /*
									 * Létrehozunk egy új TrainStation-t, mely
									 * zöld színu lesz.
									 */

		case "3":
			System.out.println("Beolvasott elem: Blue TrainStation");
			return new TrainStation(
					Color.BLUE); /*
									 * Létrehozunk egy új TrainStation-t, mely
									 * kék színu lesz.
									 */

		case "4":
			System.out.println("Beolvasott elem: Red TrainStation Passengers");
			TrainStation station0 = new TrainStation(Color.RED);
			station0.addPassengers();
			return station0; /*
								 * Létrehozunk egy új TrainStation-t, mely piros
								 * színu lesz utasokkal.
								 */

		case "5":
			System.out.println("Beolvasott elem: Green TrainStation Passengers");
			TrainStation station1 = new TrainStation(Color.GREEN);
			station1.addPassengers();
			return station1;/*
							 * Létrehozunk egy új TrainStation-t, mely zöld
							 * színu lesz utasokkal.
							 */

		case "6":
			System.out.println("Beolvasott elem: Blue TrainStation Passengers");
			TrainStation station2 = new TrainStation(Color.BLUE);
			station2.addPassengers();
			return station2; /*
								 * Létrehozunk egy új TrainStation-t, mely kék
								 * színu leszutasokkal.
								 */
		default:
			System.out.println("Beolvasott elem: ures ");
			return null; /*
							 * Ez a lehetoség akkor fut le ha nem ismert betü
							 * van a szövegünben, mely ilyenkor egy üres mezö
							 * lesz
							 */
		}
	}

	/**
	 * Meg kell nézni minden léptetés után hogy a vonatok kiürültek e
	 * 
	 * @return Ha mindegyik vonat kiürült, akkor igazzal térünk vissza,
	 *         egyébként hamissal.
	 */
	private static boolean hasTheGameEnded() {
		System.out.println(
				"Class: GameController\t Object: GameController@STATIC\t Method: hasTheGameEnded\t Vonatok uressegenek ellenorzese");

		Boolean isAllEmpty = trainCollection.isAllEmpty()
				&& allStationsEmpty(); /*
										 * Ha mindegyik vonat kiürült, akkor
										 * igazzal térünk vissza, egyébként
										 * hamissal.
										 */
		System.out.println("Class: GameController\t Object: GameController@STATIC\t Returned: "
				+ isAllEmpty); /* Kiíratás a Szkeleton vezérlésének */
		return isAllEmpty;
	}

	/**
	 * Kizárólag a teszteléshez létrehozott metódus. Annyi alagútszályat tud
	 * aktiválni, ahányat megadunk neki. (ennek mennyiségét nem ellenorzi,
	 * hiszen azt a Main megfelelo része már megtette.
	 * 
	 * A getClass fv csak azért kell bele, mert a végso verzióban a GUI-n
	 * történo kattintásból egybol meg fogjuk tudni az eventet kiváltó objektum
	 * id-jét, GUI hiányában azonban erre nincs lehetoségünk. Ha nagyon
	 * szükséges, meg tudjuk oldani enélkül is (de az ocsmányabb lenne) ezért
	 * ennél maradtunk.
	 * 
	 * @param tunnelEntranceCounterToBeActivated
	 *            Hány alagútszályat akarunk aktiválni.
	 */
	public void skeletonTesterActivateTunnelEntrance(int tunnelEntranceCounterToBeActivated) {
		for (int i = 0; i < tunnelEntranceCounterToBeActivated; i++) { /*
																		 * Annyi
																		 * alagútszájat
																		 * keresünk,
																		 * amennyit
																		 * paraméterül
																		 * megadtak
																		 */
			Boolean notActivatedTunnelEntranceFound = false;
			while (!notActivatedTunnelEntranceFound) { /*
														 * amíg nem találunk egy
														 * nem aktivált
														 * alagútszájat
														 */
				for (Rail oneRail : railCollection) {
					if (oneRail
							.getClass() == TunnelEntrance.class) { /*
																	 * megnézzük
																	 * TunnelEntrance
																	 * -e a sín.
																	 */
						TunnelEntrance oneTunnel = (TunnelEntrance) oneRail; /*
																				 * Ha
																				 * igen,
																				 * akkor
																				 * átkasztoljuk
																				 */
						if (!oneTunnel.checkIfActivated()) { /*
																 * Ha még nincs
																 * aktiválva
																 */
							oneTunnel.activate(); /*
													 * Aktiváljuk, és
													 * megnöveljük az aktív
													 * alagútszájak számát
													 * eggyel
													 */
							activeEntranceCounter++;
							notActivatedTunnelEntranceFound = true;
							break; /*
									 * Break, kezdodik a következo keresésése,
									 * ha erre szükség van.
									 */
						}
					}
				}
			}

		}
		System.out.println("Class: GameController\t Object: GameController@STATIC\t Aktív alagútszájak száma: "
				+ activeEntranceCounter);/* Kiíratás a Szkeleton vezérlésének */
		if (activeEntranceCounter == 2) { /*
											 * Ha két aktív alagútszájunk van,
											 * akkor kezdeményezzük az alagút
											 * létrehozását.
											 */
			System.out.println("\nClass: GameController\t Object: GameController@STATIC\t Alagút létrehozása");/*
																												 * Kiíratás
																												 * a
																												 * Szkeleton
																												 * vezérlésének
																												 */

			/*
			 * Disclaimer: Az alábbi kódot Long csinálta, szóval God have mercy
			 * on your soul, if you want to understand this.
			 */
			Rail entrance1, entrance2;
			entrance1 = null;
			entrance2 = null;

			/* Kikeressük a kettöt */
			for (Rail rail : railCollection) {
				if (rail.getClass() == TunnelEntrance.class) {
					if (entrance1 == null) {
						entrance1 = rail;
					} else {
						entrance2 = rail;
					}
				}
			}
			/* Segédváltozók */
			int e1X = entrance1.getX();
			int e1Y = entrance1.getY();
			int e2X = entrance2.getX();
			int e2Y = entrance2.getY();

			ArrayList<Rail> newTunnels = new ArrayList<Rail>();
			Tunnel tmp;

			/* Az elso része a belépési pont lesz */
			newTunnels.add(entrance1);

			/* Viszintes tengelyen vizsgáljuk */
			/* Ha az elsö alagut jobbrább van */

			if (e1X > e2X) {
				while (e1X > e2X) { /*
									 * Amig nem érünk egy szintre a kijárattal
									 */
					if (e1X - 1 == e2X && e1Y == e2Y) { /*
														 * Pont tole jobbra van
														 * a kijárat
														 */
						newTunnels.add(entrance2); /* Bekötjük a kijárathoz */
						e1X--;
					} else { /* Ha ez még csak egy alagut */
						e1X--; /* Közelebb hozzuk */
						tmp = new Tunnel(); /*
											 * Létrehozzuk és beállítjuk az
											 * adatait
											 */
						tmp.setX(e1X);
						tmp.setY(e1Y);
						newTunnels.add(tmp); /* Felépitjük */
					}
				}
			}
			/* Ha az elsö alagut balrább van */
			else if (e1X < e2X) {
				while (e1X < e2X) { /*
									 * Amig nem érünk egy szintre a kijárattal
									 */
					if (e1X + 1 == e2X && e1Y == e2Y) { /*
														 * Pont tole balrara van
														 * a kijárat
														 */
						newTunnels.add(entrance2); /* Bekötjük a kijárathoz */
						e1X++;
					} else { /* Ha ez még csak egy alagut */
						e1X++; /* Közelebb hozzuk */
						tmp = new Tunnel(); /*
											 * Létrehozzuk és beállítjuk az
											 * adatait
											 */
						tmp.setX(e1X);
						tmp.setY(e1Y);
						newTunnels.add(tmp); /* Felépitjük */
					}
				}
			}

			/* Függöleges tengelyen vizsgáljuk */
			/* Ha az elsö alagut feljebb van */
			if (e1Y > e2Y) {
				while (e1Y > e2Y) { /*
									 * Amig nem érünk egy szintre a kijárattal
									 */
					if (e1Y - 1 == e2Y
							&& e1X == e2X) { /* Pont felette van a kijárat */
						newTunnels.add(entrance2); /* Bekötjük a kijárathoz */
						e1Y--;
					} else { /* Ha ez még csak egy alagut */
						e1Y--; /* Közelebb hozzuk */
						tmp = new Tunnel(); /*
											 * Létrehozzuk és beállítjuk az
											 * adatait
											 */
						tmp.setX(e1X);
						tmp.setY(e1Y);
						newTunnels.add(tmp); /* Felépitjük */
					}
				}
			}
			/* Ha az elsö alagut lejjebb van */
			else if (e1Y < e2Y) {
				while (e1Y < e2Y) { /*
									 * Amig nem érünk egy szintre a kijárattal
									 */
					if (e1Y + 1 == e2Y
							&& e1X == e2X) { /* Pont alatta van a kijárat */
						newTunnels.add(entrance2); /* Bekötjük a kijárathoz */
						e1Y++;
					} else { /* Ha ez még csak egy alagut */
						e1Y++; /* Közelebb hozzuk */
						tmp = new Tunnel(); /*
											 * Létrehozzuk és beállítjuk az
											 * adatait
											 */
						tmp.setX(e1X);
						tmp.setY(e1Y);
						newTunnels.add(tmp); /* Felépitjük */
					}
				}
			}

			/* Minden alagutelemet felvettünk */
			/* ? sszekötjük öket */
			for (int i = 0; i < newTunnels.size(); i++) {
				if (i - 1 >= 0)
					newTunnels.get(i).addNeighbourRail(newTunnels.get(i - 1));
				if (i + 1 < newTunnels.size())
					newTunnels.get(i).addNeighbourRail(newTunnels.get(i + 1));
				if (i > 0 && i < newTunnels.size() - 1)
					railCollection.add(newTunnels.get(i));
			}
		}
	}

	/**
	 * Kizárólag a teszteléshez létrehozott metódus. Ellenorzi hány aktív
	 * alagútszáj van. Ha ketto, akkor lebontja az alagutat, és deaktivál egyet.
	 * 
	 * A getClass fv csak azért kell bele, mert a végso verzióban a GUI-n
	 * történo kattintásból egybol meg fogjuk tudni az eventet kiváltó objektum
	 * id-jét, GUI hiányában azonban erre nincs lehetoségünk. Ha nagyon
	 * szükséges, meg tudjuk oldani enélkül is (de az ocsmányabb lenne) ezért
	 * ennél maradtunk.
	 */
	public void skeletonTesterDeActivateATunnelEntrance() {

		Boolean activatedTunnelEntranceFound = false;
		while (!activatedTunnelEntranceFound) { /*
												 * amíg nem találunk egy
												 * aktivált
												 */
			for (Rail oneRail : railCollection) {
				if (oneRail
						.getClass() == TunnelEntrance.class) { /*
																 * Megkeressük a
																 * TunnelEntrancet
																 */
					TunnelEntrance oneTunnel = (TunnelEntrance) oneRail; /* átkasztoljuk */
					if (oneTunnel.checkIfActivated()) { /*
														 * Ellenorizzük, hogy
														 * aktiválva van-e
														 */
						oneTunnel.deActivate(); /*
												 * Deaktiváljuk, mely során
												 * kitörlodik a
												 * szomszédlistájából a
												 * referencia az alagútra
												 */
						activatedTunnelEntranceFound = true;
						break;
					}
				}
			}
		}

		if (activeEntranceCounter == 2) { /* Ha két aktív TunnelEntrance volt */
			System.out.println("\nClass: GameController\t Object: GameController@STATIC\t Alagút lerombolása"); /*
																												 * Kiíratás
																												 * a
																												 * Szkeleton
																												 * vezérlésének
																												 */

			Boolean railCollectionIsFreeOfTunnels = false;

			while (!railCollectionIsFreeOfTunnels) {
				railCollectionIsFreeOfTunnels = true;
				Rail railToGetDeleted = null;

				for (Rail rail : railCollection) {
					if (rail.getClass() == Tunnel.class) {
						railToGetDeleted = rail;
						railCollectionIsFreeOfTunnels = false;
					}
				}

				if (!railCollectionIsFreeOfTunnels) {
					railCollection.remove(railToGetDeleted);
				}
			}

		}

		activeEntranceCounter--;
		System.out.println("Class: GameController\t Object: GameController@STATIC\t Aktív alagútszájak száma: "
				+ activeEntranceCounter);
	}

	public void skeletonTesterSwitchASwitch() {
		Boolean switchFound = false;
		while (!switchFound) {
			for (Rail oneRail : railCollection) {
				if (oneRail.getClass() == Switch.class) {
					Switch oneSwitch = (Switch) oneRail;
					oneSwitch.switchRail();
					switchFound = true;
					break;

				}
			}
		}
	}

	/**
	 * Váltót lehet vele állítani, meg alagutat építeni. Ha a paraméterül kapott
	 * helyen lévo elemrol kideríti, hogy alagútszáj-e; ha igen, akkor aktiválva
	 * van-e. Ha igen, deaktiválja, ha nem , aktiválja. Ha ez alagút
	 * építést/bontást von maga után, akkor megteszi. Ezzel egyidoben átállítja
	 * az alagútszáj váltóját is (aktiválás -> be az alagútba, deaktiválás ->
	 * alaphelyzetbe). Ha a kattintott elem váltó, akkor átállítja. Jelenleg
	 * nincs TELJESEN kész, a grafikus részben ezt fel kell majd iratkoztatni a
	 * kattintásra. Jelenleg a kapott koordináta így nem a kattintásé, hanem a
	 * konkrét elemé amire kattintani akarunk. Ha mondjuk a cellák 20*20
	 * pixelesek lesznek és ide az érkezik, hogy 33, 21, akkor ebbol még le kell
	 * hozni, hogy ez az 1-1-es indexu cella. Ha ez megvan onnantól viszont ez
	 * már jó kell hogy legyen
	 * 
	 * @param X
	 *            Kattintás X koordinátája
	 * @param Y
	 *            Kattintás Y koordinátája
	 */
	public static void ClickHandler(int X, int Y, int btn) {
		if (isTheGameRunning && !gui.isGameClosed()) {
			for (Rail rail : railCollection) {
				if (rail.getX() == X && rail
						.getY() == Y) { /* Megkeressük a kattintott elemet */
					try {

						TunnelEntrance thisEntrance = (TunnelEntrance) rail;

						if (thisEntrance.checkIfActivated() && btn == 1) {
							gui.getGameView().deactivateTunnel(X, Y);
							thisEntrance.deActivate();
							activeEntranceCounter--;

							if (wasTrainInTunnel())
								failIntent();

							if (activeEntranceCounter == 1) {
								for (Rail rail2 : railCollection) {
									try {
										TunnelEntrance otherEntrance = (TunnelEntrance) rail2;
										if (otherEntrance.checkIfActivated() == false) {
											continue;
										}

										System.out.println(
												"\nClass: GameController\t Object: GameController@STATIC\t Alagút lerombolása");

										Boolean railCollectionIsFreeOfTunnels = false;

										while (!railCollectionIsFreeOfTunnels) {
											railCollectionIsFreeOfTunnels = true;
											Rail railToGetDeleted = null;

											for (Rail rail3 : railCollection) {
												if (rail3.getClass() == Tunnel.class) {
													railToGetDeleted = rail3;
													railCollectionIsFreeOfTunnels = false;
												}
											}

											if (!railCollectionIsFreeOfTunnels) {
												railCollection.remove(railToGetDeleted);
											}
										}
										break;

									} catch (Exception e) {
										// TODO: handle exception
									}
								}
							}
						}

						else if (!thisEntrance.checkIfActivated() && btn == 0) {
							// ha van 2 ne legyen több
							if (activeEntranceCounter == 2)
								return;

							gui.getGameView().activateTunnel(X, Y);
							thisEntrance.activate();
							activeEntranceCounter++;
							if (activeEntranceCounter == 2) {
								for (Rail rail2 : railCollection) {
									try {
										TunnelEntrance otherEntrance = (TunnelEntrance) rail2;
										if (thisEntrance.equals(otherEntrance)
												|| otherEntrance.checkIfActivated() == false) {
											continue;
										}

										Tunnel first = gui.getGameView().getFirstTunnelPart(thisEntrance);
										Tunnel last = gui.getGameView().getFirstTunnelPart(otherEntrance);

										int e1X = first.getX();
										int e1Y = first.getY();
										int e2X = last.getX();
										int e2Y = last.getY();

										ArrayList<Rail> newTunnels = new ArrayList<Rail>();
										Tunnel tmp;

										newTunnels.add(thisEntrance);
										newTunnels.add(first);

										if (e1X > e2X) {
											while (e1X > e2X) {
												if (e1X - 1 == e2X && e1Y == e2Y) {
													newTunnels.add(last);
													e1X--;
												} else {
													e1X--;
													tmp = new Tunnel();
													tmp.setX(e1X);
													tmp.setY(e1Y);
													newTunnels.add(tmp);
												}
											}
										}

										else if (e1X < e2X) {
											while (e1X < e2X) {
												if (e1X + 1 == e2X && e1Y == e2Y) {
													newTunnels.add(last);
													e1X++;
												} else {
													e1X++;
													tmp = new Tunnel();
													tmp.setX(e1X);
													tmp.setY(e1Y);
													newTunnels.add(tmp);
												}
											}
										}

										if (e1Y > e2Y) {
											while (e1Y > e2Y) {
												if (e1Y - 1 == e2Y && e1X == e2X) {
													newTunnels.add(last);
													e1Y--;
												} else {
													e1Y--;
													tmp = new Tunnel();
													tmp.setX(e1X);
													tmp.setY(e1Y);
													newTunnels.add(tmp);
												}
											}
										}

										else if (e1Y < e2Y) {
											while (e1Y < e2Y) {
												if (e1Y + 1 == e2Y && e1X == e2X) {
													newTunnels.add(last);
													e1Y++;
												} else {
													e1Y++;
													tmp = new Tunnel();
													tmp.setX(e1X);
													tmp.setY(e1Y);
													newTunnels.add(tmp);
												}
											}
										}
										newTunnels.add(otherEntrance);

										for (int i = 0; i < newTunnels.size(); i++) {
											if (i - 1 >= 0)
												newTunnels.get(i).addNeighbourRail(newTunnels.get(i - 1));
											if (i + 1 < newTunnels.size())
												newTunnels.get(i).addNeighbourRail(newTunnels.get(i + 1));
											if (i >= 0 && i < newTunnels.size() - 1)
												railCollection.add(newTunnels.get(i));
										}

										break;
									} catch (Exception e) {
									}
								}
							}
						} else if (thisEntrance.checkIfActivated() && btn == 0) {
							thisEntrance.switchRail();
							gui.getGameView().switchState(X, Y, thisEntrance.getState());
						}

						rail = thisEntrance;
						break;
					} catch (Exception e) {
						System.out.println("A kattintott elem nem tunnelEntrance");
					}

					try {
						Switch sw = (Switch) rail;
						sw.switchRail();
						gui.getGameView().switchState(X, Y, sw.getState());

						rail = sw;
					} catch (Exception e) {
						System.out.println("A kattintott elem nem switch");
					}

					break;

				}
			}
		} else if (!isTheGameRunning && !gui.isGameClosed()) {
			// Véget ért, de még nem kattintottuk el..
			if (mainThread.isAlive())
				mainThread.stop();
			endGameSession();
			// Betöltünk vagy nemtom

			if (continueLevels && levelCounter < 4) {
				levelCounter++;
				gui.getGameView().stopRender();
				gui.remove(gui.getGameView());
				startNewGame(100 + levelCounter);
			} else {
				// Vissza a menübe
				gui.getGameView().stopRender();
				gui.remove(gui.getGameView());
				gui.setMenuView();
				gui.getMenuView().startRender();
			}

		} else if (!gui.isMenuClosed() && gui.isGameClosed()) {
			// Menübenválaszttunk
			switch (gui.getMenuView().getClickedItem(X, Y)) {
			case 0:
				return;
			case 1:
				levelCounter = 1;
				continueLevels = true;
				gui.getMenuView().stopRender();
				gui.remove(gui.getMenuView());
				startNewGame(101);
				break;
			case 21:
				continueLevels = false;
				gui.getMenuView().stopRender();
				gui.remove(gui.getMenuView());
				startNewGame(101);
				break;
			case 22:
				continueLevels = false;
				gui.getMenuView().stopRender();
				gui.remove(gui.getMenuView());
				startNewGame(102);
				break;
			case 23:
				continueLevels = false;
				gui.getMenuView().stopRender();
				gui.remove(gui.getMenuView());
				startNewGame(103);
				break;
			case 24:
				continueLevels = false;
				gui.getMenuView().stopRender();
				gui.remove(gui.getMenuView());
				startNewGame(104);
				break;
			case 3:
				gui.setVisible(fail);
				gui.dispatchEvent(new WindowEvent(gui, WindowEvent.WINDOW_CLOSING));
			}
		}
	}

	/**
	 * Kizárólag a teszteléshez létrehozott metódus. A pályán megkeresi az elso
	 * tunnelEntrancet amit talál és beállítja a kívánt állapotra.
	 * 
	 * A getClass fv csak azért kell bele, mert a végso verzióban a GUI-n
	 * történo kattintásból egybol meg fogjuk tudni az eventet kiváltó objektum
	 * id-jét, GUI hiányában azonban erre nincs lehetoségünk. Ha nagyon
	 * szükséges, meg tudjuk oldani enélkül is (de az ocsmányabb lenne) ezért
	 * ennél maradtunk.
	 */
	public void skeletonTesterSwitchATunnelEntrance() {
		Boolean tunnelEntranceFound = false;
		while (!tunnelEntranceFound) { /* Keresünk egy TunnelEntrance-t */
			for (Rail oneRail : railCollection) {
				if (oneRail.getClass() == TunnelEntrance.class) { /*
																	 * végignézzük
																	 * melyik a
																	 * TunnelEntrance
																	 */
					TunnelEntrance oneTunnelEntrance = (TunnelEntrance) oneRail; /* átkasztoljuk */
					oneTunnelEntrance
							.switchRail();/* változtatunk az állapotán egyet */
					tunnelEntranceFound = true;
					break;/* ha szükséges keresünk egy következot. */

				}
			}
		}
	}

	/**
	 * Kizárólag a teszteléshez létrehozott metódus A Main-tol megadott
	 * paraméterekkel létrehoz egy vonatot,
	 * 
	 * @param cabinColors
	 *            A kabinok színének listája amibol a vonat felépül
	 */
	public void skeletonTesterAddNewTrain(ArrayList<Color> cabinColors) {

		System.out.println("\nClass: GameController\t Object: GameController@STATIC\t ? j vonat hozzáadása");/*
																												 * Kiíratás
																												 * a
																												 * Szkeleton
																												 * vezérlésének
																												 */
		Train testTrain = new Train(
				cabinColors); /*
								 * A megadott paraméterekkel létrehozunk egy új
								 * vonatot
								 */

		EnterPoint enterPoint = null;

		Boolean enterPointFound = false; /*
											 * Kikeressük melyik a
											 * railCollection-ben az EnterPoint
											 * és azt eltároljuk, hogy utána
											 * beállítsuk elso sínként a
											 * vonatnak.
											 */
		while (!enterPointFound) {
			for (Rail oneRail : railCollection) {
				if (oneRail.getClass() == EnterPoint.class) {
					enterPoint = (EnterPoint) oneRail;
					enterPointFound = true;
				}
			}
		}

		testTrain.setNextRail(
				enterPoint); /* Beállítjuk elso sínnek a belépési pontot */
		trainCollection.addNewTrain(
				testTrain); /* Hozzáadjuk a trainCollection-be az új vonatot. */
	}

	/**
	 * Kizárólag a teszteléshez létrehozott metódus Lépteti a vonatokat egyszer.
	 */
	public void skeletonTesterMakeTrainsMove() {
		System.out.println(
				"\nClass: GameController\t Object: GameController@STATIC\t ? tközéshez szükséges vonat hossz számlálók csökkentése");/*
																																		 * Kiíratás
																																		 * a
																																		 * Szkeleton
																																		 * vezérlésének
																																		 */
		for (Rail oneRail : railCollection) { /*
												 * Minden eggyes sínnek
												 * csökkentjük eggyel a rajta
												 * még áthaladó kabinok számát,
												 * mivel lép egyet minden vonat.
												 */
			oneRail.lowerTrainLenghtCounter();
		}

		System.out.println("\nClass: GameController\t Object: GameController@STATIC\t Vonatok léptetése"); /*
																											 * Kiíratás
																											 * a
																											 * Szkeleton
																											 * vezérlésének
																											 */
		trainCollection
				.moveAllTrains(); /*
									 * Minden egyes vonatot léptetünk eggyel.
									 * Késobb ezt egy thread fogja csinálni,
									 * jelen szkeletonban elég ennyi.
									 */
	}

	/**
	 * Kizárólag a teszteléshez létrehozott metódus
	 * 
	 * Kiiratjuk a felépített pálya adatait egy fájla hogy késöbb
	 * elelnörizhessük öket
	 * 
	 * @param mapNumber
	 *            a betöltendo pálya száma
	 * @param name
	 *            ez lesz a létrehozott fájl neve
	 * @throws IOException
	 */
	public void createMapTestFile(int mapNumber, String name) throws IOException {
		PrintWriter writer = new PrintWriter(name + ".txt",
				"UTF-8"); /* Kell egy TXT íro */

		String[] splitresult; /* Egyetlen sor felbontása */
		String result; /* Egy sor egy részletét tárolja */
		for (Rail rail : railCollection) { /* Megnézünk minden sint */
			splitresult = rail.toString().split(
					"@"); /* Felmbontjuk az objekumot a pointere mentén */
			result = rail.getClass()
					.getSimpleName(); /* Elkérjük az objektum osztályát */

			/*
			 * Ha állomásunk volt, annak szine is van, akkor a színre is
			 * szükségünk lesz
			 */
			if (rail.getClass().getSimpleName().toString().trim().equals("TrainStation")) {
				Color c = ((TrainStation) rail).getColor();
				if (c.equals(Color.RED))
					result += "1"; /*
									 * Ha piros volt, a neve után írunk egy 1
									 * est
									 */
				if (c.equals(Color.GREEN))
					result += "2"; /*
									 * Ha zöld volt, a neve után írunk egy 2 est
									 */
				if (c.equals(Color.BLUE))
					result += "3"; /*
									 * Ha kék volt, a neve után írunk egy 3 mast
									 */
			}

			/* Egyebe füzzük az adatokat a következö séma szerint: */
			/* név [x,y] (pointer) {szomszéd1, szomszéd2,...} */
			result += " [" + rail.getX() + "," + rail.getY() + "]" + " (" + splitresult[1] + ")";
			result += " {";
			int comacounter = rail.getNeighbourRails().size()
					- 1; /* Elemszám -1 vesszöt kell letennünk majd a fájlba */
			for (Rail neighbour : rail.getNeighbourRails()) {
				splitresult = neighbour.toString().split("@");
				result += splitresult[1];
				if (comacounter > 0) { /*
										 * Számoljuk hogy hány vesszöt tettünk
										 * le a szomszdok utána
										 */
					comacounter--;
					result += ",";
				}
			}
			result += "}"; /* Lezárjuk */
			writer.println(result); /* Kiiratjuk a fájlba */
		}
		writer.close(); /* Bezárjuk az olvasást */
		System.out.println("Save Done");
		MapCreationTest.main("maps/map" + mapNumber + ".txt",
				name + ".txt"); /* Indítjuk a tesztet */
	}

	/**
	 * A száékezelés tesztelésére írt egyszerü program, fix idöközönként generál
	 * egy történést.
	 * 
	 */
	public void runThreadTest() {
		Thread testThread = new Thread() {
			ArrayList<Color> testColors; /* Vonat kocsiszíneit tárolja */
			Train testTrain; /* Egy teszt vonat */
			EnterPoint enterPoint; /* Egy véletlenül talált belépési pont */

			public void run() { /* Megírjuk az esemnyeket */
				testColors = new ArrayList<Color>(); /*
														 * Inicializáljuk a
														 * listát
														 */
				testColors.add(Color.BLUE);/* Színt adunk hozzá */
				testTrain = new Train(
						testColors); /*
										 * A szinekkel inicializálunk egy
										 * vonatot
										 */

				for (Rail oneRail : railCollection) { /*
														 * Kikeresünk egy
														 * belépésíi pontot
														 */
					if (oneRail.getClass() == EnterPoint.class) {
						enterPoint = (EnterPoint) oneRail;
					}
				}

				if (enterPoint == null) { /*
											 * Amennyiben null az objektumunk,
											 * nem volt belépési pont a pályán
											 */
					System.out.println("Nincs belépési pont a pályán");
					return; /* Ilyenkor vége a futásnak */
				}

				/* A belépési pontra rakjuk a vonatunk */
				testTrain.setNextRail(enterPoint);
				trainCollection.addNewTrain(testTrain);

				/* 16 léptetést hajtunk végre */
				for (int i = 0; i < 16; i++) {
					for (Rail oneRail : railCollection) { /*
															 * Minden eggyes
															 * sínnek
															 * csökkentjük
															 * eggyel a rajta
															 * még áthaladó
															 * kabinok számát,
															 * mivel lép egyet
															 * minden vonat.
															 */
						oneRail.lowerTrainLenghtCounter();
					}
					trainCollection.moveAllTrains();
					drawToConsole(); /* Közben frissítjuk a nézetet */
					sleepTime(1000);
				}
			}
		};
		testThread.start(); /* Indítuk a szálat */
	}

	// Long
	private static void startMainThread() {
		mainThread = new Thread() {
			public void run() {
				// Start events
				gui.getGameView().addAnimation(GameGUI.BOARDWIDTH / 2, GameGUI.BOARDHEIGHT / 2, "321GO");
				sleepTime(1400);
				gui.getGameView().paintTrain();
				SoundManager.playSound("Start");
				SoundManager.playTrainSound();
				/*Fix*/
				ClickHandler(0,0,0);
				
				while (isTheGameRunning) {
					/*Csökkentjük a sinek vagonszámát */
					for (Rail oneRail : railCollection) { 
						oneRail.lowerTrainLenghtCounter();
					}
					
					/*Hozzáadjuk ha van uj vonat*/
					if(!scheduledTrains.equals("")){
						System.out.println("TIMESCHEDULE");
						scheduleTrains();
						trainScheduleTimer++;
					}

					trainCollection.moveAllTrains();

					gui.getGameView().moveAllTrain(trainCollection.getNextCoords());
					gui.getGameView().setCabStates(trainCollection.getCabStates());

					if (fail) {
						trainCollection.moveAllTrains();
						if (hasTheGameEnded()) {
							ultimateWinEvent();
						} else {
							loseEvent();
						}
					} else if (hasTheGameEnded()) {
						winEvent();
					}

					for (int j = 0; j < stepParts; j++) {
						gui.getGameView().updateTime(j * STEPTIME / stepParts);
						sleepTime(STEPTIME / stepParts / speed);
					}
				}
			}
		};
		mainThread.start();
		gui.getGameView().startRender();
		SoundManager.playBackgroundMusic();

	}

	private static void startControllThread() {
		controllThread = new Thread() {
			public void run() {
				while (!stopControll) {
					if (!gui.isGameClosed()) {
						doGuiClickLogAction(gui.getGameView().getClickLog());
					}
					if (!gui.isMenuClosed()) {
						doGuiClickLogAction(gui.getMenuView().getClickLog());
					}
					sleepTime(50);

				}
			}
		};
		controllThread.start();
	}

	private static void doGuiClickLogAction(String log) {
		if (log == null || log.equals(""))
			return;
		String changedTiles[];
		if (log.contains(";")) {
			changedTiles = log.split(";");

		} else {
			changedTiles = new String[1];
			changedTiles[0] = log;
		}

		for (String change : changedTiles) {
			String coords[] = change.split(",");
			ClickHandler(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]), Integer.parseInt(coords[2]));
		}

	}

	public static void failIntent() {
		fail = true;
	}

	private static boolean allStationsEmpty() {
		for (Rail rail : railCollection) {
			try {
				TrainStation station = (TrainStation) rail;
				if (!station.getPassengersColor().equals(Color.BLACK))
					return false;
			} catch (Exception e) {
				// Nem Station
			}
		}
		return true;
	}

	private static boolean wasTrainInTunnel() {
		for (Rail rail : railCollection) {
			try {
				Tunnel tunnel = (Tunnel) rail;
				if (tunnel.checkIfOccupied())
					return true;
			} catch (Exception e) {
				// Nem Station
			}
		}

		return false;
	}

	private static void ultimateWinEvent() {
		SoundManager.stopTrainSound();
		SoundManager.playSound("Epic");
		gui.getGameView().addAnimation(GameGUI.BOARDWIDTH / 2, GameGUI.BOARDHEIGHT / 2, "Epic");
		isTheGameRunning = false;
	}

	private static void sleepTime(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
			// Nem itt lesz baj
		}
	}

	public static boolean sessionEnded() {
		return sessionEnded();
	}

	private static void endGameSession() {
		gui.getGameView().stopRender();
		isTheGameRunning = false;
	}

	public static void showMenu() {
		gui.setMenuView();
		startControllThread();
	}

	private static void scheduleTrains() {
		String leftOver = "";
		String separateTrains[] = scheduledTrains.split(";");
		for (String train : separateTrains) {
			String data[] = train.split(",");
			try {
				if (Integer.parseInt(data[1]) == trainScheduleTimer) {
					addTrain(train);
				} else {
					leftOver += train+";";
					System.out.println("L"+leftOver);
				}
			} catch (Exception e) {
			}
		}
		scheduledTrains = leftOver;
	}

	private static void addTrain(String trainData) {
		ArrayList<Color> cabColors = new ArrayList<Color>();
		Train newTrain = null;
		EnterPoint enterPoint = null;
		int enterCounter;

		/* Felbontjuk az adatokat */
		String datas[] = trainData.split(",");

		/* Megadjuk hogy hányadik pontra raknánk */
		enterCounter = Integer.parseInt(datas[0]);
		/*
		 * Az idöpontot már felhasználtuk, ezért a szineket fogjuk msot
		 * használni
		 */

		String colors[] = datas[2].split("");
		for (String color : colors) {
			switch (color) {
			case "R":
				cabColors.add(Color.RED);
				break;
			case "G":
				cabColors.add(Color.GREEN);
				break;
			case "B":
				cabColors.add(Color.BLUE);
				break;
			}
		}

		newTrain = new Train(
				cabColors); /* A szinekkel inicializálunk egy vonatot */

		/* Megkeressük a belépési pontját */
		for (Rail oneRail : railCollection) {
			if (oneRail.getClass() == EnterPoint.class) {
				enterCounter--;
				if (enterCounter == 0){
					enterPoint = (EnterPoint) oneRail;
					
				}
				
			}
		}
		
		
		
		/* Ha null akkor nem találtuk megefelelö belépési pontot */
		if (enterPoint == null) {
			System.out.println("Nincs belépési pont a pályán");
			return; /* Ilyenkor vége a futásnak */
		}

		/* A belépési pontra rakjuk a vonatunk */
		newTrain.setNextRail(enterPoint);
		trainCollection.addNewTrain(newTrain);

		/* A UI-hoz is hozzá adjuk */
		gui.getGameView().addTrain(datas[2], enterPoint.getX(), enterPoint.getY(), enterPoint.getNextRail(null).getX(),
				enterPoint.getNextRail(null).getY());
	}
	// Long

	public void drawToConsole() {

		int maxX = 0; /* mi a legnagyobb x index ( = pályaszélesség - 1) */
		int maxY = 0; /* mi a legnagyobb y index ( = pályamagasság - 1) */
		for (Rail rail : railCollection) {
			if (rail.getY() > maxY) {
				maxY = rail.getY();
			}
			if (rail.getX() > maxX) {
				maxX = rail.getX();
			}
		}

		Rail[][] mapToDraw = new Rail[maxY + 1][maxX
				+ 1]; /*
						 * sajnos a railcollectionben nem sorfojtonosan egymás
						 * után vannak az elemek. Kell egy map helyette
						 */
		for (Rail rail : railCollection) {
			mapToDraw[rail.getY()][rail.getX()] = rail; /* feltöltjük a mapet */
		}

		char[][] charMap = new char[maxY + 1][maxX
				+ 1]; /*
						 * ebben vannak a konkrét karakterek amiket ki fogunk
						 * írni
						 */
		for (char[] cs : charMap) {
			for (char c : cs) {
				c = ' '; /*
							 * kezdetben minden legyen üres, majd felül lesznek
							 * írva elemekkel
							 */
			}
		}

		for (int line = 0; line < maxY
				+ 1; line++) { /*
								 * charmap feltöltése kiírandó betukkel a
								 * mapToDraw alapján
								 */
			for (int col = 0; col < maxX + 1; col++) {
				if (mapToDraw[line][col] == null) { /*
													 * Ha egy cella üres a
													 * mapToDraw-ban, akkor az a
													 * charMap-ben ' ' marad
													 */
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
					TrainStation ts = (TrainStation) mapToDraw[line][col];
					if (ts.getColor() == Color.RED) {
						charMap[line][col] = '1';
					}
					if (ts.getColor() == Color.GREEN) {
						charMap[line][col] = '2';
					}
					if (ts.getColor() == Color.BLUE) {
						charMap[line][col] = '3';
					}

				default:
					charMap[line][col] = ' ';
					break;
				}
			}
		}

		/*------------------- Idáig csak a statikus pálya kiírása volt, innetol jön a vonat berakása -------------------*/

		for (int line = 0; line < maxY
				+ 1; line++) { /*
								 * foglalt sínt átírjuk 'V'-re (V mint Vonat)
								 * Sajnos a síneknek fogalmuk sincs pontosan mi
								 * lépett rájuk
								 */
			for (int col = 0; col < maxX + 1; col++) {
				if (mapToDraw[line][col] == null) {
					continue;
				}
				if (mapToDraw[line][col].checkIfOccupied()) {
					if (charMap[line][col] != 'T') { /*
														 * Alagútban nem
														 * látszódhat a vonat.
														 * Ha a betu eredetilet
														 * 'T' volt, meghagyjuk
														 */
						charMap[line][col] = 'V';
					}
				}
			}
		}

		for (char[] cs : charMap) { /* maga a konzolra írás */
			for (char c : cs) {
				System.out.print(c);
			}
			System.out.println();
		}
	}

}