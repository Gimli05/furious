package sandbox;

import java.awt.Color;

/**
 * Speciális sín. Van színe. Amennyiben egy vagon halad el rajta megegyezõ színnel, a vagonról az utasok leszállnak.
 */
public class TrainStation extends Rail{
	private Color color; /* Az állomás színe. 
						  * Ahhoz hogy egy adott vagonból leugorjanak az utasok az állomásnak és a mozdonytól hátrafelé elsõ nem üres kabinnak a színe meg kell egyezzen. */
	
	
	/**
	 * A megálló konstruktora.
	 * 
	 * @param stationColor	A megálló színe.
	 */
	public TrainStation(Color stationColor){
		color = stationColor;
	}
	
	
	/**
	 * Megadja az állomás színét. Amikor visitel a vonat, a vonat lekérdezi milyen színû az állomás majd ez alapján cselekszik. 
	 * @return	Az állomás színe
	 */
	public Color getColor(){
		return color;
	}
}
