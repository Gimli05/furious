package sandbox;

import java.util.ArrayList;

/**
 * Speciális sín, két lehetséges továbbhaladási iránnyal. Összesen 3 másik sínnel szomszédos. 
 * A felhasználó tudja beállítani, hogy a két lehetséges továbbhaladási irány közül melyik aktív. 
 * Visitor fogadásánál ezt felhasználva mondja meg, hogy az õsbõl származó (de felülírt) getNextRail metódusa mivel tér vissza.
 */
public class Switch extends Rail{
	private Boolean state; /* A váltó állapotát tároló változó. A váltónak két állapota lehet: módosítja az irányt, vagy nem módosítja azt. 
							* Ha módosítja, akkor true az értéke, ha nem módosítja az irány, akkor false. Ha módosítja az irányt, 
							* akkor a menetirány szerinti jobb vagy bal oldalra irányítja át a vonatot, ha nem módosítja, akkor egyenesen küldi tovább. */
	
	
	/**
	 * A váltó konstruktora.
	 * Alap értelmezetten nem módosít a vonat irányán.
	 */
	public Switch(){
		System.out.println("Class: Switch\t Method: Constructor\t Param: -");
		state = false;
	}

	
	
	/**
	 * !!!MIVEL A JAVABAN VAN ALAP SWITCH EZÉRT ÁT KELLET ÍRNOM SWITCHRAIL-RE A FÜGGVÉNYNEVET!!!!
	 * A váltó irányának megváltoztatására szolgál. A játékos aktiválja a váltót, melyre ez a függvény hívódik meg Ha eddig a state true-volt akkor most false lesz, 
	 * ha false volt akkor pedig true. Ezzel megvalósul, hogy ha eddig megváltoztatta az irányt a váltó, akkor most pont át fogja engedni egyenesen, ha eddig átengedte, most elkanyarítja.
	 */
	public void switchRail(){ /* Eredetileg switch() */
		System.out.println("Class: Switch\t Method: switchRail\t Param: -\t Változik a továbbhaladási irány");
		state = !state;
	}
	
	
	@Override
	public Rail getNextRail(Rail trainPreviousRail){
		System.out.println("Class: Switch overriding Rail\t Method: getNextRail\t Param: Rail trainPreviousRail\t A következő sín.");
		int counter=state?1:0; /* Ha módosítani akarjuk az irányt, akkor a count 1-lesz, ha nem akarjuk módosítani, akkor nulla */
		
		for(Rail oneNeighbourRail:neighbourRails){/* Minden sínt végignézünk. */
			if(trainPreviousRail!=oneNeighbourRail){ /* Ha nem az a sín amit nézünk, ahonnan jött a vonat */
				if(counter==0){
					return oneNeighbourRail; /* A counter 0, azaz ezt a sínt kell visszaadjuk. */
				} else {
					counter--; /* A counter még nem nulla, így nem ezt kell visszaadnunk. */
				}		
			}
		}
		return null; /* Nem találtunk következő sínt, így null-al tértünk vissza. */
	}

}
