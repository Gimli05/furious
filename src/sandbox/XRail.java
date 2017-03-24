package sandbox;
/**
 * Egym�st keresztez� s�nek aminek le�r�s�t majd
 * CSOOOONGIIIII
 * meg�rja mert Long jelenleg Anya Spagetti�t eszi
 */
public class XRail extends Rail{
	
	/**
	 * A vonat a visitje sor�n megh�vja ezt a f�ggv�nyt, mely a vonat el�z� poz�ci�ja alapj�n egy�rtelm�en megadja melyik s�nre kell tov�bb mennie. 
	 * Ezt a vonat elt�rolja, �s k�vetkez� k�rben oda fog majd l�pni.
	 * 
	 * Az XRail az els� kett� �s a m�sodik kett� sint kapcsolja p�ra, ez�ltal megengedi hogy k�t ir�nyba
	 * haladjanak rajtam, mig figyeli hogy csak egy vonatelem lehessen rajta.
	 * Az k�vetkez� s�n visszaad�sa is ezen p�rok alapj�n t�rt�nik.
	 * 
	 * @param trainPreviousRail	Az el�z� s�n, ahol a vonat �llt.
	 * @return	A k�vetkez� s�n ahova l�pni fog a vonat.
	 */
	@Override
	public Rail getNextRail(Rail trainPreviousRail){
		System.out.println("Class: XRail\t\t Object: "+this+"\t\t\t Method: getNextRail\t Param: "+trainPreviousRail+"\t A kovetkezo sin."); /* Ki�rat�s a Szkeleton vez�rl�s�nek */
		
		int idx=0; 											/*Index jegyz� seg�dv�ltoz�*/
		while(neighbourRails.get(idx)!=trainPreviousRail){ 	/*Megkeress�k a bej�v� Rail-t*/
			idx++;							 				/*Megkeress�k hogy h�nyadik indexen van*/
		}
		
		/**
		 * Ezen a ponton �rdemes belegondolni hogy hogy is m�k�dik.
		 * Mivel a sinek p�rokba vannak rendezve ez�rt a lehets�ges p�rok:
		 * 01,23,45,... indexek
		 * 
		 * Ez�ltal ha az "idx" p�ratlan, akkor az el�tte l�v�t kell visszadunk,
		 * ha p�ros akkor az ut�na l�v�t.
		 *(Ez�ltal ak�r t�bbes el�gaz�s is �p�thet� lenne)
		 * 
		 * Ezt is fogjuk tenni.
		 */
		
		Rail tmp=null; 							/*Seg�dv�ltoz�, arra az esetre ha rosszul lenne megadva a Rail*/
		if(idx%2==0){ 							/*P�ros indexen vagyunk*/
			tmp = neighbourRails.get(idx+1);	/*Ut�na l�v� a p�rja*/
		}else{ 									/*P�ratlan indexen vagyunk*/
			tmp =  neighbourRails.get(idx+1); 	/*El�tte l�v� a p�rja*/
		}
												/* Ha nincs hova l�pnie, akkor null-t adunk vissza */
		
		/**CSONGI EZ IDE MIFASZNAK KELL ? :D */
		System.out.println("Class: Rail\t\t Object: "+this+"\t\t\t Returned: null"); /* Ki�rat�s a Szkeleton vez�rl�s�nek */
		
		return tmp; 							/*Visszat�r�nk*/
	}
}
