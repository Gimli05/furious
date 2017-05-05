package sandbox;


/**
 * Speci�lis s�n ami k�t alag�tsz�j k�z�tt helyezkedik el. Ez a s�n mindenf�le k�vetkezm�ny n�lk�l keresztezhet m�s s�neket. 
 */
public class Tunnel extends Rail {
	
	/** Az alag�t konstruktora **/
	public Tunnel(){
		System.out.println("Class: Tunnel\t Object: "+this+"\t Method: Constructor\t "); /* Ki�rat�s a Szkeleton vez�rl�s�nek */
	}
	
	/**
	 * A visitor tervez�si minta egyik f�ggv�nye. 
	 * Ennek seg�ts�g�vel B�rmelyik s�n t�pus k�nnyed�n fogadhat l�togat�t, melyek ezen overloadolt f�ggv�ny seg�ts�g�vel mindegyik v�gre tudja hajtani a saj�t funkci�j�t.
	 * 
	 * @param visitor A l�togat�, melyet fogadni tud.
	 */
	@Override
	public void accept(Visitor visitor){
		System.out.println("Class: Tunnel\t\t Object: "+this+"\t\t\t Method: Accept\t Param: "+visitor); /* Ki�rat�s a Szkeleton vez�rl�s�nek */
		visitor.visit(this); /* Elfogadjuk a visitort, �s �tadjuk magunkat, hogy n�zzen meg minket. */
	}
	
}
