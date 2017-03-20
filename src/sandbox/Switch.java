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
	public Switch(ArrayList<Rail> neighbourRails){
		super(neighbourRails);
		state = false;
	}

	
	
	/**
	 * !!!MIVEL A JAVABAN VAN ALAP SWITCH EZÉRT ÁT KELLET ÍRNOM SWITCHRAIL-RE A FÜGGVÉNYNEVET!!!!
	 * A váltó irányának megváltoztatására szolgál. A játékos aktiválja a váltót, melyre ez a függvény hívódik meg Ha eddig a state true-volt akkor most false lesz, 
	 * ha false volt akkor pedig true. Ezzel megvalósul, hogy ha eddig megváltoztatta az irányt a váltó, akkor most pont át fogja engedni egyenesen, ha eddig átengedte, most elkanyarítja.
	 */
	public void switchRail(){ /*Eredetileg switch() */
		state= !state;
	}
	
	
	@Override
	public Rail getNextRail(Rail trainPreviousRail){
		//TODO: kitÃ¶lteni
		//TODO: statetÅ‘l fÃ¼ggÅ‘en az elsÅ‘ vagy a mÃ¡sodik kell
		int count=state?1:0; //(count 1 ha vÃ¡lt Ã©s 0 ha nem, annyiadikat adjuk vissza a sorban)
		for(Rail r:neighbourRails){
			if(trainPreviousRail!=r){
				if(count==0){
					return r; //Ez kell nekÃ¼nk
				}else{
					count--; //Egyet kihagyunk
				}		
			}
		}
		return null; //Ha nem lenne semmi
	}

}
