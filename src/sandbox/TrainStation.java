package sandbox;

import java.awt.Color;

/**
 * Speci�lis s�n. Van sz�ne. Amennyiben egy vagon halad el rajta megegyez� sz�nnel, a vagonr�l az utasok lesz�llnak.
 */
public class TrainStation extends Rail{
	
	/**
	 * Az �llom�s sz�ne. 
	 * Ahhoz hogy egy adott vagonb�l leugorjanak az utasok az �llom�snak �s a mozdonyt�l h�trafel� els� nem �res kabinnak a sz�ne meg kell egyezzen.
	 */
	private Color color;
	
	/**
	 * Van e utas a meg�ll�ban, ha fekete akkor nincs, ha b�rmi m�s akkor van
	 */
	private Color passengersColor;
	
	/**
	 * A meg�ll� konstruktora.
	 * 
	 * @param stationColor	A meg�ll� sz�ne.
	 */
	public TrainStation(Color stationColor){
		System.out.println("Class: TrainStation\t Object: "+this+"\t\t Method: Constructor\t Param: "+stationColor); /* Ki�rat�s a Szkeleton vez�rl�s�nek */
		color = stationColor; /* Megadjuk sz�nnek a param�terk�nt kapott sz�nt. */
	}
	
	
	/**
	 * Megadja az �llom�s sz�n�t. Amikor visitel a vonat, a vonat lek�rdezi milyen sz�n� az �llom�s majd ez alapj�n cselekszik. 
	 * 
	 * @return	Az �llom�s sz�ne
	 */
	public Color getColor(){
		System.out.println("Class: TrainStation\t Object: "+this+"\t\t Method: getColor"); /* Ki�rat�s a Szkeleton vez�rl�s�nek */
		System.out.println("Class: TrainStation\t Object: "+this+"\t\t Returned: "+color); /* Ki�rat�s a Szkeleton vez�rl�s�nek */
		return color;
	}
	
	
	/**
	 * A visitor tervez�si minta egyik f�ggv�nye. 
	 * Ennek seg�ts�g�vel B�rmelyik s�n t�pus k�nnyed�n fogadhat l�togat�t, melyek ezen overloadolt f�ggv�ny seg�ts�g�vel mindegyik v�gre tudja hajtani a saj�t funkci�j�t.
	 * 
	 * @param visitor A l�togat�, melyet fogadni tud.
	 */
	@Override
	public void accept(Visitor visitor){
		System.out.println("Class: TrainStation\t\t Object: "+this+"\t\t\t Method: Accept\t Param: "+visitor); /* Ki�rat�s a Szkeleton vez�rl�s�nek */
		visitor.visit(this); /* Elfogadjuk a visitort, �s �tadjuk magunkat, hogy n�zzen meg minket. */
	}
	
	/**
	 * Megadja hogy van e utas a meg�ll�ban, �s ha igen akkor milyen sz�n�.
	 * Ha nincs utas akkor feket�vel jelezz�k;
	 * 
	 * @return A meg�ll�ban l�v� utasok sz�ne
	 */
	public Color getPassengersColor(){
		return passengersColor;
	}
	
	/**Ha valaki felsz�ll akkor elt�ntetj�k a p�lyaudvarr�l**/
	public void boardPassengers(){
		passengersColor=Color.BLACK;
	}
	
	public void addPassengers(){
		passengersColor=getColor();
	}
}
