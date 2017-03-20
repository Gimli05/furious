package sandbox;

import java.awt.Color;

/**
 * Színnel nem rendelkezõ (fekete) vonatelem. Utasokat nem szállít. Mindig a vonat elején helyezkedik el, haladási irányban. 
 * A vonatoknak mindig rendelkeznek pontosan egy engine-nel. Mikor a vonat pozíciójára hivatkozunk, akkor az a mozdony pozícióját jelenti.
 */
public class Engine extends TrainElement{
	/**
	 * Az engine konstruktora. beállítja feketére a mozdony színét.
	 */
	public Engine(){
		System.out.println("Class: Engine\t Method: Constructor\t Param: -");
		color = Color.BLACK; /* A mozdonynak fekete színe van, így ezt a létrejövetelekor beállítjuk */
	}
}
