package sandbox;

import java.util.ArrayList;

/**
 * Nyilv�ntartja a vonatokat, kezdem�nyezi a mozgat�sukat. �j vonatot tud hozz�adni a j�t�khoz. 
 * Ekkor a vonat nextRail attrib�tuma kezd��rt�kk�nt arra az enterPointra mutat ahonnan a vonatot ind�tani szeretn�nk.
 */
public class TrainCollection {
	private ArrayList<Train> trains; /* Ebben a list�ban t�roljuk a vonatokat. */
	
	public TrainCollection (){
		trains = new ArrayList<Train>();
	}
	
	/**
	 * Az �sszes vonatot l�ptet� f�ggv�ny. 
	 * V�gigp�rgeti a trains arrayListet �s minden vonatnak lek�ri a getNextRail() met�dussal azt a Railt, ahova l�pni fog. 
	 * Ezut�n az adott Rail-re megh�vja az accept(train) fv-t ami megh�vja a train.visit met�dus�t �s ezzel elv�gzi az adott Railen v�gzend� m�veletet, 
	 * vagy adott esetben csak be�ll�tja a k�vetkez� l�p�s hely�t a train setNextRail() f�ggv�ny�vel.
	 */
	public void moveAllTrains(){
		System.out.println("Class: TrainCollection\t Method: moveAllTrains\t Param: -\t Minden vonat l�p.");
		for(Train train: trains){ /* Minden vonatot a neki k�vetkez� s�nre l�ptet�nk */
			Rail rail=train.getNextRail();
			rail.accept(train);
		}
	}
	
	
	/**
	 * Ezzel a f�ggv�nnyel lehet hozz�adni �j vonatot. 
	 * Minden p�ly�hoz tartozik egy �temterv ami szerint megfelel� id�pillanatokban el�re megadott vonatok a p�ly�ra l�pnek a bel�p�si pontn�l. 
	 * Ez �gy t�rt�nik, hogy amikor egy �j vonatot be akarunk l�ptetni a p�ly�ra, akkor hozz�adjuk eme f�ggv�ny seg�ts�g�vel a kollekci�hoz (nextRail = EntryPoint �rt�kkel), 
	 * �s �gy k�vetkez� alkalommal m�r a l�ptet�skor be fog l�pni a p�ly�ra.
	 * 
	 * @param train Az �j hozz�adand� vonat referenci�ja.
	 */
	public void addNewTrain(Train train){
		System.out.println("Class: TrainCollection\t Method: addNewTrain\t Param: train");
		trains.add(train);
	}	
	
	/**
	 * �r�tj�k a vonatokat list�j�t
	 */
	
	public void clear(){
		System.out.println("Class: TrainCollection\t Method: clear\t Param: -");
		trains.clear();
	}
	
	/**
	 * Megn�zz�k hogy m�g van e nem �res vagon b�rmelyik voanton
	 */
	public boolean isAllEmpty(){
		for(Train train: trains){ /*Minden vonatot vizsg�lunk*/
			if(getFirstNotEmptyCabColor != Color.BLACK)return false; /*Ha van aminek van nem fekete vagonja akkor van utas*/
		}
		return true;
	}
}
