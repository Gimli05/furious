package sandbox;


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
		state=false;
	}
	
	
	/**
	 * Deaktiválja az alagút szájat. Deaktiválás után ismételten csak egyenesen haladhat át a vonat az adott sínen.
	 * Ugyan ez a függvény felelõs azért, hogy törölje a szomszédok listájából a megszûnt tunnelt.
	 */
	public void deActivate(){
		System.out.println("Class: TunnelEntrance\t Object: "+this+"\t Method: Deactivate\t Deaktivalta az alagutszajat"); /* Kiíratás a Szkeleton vezérlésének */
		isActivated = false; /* Deaktiváljuk az alagútszájat. */
		state=false;
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
		
		if(isActivated){
			//Ha aktív akkor egy sima váltó, amiben már fel van építve az alagut
			Rail np = trainPreviousRail;
			
			
			for (Rail oneNeighbourRail : neighbourRails) {
				if (trainPreviousRail != oneNeighbourRail) {
					if (n2 == null)
						n2 = oneNeighbourRail;
					else
						n3 = oneNeighbourRail;
				}
			}
			
			//Függöleges tagozodás
			if (np.getX() == n2.getX()) {
				if (n3.getX() > n2.getX()) {
					if (!state) {
						return n2;
					} else {
						return n3;
					}
				} else {
					if (!state) {
						return n2;
					} else {
						return n3;
					}
				}
			} else if (n2.getX() == n3.getX()) {
				if (np.getX() > n3.getX()) {
					if (!state) {
						if(n2.getY()>n3.getY())return n3;
						else return n2;
					} else {
						if(n2.getY()>n3.getY())return n2;
						else return n3;
					}
				} else {
					if (!state) {
						if(n2.getY()>n3.getY())return n3;
						else return n3;
					} else {
						if(n2.getY()>n3.getY())return n3;
						else return n2;
					}
				}
			} else if (np.getX() == n3.getX()) {
				if (n2.getX() > n3.getX()) {
					if (!state) {
						return n3;
					} else {
						return n2;
					}
				} else {
					if (!state) {
						return n3;
					} else {
						return n2;
					}
				}
			}
			//vizszintes tagozodás
			if (np.getY() == n2.getY()) {
				if (n3.getY() < n2.getY()) {
					if (!state) {
						return n2;
					} else {
						return n3;
					}
				} else {
					if (!state) {
						return n2;
					} else {
						return n3;
					}
				}
			} else if (n2.getY() == n3.getY()) {
				if (np.getY() < n3.getY()) {
					if (!state) {
						
						if(n2.getX()>n3.getX())return n3;
						else return n2;
					} else {
						if(n2.getX()>n3.getX())return n2;
						else return n3;
					}
				} else {
					if (!state) {
						if(n2.getX()>n3.getX())return n2;
						else return n3;
					} else {
						if(n2.getX()>n3.getX())return n3;
						else return n2;
					}
				}
			} else if (np.getY() == n3.getY()) {
				if (n2.getY() < n3.getY()) {
					if (!state) {
						return n3;
					} else {
						return n2;
					}
				} else {
					if (!state) {
						return n3;
					} else {
						return n2;
					}
				}
			}
			
		}else{
			//Ha nem aktív akkor viszont csak a két sín között mehetünk át
			if(n2!=null)return n2;
			else return n3;
		}
		
		return null;
	}
	
}
