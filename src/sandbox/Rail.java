package sandbox;

import java.util.ArrayList;

/**
 * A pálya egy celláját kifoglaló sín, amin legfeljebb egy vonat elem állhat egyszerre. 
 * Ismeri a szomszédos cellákban elhelyezkedõ síneket. Két másik sínnel szomszédos. 
 * Vonat léptetéskor a vonatot mint visitort fogadja. Amennyiben egy vonat (vagyis egy visitort megvalósító osztály) valamely metódusa paraméterként megkapja ezt az osztályt, 
 * vagy egy leszármazottját, megtudható a getNextrRail metódus segítségével és a vonat previousRail attribútumával együtt, hogy a vonatnak merre kell továbblépnie.
 */
public class Rail {
	protected ArrayList<Rail> neighbourRails; /* Egy lista a szomszédos sínekre mutató pointerekrõl, mely az adott sín közvetlen közelében találhatóak. */
	protected int trainLenghtCounter; /* Ebben tároljuk, hogy hány kabin fog még áthaladni a sínen (mivel a Train entity csak 1 mezõt foglal el konkrétan) */
	protected int x,y; /*Pozício két koordinátája. Ez az alagút létrehozása miatt fontos, mivel akár több alagútszály is lehet, melyek között procedurálisan jön létre az alagút.*/
	
	/**
	 * A Rail konstruktora. Beállítjaa countert 0-ra.
	 */
	public Rail(){
		System.out.println("Class: Rail\t\t Object: "+this+"\t\t\t Method: Constructor\t"); /* Kiíratás a Szkeleton vezérlésének */
		trainLenghtCounter = 0; /* Létrehozásakor minden sín üres. */
		x=0; /*Origoba rakjuk*/
		y=0; /* y koordinátát is */
	}
	
