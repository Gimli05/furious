package sandbox;

import java.awt.Color;
import java.util.ArrayList;

/**
 * Magába foglal egy mozdonyt és tetszõleges számú vagont. Képes az egyel elõrébb lévõ sínre helyezni a mozdonyát és az összes vagonját. 
 * Mikor a helyére hivatkozunk, a mozdony helyét értjük alatta. Visitorként képes a síneken és az azokból leszármazó speciális síneken lépkedni. 
 * Mikor egy sínre szeretne lépni, átad neki egy magára  mutató referenciát. A sín ezután meghívja a vonat visit metódusát, átadva neki magát paraméterként. 
 * Ezután a vonat már lekérdezheti, hogy hova kell lépnie a következõ körben a sín getNextRail metódusával és a saját tárolt sínjeivel. 
 * Le tudja szállítani az utasokat a vagonjairól, ha az állomás amire rálép a mozdony, olyan színû mint a legelsõ nem üres vagonnak.
 */
public class Train implements Visitor{
	
	/**
	 * A következõ sínre mutató pointer. A következõ léptetéskor ezen fog meghívódni a rail accept() függvénye.
	 */
	private Rail nextRail;
	
	/**
	 * Ez tárolja, hogy honnan jött a vonat. Ennek segítségével a Railek könnyedén meg tudják mondani,
	 * hogy a lehetséges útvonalak közül melyik az, amerre tovább kell haladnia a vonatnak.
	 */
	private Rail previousRail;
	
	/**
	 * A mozdony a vonatban.
	 */
	private Engine engine;
	
	/**
	 * A vonatban található kabinok listája. Mindegyik kabin egy saját színnel rendelkezik,
	 * melyek a mozdonytól hátrafelé tudnak kiürülni,
	 * amennyiben az elõttük levõ kabin már üres és a saját színükkel egyezõ színû állomásra értek.
	 */
	private ArrayList<Cab> cabins;
	
	/**
	 * A vonat konstruktora. Listában megkapja a kabinok színeit.
	 * Ahány kabin színt kap, annyi kabint fog létrehozni, mindegyik kabint tele tölti utassal.
	 * 
	 * @param cabColors	A kabinok színeit tartalmazó lista.
	 */
	public Train(ArrayList<Color> cabColors){
		System.out.println("Class: Train\t\t Object: "+this+"\t\t\t Method: Constructor\t Param: "+cabColors); /* Kiíratás a Szkeleton vezérlésének */
		engine=new Engine(); /* Létrehozunk egy új mozdonyt. */
		cabins=new ArrayList<Cab>(); /* Létrehozunk egy új ArrayListet. */
		for(Color color:cabColors){
			if(color.equals(Color.BLACK))cabins.add(new CoalCarriage());
			else cabins.add(new Cab(color)); /* A lista végére szúrjuk be az új kabint. */
		}
	}
	
	
	/**
	 * Megadja az elsõ nem üres kabin színét. Ennek segítségével amikor egy állomást megvisitel, az állomás le tudja szállíttatni az oda tartozó utasokat. 
	 * Amennyiben a fekete színnel tér vissza, minden kabinból kiürültek az utasok.
	 * 
	 * @return 	Az elsõ nem üres kabin színe.
	 */
	public Color getFirstNotEmptyCabColor(){
		//System.out.println("Class: Train\t\t Object: "+this+"\t\t\t Method: getFirstNotEmptyCabColor\t "); /* Kiíratás a Szkeleton vezérlésének */
		for(Cab oneCabin:cabins){ /* végignézünk minden kabint */
			if(oneCabin.isFull()){/* Ha a kabin tele van, akkor visszatérünk a színével. */
				Color cabinColor = oneCabin.getColor(); /* lekérjük a kabin színét */
				System.out.println("Class: Train\t\t Object: "+this+"\t\t\t Returned: "+cabinColor+"\t Az elso nem üres kabin színe."); /* Kiíratás a Szkeleton vezérlésének */
				return cabinColor; 
			}
		}
		//System.out.println("Class: Train\t\t Object: "+this+"\t\t\t Returned: "+Color.BLACK+"\t Nincs teli kabin."); /* Kiíratás a Szkeleton vezérlésének */
		return Color.BLACK; /* Ha nincs teli kabin, akkor feketével térünk vissza. */
	}
	
	
	/**
	 * A mozdonytól hátrafelé nézve legelsõ nem üres kabinból leszállítja az utasokat. 
	 * A trainStation accept metódusa alatt hívodhat meg, így garantálva van, 
	 * hogy az adott TrainStationnek a kabinnal megegyezõ a színe (csak akkor hívja meg, ha ez teljesül).
	 */
	public void emptyTheFirstNotEmptyCab(){
		//System.out.println("Class: Train\t\t Object: "+this+"\t\t\t Method: emptyTheFirstNotEmptyCab\t "); /* Kiíratás a Szkeleton vezérlésének */
		for(Cab oneCabin:cabins){ /* végig nézzük a kabinokat */
			if(oneCabin.isFull()){ /* Ha a kabin tele van, akkor kiürítjük. */
				oneCabin.emptyCab();
				return;
			}
		}
	}
	
	
	/**
	 * Ez a függvény adja meg, hogy hova kell legközelebb lépnie. Amikor a TrainCollection végiglép minden vonaton, 
	 * mindegyik vonatnak lekéri a következõ Railt ahova lépnie kell, amin meghívja a rail accept() függvényét.
	 * 
	 * @return A következõ sín, ahova lépnie kell majd a vonatnak.
	 */
	public Rail getNextRail(){
	//	System.out.println("Class: Train\t\t Object: "+this+"\t\t\t Method: getNextRail\t A következo sínt adja meg.");
	//	System.out.println("Class: Train\t\t Object: "+this+"\t\t\t Returned: "+nextRail);
		return nextRail;
	}
	
