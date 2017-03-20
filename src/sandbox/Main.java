package sandbox;

import java.io.IOException;
import java.util.Scanner;

/**
 * A szkeleton vez�rl�s��rt felel�s oszt�ly, mely kezeli a felhaszn�l�val val� kommunik�ci�t �s az alapj�n vez�rli a programot.
 */
public class Main {
	public static void main(String[] args) throws IOException{
		 System.out.println("Long /* NEM */ dolgozik");
		
		 Scanner scanner = new Scanner(System.in);
		 
		 boolean run = true;
		 
		while(run){
			System.out.println("\n V�lassz az al�bbi lehet�s�gek k�z�l:"
					+ "\n 1: �j j�t�k ind�t�sa"
					+ "\n 2: Alag�tsz�j aktiv�l�sa"
					+ "\n 3: Alag�tsz�j deaktiv�l�sa"
					+ "\n 4: V�lt� �ll�t�sa"
					+ "\n 5: Vonatok l�ptet�se"
					+ "\n 6: �j vonat �rkez�se"
					+ "\n 7: Gy�zelem"
					+ "\n 8: kil�p�s");
			
			switch(Integer.parseInt(scanner.nextLine())){
			
				case 1: System.out.println("1: �j j�t�k ind�t�sa. Melyik p�lya legyen? [1, 2]");
					switch(Integer.parseInt(scanner.nextLine())){
						case 1: System.out.println("1. p�lya ind�t�sa...");
							/* TODO tesztesetet kit�lteni */
						break;
						case 2: System.out.println("2. p�lya ind�t�sa...");
						/* TODO tesztesetet kit�lteni */
						break;
						default: System.out.println("Csak 1. �s 2. p�lya van. Az elv�rt bemenet az '1' vagy a '2'");
					}
				break;
					
				case 2: System.out.println("Alag�tsz�ly aktiv�l�sa. H�ny alag�tsz�ly van jelenleg nyitva? [0, 1]");
				switch (Integer.parseInt(scanner.nextLine())){
					case 0: System.out.println("Els� alag�tsz�j l�trehoz�sa...");
					/*TODO */
					break;
					case 1: System.out.println("M�sodik alag�tsz�j l�trhoz�sa...");
					/*TODO */
					break;
					default: System.out.println("Nincs ilyen opci�. Az elv�rt bemenet az '1' vagy a '2'");
				}
				break;
				
				case 3: System.out.println("Alag�tsz�ly deaktiv�l�sa. H�ny alag�tsz�ly van jelenleg nyitva? [1, 2]");
				switch (Integer.parseInt(scanner.nextLine())){
					case 1: System.out.println("Egyed�li alag�tsz�j z�r�sa..."); 
					/*TODO */
					break;
					case 2: System.out.println("Egyik alag�tsz�j z�r�sa...");
					/*TODO */
					break;
					default: System.out.println("Csak akkor lehet alag�tsz�jat z�rni, ha 1 vagy 2 van nyitva");
				}
				break;
				
				case 4: System.out.println("V�lt� �ll�t�s. A v�lt� eredetileg m�dos�totta az ir�nyt? [i/n]");
				
				switch (scanner.nextLine().charAt(0)){
					case 'i': System.out.println("V�lt� �ll�t�sa alaphelyzetbe..."); 
					/*TODO */
					break;
					case 'n': System.out.println("V�lt� �ll�t�sa alternat�v helyzetbe...");
					/*TODO */
					break;
					default: System.out.println("Csak a kis 'i' �s kis 'n' a t�mogatott bemenet");
				}
				break;
				
				case 5: System.out.println("Vonatok l�ptet�se. Hova l�p a vonat?"
						+ "\n 1: s�nre"
						+ "\n 2: v�lt�ra"
						+ "\n 3: alag�tsz�jra"
						+ "\n 4: meg�ll�ra"
						+ "\n 5: Vonat elhagyja a j�t�kteret bel�p�si ponton kereszt�l"
						+ "\n 6: K�t vonat �tk�zik");
				switch (Integer.parseInt(scanner.nextLine())){
					case 1: System.out.println("A vonat s�nre l�p..."); 
					/*TODO */
					break;
					case 2: System.out.println("A vonat v�lt�ra l�p. A v�lt� alaphelyzetben (i) vagy az elternat�v helyzetben (n) van? [i, n]");
					switch (scanner.nextLine().charAt(0)){
						case 'i': System.out.println("A vonat egyenesen halad tov�bb..."); 
						/*TODO */
						break;
						case 'n': System.out.println("A vonat elkanyarodik...");
						/*TODO */
						break;
						default: System.out.println("Csak a kis 'i' �s kis 'n' a t�mogatott bemenet");
					}
					break;
					case 3: System.out.println("A vonat alag�tsz�jra l�p. A v�lt� az alag�tsz�j fel� ir�ny�tja a vonatot? [i, n]");
					switch (scanner.nextLine().charAt(0)){
						case 'i': System.out.println("H�ny akt�v alag�tsz�j van a p�ly�n? [1, 2]"); 
							switch (Integer.parseInt(scanner.nextLine())){
							case 1: System.out.println("A vonat bel�p a zs�kutca alag�tba..."); 
							/*TODO */
							break;
							case 2: System.out.println("A vonat bel�p az alag�tba...");
							/*TODO */
							break;
							default: System.out.println("Csak akkor l�phet a vonat alag�tba, ha, ha 1 vagy 2 nyitott alag�tsz�j van.");
						}
						break;
						case 'n': System.out.println("A vonat elker�li az alagutat...");
						/*TODO */
						break;
						default: System.out.println("Csak a kis 'i' �s kis 'n' a t�mogatott bemenet");
					}
					break;
					case 4: System.out.println("A vonat meg�ll�ra l�p. Van nem �res kabin a vonatban? [i, n]");
						switch (scanner.nextLine().charAt(0)){
							case 'i': System.out.println("Milyen sz�n� a meg�ll�? Piros, z�ld vagy k�k lehet. [r, g, b]"); 
							char megalloSzin = scanner.nextLine().charAt(0);
							System.out.println("Milyen sz�n� a vagon? Piros, z�ld vagy k�k lehet. [r, g, b]"); 
							char vagonSzin = scanner.nextLine().charAt(0);
							if ((megalloSzin != 'r' && megalloSzin != 'g' && megalloSzin != 'b') || 
									(vagonSzin != 'r' && vagonSzin != 'g' && vagonSzin != 'b')){
								System.out.println("A vagonok �s a meg�ll�k csak 'r', 'g' vagy 'b' sz�n�ek lehetnek");
								break;
							}
							if(megalloSzin == vagonSzin){
								System.out.println("Lesz�llnak az utasok...");
								/* TODO */
							}else{
								System.out.println("Nem sz�llnak le az utasok...");
								/* TODO*/
							}
						
						break;
						case 'n': System.out.println("A vonat �tjalad a meg�ll�n �s semmi m�s nem t�rt�nik...");
						/*TODO */
						break;
						default: System.out.println("Csak a kis 'i' �s kis 'n' a t�mogatott bemenet");
					}
					break;
					case 5: System.out.println("A vonat elhagyja a p�ly�t...");
					/*TODO */
					break;
					case 6: System.out.println("A vonat �ssze�tk�zik egy m�sikkal...");
					/*TODO */
					break;					
					default: System.out.println("A vonat csak a fent jelzett 6 dologra l�phet, az elv�rt bemenet 1, 2, 3, 4, 5 vagy 6");
			}
			break;
			
				case 6: System.out.println("�j vonat �rkezik. H�ny vagon legyen? [pozit�v eg�sz sz�m]");
				int vagonSzam = Integer.parseInt(scanner.nextLine());
				if(vagonSzam <= 0) break;
				/* TODO l�trehozni egy vonatot a collectionben adott vagonsz�mmal */
				System.out.println("A vonatnak " + vagonSzam + " vagonja lesz");
				boolean ervenyesBemenetSzin = false;
				boolean ervenyesBemenetVagonteli = false;
				for(int i = 0; i < vagonSzam; i++){
					ervenyesBemenetSzin = false;
					ervenyesBemenetVagonteli = false;
					while(!ervenyesBemenetSzin){
						System.out.println("Milyen sz�n� legyen a " + i + ". vagon? [r, g, b]");
						switch (scanner.nextLine().charAt(0)){
							case 'r': System.out.println("Piros a(z) " + i + ". vagon"); 
							ervenyesBemenetSzin = true;
							/*TODO */
							break;
							case 'g': System.out.println("Z�ld a(z) " + i + ". vagon");
							ervenyesBemenetSzin = true;
							/*TODO */
							break;
							case 'b': System.out.println("K�k a(z) " + i + ". vagon");
							ervenyesBemenetSzin = true;
							/*TODO */
							break;
							default: System.out.println("Csak a kis 'r', 'g' �s 'b' a t�mogatott bemenet.");
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
							default: System.out.println("Csak a kis 'i' �s kis 'n' a t�mogatott bemenet");
						}
					}
					
					/* TODO be�ll�tani az adott vagon sz�n�t */
				}
				break;
				
				case 7: System.out.println("Gy�zelem. Melyik p�ly�n volt? [1, 2]");
				switch (scanner.nextLine().charAt(0)){
					case 1: System.out.println("Gy�zelem az els� p�ly�n...");
					break;
					case 2: System.out.println("Gy�zelem a m�sodik p�ly�n");
					break;
					default: System.out.println("Csak az 1 �s a 2 a megengedett bemenet.");
				}
				case 8: run = false; break;
			
			default: System.out.println("Nincs ilyen teszteset");
			}
		}
		scanner.close();
	}
}
