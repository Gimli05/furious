package sandbox;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * A szkeleton vez�rl�s��rt felel�s oszt�ly, mely kezeli a felhaszn�l�val val� kommunik�ci�t �s az alapj�n vez�rli a programot.
 * A szkeleton tesztel�s�re 2 teljes �s 6 tov�bbi egyszer�s�tett teszt p�ly�t hoztunk l�tre. A hat r�vid p�lya teljes j�t�k ind�t�s�ra nem alkalmas.
 */
public class Main {
	public static void main(String[] args) throws IOException{
		 Scanner scanner = new Scanner(System.in);
		 /* Ezzel t�rt�nik majd a konzolon felhaszn�l� �ltal bemeneten megadott karakter beolvas�sa */
		 
		 boolean run = true;
		 /* Ez a boolean v�ltoz� felel az�rt, hogy a f�ciklus ami a teszteket tartalmazza folyamatosan fusson eg�szen */
		 /* addig am�g az �rt�k�t �t nem �ll�tj�k false-ba */
		 
		 /* A skeleton tesztel� f�ciklusa. Folyamatosan fut, am�g a run �rt�k�t �t nem �rj�k false-ba. Ezt a kil�p�sre
		  * szolg�l� billenty� lenyom�s�val lehet parancsba adni.
		  */
		while(run){
			
			/* A program el�sz�r felk�n�lja a felhaszn�l�nak a teszteseteket amiket
			 * az al�bb felt�ntetett billenyt�k lenyom�s�val lehet el�rni. 
			 * Az al�bbi tesztesetek a konzolra �r�dnak ki de a dokument�ci�ban is megtal�lhat� a r�szletes le�r�suk
			 * A felhaszn�l� a bemenetet �gyszint�n  a konzolon kereszt�l viheti be.
			 * Ezek ut�n a program �ltal adott v�lasz, vagyis a konkr�t teszteset lefut�s�nak
			 * az eredm�nye a konzolon jelenik meg.
			 */
			
			System.out.println("\n V�lassz az al�bbi lehet�s�gek k�z�l:"
					+ "\n 1: �j j�t�k ind�t�sa"
					+ "\n 2: Alag�tsz�j aktiv�l�sa"
					+ "\n 3: Alag�tsz�j deaktiv�l�sa"
					+ "\n 4: V�lt� �ll�t�sa"
					+ "\n 5: Vonatok l�ptet�se"
					+ "\n 6: �j vonat �rkez�se"
					+ "\n 7: Gy�zelem"
					+ "\n 8: kil�p�s");
			
			/* Fentebb olvashat�ak a tesztesetek. 
			 * Az egyes billenty� lenyom�s�nak hat�s�ra a szkeleton tesztel� program elind�tja az "�j j�t�k ind�t�sa" tesztesetet. 
			 * A kettes billenty� lenyom�s�nak hat�s�ra a szkeleton tesztel� program elind�tja az "Alag�tsz�j aktiv�l�sa" tesztesetet. 
			 * A h�rmas billenty� lenyom�s�nak hat�s�ra a szkeleton tesztel� program elind�tja az "Alag�tsz�j deaktiv�l�sa" tesztesetet. 
			 * A n�gyes billenty� lenyom�s�nak hat�s�ra a szkeleton tesztel� program elind�tja a "V�lt� �ll�t�sa" tesztesetet. 
			 * Az �t�s billenty� lenyom�s�nak hat�s�ra a szkeleton tesztel� program elind�tja a "Vonatok l�ptet�se" tesztesetet. 
			 * A hatos billenty� lenyom�s�nak hat�s�ra a szkeleton tesztel� program elind�tja az "�j vonat �rkez�se" tesztesetet. 
			 * A hetes billenty� lenyom�s�nak hat�s�ra a szkeleton tesztel� program elind�tja az "Gy�zelem" tesztesetet. 
			 * A nyolcas billenty� lenyom�s�nak hat�s�ra a szkeleton tesztel� program fut�sa megszakad.
			 */
			
			switch(Integer.parseInt(scanner.nextLine())){
			/* El�sz�r is intt� kasztolom a bemenetet.*/
			
				/* --------------------------------------------------------------------------------- 1. MEN�PONT ----------------------------------------------------------------------------- */
			
				case 1: System.out.println("1: �j j�t�k ind�t�sa. Melyik p�lya legyen? [1, 2]");
				/* Ez a teszteset �j j�t�kot ind�t. Bemenetk�nt v�r a felhaszn�l�t�l egy sz�mot, hogy melyik p�lya
				 * induljon el. */
					switch(Integer.parseInt(scanner.nextLine())){
						case 1: System.out.println("\n1. p�lya ind�t�sa...");	
						/* Ez a programr�sz akkor fut le, ha a felhaszn�l� az els� p�ly�t v�lasztja. L�trej�n egy GameController. */
						/* a gc ut�ni sz�m azt mutatja, melyik feladat melyik r�szfeladat�hoz tartozik. */
						GameController gc11 = new GameController();
						gc11.startNewGame(1); /* elind�tjuk a gameControllert az 1-es p�ly�val-  */
						break;
						
						case 2: System.out.println("\n2. p�lya ind�t�sa...");
						/* Ez a programr�sz akkor fut le, ha a felhaszn�l� a m�sodik p�ly�t v�lasztja. L�trej�n egy GameController. */
						GameController gc12 = new GameController();
						gc12.startNewGame(2);  /* elind�tjuk a gameControllert az 2-es p�ly�val-  */
						break;
/**LONG TESZTEL--------------------------*/
						case 69: System.out.println("\nXRail Teszt...");
						/* Ez a programr�sz akkor fut le, ha a felhaszn�l� a 69. p�ly�t v�lasztja. L�trej�n egy GameController. */
						GameController gc169 = new GameController();
						gc169.startNewGame(69);  /* elind�tjuk a gameControllert az 69-es p�ly�val-  */
						break;
/**LONG TESZTEL--------------------------*/						
						/* Ez a programr�sz akkor fut le, ha a felhaszn�l� nem 1-est vagy kettest adott meg bemenetk�nt. 
						 * Ilyen p�lya ugyanis nincs. Ekkor a program visszat�r a f�men�be. */
						default: System.out.println("Csak 1. �s 2. p�lya van. Az elv�rt bemenet az '1' vagy a '2'");
					}
				break;
					
				
				/* --------------------------------------------------------------------------------- 2. MEN�PONT ----------------------------------------------------------------------------- */
				
				/* Ez a teszteset alag�tsz��jak l�rehoz�s�t teszi lehet�v� a felhaszn�l� sz�m�ra. 
				 * El�sz�r megk�rdezi, h�ny alag�tsz�j van nyitva jelenleg a p�ly�n. Egyszerre csak k�t  
				 * alag�tsz�j lehet nyitva a p�ly�n, �gy a megengedett bemenetek a 0 �s az 1.
				 */
				case 2: System.out.println("Alag�tsz�ly aktiv�l�sa. H�ny alag�tsz�ly van jelenleg nyitva? [0, 1]");
				switch (Integer.parseInt(scanner.nextLine()))
				{
					case 0: System.out.println("Els� alag�tsz�j aktiv�l�sa...");
					/* Ez a k�dr�szlet fut le akkor, ha  a felhaszn�l� futtatja az alag�tsz�j aktiv�l�sa tesztesetet
					 * �gy, hogy, a p�ly�n a �ppen nincs egyetlen alag�tsz�j se nyitott �llapotban.
					 */
					GameController gc21 = new GameController();
					gc21.startNewGame(1);  /* elind�tjuk a gameControllert az 1-es p�ly�val-  */
					gc21.skeletonTesterActivateTunnelEntrance(1);/* Aktiv�ljuk az els� alag�tsz�jat. */
					break;
					
					case 1: System.out.println("Els� �s m�sodik alag�tsz�j aktiv�l�sa...");
					/* Ez a k�dr�szlet fog lefutni abban az esetben, ha a p�ly�n m�r tal�lhat� egy akt�v
					 * alag�tsz�ly �s mi m�g egy alag�tsz�jat szeretn�nk l�trehozni mell�. Ekkor az alag�tsz�jon
					 * k�v�l egy alag�t is l�trej�n a k�t alag�tsz�j k�z�tt.
					 */
					GameController gc22 = new GameController();
					gc22.startNewGame(1);  /* elind�tjuk a gameControllert az 1-es p�ly�val-  */
					gc22.skeletonTesterActivateTunnelEntrance(1);/* Aktiv�ljuk az els� alag�tsz�jat. */				
					break;
					
					/* Amennyiben a felhaszn�l� nem az 1-es vagy a 2-es gombot nyomta meg, egyik fenti teszteset
					 * sem fog lefutni. A szkeleton tesztel� visszat�r a f�men�be.
					 */
					default: System.out.println("Nincs ilyen opci�. Az elv�rt bemenet az '1' vagy a '2'");
				}
				break;
				
				
				/* --------------------------------------------------------------------------------- 3. MEN�PONT ----------------------------------------------------------------------------- */
				
				/* Ez a teszteset alag�tsz�j deaktiv�l�s�ra szolg�l.
				 * Mivel egyszerre csak k�t alag�tsz�j lehet akt�v a p�ly�n, ez�rt  a megengedett bemenetek az 1 �s a 2.
				 * �rtelemszer�en kett�n�l t�bb akt�v alag�tsz�j nem l�tezhet egy p�ly�n egyszerre, ugyanis
				 * ez szembemenne a feladat specifik�ci�j�val. Ugyanitt, nem lehet 0 akt�v alag�tsz�j, ha �ppen deaktiv�lni
				 * szeretn�nk egy alag�tsz�jat, ebben az esetben ugyanis nem lenne mit deaktiv�lni.
				 */
				case 3: System.out.println("Alag�tsz�ly deaktiv�l�sa. H�ny alag�tsz�j van jelenleg nyitva? [1, 2]");
				switch (Integer.parseInt(scanner.nextLine()))
				{
					case 1: 	
					/* Ez a programr�sz akkor fut le, amikor egy alag�tsz�jat deaktiv�lunk �gy, hogy ez volt az egyetlen alag�tsz�j a p�ly�n.
					 * Ebben az esetben csak egyszer�en deaktiv�l�dik az alag�tsz�j.
					 */
						GameController gc31 = new GameController();
						gc31.startNewGame(1);  /* elind�tjuk a gameControllert az 1-es p�ly�val-  */
						gc31.skeletonTesterActivateTunnelEntrance(1);/* Aktiv�ljuk az els� alag�tsz�jat. */
						
						System.out.println("\nEgyed�li alag�tsz�j z�r�sa..."); 
						gc31.skeletonTesterDeActivateATunnelEntrance(); /* deaktiv�ljuk az alag�tsz�jat */
					break;
					
					case 2: 
					/* Ez a programr�sz akkor fut le, ha a felhaszn�l� �gy deaktiv�l alag�tsz�jat, hogy a 
					 * p�ly�n k�t alag�tsz�j volt akt�v. Ebben az esetben az alag�t a k�t alag�tsz�j k�z�tt megsz�nik, 
					 * �s elt�nik az alag�tsz�j.
					 */
						GameController gc32 = new GameController();
						gc32.startNewGame(1);  /* elind�tjuk a gameControllert az 1-es p�ly�val-  */
						gc32.skeletonTesterActivateTunnelEntrance(2);/* Aktiv�lunk k�t alag�tsz�jat. */
						
						System.out.println("\nAlag�t bont�sa �s az egyik alag�tsz�j z�r�sa...");
						gc32.skeletonTesterDeActivateATunnelEntrance(); /* Deaktiv�ljuk az egyik alag�tsz�jat mely sor�n megsz�nik az alag�t. */
					break;
					
					/* Ha a felhaszn�l� nem 1-et vagy 2-t adott meg bemenetk�nt akkor a fenti 
					 * tesztesetek k�z�l egyik se fut le. A program visszal�p a f�men�be.
					 */
					default: System.out.println("Csak akkor lehet alag�tsz�jat z�rni, ha 1 vagy 2 van nyitva");
				}
				break;
				
				
				/* --------------------------------------------------------------------------------- 4. MEN�PONT ----------------------------------------------------------------------------- */
				
				/* 
				 * Ez a teszteset a p�ly�n tal�lhat� v�lt�k �ll�t�s�t szimul�lja. 
				 * A v�lt�knak k�t �ll�sa van, egy alaphelyzet, amiben a rajta �thalad�
				 * vonat halad�si ir�nya nem v�ltozik meg. A m�sik az alternat�v helyzetben a 
				 * vonat ir�nyt v�lt. 
				 * A tesztesethez haszn�lt map-en csak egy v�lt� van. (map3.txt)
				 */
				case 4: System.out.println("V�lt� �ll�t�s. A v�lt� eredetileg m�dos�totta az ir�nyt? [i/n]");
				
				switch (scanner.nextLine().charAt(0))
				{
					case 'I': 
					case 'i': 
					/* Ez a k�dr�szlet akkor fut le ha a v�lt� alternat�v helyzetben volt �s mi alaphelyzetbe
					 * szeretn�nk �ll�tani. Ez majd GUI-n eventekkel t�rt�nik majd a j�v�ben. Mivel a v�lt� alaphelyzetb�l indul,
					 * ehhez el�bb ki kell billenteni az alternat�v helyzetbe, majd onnan vissza.
					 */
					GameController gc41 = new GameController();
					gc41.startNewGame(1);  /* elind�tjuk a gameControllert az 3-as p�ly�val  */
					gc41.skeletonTesterSwitchASwitch(); /* Be�ll�tjuk a v�lt�t, hogy a feladat ki�r�snak megfelel� alaphelyzetben �lljon. */
					
					System.out.println("V�lt� �ll�t�sa alaphelyzetbe..."); 
					gc41.skeletonTesterSwitchASwitch(); /* �ll�tunk egyet a v�lt�n. */
					break;
					
					case 'N':
					case 'n': 
					/*Ez a k�dr�szlet akkor fut le ha a v�lt� alaphelyzetben volt
					 * �s mi szeretn�nk a m�sik �llapot�ba helyezni. Ez majd GUI-n eventekkel t�rt�nik majd a j�v�ben.
					 */
					GameController gc42 = new GameController();
					gc42.startNewGame(1);  /* elind�tjuk a gameControllert az 3-as p�ly�val  */
					
					
					System.out.println("V�lt� �ll�t�sa alternat�v helyzetbe..."); /*A v�lt� alapb�l alap helyzetben van, �gy csak egy v�lt�s sz�ks�ges rajta */
					gc42.skeletonTesterSwitchASwitch(); /* �ll�tunk egyet a v�lt�n. */
					break;
					
					/* Ha a felhaszn�l� nem i vagy n bemenetet adott, akkor a
					 * fenti tesztesetek k�z�l egyik sem fut le. Ehelyett a program visszal�p 
					 * a f�men�be.
					 */
					default: System.out.println("Csak az 'i' �s 'n' a t�mogatott bemenet");
				}
				break;
				
				
				/* --------------------------------------------------------------------------------- 5. MEN�PONT ----------------------------------------------------------------------------- */
				
				/*Ez a teszteset a vonat l�ptet�s�t szimul�lja
				 * A vonat 5 k�l�nb�z� s�nre l�phet. Ezek a k�vetkez�k:
				 * Az egyes billenty� lenyom�s�nak hat�s�ra a l�ptet�st szimul�l� teszteset s�nre l�pteti a vonatot. 
				 * A kettes billenty� lenyom�s�nak hat�s�ra a l�ptet�st szimul�l� teszteset v�lt�ra l�pteti a vonatot. 
				 * A v�lt� egy sepci�lis s�n
				 * A h�rmas billenty� lenyom�s�nak hat�s�ra a l�ptet�st szimul�l� teszteset alag�tsz�jra l�pteti a vonatot. 
				 * Itt fontos megjegyezni, hogy az alag�tsz�j is egyfajta v�lt�, aminek az alternat�v �ll�sa vezet
				 * az alag�tba. Alap�ll�s�ban a vonat elker�li az alagutat.
				 * A n�gyes billenty� lenyom�s�nak hat�s�ra a l�ptet�st szimul�l� teszteset meg�ll�ra l�pteti a vonatot. 
				 * A meg�ll� is egyfajta speci�lis s�n ami sz�nnel rendelkezik
				 * Az �t�s billenty� lenyom�s�nak hat�s�ra a l�ptet�st szimul�l� teszteset bel�p�si pontra l�pteti a vonatot.
				 * A bel�p�si pont egyfajta speci�lis s�n, amin kereszt�l bejutnak a vonatok a p�ly�ra. Ez a teszteset viszont
				 * azt szimul�lja, hogy a vonat ezen  kereszt�l elhagyja a p�ly�t.
				 * Az �t�s billenyt� egy speci�lis teszteset, ugyanis ez nem s�nhez k�thet�, hanem azt szimul�lja
				 * hogy k�t vonat �ssze�tk�zik. 
				 */
				case 5: System.out.println("Vonatok l�ptet�se. Hova l�p a vonat?"
						+ "\n 1: s�nre"
						+ "\n 2: v�lt�ra"
						+ "\n 3: alag�tsz�jra"
						+ "\n 4: meg�ll�ra"
						+ "\n 5: Vonat elhagyja a j�t�kteret bel�p�si ponton kereszt�l"
						+ "\n 6: K�t vonat �tk�zik");
				switch (Integer.parseInt(scanner.nextLine()))
				{
					case 1: System.out.println("A vonat s�nre l�p..."); 
					/*Ez a programr�sz fut le akkor, ha a vonat egy egyszer� s�nre l�p. */
					
						ArrayList<Color>cabinColors = new ArrayList<Color>(); /* L�trehozunk egy sz�nlist�t. Ebb�l fog fel�p�lni a vonat. */
						cabinColors.add(Color.RED); /* A sz�nlist�hoz hozz�adunk egy piros sz�nt. */
						cabinColors.add(Color.GREEN); /* A sz�nlist�hoz hozz�adunk egy z�ld sz�nt. */
						cabinColors.add(Color.BLUE); /* A sz�nlist�hoz hozz�adunk egy k�k sz�nt. */
						
						GameController gc51 = new GameController(); /* l�trehozunk egy gameControllert */
						gc51.startNewGame(51); /* elind�tjuk a j�t�kot az 51-es p�ly�n*/
						gc51.skeletonTesterAddNewTrain(cabinColors); /* hozz�adjuk az �jonnan l�trehozott vonatot */
						gc51.skeletonTesterMakeTrainsMove(); /* L�ptetj�k egyet a vonatokat. */
						gc51.skeletonTesterMakeTrainsMove(); /* L�ptetj�k egyet a vonatokat. */
					break;
					
					case 2: System.out.println("A vonat v�lt�ra l�p. A v�lt� alaphelyzetben (i) vagy az elternat�v helyzetben (n) van? [i, n]");
						switch (scanner.nextLine().charAt(0)){
							case 'I': 
							case 'i': System.out.println("A vonat egyenesen halad tov�bb..."); 
							/* Ez a programr�sz akkor fut le, ha a vonat egy v�lt�ra l�p �s a v�lt� alap�llapotban van */
							ArrayList<Color>cabinColors21 = new ArrayList<Color>(); /* L�trehozunk egy sz�nlist�t. Ebb�l fog fel�p�lni a vonat. */
							cabinColors21.add(Color.RED);/* A sz�nlist�hoz hozz�adunk egy piros sz�nt. */
							cabinColors21.add(Color.GREEN);/* A sz�nlist�hoz hozz�adunk egy z�ld sz�nt. */
							cabinColors21.add(Color.BLUE);/* A sz�nlist�hoz hozz�adunk egy k�k sz�nt. */
							
							GameController gc521 = new GameController(); /* l�trehozunk egy gameControllert */
							gc521.startNewGame(52); /* elind�tjuk a j�t�kot az 52-es p�ly�n*/
							gc521.skeletonTesterAddNewTrain(cabinColors21); /* hozz�adjuk az �jonnan l�trehozott vonatot */
							gc521.skeletonTesterMakeTrainsMove(); /* L�ptetj�k egyet a vonatokat. */
							gc521.skeletonTesterMakeTrainsMove();/* L�ptetj�k egyet a vonatokat. */
							break;
							
							case 'N':
							case 'n': System.out.println("A vonat elkanyarodik...");
							/* Ez a programr�sz akkor fut le, ha a vonat egy v�lt�ra l�p �s a v�lt� alternat�v   van */
							ArrayList<Color>cabinColors22 = new ArrayList<Color>();/* L�trehozunk egy sz�nlist�t. Ebb�l fog fel�p�lni a vonat. */
							cabinColors22.add(Color.RED);/* A sz�nlist�hoz hozz�adunk egy piros sz�nt. */
							cabinColors22.add(Color.GREEN); /* A sz�nlist�hoz hozz�adunk egy z�ld sz�nt. */
							cabinColors22.add(Color.BLUE); /* A sz�nlist�hoz hozz�adunk egy k�k sz�nt. */
							
							GameController gc522 = new GameController(); /* l�trehozunk egy gameControllert */
							gc522.startNewGame(52); /* elind�tjuk a j�t�kot az 52-es p�ly�n*/
							gc522.skeletonTesterSwitchASwitch();  /* �ll�tunk egyet a v�lt�n. */
							gc522.skeletonTesterAddNewTrain(cabinColors22); /* hozz�adjuk az �jonnan l�trehozott vonatot */
							gc522.skeletonTesterMakeTrainsMove();/* L�ptetj�k egyet a vonatokat. */
							gc522.skeletonTesterMakeTrainsMove();/* L�ptetj�k egyet a vonatokat. */
							break;
							
							default: System.out.println("Csak az 'i' �s 'n' a t�mogatott bemenet");
						}
					break;
					
					
					case 3: System.out.println("A vonat alag�tsz�jra l�p. A v�lt� az alag�tsz�j fel� ir�ny�tja a vonatot? [i, n]");
					switch (scanner.nextLine().charAt(0))
					{
						case 'I':
						case 'i': System.out.println("H�ny akt�v alag�tsz�j van a p�ly�n? [1, 2]"); 
							switch (Integer.parseInt(scanner.nextLine()))
							{
							case 1: System.out.println("A vonat bel�p a zs�kutca alag�tba..."); 
							/* Ez a teszteset akkor fut le, ha a vonat alag�tba l�pne, de nincs alag�t, mert csak egy akt�v alag�tsz�j 
							 * tal�lhat� jelenleg a p�ly�n.
							 */
							
							ArrayList<Color>cabinColors31 = new ArrayList<Color>();/* L�trehozunk egy sz�nlist�t. Ebb�l fog fel�p�lni a vonat. */
							cabinColors31.add(Color.RED); /* A sz�nlist�hoz hozz�adunk egy piros sz�nt. */
							cabinColors31.add(Color.GREEN); /* A sz�nlist�hoz hozz�adunk egy z�ld sz�nt. */
							cabinColors31.add(Color.BLUE); /* A sz�nlist�hoz hozz�adunk egy k�k sz�nt. */
							
							GameController gc531 = new GameController(); /* l�trehozunk egy gameControllert */
							gc531.startNewGame(53); /* elind�tjuk a j�t�kot az 53-as p�ly�n*/
							gc531.skeletonTesterActivateTunnelEntrance(1); /* aktiv�lunk egy alag�tsz�jat */
							gc531.skeletonTesterSwitchATunnelEntrance(); /* v�ltunk egyet az alag�tsz�jon l�v� v�lt�n. */ 
							
							gc531.skeletonTesterAddNewTrain(cabinColors31); /* hozz�adjuk az �jonnan l�trehozott vonatot */
							gc531.skeletonTesterMakeTrainsMove(); /* L�ptetj�k egyet a vonatokat. */
							gc531.skeletonTesterMakeTrainsMove();/* L�ptetj�k egyet a vonatokat. */
							break;
							
							case 2: System.out.println("A vonat bel�p az alag�tba...");
							/* Ez a teszteset akkor fut le, ha a vonat alag�tba l�p �s van is alag�t, vagyis a 
							 * p�ly�n k�t akt�v alag�tsz�j tal�lhat�.
							 */
							ArrayList<Color>cabinColors32 = new ArrayList<Color>();/* L�trehozunk egy sz�nlist�t. Ebb�l fog fel�p�lni a vonat. */
							cabinColors32.add(Color.RED); /* A sz�nlist�hoz hozz�adunk egy piros sz�nt. */
							cabinColors32.add(Color.GREEN);/* A sz�nlist�hoz hozz�adunk egy z�ld sz�nt. */
							cabinColors32.add(Color.BLUE);/* A sz�nlist�hoz hozz�adunk egy k�k sz�nt. */
							
							GameController gc532 = new GameController();/* l�trehozunk egy gameControllert */
							gc532.startNewGame(53);/* elind�tjuk a j�t�kot az 53-as p�ly�n*/
							gc532.skeletonTesterActivateTunnelEntrance(2);/* K�t alag�tsz�jat aktiv�lunk */
							gc532.skeletonTesterSwitchATunnelEntrance(); /* az egyiken v�ltunk egyet. */
							
							gc532.skeletonTesterAddNewTrain(cabinColors32); /* hozz�adjuk az �jonnan l�trehozott vonatot */
							gc532.skeletonTesterMakeTrainsMove();/* L�ptetj�k egyet a vonatokat. */
							gc532.skeletonTesterMakeTrainsMove();/* L�ptetj�k egyet a vonatokat. */
							break;
							
							default: System.out.println("Csak akkor l�phet a vonat alag�tba, ha, ha 1 vagy 2 nyitott alag�tsz�j van.");
						}
						break;
						case 'N':
						case 'n': System.out.println("A vonat elker�li az alagutat...");
						/* Ha az alag�tsz�jn�l �ll� v�lt� alaphelyzetben van akkor a vonat nem megy be az alag�tba*/
						ArrayList<Color>cabinColors33 = new ArrayList<Color>();/* L�trehozunk egy sz�nlist�t. Ebb�l fog fel�p�lni a vonat. */
						cabinColors33.add(Color.RED);/* A sz�nlist�hoz hozz�adunk egy piros sz�nt. */
						cabinColors33.add(Color.GREEN);/* A sz�nlist�hoz hozz�adunk egy z�ld sz�nt. */
						cabinColors33.add(Color.BLUE);/* A sz�nlist�hoz hozz�adunk egy k�k sz�nt. */
						
						GameController gc533 = new GameController();/* l�trehozunk egy gameControllert */
						gc533.startNewGame(53);/* elind�tjuk a j�t�kot az 53-as p�ly�n*/
						
						gc533.skeletonTesterAddNewTrain(cabinColors33);/* hozz�adjuk az �jonnan l�trehozott vonatot */
						gc533.skeletonTesterMakeTrainsMove();/* L�ptetj�k egyet a vonatokat. */
						gc533.skeletonTesterMakeTrainsMove();/* L�ptetj�k egyet a vonatokat. */
						break;
						
						default: System.out.println("Csak az 'i' �s 'n' a t�mogatott bemenet");
					}
					break;
					case 4: System.out.println("A vonat Z�LD meg�ll�ra l�p. Van nem �res kabin a vonatban? [i, n]");
					/* Ha a vonat meg�ll�ra l�p, az els� k�rd�s, hogy utaznak-e a vonatban. Ha nem, akkor a vonat
					 * egyszer�en �thalad a meg�ll�n. Ha van olyan kabin ami nem �res akkor ha az els� 
					 * nem �res vagon sz�ne megegyezik a meg�ll��val, lesz�llnak az utasok arr�l a vagonr�l.
					 */
						switch (scanner.nextLine().charAt(0))
						{
							case 'I':
							case 'i': System.out.println("Milyen sz�n� az els� nem �res vagon? Piros, z�ld vagy k�k lehet. [r, g, b]"); 
							//* Ebben t�roljuk a vagon sz�n�t amit a felhaszn�l� megad. */
							char vagonSzin = scanner.nextLine().charAt(0);		
							Color cabColor = null; /* Ilyen sz�n� lesz majd a vagon. */
														
							if(vagonSzin == 'r' || vagonSzin == 'R'){ /* ellen�rizz�k a sz�neket. Ha piros, akkor nem fognak lesz�llni utasok, �s megadjuk a cabColort pirosnak. */
								System.out.println("Nem sz�llnak le az utasok...");
								cabColor = Color.RED;
							} else if(vagonSzin == 'g' || vagonSzin == 'G'){ /* Ha a vagon z�ld, akkor le kell majd sz�ljanak az utasok. megadjuk a cabColort z�ldnek. */
								System.out.println("Lesz�llnak az utasok...");
								cabColor = Color.GREEN;
							} else if(vagonSzin == 'b' || vagonSzin == 'B'){ /* Ha k�k, akkor szint�n nem fognak lesz�llni az utaasok. Megadjuk a cabColort k�knek. */
								System.out.println("Nem sz�llnak le az utasok...");
								cabColor = Color.BLUE;
							} else {						
								System.out.println("A vagonok �s a meg�ll�k csak 'r', 'g' vagy 'b' sz�n�ek lehetnek");
								break;
							}
							
							
							ArrayList<Color>cabinColors41 = new ArrayList<Color>();/* L�trehozunk egy sz�nlist�t. Ebb�l fog fel�p�lni a vonat. */
							cabinColors41.add(cabColor);/* A sz�nlist�hoz hozz�adjuk a kiv�lasztott sz�nt. */
							
							GameController gc541 = new GameController();/* l�trehozunk egy gameControllert */
							gc541.startNewGame(54);/* elind�tjuk a j�t�kot az 54-es p�ly�n*/
							
							gc541.skeletonTesterAddNewTrain(cabinColors41);/* hozz�adjuk az �jonnan l�trehozott vonatot */
							gc541.skeletonTesterMakeTrainsMove();/* L�ptetj�k egyet a vonatokat. */
							gc541.skeletonTesterMakeTrainsMove();/* L�ptetj�k egyet a vonatokat. */
						break;
						
						case 'N':
						case 'n': System.out.println("A vonat �thalad a meg�ll�n �s semmi m�s nem t�rt�nik...");
						/* Ez a programr�sz akkor fut le, ha senki nem utazott a vonaton mikor az a meg�ll�ra l�pett. 
						 * Ekkor ugyanis nincs kinek lesz�llnia a vonatr�l. */
						ArrayList<Color>cabinColors42 = new ArrayList<Color>();/* L�trehozunk egy sz�nlist�t. Ebb�l fog fel�p�lni a vonat. */
						cabinColors42.add(Color.BLACK); /* Hozz�adjuk a sz�nlist�hoz a fekete sz�nt, �gy olyan, mint ha �res lenne a vagon */
						
						GameController gc542 = new GameController(); /* l�trehozunk egy gameControllert */
						gc542.startNewGame(54);/* elind�tjuk a j�t�kot az 54-es p�ly�n*/
						
						gc542.skeletonTesterAddNewTrain(cabinColors42);/* hozz�adjuk az �jonnan l�trehozott vonatot */
						gc542.skeletonTesterMakeTrainsMove();/* L�ptetj�k egyet a vonatokat. */
						gc542.skeletonTesterMakeTrainsMove();/* L�ptetj�k egyet a vonatokat. */
						break;
						
						/* a program visszal�p a f�men�be, ha nem kap �rv�nyes bemenetet arra a k�rd�sre
						 * hogy utanak-e a vonaton
						 */
						default: System.out.println("Csak az 'i' �s 'n' a t�mogatott bemenet");
					}
					break;
					
					case 5: System.out.println("A vonat elhagyja a p�ly�t...");
					/* Ez a teszteset szimul�lja, hogy a vonat egy bemeneti ponton kereszt�l kifel� 
					 * haladva elhagyja a p�ly�t.
					 */
					ArrayList<Color>cabinColors5 = new ArrayList<Color>(); /* L�trehozunk egy sz�nlist�t. Ebb�l fog fel�p�lni a vonat. */
					cabinColors5.add(Color.RED); /* A sz�nlist�hoz hozz�adunk egy piros sz�nt. */
					cabinColors5.add(Color.GREEN);/* A sz�nlist�hoz hozz�adunk egy z�ld sz�nt. */
					cabinColors5.add(Color.BLUE);/* A sz�nlist�hoz hozz�adunk egy k�k sz�nt. */
					
					GameController gc55 = new GameController();/* l�trehozunk egy gameControllert */
					gc55.startNewGame(55);/* elind�tjuk a j�t�kot az 55-�s p�ly�n*/
									
					gc55.skeletonTesterAddNewTrain(cabinColors5);/* hozz�adjuk az �jonnan l�trehozott vonatot */
					gc55.skeletonTesterMakeTrainsMove();/* L�ptetj�k egyet a vonatokat. */
					gc55.skeletonTesterMakeTrainsMove();/* L�ptetj�k egyet a vonatokat. */
					break;
					
					case 6: System.out.println("A vonat �ssze�tk�zik egy m�sikkal...");
					/*Ez a teszteset szimul�lja, hogy a vonat �ssze�tk�zik egy m�sik vonattal. */
					ArrayList<Color>cabinColors6 = new ArrayList<Color>();/* L�trehozunk egy sz�nlist�t. Ebb�l fog fel�p�lni a vonat. */
					cabinColors6.add(Color.RED);/* A sz�nlist�hoz hozz�adunk egy piros sz�nt. */
					cabinColors6.add(Color.GREEN);/* A sz�nlist�hoz hozz�adunk egy z�ld sz�nt. */
					cabinColors6.add(Color.BLUE);/* A sz�nlist�hoz hozz�adunk egy k�k sz�nt. */
					
					GameController gc56 = new GameController();/* l�trehozunk egy gameControllert */
					gc56.startNewGame(56);/* elind�tjuk a j�t�kot az 56-os p�ly�n*/
									
					gc56.skeletonTesterAddNewTrain(cabinColors6);/* hozz�adunk egy vonatot a megadott sz�nekkel */
					gc56.skeletonTesterMakeTrainsMove();/* l�ptetj�k egyet */
					
					gc56.skeletonTesterAddNewTrain(cabinColors6); /* Hozz�adunk m�gegy vonatot */
					gc56.skeletonTesterMakeTrainsMove(); /* l�ptetj�k, melynek hat�s�ra bel�p a j�t�kt�rbe �s �tk�zik az el�bb hozz�adott vonattal. */
					break;	
					
					/* Ha a felhaszn�l� nem a v�rt kimenetet adja (vagyis nem 1, 2, 3,4 , 5, 6 sz�mok valamelyik�t
					 * akkor a tesztel� program kil�p a f�men�be.
					 */
					default: System.out.println("A vonat csak a fent jelzett 6 dologra l�phet, az elv�rt bemenet 1, 2, 3, 4, 5 vagy 6");
				}
				break;
			
				
				/* --------------------------------------------------------------------------------- 6. MEN�PONT ----------------------------------------------------------------------------- */
			
				/* Ez a teszteset felel az�rt, ha egy �j vonat �rkezik a p�ly�ra.
				 * El�sz�r lek�rdezi a felhaszn�l�t�l a program, hogy h�ny vagon legyen a vonatban.
				 */
				case 6: System.out.println("�j vonat �rkezik. H�ny vagon legyen? [pozit�v eg�sz sz�m]");
				int vagonSzam = Integer.parseInt(scanner.nextLine());
				/* Megvizsg�ljuk, hogy a felhaszn�l� �rv�nyes vagon sz�mot �rt-e be. */
				if(vagonSzam <= 0) {
					System.out.println("Csak pozit�v eg�sz sz�m lehet!");
					break;
				}
				
				System.out.println("A vonatnak " + vagonSzam + " vagonja lesz");
				
				/*
				 * Ezek a booleanok az�rt kellenek, mert ha m�r elkezd�dik a vonat vagonjainak felkonfigur�l�sa,
				 * akkor ha a felhaszn�l� nem l�tez� sz�nt �rna be vagy arra a k�rd�sre, hogy a vagonban
				 * vannak-e �rv�nytelen v�laszt adna, akkor a vagon �rv�nytelen param�terekkel lenne inicializ�lva.
				 * Ezt elker�lend�, k�t while ciklus addig nem engedi tov�bb a program fut�s�t, am�g a 
				 * felhaszn�l� �rv�nyes bemenetet nem ad az adott vagonra, biztos�tva ezzel,
				 * hogy nem lesznek vagonok �rv�nytelen param�terekkel felkonfigur�lva.
				 */
				boolean ervenyesBemenetSzin = false;		
				/*Ebben t�roljuk a vagonok sz�n�t amit a felhaszn�l� ad meg*/
				ArrayList<Color> cabinColors = new ArrayList<Color>();
				/* Ez a ciklus v�grehajtja a vagon inicializ�l�st minden vagonra. */
				for(int i = 0; i < vagonSzam; i++)
				{
					ervenyesBemenetSzin = false;
					while(!ervenyesBemenetSzin)
					{
						System.out.println("Milyen sz�n� legyen a " + (i+1) + ". vagon? [r, g, b]");
						switch (scanner.nextLine().charAt(0))
						{
							case 'R':
							case 'r': System.out.println("Piros a(z) " + (i+1) + ". vagon"); 
							/* Ha egy �rv�nyes sz�nt ad meg a felhaszn�l�, akkor kil�p ebb�l a while
							 * ciklusb�l Ellenkez� esetben, megint azt k�rdezi t�le a program, milyen sz�n� legyen
							 * ez a vagon.
							 */
							ervenyesBemenetSzin = true;
							/* be�ll�tjuk az ideiglenes t�mb�nkben, hogy a felhaszn�l� azt mondta, legyen az i.-edik elem piros */
							cabinColors.add(Color.RED);
							break;
							
							case 'G':
							case 'g': System.out.println("Z�ld a(z) " + (i+1) + ". vagon");
							ervenyesBemenetSzin = true;
							cabinColors.add(Color.GREEN);
							break;
							
							case 'B':
							case 'b': System.out.println("K�k a(z) " + (i+1) + ". vagon");
							ervenyesBemenetSzin = true;
							cabinColors.add(Color.BLUE);
							break;
							
							default: System.out.println("Csak az 'r', 'g' �s 'b' a t�mogatott bemenet.");
						}
					}
				}
				
				GameController gc6 = new GameController(); /* l�trehozunk egy gameControllert */
				gc6.startNewGame(1);/* elind�tjuk a j�t�kot az 1-es p�ly�n*/
				gc6.skeletonTesterAddNewTrain(cabinColors);/* hozz�adjuk az �jonnan l�trehozott vonatot */
				
				break;
				
				
				/* --------------------------------------------------------------------------------- 7. MEN�PONT ----------------------------------------------------------------------------- */
				
				/* Ez a teszteset azt szimul�lja, ha a j�t�kos nyer.
				 * Ebben az esetben megk�rdezi, melyik p�ly�t nyerte meg.
				 */
				case 7: System.out.println("Gy�zelem. Melyik p�ly�n volt? [1, 2]");
				switch (Integer.parseInt(scanner.nextLine())){
					case 1: System.out.println("Gy�zelem az els� p�ly�n...");
					/* Ha a j�t�kos az els� p�ly�t nyeri meg akkor a j�t�k a m�sodik p�ly�t elkezdi. */
					GameController gc71 = new GameController(); /* l�trehozunk egy gameControllert */
					gc71.startNewGame(1); /* elind�tjuk a j�t�kot az 1-es p�ly�n */
					gc71.winEvent(); /* Megh�vjuk a l�trehozott gameController winEventj�t. A j�t�k a 2. p�ly�n folytat�dik.*/
					
					break;
					case 2: System.out.println("Gy�zelem a m�sodik p�ly�n");
					GameController gc72 = new GameController(); /* l�trehozunk egy gameControllert */
					gc72.startNewGame(2); /* elind�tjuk a j�t�kot a 2-es p�ly�n*/
					gc72.winEvent(); /* Megh�vjuk a l�trehozott gameController winEventj�t. Tov�bbi p�ly�k hi�ny�ban a j�t�k le�ll.*/
					break;
					
					default: System.out.println("Csak az 1 �s a 2 a megengedett bemenet.");
				}
				break;
				
				
				/* --------------------------------------------------------------------------------- 8. MEN�PONT ----------------------------------------------------------------------------- */
				
				/*Ha a felhszn�l� a nyolcas gombot nyomja meg, a a tesztprogram v�get �r.*/
				case 8: run = false; break;
				
			/*Ha olyan tesztesetet ind�tana  a j�t�kos ami nem is l�tezik, akkor a f�men�ben marad. */
			default: System.out.println("Nincs ilyen teszteset");
			}
		}
		scanner.close();
	}
}
