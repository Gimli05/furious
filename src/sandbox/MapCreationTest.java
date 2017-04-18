package sandbox;

import java.io.BufferedReader;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * A pályafelépítés és összekötésének helyességét tesztelö programsorok
 * @author Long
 *
 */

public class MapCreationTest {
	private static ArrayList<MapObject> objects = new ArrayList<MapObject>();
	private static int mapX; /* Pályánk szélessége */
	private static int mapY; /* Pályánk magassága */
	private static String[][] mapView; /* Pálya kinézete */
	private static MapObject[][] mapObjects; /* Pálya elemei */	
	private static String newMapFileName = "LastMapView.txt"; /*A mentés neve*/
	
	/**
	 * Az összes teszt elindításáért felelös osztály
	 */
	public static void main(String originalMapFileName, String saveFileName) throws IOException {
		System.out.println();
		System.out.println("Test Running...");
		readMapTest(saveFileName); /*Megpróbáljuk beolvasni a mentés alapjána pályát*/
		createMap(); /*Megpróbáljuk a mentés alapján rekonstruálni a pálya kinézetét.*/
		reconstructionTest(originalMapFileName); /*Megnézzük hogy a másolat egyezik e az eredetivel*/
		neighbourTest();	/*Ellenörizzük a szomszédságokat*/
		System.out.println();
		System.out.println("Test Finished!");
		System.out.println();
	}
	
	/**
	 * Az elözöleg megírt mentési fájlból megpróbáljuk kiolvasni az objektumok adatait
	 */
	private static void readMapTest(String fileName) throws NumberFormatException, IOException {
		System.out.println();
		System.out.println("Map Reading Test Started...");

		/*Fájl beolvasására hsznált Readerek*/
		FileReader reader = new FileReader(fileName);
		BufferedReader br = new BufferedReader(reader);

		/* Egy sort vizsgáljuk */
		String line;
		String[] split;

		/* Sor egy részletéte vizsgáljuk */
		String subline;
		String[] subsplit;

		/* Kiolvasot tényleges adatok */
		String type, address;
		ArrayList<String> neighbours; /* Nem igen lehet több mint 4... */
		int x, y;

		/* A pályánk méretét meg kell keresnünk */

		/*
		 * A mentett fájlunk formai megkötése: |type [x,y] (address)
		 * {neighbour,neighbour,... }| Ezt fogjuk minden sorban és minden
		 * részletben szétbontani
		 */

		while ((line = br.readLine()) != null && line.length() != 0) {
			split = line.split(" ");
			neighbours = new ArrayList<String>();

			/* Név kiolvasása */
			type = split[0]; /* type ... */

			/* Koordináták kiolvasása */
			subline = split[1]; /* [x,y] */
			subsplit = subline.split(","); /* [x | y] */
			subsplit[0] = subsplit[0].substring(1,
					subsplit[0].length()); /* x | y] */
			subsplit[1] = subsplit[1].substring(0,
					subsplit[1].length() - 1); /* x | y */
			x = Integer.parseInt(subsplit[0]); /* x */
			y = Integer.parseInt(subsplit[1]); /* y */

			/*
			 * Ha ekkora indexünk még nem volt akkor a pálya nagyobb mint
			 * gondoltuk eddig
			 */
			if (mapX < x)
				mapX = x;
			if (mapY < y)
				mapY = y;

			/* Cim kiolvasása */
			subline = split[2]; /* (address) */
			subline = subline.substring(1, subline.length() - 1); /* address */
			address = subline;

			/* Szomszédok kiolvasása */
			split[3] = split[3].substring(1, split[3].length()
					- 1); /* {neighbour, neighbour} -> neighbour,neighbour */
			subsplit = split[3].split(","); /* neighbour | neighbour */

			/* Minden szomszédot felveszünk a listába */
			for (String adr : subsplit) {
				neighbours.add(adr);
			}
			/* Kiolvasás kész */

			/* Felvesszük a tárunkba */
			MapObject newMapObject = new MapObject(type, address, x, y, neighbours);
			objects.add(newMapObject);
		}
		br.close();
		mapX++; /* 0tol indexeltünk, ezt javitjuk */
		mapY++; /* 0tol indexeltünk, ezt javitjuk */
		System.out.println(objects.size() + " Map Objects Created!");
		System.out.println("Map Reading Test was Successful!");
	}

