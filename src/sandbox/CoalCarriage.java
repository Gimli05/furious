package sandbox;

import java.awt.Color;

/**
 * A szenes kocsi, amire nem lehet felszállni, csak megtoldja a vonatot. 
*/
public class CoalCarriage extends Cab{

	/**
	 * Default konstruktor, csak a színét állítja feketére.
	 */
	public CoalCarriage(){
		super(Color.BLACK); /*Fekete, tehát nem vizsgáljuk*/
		hasPassenger=false;	/*Nem szállít utast*/
	}
}
