package sandbox;

import java.awt.Color;

/**
 * Sz�nnel nem rendelkez� (fekete) vonatelem. Utasokat nem sz�ll�t. Mindig a vonat elej�n helyezkedik el, halad�si ir�nyban. 
 * A vonatoknak mindig rendelkeznek pontosan egy engine-nel. Mikor a vonat poz�ci�j�ra hivatkozunk, akkor az a mozdony poz�ci�j�t jelenti.
 */
public class Engine extends TrainElement{
	/**
	 * Az engine konstruktora. be�ll�tja feket�re a mozdony sz�n�t.
	 */
	public Engine(){
		color = new Color(0,0,0); /* A mozdonynak fekete sz�ne van, �gy ezt a l�trej�vetelekor be�ll�tjuk */
	}
}
