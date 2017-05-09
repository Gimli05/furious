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
 * A j�t�kmenet vez�rl�s��rt felel. Bet�lti a j�t�kos �ltal kiv�lasztott p�ly�t
 * f�jlb�l, fel�p�ti a p�ly�hoz tartoz� s�n h�l�zatot �s vez�rli melyik
 * EnterPoint-on milyen �temez�ssel h�ny darab vonat �rkezzen meg. Ez az oszt�ly
 * felel a vonatok sebess�g�nek meghat�roz�s��rt, �s a j�t�k kimenetel�nek
 * eld�nt�s��rt �s ennek lekezel�s��rt. Felelos tov�bb� annak fel�gyel�s��rt,
 * hogy csak k�t alag�tsz�j lehessen egyszerre akt�v.
 */
public class GameController {

	/**
	 * Megadja, hogy �ppen folyamatban van-e j�t�k. True, ha igen, false ha nem.
	 */
	private static Boolean isTheGameRunning;

	/**
	 * A s�neket t�rol� arraylist. A p�lya bet�lt�se ut�n ebben t�roljuk el a
	 * teljes s�nh�l�zatot
	 */
	private static ArrayList<Rail> railCollection;

	/**
	 * A vonatokat t�rol� kollekci�. A j�t�k fut�sa sor�n az adott p�ly�hoz
	 * tartoz� �temez�s szerint adagoljuk a p�ly�hoz tartoz� hossz�s�g�
	 * vonatokat a t�rol�ba a t�rol� addTrain(Train) met�dus�val.
	 */
	private static TrainCollection trainCollection;

	/**
	 * Az akt�v alag�tsz�lyak sz�m�t t�rolja. Seg�ts�g�vel, ha k�t akt�v
	 * alag�tsz�ly van, akkor l�trej�het az alag�t k�z�tt�k.
	 */
	private static int activeEntranceCounter;

	/**
	 * Ebben t�roljuk, melyik p�ly�t ind�tottuk el legutolj�ra.
	 */
	private static int lastPlayedMapNumber;

	// LOOOOOOOOOOOOONG
	/**A vizalis megjelenites ert felelol **/
	private static GUI gui; 
	/**A jatekot vezerli**/
	private static Thread mainThread;
	/**Kattintasokat kezel**/
	private static Thread controllThread;
	/**A vonat egyik blokkrol a masikra lepteteseig eltelo ido**/
	private static int STEPTIME = 1000;
	/**A jatek a kovetkezo leptetesen veget er e**/
	private static boolean fail = false;
	/**Szukseg van e meg a kattintasok kezelesere**/
	private static boolean stopControll = false;
	/**A jatek futasi sebessege**/
	private static int speed = 2;
	/**Hany kepkockaban er at az egyik blokkrol a masikra**/
	private static int stepParts = 20;
	/**A palya teljesitese utan lepjunk e a kovetkezore**/
	private static boolean continueLevels = false;
	/**hanyadik palya a jelenlegi**/
	private static int levelCounter = 1;

	/**A menetrendi vonatokat tarolja, ebben keresunk**/
	private static String scheduledTrains;
	/**A voantok indulasat kezeli**/
	private static int trainScheduleTimer;
	// LOOOOOOOOOOOOONG

	/**
	 * A GameController konstruktora.
	 */
	public GameController() {
		System.out.println("Class: GameController\t Object: GameController@STATIC\t Method: Constructor\t ");/*
																												 * Ki�rat�s
																												 * a
																												 * Szkeleton
																												 * vez�rl�s�nek
																												 */
		isTheGameRunning = false; /* Alapb�l nem fut a j�t�k */
		railCollection = new ArrayList<Rail>(); /* ? j list�t hozunk l�tre */
		trainCollection = new TrainCollection(); /*
													 * �j kollekci�t hozunk
													 * l�tre.
													 */

		gui = new GUI();
		;
	}

	/**
	 * �j j�t�k ind�t�s�ra szolg�l� f�ggv�ny. Megjelen�ti a j�t�kosnak a
	 * v�laszthat� p�ly�k list�j�t. Miut�n a j�t�kos kiv�lasztotta melyik p�ly�n
	 * akar j�tszani, bet�lti a p�ly�hoz tartoz� s�nh�l�zatot �s vonat
	 * �temez�st. Ezut�n elind�tja a vonatok l�ptet�s��rt felelos sz�lat.
	 * 
	 * @param mapNumber
	 *            a bet�ltendo p�lya sorsz�ma
	 */
	public static void startNewGame(int mapNumber) {
		fail = false;
		scheduledTrains="";
		trainScheduleTimer = 0;
		activeEntranceCounter=0;
		System.out.println(
				"Class: GameController\t Object: Object: GameController@STATIC\t Method: startNewGame\t Param: "
						+ mapNumber); /* Ki�rat�s a Szkeleton vez�rl�s�nek */

		String mapName = new String("maps/map" + mapNumber+ ".txt");
		
		/*mivel t�bb p�lya is lehet, ez�rt dinamikusan �ll�tjuk �ssze a bet�ltendo nevet.*/
		try {
			buildFromFile(
					mapName); /* Megpr�b�ljuk a f�jlb�l fel�p�teni a p�ly�t */
			isTheGameRunning = true; /* Elinditjuka j�tkot */

			startMainThread();
			startControllThread();

			System.out.println("\nClass: GameController\t Object: GameController@STATIC\t A j�t�k elindult\n"); /*
																												 * Ki�rat�s
																												 * a
																												 * Szkeleton
																												 * vez�rl�s�nek
																												 */
			lastPlayedMapNumber = mapNumber; /*
												 * Elmentj�k melyik p�ly�t
												 * t�lt�tt�k be utolj�ra.
												 */
		} catch (IOException e) {
			System.err.println(
					"\nClass: GameController\t Object: GameController@STATIC\t HIBA A P? LYA BET? LT? SE K? ZBEN\n"); /*
																														 * Ha
																														 * v�letlen
																														 * nem
																														 * lehet
																														 * bet�lteni
																														 * a
																														 * p�ly�t,
																														 * akkor
																														 * k�ld�nk
																														 * egy
																														 * �rtes�tst
																														 * errol
																														 */
		}
	}

