package sandbox;

import java.io.BufferedReader;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class MapCreationTest {
	private static ArrayList<MapObject> objects = new ArrayList<MapObject>();
	private static int mapX; /* P�ly�nk sz�less�ge */
	private static int mapY; /* P�ly�nk magass�ga */
	private static String[][] mapView; /* P�lya kin�zete */
	private static MapObject[][] mapObjects; /* P�lya elemei */	
	private static String newMapFileName = "LastMapView.txt";
	
public static void main(String originalMapFileName, String saveFileName) throws IOException {
		System.out.println();
		System.out.println("Test Running...");
		readMapTest(saveFileName);
		createMap();
		reconstructionTest(originalMapFileName);
		neighbourTest();
		System.out.println();
		System.out.println("Test Finished!");
		System.out.println();
	}

	private static void readMapTest(String fileName) throws NumberFormatException, IOException {
		System.out.println();
		System.out.println("Map Reading Test Started...");

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

	private static void createMap() throws IOException {
		System.out.println();
		System.out.println("Creatin Map View File...");
		mapView = new String[mapX][mapY];
		mapObjects = new MapObject[mapX][mapY];

		/* �res p�ly�t hozunk l�tre */
		for (int x = 0; x < mapX; x++) {
			for (int y = 0; y < mapY; y++) {
				mapView[x][y] = "x";
				mapObjects[x][y] = null;
			}
		}
		System.out.println("Map Size: " + mapX + "x" + mapY);
		/* Kit�ltj�k a p�ly�t */
		for (MapObject obj : objects) {
			mapView[obj.getX()][obj.getY()] = getCharacterCode(obj.getType());
			mapObjects[obj.getX()][obj.getY()] = obj;
		}

		/* Kimentj�k egy f�jlba hogy l�thassuk is */
		FileWriter fw = new FileWriter(newMapFileName);
		PrintWriter writer = new PrintWriter(fw);

		/* M�ret beir�sa */
		writer.print(mapX + ";" + mapY);

		/* Elemek beir�sa */
		for (int y = 0; y < mapY; y++) {
			writer.println();
			for (int x = 0; x < mapX; x++) {
				writer.print(mapView[x][y]);
			}
		}
		writer.flush();
		writer.close();
		System.out.println("Map View File Created!");
	}

	private static void reconstructionTest(String original) throws IOException {
		System.out.println();
		System.out.println("Reconstruction Test Started...");

		FileReader Oreader = new FileReader(original);
		BufferedReader Obr = new BufferedReader(Oreader);

		FileReader Nreader = new FileReader(newMapFileName);
		BufferedReader Nbr = new BufferedReader(Nreader);

		String OldLine, NewLine;
		while ((OldLine = Obr.readLine()) != null && (NewLine = Nbr.readLine()) != null) {
			if (!OldLine.equals(NewLine)) {
				System.out.println("The two maps do not match!");
				System.out.println("Reconstruction Test Failed!");
				Obr.close();
				Nbr.close();
				return;
			}
		}
		Obr.close();
		Nbr.close();
		System.out.println("The two maps match!");
		System.out.println("Reconstruction Test Successful!");
	}

	private static void neighbourTest() {
		System.out.println();
		System.out.println("Neighbour Test Started...");
		int errors = 0;
		for (MapObject obj : objects) {
			int objX = obj.getX();
			int objY = obj.getY();

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

		System.out.println("Neighbour Test Ended!");
		System.out.println("There were " + errors + " error(s)!");
	}

	private static String getCharacterCode(String type) {
		switch (type) {
		case "EnterPoint":
			return "E";
		case "Rail":
			return "R";
		case "Switch":
			return "S";
		case "TunnelEntrance":
			return "U";
		case "XRail":
			return "X";
		case "TrainStation1":
			return "1";
		case "TrainStation2":
			return "2";
		case "TrainStation3":
			return "3";
		default:
			return "x";
		}
	}

	public static class MapObject {
		int x, y;
		String type;
		String address;
		ArrayList<String> neighbours;

		public MapObject() {
			neighbours = new ArrayList<String>();
		}

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

		public void setXY(int X, int Y) {
			x = X;
			y = Y;
		}

		public void setType(String T) {
			type = T;
		}

		public void addNeighbour(String neighbour) {
			neighbours.add(neighbour);
		}

		public void setAddress(String adr) {
			address = adr;
		}

		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}

		public String getType() {
			return type;
		}

		public String getAddress() {
			return address;
		}

		public ArrayList<String> getNeighbours() {
			return neighbours;
		}

		public boolean isNeighbour(MapObject neigh) {
			for (String neighbour : neighbours) {
				if (neighbour.equals(neigh.getAddress()))
					return true;
			}
			return false;
		}
	}
}
