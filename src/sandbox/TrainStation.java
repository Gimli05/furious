package sandbox;

import java.awt.Color;
import java.util.ArrayList;

/**
 * Speciális sín. Van színe. Amennyiben egy vagon halad el rajta megegyezõ színnel, a vagonról az utasok leszállnak.
 */
public class TrainStation extends Rail{
	private Color color; /* Az állomás színe. 
						  * Ahhoz hogy egy adott vagonból leugorjanak az utasok az állomásnak és a mozdonytól hátrafelé elsõ nem üres kabinnak a színe meg kell egyezzen. */
	
	private Color passengersColor; /*Van e utas a megállóban, ha fekete akkor nincs, ha bármi más akkor van*/
	/**
	 * A megálló konstruktora.
	 * 
	 * @param stationColor	A megálló színe.
	 */
	public TrainStation(Color stationColor){
		System.out.println("Class: TrainStation\t Object: "+this+"\t\t Method: Constructor\t Param: "+stationColor); /* Kiíratás a Szkeleton vezérlésének */
		color = stationColor; /* Megadjuk színnek a paraméterként kapott színt. */
	}
	
	
	/**
	 * Megadja az állomás színét. Amikor visitel a vonat, a vonat lekérdezi milyen színû az állomás majd ez alapján cselekszik. 
	 * @return	Az állomás színe
	 */
	public Color getColor(){
		System.out.println("Class: TrainStation\t Object: "+this+"\t\t Method: getColor"); /* Kiíratás a Szkeleton vezérlésének */
		System.out.println("Class: TrainStation\t Object: "+this+"\t\t Returned: "+color); /* Kiíratás a Szkeleton vezérlésének */
		return color;
	}
	
	
	/**
	 * A visitor tervezési minta egyik függvénye. 
	 * Ennek segítségével Bármelyik sín típus könnyedén fogadhat látogatót, melyek ezen overloadolt függvény segítségével mindegyik végre tudja hajtani a saját funkcióját.
	 * 
	 * @param visitor A látogató, melyet fogadni tud.
	 */
	public void accept(Visitor visitor){
		System.out.println("Class: TrainStation\t\t Object: "+this+"\t\t\t Method: Accept\t Param: "+visitor); /* Kiíratás a Szkeleton vezérlésének */
		visitor.visit(this); /* Elfogadjuk a visitort, és átadjuk magunkat, hogy nézzen meg minket. */
	}
	
	/**
	 * Megadja hogy van e utas a megállóban, és ha igen akkor milyen színü.
	 * Ha nincs utas akkor feketével jelezzük;
	 */
	public Color getPassengersColor(){
		return passengersColor;
	}
	
	/**Ha valaki felszáll akkor eltüntetjük a pályaudvarról**/
	public void boardPassengers(){
		passengersColor=Color.BLACK;
	}
}
