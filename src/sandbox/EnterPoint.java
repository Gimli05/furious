package sandbox;

import java.util.ArrayList;

/**
 * peciális sín. A GameController ezen keresztül ad új vonatokat a játékhoz. 
 * Ha egy vonat a pályát elhagyva halad át rajta úgy, hogy a vonat tartalmaz utasokat szállító (nem üres) vagonokat, 
 * a szerelvény egy Michael Bay Effekt keretében felrobban, és a játékos veszít.
 */
public class EnterPoint extends Rail {
	public EnterPoint(){
		System.out.println("Class: EnterPoint\t Object: "+this+"\t\t Method: Constructor\t ");
	}
}
