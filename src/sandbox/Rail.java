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
	
	
	/**
	 * A Rail konstruktora. Be�ll�tjaa countert 0-ra.
	 */
	public Rail(){
		trainLenghtCounter = 0; /* L�trehoz�sakor minden s�n �res. */
	}
	
	
	/**
	 * A vonat a visitje sor�n megh�vja ezt a f�ggv�nyt, mely a vonat el�z� poz�ci�ja alapj�n egy�rtelm�en megadja melyik s�nre kell tov�bb mennie. 
	 * Ezt a vonat elt�rolja, �s k�vetkez� k�rben oda fog majd l�pni.
	 * 
	 * @param trainPreviousRail	Az el�z� s�n, ahol a vonat �llt.
	 * @return	A k�vetkez� s�n ahova l�pni fog a vonat.
	 */
	public Rail getNextRail(Rail trainPreviousRail){
		for(Rail oneNeighbourRail:neighbourRails){ /* V�gign�zz�k az �sszes s�nt, �s amelyik nem az �rkez� s�n, az a k�vetkez� s�n, mivel csak 2 szomsz�dja van egy sima s�nnek */
			if(trainPreviousRail!=oneNeighbourRail){
				return oneNeighbourRail;
			}
		}
		return null; /* Ha nincs hova l�pnie, akkor null-t adunk vissza */

	}
	
	
	/**
	 * A visitor tervez�si minta egyik f�ggv�nye. 
	 * Ennek seg�ts�g�vel B�rmelyik s�n t�pus k�nnyed�n fogadhat l�togat�t, melyek ezen overloadolt f�ggv�ny seg�ts�g�vel mindegyik v�gre tudja hajtani a saj�t funkci�j�t.
	 * 
	 * @param visitor A l�togat�, melyet fogadni tud.
	 */
	public void accept(Visitor visitor){
		visitor.visit(this); /* Elfogadjuk a visitort, �s �tadjuk magunkat, hogy n�zzen meg minket. */
	}
	
	
	/**
	 * Ellen�rzi, hogy van-e m�g vonat a s�nen. 
	 * Ha van, akkor true-val, ha nincs akkor false-al t�r vissza.
	 * 
	 * @return	Foglalt-e a s�n.
	 */
	public boolean checkIfOccupied(){
		if(trainLenghtCounter == 0){ /* Ha nincs m�r rajta egyetlen cab-sem akkor false-al t�r vissza egy�bk�nt true-val. */
			return false;
		} else {
			return true;
		}
	}
	
	
	/**
	 * Miut�n minden Rail-t l�trehozutnk, be�ll�tjuk melyik az adott s�nnek a szomsz�dja.
	 * 
	 * @param newNeighbourRails	Az adott s�n szomsz�jdai.
	 */
	public void setNeighbourRails(ArrayList<Rail> newNeighbourRails){
		neighbourRails = newNeighbourRails;
	}
	
	
	/**
	 * Cs�kkenti a trainLenghtCountert
	 * Minden l�ptet�s sor�n cs�kkentj�k egyel a sz�ml�l�t, hiszen m�r elhaladt egy kabin.
	 */
	public void lowerTrainLenghtCounter(){
		trainLenghtCounter--;
	}
}
