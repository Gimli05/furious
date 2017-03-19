package sandbox;

import java.util.ArrayList;

/**
 * A játékmenet vezérléséért felel. Betölti a játékos által kiválasztott pályát fájlból, felépíti a pályához tartozó sín hálózatot és 
 * vezérli melyik EnterPoint-on milyen ütemezéssel hány darab vonat érkezzen meg. Ez az osztály felel a vonatok sebességének meghatározásáért, 
 * és a játék kimenetelének eldöntéséért és ennek lekezeléséért. Felelõs továbbá annak felügyeléséért, hogy csak két alagútszáj lehessen egyszerre aktív.
 */
public class GameController {
	private Boolean isTheGameRunning; /* Megadja, hogy éppen folyamatban van-e játék. True, ha igen, false ha nem. */
	
	private ArrayList<Rail> railCollection; /* A síneket tároló arraylist. A pálya betöltése után ebben tároljuk el a teljes sínhálózatot */
	
	private TrainCollection trainCollection; /* A vonatokat tároló kollekció. A játék futása során az adott pályához tartozó ütemezés szerint adagoljuk 
											  * a pályához tartozó hosszúságú vonatokat a tárolóba a tároló addTrain(Train) metódusával.*/
	
	
	/**
	 * A GameController konstruktora.
	 */
	public GameController(){
		isTheGameRunning = false;
	}
	
	
	/**
	 * Új játék indítására szolgáló függvény. Megjeleníti a játékosnak a választható pályák listáját. 
	 * Miután a játékos kiválasztotta melyik pályán akar játszani, betölti a pályához tartozó sínhálózatot és vonat ütemezést. 
	 * Ezután elindítja a vonatok léptetéséért felelõs szálat.
	 */
	public void startNewGame(){
		//TODO: kitolteni
	}
	
	
	/**
	 * Értesíti a játékost, hogy nyert, és leállítja a játékot.
	 */
	public void winEvent(){
		//TODO: kitolteni
	}
	
	
	/**
	 * Értesíti a játékost, hogy vesztett, és leállítja a játékot.
	 */
	public void loseEvent(){
		//TODO: kitolteni
	}
}
