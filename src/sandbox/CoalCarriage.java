package sandbox;

import java.awt.Color;

/**
 * A szenes kocsi, amire nem lehet felsz�llni, csak megtoldja a vonatot. 
*/
public class CoalCarriage extends Cab{

	/**
	 * Default konstruktor, csak a sz�n�t �ll�tja feket�re.
	 */
	public CoalCarriage(){
		super(Color.BLACK); /*Fekete, teh�t nem vizsg�ljuk*/
		hasPassenger=false;	/*Nem sz�ll�t utast*/
	}
}
