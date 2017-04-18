package sandbox;

import java.io.BufferedReader;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * A p�lyafel�p�t�s �s �sszek�t�s�nek helyess�g�t tesztel� programsorok
 * @author Long
 *
 */

public class MapCreationTest {
	private static ArrayList<MapObject> objects = new ArrayList<MapObject>();
	private static int mapX; /* P�ly�nk sz�less�ge */
	private static int mapY; /* P�ly�nk magass�ga */
	private static String[][] mapView; /* P�lya kin�zete */
	private static MapObject[][] mapObjects; /* P�lya elemei */	
	private static String newMapFileName = "LastMapView.txt"; /*A ment�s neve*/
	
	/**
	 * Az �sszes teszt elind�t�s��rt felel�s oszt�ly
	 */
	public static void main(String originalMapFileName, String saveFileName) throws IOException {
		System.out.println();
		System.out.println("Test Running...");
		readMapTest(saveFileName); /*Megpr�b�ljuk beolvasni a ment�s alapj�na p�ly�t*/
		createMap(); /*Megpr�b�ljuk a ment�s alapj�n rekonstru�lni a p�lya kin�zet�t.*/
		reconstructionTest(originalMapFileName); /*Megn�zz�k hogy a m�solat egyezik e az eredetivel*/
		neighbourTest();	/*Ellen�rizz�k a szomsz�ds�gokat*/
		System.out.println();
		System.out.println("Test Finished!");
		System.out.println();
	}
	
	/**
	 * Az el�z�leg meg�rt ment�si f�jlb�l megpr�b�ljuk kiolvasni az objektumok adatait
	 */
	private static void readMapTest(String fileName) throws NumberFormatException, IOException {
		System.out.println();
		System.out.println("Map Reading Test Started...");

		/*F�jl beolvas�s�ra hszn�lt Readerek*/
		FileReader reader = new FileReader(fileName);
		BufferedReader br = new BufferedReader(reader);

		/* Egy sort vizsg�ljuk */
		String line;
		String[] split;

		/* Sor egy r�szlet�te vizsg�ljuk */
		String subline;
		String[] subsplit;

		/* Kiolvasot t�nyleges adatok */
		String type, address;
		ArrayList<String> neighbours; /* Nem igen lehet t�bb mint 4... */
		int x, y;

		/* A p�ly�nk m�ret�t meg kell keresn�nk */

		/*
		 * A mentett f�jlunk formai megk�t�se: |type [x,y] (address)
		 * {neighbour,neighbour,... }| Ezt fogjuk minden sorban �s minden
		 * r�szletben sz�tbontani
		 */

		while ((line = br.readLine()) != null && line.length() != 0) {
			split = line.split(" ");
			neighbours = new ArrayList<String>();

			/* N�v kiolvas�sa */
			type = split[0]; /* type ... */

			/* Koordin�t�k kiolvas�sa */
			subline = split[1]; /* [x,y] */
			subsplit = subline.split(","); /* [x | y] */
			subsplit[0] = subsplit[0].substring(1,
					subsplit[0].length()); /* x | y] */
			subsplit[1] = subsplit[1].substring(0,
					subsplit[1].length() - 1); /* x | y */
			x = Integer.parseInt(subsplit[0]); /* x */
			y = Integer.parseInt(subsplit[1]); /* y */

			/*
			 * Ha ekkora index�nk m�g nem volt akkor a p�lya nagyobb mint
			 * gondoltuk eddig
			 */
			if (mapX < x)
				mapX = x;
			if (mapY < y)
				mapY = y;

			/* Cim kiolvas�sa */
			subline = split[2]; /* (address) */
			subline = subline.substring(1, subline.length() - 1); /* address */
			address = subline;

			/* Szomsz�dok kiolvas�sa */
			split[3] = split[3].substring(1, split[3].length()
					- 1); /* {neighbour, neighbour} -> neighbour,neighbour */
			subsplit = split[3].split(","); /* neighbour | neighbour */

			/* Minden szomsz�dot felvesz�nk a list�ba */
			for (String adr : subsplit) {
				neighbours.add(adr);
			}
			/* Kiolvas�s k�sz */

			/* Felvessz�k a t�runkba */
			MapObject newMapObject = new MapObject(type, address, x, y, neighbours);
			objects.add(newMapObject);
		}
		br.close();
		mapX++; /* 0tol indexelt�nk, ezt javitjuk */
		mapY++; /* 0tol indexelt�nk, ezt javitjuk */
		System.out.println(objects.size() + " Map Objects Created!");
		System.out.println("Map Reading Test was Successful!");
	}

