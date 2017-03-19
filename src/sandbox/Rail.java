package sandbox;

import java.util.ArrayList;

/**
 * A pálya egy celláját kifoglaló sín, amin legfeljebb egy vonat elem állhat egyszerre. 
 * Ismeri a szomszédos cellákban elhelyezkedõ síneket. Két másik sínnel szomszédos. 
 * Vonat léptetéskor a vonatot mint visitort fogadja. Amennyiben egy vonat (vagyis egy visitort megvalósító osztály) valamely metódusa paraméterként megkapja ezt az osztályt, 
 * vagy egy leszármazottját, megtudható a getNextrRail metódus segítségével és a vonat previousRail attribútumával együtt, hogy a vonatnak merre kell továbblépnie.
 */
public class Rail {
	private ArrayList<Rail> neighbourRails; /*Egy lista a szomszédos sínekre mutató pointerekrõl, mely az adott sín közvetlen közelében találhatóak. */
	
	
	//TODO: konstruktor?
	
	
	
	/**
	 * A vonat a visitje során meghívja ezt a függvényt, mely a vonat elõzõ pozíciója alapján egyértelmûen megadja melyik sínre kell tovább mennie. 
	 * Ezt a vonat eltárolja, és következõ körben oda fog majd lépni.
	 * 
	 * @param trainPreviousRail	Az elõzõ sín, ahol a vonat állt.
	 * @return	A következõ sín ahova lépni fog a vonat.
	 */
	public Rail getNextRail(Rail trainPreviousRail){
		//TODO: kitölteni
		return null;
	}
	
	
	/**
	 * A visitor tervezési minta egyik függvénye. 
	 * Ennek segítségével Bármelyik sín típus könnyedén fogadhat látogatót, melyek ezen overloadolt függvény segítségével mindegyik végre tudja hajtani a saját funkcióját.
	 * 
	 * @param visitor A látogató, melyet fogadni tud.
	 */
	public void accept(Visitor visitor){
		//TODO: kitölteni.
	}
}
