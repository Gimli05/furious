package sandbox;

import java.util.ArrayList;

/**
 * A p�lya egy cell�j�t kifoglal� s�n, amin legfeljebb egy vonat elem �llhat egyszerre. 
 * Ismeri a szomsz�dos cell�kban elhelyezked� s�neket. K�t m�sik s�nnel szomsz�dos. 
 * Vonat l�ptet�skor a vonatot mint visitort fogadja. Amennyiben egy vonat (vagyis egy visitort megval�s�t� oszt�ly) valamely met�dusa param�terk�nt megkapja ezt az oszt�lyt, 
 * vagy egy lesz�rmazottj�t, megtudhat� a getNextrRail met�dus seg�ts�g�vel �s a vonat previousRail attrib�tum�val egy�tt, hogy a vonatnak merre kell tov�bbl�pnie.
 */
public class Rail {
	private ArrayList<Rail> neighbourRails; /*Egy lista a szomsz�dos s�nekre mutat� pointerekr�l, mely az adott s�n k�zvetlen k�zel�ben tal�lhat�ak. */
	
	
	//TODO: konstruktor?
	
	
	
	/**
	 * A vonat a visitje sor�n megh�vja ezt a f�ggv�nyt, mely a vonat el�z� poz�ci�ja alapj�n egy�rtelm�en megadja melyik s�nre kell tov�bb mennie. 
	 * Ezt a vonat elt�rolja, �s k�vetkez� k�rben oda fog majd l�pni.
	 * 
	 * @param trainPreviousRail	Az el�z� s�n, ahol a vonat �llt.
	 * @return	A k�vetkez� s�n ahova l�pni fog a vonat.
	 */
	public Rail getNextRail(Rail trainPreviousRail){
		//TODO: kit�lteni
		return null;
	}
	
	
	/**
	 * A visitor tervez�si minta egyik f�ggv�nye. 
	 * Ennek seg�ts�g�vel B�rmelyik s�n t�pus k�nnyed�n fogadhat l�togat�t, melyek ezen overloadolt f�ggv�ny seg�ts�g�vel mindegyik v�gre tudja hajtani a saj�t funkci�j�t.
	 * 
	 * @param visitor A l�togat�, melyet fogadni tud.
	 */
	public void accept(Visitor visitor){
		//TODO: kit�lteni.
	}
}