	/**
	 * L�trehozunk egy l�tv�nytervet a p�ly�r�l a beolvasott adatok alapj�n
	 */
	private static void createMap() throws IOException {
		System.out.println();
		System.out.println("Creatin Map View File...");
		mapView = new String[mapX][mapY]; /*A p�lya karakteres reprezent�ci�j�t t�rolj*/
		mapObjects = new MapObject[mapX][mapY]; /*A l�trehozott p�lyaobjektumokat t�rolja*/

		/* �res p�ly�t hozunk l�tre */
		for (int x = 0; x < mapX; x++) {
			for (int y = 0; y < mapY; y++) {
				mapView[x][y] = "x";
				mapObjects[x][y] = null;
			}
		}
		System.out.println("Map Size: " + mapX + "x" + mapY);
		/* Kit�ltj�k a p�ly�t a karakterk�dok alapj�n */
		for (MapObject obj : objects) {
			mapView[obj.getX()][obj.getY()] = getCharacterCode(obj.getType());
			mapObjects[obj.getX()][obj.getY()] = obj;
		}

		/* Kimentj�k egy f�jlba hogy l�thassuk is, ehez kellenek az �r�k*/
		FileWriter fw = new FileWriter(newMapFileName);
		PrintWriter writer = new PrintWriter(fw);

		/* M�ret beir�sa a f�jlba*/
		writer.print(mapX + ";" + mapY);

		/* Elemek beir�sa */
		for (int y = 0; y < mapY; y++) {
			writer.println();
			for (int x = 0; x < mapX; x++) {
				writer.print(mapView[x][y]);
			}
		}
		/*V�grehajtjuk �s z�rjuk az �r�kat*/
		writer.flush();
		writer.close();
		System.out.println("Map View File Created!");
	}

	/**
	 * Megn�zz�k hogy az eredeti p�lya �s a vissza�ll�tott
	 * adatok alapj�n k�sz�lt m�solat minden eleme egyezik e
	 */
	private static void reconstructionTest(String original) throws IOException {
		System.out.println();
		System.out.println("Reconstruction Test Started...");
		
		/*Eredeti �s az �j f�jlunknak is kel egy olvas�*/
		FileReader Oreader = new FileReader(original);
		BufferedReader Obr = new BufferedReader(Oreader);

		FileReader Nreader = new FileReader(newMapFileName);
		BufferedReader Nbr = new BufferedReader(Nreader);
		
		String OldLine, NewLine; /*Soronk�nt olvasunk ki a f�jlokbol ezekbe*/
		while ((OldLine = Obr.readLine()) != null && (NewLine = Nbr.readLine()) != null) { /*Ha nem fogyott el a sor egyikben sem*/
			if (!OldLine.equals(NewLine)) { /*Ha nem egyezik meg a k�t sor*/
				System.out.println("The two maps do not match!");
				System.out.println("Reconstruction Test Failed!");
				Obr.close();
				Nbr.close();
				return;
			}
		}
		/*Ha ide �r�nk a k�t f�jl egyezett*/
		/*Z�rjuk az olvas�kat*/
		Obr.close();
		Nbr.close();
		System.out.println("The two maps match!");
		System.out.println("Reconstruction Test Successful!");
	}
	
	/**
	 * Megn�zz�k hogy minden szomsz�doss�g k�lcs�n�s e, �s megvannak e a soron k�vetkez� elemek
	 */
	private static void neighbourTest() {
		System.out.println();
		System.out.println("Neighbour Test Started...");
		int errors = 0; /*Sz�moljuk a hib�kat*/
		for (MapObject obj : objects) { /*Minden koordin�t�ra megn�zz�k*/
			int objX = obj.getX();
			int objY = obj.getY();
			
			/*Innent�l azt figyelj�k hogy ha van �s � nem a mi szomsz�d�nk c�m szerint akkor hib�t kell kapnunk.
			
			/* Bal oldali szomsz�d */
			if (objX > 0) {
				if (mapObjects[objX - 1][objY] != null && !obj.isNeighbour(mapObjects[objX - 1][objY])) {
					System.out.println("ERROR! " + obj.getAddress() + " has no neighbour called "
							+ mapObjects[objX - 1][objY].getAddress() + "!");
					errors++;
				}
			}
			/* Jobb oldali szomsz�d */
			if (objX < mapX - 1) {
				if (mapObjects[objX + 1][objY] != null && !obj.isNeighbour(mapObjects[objX + 1][objY])) {
					System.out.println("ERROR! " + obj.getAddress() + " has no neighbour called "
							+ mapObjects[objX + 1][objY].getAddress() + "!");
					errors++;
				}
			}
			/* Felette l�v� szomsz�d */
			if (objY > 0) {
				if (mapObjects[objX][objY - 1] != null && !obj.isNeighbour(mapObjects[objX][objY - 1])) {
					System.out.println("ERROR! " + obj.getAddress() + " has no neighbour called "
							+ mapObjects[objX][objY - 1].getAddress() + "!");
					errors++;
				}
			}
			/* Alatta l�v� szomsz�d */
			if (objY < mapY - 1) {
				if (mapObjects[objX][objY + 1] != null && !obj.isNeighbour(mapObjects[objX][objY + 1])) {
					System.out.println("ERROR! " + obj.getAddress() + " has no neighbour called "
							+ mapObjects[objX][objY + 1].getAddress() + "!");
					errors++;
				}
			}
			;
		}
		/*Fut�s v�ge*/
		System.out.println("Neighbour Test Ended!");
		System.out.println("There were " + errors + " error(s)!");
	}

