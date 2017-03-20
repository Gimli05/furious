package sandbox;

import java.io.IOException;
import java.util.Scanner;

/**
 * A szkeleton vezérléséért felelõs osztály, mely kezeli a felhasználóval való kommunikációt és az alapján vezérli a programot.
 */
public class Main {
	public static void main(String[] args) throws IOException{
		 System.out.println("Long /* NEM */ dolgozik");
		
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
			 * Az egyes billentyû lenyomásának hatására a szkeleton tesztelõ program futása megszakad.
			 */
			
			switch(Integer.parseInt(scanner.nextLine())){
			/* Elõször is intté kasztolom a bemenetet.*/
			
				case 1: System.out.println("1: Új játék indítása. Melyik pálya legyen? [1, 2]");
				/* Ez a teszteset új játékot indít. Bemenetként vár a felhasználótól egy számot, hogy melyik pálya
				 * induljon el. */
					switch(Integer.parseInt(scanner.nextLine())){
						case 1: System.out.println("1. pálya indítása...");	
						
						/* Ez a programrész akkor fut le, ha a felhasználó az elsõ pályát választja. Létrejön egy GameController. */
						GameController gc1 = new GameController();
						/* TODO Ebben még így nincs benne, hogy az 1. pálya indul. */
						
						break;
						case 2: System.out.println("2. pálya indítása...");
						
						/* Ez a programrész akkor fut le, ha a felhasználó a második pályát választja. Létrejön egy GameController. */
						GameController gc2 = new GameController();
						/* TODO Ebben még így nincs benne, hogy a 2. pálya indul. Nem jó, ezt GC-n keresztül kellene */
						
						break;
						/* Ez a programrész akkor fut le, ha a felhasználó nem 1-est vagy kettest adott meg bemenetként. 
						 * Ilyen pálya ugyanis nincs. Ekkor a program visszatér a fõmenübe. */
						default: System.out.println("Csak 1. és 2. pálya van. Az elvárt bemenet az '1' vagy a '2'");
					}
				break;
					
				/* Ez a teszteset alagútszáájak lérehozását teszi lehetõvé a felhasználó számára. 
				 * Elõször megkérdezi, hány alagútszáj van nyitva jelenleg a pályán. Egyszerre csak két  
				 * alagútszáj lehet nyitva a pályán, így a megengedett bemenetek a 0 és az 1.
				 */
				case 2: System.out.println("Alagútszály aktiválása. Hány alagútszály van jelenleg nyitva? [0, 1]");
				switch (Integer.parseInt(scanner.nextLine()))
				{
					case 0: System.out.println("Elsõ alagútszáj létrehozása...");
					
					/* Ez a kódrészlet fut le akkor, ha  a felhasználó futtatja az alagútszáj aktiválása tesztesetet
					 * úgy, hogy, a pályán a éppen nincs egyetlen alagútszáj se nyitott állapotban.
					 */
					TunnelEntrance te1 = new TunnelEntrance();
					te1.activate();
					/* TODO Ebbõl még így nem derül ki, hogy már volt-e valahol alagútszáj. Nem jó, ezt GC-n keresztül kellene
					 * Plusz bullshit ha kerül ide kód*/
					
					break;
					
					case 1: System.out.println("Második alagútszáj létrhozása...");

					/* Ez a kódrészlet fog lefutni abban az esetben, ha a pályán már található egy aktív
					 * alagútszály és mi még egy alagútszájat szeretnénk létrehozni mellé. Ekkor az alagútszájon
					 * kívül egy alagút is létrejön a két alagútszáj között.
					 */
					TunnelEntrance te2 = new TunnelEntrance();
					te2.activate();
					/* TODO Ebbõl még így nem derül ki, hogy már volt-e valahol alagútszáj. Nem jó, ezt GC-n keresztül kellene*/
					
					break;
					/* Amennyiben a felhasználó nem az 1-es vagy a 2-es gombot nyomta meg, egyik fenti teszteset
					 * sem fog lefutni. A szkeleton tesztelõ visszatér a fõmenübe.
					 */
					default: System.out.println("Nincs ilyen opció. Az elvárt bemenet az '1' vagy a '2'");
				}
				break;
				
				/* Ez a teszteset alagútszáj deaktiválására szolgál.
				 * Mivel egyszerre csak két alagútszáj lehet aktív a pályán, ezért  a megengedett bemenetek az 1 és a 2.
				 * Értelemszerûen kettõnél több aktív alagútszáj nem létezhet egy pályán egyszerre, ugyanis
				 * ez szembemenne a feladat specifikációjával. Ugyanitt, nem lehet 0 aktív alagútszáj, ha éppen deaktiválni
				 * szeretnénk egy alagútszájat, ebben az esetben ugyanis nem lenne mit deaktiválni.
				 */
				case 3: System.out.println("Alagótszály deaktiválása. Hány alagútszály van jelenleg nyitva? [1, 2]");
				switch (Integer.parseInt(scanner.nextLine()))
				{
					case 1: System.out.println("Egyedüli alagútszáj zárása..."); 
					/* Ez a programrész akkor fut le, amikor egy alagútszájat deaktiválunk úgy, hogy ez volt az egyetlen alagútszáj a pályán.
					 * Ebben az esetben csak egyszerûen deaktiválódik az alagútszáj.
					 */
					/*TODO */
					break;
					case 2: System.out.println("Egyik alagútszáj zárása...");
					/* Ez a programrész akkor fut le, ha a felhasználó úgy deaktivál alagútszájat, hogy a 
					 * pályán két alagútszáj volt aktív. Ebben az esetben az alagút a két alagútszáj között megszûnik, 
					 * és eltûnik az alagútszáj.
					 */
					/*TODO */
					break;
					/* Ha a felhasználó nem 1-et vagy 2-t adott meg bemenetként akkor a fenti 
					 * tesztesetek közül egyik se fut le. A program visszalép a fõmenübe.
					 */
					default: System.out.println("Csak akkor lehet alagútszájat zárni, ha 1 vagy 2 van nyitva");
				}
				break;
				
				/* 
				 * Ez a teszteset a pályán található váltók állítását szimulálja. 
				 * A váltóknak két állása van, egy alaphelyzet, amiben a rajta áthaladó
				 * vonat haladási iránya nem változik meg. A másik az alternatív helyzetben a 
				 * vonat irányt vált. 
				 */
				case 4: System.out.println("Váltó állítás. A váltó eredetileg módosította az irányt? [i/n]");
				
				switch (scanner.nextLine().charAt(0))
				{
					case 'I': 
					case 'i': System.out.println("Váltó állítása alaphelyzetbe..."); 
					
					/* Ez a kódrészlet akkor fut le ha a váltó alternatív helyzetben volt és mi alaphelyzetbe
					 * szeretnénk állítani.
					 */
					/*TODO */
					break;
					case 'N':
					case 'n': System.out.println("Váltó állítása alternatív helyzetbe...");
					
					/*Ez a kódrészlet akkor fut le ha a váltó alaphelyzetben volt
					 * és mi szeretnénk a másik állapotába helyezni.
					 */
					/*TODO */
					break;
					/* Ha a felhasználó nem i vagy n bemenetet adott, akkor a
					 * fenti tesztesetek közül egyik sem fut le. Ehelyett a program visszalép 
					 * a fõmenübe.
					 */
					default: System.out.println("Csak az 'i' és 'n' a támogatott bemenet");
				}
				break;
				
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
					/*TODO */
					break;
					case 2: System.out.println("A vonat váltóra lép. A váltó alaphelyzetben (i) vagy az elternatív helyzetben (n) van? [i, n]");
					switch (scanner.nextLine().charAt(0)){
						case 'I': 
						case 'i': System.out.println("A vonat egyenesen halad tovább..."); 
						/* Ez a programrész akkor fut le, ha a vonat egy váltóra lép és a váltó alapállapotban van
						 * 
						 */
						/*TODO */
						break;
						case 'N':
						case 'n': System.out.println("A vonat elkanyarodik...");
						/* Ez a programrész akkor fut le, ha a vonat egy váltóra lép és a váltó alternatív   van */
						/*TODO */
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
							/*TODO */
							break;
							case 2: System.out.println("A vonat belép az alagútba...");
							/* Ez a teszteset akkor fut le, ha a vonat alagútba lép és van is alagút, vagyis a 
							 * pályán két aktív alagútszáj található.
							 */
							/*TODO */
							break;
							default: System.out.println("Csak akkor léphet a vonat alagútba, ha, ha 1 vagy 2 nyitott alagútszáj van.");
						}
						break;
						case 'N':
						case 'n': System.out.println("A vonat elkerüli az alagutat...");
						/* Ha az alagútszájnál álló váltó alaphelyzetben van akkor a vonat nem megy be az alagútba*/
						/*TODO */
						break;
						default: System.out.println("Csak az 'i' és 'n' a támogatott bemenet");
					}
					break;
					case 4: System.out.println("A vonat megállóra lép. Van nem üres kabin a vonatban? [i, n]");
					/* Ha a vonat megállóra lép, az elsõ kérdés, hogy utaznak-e a vonatban. Ha nem, akkor a vonat
					 * egyszerûen áthalad a megállón. Ha van olyan kabin ami nem üres akkor ha az elsõ 
					 * nem üres vagon színe megegyezik a megállóéval, leszállnak az utasok arról a vagonról.
					 */
						switch (scanner.nextLine().charAt(0))
						{
							case 'I':
							case 'i': System.out.println("Milyen színû a megálló? Piros, zöld vagy kék lehet. [r, g, b]"); 
							/* Ebben tároljuk a megálló színét amit a felhasználó megad. */
							char megalloSzin = scanner.nextLine().charAt(0);	
							
							System.out.println("Milyen színû az elsõ nem üres vagon? Piros, zöld vagy kék lehet. [r, g, b]"); 
							/* Ebben tároljuk a vagon színét amit a felhasználó megad. */
							char vagonSzin = scanner.nextLine().charAt(0);	
							
							/*Ez az if csak azért van itt, hogy letesztelje, a megadott szín érvényes-e. Ha nem az, megszakad a teszteset.*/
							if ((megalloSzin != 'r' && megalloSzin != 'g' && megalloSzin != 'b' 
									&& megalloSzin != 'R' && megalloSzin != 'G' && megalloSzin != 'B') || 
									(vagonSzin != 'r' && vagonSzin != 'g' && vagonSzin != 'b' &&
									vagonSzin != 'R' && vagonSzin != 'G' && vagonSzin != 'B'))
							{
								System.out.println("A vagonok és a megállók csak 'r', 'g' vagy 'b' színûek lehetnek");
								break;
							}
							if(megalloSzin == vagonSzin)
							{
								System.out.println("Leszállnak az utasok...");
								/* Ha az elsõ nem üres vagon színe megegyezik a megálló színével, leszállnak az utasok. */
								/* TODO */
							}else
							{
								System.out.println("Nem szállnak le az utasok...");
								/* Ha az elsõ nem üres vagon színe nem egyezik meg a megálló színével, nem szállnak le az utasok. */
								/* TODO*/
							}
						
						break;
						case 'n': System.out.println("A vonat áthalad a megállón és semmi más nem történik...");
						/* Ez a programrész akkor fut le, ha senki nem utazott a vonaton mikor az a megállóra lépett. 
						 * Ekkor ugyanis nincs kinek leszállnia a vonatról. */
						/*TODO */
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
					/*TODO */
					break;
					case 6: System.out.println("A vonat összeütközik egy másikkal...");
					/*Ez a teszteset szimulálja, hogy a vonat összeütközik egy másik vonattal. */
					/*TODO */
					break;	
					/* Ha a felhasználó nem a várt kimenetet adja (vagyis nem 1, 2, 3,4 , 5, 6 számok valamelyikét
					 * akkor a tesztelõ program kilép a fõmenübe.
					 */
					default: System.out.println("A vonat csak a fent jelzett 6 dologra léphet, az elvárt bemenet 1, 2, 3, 4, 5 vagy 6");
			}
			break;
			
				/* Ez a teszteset felel azért, ha egy új vonat érkezik a pályára.
				 * Elõször lekérdezi a felhasználótól a program, hogy hány vagon legyen a vonatban.
				 */
				case 6: System.out.println("Új vonat érkezik. Hány vagon legyen? [pozitív egész szám]");
				int vagonSzam = Integer.parseInt(scanner.nextLine());
				/* Megvizsgáljuk, hogy a felhasználó érvényes vagon számot írt-e be. */
				if(vagonSzam <= 0) break;
				/* TODO létrehozni egy vonatot a collectionben adott vagonszámmal */
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
				boolean ervenyesBemenetVagonteli = false;
				/* Ez a ciklus végrehajtja a vagon inicializálást minden vagonra. */
				for(int i = 0; i < vagonSzam; i++)
				{
					ervenyesBemenetSzin = false;
					ervenyesBemenetVagonteli = false;
					while(!ervenyesBemenetSzin)
					{
						System.out.println("Milyen színû legyen a " + i + ". vagon? [r, g, b]");
						switch (scanner.nextLine().charAt(0))
						{
							case 'R':
							case 'r': System.out.println("Piros a(z) " + i + ". vagon"); 
							/* Ha egy érvényes színt ad meg a felhasználó, akkor kilép ebbõl a while
							 * ciklusból Ellenkezõ esetben, megint azt kérdezi tõle a program, milyen színû legyen
							 * ez a vagon.
							 */
							ervenyesBemenetSzin = true;
							/*TODO */
							break;
							case 'G':
							case 'g': System.out.println("Zöld a(z) " + i + ". vagon");
							ervenyesBemenetSzin = true;
							/*TODO */
							break;
							case 'B':
							case 'b': System.out.println("Kék a(z) " + i + ". vagon");
							ervenyesBemenetSzin = true;
							/*TODO */
							break;
							default: System.out.println("Csak az 'r', 'g' és 'b' a támogatott bemenet.");
						}
						
					}
					while(!ervenyesBemenetVagonteli)
					{
						System.out.println("Vannak ebben a vagonban utasok? [i, n]");
						switch (scanner.nextLine().charAt(0)){
							case 'I': 
							case 'i': System.out.println("Ebben a vagonban lesznek utasok.");
							ervenyesBemenetVagonteli = true;
							/*TODO */
							break;
							case 'N':
							case 'n': System.out.println("Ebben a vagonban nem lesznek utasok.");
							ervenyesBemenetVagonteli = true;
							/*TODO */
							break;
							default: System.out.println("Csak az 'i' és kis 'n' a támogatott bemenet");
						}
					}
					
					/* TODO beállítani az adott vagon színét */
				}
				break;
				
				/* Ez a teszteset azt szimulálja, ha a játékos nyer.
				 * Ebben az esetben megkérdezi, melyik pályát nyerte meg.
				 */
				case 7: System.out.println("Gyõzelem. Melyik pályán volt? [1, 2]");
				switch (scanner.nextLine().charAt(0)){
					case 1: System.out.println("Gyõzelem az elsõ pályán...");
					/* Ha a játékos az elsõ pályát nyeri meg akkor a játék a második pályát elkezdi. */
					/*TODO*/
					break;
					case 2: System.out.println("Gyõzelem a második pályán");
					/* Ha a játékos a második pályán nyer, a játék véget ér*/
					/*TODO */
					break;
					default: System.out.println("Csak az 1 és a 2 a megengedett bemenet.");
				}
				/*Ha a felhsználó a nyolcas gombot nyomja meg, a a tesztprogram véget ér.*/
				case 8: run = false; break;
			/*Ha olyan tesztesetet indítana  a játékos ami nem is létezik, akkor a fõmenüben marad. */
			default: System.out.println("Nincs ilyen teszteset");
			}
		}
		scanner.close();
	}
}
