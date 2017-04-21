package sandbox;

import java.util.ArrayList;

/**
 * Speciális sín, két lehetséges továbbhaladási iránnyal. Összesen 3 másik sínnel szomszédos. 
 * A felhasználó tudja beállítani, hogy a két lehetséges továbbhaladási irány közül melyik aktív. 
 * Visitor fogadásánál ezt felhasználva mondja meg, hogy az õsbõl származó (de felülírt) getNextRail metódusa mivel tér vissza.
 */
public class Switch extends Rail{
	
	/**
	 * A váltó állapotát tároló változó. A váltónak két állapota lehet: módosítja az irányt, vagy nem módosítja azt. 
	 * Ha módosítja, akkor true az értéke, ha nem módosítja az irány, akkor false. Ha módosítja az irányt, 
	 * akkor a menetirány szerinti jobb vagy bal oldalra irányítja át a vonatot, ha nem módosítja, akkor egyenesen küldi tovább.
	 */
	protected Boolean state;
	
	
	/**
	 * A váltó konstruktora.
	 * Alap értelmezetten nem módosít a vonat irányán.
	 */
	public Switch(){
		System.out.println("Class: Switch\t\t Object: "+this+"\t\t Method: Constructor\t"); /* Kiíratás a Szkeleton vezérlésének */
		state = false;/* alapértelmezetten továbbengedi a vonatot eredeti irányába. */
	}

	
	
	/**
	 * !!!MIVEL A JAVABAN VAN ALAP SWITCH EZÉRT ÁT KELLET ÍRNOM SWITCHRAIL-RE A FÜGGVÉNYNEVET!!!!
	 * A váltó irányának megváltoztatására szolgál. A játékos aktiválja a váltót, melyre ez a függvény hívódik meg Ha eddig a state true-volt akkor most false lesz, 
	 * ha false volt akkor pedig true. Ezzel megvalósul, hogy ha eddig megváltoztatta az irányt a váltó, akkor most pont át fogja engedni egyenesen, ha eddig átengedte, most elkanyarítja.
	 */
	public void switchRail(){ /* Eredetileg switch() */
		System.out.println("Class: Switch\t\t Object: "+this+"\t\t Method: switchRail\t Változik a továbbhaladási irány"); /* Kiíratás a Szkeleton vezérlésének */
		state = !state; /* Negáljuk az állapotot */
		System.out.println("Class: Switch\t\t Object: "+this+"\t\t New state: "+state); /* Kiíratás a Szkeleton vezérlésének */
	}
	
	
	//LONG MOKOLTA DE IGY JOBB
	@Override
	public Rail getNextRail(Rail trainPreviousRail){
		System.out.println("Class: Switch\t\t Object: "+this+"\t Method: getNextRail\t Param: "+trainPreviousRail+"\t A következö sín."); /* Kiíratás a Szkeleton vezérlésének */		
		
		Rail n2=null;
		Rail n3=null;
		
		for(Rail oneNeighbourRail:neighbourRails){
			if(trainPreviousRail!=oneNeighbourRail){
				if(n2==null)n2=oneNeighbourRail;
				else n3=oneNeighbourRail;
			}
		}
		
		if(state){
			if(trainPreviousRail.getX()==n2.getX() || trainPreviousRail.getY()==n2.getY())return n3;
			if(n2.getX()==n2.getX() || n3.getY()==n3.getY())return n3;
			if(trainPreviousRail.getX()==n3.getX() || trainPreviousRail.getY()==n3.getY())return n2;
			
		}else{
			if(trainPreviousRail.getX()==n2.getX() || trainPreviousRail.getY()==n2.getY())return n2;
			if(n2.getX()==n2.getX() || n3.getY()==n3.getY())return n2;
			if(trainPreviousRail.getX()==n3.getX() || trainPreviousRail.getY()==n3.getY())return n3;
		}
		return null;
	}
	
	public boolean getGUIState(){
		return state;
	}
	//LONG EDDIG MOKOLTA
	
	/**
	 * A visitor tervezési minta egyik függvénye. 
	 * Ennek segítségével Bármelyik sín típus könnyedén fogadhat látogatót, melyek ezen overloadolt függvény segítségével mindegyik végre tudja hajtani a saját funkcióját.
	 * 
	 * @param visitor A látogató, melyet fogadni tud.
	 */
	@Override
	public void accept(Visitor visitor){
		System.out.println("Class: Switch\t\t Object: "+this+"\t\t\t Method: Accept\t Param: "+visitor); /* Kiíratás a Szkeleton vezérlésének */
		visitor.visit(this); /* Elfogadjuk a visitort, és átadjuk magunkat, hogy nézzen meg minket. */
	}

}