	/**
	 * Az oszt�lyn�v alapj�n megadja, a jel�l�s�t a sz�vegf�jlban
	 */
	private static String getCharacterCode(String type) {
		switch (type) {
		case "EnterPoint": 		/*EnterPoint eset�n a sz�vegbe E ker�l*/
			return "E";
		case "Rail":			/*Rail eset�n a sz�vegbe R ker�l*/
			return "R";
		case "Switch":			/*Switch eset�n a sz�vegbe S ker�l*/
			return "S";			
		case "TunnelEntrance":	/*TunnelEntrance eset�n a sz�vegbe U ker�l*/
			return "U";
		case "XRail":			/*XRail eset�n a sz�vegbe X ker�l*/
			return "X";
		case "TrainStation1":	/*Piros TrainStaion eset�n a sz�vegbe 1 ker�l*/
			return "1";
		case "TrainStation2":	/*Z�ld TrainStaion eset�n a sz�vegbe 2 ker�l*/
			return "2";
		case "TrainStation3":	/*K�k TrainStaion eset�n a sz�vegbe 3 ker�l*/
			return "3";
		default:				/*Ha egyik sem, akkor �res elem ami x*/
			return "x";
		}
	}
	
	/**
	 * Egy oszt�ly, amiben a program fut�sa k�zben gener�lt oszt�lyok minden adat�t t�roljuk
	 * Ezek seg�ts�g�vel vizsg�ljuk a fel�p�tett p�lya szab�lyoss�g�t
	 * @author Long
	 */
	public static class MapObject {
		int x, y;						/*Poz�c�t t�rol*/
		String type;					/*Oszt�lyt�pust t�rol*/
		String address;					/*Pointert t�rol*/
		ArrayList<String> neighbours;	/*Szomsz�doakt t�rolja*/

		public MapObject() {
			neighbours = new ArrayList<String>();
		}
		
		/**
		 * Konstruktorok
		 */
		public MapObject(int X, int Y) {
			neighbours = new ArrayList<String>();
			x = X;
			y = Y;
		}

		public MapObject(String adr, int X, int Y) {
			neighbours = new ArrayList<String>();
			x = X;
			y = Y;
		}

		public MapObject(String T, String adr, int X, int Y) {
			neighbours = new ArrayList<String>();
			x = X;
			y = Y;
			type = T;
			address = adr;
		}

		public MapObject(String T, String adr, int X, int Y, ArrayList<String> negh) {

			x = X;
			y = Y;
			type = T;
			address = adr;
			neighbours = new ArrayList<String>();
			for (String s : negh) {
				neighbours.add(s);
			}
		}
		
		/**
		 * Be�ll�tja a koordin�t�it
		 */
		public void setXY(int X, int Y) {
			x = X;
			y = Y;
		}
		
		/**
		 * Be�ll�tja a t�pus�t
		 * @param T
		 */
		public void setType(String T) {
			type = T;
		}
		/**
		 * �j szomsz�dot ad hozz�
		 * @param neighbour
		 */
		public void addNeighbour(String neighbour) {
			neighbours.add(neighbour);
		}
		/**
		 * Be�ll�tja a mem�riacmet
		 * @param adr
		 */
		public void setAddress(String adr) {
			address = adr;
		}
		/**
		 * Visszadja az X koordin�t�t
		 * @return
		 */
		public int getX() {
			return x;
		}
		/**
		 * Visszaadja az X koordin�t�t
		 * @return
		 */
		public int getY() {
			return y;
		}
		/**
		 * Visszaadja az oszt�lynevet
		 * @return
		 */
		public String getType() {
			return type;
		}
		/**
		 * Visszaadja a mam�riac�met
		 * @return
		 */
		public String getAddress() {
			return address;
		}
		
		/**
		 * Visszaadja a szomsz�doakt
		 */
		
		public ArrayList<String> getNeighbours() {
			return neighbours;
		}
		
		/**
		 * Megn�zi, hogy az adott objektum a szomszdja e
		 */
		public boolean isNeighbour(MapObject neigh) {
			for (String neighbour : neighbours) {
				if (neighbour.equals(neigh.getAddress()))
					return true;
			}
			return false;
		}
	}
}
