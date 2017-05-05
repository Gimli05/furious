package sandbox;


/**
 * peciális sín. A GameController ezen keresztül ad új vonatokat a játékhoz. 
 * Ha egy vonat a pályát elhagyva halad át rajta úgy, hogy a vonat tartalmaz utasokat szállító (nem üres) vagonokat, 
 * a szerelvény egy Michael Bay Effekt keretében felrobban, és a játékos veszít.
 */
public class EnterPoint extends Rail {
	
	/** Belépési pont konstruktora **/
	public EnterPoint(){
		System.out.println("Class: EnterPoint\t Object: "+this+"\t\t Method: Constructor\t "); /* Kiíratás a Szkeleton vezérlésének */
	}
	
	/**
	 * A visitor tervezési minta egyik függvénye. 
	 * Ennek segítségével Bármelyik sín típus könnyedén fogadhat látogatót, melyek ezen overloadolt függvény segítségével mindegyik végre tudja hajtani a saját funkcióját.
	 * 
	 * @param visitor A látogató, melyet fogadni tud.
	 */
	@Override
	public void accept(Visitor visitor){
		System.out.println("Class: EnterPoint\t\t Object: "+this+"\t\t\t Method: Accept\t Param: "+visitor); /* Kiíratás a Szkeleton vezérlésének */
		visitor.visit(this); /* Elfogadjuk a visitort, és átadjuk magunkat, hogy nézzen meg minket. */
	}
}
