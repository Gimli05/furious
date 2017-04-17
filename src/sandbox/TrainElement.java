package sandbox;

import java.awt.Color;

/**
 * Absztrakt osztály, tartalmazza azokat a tulajdonságokat amik a vonatok minden egyes elemeit jellemzik. Van színük, és ez a szín lekérdezhetõ.
 */
public abstract class TrainElement {
	
	/**
	 * Az adott elem színe. A mozdonyok csakis fekete színnel léteznek, míg a kabinoknak saját színe van. 
	 * Két vagy több kabinnak lehet azonos színe, ez annyit tesz, hogy ilyen esetben akár annyi kört meg kell tegyen a vonat, 
	 * ahány azonos színû vogn van (feltéve, hogy csak 1 olyan színû állomás van)
	 */
	protected Color color;
	
	/**
	 * Visszatér a kabin színével. Az állomásokon való leszállás feltétel ellenõrzésekor van erre szükség.
	 * 
	 * @return	A Kabin színe.
	 */
	public Color getColor(){
		System.out.println("Class: TrainElement\t Object: "+this+"\t Method: getColor\t"); /* Kiíratás a Szkeleton vezérlésének */
		System.out.println("Class: TrainElement\t Object: "+this+"\t Returned: "+color); /* Kiíratás a Szkeleton vezérlésének */
		return color;
	}
}
