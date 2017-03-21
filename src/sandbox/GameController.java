package sandbox;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * A j�t�kmenet vez�rl�s��rt felel. Bet�lti a j�t�kos �ltal kiv�lasztott p�ly�t f�jlb�l, fel�p�ti a p�ly�hoz tartoz� s�n h�l�zatot �s 
 * vez�rli melyik EnterPoint-on milyen �temez�ssel h�ny darab vonat �rkezzen meg. Ez az oszt�ly felel a vonatok sebess�g�nek meghat�roz�s��rt, 
 * �s a j�t�k kimenetel�nek eld�nt�s��rt �s ennek lekezel�s��rt. Felel�s tov�bb� annak fel�gyel�s��rt, hogy csak k�t alag�tsz�j lehessen egyszerre akt�v.
 */
public class GameController {
	private static Boolean isTheGameRunning; /* Megadja, hogy �ppen folyamatban van-e j�t�k. True, ha igen, false ha nem. */
	
	private static ArrayList<Rail> railCollection; /* A s�neket t�rol� arraylist. A p�lya bet�lt�se ut�n ebben t�roljuk el a teljes s�nh�l�zatot */
	
	private static TrainCollection trainCollection; /* A vonatokat t�rol� kollekci�. A j�t�k fut�sa sor�n az adott p�ly�hoz tartoz� �temez�s szerint adagoljuk 
											  * a p�ly�hoz tartoz� hossz�s�g� vonatokat a t�rol�ba a t�rol� addTrain(Train) met�dus�val.*/
	
