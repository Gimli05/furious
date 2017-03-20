package sandbox;

import java.awt.Color;

/**
 * Egyedi sz�nnel rendelkez� vagon, mely utasokat sz�ll�t a sz�mukra megfelel� (azonos sz�n�) �llom�sra a megfelel� sorrendben. 
 * Egy vonatnak tetsz�leges sz�m� kabinja lehet. A j�t�k megnyer�s�nek a felt�tele ezen vagonok ki�r�t�se. 
 * Mikor a vonat visitel egy �llom�st (vagyis a mozdony az �llom�shoz �r), megn�zi, hogy melyik a legels� olyan vagon aminek m�g vannak utasai. 
 * Megn�zi, hogy megegyezik-e ennek a vagonnak a sz�ne a sz�ne az �llom�s�val, �s ha igen, akkor ki�r�ti a vagont (emptyCab).
 */
public class Cab extends TrainElement{
	private Boolean hasPassenger;/* Ebben a v�ltoz�ban t�roljuk, hogy az adott vagonban vannak-e m�g utasok. True ha vannak benne utasok, false ha nincsenek. */
	
	
	/**
	 * A kabin konstruktora. 
	 * @param cabColor	Az adott kabin sz�ne.
	 */
	public Cab(Color cabColor){
		System.out.println("Class: Cab\t Method: Constructor\t Param: cabColor");
		hasPassenger = true; /* Kezdetben minden kabint be�ll�tunk, hogy utassal teli */
		color = cabColor; /* A kabin sz�n�t be�ll�tjuk a megadott sz�nre. */
	}
	
	
	/**
	 * Megadja, hogy vannak-e utasok az adott vagonban. Amennyiben vannak, �gy true-val amennyiben nincsenek, �gy false-al t�r vissza.
	 * 
	 * @return	Van-e utas a kabinban.
	 */
	public Boolean isFull(){
		System.out.println("Class: Cab\t Method: isFull\t Param: -\t Van-e utasa a vagonnak?");
		return hasPassenger; 
	}
	
	
	/** 
	 * Ki�r�ti a kabint. A j�t�k c�lja, hogy minden utas leugorjon a saj�t sz�n� �llom�s�n. 
	 * Ehhez ha egy kabin ki�r�thet� �llapotban van - azaz a mozdonyt�l h�trafel� n�zve � az els� kabin ahol m�g vannak utasok, �s a sz�ne megegyezik  az �llom�s sz�n�vel - 
	 * akkor ennek a f�ggv�nyek a megh�v�s�val lehet ki�r�teni az utasokat az adott meg�ll�n�l. 
	 */
	public void emptyCab(){
		System.out.println("Class: Cab\t Method: emptyCab\t Param: -\t Ki�r�l a vagon.");
		hasPassenger = false;
	}
	
}
