package sandbox;

import java.awt.Color;
import java.util.ArrayList;

/**
 * Speci�lis s�n. Van sz�ne. Amennyiben egy vagon halad el rajta megegyez� sz�nnel, a vagonr�l az utasok lesz�llnak.
 */
public class TrainStation extends Rail{
	private Color color; /* Az �llom�s sz�ne. 
						  * Ahhoz hogy egy adott vagonb�l leugorjanak az utasok az �llom�snak �s a mozdonyt�l h�trafel� els� nem �res kabinnak a sz�ne meg kell egyezzen. */
	
	
	/**
	 * A meg�ll� konstruktora.
	 * 
	 * @param stationColor	A meg�ll� sz�ne.
	 */
	public TrainStation(Color stationColor){
		System.out.println("Class: TrainStation\t Method: Constructor\t Param: "+stationColor);
		color = stationColor;
	}
	
	
	/**
	 * Megadja az �llom�s sz�n�t. Amikor visitel a vonat, a vonat lek�rdezi milyen sz�n� az �llom�s majd ez alapj�n cselekszik. 
	 * @return	Az �llom�s sz�ne
	 */
	public Color getColor(){
		System.out.println("Class: TrainStation\t Method: getColor");
		System.out.println("Returned: "+color);
		return color;
	}
}
