package sandbox;
/**
 * Egymást keresztező sínek aminek leírását majd
 * CSOOOONGIIIII
 * megírja mert Long jelenleg Anya Spagettiét eszi
 */
public class XRail extends Rail{
	
	/**
	 * A vonat a visitje során meghívja ezt a függvényt, mely a vonat elõzõ pozíciója alapján egyértelmûen megadja melyik sínre kell tovább mennie. 
	 * Ezt a vonat eltárolja, és következõ körben oda fog majd lépni.
	 * 
	 * Az XRail az elsö kettö és a második kettö sint kapcsolja pára, ezáltal megengedi hogy két irányba
	 * haladjanak rajtam, mig figyeli hogy csak egy vonatelem lehessen rajta.
	 * Az következü sín visszaadása is ezen párok alapján történik.
	 * 
	 * @param trainPreviousRail	Az elõzõ sín, ahol a vonat állt.
	 * @return	A következõ sín ahova lépni fog a vonat.
	 */
	@Override
	public Rail getNextRail(Rail trainPreviousRail){
		System.out.println("Class: XRail\t\t Object: "+this+"\t\t\t Method: getNextRail\t Param: "+trainPreviousRail+"\t A kovetkezo sin."); /* Kiíratás a Szkeleton vezérlésének */
		
		int idx=0; 											/*Index jegyzö segédváltozó*/
		while(neighbourRails.get(idx)!=trainPreviousRail){ 	/*Megkeressük a bejövő Rail-t*/
			idx++;							 				/*Megkeressük hogy hányadik indexen van*/
		}
		
		/**
		 * Ezen a ponton érdemes belegondolni hogy hogy is működik.
		 * Mivel a sinek párokba vannak rendezve ezért a lehetséges párok:
		 * 01,23,45,... indexek
		 * 
		 * Ezáltal ha az "idx" páratlan, akkor az elötte lévöt kell visszadunk,
		 * ha páros akkor az utána lévöt.
		 *(Ezáltal akár többes elágazás is építhetö lenne)
		 * 
		 * Ezt is fogjuk tenni.
		 */
		
		Rail tmp=null; 							/*Segédváltozó, arra az esetre ha rosszul lenne megadva a Rail*/
		if(idx%2==0){ 							/*Páros indexen vagyunk*/
			tmp = neighbourRails.get(idx+1);	/*Utána lévö a párja*/
		}else{ 									/*Páratlan indexen vagyunk*/
			tmp =  neighbourRails.get(idx+1); 	/*Elötte lévö a párja*/
		}
												/* Ha nincs hova lépnie, akkor null-t adunk vissza */
		
		/**CSONGI EZ IDE MIFASZNAK KELL ? :D */
		System.out.println("Class: Rail\t\t Object: "+this+"\t\t\t Returned: null"); /* Kiíratás a Szkeleton vezérlésének */
		
		return tmp; 							/*Visszatérünk*/
	}
}
