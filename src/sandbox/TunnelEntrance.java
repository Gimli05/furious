package sandbox;


/**
 * Speci�lis v�lt�. Egyszerre csak kett� lehet akt�v. A felhaszn�l� hat�rozza meg, hogy a p�ly�n melyik k�t alag�tsz�j legyen akt�v. 
 * Ha van k�t akt�v alag�tsz�j a p�ly�n, akkor v�lt�k�nt m�k�dik, aminek a k�t lehets�ges �ll�sa van. Az egyik az eddig is l�tez� ir�ny, a m�sik a l�trej�tt alag�t fel� mutat.
 */
public class TunnelEntrance extends Switch{
	
	/**
	 * Megadja, hogy az adott alag�t sz�j aktiv�lva van-e. Ha nincs aktiv�lva, akkor false az �rt�ke, ha aktiv�lva van, akkor true. 
	 * Am�g inakt�v, addig egyszer� s�nk�nt m�k�dik az alag�t sz�j, �s tov�bb engedi egyenes ir�nyba az �rkez� vonatokat. 
	 * Amint k�t alag�tsz�jat aktiv�l a j�t�kos, a k�t alag�tsz�jat alag�t k�ti �ssze. Amikor a felhaszn�l� aktiv�lja, lefut az activate() f�ggv�ny, 
	 * mely hat�s�ra a bool igazz� v�lik. Akt�v �llapotban a funkci�j�t tekintve v�lt�k�nt �zemel, ugyanis ebben az esetben egyenesen, 
	 * vagy oldalra is el tudja ir�ny�tani a vonatot (bele az alag�tba).
	 */
	private Boolean isActivated; 
	
	/**
	 * TunnelEntrance konstruktora. 
	 * alap�rtelmezetten egyik alag�tsz�j sincs aktiv�lva.
	 */
	public TunnelEntrance(){
		System.out.println("Class: TunnelEntrance\t Object: "+this+"\t Method: Constructor\t"); /* Ki�rat�s a Szkeleton vez�rl�s�nek */
		isActivated = false;
	}
	
	/**
	 * Aktiv�lja az alag�t sz�jat, azaz igazz� teszi az isActivated v�ltoz�t. Innent�l kezdve a j�t�kos a switch() met�dus seg�ts�g�vel tudja kiv�lasztani merre menjen a vonat.
	 */
	public void activate(){
		System.out.println("Class: TunnelEntrance\t Object: "+this+"\t Method: Activate\t Aktivalta az alagutszajat"); /* Ki�rat�s a Szkeleton vez�rl�s�nek */
		isActivated = true; /* aktiv�ljuk az alag�tsz�jat */
		state=false;
	}
	
	
	/**
	 * Deaktiv�lja az alag�t sz�jat. Deaktiv�l�s ut�n ism�telten csak egyenesen haladhat �t a vonat az adott s�nen.
	 * Ugyan ez a f�ggv�ny felel�s az�rt, hogy t�r�lje a szomsz�dok list�j�b�l a megsz�nt tunnelt.
	 */
	public void deActivate(){
		System.out.println("Class: TunnelEntrance\t Object: "+this+"\t Method: Deactivate\t Deaktivalta az alagutszajat"); /* Ki�rat�s a Szkeleton vez�rl�s�nek */
		isActivated = false; /* Deaktiv�ljuk az alag�tsz�jat. */
		state=false;
		Rail neighbourRailToGetDeleted = null; /* Ebben fogjuk elt�rolni a t�rlend� Tunnel szomsz�dot. */
		for(Rail rail:neighbourRails){  /* v�gign�z�nk minden szomsz�dot */
			if(rail.getClass() == Tunnel.class){ /* amelyik tunnel, azt megjel�lj�k, mint t�rlend� szomsz�dot. */
				neighbourRailToGetDeleted = rail;
			}
		}
		neighbourRails.remove(neighbourRailToGetDeleted); /* T�r�lj�k a megjel�lt szomsz�dot, mivel metsz�tn a  */
	}
	
	
	/**
	 * Megmondja, hogy akt�v-e az alag�tsz�j.
	 * 
	 * @return	Akt�v-e az alag�tsz�j.
	 */
	public Boolean checkIfActivated(){
		System.out.println("Class: TunnelEntrance\t Object: "+this+"\t Method: checkIfActivated\t Ellenorzi aktiv-e az alagutszaj."); /* Ki�rat�s a Szkeleton vez�rl�s�nek */
		System.out.println("Class: TunnelEntrance\t Object: "+this+"\t Returned: "+isActivated); /* Ki�rat�s a Szkeleton vez�rl�s�nek */
		return isActivated;
	}
	
	
	/**
	 * A visitor tervez�si minta egyik f�ggv�nye. 
	 * Ennek seg�ts�g�vel B�rmelyik s�n t�pus k�nnyed�n fogadhat l�togat�t, melyek ezen overloadolt f�ggv�ny seg�ts�g�vel mindegyik v�gre tudja hajtani a saj�t funkci�j�t.
	 * 
	 * @param visitor A l�togat�, melyet fogadni tud.
	 */
	@Override
	public void accept(Visitor visitor){
		System.out.println("Class: TunnelEntrance\t\t Object: "+this+"\t\t\t Method: Accept\t Param: "+visitor); /* Ki�rat�s a Szkeleton vez�rl�s�nek */
		visitor.visit(this); /* Elfogadjuk a visitort, �s �tadjuk magunkat, hogy n�zzen meg minket. */
	}
	

	//LONG MOKOLTA DE IGY JOBB
	@Override
	public Rail getNextRail(Rail trainPreviousRail){
		System.out.println("Class: Switch\t\t Object: "+this+"\t Method: getNextRail\t Param: "+trainPreviousRail+"\t A k�vetkez� s�n."); /* Ki�rat�s a Szkeleton vez�rl�s�nek */		
		
		Rail n2=null;
		Rail n3=null;
		
		for(Rail oneNeighbourRail:neighbourRails){
			if(trainPreviousRail!=oneNeighbourRail){
				if(n2==null)n2=oneNeighbourRail;
				else n3=oneNeighbourRail;
			}
		}
		
		if(isActivated){
			//Ha akt�v akkor egy sima v�lt�, amiben m�r fel van �p�tve az alagut
			Rail np = trainPreviousRail;
			
			
			for (Rail oneNeighbourRail : neighbourRails) {
				if (trainPreviousRail != oneNeighbourRail) {
					if (n2 == null)
						n2 = oneNeighbourRail;
					else
						n3 = oneNeighbourRail;
				}
			}
			
			//F�gg�leges tagozod�s
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
			//vizszintes tagozod�s
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
			//Ha nem akt�v akkor viszont csak a k�t s�n k�z�tt mehet�nk �t
			if(n2!=null)return n2;
			else return n3;
		}
		
		return null;
	}
	
}