	/**
	 * �rtes�ti a j�t�kost, hogy nyert, �s le�ll�tja a j�t�kot.
	 */
	public static void winEvent() {
		SoundManager.stopTrainSound();
		gui.getGameView().addAnimation(GameGUI.BOARDWIDTH / 2, GameGUI.BOARDHEIGHT / 2, "Win");
		SoundManager.playSound("Win");
		isTheGameRunning = false; /*
									 * Le�ll�tjuk a j�t�kot. Ez majd a GUI-t
									 * futtat� threadn�l lesz fontos
									 */
		System.out.println("Class: GameController\t Object: GameController@STATIC\t Method: winEvent\t Gyozelem."); /*
																													 * Ki�rat�s
																													 * a
																													 * Szkeleton
																													 * vez�rl�s�nek
																													 */
	}

	/**
	 * �rtes�ti a j�t�kost, hogy vesztett, �s le�ll�tja a j�t�kot.
	 */
	public static void loseEvent() {
		SoundManager.stopTrainSound();
		SoundManager.playSound("Lose");
		gui.getGameView().addAnimation(GameGUI.BOARDWIDTH / 2, GameGUI.BOARDHEIGHT / 2, "Lose");
		isTheGameRunning = false; /* Le�ll�tjuk a j�t�kot */
		continueLevels = false;
		System.out.println("Class: GameController\t Object: GameController@STATIC\t Method: loseEvent\t Vereseg"); /*
																													 * Ki�rat�s
																													 * a
																													 * Szkeleton
																													 * vez�rl�s�nek
																													 */
		System.out.println("Class: GameController\t Object: GameController@STATIC\t Michael Bay Effekt"); /*
																											 * Ki�rat�s
																											 * a
																											 * Szkeleton
																											 * vez�rl�s�nek
																											 */
	}

	/**
	 * Ebben a r�szben egy el�re kit�lt�tt sz�veges f�jlb�l ovlasunk majd be
	 * karaktereket, ami alapj�n automatikusan fel�pitj�k a p�ly�nkat. A f�jl
	 * k�t�tt form�tum�, ha ezt nem tartjuk a program hibval z�rulna. A
	 * sz�vegf�j formai k�vetelm�nyei: * Az els� sor a p�lya sz�less�ge �s
	 * magass�ga lesz, ;-vel elv�lasztva (pl.: 12;7) * Ezut�n az elementReadeben
	 * le�rtak alapj�n t�ltj�k ki af�jlunkat * Fontos hogy minden sorban
	 * pontosan annyi karakter legyen, amennyit meghat�roztunk kezdetben * Nincs
	 * ellen�rizve, hogy van e bel�p�si pontunk, de az akad�lytalan fut�s
	 * �rdek�ben aj�nlott. * Az egym�s mellett l�v� sinek szomsz�dosnak lesznek
	 * v�ve, �gy egy legal�bb egy mez�nyi helyet kell hagyni k�zt�k, hogy j�l
	 * �p�tse fel. pl.: ERRR xRxR xRRR Ha ezeket tartjuk, v�rhat�an j� eredm�nyt
	 * kapunk
	 * 
	 * A gyakorlati m�k�d�s: * Kezdetben l�trehozunk egy olvas�t, amivel
	 * soronk�nt v�gign�zz�k a f�jlt * Az els� sor�t kiolvassuk �s l�trehozunk 2
	 * t�mb�t: tempMap-ot ami konkr�t sineket t�rol �s a tempView-t ami a
	 * kiolvasott karaktereket. * A kiolvasott karakterekt�l f�gg�en az
	 * elementReader visszaadja amegfelel� tipus� Rail-t * V�gigolvassuk a f�jlt
	 * �s kit�lt�j�k a k�t t�mb�t
	 * 
	 * * Ezut�n sorban v�gigj�rjuk a tempView mez�it �s ha Rail, akkor megn�zz�k
	 * hogy a 8 szomsz�dj�b�l melyik l�tez� Rail * Ha a 8 szomsz�dos mez�n van
	 * Rail akkor azt felvessz�k az aktu�lis mez� szomsz�dai k�z� * V�g�l
	 * hozz�adjuk a szomsz�dokat a tempMap megfelel� Rail-j�hez * ? gyel�nk arra
	 * hogy az ellen�rz�tt 8 mez� ne l�gjon le a p�ly�r�l
	 * 
	 * * V�g�l bej�rjuk a tempMap-t �s az �sszes l�tez� Railt hozz�aduk a
	 * railCollectionhoz
	 * 
	 * Ekkorra minden szomsz�doss�g fel van �p�tve �s minden mez� megjelenik a
	 * collection-ban. A met�dus visszat�r.
	 * 
	 * @param filename
	 *            a beolvasand� file neve
	 * 
	 */

