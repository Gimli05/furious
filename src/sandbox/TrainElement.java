package sandbox;

import java.awt.Color;

/**
 * Absztrakt oszt�ly, tartalmazza azokat a tulajdons�gokat amik a vonatok minden egyes elemeit jellemzik. Van sz�n�k, �s ez a sz�n lek�rdezhet�.
 */
public abstract class TrainElement {
	
	/**
	 * Az adott elem sz�ne. A mozdonyok csakis fekete sz�nnel l�teznek, m�g a kabinoknak saj�t sz�ne van. 
	 * K�t vagy t�bb kabinnak lehet azonos sz�ne, ez annyit tesz, hogy ilyen esetben ak�r annyi k�rt meg kell tegyen a vonat, 
	 * ah�ny azonos sz�n� vogn van (felt�ve, hogy csak 1 olyan sz�n� �llom�s van)
	 */
	protected Color color;
	
	/**
	 * Visszat�r a kabin sz�n�vel. Az �llom�sokon val� lesz�ll�s felt�tel ellen�rz�sekor van erre sz�ks�g.
	 * 
	 * @return	A Kabin sz�ne.
	 */
	public Color getColor(){
		System.out.println("Class: TrainElement\t Object: "+this+"\t Method: getColor\t"); /* Ki�rat�s a Szkeleton vez�rl�s�nek */
		System.out.println("Class: TrainElement\t Object: "+this+"\t Returned: "+color); /* Ki�rat�s a Szkeleton vez�rl�s�nek */
		return color;
	}
}
