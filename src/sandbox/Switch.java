package sandbox;

/**
 * Speci�lis s�n, k�t lehets�ges tov�bbhalad�si ir�nnyal. �sszesen 3 m�sik s�nnel szomsz�dos. 
 * A felhaszn�l� tudja be�ll�tani, hogy a k�t lehets�ges tov�bbhalad�si ir�ny k�z�l melyik akt�v. 
 * Visitor fogad�s�n�l ezt felhaszn�lva mondja meg, hogy az �sb�l sz�rmaz� (de fel�l�rt) getNextRail met�dusa mivel t�r vissza.
 */
public class Switch extends Rail{
	private Boolean state; /* A v�lt� �llapot�t t�rol� v�ltoz�. A v�lt�nak k�t �llapota lehet: m�dos�tja az ir�nyt, vagy nem m�dos�tja azt. 
							* Ha m�dos�tja, akkor true az �rt�ke, ha nem m�dos�tja az ir�ny, akkor false. Ha m�dos�tja az ir�nyt, 
							* akkor a menetir�ny szerinti jobb vagy bal oldalra ir�ny�tja �t a vonatot, ha nem m�dos�tja, akkor egyenesen k�ldi tov�bb. */
	
	
	/**
	 * A v�lt� konstruktora.
	 * Alap �rtelmezetten nem m�dos�t a vonat ir�ny�n.
	 */
	public Switch(){
		state = false;
	}
	
	
	/**
	 * !!!MIVEL A JAVABAN VAN ALAP SWITCH EZ�RT �T KELLET �RNOM SWITCHRAIL-RE A F�GGV�NYNEVET!!!!
	 * A v�lt� ir�ny�nak megv�ltoztat�s�ra szolg�l. A j�t�kos aktiv�lja a v�lt�t, melyre ez a f�ggv�ny h�v�dik meg Ha eddig a state true-volt akkor most false lesz, 
	 * ha false volt akkor pedig true. Ezzel megval�sul, hogy ha eddig megv�ltoztatta az ir�nyt a v�lt�, akkor most pont �t fogja engedni egyenesen, ha eddig �tengedte, most elkanyar�tja.
	 */
	public void switchRail(){ /*Eredetileg switch() */
		if (state == true){
			state = false;
		} else {
			state = true;
		}
	}
	
	
	@Override
	public void accept(Visitor visitor){
		//TODO: kit�lteni.
	}
}
