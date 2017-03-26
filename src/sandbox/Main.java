package sandbox;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * A szkeleton vezérléséért felelõs osztály, mely kezeli a felhasználóval való kommunikációt és az alapján vezérli a programot.
 * A szkeleton tesztelésére 2 teljes és 6 további egyszerûsített teszt pályát hoztunk létre. A hat rövid pálya teljes játék indítására nem alkalmas.
 */
public class Main {
	public static void main(String[] args) throws IOException{
		 Scanner scanner = new Scanner(System.in);
		 /* Ezzel történik majd a konzolon felhasználó által bemeneten megadott karakter beolvasása */
		 
		 boolean run = true;
		 /* Ez a boolean változó felel azért, hogy a fõciklus ami a teszteket tartalmazza folyamatosan fusson egészen */
		 /* addig amíg az értékét át nem állítják false-ba */
		 
		 /* A skeleton tesztelõ fõciklusa. Folyamatosan fut, amíg a run értékét át nem írják false-ba. Ezt a kilépésre
		  * szolgáló billentyû lenyomásával lehet parancsba adni.
		  */
		while(run){
			
			/* A program elõször felkínálja a felhasználónak a teszteseteket amiket
			 * az alább feltüntetett billenytûk lenyomásával lehet elérni. 
			 * Az alábbi tesztesetek a konzolra íródnak ki de a dokumentációban is megtalálható a részletes leírásuk
			 * A felhasználó a bemenetet úgyszintén  a konzolon keresztül viheti be.
			 * Ezek után a program által adott válasz, vagyis a konkrét teszteset lefutásának
			 * az eredménye a konzolon jelenik meg.
			 */
			
			System.out.println("\n Válassz az alábbi lehetõségek közül:"
					+ "\n 1: Új játék indítása"
					+ "\n 2: Alagútszáj aktiválása"
					+ "\n 3: Alagútszáj deaktiválása"
					+ "\n 4: Váltó állítása"
					+ "\n 5: Vonatok léptetése"
					+ "\n 6: Új vonat érkezése"
					+ "\n 7: Gyõzelem"
					+ "\n 8: kilépés");
			
			/* Fentebb olvashatóak a tesztesetek. 
			 * Az egyes billentyû lenyomásának hatására a szkeleton tesztelõ program elindítja az "Új játék indítása" tesztesetet. 
			 * A kettes billentyû lenyomásának hatására a szkeleton tesztelõ program elindítja az "Alagútszáj aktiválása" tesztesetet. 
			 * A hármas billentyû lenyomásának hatására a szkeleton tesztelõ program elindítja az "Alagútszáj deaktiválása" tesztesetet. 
			 * A négyes billentyû lenyomásának hatására a szkeleton tesztelõ program elindítja a "Váltó állítása" tesztesetet. 
			 * Az ötös billentyû lenyomásának hatására a szkeleton tesztelõ program elindítja a "Vonatok léptetése" tesztesetet. 
			 * A hatos billentyû lenyomásának hatására a szkeleton tesztelõ program elindítja az "Új vonat érkezése" tesztesetet. 
			 * A hetes billentyû lenyomásának hatására a szkeleton tesztelõ program elindítja az "Gyõzelem" tesztesetet. 
			 * A nyolcas billentyû lenyomásának hatására a szkeleton tesztelõ program futása megszakad.
			 */
			
			switch(Integer.parseInt(scanner.nextLine())){
			/* Elõször is intté kasztolom a bemenetet.*/
			
				/* --------------------------------------------------------------------------------- 1. MENÜPONT ----------------------------------------------------------------------------- */
			
				case 1: System.out.println("1: Új játék indítása. Melyik pálya legyen? [1, 2]");
				/* Ez a teszteset új játékot indít. Bemenetként vár a felhasználótól egy számot, hogy melyik pálya
				 * induljon el. */
					switch(Integer.parseInt(scanner.nextLine())){
						case 1: System.out.println("\n1. pálya indítása...");	
						/* Ez a programrész akkor fut le, ha a felhasználó az elsõ pályát választja. Létrejön egy GameController. */
						/* a gc utáni szám azt mutatja, melyik feladat melyik részfeladatához tartozik. */
						GameController gc11 = new GameController();
						gc11.startNewGame(1); /* elindítjuk a gameControllert az 1-es pályával-  */
						break;
						
						case 2: System.out.println("\n2. pálya indítása...");
						/* Ez a programrész akkor fut le, ha a felhasználó a második pályát választja. Létrejön egy GameController. */
						GameController gc12 = new GameController();
						gc12.startNewGame(2);  /* elindítjuk a gameControllert az 2-es pályával-  */
						break;
/**LONG TESZTEL--------------------------*/
						case 69: System.out.println("\nXRail Teszt...");
						/* Ez a programrész akkor fut le, ha a felhasználó a 69. pályát választja. Létrejön egy GameController. */
						GameController gc169 = new GameController();
						gc169.startNewGame(69);  /* elindítjuk a gameControllert az 69-es pályával-  */
						break;
/**LONG TESZTEL--------------------------*/						
						/* Ez a programrész akkor fut le, ha a felhasználó nem 1-est vagy kettest adott meg bemenetként. 
						 * Ilyen pálya ugyanis nincs. Ekkor a program visszatér a fõmenübe. */
						default: System.out.println("Csak 1. és 2. pálya van. Az elvárt bemenet az '1' vagy a '2'");
					}
				break;
					
				
				/* --------------------------------------------------------------------------------- 2. MENÜPONT ----------------------------------------------------------------------------- */
				
				/* Ez a teszteset alagútszáájak lérehozását teszi lehetõvé a felhasználó számára. 
				 * Elõször megkérdezi, hány alagútszáj van nyitva jelenleg a pályán. Egyszerre csak két  
				 * alagútszáj lehet nyitva a pályán, így a megengedett bemenetek a 0 és az 1.
				 */
				case 2: System.out.println("Alagútszály aktiválása. Hány alagútszály van jelenleg nyitva? [0, 1]");
				switch (Integer.parseInt(scanner.nextLine()))
				{
					case 0: System.out.println("Elsõ alagútszáj aktiválása...");
					/* Ez a kódrészlet fut le akkor, ha  a felhasználó futtatja az alagútszáj aktiválása tesztesetet
					 * úgy, hogy, a pályán a éppen nincs egyetlen alagútszáj se nyitott állapotban.
					 */
					GameController gc21 = new GameController();
					gc21.startNewGame(1);  /* elindítjuk a gameControllert az 1-es pályával-  */
					gc21.skeletonTesterActivateTunnelEntrance(1);/* Aktiváljuk az elsõ alagútszájat. */
					break;
					
					case 1: System.out.println("Elsõ és második alagútszáj aktiválása...");
					/* Ez a kódrészlet fog lefutni abban az esetben, ha a pályán már található egy aktív
					 * alagútszály és mi még egy alagútszájat szeretnénk létrehozni mellé. Ekkor az alagútszájon
					 * kívül egy alagút is létrejön a két alagútszáj között.
					 */
					GameController gc22 = new GameController();
					gc22.startNewGame(1);  /* elindítjuk a gameControllert az 1-es pályával-  */
					gc22.skeletonTesterActivateTunnelEntrance(1);/* Aktiváljuk az elsõ alagútszájat. */				
					break;
					
					/* Amennyiben a felhasználó nem az 1-es vagy a 2-es gombot nyomta meg, egyik fenti teszteset
					 * sem fog lefutni. A szkeleton tesztelõ visszatér a fõmenübe.
					 */
					default: System.out.println("Nincs ilyen opció. Az elvárt bemenet az '1' vagy a '2'");
				}
				break;
				
				
				/* --------------------------------------------------------------------------------- 3. MENÜPONT ----------------------------------------------------------------------------- */
				
				/* Ez a teszteset alagútszáj deaktiválására szolgál.
				 * Mivel egyszerre csak két alagútszáj lehet aktív a pályán, ezért  a megengedett bemenetek az 1 és a 2.
				 * Értelemszerûen kettõnél több aktív alagútszáj nem létezhet egy pályán egyszerre, ugyanis
				 * ez szembemenne a feladat specifikációjával. Ugyanitt, nem lehet 0 aktív alagútszáj, ha éppen deaktiválni
				 * szeretnénk egy alagútszájat, ebben az esetben ugyanis nem lenne mit deaktiválni.
				 */
				case 3: System.out.println("Alagútszály deaktiválása. Hány alagútszáj van jelenleg nyitva? [1, 2]");
				switch (Integer.parseInt(scanner.nextLine()))
				{
					case 1: 	
					/* Ez a programrész akkor fut le, amikor egy alagútszájat deaktiválunk úgy, hogy ez volt az egyetlen alagútszáj a pályán.
					 * Ebben az esetben csak egyszerûen deaktiválódik az alagútszáj.
					 */
						GameController gc31 = new GameController();
						gc31.startNewGame(1);  /* elindítjuk a gameControllert az 1-es pályával-  */
						gc31.skeletonTesterActivateTunnelEntrance(1);/* Aktiváljuk az elsõ alagútszájat. */
						
						System.out.println("\nEgyedüli alagútszáj zárása..."); 
						gc31.skeletonTesterDeActivateATunnelEntrance(); /* deaktiváljuk az alagútszájat */
					break;
					
					case 2: 
					/* Ez a programrész akkor fut le, ha a felhasználó úgy deaktivál alagútszájat, hogy a 
					 * pályán két alagútszáj volt aktív. Ebben az esetben az alagút a két alagútszáj között megszûnik, 
					 * és eltûnik az alagútszáj.
					 */
						GameController gc32 = new GameController();
						gc32.startNewGame(1);  /* elindítjuk a gameControllert az 1-es pályával-  */
						gc32.skeletonTesterActivateTunnelEntrance(2);/* Aktiválunk két alagútszájat. */
						
						System.out.println("\nAlagút bontása és az egyik alagútszáj zárása...");
						gc32.skeletonTesterDeActivateATunnelEntrance(); /* Deaktiváljuk az egyik alagútszájat mely során megszûnik az alagút. */
					break;
					
					/* Ha a felhasználó nem 1-et vagy 2-t adott meg bemenetként akkor a fenti 
					 * tesztesetek közül egyik se fut le. A program visszalép a fõmenübe.
					 */
					default: System.out.println("Csak akkor lehet alagútszájat zárni, ha 1 vagy 2 van nyitva");
				}
				break;
				
				
				/* --------------------------------------------------------------------------------- 4. MENÜPONT ----------------------------------------------------------------------------- */
				
				/* 
				 * Ez a teszteset a pályán található váltók állítását szimulálja. 
				 * A váltóknak két állása van, egy alaphelyzet, amiben a rajta áthaladó
				 * vonat haladási iránya nem változik meg. A másik az alternatív helyzetben a 
				 * vonat irányt vált. 
				 * A tesztesethez használt map-en csak egy váltó van. (map3.txt)
				 */
				case 4: System.out.println("Váltó állítás. A váltó eredetileg módosította az irányt? [i/n]");
				
				switch (scanner.nextLine().charAt(0))
				{
					case 'I': 
					case 'i': 
					/* Ez a kódrészlet akkor fut le ha a váltó alternatív helyzetben volt és mi alaphelyzetbe
					 * szeretnénk állítani. Ez majd GUI-n eventekkel történik majd a jövõben. Mivel a váltó alaphelyzetbõl indul,
					 * ehhez elõbb ki kell billenteni az alternatív helyzetbe, majd onnan vissza.
					 */
					GameController gc41 = new GameController();
					gc41.startNewGame(1);  /* elindítjuk a gameControllert az 3-as pályával  */
					gc41.skeletonTesterSwitchASwitch(); /* Beállítjuk a váltót, hogy a feladat kiírásnak megfelelõ alaphelyzetben álljon. */
					
					System.out.println("Váltó állítása alaphelyzetbe..."); 
					gc41.skeletonTesterSwitchASwitch(); /* Állítunk egyet a váltón. */
					break;
					
					case 'N':
					case 'n': 
					/*Ez a kódrészlet akkor fut le ha a váltó alaphelyzetben volt
					 * és mi szeretnénk a másik állapotába helyezni. Ez majd GUI-n eventekkel történik majd a jövõben.
					 */
					GameController gc42 = new GameController();
					gc42.startNewGame(1);  /* elindítjuk a gameControllert az 3-as pályával  */
					
					
					System.out.println("Váltó állítása alternatív helyzetbe..."); /*A váltó alapból alap helyzetben van, így csak egy váltás szükséges rajta */
					gc42.skeletonTesterSwitchASwitch(); /* Állítunk egyet a váltón. */
					break;
					
					/* Ha a felhasználó nem i vagy n bemenetet adott, akkor a
					 * fenti tesztesetek közül egyik sem fut le. Ehelyett a program visszalép 
					 * a fõmenübe.
					 */
					default: System.out.println("Csak az 'i' és 'n' a támogatott bemenet");
				}
				break;
				
				
				/* --------------------------------------------------------------------------------- 5. MENÜPONT ----------------------------------------------------------------------------- */
				
				/*Ez a teszteset a vonat léptetését szimulálja
				 * A vonat 5 különbözõ sínre léphet. Ezek a következõk:
				 * Az egyes billentyû lenyomásának hatására a léptetést szimuláló teszteset sínre lépteti a vonatot. 
				 * A kettes billentyû lenyomásának hatására a léptetést szimuláló teszteset váltóra lépteti a vonatot. 
				 * A váltó egy sepciális sín
				 * A hármas billentyû lenyomásának hatására a léptetést szimuláló teszteset alagútszájra lépteti a vonatot. 
				 * Itt fontos megjegyezni, hogy az alagútszáj is egyfajta váltó, aminek az alternatív állása vezet
				 * az alagútba. Alapállásában a vonat elkerüli az alagutat.
				 * A négyes billentyû lenyomásának hatására a léptetést szimuláló teszteset megállóra lépteti a vonatot. 
				 * A megálló is egyfajta speciális sín ami színnel rendelkezik
				 * Az ötös billentyû lenyomásának hatására a léptetést szimuláló teszteset belépési pontra lépteti a vonatot.
				 * A belépési pont egyfajta speciális sín, amin keresztül bejutnak a vonatok a pályára. Ez a teszteset viszont
				 * azt szimulálja, hogy a vonat ezen  keresztül elhagyja a pályát.
				 * Az ötös billenytû egy speciális teszteset, ugyanis ez nem sínhez köthetõ, hanem azt szimulálja
				 * hogy két vonat összeütközik. 
				 */
				case 5: System.out.println("Vonatok léptetése. Hova lép a vonat?"
						+ "\n 1: sínre"
						+ "\n 2: váltóra"
						+ "\n 3: alagútszájra"
						+ "\n 4: megállóra"
						+ "\n 5: Vonat elhagyja a játékteret belépési ponton keresztül"
						+ "\n 6: Két vonat ütközik");
				switch (Integer.parseInt(scanner.nextLine()))
				{
					case 1: System.out.println("A vonat sínre lép..."); 
					/*Ez a programrész fut le akkor, ha a vonat egy egyszerû sínre lép. */
					
						ArrayList<Color>cabinColors = new ArrayList<Color>(); /* Létrehozunk egy színlistát. Ebbõl fog felépülni a vonat. */
						cabinColors.add(Color.RED); /* A színlistához hozzáadunk egy piros színt. */
						cabinColors.add(Color.GREEN); /* A színlistához hozzáadunk egy zöld színt. */
						cabinColors.add(Color.BLUE); /* A színlistához hozzáadunk egy kék színt. */
						
						GameController gc51 = new GameController(); /* létrehozunk egy gameControllert */
						gc51.startNewGame(51); /* elindítjuk a játékot az 51-es pályán*/
						gc51.skeletonTesterAddNewTrain(cabinColors); /* hozzáadjuk az újonnan létrehozott vonatot */
						gc51.skeletonTesterMakeTrainsMove(); /* Léptetjük egyet a vonatokat. */
						gc51.skeletonTesterMakeTrainsMove(); /* Léptetjük egyet a vonatokat. */
					break;
					
					case 2: System.out.println("A vonat váltóra lép. A váltó alaphelyzetben (i) vagy az elternatív helyzetben (n) van? [i, n]");
						switch (scanner.nextLine().charAt(0)){
							case 'I': 
							case 'i': System.out.println("A vonat egyenesen halad tovább..."); 
							/* Ez a programrész akkor fut le, ha a vonat egy váltóra lép és a váltó alapállapotban van */
							ArrayList<Color>cabinColors21 = new ArrayList<Color>(); /* Létrehozunk egy színlistát. Ebbõl fog felépülni a vonat. */
							cabinColors21.add(Color.RED);/* A színlistához hozzáadunk egy piros színt. */
							cabinColors21.add(Color.GREEN);/* A színlistához hozzáadunk egy zöld színt. */
							cabinColors21.add(Color.BLUE);/* A színlistához hozzáadunk egy kék színt. */
							
							GameController gc521 = new GameController(); /* létrehozunk egy gameControllert */
							gc521.startNewGame(52); /* elindítjuk a játékot az 52-es pályán*/
							gc521.skeletonTesterAddNewTrain(cabinColors21); /* hozzáadjuk az újonnan létrehozott vonatot */
							gc521.skeletonTesterMakeTrainsMove(); /* Léptetjük egyet a vonatokat. */
							gc521.skeletonTesterMakeTrainsMove();/* Léptetjük egyet a vonatokat. */
							break;
							
							case 'N':
							case 'n': System.out.println("A vonat elkanyarodik...");
							/* Ez a programrész akkor fut le, ha a vonat egy váltóra lép és a váltó alternatív   van */
							ArrayList<Color>cabinColors22 = new ArrayList<Color>();/* Létrehozunk egy színlistát. Ebbõl fog felépülni a vonat. */
							cabinColors22.add(Color.RED);/* A színlistához hozzáadunk egy piros színt. */
							cabinColors22.add(Color.GREEN); /* A színlistához hozzáadunk egy zöld színt. */
							cabinColors22.add(Color.BLUE); /* A színlistához hozzáadunk egy kék színt. */
							
							GameController gc522 = new GameController(); /* létrehozunk egy gameControllert */
							gc522.startNewGame(52); /* elindítjuk a játékot az 52-es pályán*/
							gc522.skeletonTesterSwitchASwitch();  /* Állítunk egyet a váltón. */
							gc522.skeletonTesterAddNewTrain(cabinColors22); /* hozzáadjuk az újonnan létrehozott vonatot */
							gc522.skeletonTesterMakeTrainsMove();/* Léptetjük egyet a vonatokat. */
							gc522.skeletonTesterMakeTrainsMove();/* Léptetjük egyet a vonatokat. */
							break;
							
							default: System.out.println("Csak az 'i' és 'n' a támogatott bemenet");
						}
					break;
					
					
					case 3: System.out.println("A vonat alagútszájra lép. A váltó az alagútszáj felé irányítja a vonatot? [i, n]");
					switch (scanner.nextLine().charAt(0))
					{
						case 'I':
						case 'i': System.out.println("Hány aktív alagútszáj van a pályán? [1, 2]"); 
							switch (Integer.parseInt(scanner.nextLine()))
							{
							case 1: System.out.println("A vonat belép a zsákutca alagútba..."); 
							/* Ez a teszteset akkor fut le, ha a vonat alagútba lépne, de nincs alagút, mert csak egy aktív alagútszáj 
							 * található jelenleg a pályán.
							 */
							
							ArrayList<Color>cabinColors31 = new ArrayList<Color>();/* Létrehozunk egy színlistát. Ebbõl fog felépülni a vonat. */
							cabinColors31.add(Color.RED); /* A színlistához hozzáadunk egy piros színt. */
							cabinColors31.add(Color.GREEN); /* A színlistához hozzáadunk egy zöld színt. */
							cabinColors31.add(Color.BLUE); /* A színlistához hozzáadunk egy kék színt. */
							
							GameController gc531 = new GameController(); /* létrehozunk egy gameControllert */
							gc531.startNewGame(53); /* elindítjuk a játékot az 53-as pályán*/
							gc531.skeletonTesterActivateTunnelEntrance(1); /* aktiválunk egy alagútszájat */
							gc531.skeletonTesterSwitchATunnelEntrance(); /* váltunk egyet az alagútszájon lévõ váltón. */ 
							
							gc531.skeletonTesterAddNewTrain(cabinColors31); /* hozzáadjuk az újonnan létrehozott vonatot */
							gc531.skeletonTesterMakeTrainsMove(); /* Léptetjük egyet a vonatokat. */
							gc531.skeletonTesterMakeTrainsMove();/* Léptetjük egyet a vonatokat. */
							break;
							
							case 2: System.out.println("A vonat belép az alagútba...");
							/* Ez a teszteset akkor fut le, ha a vonat alagútba lép és van is alagút, vagyis a 
							 * pályán két aktív alagútszáj található.
							 */
							ArrayList<Color>cabinColors32 = new ArrayList<Color>();/* Létrehozunk egy színlistát. Ebbõl fog felépülni a vonat. */
							cabinColors32.add(Color.RED); /* A színlistához hozzáadunk egy piros színt. */
							cabinColors32.add(Color.GREEN);/* A színlistához hozzáadunk egy zöld színt. */
							cabinColors32.add(Color.BLUE);/* A színlistához hozzáadunk egy kék színt. */
							
							GameController gc532 = new GameController();/* létrehozunk egy gameControllert */
							gc532.startNewGame(53);/* elindítjuk a játékot az 53-as pályán*/
							gc532.skeletonTesterActivateTunnelEntrance(2);/* Két alagútszájat aktiválunk */
							gc532.skeletonTesterSwitchATunnelEntrance(); /* az egyiken váltunk egyet. */
							
							gc532.skeletonTesterAddNewTrain(cabinColors32); /* hozzáadjuk az újonnan létrehozott vonatot */
							gc532.skeletonTesterMakeTrainsMove();/* Léptetjük egyet a vonatokat. */
							gc532.skeletonTesterMakeTrainsMove();/* Léptetjük egyet a vonatokat. */
							break;
							
							default: System.out.println("Csak akkor léphet a vonat alagútba, ha, ha 1 vagy 2 nyitott alagútszáj van.");
						}
						break;
						case 'N':
						case 'n': System.out.println("A vonat elkerüli az alagutat...");
						/* Ha az alagútszájnál álló váltó alaphelyzetben van akkor a vonat nem megy be az alagútba*/
						ArrayList<Color>cabinColors33 = new ArrayList<Color>();/* Létrehozunk egy színlistát. Ebbõl fog felépülni a vonat. */
						cabinColors33.add(Color.RED);/* A színlistához hozzáadunk egy piros színt. */
						cabinColors33.add(Color.GREEN);/* A színlistához hozzáadunk egy zöld színt. */
						cabinColors33.add(Color.BLUE);/* A színlistához hozzáadunk egy kék színt. */
						
						GameController gc533 = new GameController();/* létrehozunk egy gameControllert */
						gc533.startNewGame(53);/* elindítjuk a játékot az 53-as pályán*/
						
						gc533.skeletonTesterAddNewTrain(cabinColors33);/* hozzáadjuk az újonnan létrehozott vonatot */
						gc533.skeletonTesterMakeTrainsMove();/* Léptetjük egyet a vonatokat. */
						gc533.skeletonTesterMakeTrainsMove();/* Léptetjük egyet a vonatokat. */
						break;
						
						default: System.out.println("Csak az 'i' és 'n' a támogatott bemenet");
					}
					break;
					case 4: System.out.println("A vonat ZÖLD megállóra lép. Van nem üres kabin a vonatban? [i, n]");
					/* Ha a vonat megállóra lép, az elsõ kérdés, hogy utaznak-e a vonatban. Ha nem, akkor a vonat
					 * egyszerûen áthalad a megállón. Ha van olyan kabin ami nem üres akkor ha az elsõ 
					 * nem üres vagon színe megegyezik a megállóéval, leszállnak az utasok arról a vagonról.
					 */
						switch (scanner.nextLine().charAt(0))
						{
							case 'I':
							case 'i': System.out.println("Milyen színû az elsõ nem üres vagon? Piros, zöld vagy kék lehet. [r, g, b]"); 
							//* Ebben tároljuk a vagon színét amit a felhasználó megad. */
							char vagonSzin = scanner.nextLine().charAt(0);		
							Color cabColor = null; /* Ilyen színû lesz majd a vagon. */
														
							if(vagonSzin == 'r' || vagonSzin == 'R'){ /* ellenõrizzük a színeket. Ha piros, akkor nem fognak leszállni utasok, és megadjuk a cabColort pirosnak. */
								System.out.println("Nem szállnak le az utasok...");
								cabColor = Color.RED;
							} else if(vagonSzin == 'g' || vagonSzin == 'G'){ /* Ha a vagon zöld, akkor le kell majd száljanak az utasok. megadjuk a cabColort zöldnek. */
								System.out.println("Leszállnak az utasok...");
								cabColor = Color.GREEN;
							} else if(vagonSzin == 'b' || vagonSzin == 'B'){ /* Ha kék, akkor szintén nem fognak leszállni az utaasok. Megadjuk a cabColort kéknek. */
								System.out.println("Nem szállnak le az utasok...");
								cabColor = Color.BLUE;
							} else {						
								System.out.println("A vagonok és a megállók csak 'r', 'g' vagy 'b' színûek lehetnek");
								break;
							}
							
							
							ArrayList<Color>cabinColors41 = new ArrayList<Color>();/* Létrehozunk egy színlistát. Ebbõl fog felépülni a vonat. */
							cabinColors41.add(cabColor);/* A színlistához hozzáadjuk a kiválasztott színt. */
							
							GameController gc541 = new GameController();/* létrehozunk egy gameControllert */
							gc541.startNewGame(54);/* elindítjuk a játékot az 54-es pályán*/
							
							gc541.skeletonTesterAddNewTrain(cabinColors41);/* hozzáadjuk az újonnan létrehozott vonatot */
							gc541.skeletonTesterMakeTrainsMove();/* Léptetjük egyet a vonatokat. */
							gc541.skeletonTesterMakeTrainsMove();/* Léptetjük egyet a vonatokat. */
						break;
						
						case 'N':
						case 'n': System.out.println("A vonat áthalad a megállón és semmi más nem történik...");
						/* Ez a programrész akkor fut le, ha senki nem utazott a vonaton mikor az a megállóra lépett. 
						 * Ekkor ugyanis nincs kinek leszállnia a vonatról. */
						ArrayList<Color>cabinColors42 = new ArrayList<Color>();/* Létrehozunk egy színlistát. Ebbõl fog felépülni a vonat. */
						cabinColors42.add(Color.BLACK); /* Hozzáadjuk a színlistához a fekete színt, így olyan, mint ha üres lenne a vagon */
						
						GameController gc542 = new GameController(); /* létrehozunk egy gameControllert */
						gc542.startNewGame(54);/* elindítjuk a játékot az 54-es pályán*/
						
						gc542.skeletonTesterAddNewTrain(cabinColors42);/* hozzáadjuk az újonnan létrehozott vonatot */
						gc542.skeletonTesterMakeTrainsMove();/* Léptetjük egyet a vonatokat. */
						gc542.skeletonTesterMakeTrainsMove();/* Léptetjük egyet a vonatokat. */
						break;
						
						/* a program visszalép a fõmenübe, ha nem kap érvényes bemenetet arra a kérdésre
						 * hogy utanak-e a vonaton
						 */
						default: System.out.println("Csak az 'i' és 'n' a támogatott bemenet");
					}
					break;
					
					case 5: System.out.println("A vonat elhagyja a pályát...");
					/* Ez a teszteset szimulálja, hogy a vonat egy bemeneti ponton keresztül kifelé 
					 * haladva elhagyja a pályát.
					 */
					ArrayList<Color>cabinColors5 = new ArrayList<Color>(); /* Létrehozunk egy színlistát. Ebbõl fog felépülni a vonat. */
					cabinColors5.add(Color.RED); /* A színlistához hozzáadunk egy piros színt. */
					cabinColors5.add(Color.GREEN);/* A színlistához hozzáadunk egy zöld színt. */
					cabinColors5.add(Color.BLUE);/* A színlistához hozzáadunk egy kék színt. */
					
					GameController gc55 = new GameController();/* létrehozunk egy gameControllert */
					gc55.startNewGame(55);/* elindítjuk a játékot az 55-ös pályán*/
									
					gc55.skeletonTesterAddNewTrain(cabinColors5);/* hozzáadjuk az újonnan létrehozott vonatot */
					gc55.skeletonTesterMakeTrainsMove();/* Léptetjük egyet a vonatokat. */
					gc55.skeletonTesterMakeTrainsMove();/* Léptetjük egyet a vonatokat. */
					break;
					
					case 6: System.out.println("A vonat összeütközik egy másikkal...");
					/*Ez a teszteset szimulálja, hogy a vonat összeütközik egy másik vonattal. */
					ArrayList<Color>cabinColors6 = new ArrayList<Color>();/* Létrehozunk egy színlistát. Ebbõl fog felépülni a vonat. */
					cabinColors6.add(Color.RED);/* A színlistához hozzáadunk egy piros színt. */
					cabinColors6.add(Color.GREEN);/* A színlistához hozzáadunk egy zöld színt. */
					cabinColors6.add(Color.BLUE);/* A színlistához hozzáadunk egy kék színt. */
					
					GameController gc56 = new GameController();/* létrehozunk egy gameControllert */
					gc56.startNewGame(56);/* elindítjuk a játékot az 56-os pályán*/
									
					gc56.skeletonTesterAddNewTrain(cabinColors6);/* hozzáadunk egy vonatot a megadott színekkel */
					gc56.skeletonTesterMakeTrainsMove();/* léptetjük egyet */
					
					gc56.skeletonTesterAddNewTrain(cabinColors6); /* Hozzáadunk mégegy vonatot */
					gc56.skeletonTesterMakeTrainsMove(); /* léptetjük, melynek hatására belép a játéktérbe és ütközik az elõbb hozzáadott vonattal. */
					break;	
					
					/* Ha a felhasználó nem a várt kimenetet adja (vagyis nem 1, 2, 3,4 , 5, 6 számok valamelyikét
					 * akkor a tesztelõ program kilép a fõmenübe.
					 */
					default: System.out.println("A vonat csak a fent jelzett 6 dologra léphet, az elvárt bemenet 1, 2, 3, 4, 5 vagy 6");
				}
				break;
			
				
				/* --------------------------------------------------------------------------------- 6. MENÜPONT ----------------------------------------------------------------------------- */
			
				/* Ez a teszteset felel azért, ha egy új vonat érkezik a pályára.
				 * Elõször lekérdezi a felhasználótól a program, hogy hány vagon legyen a vonatban.
				 */
				case 6: System.out.println("Új vonat érkezik. Hány vagon legyen? [pozitív egész szám]");
				int vagonSzam = Integer.parseInt(scanner.nextLine());
				/* Megvizsgáljuk, hogy a felhasználó érvényes vagon számot írt-e be. */
				if(vagonSzam <= 0) {
					System.out.println("Csak pozitív egész szám lehet!");
					break;
				}
				
				System.out.println("A vonatnak " + vagonSzam + " vagonja lesz");
				
				/*
				 * Ezek a booleanok azért kellenek, mert ha már elkezdõdik a vonat vagonjainak felkonfigurálása,
				 * akkor ha a felhasználó nem létezõ színt írna be vagy arra a kérdésre, hogy a vagonban
				 * vannak-e érvénytelen választ adna, akkor a vagon érvénytelen paraméterekkel lenne inicializálva.
				 * Ezt elkerülendõ, két while ciklus addig nem engedi tovább a program futását, amíg a 
				 * felhasználó érvényes bemenetet nem ad az adott vagonra, biztosítva ezzel,
				 * hogy nem lesznek vagonok érvénytelen paraméterekkel felkonfigurálva.
				 */
				boolean ervenyesBemenetSzin = false;		
				/*Ebben tároljuk a vagonok színét amit a felhasználó ad meg*/
				ArrayList<Color> cabinColors = new ArrayList<Color>();
				/* Ez a ciklus végrehajtja a vagon inicializálást minden vagonra. */
				for(int i = 0; i < vagonSzam; i++)
				{
					ervenyesBemenetSzin = false;
					while(!ervenyesBemenetSzin)
					{
						System.out.println("Milyen színû legyen a " + (i+1) + ". vagon? [r, g, b]");
						switch (scanner.nextLine().charAt(0))
						{
							case 'R':
							case 'r': System.out.println("Piros a(z) " + (i+1) + ". vagon"); 
							/* Ha egy érvényes színt ad meg a felhasználó, akkor kilép ebbõl a while
							 * ciklusból Ellenkezõ esetben, megint azt kérdezi tõle a program, milyen színû legyen
							 * ez a vagon.
							 */
							ervenyesBemenetSzin = true;
							/* beállítjuk az ideiglenes tömbünkben, hogy a felhasználó azt mondta, legyen az i.-edik elem piros */
							cabinColors.add(Color.RED);
							break;
							
							case 'G':
							case 'g': System.out.println("Zöld a(z) " + (i+1) + ". vagon");
							ervenyesBemenetSzin = true;
							cabinColors.add(Color.GREEN);
							break;
							
							case 'B':
							case 'b': System.out.println("Kék a(z) " + (i+1) + ". vagon");
							ervenyesBemenetSzin = true;
							cabinColors.add(Color.BLUE);
							break;
							
							default: System.out.println("Csak az 'r', 'g' és 'b' a támogatott bemenet.");
						}
					}
				}
				
				GameController gc6 = new GameController(); /* létrehozunk egy gameControllert */
				gc6.startNewGame(1);/* elindítjuk a játékot az 1-es pályán*/
				gc6.skeletonTesterAddNewTrain(cabinColors);/* hozzáadjuk az újonnan létrehozott vonatot */
				
				break;
				
				
				/* --------------------------------------------------------------------------------- 7. MENÜPONT ----------------------------------------------------------------------------- */
				
				/* Ez a teszteset azt szimulálja, ha a játékos nyer.
				 * Ebben az esetben megkérdezi, melyik pályát nyerte meg.
				 */
				case 7: System.out.println("Gyõzelem. Melyik pályán volt? [1, 2]");
				switch (Integer.parseInt(scanner.nextLine())){
					case 1: System.out.println("Gyõzelem az elsõ pályán...");
					/* Ha a játékos az elsõ pályát nyeri meg akkor a játék a második pályát elkezdi. */
					GameController gc71 = new GameController(); /* létrehozunk egy gameControllert */
					gc71.startNewGame(1); /* elindítjuk a játékot az 1-es pályán */
					gc71.winEvent(); /* Meghívjuk a létrehozott gameController winEventjét. A játék a 2. pályán folytatódik.*/
					
					break;
					case 2: System.out.println("Gyõzelem a második pályán");
					GameController gc72 = new GameController(); /* létrehozunk egy gameControllert */
					gc72.startNewGame(2); /* elindítjuk a játékot a 2-es pályán*/
					gc72.winEvent(); /* Meghívjuk a létrehozott gameController winEventjét. További pályák hiányában a játék leáll.*/
					break;
					
					default: System.out.println("Csak az 1 és a 2 a megengedett bemenet.");
				}
				break;
				
				
				/* --------------------------------------------------------------------------------- 8. MENÜPONT ----------------------------------------------------------------------------- */
				
				/*Ha a felhsználó a nyolcas gombot nyomja meg, a a tesztprogram véget ér.*/
				case 8: run = false; break;
				
			/*Ha olyan tesztesetet indítana  a játékos ami nem is létezik, akkor a fõmenüben marad. */
			default: System.out.println("Nincs ilyen teszteset");
			}
		}
		scanner.close();
	}
}
