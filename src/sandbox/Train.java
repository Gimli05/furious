package sandbox;

import java.awt.Color;
import java.util.ArrayList;

/**
 * Mag�ba foglal egy mozdonyt �s tetsz�leges sz�m� vagont. K�pes az egyel el�r�bb l�v� s�nre helyezni a mozdony�t �s az �sszes vagonj�t. 
 * Mikor a hely�re hivatkozunk, a mozdony hely�t �rtj�k alatta. Visitork�nt k�pes a s�neken �s az azokb�l lesz�rmaz� speci�lis s�neken l�pkedni. 
 * Mikor egy s�nre szeretne l�pni, �tad neki egy mag�ra  mutat� referenci�t. A s�n ezut�n megh�vja a vonat visit met�dus�t, �tadva neki mag�t param�terk�nt. 
 * Ezut�n a vonat m�r lek�rdezheti, hogy hova kell l�pnie a k�vetkez� k�rben a s�n getNextRail met�dus�val �s a saj�t t�rolt s�njeivel. 
 * Le tudja sz�ll�tani az utasokat a vagonjair�l, ha az �llom�s amire r�l�p a mozdony, olyan sz�n� mint a legels� nem �res vagonnak.
 */
public class Train implements Visitor{
	private Rail nextRail;  /* A k�vetkez� s�nre mutat� pointer. A k�vetkez� l�ptet�skor ezen fog megh�v�dni a rail accept() f�ggv�nye. */
	
	private Rail previousRail; /* Ez t�rolja, hogy honnan j�tt a vonat. Ennek seg�ts�g�vel a Railek k�nnyed�n meg tudj�k mondani, 
								* hogy a lehets�ges �tvonalak k�z�l melyik az, amerre tov�bb kell haladnia a vonatnak. */
	
	private Engine engine; /* A mozdony a vonatban. */
	
