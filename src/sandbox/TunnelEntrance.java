package sandbox;

import java.util.ArrayList;

/**
 * Speciális váltó. Egyszerre csak kettõ lehet aktív. A felhasználó határozza meg, hogy a pályán melyik két alagútszáj legyen aktív. 
 * Ha van két aktív alagútszáj a pályán, akkor váltóként mûködik, aminek a két lehetséges állása van. Az egyik az eddig is létezõ irány, a másik a létrejött alagút felé mutat.
 */
public class TunnelEntrance extends Switch{
	private Boolean isActivated; /* Megadja, hogy az adott alagút száj aktiválva van-e. Ha nincs aktiválva, akkor false az értéke, ha aktiválva van, akkor true. 
								  * Amíg inaktív, addig egyszerû sínként mûködik az alagút száj, és tovább engedi egyenes irányba az érkezõ vonatokat. 
								  * Amint két alagútszájat aktivál a játékos, a két alagútszájat alagút köti össze. Amikor a felhasználó aktiválja, lefut az activate() függvény, 
								  * mely hatására a bool igazzá válik. Aktív állapotban a funkcióját tekintve váltóként üzemel, ugyanis ebben az esetben egyenesen, 
								  * vagy oldalra is el tudja irányítani a vonatot (bele az alagútba).*/
	
	
	/**
	 * Aktiválja az alagút szájat, azaz igazzá teszi az isActivated változót. Innentõl kezdve a játékos a switch() metódus segítségével tudja kiválasztani merre menjen a vonat.
	 */
	public void activate(){
		System.out.println("Class: TunnelEntrance\t Method: Activate\t Param: -\t Aktiválta az alagútszájat");
		isActivated = true;
	}
	
	
	/**
	 * Deaktiválja az alagút szájat. Deaktiválás után ismételten csak egyenesen haladhat át a vonat az adott sínen.
	 */
	public void deActivate(){
		System.out.println("Class: TunnelEntrance\t Method: Deactivate\t Param: -\t Deaktiválta az alagútszájat");
		isActivated = false;
	}
	
}
