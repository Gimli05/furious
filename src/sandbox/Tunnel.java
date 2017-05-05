package sandbox;


/**
 * Speciális sín ami két alagútszáj között helyezkedik el. Ez a sín mindenféle következmény nélkül keresztezhet más síneket. 
 */
public class Tunnel extends Rail {
	
	/** Az alagút konstruktora **/
	public Tunnel(){
		System.out.println("Class: Tunnel\t Object: "+this+"\t Method: Constructor\t "); /* Kiíratás a Szkeleton vezérlésének */
	}
	
	/**
	 * A visitor tervezési minta egyik függvénye. 
	 * Ennek segítségével Bármelyik sín típus könnyedén fogadhat látogatót, melyek ezen overloadolt függvény segítségével mindegyik végre tudja hajtani a saját funkcióját.
	 * 
	 * @param visitor A látogató, melyet fogadni tud.
	 */
	@Override
	public void accept(Visitor visitor){
		System.out.println("Class: Tunnel\t\t Object: "+this+"\t\t\t Method: Accept\t Param: "+visitor); /* Kiíratás a Szkeleton vezérlésének */
		visitor.visit(this); /* Elfogadjuk a visitort, és átadjuk magunkat, hogy nézzen meg minket. */
	}
	
}