	private ArrayList<Cab> cabins; /* A vonatban tal�lhat� kabinok list�ja. Mindegyik kabin egy saj�t sz�nnel rendelkezik, 
									* melyek a mozdonyt�l h�trafel� tudnak ki�r�lni, 
									* amennyiben az el�tt�k lev� kabin m�r �res �s a saj�t sz�n�kkel egyez� sz�n� �llom�sra �rtek. */
	
	
	/**
	 * A vonat konstruktora. List�ban megkapja a kabinok sz�neit.
	 * Ah�ny kabin sz�nt kap, annyi kabint fog l�trehozni, mindegyik kabint tele t�lti utassal.
	 * @param cabColors	A kabinok sz�neit tartalmaz� lista.
	 */
	public Train(ArrayList<Color> cabColors){
		System.out.println("Class: Train\t Method: Constructor\t Param: cabColors");
		engine=new Engine();
		cabins=new ArrayList<Cab>(); /* L�trehozunk egy �j ArrayListet. */
		for(Color color:cabColors){
			cabins.add(new Cab(color)); /* A lista v�g�re sz�rjuk be az �j kabint. */
		}
	}
	
	
	/**
	 * Megadja az els� nem �res kabin sz�n�t. Ennek seg�ts�g�vel amikor egy �llom�st megvisitel, az �llom�s le tudja sz�ll�ttatni az oda tartoz� utasokat. 
	 * Amennyiben a fekete sz�nnel t�r vissza, minden kabinb�l ki�r�ltek az utasok.
	 * 
	 * @return 	Az els� nem �res kabin sz�ne.
	 */
	public Color getFirstNotEmptyCabColor(){
		System.out.println("Class: Train\t Method: getFirstNotEmptyCabColor\t Param: -");
		for(Cab oneCabin:cabins){
			if(oneCabin.isFull()){/* Ha a kabin tele van, akkor visszat�r�nk a sz�n�vel. */
				System.out.println("Return: color\t Az elso nem �res kabin sz�ne.");
				return oneCabin.getColor();
			}
		}
		System.out.println("Return: color\t Nincs teli kabin.");
		return Color.BLACK; /* Ha nincs teli kabin, akkor feket�vel t�r�nk vissza. */
	}
	
	
	/**
	 * A mozdonyt�l h�trafel� n�zve legels� nem �res kabinb�l lesz�ll�tja az utasokat. 
	 * A trainStation accept met�dusa alatt h�vodhat meg, �gy garant�lva van, 
	 * hogy az adott TrainStationnek a kabinnal megegyez� a sz�ne (csak akkor h�vja meg, ha ez teljes�l).
	 */
	public void emptyTheFirstNotEmptyCab(){
		System.out.println("Class: Train\t Method: emptyTheFirstNotEmptyCab\t Param: -");
		for(Cab oneCabin:cabins){
			if(oneCabin.isFull()){ /* Ha a kabin tele van, akkor ki�r�tj�k. */
				oneCabin.emptyCab();
				return;
			}
		}
	}
	
	
	/**
	 * Ez a f�ggv�ny adja meg, hogy hova kell legk�zelebb l�pnie. Amikor a TrainCollection v�gigl�p minden vonaton, 
	 * mindegyik vonatnak lek�ri a k�vetkez� Railt ahova l�pnie kell, amin megh�vja a rail accept() f�ggv�ny�t.
	 * 
	 * @return A k�vetkez� s�n, ahova l�pnie kell majd a vonatnak.
	 */
	public Rail getNextRail(){
		System.out.println("Class: Train\t Method: getNextRail\t Param: -\t Return: nextRail\t A k�vetkezo s�n.");
		return nextRail;
	}
	
	
	/**
	 * Ezzel lehet be�ll�tani egy adott l�p�s v�g�vel, hogy hova kell legk�zelebb l�pnie a vonatnak.
	 * 
	 * @param rail	A k0vetkez� s�n, ahova l�pni fog majd a vonat.
	 */
	public void setNextRail(Rail rail){
		System.out.println("Class: Train\t Method: setNextRail\t Param: rail\t Be�ll�tjuk a k�vetkezo s�nt.");
		previousRail = nextRail;
		nextRail = rail; /* friss�tj�k, hogy honnan j�tt�nk, �s elmentj�k, hogy hov� fogunk menni. */ 
	}

	
	/* 
	 * Az �sszes a Visitorban tal�lhat� Visit met�dus Overrideol�sa.
	 * Mindegyik f�ggv�ny egy bizonyos s�nt�pus megl�togat�s�ra szolg�l. B�vebb le�r�s a Visitorban.
	 */
	@Override
	public void visit(Rail rail) {
		System.out.println("Class: Train override\t Method: visit\t Param: rail\t S�nre l�p.");
		if(!rail.checkIfOccupied()){ /* Ellen�rizz�k, hogy foglalt-e a s�n. */
			this.setNextRail(rail.getNextRail(previousRail)); /* Ha nem foglalt, akkor lek�rj�k a k�vetkez� s�nt, ahova l�pnie kell a vonatnak, 
			   												   * �s be�ll�tjuk azt a vonat k�vetkez� s�nj�nek. */
			rail.setTrainLenghtCounter(cabins.size()+1); /* Be�ll�tjuk a s�nnek, hogy m�g h�ny kabin kell �tmenjen rajta. 
														  * Am�g ez a sz�ml�l� 0-ra nem cs�kken, foglalt a s�n, �gy ha egy m�sik vonat r�megy, akkor �tk�z�s t�rt�nt.
														  */
		} else {
			GameController.loseEvent(); /* Ha foglalt a s�n, akkor a vonat �tk�z�tt, ez�rt megh�vjuk a GameController loseEvent-j�t, 
										 * mely sor�n egy Michael Bay Effekt keret�ben v�get �r a j�t�k. */
		}
	}

	
	@Override
	public void visit(Switch rail) {
		System.out.println("Class: Train override\t Method: visit\t Param: rail\t Switchre l�p.");
		if(!rail.checkIfOccupied()){ /* Ellen�rizz�k, hogy foglalt-e a s�n. */
			this.setNextRail(rail.getNextRail(previousRail)); /* Ha nem foglalt, akkor lek�rj�k a k�vetkez� s�nt, ahova l�pnie kell a vonatnak, 
			 												   * �s be�ll�tjuk azt a vonat k�vetkez� s�nj�nek. */
			rail.setTrainLenghtCounter(cabins.size()+1); /* Be�ll�tjuk a s�nnek, hogy m�g h�ny kabin kell �tmenjen rajta. 
														  * Am�g ez a sz�ml�l� 0-ra nem cs�kken, foglalt a s�n, �gy ha egy m�sik vonat r�megy, akkor �tk�z�s t�rt�nt.
														  */
		} else {
			GameController.loseEvent(); /* Ha foglalt a s�n, akkor a vonat �tk�z�tt, ez�rt megh�vjuk a GameController loseEvent-j�t, 
										 * mely sor�n egy Michael Bay Effekt keret�ben v�get �r a j�t�k. */
		}
	}

	
	@Override
	public void visit(TunnelEntrance rail) {
		System.out.println("Class: Train override\t Method: visit\t Param: rail\t Alag�tsz�jba l�p.");
		if(!rail.checkIfOccupied()){ /* Ellen�rizz�k, hogy foglalt-e a s�n. */
			this.setNextRail(rail.getNextRail(previousRail)); /* Ha nem foglalt, akkor lek�rj�k a k�vetkez� s�nt, ahova l�pnie kell a vonatnak, 
			 												   * �s be�ll�tjuk azt a vonat k�vetkez� s�nj�nek. */
			rail.setTrainLenghtCounter(cabins.size()+1); /* Be�ll�tjuk a s�nnek, hogy m�g h�ny kabin kell �tmenjen rajta. 
														  * Am�g ez a sz�ml�l� 0-ra nem cs�kken, foglalt a s�n, �gy ha egy m�sik vonat r�megy, akkor �tk�z�s t�rt�nt.
														  */
		} else {
			GameController.loseEvent(); /* Ha foglalt a s�n, akkor a vonat �tk�z�tt, ez�rt megh�vjuk a GameController loseEvent-j�t, 
										 * mely sor�n egy Michael Bay Effekt keret�ben v�get �r a j�t�k. */
		}
	}

	
	@Override
	public void visit(Tunnel rail) {
		System.out.println("Class: Train override\t Method: visit\t Param: rail\t Alag�tban l�p.");
		if(!rail.checkIfOccupied()){ /* Ellen�rizz�k, hogy foglalt-e a s�n. */
			this.setNextRail(rail.getNextRail(previousRail)); /* Ha nem foglalt, akkor lek�rj�k a k�vetkez� s�nt, ahova l�pnie kell a vonatnak, 
			   												   * �s be�ll�tjuk azt a vonat k�vetkez� s�nj�nek. */
			rail.setTrainLenghtCounter(cabins.size()+1); /* Be�ll�tjuk a s�nnek, hogy m�g h�ny kabin kell �tmenjen rajta. 
														  * Am�g ez a sz�ml�l� 0-ra nem cs�kken, foglalt a s�n, �gy ha egy m�sik vonat r�megy, akkor �tk�z�s t�rt�nt.
														  */
		} else {
			GameController.loseEvent(); /* Ha foglalt a s�n, akkor a vonat �tk�z�tt, ez�rt megh�vjuk a GameController loseEvent-j�t, 
										 * mely sor�n egy Michael Bay Effekt keret�ben v�get �r a j�t�k. */
		}
	}

	
	@Override
	public void visit(TrainStation rail) {
		System.out.println("Class: Train override\t Method: visit\t Param: rail\t �llom�sra l�p.");
		if(!rail.checkIfOccupied()){ /* Ellen�rizz�k, hogy foglalt-e a s�n. */
			this.setNextRail(rail.getNextRail(previousRail)); /* Ha nem foglalt, akkor lek�rj�k a k�vetkez� s�nt, ahova l�pnie kell a vonatnak, 
															   * �s be�ll�tjuk azt a vonat k�vetkez� s�nj�nek. */
			rail.setTrainLenghtCounter(cabins.size()+1); /* Be�ll�tjuk a s�nnek, hogy m�g h�ny kabin kell �tmenjen rajta. 
														  * Am�g ez a sz�ml�l� 0-ra nem cs�kken, foglalt a s�n, �gy ha egy m�sik vonat r�megy, akkor �tk�z�s t�rt�nt.
														  */
			Color trainStationColor = rail.getColor(); /* Lek�rj�k az �llom�s sz�n�t */
			Color trainColor = this.getFirstNotEmptyCabColor(); /* lek�rj�k az els� nem �res kabin sz�n�t */
			
			if(trainColor == trainStationColor){ /* Ha az �llom�s �s az els� nem �res kabin sz�ne egyezik */
				this.emptyTheFirstNotEmptyCab(); /* ki�r�tj�k az els� nem �res kabint. */
			}
		} else {
			GameController.loseEvent(); /* Ha foglalt a s�n, akkor a vonat �tk�z�tt, ez�rt megh�vjuk a GameController loseEvent-j�t, 
										 * mely sor�n egy Michael Bay Effekt keret�ben v�get �r a j�t�k. */
		}
	}
}
