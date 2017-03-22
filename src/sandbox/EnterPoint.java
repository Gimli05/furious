package sandbox;

import java.util.ArrayList;

/**
 * peci�lis s�n. A GameController ezen kereszt�l ad �j vonatokat a j�t�khoz. 
 * Ha egy vonat a p�ly�t elhagyva halad �t rajta �gy, hogy a vonat tartalmaz utasokat sz�ll�t� (nem �res) vagonokat, 
 * a szerelv�ny egy Michael Bay Effekt keret�ben felrobban, �s a j�t�kos vesz�t.
 */
public class EnterPoint extends Rail {
	public EnterPoint(){
		System.out.println("Class: EnterPoint\t Object: "+this+"\t\t Method: Constructor\t "); /* Ki�rat�s a Szkeleton vez�rl�s�nek */
	}
	
	/**
	 * A visitor tervez�si minta egyik f�ggv�nye. 
	 * Ennek seg�ts�g�vel B�rmelyik s�n t�pus k�nnyed�n fogadhat l�togat�t, melyek ezen overloadolt f�ggv�ny seg�ts�g�vel mindegyik v�gre tudja hajtani a saj�t funkci�j�t.
	 * 
	 * @param visitor A l�togat�, melyet fogadni tud.
	 */
	public void accept(Visitor visitor){
		System.out.println("Class: EnterPoint\t\t Object: "+this+"\t\t\t Method: Accept\t Param: "+visitor); /* Ki�rat�s a Szkeleton vez�rl�s�nek */
		visitor.visit(this); /* Elfogadjuk a visitort, �s �tadjuk magunkat, hogy n�zzen meg minket. */
	}
}
