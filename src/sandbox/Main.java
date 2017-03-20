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
		 
		 boolean run = true;
		 
		while(run){
			System.out.println("\n Válassz az alábbi lehetõségek közül:"
					+ "\n 1: Új játék indítása"
					+ "\n 2: Alagútszáj aktiválása"
					+ "\n 3: Alagútszáj deaktiválása"
					+ "\n 4: Váltó állítása"
					+ "\n 5: Vonatok léptetése"
					+ "\n 6: Új vonat érkezése"
					+ "\n 7: Gyõzelem"
					+ "\n 8: kilépés");
			
			switch(Integer.parseInt(scanner.nextLine())){
			
				case 1: System.out.println("1: Új játék indítása. Melyik pálya legyen? [1, 2]");
					switch(Integer.parseInt(scanner.nextLine())){
						case 1: System.out.println("1. pálya indítása...");
							/* TODO tesztesetet kitölteni */
						break;
						case 2: System.out.println("2. pálya indítása...");
						/* TODO tesztesetet kitölteni */
						break;
						default: System.out.println("Csak 1. és 2. pálya van. Az elvárt bemenet az '1' vagy a '2'");
					}
				break;
					
				case 2: System.out.println("Alagútszály aktiválása. Hány alagútszály van jelenleg nyitva? [0, 1]");
				switch (Integer.parseInt(scanner.nextLine())){
					case 0: System.out.println("Elsõ alagútszáj létrehozása...");
					/*TODO */
					break;
					case 1: System.out.println("Második alagútszáj létrhozása...");
					/*TODO */
					break;
					default: System.out.println("Nincs ilyen opció. Az elvárt bemenet az '1' vagy a '2'");
				}
				break;
				
				case 3: System.out.println("Alagótszály deaktiválása. Hány alagútszály van jelenleg nyitva? [1, 2]");
				switch (Integer.parseInt(scanner.nextLine())){
					case 1: System.out.println("Egyedüli alagútszáj zárása..."); 
					/*TODO */
					break;
					case 2: System.out.println("Egyik alagútszáj zárása...");
					/*TODO */
					break;
					default: System.out.println("Csak akkor lehet alagútszájat zárni, ha 1 vagy 2 van nyitva");
				}
				break;
				
				case 4: System.out.println("Váltó állítás. A váltó eredetileg módosította az irányt? [i/n]");
				
				switch (scanner.nextLine().charAt(0)){
					case 'i': System.out.println("Váltó állítása alaphelyzetbe..."); 
					/*TODO */
					break;
					case 'n': System.out.println("Váltó állítása alternatív helyzetbe...");
					/*TODO */
					break;
					default: System.out.println("Csak a kis 'i' és kis 'n' a támogatott bemenet");
				}
				break;
				
				case 5: System.out.println("Vonatok léptetése. Hova lép a vonat?"
						+ "\n 1: sínre"
						+ "\n 2: váltóra"
						+ "\n 3: alagútszájra"
						+ "\n 4: megállóra"
						+ "\n 5: Vonat elhagyja a játékteret belépési ponton keresztül"
						+ "\n 6: Két vonat ütközik");
				switch (Integer.parseInt(scanner.nextLine())){
					case 1: System.out.println("A vonat sínre lép..."); 
					/*TODO */
					break;
					case 2: System.out.println("A vonat váltóra lép. A váltó alaphelyzetben (i) vagy az elternatív helyzetben (n) van? [i, n]");
					switch (scanner.nextLine().charAt(0)){
						case 'i': System.out.println("A vonat egyenesen halad tovább..."); 
						/*TODO */
						break;
						case 'n': System.out.println("A vonat elkanyarodik...");
						/*TODO */
						break;
						default: System.out.println("Csak a kis 'i' és kis 'n' a támogatott bemenet");
					}
					break;
					case 3: System.out.println("A vonat alagútszájra lép. A váltó az alagútszáj felé irányítja a vonatot? [i, n]");
					switch (scanner.nextLine().charAt(0)){
						case 'i': System.out.println("Hány aktív alagútszáj van a pályán? [1, 2]"); 
							switch (Integer.parseInt(scanner.nextLine())){
							case 1: System.out.println("A vonat belép a zsákutca alagútba..."); 
							/*TODO */
							break;
							case 2: System.out.println("A vonat belép az alagútba...");
							/*TODO */
							break;
							default: System.out.println("Csak akkor léphet a vonat alagútba, ha, ha 1 vagy 2 nyitott alagútszáj van.");
						}
						break;
						case 'n': System.out.println("A vonat elkerüli az alagutat...");
						/*TODO */
						break;
						default: System.out.println("Csak a kis 'i' és kis 'n' a támogatott bemenet");
					}
					break;
					case 4: System.out.println("A vonat megállóra lép. Van nem üres kabin a vonatban? [i, n]");
						switch (scanner.nextLine().charAt(0)){
							case 'i': System.out.println("Milyen színû a megálló? Piros, zöld vagy kék lehet. [r, g, b]"); 
							char megalloSzin = scanner.nextLine().charAt(0);
							System.out.println("Milyen színû a vagon? Piros, zöld vagy kék lehet. [r, g, b]"); 
							char vagonSzin = scanner.nextLine().charAt(0);
							if ((megalloSzin != 'r' && megalloSzin != 'g' && megalloSzin != 'b') || 
									(vagonSzin != 'r' && vagonSzin != 'g' && vagonSzin != 'b')){
								System.out.println("A vagonok és a megállók csak 'r', 'g' vagy 'b' színûek lehetnek");
								break;
							}
							if(megalloSzin == vagonSzin){
								System.out.println("Leszállnak az utasok...");
								/* TODO */
							}else{
								System.out.println("Nem szállnak le az utasok...");
								/* TODO*/
							}
						
						break;
						case 'n': System.out.println("A vonat átjalad a megállón és semmi más nem történik...");
						/*TODO */
						break;
						default: System.out.println("Csak a kis 'i' és kis 'n' a támogatott bemenet");
					}
					break;
					case 5: System.out.println("A vonat elhagyja a pályát...");
					/*TODO */
					break;
					case 6: System.out.println("A vonat összeütközik egy másikkal...");
					/*TODO */
					break;					
					default: System.out.println("A vonat csak a fent jelzett 6 dologra léphet, az elvárt bemenet 1, 2, 3, 4, 5 vagy 6");
			}
			break;
			
				case 6: System.out.println("Új vonat érkezik. Hány vagon legyen? [pozitív egész szám]");
				int vagonSzam = Integer.parseInt(scanner.nextLine());
				if(vagonSzam <= 0) break;
				/* TODO létrehozni egy vonatot a collectionben adott vagonszámmal */
				System.out.println("A vonatnak " + vagonSzam + " vagonja lesz");
				boolean ervenyesBemenetSzin = false;
				boolean ervenyesBemenetVagonteli = false;
				for(int i = 0; i < vagonSzam; i++){
					ervenyesBemenetSzin = false;
					ervenyesBemenetVagonteli = false;
					while(!ervenyesBemenetSzin){
						System.out.println("Milyen színû legyen a " + i + ". vagon? [r, g, b]");
						switch (scanner.nextLine().charAt(0)){
							case 'r': System.out.println("Piros a(z) " + i + ". vagon"); 
							ervenyesBemenetSzin = true;
							/*TODO */
							break;
							case 'g': System.out.println("Zöld a(z) " + i + ". vagon");
							ervenyesBemenetSzin = true;
							/*TODO */
							break;
							case 'b': System.out.println("Kék a(z) " + i + ". vagon");
							ervenyesBemenetSzin = true;
							/*TODO */
							break;
							default: System.out.println("Csak a kis 'r', 'g' és 'b' a támogatott bemenet.");
						}
						
					}
					while(!ervenyesBemenetVagonteli){
						System.out.println("Vannak ebben a vagonban utasok? [i, n]");
						switch (scanner.nextLine().charAt(0)){
							case 'i': System.out.println("Ebben a vagonban lesznek utasok.");
							ervenyesBemenetVagonteli = true;
							/*TODO */
							break;
							case 'n': System.out.println("Ebben a vagonban nem lesznek utasok.");
							ervenyesBemenetVagonteli = true;
							/*TODO */
							break;
							default: System.out.println("Csak a kis 'i' és kis 'n' a támogatott bemenet");
						}
					}
					
					/* TODO beállítani az adott vagon színét */
				}
				break;
				
				case 7: System.out.println("Gyõzelem. Melyik pályán volt? [1, 2]");
				switch (scanner.nextLine().charAt(0)){
					case 1: System.out.println("Gyõzelem az elsõ pályán...");
					break;
					case 2: System.out.println("Gyõzelem a második pályán");
					break;
					default: System.out.println("Csak az 1 és a 2 a megengedett bemenet.");
				}
				case 8: run = false; break;
			
			default: System.out.println("Nincs ilyen teszteset");
			}
		}
		scanner.close();
	}
}