	/**
	 *  X koordinátát kérjük le
	 */
	public int getX(){/* Visszatér az X koordinátával */
		return x;
	}
		
	
	/**
	 *  Y koordinátát kérjük le
	 */
	public int getY(){ /* Visszatér az Y koordinátával */
		return y;
	}
	
	
	/**
	 * X koordinátát beállító függvény.
	 * @param x2	az új X koordináta
	 */
	public void setX(int x2) {
		x=x2;
		
	}
	
	
	/**
	 * Y koordinátát beállító függvény.
	 * @param y2	az új Y koordináta
	 */
	public void setY(int y2){
		y=y2;
	}
	
	
	/**
	 * A vonat a visitje során meghívja ezt a függvényt, mely a vonat elõzõ pozíciója alapján egyértelmûen megadja melyik sínre kell tovább mennie. 
	 * Ezt a vonat eltárolja, és következõ körben oda fog majd lépni.
	 * 
	 * @param trainPreviousRail	Az elõzõ sín, ahol a vonat állt.
	 * @return	A következõ sín ahova lépni fog a vonat.
	 */
	public Rail getNextRail(Rail trainPreviousRail){
		System.out.println("Class: Rail\t\t Object: "+this+"\t\t\t Method: getNextRail\t Param: "+trainPreviousRail+"\t A kovetkezo sin."); /* Kiíratás a Szkeleton vezérlésének */
		for(Rail oneNeighbourRail:neighbourRails){ /* Végignézzük az összes sínt, és amelyik nem az érkezõ sín, az a következõ sín, mivel csak 2 szomszédja van egy sima sínnek */
			if(trainPreviousRail!=oneNeighbourRail){ /*visszaadjuk azt, amelyik nem az érkezõ sín*/
				System.out.println("Class: Rail\t\t Object: "+this+"\t\t\t Returned: "+oneNeighbourRail); /* Kiíratás a Szkeleton vezérlésének */
				return oneNeighbourRail;
			}
		}
		System.out.println("Class: Rail\t\t Object: "+this+"\t\t\t Returned: null"); /* Kiíratás a Szkeleton vezérlésének */
		return null; /* Ha nincs hova lépnie, akkor null-t adunk vissza */

	}
	
	
	/**
	 * A visitor tervezési minta egyik függvénye. 
	 * Ennek segítségével Bármelyik sín típus könnyedén fogadhat látogatót, melyek ezen overloadolt függvény segítségével mindegyik végre tudja hajtani a saját funkcióját.
	 * 
	 * @param visitor A látogató, melyet fogadni tud.
	 */
	public void accept(Visitor visitor){
		System.out.println("Class: Rail\t\t Object: "+this+"\t\t\t Method: Accept\t Param: "+visitor); /* Kiíratás a Szkeleton vezérlésének */
		visitor.visit(this); /* Elfogadjuk a visitort, és átadjuk magunkat, hogy nézzen meg minket. */
	}
	
	
	/**
	 * Ellenõrzi, hogy van-e még vonat a sínen. 
	 * Ha van, akkor true-val, ha nincs akkor false-al tér vissza.
	 * 
	 * @return	Foglalt-e a sín.
	 */
	public boolean checkIfOccupied(){
		System.out.println("Class: Rail\t\t Object: "+this+"\t\t\t Method: checkIfOccupied\t Van-e még vonat a sínen?");/* Kiíratás a Szkeleton vezérlésének */
		if(trainLenghtCounter == 0){ /* Ha nincs már rajta egyetlen cab-sem akkor false-al tér vissza egyébként true-val. */
			System.out.println("Class: Rail\t\t Object: "+this+"\t\t\t Returned: false");/* Kiíratás a Szkeleton vezérlésének */
			return false;
		} else {
			System.out.println("Class: Rail\t\t Object: "+this+"\t\t\t Returned: true");/* Kiíratás a Szkeleton vezérlésének */
			return true;
		}
	}
	
	
	/**
	 * Miután minden Rail-t létrehozutnk, beállítjuk melyik az adott sínnek a szomszédja.
	 * 
	 * @param newNeighbourRails	Az adott sín szomszéjdai.
	 */
	public void setNeighbourRails(ArrayList<Rail> newNeighbourRails){
		System.out.println("Class: Rail\t\t Object: "+this+"\t\t\t Method: setNeighbourRails\t Param: "+newNeighbourRails);/* Kiíratás a Szkeleton vezérlésének */
		neighbourRails = newNeighbourRails; /* beállítjuk az új szomszéd listát */
	}
	
	
	/**
	 * A tunnel létrehozása miatt szükség van on the fly hozzáadni rail-eket.
	 * Ez a függvény ezt a célt szolgálja.
	 * 
	 * @param newNeighbourRail Új szomszéd
	 */
	public void addNeighbourRail(Rail newNeighbourRail){
		System.out.println("Class: Rail\t\t Object: "+this+"\t\t\t Method: setNeighbourRails\t Param: "+newNeighbourRail);/* Kiíratás a Szkeleton vezérlésének */
		if(neighbourRails==null)neighbourRails=new ArrayList<Rail>(); /* Hozzáadjuk a már létezõ (vagy most létrehozott) listához egy új szomszédot */
		neighbourRails.add(newNeighbourRail);
	}
	
	
	/**
	 * Csökkenti a trainLenghtCountert
	 * Minden léptetés során csökkentjük egyel a számlálót, hiszen már elhaladt egy kabin.
	 */
	public void lowerTrainLenghtCounter(){
		System.out.println("Class: Rail\t\t Object: "+this+"\t\t\t Method: lowerTrainLenghtCounter\t A vagon továbbhalad a sínen."); /* Kiíratás a Szkeleton vezérlésének */
		if(trainLenghtCounter > 0){ /* Ha még nem nulla a számláló, azaz van még kabin a sínen, akkor csökkentjük egyel, hiszen most haladt tovább */
			trainLenghtCounter--; 
		}
	}
	
	
	/**
	 * A vonat áthaladásakor beállítjuk, hogy milyen hosszú a vonat, ami áthalad rajta, azaz hány léptetés ciklusig fog még folglat lenni az adott sín.
	 * 
	 * @param newCounter	A vonat hossza.
	 */
	public void setTrainLenghtCounter(int newCounter){
		System.out.println("Class: Rail\t\t Object: "+this+"\t\t\t Method: setTrainLenghtCounter\t Param: "+newCounter+"\t Vonat lépett a sínre."); /* Kiíratás a Szkeleton vezérlésének */
		trainLenghtCounter = newCounter; /* beállítjuk az új számlálót, hogy még hány kabin kell áthaladjon az adott sínen. Ennyi léptetés ciklusig lesz foglalt. */
	}
}