	/**
	 * Létrehozunk egy látványtervet a pályáról a beolvasott adatok alapján
	 */
	private static void createMap() throws IOException {
		System.out.println();
		System.out.println("Creatin Map View File...");
		mapView = new String[mapX][mapY]; /*A pálya karakteres reprezentációját tárolj*/
		mapObjects = new MapObject[mapX][mapY]; /*A létrehozott pályaobjektumokat tárolja*/

		/* Üres pályát hozunk létre */
		for (int x = 0; x < mapX; x++) {
			for (int y = 0; y < mapY; y++) {
				mapView[x][y] = "x";
				mapObjects[x][y] = null;
			}
		}
		System.out.println("Map Size: " + mapX + "x" + mapY);
		/* Kitöltjük a pályát a karakterkódok alapján */
		for (MapObject obj : objects) {
			mapView[obj.getX()][obj.getY()] = getCharacterCode(obj.getType());
			mapObjects[obj.getX()][obj.getY()] = obj;
		}

		/* Kimentjük egy fájlba hogy láthassuk is, ehez kellenek az írók*/
		FileWriter fw = new FileWriter(newMapFileName);
		PrintWriter writer = new PrintWriter(fw);

		/* Méret beirása a fájlba*/
		writer.print(mapX + ";" + mapY);

		/* Elemek beirása */
		for (int y = 0; y < mapY; y++) {
			writer.println();
			for (int x = 0; x < mapX; x++) {
				writer.print(mapView[x][y]);
			}
		}
		/*Végrehajtjuk és zárjuk az írókat*/
		writer.flush();
		writer.close();
		System.out.println("Map View File Created!");
	}

	/**
	 * Megnézzük hogy az eredeti pálya és a visszaállított
	 * adatok alapján készült másolat minden eleme egyezik e
	 */
	private static void reconstructionTest(String original) throws IOException {
		System.out.println();
		System.out.println("Reconstruction Test Started...");
		
		/*Eredeti és az új fájlunknak is kel egy olvasó*/
		FileReader Oreader = new FileReader(original);
		BufferedReader Obr = new BufferedReader(Oreader);

		FileReader Nreader = new FileReader(newMapFileName);
		BufferedReader Nbr = new BufferedReader(Nreader);
		
		String OldLine, NewLine; /*Soronként olvasunk ki a fájlokbol ezekbe*/
		while ((OldLine = Obr.readLine()) != null && (NewLine = Nbr.readLine()) != null) { /*Ha nem fogyott el a sor egyikben sem*/
			if (!OldLine.equals(NewLine)) { /*Ha nem egyezik meg a két sor*/
				System.out.println("The two maps do not match!");
				System.out.println("Reconstruction Test Failed!");
				Obr.close();
				Nbr.close();
				return;
			}
		}
		/*Ha ide érünk a két fájl egyezett*/
		/*Zárjuk az olvasókat*/
		Obr.close();
		Nbr.close();
		System.out.println("The two maps match!");
		System.out.println("Reconstruction Test Successful!");
	}
	
