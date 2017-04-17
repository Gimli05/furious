package sandbox;

import java.util.ArrayList;

/**
 * Speciális váltó. Egyszerre csak kettõ lehet aktív. A felhasználó határozza meg, hogy a pályán melyik két alagútszáj legyen aktív. 
 * Ha van két aktív alagútszáj a pályán, akkor váltóként mûködik, aminek a két lehetséges állása van. Az egyik az eddig is létezõ irány, a másik a létrejött alagút felé mutat.
 */
public class TunnelEntrance extends Switch{
	
	/**
	 * Megadja, hogy az adott alagút száj aktiválva van-e. Ha nincs aktiválva, akkor false az értéke, ha aktiválva van, akkor true. 
	 * Amíg inaktív, addig egyszerû sínként mûködik az alagút száj, és tovább engedi egyenes irányba az érkezõ vonatokat. 
	 * Amint két alagútszájat aktivál a játékos, a két alagútszájat alagút köti össze. Amikor a felhasználó aktiválja, lefut az activate() függvény, 
	 * mely hatására a bool igazzá válik. Aktív állapotban a funkcióját tekintve váltóként üzemel, ugyanis ebben az esetben egyenesen, 
	 * vagy oldalra is el tudja irányítani a vonatot (bele az alagútba).
	 */
	private Boolean isActivated; 
	
	/**
	 * TunnelEntrance konstruktora. 
	 * alapértelmezetten egyik alagútszáj sincs aktiválva.
	 */
	public TunnelEntrance(){
		System.out.println("Class: TunnelEntrance\t Object: "+this+"\t Method: Constructor\t"); /* Kiíratás a Szkeleton vezérlésének */
		isActivated = false;
	}
	
	/**
	 * Aktiválja az alagút szájat, azaz igazzá teszi az isActivated változót. Innentõl kezdve a játékos a switch() metódus segítségével tudja kiválasztani merre menjen a vonat.
	 */
	public void activate(){
		System.out.println("Class: TunnelEntrance\t Object: "+this+"\t Method: Activate\t Aktivalta az alagutszajat"); /* Kiíratás a Szkeleton vezérlésének */
		isActivated = true; /* aktiváljuk az alagútszájat */
	}
	
	
	/**
	 * Deaktiválja az alagút szájat. Deaktiválás után ismételten csak egyenesen haladhat át a vonat az adott sínen.
	 * Ugyan ez a függvény felelõs azért, hogy törölje a szomszédok listájából a megszûnt tunnelt.
	 */
	public void deActivate(){
		System.out.println("Class: TunnelEntrance\t Object: "+this+"\t Method: Deactivate\t Deaktivalta az alagutszajat"); /* Kiíratás a Szkeleton vezérlésének */
		isActivated = false; /* Deaktiváljuk az alagútszájat. */
		
		Rail neighbourRailToGetDeleted = null; /* Ebben fogjuk eltárolni a törlendõ Tunnel szomszédot. */
		for(Rail rail:neighbourRails){  /* végignézünk minden szomszédot */
			if(rail.getClass() == Tunnel.class){ /* amelyik tunnel, azt megjelöljük, mint törlendõ szomszédot. */
				neighbourRailToGetDeleted = rail;
			}
		}
		neighbourRails.remove(neighbourRailToGetDeleted); /* Töröljük a megjelölt szomszédot, mivel metszûtn a  */
	}
	
	
	/**
	 * Megmondja, hogy aktív-e az alagútszáj.
	 * 
	 * @return	Aktív-e az alagútszáj.
	 */
	public Boolean checkIfActivated(){
		System.out.println("Class: TunnelEntrance\t Object: "+this+"\t Method: checkIfActivated\t Ellenorzi aktiv-e az alagutszaj."); /* Kiíratás a Szkeleton vezérlésének */
		System.out.println("Class: TunnelEntrance\t Object: "+this+"\t Returned: "+isActivated); /* Kiíratás a Szkeleton vezérlésének */
		return isActivated;
	}
	
	
	/**
	 * A visitor tervezési minta egyik függvénye. 
	 * Ennek segítségével Bármelyik sín típus könnyedén fogadhat látogatót, melyek ezen overloadolt függvény segítségével mindegyik végre tudja hajtani a saját funkcióját.
	 * 
	 * @param visitor A látogató, melyet fogadni tud.
	 */
	@Override
	public void accept(Visitor visitor){
		System.out.println("Class: TunnelEntrance\t\t Object: "+this+"\t\t\t Method: Accept\t Param: "+visitor); /* Kiíratás a Szkeleton vezérlésének */
		visitor.visit(this); /* Elfogadjuk a visitort, és átadjuk magunkat, hogy nézzen meg minket. */
	}
	
}