	private static int activeEntranceCounter; /* Az akt�v alag�tsz�lyak sz�m�t t�rolja. Seg�ts�g�vel, ha k�t akt�v alag�tsz�ly van, akkor l�trej�het az alag�t k�z�tt�k. */
	private static int lastPlayedMapNumber;
	
	
	/**
	 * A GameController konstruktora.
	 */
	public GameController(){
		System.out.println("Class: GameController\t Object: GameController@STATIC\t Method: Constructor\t ");
		isTheGameRunning = false;
		railCollection=new ArrayList<Rail>();
		trainCollection = new TrainCollection();
	}
	
	
	/**
	 * �j j�t�k ind�t�s�ra szolg�l� f�ggv�ny. Megjelen�ti a j�t�kosnak a v�laszthat� p�ly�k list�j�t. 
	 * Miut�n a j�t�kos kiv�lasztotta melyik p�ly�n akar j�tszani, bet�lti a p�ly�hoz tartoz� s�nh�l�zatot �s vonat �temez�st. 
	 * Ezut�n elind�tja a vonatok l�ptet�s��rt felel�s sz�lat.
	 */
	public static void startNewGame(int mapNumber){
		System.out.println("Class: GameController\t Object: Object: GameController@STATIC\t Method: startNewGame\t Param: "+mapNumber);
		
		
		String mapName = new String("maps/map" + mapNumber + ".txt");
		try {
			buildFromFile(mapName);
			isTheGameRunning=true; /*Elinditjuka j�tkot*/
			System.out.println("\nClass: GameController\t Object: GameController@STATIC\t A j�t�k elindult\n");
			lastPlayedMapNumber = mapNumber;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * �rtes�ti a j�t�kost, hogy nyert, �s le�ll�tja a j�t�kot.
	 */
	public static void winEvent(){
		isTheGameRunning = false;
		System.out.println("Class: GameController\t Object: GameController@STATIC\t Method: winEvent\t Gyozelem.");
		if(lastPlayedMapNumber == 1){
			System.out.println("Class: GameController\t Object: GameController@STATIC\t Van m�g �j p�lya, �gy az k�vetkezik.");
			System.out.println("\n\n�J J�T�K KEZD�DIK \n\n");
			startNewGame(2);
		} else {
			System.out.println("Class: GameController\t Object: GameController@STATIC\t Nincs tobb palya, jatek vege.");
		}
	}
	
	
	/**
	 * �rtes�ti a j�t�kost, hogy vesztett, �s le�ll�tja a j�t�kot.
	 */
	public static void loseEvent(){
		isTheGameRunning = false;
		System.out.println("Class: GameController\t Object: GameController@STATIC\t Method: loseEvent\t Vereseg");
		System.out.println("Class: GameController\t Object: GameController@STATIC\t Michael Bay Effekt");
	}
	
	/**
	 * Ebben a r�szben egy el�re kit�lt�tt sz�veges f�jlb�l ovlasunk majd be karaktereket,
	 * ami alapj�n automatikusan fel�pitj�k a p�ly�nkat. A f�jl k�t�tt form�tum�, ha ezt 
	 * nem tartjuk a program hibval z�rulna.
	 * A sz�vegf�j formai k�vetelm�nyei:
	 * 	* Az els� sor a p�lya sz�less�ge �s magass�ga lesz, ;-vel elv�lasztva (pl.: 12;7)
	 *  * Ezut�n az elementReadeben le�rtak alapj�n t�ltj�k ki af�jlunkat
	 *  * Fontos hogy minden sorban pontosan annyi karakter legyen, amennyit meghat�roztunk kezdetben
	 *  * Nincs ellen�rizve, hogy van e bel�p�si pontunk, de az akad�lytalan fut�s �rdek�ben aj�nlott.
	 *  * Az egym�s mellett l�v� sinek szomsz�dosnak lesznek v�ve, �gy egy legal�bb egy mez�nyi helyet
	 *    kell hagyni k�zt�k, hogy j�l �p�tse fel.
	 *    pl.: ERRR
	 *    	   xRxR
	 *  	   xRRR
	 * Ha ezeket tartjuk, v�rhat�an j� eredm�nyt kapunk
	 * 
	 * A gyakorlati m�k�d�s:
	 * 	* Kezdetben l�trehozunk egy olvas�t, amivel soronk�nt v�gign�zz�k a f�jlt
	 * 	* Az els� sor�t kiolvassuk �s l�trehozunk 2 t�mb�t: tempMap-ot ami konkr�t sineket t�rol
	 * 	  �s a tempView-t ami a kiolvasott karaktereket.
	 * 	* A kiolvasott karakterekt�l f�gg�en az elementReader visszaadja amegfelel� tipus� Rail-t
	 *  * V�gigolvassuk a f�jlt �s kit�lt�j�k a k�t t�mb�t
	 *  
	 *  * Ezut�n sorban v�gigj�rjuk a tempView mez�it �s ha Rail, akkor megn�zz�k hogy a 8 szomsz�dj�b�l melyik l�tez� Rail
	 *  * Ha a 8 szomsz�dos mez�n van Rail akkor azt felvessz�k az aktu�lis mez� szomsz�dai k�z�
	 *  * V�g�l hozz�adjuk a szomsz�dokat a tempMap megfelel� Rail-j�hez
	 *  * �gyel�nk arra hogy az ellen�rz�tt 8 mez� ne l�gjon le a p�ly�r�l
	 *  
	 *  * V�g�l bej�rjuk a tempMap-t �s az �sszes l�tez� Railt hozz�aduk a railCollectionhoz
	 *  
	 * Ekkorra minden szomsz�doss�g fel van �p�tve �s minden mez� megjelenik a collection-ban.
	 * A met�dus visszat�r.
	 *  
	 */
	
	private static void buildFromFile(String filename) throws IOException{
		System.out.println("Class: GameController\t Object: GameController@STATIC\t Method: buildFromFile\t Param: "+filename+"\t Betoltes.");
		
		/*Kezdetben meg�ll�tjk a j�t�kot �s t�r�lj�k azel�z� list�kat*/
		isTheGameRunning=false;
		railCollection.clear();
		trainCollection.clear();
		System.out.println("Class: GameController\t Object: GameController@STATIC\t Method: railCollection.clear");
		
		
		String in; /*Egy beolvasott sort t�rol, ebbe olvasunk*/
		String[] line; /*Felt�rdelj�k az el�z�leg olvasott sort*/
		
		
		BufferedReader brMap = new BufferedReader(new FileReader(new File(filename))); /*T�rk�p File olvas�*/
		
		String tunnelFilename=filename.substring(0,filename.length()-4)+"tunnelmap.txt";
		line=brMap.readLine().split(";"); /*Kiolvassuk a p�lya m�ret�t*/
		
		
		int width = Integer.parseInt(line[0]); /*P�lya sz�less�ge*/
		int height = Integer.parseInt(line[1]); /*P�lya magass�ga*/
		Rail[][] tempMap = new Rail[width][height]; /*P�lyaelem t�rol� m�trix*/
		String[][] tempView = new String[width][height]; /*P�lyaelem le�r� m�trix*/
		

		int x=0; /*Seg�dv�ltoz� sz�less�ghez*/
		int y=0; /*Seg�dv�ltpz� magass�ghoz*/
		while((in=brMap.readLine())!=null){ /*�sszes marad�k sort kiolvassuk*/
			line=in.split("");	
			for(String s: line){ /*Minden karaktert megn�z�nk*/
				tempMap[x][y] = elementReader(s); /*L�trehozzuk a t�pust*/
				tempView[x][y]=s; /*Mentj�k a v�zlat�t*/		
				x++;
			}
			x=0;
			y++;
		}
		
		
		System.out.println("\nClass: GameController\t Object: GameController@STATIC\t S�nek k�z�tti kapcsolatok l�trehoz�sa.");
		for(int i=0;i<width;i++){ /*V�gign�zz�k a p�ly�t sz�less�gben...*/
			for(int j=0;j<height;j++){	/*... �s magass�gban*/
				if(tempMap[i][j]!=null){		/*Ha az aktu�lis elem nem �res, akkor lehetnek szomsz�dai*/		
					ArrayList<Rail> tmp = new ArrayList<Rail>(); /*�j "szomsz�d t�rol�"*/
					if(i-1>0 && tempMap[i-1][j]!=null) tmp.add(tempMap[i-1][j]); /*Ha balra l�v� mez� a p�lya r�sze �s Rail akkor a szomsz�dja*/
					if(i+1<width && tempMap[i+1][j]!=null) tmp.add(tempMap[i+1][j]);/*Ha jobbra l�v� mez� a p�lya r�sze �s Rail akkor a szomsz�dja*/
					if(j-1>0 && tempMap[i][j-1]!=null) tmp.add(tempMap[i][j-1]);/*Ha felette l�v� mez� a p�lya r�sze �s Rail akkor a szomsz�dja*/
					if(j+1<height && tempMap[i][j+1]!=null) tmp.add(tempMap[i][j+1]);/*Ha alatta l�v� mez� a p�lya r�sze �s Rail akkor a szomsz�dja*/
					
					tempMap[i][j].setNeighbourRails(tmp); /*Hozz�adjuk az �jonnan felvett szomsz�dokat*/
				}
			}
		}
		
		for(int i=0;i<width;i++){ /*Bej�rjuk a t�bl�t*/
			for(int j=0;j<height;j++){
				if(tempMap[i][j]!=null)railCollection.add(tempMap[i][j]); /*Ha van Rail tipus, akkor a kollekci�nk r�sze kell hogy legyen, felvessz�k.*/
				}
		}

		brMap.close();
		System.out.println("Class: GameController\t Object: GameController@STATIC\t L�trehozott p�lyaelemek sz�ma: "+railCollection.size()); /*Megn�zz�k hogy v�ltozott e valam*/
		isTheGameRunning=true; /*Elinditjuka j�tkot*/
		System.out.println("\nClass: GameController\t Object: GameController@STATIC\t A j�t�k elindult\n");
	}
	

	
	 /**
     * A map-ben minden egyes mez� egy bet�vel van megadva.
     * Mindegy egyes bet� egy bizonyos cellat�pusnak felel meg.
     * Al�bbi f�ggv�ny a megkapott bet�nek megfelel� s�nt�pust hoz l�tre, �s visszaadja mely beker�l a railCollection-be.
     *
     * @param mapChar   A map-ban szerepl� karakter, mely egy s�nt�pust takar.
     * @return  A l�trehozott s�n.
     */
    private static Rail elementReader(String mapChar){
    	System.out.println("\nClass: GameController\t Object: GameController@STATIC\t Method: elementReader\t Param: "+mapChar+"\t Elem dekodolasa fajlbol"); /* A jobb olvashat�s�g �rdek�ben ezel�tt egy �j sor van. */
        switch(mapChar){
        case "E":
        	System.out.println("Beolvasott elem: EnterPoint");
            return new EnterPoint(); /* L�trehozunk egy �j enterPointot. */
           
        case "R":
        	System.out.println("Beolvasott elem: Rail");
            return new Rail(); /* L�trehozunk egy �j Railt. */
           
        case "S":
        	System.out.println("Beolvasott elem: Switch");
            return new Switch(); /* L�trehozunk egy �j Switchet. */
           
        case "U":
        	System.out.println("Beolvasott elem: TunnelEntrance");
            return new TunnelEntrance(); /* L�trehozunk egy �j TunnelEntrance-t. */
           
        case "1":
        	System.out.println("Beolvasott elem: Red TrainStation");
            return new TrainStation(Color.RED); /* L�trehozunk egy �j TrainStation-t, mely piros sz�n� lesz. */
           
        case "2":
        	System.out.println("Beolvasott elem: Green TrainStation");
            return new TrainStation(Color.GREEN); /* L�trehozunk egy �j TrainStation-t, mely z�ld sz�n� lesz. */
           
        case "3":
        	System.out.println("Beolvasott elem: Blue TrainStation");
            return new TrainStation(Color.BLUE); /* L�trehozunk egy �j TrainStation-t, mely k�k sz�n� lesz. */
           
        default:
        	System.out.println("Beolvasott elem: ures");
            return null; /* Ez a lehet�s�g akkor fut le ha nem ismert bet� van a sz�veg�nben, mely ilyenkor egy �res mez� lesz */
        }
    }
    
    /**
     * Meg kell n�zni minden l�ptet�s ut�n hogy a vonatok ki�r�ltek e
     */ 
    private boolean hasTheGameEnded(){
    	System.out.println("Class: GameController\t Object: GameController@STATIC\t Method: hasTheGameEnded\t Vonatok uressegenek ellenorzese");
    	Boolean isAllEmpty = trainCollection.isAllEmpty();
    	System.out.println("Class: GameController\t Object: GameController@STATIC\t Returned: "+isAllEmpty);
    	return isAllEmpty;
    }
   
	
	/**
	 * Kiz�r�lag a tesztel�shez l�trehozott met�dus.
	 * Annyi alag�tsz�lyat tud aktiv�lni, ah�nyat megadunk neki. (ennek mennyis�g�t nem ellen�rzi, hiszen azt a Main megfelel� r�sze m�r megtette.
	 * 
	 * A getClass fv csak az�rt kell bele, mert a v�gs� verzi�ban a GUI-n t�rt�n� kattint�sb�l egyb�l meg fogjuk tudni az eventet kiv�lt� objektum id-j�t,
	 * GUI hi�ny�ban azonban erre nincs lehet�s�g�nk. Ha nagyon sz�ks�ges, meg tudjuk oldani en�lk�l is (de az ocsm�nyabb lenne) ez�rt enn�l maradtunk.
	 * 
	 * @param tunnelEntranceCounterToBeActivated	H�ny alag�tsz�lyat akarunk aktiv�lni.
	 */
	public void skeletonTesterActivateTunnelEntrance(int tunnelEntranceCounterToBeActivated){
		for(int i=0; i < tunnelEntranceCounterToBeActivated; i++){
			Boolean notActivatedTunnelEntranceFound = false;
			while(!notActivatedTunnelEntranceFound){
				for(Rail oneRail:railCollection){
					if(oneRail.getClass() == TunnelEntrance.class){  /*  Az ilyen�rt lehet kibasznak tbh.. :D */
						TunnelEntrance oneTunnel = (TunnelEntrance) oneRail; /* Ez�rt meg f�leg. */
						if(!oneTunnel.checkIfActivated()){
							oneTunnel.activate();
							activeEntranceCounter++;
							notActivatedTunnelEntranceFound = true;
							break;
						}
					}
				}
			}
			
		}
		System.out.println("Class: GameController\t Object: GameController@STATIC\t Akt�v alag�tsz�jak sz�ma: "+activeEntranceCounter);
		
		if(activeEntranceCounter == 2){
			System.out.println("\nClass: GameController\t Object: GameController@STATIC\t Alag�t l�trehoz�sa");
			Rail entrance1, entrance2;
			
			/*Kikeress�k a kett�t*/
			for(Rail rail:railCollection){
				if(rail.getClass() == TunnelEntrance.class){
					if(enrance1==null){
						entrance1=rail
					}else{
						entrance2=rail;
					}
				}
			}
			
			/*�sszek�tj�k*/
			int e1X=entrance1.getX();
			int e1Y=entrance1.getY();
			int e2X=entrance2.getX();
			int e2Y=entrance2.getY();
			
			Rail lemarado = entrance1;
			Rail next;
			
			/*X k�zelit*/
			if(e1X>e2X){
				while(e1X>e2X){
					e1X--;
					ArrayList<Rail> neighbours = new ArrayList<Rail>();
					neighbours.add(lemarado);				
					
					if(e1X-1==e2X && e1Y==e2Y){
						e1X--;
						neighbours.add(entrance2);
					}else{
						next=new Tunnel(e1X-1,e1Y);
						neighbours.add(next);
					}
					
					Tunnel tmp = new Tunnel(e1X,e1Y);
					tmp.setNeighbourRails(neighbours);
					
					railCollection.add(tmp);
					if(e1X==e2X && e1Y==e2Y)entrance2.addNeighbourRail(tmp);				
					if(entrance1==lemarado)entrance1.addNeighbourRail(tmp);
					
					lemarado=tmp;
				}
			}else if(e1X<e2X){
				while(e1X<e2X){
					e1X++;
					ArrayList<Rail> neighbours = new ArrayList<Rail>();
					neighbours.add(lemarado);				
					
					if(e1X+1==e2X && e1Y==e2Y){
						e1X++;
						neighbours.add(entrance2);
					}else{
						next=new Tunnel(e1X+1,e1Y);
						neighbours.add(next);
					}
					
					Tunnel tmp = new Tunnel(e1X,e1Y);
					tmp.setNeighbourRails(neighbours);
					
					railCollection.add(tmp);
					if(e1X==e2X && e1Y==e2Y)entrance2.addNeighbourRail(tmp);				
					if(entrance1==lemarado)entrance1.addNeighbourRail(tmp);
					
					lemarado=tmp;
				}
			}
			
			/*Y k�zelit*/
			if(e1Y>e2Y){
				while(e1Y>e2Y){
					e1Y--;
					ArrayList<Rail> neighbours = new ArrayList<Rail>();
					neighbours.add(lemarado);				
					
					if(e1Y+1==e2Y && e1X==e2X){
						e1X--;
						neighbours.add(entrance2);
					}else{
						next=new Tunnel(e1X,e1Y-1);
						neighbours.add(next);
					}
					
					Tunnel tmp = new Tunnel(e1X,e1Y);
					tmp.setNeighbourRails(neighbours);
					
					railCollection.add(tmp);
					if(e1X==e2X && e1Y==e2Y)entrance2.addNeighbourRail(tmp);				
					if(entrance1==lemarado)entrance1.addNeighbourRail(tmp);
					
					lemarado=tmp;
				}
			}else if(e1Y<eY){
				while(e1Y<e2Y){
					e1Y++;
					ArrayList<Rail> neighbours = new ArrayList<Rail>();
					neighbours.add(lemarado);				
					
					if(e1Y+1==e2Y && e1X==e2X){
						e1Y++;
						neighbours.add(entrance2);
					}else{
						next=new Tunnel(e1X,e1Y+1);
						neighbours.add(next);
					}
					
					Tunnel tmp = new Tunnel(e1X,e1Y);
					tmp.setNeighbourRails(neighbours);
					
					railCollection.add(tmp);
					if(e1X==e2X && e1Y==e2Y)entrance2.addNeighbourRail(tmp);				
					if(entrance1==lemarado)entrance1.addNeighbourRail(tmp);
					
					lemarado=tmp;
				}
			}
		}
	}
	
	
	/**
	 * Kiz�r�lag a tesztel�shez l�trehozott met�dus.
	 * Ellen�rzi h�ny akt�v alag�tsz�j van. Ha kett�, akkor lebontja az alagutat, �s deaktiv�l egyet.
	 * 
	 * A getClass fv csak az�rt kell bele, mert a v�gs� verzi�ban a GUI-n t�rt�n� kattint�sb�l egyb�l meg fogjuk tudni az eventet kiv�lt� objektum id-j�t,
	 * GUI hi�ny�ban azonban erre nincs lehet�s�g�nk. Ha nagyon sz�ks�ges, meg tudjuk oldani en�lk�l is (de az ocsm�nyabb lenne) ez�rt enn�l maradtunk.
	 */
	public void skeletonTesterDeActivateATunnelEntrance(){
		if(activeEntranceCounter == 2){
			System.out.println("\nClass: GameController\t Object: GameController@STATIC\t Alag�t lerombol�sa");
			//TODO implement�lni
		}
		
		Boolean activatedTunnelEntranceFound = false;
		while(!activatedTunnelEntranceFound){
			for(Rail oneRail:railCollection){
				if(oneRail.getClass() == TunnelEntrance.class){  /*  Az ilyen�rt lehet kibasznak tbh.. :D */
					TunnelEntrance oneTunnel = (TunnelEntrance) oneRail; /* Ez�rt meg f�leg. */
					if(oneTunnel.checkIfActivated()){
						oneTunnel.deActivate();
						activeEntranceCounter--;
						activatedTunnelEntranceFound = true;
						break;
					}
				}
			}
		}
		System.out.println("Class: GameController\t Object: GameController@STATIC\t Akt�v alag�tsz�jak sz�ma: "+activeEntranceCounter);
	}
	
	
	/**
	 * Kiz�r�lag a tesztel�shez l�trehozott met�dus.
	 * A p�ly�n megkeresi az els� v�lt�t amit tal�l �s be�ll�tja a k�v�nt �llapotra.
	 * 
	 * A getClass fv csak az�rt kell bele, mert a v�gs� verzi�ban a GUI-n t�rt�n� kattint�sb�l egyb�l meg fogjuk tudni az eventet kiv�lt� objektum id-j�t,
	 * GUI hi�ny�ban azonban erre nincs lehet�s�g�nk. Ha nagyon sz�ks�ges, meg tudjuk oldani en�lk�l is (de az ocsm�nyabb lenne) ez�rt enn�l maradtunk.
	 */
	public void skeletonTesterSwitchASwitch(){
		Boolean switchFound = false;
		while(!switchFound){
			for(Rail oneRail:railCollection){
				if(oneRail.getClass() == Switch.class){  /*  Az ilyen�rt lehet kibasznak tbh.. :D */
					Switch oneSwitch = (Switch) oneRail; /* Ez�rt meg f�leg. */
					oneSwitch.switchRail();
					switchFound = true;
					break;
					
				}
			}
		}
	}
	
	
	/**
	 * Kiz�r�lag a tesztel�shez l�trehozott met�dus
	 * A Main-t�l megadott param�terekkel l�trehoz egy vonatot,
	 * 
	 * @param cabinColors
	 */
	public void skeletonTesterAddNewTrain(ArrayList<Color> cabinColors){
		
		System.out.println("\nClass: GameController\t Object: GameController@STATIC\t �j vonat hozz�ad�sa");
		Train testTrain = new Train(cabinColors);
		
		EnterPoint enterPoint = null;
		
		Boolean enterPointFound = false;
		while(!enterPointFound){
			for(Rail oneRail:railCollection){
				if(oneRail.getClass() == EnterPoint.class){  /*  Az ilyen�rt lehet kibasznak tbh.. :D */
					enterPoint = (EnterPoint) oneRail;	
					enterPointFound = true;
				}
			}
		}
		
		
		testTrain.setNextRail(enterPoint);
		
		trainCollection.addNewTrain(testTrain);
	}

}