	/**
	 * Megnézzük hogy minden szomszédosság kölcsönös e, és megvannak e a soron következö elemek
	 */
	private static void neighbourTest() {
		System.out.println();
		System.out.println("Neighbour Test Started...");
		int errors = 0; /*Számoljuk a hibákat*/
		for (MapObject obj : objects) { /*Minden koordinátára megnézzük*/
			int objX = obj.getX();
			int objY = obj.getY();
			
			/*Innentöl azt figyeljük hogy ha van és ö nem a mi szomszédünk cím szerint akkor hibát kell kapnunk.
			
			/* Bal oldali szomszéd */
			if (objX > 0) {
				if (mapObjects[objX - 1][objY] != null && !obj.isNeighbour(mapObjects[objX - 1][objY])) {
					System.out.println("ERROR! " + obj.getAddress() + " has no neighbour called "
							+ mapObjects[objX - 1][objY].getAddress() + "!");
					errors++;
				}
			}
			/* Jobb oldali szomszéd */
			if (objX < mapX - 1) {
				if (mapObjects[objX + 1][objY] != null && !obj.isNeighbour(mapObjects[objX + 1][objY])) {
					System.out.println("ERROR! " + obj.getAddress() + " has no neighbour called "
							+ mapObjects[objX + 1][objY].getAddress() + "!");
					errors++;
				}
			}
			/* Felette lévö szomszéd */
			if (objY > 0) {
				if (mapObjects[objX][objY - 1] != null && !obj.isNeighbour(mapObjects[objX][objY - 1])) {
					System.out.println("ERROR! " + obj.getAddress() + " has no neighbour called "
							+ mapObjects[objX][objY - 1].getAddress() + "!");
					errors++;
				}
			}
			/* Alatta lévö szomszéd */
			if (objY < mapY - 1) {
				if (mapObjects[objX][objY + 1] != null && !obj.isNeighbour(mapObjects[objX][objY + 1])) {
					System.out.println("ERROR! " + obj.getAddress() + " has no neighbour called "
							+ mapObjects[objX][objY + 1].getAddress() + "!");
					errors++;
				}
			}
			;
		}
		/*Futás vége*/
		System.out.println("Neighbour Test Ended!");
		System.out.println("There were " + errors + " error(s)!");
	}

	/**
	 * Az osztálynév alapján megadja, a jelölését a szövegfájlban
	 */
	private static String getCharacterCode(String type) {
		switch (type) {
		case "EnterPoint": 		/*EnterPoint esetén a szövegbe E kerül*/
			return "E";
		case "Rail":			/*Rail esetén a szövegbe R kerül*/
			return "R";
		case "Switch":			/*Switch esetén a szövegbe S kerül*/
			return "S";			
		case "TunnelEntrance":	/*TunnelEntrance esetén a szövegbe U kerül*/
			return "U";
		case "XRail":			/*XRail esetén a szövegbe X kerül*/
			return "X";
		case "TrainStation1":	/*Piros TrainStaion esetén a szövegbe 1 kerül*/
			return "1";
		case "TrainStation2":	/*Zöld TrainStaion esetén a szövegbe 2 kerül*/
			return "2";
		case "TrainStation3":	/*Kék TrainStaion esetén a szövegbe 3 kerül*/
			return "3";
		default:				/*Ha egyik sem, akkor üres elem ami x*/
			return "x";
		}
	}
	
	/**
	 * Egy osztály, amiben a program futása közben generált osztályok minden adatát tároljuk
	 * Ezek segítségével vizsgáljuk a felépített pálya szabályosságát
	 * @author Long
	 */
	public static class MapObject {
		int x, y;						/*Pozícót tárol*/
		String type;					/*Osztálytípust tárol*/
		String address;					/*Pointert tárol*/
		ArrayList<String> neighbours;	/*Szomszédoakt tárolja*/

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
		 * Beállítja a koordinátáit
		 */
		public void setXY(int X, int Y) {
			x = X;
			y = Y;
		}
		
		/**
		 * Beállítja a típusát
		 * @param T
		 */
		public void setType(String T) {
			type = T;
		}
		/**
		 * Új szomszédot ad hozzá
		 * @param neighbour
		 */
		public void addNeighbour(String neighbour) {
			neighbours.add(neighbour);
		}
		/**
		 * Beállítja a memóriacmet
		 * @param adr
		 */
		public void setAddress(String adr) {
			address = adr;
		}
		/**
		 * Visszadja az X koordinátát
		 * @return
		 */
		public int getX() {
			return x;
		}
		/**
		 * Visszaadja az X koordinátát
		 * @return
		 */
		public int getY() {
			return y;
		}
		/**
		 * Visszaadja az osztálynevet
		 * @return
		 */
		public String getType() {
			return type;
		}
		/**
		 * Visszaadja a mamóriacímet
		 * @return
		 */
		public String getAddress() {
			return address;
		}
		
		/**
		 * Visszaadja a szomszédoakt
		 */
		
		public ArrayList<String> getNeighbours() {
			return neighbours;
		}
		
		/**
		 * Megnézi, hogy az adott objektum a szomszdja e
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
