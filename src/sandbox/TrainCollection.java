package sandbox;

import java.awt.Color;
import java.util.ArrayList;

/**
 * Nyilvántartja a vonatokat, kezdeményezi a mozgatásukat. Új vonatot tud hozzáadni a játékhoz. 
 * Ekkor a vonat nextRail attribútuma kezdõértékként arra az enterPointra mutat ahonnan a vonatot indítani szeretnénk.
 */
public class TrainCollection {
	
	/** Ebben a listában tároljuk a vonatokat. **/
	private ArrayList<Train> trains;
	
	/**
	 * A TrainCollection konstruktora
	 * Létrehoz egy új listát ami vonatokat tartalmaz.
	 */
	public TrainCollection (){
		System.out.println("Class: TrainCollection\t Object: "+this+"\t Method: Constructor\t "); /* Kiíratás a Szkeleton vezérlésének */
		trains = new ArrayList<Train>(); /* létrehozunk egy új listát */
	}
	
	/**
	 * Az összes vonatot léptetõ függvény. 
	 * Végigpörgeti a trains arrayListet és minden vonatnak lekéri a getNextRail() metódussal azt a Railt, ahova lépni fog. 
	 * Ezután az adott Rail-re meghívja az accept(train) fv-t ami meghívja a train.visit metódusát és ezzel elvégzi az adott Railen végzendõ mûveletet, 
	 * vagy adott esetben csak beállítja a következõ lépés helyét a train setNextRail() függvényével.
	 */
	public void moveAllTrains(){
		System.out.println("Class: TrainCollection\t Object: "+this+"\t Method: moveAllTrains\t  Minden vonat lép."); /* Kiíratás a Szkeleton vezérlésének */
		for(Train train: trains){ /* Minden vonatot a neki következõ sínre léptetünk */
			Rail rail=train.getNextRail(); /* lekérjük a következõ sínt */
			rail.accept(train); /* A visitor minta szerint elfogadtatjuk a sínnel a vonatot. */
		}
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
		System.out.println("Class: TrainCollection\t Object: "+this+"\t Method: addNewTrain\t Param: "+ train); /* Kiíratás a Szkeleton vezérlésének */
		trains.add(train); /* hozzáadjuk az új vonatot a kollekcióhoz. */
	}	
	
	
	/**
	 * Ürítjük a vonatokat listáját
	 */
	public void clear(){
		System.out.println("Class: TrainCollection\t Object: "+this+"\t Method: clear\t "); /* Kiíratás a Szkeleton vezérlésének */
		trains.clear(); /* kiürítjük a kollekciót. */
	}
	
	
	/**
	 * Megnézzük hogy még van e nem üres vagon bármelyik vonatban.
	 * @param	Ha van, akkor hamissal, ha nincs akkor igazzal térünk vissza.
	 */
	public boolean isAllEmpty(){
		System.out.println("Class: TrainCollection\t Object: "+this+"\t Method: isAllEmpty\t "); /* Kiíratás a Szkeleton vezérlésének */
		for(Train train: trains){ /*Minden vonatot vizsgálunk*/
			if(train.getFirstNotEmptyCabColor() != Color.BLACK){ /* ellenõrizzük melyiknek nem fekete a színe (Ha üres, akkor feketét ad vissza a meghívott fv) */
				System.out.println("Class: TrainCollection\t Object: "+this+" Returned: false"); /* Kiíratás a Szkeleton vezérlésének */
				return false; /*Ha van aminek van nem fekete vagonja akkor van utas*/
			}
		}
		System.out.println("Class: TrainCollection\t Object: "+this+"\t Returned: true"); /* Kiíratás a Szkeleton vezérlésének */
		return true; /* Nem találtunk megfelelõ vagont, ezért igazzal térünk vissza. */
	}
}
