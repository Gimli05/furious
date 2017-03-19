package sandbox;

import java.util.ArrayList;

/**
 * Nyilvántartja a vonatokat, kezdeményezi a mozgatásukat. Új vonatot tud hozzáadni a játékhoz. 
 * Ekkor a vonat nextRail attribútuma kezdõértékként arra az enterPointra mutat ahonnan a vonatot indítani szeretnénk.
 */
public class TrainCollection {
	private ArrayList<Train> trains; /* Ebben a listában tároljuk a vonatokat. */
	
	
	/**
	 * Az összes vonatot léptetõ függvény. 
	 * Végigpörgeti a trains arrayListet és minden vonatnak lekéri a getNextRail() metódussal azt a Railt, ahova lépni fog. 
	 * Ezután az adott Rail-re meghívja az accept(train) fv-t ami meghívja a train.visit metódusát és ezzel elvégzi az adott Railen végzendõ mûveletet, 
	 * vagy adott esetben csak beállítja a következõ lépés helyét a train setNextRail() függvényével.
	 */
	public void moveAllTrains(){
		//TODO: kitölteni.
	}
	
	
	/**
	 * Ezzel a függvénnyel lehet hozzáadni új vonatot. 
	 * Minden pályához tartozik egy ütemterv ami szerint megfelelõ idõpillanatokban elõre megadott vonatok a pályára lépnek a belépési pontnál. 
	 * Ez úgy történik, hogy amikor egy új vonatot be akarunk léptetni a pályára, akkor hozzáadjuk eme függvény segítségével a kollekcióhoz (nextRail = EntryPoint értékkel), 
	 * és így következõ alkalommal már a léptetéskor be fog lépni a pályára.
	 * 
	 * @param train Az új hozzáadandó vonat referenciája.
	 */
	public void addNewTrain(Train train){
		trains.add(train);
	}	
}