	private static void buildFromFile(String filename) throws IOException {
		System.out.println("Class: GameController\t Object: GameController@STATIC\t Method: buildFromFile\t Param: "
				+ filename
				+ "\t Betoltes."); /* Ki�rat�s a Szkeleton vez�rl�s�nek */

		/* Kezdetben meg�ll�tjk a j�t�kot �s t�r�lj�k azel�z� list�kat */
		isTheGameRunning = false;
		railCollection.clear();
		trainCollection.clear();
		System.out.println("Class: GameController\t Object: GameController@STATIC\t Method: railCollection.clear"); /*
																													 * Ki�rat�s
																													 * a
																													 * Szkeleton
																													 * vez�rl�s�nek
																													 */

		String in = ""; /* Egy beolvasott sort t�rol, ebbe olvasunk */
		String[] line; /* Felt�rdelj�k az el�z�leg olvasott sort */

		BufferedReader brMap = new BufferedReader(
				new FileReader(new File(filename))); /* T�rk�p File olvas� */

		line = brMap.readLine().split(";"); /* Kiolvassuk a p�lya m�ret�t */

		int width = Integer.parseInt(line[0]); /* P�lya sz�less�ge */
		int height = Integer.parseInt(line[1]); /* P�lya magass�ga */
		Rail[][] tempMap = new Rail[width][height]; /*
													 * P�lyaelem t�rol� m�trix
													 */
		String[][] tempView = new String[width][height]; /*
															 * P�lyaelem le�r�
															 * m�trix
															 */

		gui.setGameView(width, height, STEPTIME);

		int x = 0; /* Seg�dv�ltoz� sz�less�ghez */
		int y = 0; /* Seg�dv�ltpz� magass�ghoz */
		while (y < height && (in = brMap
				.readLine()) != null) { /* ? sszes marad�k sort kiolvassuk */
			line = in.split("");
			for (String s : line) { /* Minden karaktert megn�z�nk */
				tempMap[x][y] = elementReader(s); /* L�trehozzuk a t�pust */
				if (tempMap[x][y] != null) {
					tempMap[x][y].setX(x);
					tempMap[x][y].setY(y);
				}

				gui.getGameView().setBaseTileMap(x, y, s);
				gui.getGameView().addAnimation(x, y, s);
				tempView[x][y] = s; /* Mentj�k a v�zlat�t */
				x++;
			}
			x = 0;
			y++;
		}

		/*Beolvasott vonatokat t�roljuk*/
		scheduledTrains=brMap.readLine();
		/*Kiiratjuk a vez�rl�snek*/
		
 		System.out.println("\nClass: GameController\t Object: GameController@STATIC\t S�nek k�z�tti kapcsolatok l�trehoz�sa."); 
 		

		for (int i = 0; i < width; i++) { /*
											 * V�gign�zz�k a p�ly�t
											 * sz�less�gben...
											 */
			for (int j = 0; j < height; j++) { /* ... �s magass�gban */
				if (tempMap[i][j] != null) { /*
												 * Ha az aktu�lis elem nem �res,
												 * akkor lehetnek szomsz�dai
												 */
					ArrayList<Rail> tmp = new ArrayList<Rail>(); /* �j "szomsz�d t�rol�" */

					if (i - 1 >= 0 && tempMap[i - 1][j] != null)
						tmp.add(tempMap[i
								- 1][j]); /*
											 * Ha balra l�v� mez� a p�lya r�sze
											 * �s Rail akkor a szomsz�dja
											 */
					if (i + 1 < width && tempMap[i + 1][j] != null)
						tmp.add(tempMap[i
								+ 1][j]);/*
											 * Ha jobbra l�v� mez� a p�lya r�sze
											 * �s Rail akkor a szomsz�dja
											 */
					if (j - 1 >= 0 && tempMap[i][j - 1] != null)
						tmp.add(tempMap[i][j
								- 1]);/*
										 * Ha felette l�v� mez� a p�lya r�sze �s
										 * Rail akkor a szomsz�dja
										 */
					if (j + 1 < height && tempMap[i][j + 1] != null)
						tmp.add(tempMap[i][j
								+ 1]);/*
										 * Ha alatta l�v� mez� a p�lya r�sze �s
										 * Rail akkor a szomsz�dja
										 */

					/*
					 * Itt a sorrend sz�m�t, mert az XRail az els� kett�t �s a
					 * m�sodik kett�t kapcsolja p�rba
					 */

					tempMap[i][j]
							.setNeighbourRails(tmp); /*
														 * Hozz�adjuk az �jonnan
														 * felvett szomsz�dokat
														 */
				}
			}
		}

		for (int i = 0; i < width; i++) { /* Bej�rjuk a t�bl�t */
			for (int j = 0; j < height; j++) {
				if (tempMap[i][j] != null)
					railCollection
							.add(tempMap[i][j]); /*
													 * Ha van Rail tipus, akkor
													 * a kollekci�nk r�sze kell
													 * hogy legyen, felvessz�k.
													 */
			}
		}

		brMap.close();
		System.out.println("Class: GameController\t Object: GameController@STATIC\t L�trehozott p�lyaelemek sz�ma: "
				+ railCollection.size()); /* Megn�zz�k hogy v�ltozott e valam */
	}

	/**
	 * A map-ben minden egyes mezo egy betuvel van megadva. Mindegy egyes betu
	 * egy bizonyos cellat�pusnak felel meg. Al�bbi f�ggv�ny a megkapott betunek
	 * megfelelo s�nt�pust hoz l�tre, �s visszaadja mely beker�l a
	 * railCollection-be.
	 *
	 * @param mapChar
	 *            A map-ban szereplo karakter, mely egy s�nt�pust takar.
	 * @return A l�trehozott s�n.
	 */
	private static Rail elementReader(String mapChar) {
		System.out.println("\nClass: GameController\t Object: GameController@STATIC\t Method: elementReader\t Param: "
				+ mapChar
				+ "\t Elem dekodolasa fajlbol"); /*
													 * A jobb olvashat�s�g
													 * �rdek�ben ezelott egy �j
													 * sor van.
													 */
		switch (mapChar) {
		case "E":
			System.out.println("Beolvasott elem: EnterPoint");
			return new EnterPoint(); /* L�trehozunk egy �j enterPointot. */

		case "R":
			System.out.println("Beolvasott elem: Rail");
			return new Rail(); /* L�trehozunk egy �j Railt. */

		case "S":
			System.out.println("Beolvasott elem: Switch");
			return new Switch(); /* L�trehozunk egy �j Switchet. */

		case "U":
			System.out.println("Beolvasott elem: TunnelEntrance");
			return new TunnelEntrance(); /*
											 * L�trehozunk egy �j
											 * TunnelEntrance-t.
											 */

		case "X":
			System.out.println("Beolvasott elem: XRail");
			return new XRail(); /* L�trehozunk egy �j XRail-t. */

		case "1":
			System.out.println("Beolvasott elem: Red TrainStation");
			return new TrainStation(
					Color.RED); /*
								 * L�trehozunk egy �j TrainStation-t, mely piros
								 * sz�nu lesz.
								 */

		case "2":
			System.out.println("Beolvasott elem: Green TrainStation");
			return new TrainStation(
					Color.GREEN); /*
									 * L�trehozunk egy �j TrainStation-t, mely
									 * z�ld sz�nu lesz.
									 */

		case "3":
			System.out.println("Beolvasott elem: Blue TrainStation");
			return new TrainStation(
					Color.BLUE); /*
									 * L�trehozunk egy �j TrainStation-t, mely
									 * k�k sz�nu lesz.
									 */

		case "4":
			System.out.println("Beolvasott elem: Red TrainStation Passengers");
			TrainStation station0 = new TrainStation(Color.RED);
			station0.addPassengers();
			return station0; /*
								 * L�trehozunk egy �j TrainStation-t, mely piros
								 * sz�nu lesz utasokkal.
								 */

		case "5":
			System.out.println("Beolvasott elem: Green TrainStation Passengers");
			TrainStation station1 = new TrainStation(Color.GREEN);
			station1.addPassengers();
			return station1;/*
							 * L�trehozunk egy �j TrainStation-t, mely z�ld
							 * sz�nu lesz utasokkal.
							 */

		case "6":
			System.out.println("Beolvasott elem: Blue TrainStation Passengers");
			TrainStation station2 = new TrainStation(Color.BLUE);
			station2.addPassengers();
			return station2; /*
								 * L�trehozunk egy �j TrainStation-t, mely k�k
								 * sz�nu leszutasokkal.
								 */
		default:
			System.out.println("Beolvasott elem: ures ");
			return null; /*
							 * Ez a lehetos�g akkor fut le ha nem ismert bet�
							 * van a sz�veg�nben, mely ilyenkor egy �res mez�
							 * lesz
							 */
		}
	}

