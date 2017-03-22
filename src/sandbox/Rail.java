package sandbox;

import java.util.ArrayList;

/**
 * A p�lya egy cell�j�t kifoglal� s�n, amin legfeljebb egy vonat elem �llhat egyszerre. 
 * Ismeri a szomsz�dos cell�kban elhelyezked� s�neket. K�t m�sik s�nnel szomsz�dos. 
 * Vonat l�ptet�skor a vonatot mint visitort fogadja. Amennyiben egy vonat (vagyis egy visitort megval�s�t� oszt�ly) valamely met�dusa param�terk�nt megkapja ezt az oszt�lyt, 
 * vagy egy lesz�rmazottj�t, megtudhat� a getNextrRail met�dus seg�ts�g�vel �s a vonat previousRail attrib�tum�val egy�tt, hogy a vonatnak merre kell tov�bbl�pnie.
 */
public class Rail {
	protected ArrayList<Rail> neighbourRails; /* Egy lista a szomsz�dos s�nekre mutat� pointerekr�l, mely az adott s�n k�zvetlen k�zel�ben tal�lhat�ak. */
	protected int trainLenghtCounter; /* Ebben t�roljuk, hogy h�ny kabin fog m�g �thaladni a s�nen (mivel a Train entity csak 1 mez�t foglal el konkr�tan) */
	protected int x,y; /*Poz�cio k�t koordin�t�ja. Ez az alag�t l�trehoz�sa miatt fontos, mivel ak�r t�bb alag�tsz�ly is lehet, melyek k�z�tt procedur�lisan j�n l�tre az alag�t.*/
	
	/**
	 * A Rail konstruktora. Be�ll�tjaa countert 0-ra.
	 */
	public Rail(){
		System.out.println("Class: Rail\t\t Object: "+this+"\t\t\t Method: Constructor\t"); /* Ki�rat�s a Szkeleton vez�rl�s�nek */
		trainLenghtCounter = 0; /* L�trehoz�sakor minden s�n �res. */
		x=0; /*Origoba rakjuk*/
		y=0; /* y koordin�t�t is */
	}
	
