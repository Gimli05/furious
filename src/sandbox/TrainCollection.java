package sandbox;

import java.awt.Color;
import java.util.ArrayList;

/**
 * Nyilv�ntartja a vonatokat, kezdem�nyezi a mozgat�sukat. �j vonatot tud hozz�adni a j�t�khoz. 
 * Ekkor a vonat nextRail attrib�tuma kezd��rt�kk�nt arra az enterPointra mutat ahonnan a vonatot ind�tani szeretn�nk.
 */
public class TrainCollection {
	
	/** Ebben a list�ban t�roljuk a vonatokat. **/
	private ArrayList<Train> trains;
	
	/**
	 * A TrainCollection konstruktora
	 * L�trehoz egy �j list�t ami vonatokat tartalmaz.
	 */
	public TrainCollection (){
		System.out.println("Class: TrainCollection\t Object: "+this+"\t Method: Constructor\t "); /* Ki�rat�s a Szkeleton vez�rl�s�nek */
		trains = new ArrayList<Train>(); /* l�trehozunk egy �j list�t */
	}
	
	/**
	 * Az �sszes vonatot l�ptet� f�ggv�ny. 
	 * V�gigp�rgeti a trains arrayListet �s minden vonatnak lek�ri a getNextRail() met�dussal azt a Railt, ahova l�pni fog. 
	 * Ezut�n az adott Rail-re megh�vja az accept(train) fv-t ami megh�vja a train.visit met�dus�t �s ezzel elv�gzi az adott Railen v�gzend� m�veletet, 
	 * vagy adott esetben csak be�ll�tja a k�vetkez� l�p�s hely�t a train setNextRail() f�ggv�ny�vel.
	 */
	public void moveAllTrains(){
		System.out.println("Class: TrainCollection\t Object: "+this+"\t Method: moveAllTrains\t  Minden vonat l�p."); /* Ki�rat�s a Szkeleton vez�rl�s�nek */
		for(Train train: trains){ /* Minden vonatot a neki k�vetkez� s�nre l�ptet�nk */
			Rail rail=train.getNextRail(); /* lek�rj�k a k�vetkez� s�nt */
			rail.accept(train); /* A visitor minta szerint elfogadtatjuk a s�nnel a vonatot. */
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
		System.out.println("Class: TrainCollection\t Object: "+this+"\t Method: addNewTrain\t Param: "+ train); /* Ki�rat�s a Szkeleton vez�rl�s�nek */
		trains.add(train); /* hozz�adjuk az �j vonatot a kollekci�hoz. */
	}	
	
	
	/**
	 * �r�tj�k a vonatokat list�j�t
	 */
	public void clear(){
		System.out.println("Class: TrainCollection\t Object: "+this+"\t Method: clear\t "); /* Ki�rat�s a Szkeleton vez�rl�s�nek */
		trains.clear(); /* ki�r�tj�k a kollekci�t. */
	}
	
	
	/**
	 * Megn�zz�k hogy m�g van e nem �res vagon b�rmelyik vonatban.
	 * @param	Ha van, akkor hamissal, ha nincs akkor igazzal t�r�nk vissza.
	 */
	public boolean isAllEmpty(){
		System.out.println("Class: TrainCollection\t Object: "+this+"\t Method: isAllEmpty\t "); /* Ki�rat�s a Szkeleton vez�rl�s�nek */
		for(Train train: trains){ /*Minden vonatot vizsg�lunk*/
			if(train.getFirstNotEmptyCabColor() != Color.BLACK){ /* ellen�rizz�k melyiknek nem fekete a sz�ne (Ha �res, akkor feket�t ad vissza a megh�vott fv) */
				System.out.println("Class: TrainCollection\t Object: "+this+" Returned: false"); /* Ki�rat�s a Szkeleton vez�rl�s�nek */
				return false; /*Ha van aminek van nem fekete vagonja akkor van utas*/
			}
		}
		System.out.println("Class: TrainCollection\t Object: "+this+"\t Returned: true"); /* Ki�rat�s a Szkeleton vez�rl�s�nek */
		return true; /* Nem tal�ltunk megfelel� vagont, ez�rt igazzal t�r�nk vissza. */
	}
}
