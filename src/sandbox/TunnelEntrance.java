package sandbox;

import java.util.ArrayList;

/**
 * Speci�lis v�lt�. Egyszerre csak kett� lehet akt�v. A felhaszn�l� hat�rozza meg, hogy a p�ly�n melyik k�t alag�tsz�j legyen akt�v. 
 * Ha van k�t akt�v alag�tsz�j a p�ly�n, akkor v�lt�k�nt m�k�dik, aminek a k�t lehets�ges �ll�sa van. Az egyik az eddig is l�tez� ir�ny, a m�sik a l�trej�tt alag�t fel� mutat.
 */
public class TunnelEntrance extends Switch{
	private Boolean isActivated; /* Megadja, hogy az adott alag�t sz�j aktiv�lva van-e. Ha nincs aktiv�lva, akkor false az �rt�ke, ha aktiv�lva van, akkor true. 
								  * Am�g inakt�v, addig egyszer� s�nk�nt m�k�dik az alag�t sz�j, �s tov�bb engedi egyenes ir�nyba az �rkez� vonatokat. 
								  * Amint k�t alag�tsz�jat aktiv�l a j�t�kos, a k�t alag�tsz�jat alag�t k�ti �ssze. Amikor a felhaszn�l� aktiv�lja, lefut az activate() f�ggv�ny, 
								  * mely hat�s�ra a bool igazz� v�lik. Akt�v �llapotban a funkci�j�t tekintve v�lt�k�nt �zemel, ugyanis ebben az esetben egyenesen, 
								  * vagy oldalra is el tudja ir�ny�tani a vonatot (bele az alag�tba).*/
	
	
	/**
	 * Aktiv�lja az alag�t sz�jat, azaz igazz� teszi az isActivated v�ltoz�t. Innent�l kezdve a j�t�kos a switch() met�dus seg�ts�g�vel tudja kiv�lasztani merre menjen a vonat.
	 */
	public void activate(){
		System.out.println("Class: TunnelEntrance\t Method: Activate\t Param: -\t Aktiv�lta az alag�tsz�jat");
		isActivated = true;
	}
	
	
	/**
	 * Deaktiv�lja az alag�t sz�jat. Deaktiv�l�s ut�n ism�telten csak egyenesen haladhat �t a vonat az adott s�nen.
	 */
	public void deActivate(){
		System.out.println("Class: TunnelEntrance\t Method: Deactivate\t Param: -\t Deaktiv�lta az alag�tsz�jat");
		isActivated = false;
	}
	
}