	/**
	 * Meg kell n�zni minden l�ptet�s ut�n hogy a vonatok ki�r�ltek e
	 * 
	 * @return Ha mindegyik vonat ki�r�lt, akkor igazzal t�r�nk vissza,
	 *         egy�bk�nt hamissal.
	 */
	private static boolean hasTheGameEnded() {
		System.out.println(
				"Class: GameController\t Object: GameController@STATIC\t Method: hasTheGameEnded\t Vonatok uressegenek ellenorzese");

		Boolean isAllEmpty = trainCollection.isAllEmpty()
				&& allStationsEmpty(); /*
										 * Ha mindegyik vonat ki�r�lt, akkor
										 * igazzal t�r�nk vissza, egy�bk�nt
										 * hamissal.
										 */
		System.out.println("Class: GameController\t Object: GameController@STATIC\t Returned: "
				+ isAllEmpty); /* Ki�rat�s a Szkeleton vez�rl�s�nek */
		return isAllEmpty;
	}

	/**
	 * Kiz�r�lag a tesztel�shez l�trehozott met�dus. Annyi alag�tsz�lyat tud
	 * aktiv�lni, ah�nyat megadunk neki. (ennek mennyis�g�t nem ellenorzi,
	 * hiszen azt a Main megfelelo r�sze m�r megtette.
	 * 
	 * A getClass fv csak az�rt kell bele, mert a v�gso verzi�ban a GUI-n
	 * t�rt�no kattint�sb�l egybol meg fogjuk tudni az eventet kiv�lt� objektum
	 * id-j�t, GUI hi�ny�ban azonban erre nincs lehetos�g�nk. Ha nagyon
	 * sz�ks�ges, meg tudjuk oldani en�lk�l is (de az ocsm�nyabb lenne) ez�rt
	 * enn�l maradtunk.
	 * 
	 * @param tunnelEntranceCounterToBeActivated
	 *            H�ny alag�tsz�lyat akarunk aktiv�lni.
	 */
	public void skeletonTesterActivateTunnelEntrance(int tunnelEntranceCounterToBeActivated) {
		for (int i = 0; i < tunnelEntranceCounterToBeActivated; i++) { /*
																		 * Annyi
																		 * alag�tsz�jat
																		 * keres�nk,
																		 * amennyit
																		 * param�ter�l
																		 * megadtak
																		 */
			Boolean notActivatedTunnelEntranceFound = false;
			while (!notActivatedTunnelEntranceFound) { /*
														 * am�g nem tal�lunk egy
														 * nem aktiv�lt
														 * alag�tsz�jat
														 */
				for (Rail oneRail : railCollection) {
					if (oneRail
							.getClass() == TunnelEntrance.class) { /*
																	 * megn�zz�k
																	 * TunnelEntrance
																	 * -e a s�n.
																	 */
						TunnelEntrance oneTunnel = (TunnelEntrance) oneRail; /*
																				 * Ha
																				 * igen,
																				 * akkor
																				 * �tkasztoljuk
																				 */
						if (!oneTunnel.checkIfActivated()) { /*
																 * Ha m�g nincs
																 * aktiv�lva
																 */
							oneTunnel.activate(); /*
													 * Aktiv�ljuk, �s
													 * megn�velj�k az akt�v
													 * alag�tsz�jak sz�m�t
													 * eggyel
													 */
							activeEntranceCounter++;
							notActivatedTunnelEntranceFound = true;
							break; /*
									 * Break, kezdodik a k�vetkezo keres�s�se,
									 * ha erre sz�ks�g van.
									 */
						}
					}
				}
			}

		}
		System.out.println("Class: GameController\t Object: GameController@STATIC\t Akt�v alag�tsz�jak sz�ma: "
				+ activeEntranceCounter);/* Ki�rat�s a Szkeleton vez�rl�s�nek */
		if (activeEntranceCounter == 2) { /*
											 * Ha k�t akt�v alag�tsz�junk van,
											 * akkor kezdem�nyezz�k az alag�t
											 * l�trehoz�s�t.
											 */
			System.out.println("\nClass: GameController\t Object: GameController@STATIC\t Alag�t l�trehoz�sa");/*
																												 * Ki�rat�s
																												 * a
																												 * Szkeleton
																												 * vez�rl�s�nek
																												 */

			/*
			 * Disclaimer: Az al�bbi k�dot Long csin�lta, sz�val God have mercy
			 * on your soul, if you want to understand this.
			 */
			Rail entrance1, entrance2;
			entrance1 = null;
			entrance2 = null;

			/* Kikeress�k a kett�t */
			for (Rail rail : railCollection) {
				if (rail.getClass() == TunnelEntrance.class) {
					if (entrance1 == null) {
						entrance1 = rail;
					} else {
						entrance2 = rail;
					}
				}
			}
			/* Seg�dv�ltoz�k */
			int e1X = entrance1.getX();
			int e1Y = entrance1.getY();
			int e2X = entrance2.getX();
			int e2Y = entrance2.getY();

			ArrayList<Rail> newTunnels = new ArrayList<Rail>();
			Tunnel tmp;

			/* Az elso r�sze a bel�p�si pont lesz */
			newTunnels.add(entrance1);

			/* Viszintes tengelyen vizsg�ljuk */
			/* Ha az els� alagut jobbr�bb van */

			if (e1X > e2X) {
				while (e1X > e2X) { /*
									 * Amig nem �r�nk egy szintre a kij�rattal
									 */
					if (e1X - 1 == e2X && e1Y == e2Y) { /*
														 * Pont tole jobbra van
														 * a kij�rat
														 */
						newTunnels.add(entrance2); /* Bek�tj�k a kij�rathoz */
						e1X--;
					} else { /* Ha ez m�g csak egy alagut */
						e1X--; /* K�zelebb hozzuk */
						tmp = new Tunnel(); /*
											 * L�trehozzuk �s be�ll�tjuk az
											 * adatait
											 */
						tmp.setX(e1X);
						tmp.setY(e1Y);
						newTunnels.add(tmp); /* Fel�pitj�k */
					}
				}
			}
			/* Ha az els� alagut balr�bb van */
			else if (e1X < e2X) {
				while (e1X < e2X) { /*
									 * Amig nem �r�nk egy szintre a kij�rattal
									 */
					if (e1X + 1 == e2X && e1Y == e2Y) { /*
														 * Pont tole balrara van
														 * a kij�rat
														 */
						newTunnels.add(entrance2); /* Bek�tj�k a kij�rathoz */
						e1X++;
					} else { /* Ha ez m�g csak egy alagut */
						e1X++; /* K�zelebb hozzuk */
						tmp = new Tunnel(); /*
											 * L�trehozzuk �s be�ll�tjuk az
											 * adatait
											 */
						tmp.setX(e1X);
						tmp.setY(e1Y);
						newTunnels.add(tmp); /* Fel�pitj�k */
					}
				}
			}

			/* F�gg�leges tengelyen vizsg�ljuk */
			/* Ha az els� alagut feljebb van */
			if (e1Y > e2Y) {
				while (e1Y > e2Y) { /*
									 * Amig nem �r�nk egy szintre a kij�rattal
									 */
					if (e1Y - 1 == e2Y
							&& e1X == e2X) { /* Pont felette van a kij�rat */
						newTunnels.add(entrance2); /* Bek�tj�k a kij�rathoz */
						e1Y--;
					} else { /* Ha ez m�g csak egy alagut */
						e1Y--; /* K�zelebb hozzuk */
						tmp = new Tunnel(); /*
											 * L�trehozzuk �s be�ll�tjuk az
											 * adatait
											 */
						tmp.setX(e1X);
						tmp.setY(e1Y);
						newTunnels.add(tmp); /* Fel�pitj�k */
					}
				}
			}
			/* Ha az els� alagut lejjebb van */
			else if (e1Y < e2Y) {
				while (e1Y < e2Y) { /*
									 * Amig nem �r�nk egy szintre a kij�rattal
									 */
					if (e1Y + 1 == e2Y
							&& e1X == e2X) { /* Pont alatta van a kij�rat */
						newTunnels.add(entrance2); /* Bek�tj�k a kij�rathoz */
						e1Y++;
					} else { /* Ha ez m�g csak egy alagut */
						e1Y++; /* K�zelebb hozzuk */
						tmp = new Tunnel(); /*
											 * L�trehozzuk �s be�ll�tjuk az
											 * adatait
											 */
						tmp.setX(e1X);
						tmp.setY(e1Y);
						newTunnels.add(tmp); /* Fel�pitj�k */
					}
				}
			}

			/* Minden alagutelemet felvett�nk */
			/* ? sszek�tj�k �ket */
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
	 * Kiz�r�lag a tesztel�shez l�trehozott met�dus. Ellenorzi h�ny akt�v
	 * alag�tsz�j van. Ha ketto, akkor lebontja az alagutat, �s deaktiv�l egyet.
	 * 
	 * A getClass fv csak az�rt kell bele, mert a v�gso verzi�ban a GUI-n
	 * t�rt�no kattint�sb�l egybol meg fogjuk tudni az eventet kiv�lt� objektum
	 * id-j�t, GUI hi�ny�ban azonban erre nincs lehetos�g�nk. Ha nagyon
	 * sz�ks�ges, meg tudjuk oldani en�lk�l is (de az ocsm�nyabb lenne) ez�rt
	 * enn�l maradtunk.
	 */
	public void skeletonTesterDeActivateATunnelEntrance() {

		Boolean activatedTunnelEntranceFound = false;
		while (!activatedTunnelEntranceFound) { /*
												 * am�g nem tal�lunk egy
												 * aktiv�lt
												 */
			for (Rail oneRail : railCollection) {
				if (oneRail
						.getClass() == TunnelEntrance.class) { /*
																 * Megkeress�k a
																 * TunnelEntrancet
																 */
					TunnelEntrance oneTunnel = (TunnelEntrance) oneRail; /* �tkasztoljuk */
					if (oneTunnel.checkIfActivated()) { /*
														 * Ellenorizz�k, hogy
														 * aktiv�lva van-e
														 */
						oneTunnel.deActivate(); /*
												 * Deaktiv�ljuk, mely sor�n
												 * kit�rlodik a
												 * szomsz�dlist�j�b�l a
												 * referencia az alag�tra
												 */
						activatedTunnelEntranceFound = true;
						break;
					}
				}
			}
		}

		if (activeEntranceCounter == 2) { /* Ha k�t akt�v TunnelEntrance volt */
			System.out.println("\nClass: GameController\t Object: GameController@STATIC\t Alag�t lerombol�sa"); /*
																												 * Ki�rat�s
																												 * a
																												 * Szkeleton
																												 * vez�rl�s�nek
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
		System.out.println("Class: GameController\t Object: GameController@STATIC\t Akt�v alag�tsz�jak sz�ma: "
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
	 * V�lt�t lehet vele �ll�tani, meg alagutat �p�teni. Ha a param�ter�l kapott
	 * helyen l�vo elemrol kider�ti, hogy alag�tsz�j-e; ha igen, akkor aktiv�lva
	 * van-e. Ha igen, deaktiv�lja, ha nem , aktiv�lja. Ha ez alag�t
	 * �p�t�st/bont�st von maga ut�n, akkor megteszi. Ezzel egyidoben �t�ll�tja
	 * az alag�tsz�j v�lt�j�t is (aktiv�l�s -> be az alag�tba, deaktiv�l�s ->
	 * alaphelyzetbe). Ha a kattintott elem v�lt�, akkor �t�ll�tja. Jelenleg
	 * nincs TELJESEN k�sz, a grafikus r�szben ezt fel kell majd iratkoztatni a
	 * kattint�sra. Jelenleg a kapott koordin�ta �gy nem a kattint�s�, hanem a
	 * konkr�t elem� amire kattintani akarunk. Ha mondjuk a cell�k 20*20
	 * pixelesek lesznek �s ide az �rkezik, hogy 33, 21, akkor ebbol m�g le kell
	 * hozni, hogy ez az 1-1-es indexu cella. Ha ez megvan onnant�l viszont ez
	 * m�r j� kell hogy legyen
	 * 
	 * @param X
	 *            Kattint�s X koordin�t�ja
	 * @param Y
	 *            Kattint�s Y koordin�t�ja
	 */
	public static void ClickHandler(int X, int Y, int btn) {
		if (isTheGameRunning && !gui.isGameClosed()) {
			for (Rail rail : railCollection) {
				if (rail.getX() == X && rail
						.getY() == Y) { /* Megkeress�k a kattintott elemet */
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
												"\nClass: GameController\t Object: GameController@STATIC\t Alag�t lerombol�sa");

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
							// ha van 2 ne legyen t�bb
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
			// V�get �rt, de m�g nem kattintottuk el..
			if (mainThread.isAlive())
				mainThread.stop();
			endGameSession();
			// Bet�lt�nk vagy nemtom

			if (continueLevels && levelCounter < 4) {
				levelCounter++;
				gui.getGameView().stopRender();
				gui.remove(gui.getGameView());
				startNewGame(100 + levelCounter);
			} else {
				// Vissza a men�be
				gui.getGameView().stopRender();
				gui.remove(gui.getGameView());
				gui.setMenuView();
				gui.getMenuView().startRender();
			}

		} else if (!gui.isMenuClosed() && gui.isGameClosed()) {
			// Men�benv�laszttunk
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
	 * Kiz�r�lag a tesztel�shez l�trehozott met�dus. A p�ly�n megkeresi az elso
	 * tunnelEntrancet amit tal�l �s be�ll�tja a k�v�nt �llapotra.
	 * 
	 * A getClass fv csak az�rt kell bele, mert a v�gso verzi�ban a GUI-n
	 * t�rt�no kattint�sb�l egybol meg fogjuk tudni az eventet kiv�lt� objektum
	 * id-j�t, GUI hi�ny�ban azonban erre nincs lehetos�g�nk. Ha nagyon
	 * sz�ks�ges, meg tudjuk oldani en�lk�l is (de az ocsm�nyabb lenne) ez�rt
	 * enn�l maradtunk.
	 */
	public void skeletonTesterSwitchATunnelEntrance() {
		Boolean tunnelEntranceFound = false;
		while (!tunnelEntranceFound) { /* Keres�nk egy TunnelEntrance-t */
			for (Rail oneRail : railCollection) {
				if (oneRail.getClass() == TunnelEntrance.class) { /*
																	 * v�gign�zz�k
																	 * melyik a
																	 * TunnelEntrance
																	 */
					TunnelEntrance oneTunnelEntrance = (TunnelEntrance) oneRail; /* �tkasztoljuk */
					oneTunnelEntrance
							.switchRail();/* v�ltoztatunk az �llapot�n egyet */
					tunnelEntranceFound = true;
					break;/* ha sz�ks�ges keres�nk egy k�vetkezot. */

				}
			}
		}
	}

	/**
	 * Kiz�r�lag a tesztel�shez l�trehozott met�dus A Main-tol megadott
	 * param�terekkel l�trehoz egy vonatot,
	 * 
	 * @param cabinColors
	 *            A kabinok sz�n�nek list�ja amibol a vonat fel�p�l
	 */
	public void skeletonTesterAddNewTrain(ArrayList<Color> cabinColors) {

		System.out.println("\nClass: GameController\t Object: GameController@STATIC\t ? j vonat hozz�ad�sa");/*
																												 * Ki�rat�s
																												 * a
																												 * Szkeleton
																												 * vez�rl�s�nek
																												 */
		Train testTrain = new Train(
				cabinColors); /*
								 * A megadott param�terekkel l�trehozunk egy �j
								 * vonatot
								 */

		EnterPoint enterPoint = null;

		Boolean enterPointFound = false; /*
											 * Kikeress�k melyik a
											 * railCollection-ben az EnterPoint
											 * �s azt elt�roljuk, hogy ut�na
											 * be�ll�tsuk elso s�nk�nt a
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
				enterPoint); /* Be�ll�tjuk elso s�nnek a bel�p�si pontot */
		trainCollection.addNewTrain(
				testTrain); /* Hozz�adjuk a trainCollection-be az �j vonatot. */
	}

	/**
	 * Kiz�r�lag a tesztel�shez l�trehozott met�dus L�pteti a vonatokat egyszer.
	 */
	public void skeletonTesterMakeTrainsMove() {
		System.out.println(
				"\nClass: GameController\t Object: GameController@STATIC\t ? tk�z�shez sz�ks�ges vonat hossz sz�ml�l�k cs�kkent�se");/*
																																		 * Ki�rat�s
																																		 * a
																																		 * Szkeleton
																																		 * vez�rl�s�nek
																																		 */
		for (Rail oneRail : railCollection) { /*
												 * Minden eggyes s�nnek
												 * cs�kkentj�k eggyel a rajta
												 * m�g �thalad� kabinok sz�m�t,
												 * mivel l�p egyet minden vonat.
												 */
			oneRail.lowerTrainLenghtCounter();
		}

		System.out.println("\nClass: GameController\t Object: GameController@STATIC\t Vonatok l�ptet�se"); /*
																											 * Ki�rat�s
																											 * a
																											 * Szkeleton
																											 * vez�rl�s�nek
																											 */
		trainCollection
				.moveAllTrains(); /*
									 * Minden egyes vonatot l�ptet�nk eggyel.
									 * K�sobb ezt egy thread fogja csin�lni,
									 * jelen szkeletonban el�g ennyi.
									 */
	}

	/**
	 * Kiz�r�lag a tesztel�shez l�trehozott met�dus
	 * 
	 * Kiiratjuk a fel�p�tett p�lya adatait egy f�jla hogy k�s�bb
	 * eleln�rizhess�k �ket
	 * 
	 * @param mapNumber
	 *            a bet�ltendo p�lya sz�ma
	 * @param name
	 *            ez lesz a l�trehozott f�jl neve
	 * @throws IOException
	 */
	public void createMapTestFile(int mapNumber, String name) throws IOException {
		PrintWriter writer = new PrintWriter(name + ".txt",
				"UTF-8"); /* Kell egy TXT �ro */

		String[] splitresult; /* Egyetlen sor felbont�sa */
		String result; /* Egy sor egy r�szlet�t t�rolja */
		for (Rail rail : railCollection) { /* Megn�z�nk minden sint */
			splitresult = rail.toString().split(
					"@"); /* Felmbontjuk az objekumot a pointere ment�n */
			result = rail.getClass()
					.getSimpleName(); /* Elk�rj�k az objektum oszt�ly�t */

			/*
			 * Ha �llom�sunk volt, annak szine is van, akkor a sz�nre is
			 * sz�ks�g�nk lesz
			 */
			if (rail.getClass().getSimpleName().toString().trim().equals("TrainStation")) {
				Color c = ((TrainStation) rail).getColor();
				if (c.equals(Color.RED))
					result += "1"; /*
									 * Ha piros volt, a neve ut�n �runk egy 1
									 * est
									 */
				if (c.equals(Color.GREEN))
					result += "2"; /*
									 * Ha z�ld volt, a neve ut�n �runk egy 2 est
									 */
				if (c.equals(Color.BLUE))
					result += "3"; /*
									 * Ha k�k volt, a neve ut�n �runk egy 3 mast
									 */
			}

			/* Egyebe f�zz�k az adatokat a k�vetkez� s�ma szerint: */
			/* n�v [x,y] (pointer) {szomsz�d1, szomsz�d2,...} */
			result += " [" + rail.getX() + "," + rail.getY() + "]" + " (" + splitresult[1] + ")";
			result += " {";
			int comacounter = rail.getNeighbourRails().size()
					- 1; /* Elemsz�m -1 vessz�t kell letenn�nk majd a f�jlba */
			for (Rail neighbour : rail.getNeighbourRails()) {
				splitresult = neighbour.toString().split("@");
				result += splitresult[1];
				if (comacounter > 0) { /*
										 * Sz�moljuk hogy h�ny vessz�t tett�nk
										 * le a szomszdok ut�na
										 */
					comacounter--;
					result += ",";
				}
			}
			result += "}"; /* Lez�rjuk */
			writer.println(result); /* Kiiratjuk a f�jlba */
		}
		writer.close(); /* Bez�rjuk az olvas�st */
		System.out.println("Save Done");
		MapCreationTest.main("maps/map" + mapNumber + ".txt",
				name + ".txt"); /* Ind�tjuk a tesztet */
	}

	/**
	 * A sz��kezel�s tesztel�s�re �rt egyszer� program, fix id�k�z�nk�nt gener�l
	 * egy t�rt�n�st.
	 * 
	 */
	public void runThreadTest() {
		Thread testThread = new Thread() {
			ArrayList<Color> testColors; /* Vonat kocsisz�neit t�rolja */
			Train testTrain; /* Egy teszt vonat */
			EnterPoint enterPoint; /* Egy v�letlen�l tal�lt bel�p�si pont */

			public void run() { /* Meg�rjuk az esemnyeket */
				testColors = new ArrayList<Color>(); /*
														 * Inicializ�ljuk a
														 * list�t
														 */
				testColors.add(Color.BLUE);/* Sz�nt adunk hozz� */
				testTrain = new Train(
						testColors); /*
										 * A szinekkel inicializ�lunk egy
										 * vonatot
										 */

				for (Rail oneRail : railCollection) { /*
														 * Kikeres�nk egy
														 * bel�p�s�i pontot
														 */
					if (oneRail.getClass() == EnterPoint.class) {
						enterPoint = (EnterPoint) oneRail;
					}
				}

				if (enterPoint == null) { /*
											 * Amennyiben null az objektumunk,
											 * nem volt bel�p�si pont a p�ly�n
											 */
					System.out.println("Nincs bel�p�si pont a p�ly�n");
					return; /* Ilyenkor v�ge a fut�snak */
				}

				/* A bel�p�si pontra rakjuk a vonatunk */
				testTrain.setNextRail(enterPoint);
				trainCollection.addNewTrain(testTrain);

				/* 16 l�ptet�st hajtunk v�gre */
				for (int i = 0; i < 16; i++) {
					for (Rail oneRail : railCollection) { /*
															 * Minden eggyes
															 * s�nnek
															 * cs�kkentj�k
															 * eggyel a rajta
															 * m�g �thalad�
															 * kabinok sz�m�t,
															 * mivel l�p egyet
															 * minden vonat.
															 */
						oneRail.lowerTrainLenghtCounter();
					}
					trainCollection.moveAllTrains();
					drawToConsole(); /* K�zben friss�tjuk a n�zetet */
					sleepTime(1000);
				}
			}
		};
		testThread.start(); /* Ind�tuk a sz�lat */
	}

	// Long
	/**Elinditja fo jatekkezelo szalat***/
	private static void startMainThread() {
		mainThread = new Thread() {
			public void run() {
				/*Inicializalja a UI-t es lejatsza a kezdesi hangokat, animaciot*/
				gui.getGameView().addAnimation(GameGUI.BOARDWIDTH / 2, GameGUI.BOARDHEIGHT / 2, "321GO");
				sleepTime(1400);
				gui.getGameView().paintTrain();
				SoundManager.playSound("Start");
				SoundManager.playTrainSound();
				/*Fix*/
				ClickHandler(0,0,0);
				
				/*Egy palya fo szala indul*/
				while (isTheGameRunning) {
					/*Cs�kkentj�k a sinek vagonsz�m�t */
					for (Rail oneRail : railCollection) { 
						oneRail.lowerTrainLenghtCounter();
					}
					
					/*Hozz�adjuk ha van uj vonat*/
					if(!scheduledTrains.equals("")){
						System.out.println("TIMESCHEDULE");
						scheduleTrains();
						trainScheduleTimer++;
					}
					
					/*Leptetünk egyet a vonatokon*/
					trainCollection.moveAllTrains();

					/*Jelezzuk a UInak*/
					gui.getGameView().moveAllTrain(trainCollection.getNextCoords());
					gui.getGameView().setCabStates(trainCollection.getCabStates());

					/*Ha lefutottunk a sinrol akkor vége*/
					if (fail) {
						trainCollection.moveAllTrains();
						/*Ha pont akkor futunk le mikor mindenki leszall akkor EPIC*/
						if (hasTheGameEnded()) {
							ultimateWinEvent();
						} else {
							/*Ha nem akkor vesztünk*/
							loseEvent();
						}
					} else if (hasTheGameEnded()) {
						/*Ha mindneki leszalt nyertunk*/
						winEvent();
					}
					/*a UI-t vegigleptetjük*/
					for (int j = 0; j < stepParts; j++) {
						gui.getGameView().updateTime(j * STEPTIME / stepParts);
						sleepTime(STEPTIME / stepParts / speed);
					}
				}
			}
		};
		/*inditjuk a palyat*/
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

	/**A GUI kattintasait elolvassa es evegzi**/
	private static void doGuiClickLogAction(String log) {
		/*Ha nem volt kattintas**/
		if (log == null || log.equals(""))
			return;
		String changedTiles[];
		/*Felbontjuk a kattintasokat**/
		if (log.contains(";")) {
			changedTiles = log.split(";");

		} else {
			/*Ha csak egy kattintas volt kulon kezeljuk*/
			changedTiles = new String[1];
			changedTiles[0] = log;
		}
		/*Ha tobb akkor vegig iteralunk*/
		for (String change : changedTiles) {
			String coords[] = change.split(",");
			ClickHandler(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]), Integer.parseInt(coords[2]));
		}

	}
	/**Jelzi ha veget fog erni a jatek**/
	public static void failIntent() {
		fail = true;
	}
	
	/**Megnezi hogy minden allomasrol feszaltak  az utasok**/
	private static boolean allStationsEmpty() {
		/*Vegig iteralunk rajtuk es ha valahol vannak meg akkor igazzal terunk vissza*/
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

	/** Volt e vagon aki most meg alagutban tartozkodik*/
	private static boolean wasTrainInTunnel() {
		/*Ha barmelyik alagutelem fogalalt akkor volt*/
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

	/**Epic Win eseten hiodik meg**/
	private static void ultimateWinEvent() {
		/*LEjatsza ahnagokat es megallitja a jatekot*/
		SoundManager.stopTrainSound();
		SoundManager.playSound("Epic");
		gui.getGameView().addAnimation(GameGUI.BOARDWIDTH / 2, GameGUI.BOARDHEIGHT / 2, "Epic");
		isTheGameRunning = false;
	}

	/**Varakoztatja a foszalat**/
	private static void sleepTime(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
			// Nem itt lesz baj
		}
	}

	/**Veget ert e a jatekmenet**/
	public static boolean sessionEnded() {
		return sessionEnded();
	}

	/**Lezarja a jatekmenetet**/
	private static void endGameSession() {
		gui.getGameView().stopRender();
		isTheGameRunning = false;
	}

	/**Elinditja a maneut**/
	public static void showMenu() {
		gui.setMenuView();
		startControllThread();
	}

	/**Mesgnezi hoyg az adott idopillanatban a menetrend szerint erkezik e vonat**/
	private static void scheduleTrains() {
		/*Vegignezzuk a sztringet es amelyi kvonatjaink az adott pillanatban indulnak azokat belerakjuk amugy tovabb haladunk*/
		String leftOver = "";
		String separateTrains[] = scheduledTrains.split(";");
		for (String train : separateTrains) {
			String data[] = train.split(",");
			try {
				/*Ha most indulna*/
				if (Integer.parseInt(data[1]) == trainScheduleTimer) {
					addTrain(train);
				} else {
					/*Ha nem msot indul*/
					leftOver += train+";";
				}
			} catch (Exception e) {
			}
		}
		scheduledTrains = leftOver;
	}

	/**Vonatokat tud hozzaadni a palyazhoz**/
	private static void addTrain(String trainData) {
		ArrayList<Color> cabColors = new ArrayList<Color>();
		Train newTrain = null;
		EnterPoint enterPoint = null;
		int enterCounter;

		/* Felbontjuk az adatokat */
		String datas[] = trainData.split(",");

		/* Megadjuk hogy h�nyadik pontra rakn�nk */
		enterCounter = Integer.parseInt(datas[0]);
		/*
		 * Az id�pontot m�r felhaszn�ltuk, ez�rt a szineket fogjuk msot
		 * haszn�lni
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
				cabColors); /* A szinekkel inicializ�lunk egy vonatot */

		/* Megkeress�k a bel�p�si pontj�t */
		for (Rail oneRail : railCollection) {
			if (oneRail.getClass() == EnterPoint.class) {
				enterCounter--;
				if (enterCounter == 0){
					enterPoint = (EnterPoint) oneRail;
					
				}
				
			}
		}
		
		
		
		/* Ha null akkor nem tal�ltuk megefelel� bel�p�si pontot */
		if (enterPoint == null) {
			System.out.println("Nincs bel�p�si pont a p�ly�n");
			return; /* Ilyenkor v�ge a fut�snak */
		}

		/* A bel�p�si pontra rakjuk a vonatunk */
		newTrain.setNextRail(enterPoint);
		trainCollection.addNewTrain(newTrain);

		/* A UI-hoz is hozz� adjuk */
		gui.getGameView().addTrain(datas[2], enterPoint.getX(), enterPoint.getY(), enterPoint.getNextRail(null).getX(),
				enterPoint.getNextRail(null).getY());
	}
	// Long

	public void drawToConsole() {

		int maxX = 0; /* mi a legnagyobb x index ( = p�lyasz�less�g - 1) */
		int maxY = 0; /* mi a legnagyobb y index ( = p�lyamagass�g - 1) */
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
						 * sajnos a railcollectionben nem sorfojtonosan egym�s
						 * ut�n vannak az elemek. Kell egy map helyette
						 */
		for (Rail rail : railCollection) {
			mapToDraw[rail.getY()][rail.getX()] = rail; /* felt�ltj�k a mapet */
		}

		char[][] charMap = new char[maxY + 1][maxX
				+ 1]; /*
						 * ebben vannak a konkr�t karakterek amiket ki fogunk
						 * �rni
						 */
		for (char[] cs : charMap) {
			for (char c : cs) {
				c = ' '; /*
							 * kezdetben minden legyen �res, majd fel�l lesznek
							 * �rva elemekkel
							 */
			}
		}

		for (int line = 0; line < maxY
				+ 1; line++) { /*
								 * charmap felt�lt�se ki�rand� betukkel a
								 * mapToDraw alapj�n
								 */
			for (int col = 0; col < maxX + 1; col++) {
				if (mapToDraw[line][col] == null) { /*
													 * Ha egy cella �res a
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

		/*------------------- Id�ig csak a statikus p�lya ki�r�sa volt, innetol j�n a vonat berak�sa -------------------*/

		for (int line = 0; line < maxY
				+ 1; line++) { /*
								 * foglalt s�nt �t�rjuk 'V'-re (V mint Vonat)
								 * Sajnos a s�neknek fogalmuk sincs pontosan mi
								 * l�pett r�juk
								 */
			for (int col = 0; col < maxX + 1; col++) {
				if (mapToDraw[line][col] == null) {
					continue;
				}
				if (mapToDraw[line][col].checkIfOccupied()) {
					if (charMap[line][col] != 'T') { /*
														 * Alag�tban nem
														 * l�tsz�dhat a vonat.
														 * Ha a betu eredetilet
														 * 'T' volt, meghagyjuk
														 */
						charMap[line][col] = 'V';
					}
				}
			}
		}

		for (char[] cs : charMap) { /* maga a konzolra �r�s */
			for (char c : cs) {
				System.out.print(c);
			}
			System.out.println();
		}
	}

}