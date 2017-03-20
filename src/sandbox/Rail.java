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
	
	
	/**
	 * A Rail konstruktora. Beállítjaa countert 0-ra.
	 */
	public Rail(){
		trainLenghtCounter = 0; /* Létrehozásakor minden sín üres. */
	}
	
	
	/**
	 * A vonat a visitje során meghívja ezt a függvényt, mely a vonat elõzõ pozíciója alapján egyértelmûen megadja melyik sínre kell tovább mennie. 
	 * Ezt a vonat eltárolja, és következõ körben oda fog majd lépni.
	 * 
	 * @param trainPreviousRail	Az elõzõ sín, ahol a vonat állt.
	 * @return	A következõ sín ahova lépni fog a vonat.
	 */
	public Rail getNextRail(Rail trainPreviousRail){
		for(Rail oneNeighbourRail:neighbourRails){ /* Végignézzük az összes sínt, és amelyik nem az érkezõ sín, az a következõ sín, mivel csak 2 szomszédja van egy sima sínnek */
			if(trainPreviousRail!=oneNeighbourRail){
				return oneNeighbourRail;
			}
		}
		return null; /* Ha nincs hova lépnie, akkor null-t adunk vissza */

	}
	
	
	/**
	 * A visitor tervezési minta egyik függvénye. 
	 * Ennek segítségével Bármelyik sín típus könnyedén fogadhat látogatót, melyek ezen overloadolt függvény segítségével mindegyik végre tudja hajtani a saját funkcióját.
	 * 
	 * @param visitor A látogató, melyet fogadni tud.
	 */
	public void accept(Visitor visitor){
		visitor.visit(this); /* Elfogadjuk a visitort, és átadjuk magunkat, hogy nézzen meg minket. */
	}
	
	
	/**
	 * Ellenõrzi, hogy van-e még vonat a sínen. 
	 * Ha van, akkor true-val, ha nincs akkor false-al tér vissza.
	 * 
	 * @return	Foglalt-e a sín.
	 */
	public boolean checkIfOccupied(){
		if(trainLenghtCounter == 0){ /* Ha nincs már rajta egyetlen cab-sem akkor false-al tér vissza egyébként true-val. */
			return false;
		} else {
			return true;
		}
	}
	
	
	/**
	 * Miután minden Rail-t létrehozutnk, beállítjuk melyik az adott sínnek a szomszédja.
	 * 
	 * @param newNeighbourRails	Az adott sín szomszéjdai.
	 */
	public void setNeighbourRails(ArrayList<Rail> newNeighbourRails){
		neighbourRails = newNeighbourRails;
	}
	
	
	/**
	 * Csökkenti a trainLenghtCountert
	 * Minden léptetés során csökkentjük egyel a számlálót, hiszen már elhaladt egy kabin.
	 */
	public void lowerTrainLenghtCounter(){
		trainLenghtCounter--;
	}
}