	/**
	 *  X koordin�t�t k�rj�k le
	 */
	public int getX(){/* Visszat�r az X koordin�t�val */
		return x;
	}
		
	
	/**
	 *  Y koordin�t�t k�rj�k le
	 */
	public int getY(){ /* Visszat�r az Y koordin�t�val */
		return y;
	}
	
	
	/**
	 * X koordin�t�t be�ll�t� f�ggv�ny.
	 * @param x2	az �j X koordin�ta
	 */
	public void setX(int x2) {
		x=x2;
		
	}
	
	
	/**
	 * Y koordin�t�t be�ll�t� f�ggv�ny.
	 * @param y2	az �j Y koordin�ta
	 */
	public void setY(int y2){
		y=y2;
	}
	
	
	/**
	 * A vonat a visitje sor�n megh�vja ezt a f�ggv�nyt, mely a vonat el�z� poz�ci�ja alapj�n egy�rtelm�en megadja melyik s�nre kell tov�bb mennie. 
	 * Ezt a vonat elt�rolja, �s k�vetkez� k�rben oda fog majd l�pni.
	 * 
	 * @param trainPreviousRail	Az el�z� s�n, ahol a vonat �llt.
	 * @return	A k�vetkez� s�n ahova l�pni fog a vonat.
	 */
	public Rail getNextRail(Rail trainPreviousRail){
		System.out.println("Class: Rail\t\t Object: "+this+"\t\t\t Method: getNextRail\t Param: "+trainPreviousRail+"\t A kovetkezo sin."); /* Ki�rat�s a Szkeleton vez�rl�s�nek */
		for(Rail oneNeighbourRail:neighbourRails){ /* V�gign�zz�k az �sszes s�nt, �s amelyik nem az �rkez� s�n, az a k�vetkez� s�n, mivel csak 2 szomsz�dja van egy sima s�nnek */
			if(trainPreviousRail!=oneNeighbourRail){ /*visszaadjuk azt, amelyik nem az �rkez� s�n*/
				System.out.println("Class: Rail\t\t Object: "+this+"\t\t\t Returned: "+oneNeighbourRail); /* Ki�rat�s a Szkeleton vez�rl�s�nek */
				return oneNeighbourRail;
			}
		}
		System.out.println("Class: Rail\t\t Object: "+this+"\t\t\t Returned: null"); /* Ki�rat�s a Szkeleton vez�rl�s�nek */
		return null; /* Ha nincs hova l�pnie, akkor null-t adunk vissza */

	}
	
	
	/**
	 * A visitor tervez�si minta egyik f�ggv�nye. 
	 * Ennek seg�ts�g�vel B�rmelyik s�n t�pus k�nnyed�n fogadhat l�togat�t, melyek ezen overloadolt f�ggv�ny seg�ts�g�vel mindegyik v�gre tudja hajtani a saj�t funkci�j�t.
	 * 
	 * @param visitor A l�togat�, melyet fogadni tud.
	 */
	public void accept(Visitor visitor){
		System.out.println("Class: Rail\t\t Object: "+this+"\t\t\t Method: Accept\t Param: "+visitor); /* Ki�rat�s a Szkeleton vez�rl�s�nek */
		visitor.visit(this); /* Elfogadjuk a visitort, �s �tadjuk magunkat, hogy n�zzen meg minket. */
	}
	
	
	/**
	 * Ellen�rzi, hogy van-e m�g vonat a s�nen. 
	 * Ha van, akkor true-val, ha nincs akkor false-al t�r vissza.
	 * 
	 * @return	Foglalt-e a s�n.
	 */
	public boolean checkIfOccupied(){
		System.out.println("Class: Rail\t\t Object: "+this+"\t\t\t Method: checkIfOccupied\t Van-e m�g vonat a s�nen?");/* Ki�rat�s a Szkeleton vez�rl�s�nek */
		if(trainLenghtCounter == 0){ /* Ha nincs m�r rajta egyetlen cab-sem akkor false-al t�r vissza egy�bk�nt true-val. */
			System.out.println("Class: Rail\t\t Object: "+this+"\t\t\t Returned: false");/* Ki�rat�s a Szkeleton vez�rl�s�nek */
			return false;
		} else {
			System.out.println("Class: Rail\t\t Object: "+this+"\t\t\t Returned: true");/* Ki�rat�s a Szkeleton vez�rl�s�nek */
			return true;
		}
	}
	
	
	/**
	 * Miut�n minden Rail-t l�trehozutnk, be�ll�tjuk melyik az adott s�nnek a szomsz�dja.
	 * 
	 * @param newNeighbourRails	Az adott s�n szomsz�jdai.
	 */
	public void setNeighbourRails(ArrayList<Rail> newNeighbourRails){
		System.out.println("Class: Rail\t\t Object: "+this+"\t\t\t Method: setNeighbourRails\t Param: "+newNeighbourRails);/* Ki�rat�s a Szkeleton vez�rl�s�nek */
		neighbourRails = newNeighbourRails; /* be�ll�tjuk az �j szomsz�d list�t */
	}
	
	
	/**
	 * A tunnel l�trehoz�sa miatt sz�ks�g van on the fly hozz�adni rail-eket.
	 * Ez a f�ggv�ny ezt a c�lt szolg�lja.
	 * 
	 * @param newNeighbourRail �j szomsz�d
	 */
	public void addNeighbourRail(Rail newNeighbourRail){
		System.out.println("Class: Rail\t\t Object: "+this+"\t\t\t Method: setNeighbourRails\t Param: "+newNeighbourRail);/* Ki�rat�s a Szkeleton vez�rl�s�nek */
		if(neighbourRails==null)neighbourRails=new ArrayList<Rail>(); /* Hozz�adjuk a m�r l�tez� (vagy most l�trehozott) list�hoz egy �j szomsz�dot */
		neighbourRails.add(newNeighbourRail);
	}
	
	
	/**
	 * Cs�kkenti a trainLenghtCountert
	 * Minden l�ptet�s sor�n cs�kkentj�k egyel a sz�ml�l�t, hiszen m�r elhaladt egy kabin.
	 */
	public void lowerTrainLenghtCounter(){
		System.out.println("Class: Rail\t\t Object: "+this+"\t\t\t Method: lowerTrainLenghtCounter\t A vagon tov�bbhalad a s�nen."); /* Ki�rat�s a Szkeleton vez�rl�s�nek */
		if(trainLenghtCounter > 0){ /* Ha m�g nem nulla a sz�ml�l�, azaz van m�g kabin a s�nen, akkor cs�kkentj�k egyel, hiszen most haladt tov�bb */
			trainLenghtCounter--; 
		}
	}
	
	
	/**
	 * A vonat �thalad�sakor be�ll�tjuk, hogy milyen hossz� a vonat, ami �thalad rajta, azaz h�ny l�ptet�s ciklusig fog m�g folglat lenni az adott s�n.
	 * 
	 * @param newCounter	A vonat hossza.
	 */
	public void setTrainLenghtCounter(int newCounter){
		System.out.println("Class: Rail\t\t Object: "+this+"\t\t\t Method: setTrainLenghtCounter\t Param: "+newCounter+"\t Vonat l�pett a s�nre."); /* Ki�rat�s a Szkeleton vez�rl�s�nek */
		trainLenghtCounter = newCounter; /* be�ll�tjuk az �j sz�ml�l�t, hogy m�g h�ny kabin kell �thaladjon az adott s�nen. Ennyi l�ptet�s ciklusig lesz foglalt. */
	}
}