	//Long
	public Rail getPreviousRail(){
		return previousRail;
	}
	
	/**
	 * Ezzel lehet beállítani egy adott lépés végével, hogy hova kell legközelebb lépnie a vonatnak.
	 * 
	 * @param rail	A k0vetkezõ sín, ahova lépni fog majd a vonat.
	 */
	public void setNextRail(Rail rail){
	//	System.out.println("Class: Train\t\t Object: "+this+"\t\t\t Method: setNextRail\t Param: "+rail+"\t Beállítjuk a következo sínt.");
		previousRail = nextRail;
		nextRail = rail; /* frissítjük, hogy honnan jöttünk, és elmentjük, hogy hová fogunk menni. */
		if(nextRail == null){
			GameController.loseEvent();
		}
	}

	
	/* 
	 * Az összes a Visitorban található Visit metódus Overrideolása.
	 * Mindegyik függvény egy bizonyos síntípus meglátogatására szolgál. Bõvebb leírás a Visitorban.
	 */
	@Override
	public void visit(Rail rail) {
	//	System.out.println("Class: Train\t\t Object: "+this+"\t\t\t Method: visit\t Param: "+rail+"\t Sínre lép.");
		if(!rail.checkIfOccupied()){ /* Ellenõrizzük, hogy foglalt-e a sín. */
			this.setNextRail(rail.getNextRail(previousRail)); /* Ha nem foglalt, akkor lekérjük a következõ sínt, ahova lépnie kell a vonatnak, 
			   												   * és beállítjuk azt a vonat következõ sínjének. */
			rail.setTrainLenghtCounter(cabins.size()+1); /* Beállítjuk a sínnek, hogy még hány kabin kell átmenjen rajta. 
														  * Amíg ez a számláló 0-ra nem csökken, foglalt a sín, így ha egy másik vonat rámegy, akkor ütközés történt.
														  */
		} else {
			GameController.loseEvent(); /* Ha foglalt a sín, akkor a vonat ütközött, ezért meghívjuk a GameController loseEvent-jét, 
										 * mely során egy Michael Bay Effekt keretében véget ér a játék. */
		}
	}

	
	@Override
	public void visit(Switch rail) {
	//	System.out.println("Class: Train\t\t Object: "+this+"\t\t\t Method: visit\t Param: "+rail+"\t Switchre lép.");
		if(!rail.checkIfOccupied()){ /* Ellenõrizzük, hogy foglalt-e a sín. */
			this.setNextRail(rail.getNextRail(previousRail)); /* Ha nem foglalt, akkor lekérjük a következõ sínt, ahova lépnie kell a vonatnak, 
			 												   * és beállítjuk azt a vonat következõ sínjének. */
			rail.setTrainLenghtCounter(cabins.size()+1); /* Beállítjuk a sínnek, hogy még hány kabin kell átmenjen rajta. 
														  * Amíg ez a számláló 0-ra nem csökken, foglalt a sín, így ha egy másik vonat rámegy, akkor ütközés történt.
														  */
		} else {
			GameController.loseEvent(); /* Ha foglalt a sín, akkor a vonat ütközött, ezért meghívjuk a GameController loseEvent-jét, 
										 * mely során egy Michael Bay Effekt keretében véget ér a játék. */
		}
	}

	
	@Override
	public void visit(TunnelEntrance rail) {
		System.out.println("Class: Train\t\t Object: "+this+"\t\t\t Method: visit\t Param: "+rail+"\t Alagútszájba lép.");
		if(!rail.checkIfOccupied()){ /* Ellenõrizzük, hogy foglalt-e a sín. */
			this.setNextRail(rail.getNextRail(previousRail)); /* Ha nem foglalt, akkor lekérjük a következõ sínt, ahova lépnie kell a vonatnak, 
			 												   * és beállítjuk azt a vonat következõ sínjének. */
			rail.setTrainLenghtCounter(cabins.size()+1); /* Beállítjuk a sínnek, hogy még hány kabin kell átmenjen rajta. 
														  * Amíg ez a számláló 0-ra nem csökken, foglalt a sín, így ha egy másik vonat rámegy, akkor ütközés történt.
														  */
		} else {
			GameController.loseEvent(); /* Ha foglalt a sín, akkor a vonat ütközött, ezért meghívjuk a GameController loseEvent-jét, 
										 * mely során egy Michael Bay Effekt keretében véget ér a játék. */
		}
	}

	
	@Override
	public void visit(Tunnel rail) {
	//	System.out.println("Class: Train\t\t Object: "+this+"\t\t\t Method: visit\t Param: "+rail+"\t Alagútban lép.");
		if(!rail.checkIfOccupied()){ /* Ellenõrizzük, hogy foglalt-e a sín. */
			this.setNextRail(rail.getNextRail(previousRail)); /* Ha nem foglalt, akkor lekérjük a következõ sínt, ahova lépnie kell a vonatnak, 
			   												   * és beállítjuk azt a vonat következõ sínjének. */
			rail.setTrainLenghtCounter(cabins.size()+1); /* Beállítjuk a sínnek, hogy még hány kabin kell átmenjen rajta. 
														  * Amíg ez a számláló 0-ra nem csökken, foglalt a sín, így ha egy másik vonat rámegy, akkor ütközés történt.
														  */
		} else {
			GameController.loseEvent(); /* Ha foglalt a sín, akkor a vonat ütközött, ezért meghívjuk a GameController loseEvent-jét, 
										 * mely során egy Michael Bay Effekt keretében véget ér a játék. */
		}
	}

	
	@Override
	public void visit(TrainStation rail) {
	//	System.out.println("Class: Train\t\t Object: "+this+"\t\t\t Method: visit\t Param: "+rail+"\t Állomásra lép.");
		if(!rail.checkIfOccupied()){ /* Ellenõrizzük, hogy foglalt-e a sín. */
			this.setNextRail(rail.getNextRail(previousRail)); /* Ha nem foglalt, akkor lekérjük a következõ sínt, ahova lépnie kell a vonatnak, 
															   * és beállítjuk azt a vonat következõ sínjének. */
			rail.setTrainLenghtCounter(cabins.size()+1); /* Beállítjuk a sínnek, hogy még hány kabin kell átmenjen rajta. 
														  * Amíg ez a számláló 0-ra nem csökken, foglalt a sín, így ha egy másik vonat rámegy, akkor ütközés történt.
														  */
			Color trainStationColor = rail.getColor(); /* Lekérjük az állomás színét */
			Color trainColor = this.getFirstNotEmptyCabColor(); /* lekérjük az elsõ nem üres kabin színét */
			
			if(trainColor == trainStationColor){ /* Ha az állomás és az elsõ nem üres kabin színe egyezik */
				System.out.println("Class: Train\t\t Object: "+this+"\t\t\t Az állomás és a vonat színe egyezik!");
				this.emptyTheFirstNotEmptyCab(); /* kiürítjük az elsõ nem üres kabint. */
				GameGUI.addAnimation(rail.getX(), rail.getY(), "Arrive");
				SoundManager.playSound("Arrive");
			} else {
				System.out.println("Class: Train\t\t Object: "+this+"\t\t\t Az állomás és a vonat színe nem egyezik!");
			}
		
			/*Itt ellenörizzük hogy valaki fel akar-e szállni*/		
			Color passengersColor= rail.getPassengersColor();	/*Utasok színe segédváltozóba*/
			if(passengersColor!=Color.BLACK){ 					/*Ha van utasunk*/			
				for(Cab cab: cabins){							/*Minden cab-et átnézünk*/
					if(cab.addPassenger(passengersColor)){ 		/*Ha sikerült felültetni öket*/
						rail.boardPassengers(); 				/*Eltüntetjük a megálló utasait*/
						cab.addPassenger(passengersColor);		/*Megtöltjük a vagont*/
						GameGUI.removeAnimation(rail.getX(), rail.getY(), "Passengers");
						break;									/*Megállitjuk a keresést*/
					}
				}
			}	
			/*Itt ér véget a felszállítás*/
			
		} else {
			GameController.loseEvent(); /* Ha foglalt a sín, akkor a vonat ütközött, ezért meghívjuk a GameController loseEvent-jét, 
										 * mely során egy Michael Bay Effekt keretében véget ér a játék. */
		}
	}
	
	public String getCabStates(){
		String states="E";
		for(Cab cab:cabins){
			if(cab.isFull())states+="F";
			else states+="E";
		}
		
		return states;
	}
}
