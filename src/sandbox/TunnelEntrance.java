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
	
	
	public TunnelEntrance(){
		System.out.println("Class: TunnelEntrance\t Object: "+this+"\t Method: Constructor\t");
		isActivated = false;
	}
	
	/**
	 * Aktiválja az alagút szájat, azaz igazzá teszi az isActivated változót. Innentõl kezdve a játékos a switch() metódus segítségével tudja kiválasztani merre menjen a vonat.
	 */
	public void activate(){
		System.out.println("Class: TunnelEntrance\t Object: "+this+"\t Method: Activate\t Aktivalta az alagutszajat");
		isActivated = true;
	}
	
	
	/**
	 * Deaktiválja az alagút szájat. Deaktiválás után ismételten csak egyenesen haladhat át a vonat az adott sínen.
	 */
	public void deActivate(){
		System.out.println("Class: TunnelEntrance\t Object: "+this+"\t Method: Deactivate\t Deaktivalta az alagutszajat");
		isActivated = false;
	}
	
	
	/**
	 * Megmondja, hogy aktív-e az alagútszáj.
	 * @return	Aktív-e az alagútszáj.
	 */
	public Boolean checkIfActivated(){
		System.out.println("Class: TunnelEntrance\t Object: "+this+"\t Method: checkIfActivated\t Ellenorzi aktiv-e az alagutszaj.");
		System.out.println("Class: TunnelEntrance\t Object: "+this+"\t Returned: "+isActivated);
		return isActivated;
	}
	
}
