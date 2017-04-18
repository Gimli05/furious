package sandbox;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * A j�t�kmenet vez�rl�s��rt felel. Bet�lti a j�t�kos �ltal kiv�lasztott p�ly�t f�jlb�l, fel�p�ti a p�ly�hoz tartoz� s�n h�l�zatot �s 
 * vez�rli melyik EnterPoint-on milyen �temez�ssel h�ny darab vonat �rkezzen meg. Ez az oszt�ly felel a vonatok sebess�g�nek meghat�roz�s��rt, 
 * �s a j�t�k kimenetel�nek eld�nt�s��rt �s ennek lekezel�s��rt. Felel�s tov�bb� annak fel�gyel�s��rt, hogy csak k�t alag�tsz�j lehessen egyszerre akt�v.
 */
public class GameController {
	
	/**
	 * Megadja, hogy �ppen folyamatban van-e j�t�k. True, ha igen, false ha nem.
	 */
	private static Boolean isTheGameRunning;
	
	/**
	 * A s�neket t�rol� arraylist. A p�lya bet�lt�se ut�n ebben t�roljuk el a teljes s�nh�l�zatot
	 */
	private static ArrayList<Rail> railCollection;
	
	/**
	 * A vonatokat t�rol� kollekci�. A j�t�k fut�sa sor�n az adott p�ly�hoz tartoz� �temez�s szerint adagoljuk 
	 * a p�ly�hoz tartoz� hossz�s�g� vonatokat a t�rol�ba a t�rol� addTrain(Train) met�dus�val.
	 */
	private static TrainCollection trainCollection;
	
	/**
	 * Az akt�v alag�tsz�lyak sz�m�t t�rolja. Seg�ts�g�vel, ha k�t akt�v alag�tsz�ly van, akkor l�trej�het az alag�t k�z�tt�k.
	 */
	private static int activeEntranceCounter;
	
	/**
	 * Ebben t�roljuk, melyik p�ly�t ind�tottuk el legutolj�ra.
	 */
	private static int lastPlayedMapNumber;
	
	
	/**
	 * A GameController konstruktora.
	 */
	public GameController(){
		System.out.println("Class: GameController\t Object: GameController@STATIC\t Method: Constructor\t ");/* Ki�rat�s a Szkeleton vez�rl�s�nek */
		isTheGameRunning = false; /* Alapb�l nem fut a j�t�k */
		railCollection=new ArrayList<Rail>(); /* �j list�t hozunk l�tre */
		trainCollection = new TrainCollection(); /* �j kollekci�t hozunk l�tre. */
	}
	
	
	/**
	 * �j j�t�k ind�t�s�ra szolg�l� f�ggv�ny. Megjelen�ti a j�t�kosnak a v�laszthat� p�ly�k list�j�t. 
	 * Miut�n a j�t�kos kiv�lasztotta melyik p�ly�n akar j�tszani, bet�lti a p�ly�hoz tartoz� s�nh�l�zatot �s vonat �temez�st. 
	 * Ezut�n elind�tja a vonatok l�ptet�s��rt felel�s sz�lat.
	 * 
	 * @param mapNumber	a bet�ltend� p�lya sorsz�ma
	 */
	public static void startNewGame(int mapNumber){
		System.out.println("Class: GameController\t Object: Object: GameController@STATIC\t Method: startNewGame\t Param: "+mapNumber); /* Ki�rat�s a Szkeleton vez�rl�s�nek */
		
		String mapName = new String("maps/map" + mapNumber + ".txt"); /* mivel t�bb p�lya is lehet, ez�rt dinamikusan �ll�tjuk �ssze a bet�ltend� nevet. */
		try {
			buildFromFile(mapName); /* Megpr�b�ljuk a f�jlb�l fel�p�teni a p�ly�t */
			isTheGameRunning=true; /*Elinditjuka j�tkot*/
			System.out.println("\nClass: GameController\t Object: GameController@STATIC\t A j�t�k elindult\n"); /* Ki�rat�s a Szkeleton vez�rl�s�nek */
			lastPlayedMapNumber = mapNumber; /* Elmentj�k melyik p�ly�t t�lt�tt�k be utolj�ra. */
		} catch (IOException e) {
			System.err.println("\nClass: GameController\t Object: GameController@STATIC\t HIBA A P�LYA BET�LT�SE K�ZBEN\n"); /* Ha v�letlen nem lehet bet�lteni a p�ly�t, akkor k�ld�nk egy �rtes�tst err�l */
		}
	}
	
	
	/**
	 * �rtes�ti a j�t�kost, hogy nyert, �s le�ll�tja a j�t�kot.
	 */
	public static void winEvent(){
		isTheGameRunning = false; /* Le�ll�tjuk a j�t�kot. Ez majd a GUI-t futtat� threadn�l lesz fontos */
		System.out.println("Class: GameController\t Object: GameController@STATIC\t Method: winEvent\t Gyozelem."); /* Ki�rat�s a Szkeleton vez�rl�s�nek */
		if(lastPlayedMapNumber == 1){ /* Ha az els� p�lya volt az amit el�bb j�tszottunk, akkor bet�ltj�k a m�sodikat. */
			System.out.println("Class: GameController\t Object: GameController@STATIC\t Van m�g �j p�lya, �gy az k�vetkezik."); /* Ki�rat�s a Szkeleton vez�rl�s�nek */
			System.out.println("\n\n�J J�T�K KEZD�DIK \n\n"); /* Ki�rat�s a Szkeleton vez�rl�s�nek */
			startNewGame(2); /* Elindul az �j j�t�k */
		} else {
			System.out.println("Class: GameController\t Object: GameController@STATIC\t Nincs tobb palya, jatek vege."); /* Ki�rat�s a Szkeleton vez�rl�s�nek */
		}
	}
	
	
	/**
	 * �rtes�ti a j�t�kost, hogy vesztett, �s le�ll�tja a j�t�kot.
	 */
	public static void loseEvent(){
		isTheGameRunning = false; /* Le�ll�tjuk a j�t�kot */
		System.out.println("Class: GameController\t Object: GameController@STATIC\t Method: loseEvent\t Vereseg"); /* Ki�rat�s a Szkeleton vez�rl�s�nek */
		System.out.println("Class: GameController\t Object: GameController@STATIC\t Michael Bay Effekt"); /* Ki�rat�s a Szkeleton vez�rl�s�nek */
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
	 *  @param	filename	a beolvasand� file neve
	 *  
	 */
	
	private static void buildFromFile(String filename) throws IOException{
		System.out.println("Class: GameController\t Object: GameController@STATIC\t Method: buildFromFile\t Param: "+filename+"\t Betoltes."); /* Ki�rat�s a Szkeleton vez�rl�s�nek */
		
		/*Kezdetben meg�ll�tjk a j�t�kot �s t�r�lj�k azel�z� list�kat*/
		isTheGameRunning=false;
		railCollection.clear();
		trainCollection.clear();
		System.out.println("Class: GameController\t Object: GameController@STATIC\t Method: railCollection.clear"); /* Ki�rat�s a Szkeleton vez�rl�s�nek */
		
		
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
				if(tempMap[x][y]!=null){
					tempMap[x][y].setX(x);
					tempMap[x][y].setY(y);
				}

				tempView[x][y]=s; /*Mentj�k a v�zlat�t*/		
				x++;
			}
			x=0;
			y++;
		}
		
		
		System.out.println("\nClass: GameController\t Object: GameController@STATIC\t S�nek k�z�tti kapcsolatok l�trehoz�sa."); /* Ki�rat�s a Szkeleton vez�rl�s�nek */
		for(int i=0;i<width;i++){ /*V�gign�zz�k a p�ly�t sz�less�gben...*/
			for(int j=0;j<height;j++){	/*... �s magass�gban*/
				if(tempMap[i][j]!=null){		/*Ha az aktu�lis elem nem �res, akkor lehetnek szomsz�dai*/		
					ArrayList<Rail> tmp = new ArrayList<Rail>(); /*�j "szomsz�d t�rol�"*/
					
					
					
					if(i-1>=0 && tempMap[i-1][j]!=null) tmp.add(tempMap[i-1][j]); /*Ha balra l�v� mez� a p�lya r�sze �s Rail akkor a szomsz�dja*/
					if(i+1<width && tempMap[i+1][j]!=null) tmp.add(tempMap[i+1][j]);/*Ha jobbra l�v� mez� a p�lya r�sze �s Rail akkor a szomsz�dja*/
					if(j-1>=0 && tempMap[i][j-1]!=null) tmp.add(tempMap[i][j-1]);/*Ha felette l�v� mez� a p�lya r�sze �s Rail akkor a szomsz�dja*/
					if(j+1<height && tempMap[i][j+1]!=null) tmp.add(tempMap[i][j+1]);/*Ha alatta l�v� mez� a p�lya r�sze �s Rail akkor a szomsz�dja*/
					
					/*Itt a sorrend sz�m�t, mert az XRail az els� kett�t �s a m�sodik kett�t kapcsolja p�rba*/
					
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
            
        case "X":
        	System.out.println("Beolvasott elem: XRail");
            return new XRail(); /* L�trehozunk egy �j XRail-t. */
           
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
     * 
     * @return Ha mindegyik vonat ki�r�lt, akkor igazzal t�r�nk vissza, egy�bk�nt hamissal.
     */ 
    private boolean hasTheGameEnded(){
    	System.out.println("Class: GameController\t Object: GameController@STATIC\t Method: hasTheGameEnded\t Vonatok uressegenek ellenorzese"); /* Ki�rat�s a Szkeleton vez�rl�s�nek */
    	Boolean isAllEmpty = trainCollection.isAllEmpty(); /* Ha mindegyik vonat ki�r�lt, akkor igazzal t�r�nk vissza, egy�bk�nt hamissal. */
    	System.out.println("Class: GameController\t Object: GameController@STATIC\t Returned: "+isAllEmpty); /* Ki�rat�s a Szkeleton vez�rl�s�nek */
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
		for(int i=0; i < tunnelEntranceCounterToBeActivated; i++){ /* Annyi alag�tsz�jat keres�nk, amennyit param�ter�l megadtak */
			Boolean notActivatedTunnelEntranceFound = false; 
			while(!notActivatedTunnelEntranceFound){ /* am�g nem tal�lunk egy nem aktiv�lt alag�tsz�jat */
				for(Rail oneRail:railCollection){
					if(oneRail.getClass() == TunnelEntrance.class){  /* megn�zz�k TunnelEntrance-e a s�n. */
						TunnelEntrance oneTunnel = (TunnelEntrance) oneRail;  /* Ha igen, akkor �tkasztoljuk */
						if(!oneTunnel.checkIfActivated()){ /* Ha m�g nincs aktiv�lva */
							oneTunnel.activate(); /* Aktiv�ljuk, �s megn�velj�k az akt�v alag�tsz�jak sz�m�t eggyel */
							activeEntranceCounter++;
							notActivatedTunnelEntranceFound = true;
							break; /* Break, kezd�dik a k�vetkez� keres�s�se, ha erre sz�ks�g van. */
						}
					}
				}
			}
			
		}
		System.out.println("Class: GameController\t Object: GameController@STATIC\t Akt�v alag�tsz�jak sz�ma: "+activeEntranceCounter);/* Ki�rat�s a Szkeleton vez�rl�s�nek */
		if(activeEntranceCounter == 2){ /* Ha k�t akt�v alag�tsz�junk van, akkor kezdem�nyezz�k az alag�t l�trehoz�s�t. */
			System.out.println("\nClass: GameController\t Object: GameController@STATIC\t Alag�t l�trehoz�sa");/* Ki�rat�s a Szkeleton vez�rl�s�nek */
			
			/*  Disclaimer: Az al�bbi k�dot Long csin�lta, sz�val God have mercy on your soul, if you want to understand this. */
			Rail entrance1, entrance2;
			entrance1 = null;
			entrance2 = null;
			
			
			
			/*Kikeress�k a kett�t*/
			for(Rail rail:railCollection){
				if(rail.getClass() == TunnelEntrance.class){
					if(entrance1==null){
						entrance1=rail;
					}else{
						entrance2=rail;
					}
				}
			}
			/*Seg�dv�ltoz�k*/
			int e1X=entrance1.getX();
			int e1Y=entrance1.getY();
			int e2X=entrance2.getX();
			int e2Y=entrance2.getY();
			
			ArrayList<Rail> newTunnels= new ArrayList<Rail>();
			Tunnel tmp;
			
			/*Az els� r�sze a bel�p�si pont lesz*/
			newTunnels.add(entrance1);
			
			/*Viszintes tengelyen vizsg�ljuk*/
			/*Ha az els� alagut jobbr�bb van*/
			
			if(e1X>e2X){										
				while(e1X>e2X){									/*Amig nem �r�nk egy szintre a kij�rattal*/
					if(e1X-1==e2X && e1Y==e2Y){ 				/*Pont t�le jobbra van a kij�rat*/
						newTunnels.add(entrance2);				/*Bek�tj�k a kij�rathoz*/
						e1X--;
					}else{ 										/*Ha ez m�g csak egy alagut*/
						e1X--; 									/*K�zelebb hozzuk*/
						tmp=new Tunnel();						/*L�trehozzuk �s be�ll�tjuk az adatait*/
						tmp.setX(e1X);
						tmp.setY(e1Y);
						newTunnels.add(tmp); 					/*Fel�pitj�k*/
					}
				}
			}
			/*Ha az els� alagut balr�bb van*/
			else if(e1X<e2X){
				while(e1X<e2X){									/*Amig nem �r�nk egy szintre a kij�rattal*/
					if(e1X+1==e2X && e1Y==e2Y){				/*Pont t�le balrara van a kij�rat*/
						newTunnels.add(entrance2);				/*Bek�tj�k a kij�rathoz*/
						e1X++;
					}else{ 										/*Ha ez m�g csak egy alagut*/
						e1X++; 									/*K�zelebb hozzuk*/
						tmp=new Tunnel();						/*L�trehozzuk �s be�ll�tjuk az adatait*/
						tmp.setX(e1X);
						tmp.setY(e1Y);
						newTunnels.add(tmp); 					/*Fel�pitj�k*/
					}
				}
			}
			
			/*F�gg�leges tengelyen vizsg�ljuk*/
			/*Ha az els� alagut feljebb van*/
			if(e1Y>e2Y){										
				while(e1Y>e2Y){									/*Amig nem �r�nk egy szintre a kij�rattal*/
					if(e1Y-1==e2Y && e1X==e2X){ 				/*Pont felette van a kij�rat*/
						newTunnels.add(entrance2);				/*Bek�tj�k a kij�rathoz*/
						e1Y--;
					}else{ 										/*Ha ez m�g csak egy alagut*/
						e1Y--; 									/*K�zelebb hozzuk*/
						tmp=new Tunnel();						/*L�trehozzuk �s be�ll�tjuk az adatait*/
						tmp.setX(e1X);
						tmp.setY(e1Y);
						newTunnels.add(tmp); 					/*Fel�pitj�k*/
					}
				}
			}
			/*Ha az els� alagut lejjebb van*/
			else if(e1Y<e2Y){
				while(e1Y<e2Y){									/*Amig nem �r�nk egy szintre a kij�rattal*/
					if(e1Y+1==e2Y && e1X==e2X){ 				/*Pont alatta van a kij�rat*/
						newTunnels.add(entrance2);				/*Bek�tj�k a kij�rathoz*/
						e1Y++;
					}else{ 										/*Ha ez m�g csak egy alagut*/
						e1Y++;									/*K�zelebb hozzuk*/
						tmp=new Tunnel();						/*L�trehozzuk �s be�ll�tjuk az adatait*/
						tmp.setX(e1X);
						tmp.setY(e1Y);
						newTunnels.add(tmp); 					/*Fel�pitj�k*/
					}
				}
			}
			
			/*Minden alagutelemet felvett�nk*/			
			/*�sszek�tj�k �ket*/
			for(int i=0;i<newTunnels.size();i++){
				if(i-1>=0)newTunnels.get(i).addNeighbourRail(newTunnels.get(i-1));
				if(i+1<newTunnels.size())newTunnels.get(i).addNeighbourRail(newTunnels.get(i+1));
				if(i>0&&i<newTunnels.size()-1)railCollection.add(newTunnels.get(i));
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
				
		Boolean activatedTunnelEntranceFound = false;
		while(!activatedTunnelEntranceFound){ /* am�g nem tal�lunk egy aktiv�lt  */
			for(Rail oneRail:railCollection){
				if(oneRail.getClass() == TunnelEntrance.class){   /* Megkeress�k a TunnelEntrancet */
					TunnelEntrance oneTunnel = (TunnelEntrance) oneRail; /* �tkasztoljuk */
					if(oneTunnel.checkIfActivated()){ /* Ellen�rizz�k, hogy aktiv�lva van-e */
						oneTunnel.deActivate(); /* Deaktiv�ljuk, mely sor�n kit�rl�dik a szomsz�dlist�j�b�l a referencia az alag�tra */
						activatedTunnelEntranceFound = true;
						break;
					}
				}
			}
		}
		
		if(activeEntranceCounter == 2){ /* Ha k�t akt�v TunnelEntrance volt */
			System.out.println("\nClass: GameController\t Object: GameController@STATIC\t Alag�t lerombol�sa"); /* Ki�rat�s a Szkeleton vez�rl�s�nek */
			
			Boolean railCollectionIsFreeOfTunnels = false; /* A railCollectionben m�g vannak Tunnelek. Mivel deaktiv�ltunk egy alag�tsz�jat, az eddigi alagutat meg kell sz�ntetni. */
			
			while(!railCollectionIsFreeOfTunnels){ /* Am�g nem lesz Tunnel mentes a railCollection */
				railCollectionIsFreeOfTunnels = true; /* Feltessz�k, hogy �res, ha nem volt az akkor ezt a for-ban �t�rjuk. */
				Rail railToGetDeleted = null; /* Ezt akarjuk majd t�r�lni */
				
				for(Rail rail:railCollection){ /* V�gigmegy�nk az �sszes s�nen */
					if(rail.getClass() == Tunnel.class){ /* Ha tal�ltunk egy tunnelt, azt megjel�lj�k mint t�rlend� s�n �s be�ll�tjuk, hogy m�g nem volt tiszta a railCollection */
						railToGetDeleted = rail;
						railCollectionIsFreeOfTunnels = false;
					}
				}
				
				if(!railCollectionIsFreeOfTunnels){ /* Ha tal�tunk t�rlend� elemet akkor azt itt most ki is t�r�lj�k. */
					railCollection.remove(railToGetDeleted); /* Kit�r�lj�k */
				}
			}
			
		}
		
		activeEntranceCounter--; /* eggyel kevesebb akt�v alag�tsz�j lett. */
		System.out.println("Class: GameController\t Object: GameController@STATIC\t Akt�v alag�tsz�jak sz�ma: "+activeEntranceCounter); /* Ki�rat�s a Szkeleton vez�rl�s�nek */
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
		while(!switchFound){ /* Am�g nem tal�lunk egy switch-et */
			for(Rail oneRail:railCollection){
				if(oneRail.getClass() == Switch.class){  /* v�gign�zz�k melyik a switch */
					Switch oneSwitch = (Switch) oneRail;  /* �tkasztoljuk */
					oneSwitch.switchRail(); /* v�ltoztatunk az �llapot�n egyet */
					switchFound = true;
					break; /* ha sz�ks�ges keres�nk egy k�vetkez�t. */ 
					
				}
			}
		}
	}
	
	/**
	 * V�lt�t lehet vele �ll�tani, meg alagutat �p�teni.
	 * Ha a param�ter�l kapott helyen l�v� elemr�l kider�ti, hogy alag�tsz�j-e; ha igen, akkor aktiv�lva van-e.
	 * Ha igen, deaktiv�lja, ha nem , aktiv�lja. Ha ez alag�t �p�t�st/bont�st von maga ut�n, akkor megteszi.
	 * Ezzel egyid�ben �t�ll�tja az alag�tsz�j v�lt�j�t is (aktiv�l�s -> be az alag�tba, deaktiv�l�s -> alaphelyzetbe).
	 * Ha a kattintott elem v�lt�, akkor �t�ll�tja.
	 * Jelenleg nincs TELJESEN k�sz, a grafikus r�szben ezt fel kell majd iratkoztatni a kattint�sra.
	 * Jelenleg a kapott koordin�ta �gy nem a kattint�s�, hanem a konkr�t elem� amire kattintani akarunk.
	 * Ha mondjuk a cell�k 20*20 pixelesek lesznek �s ide az �rkezik, hogy 33, 21, akkor ebb�l m�g le kell hozni, hogy ez az 1-1-es index� cella.
	 * Ha ez megvan onnant�l viszont ez m�r j� kell hogy legyen
	 * 
	 * @param X	Kattint�s X koordin�t�ja
	 * @param Y	Kattint�s Y koordin�t�ja
	 */
	
	public void clickHandler(int X, int Y){
		for(Rail rail:railCollection){
			if(rail.getX() == X && rail.getY() == Y){  /* Megkeress�k a kattintott elemet */
				try {
					TunnelEntrance thisEntrance = (TunnelEntrance)rail;			/* megpr�b�ljuk �tkasztolni */
					thisEntrance.switchRail();					/* ha siker�lt, �t�ll�tjuk */
					
					if (thisEntrance.checkIfActivated()) {
						thisEntrance.deActivate();
						activeEntranceCounter--;
						if (activeEntranceCounter == 1) {				/* bontani kell */
							for (Rail rail2 : railCollection) {			/* kikeress�k a m�sik nyitott alag�tsz�jat */
								try {
									TunnelEntrance otherEntrance = (TunnelEntrance)rail2;
									if (otherEntrance.checkIfActivated() == false) {
										continue;						/* ha z�rt, folytatjuk a keres�st */
									}
									
									/* +  +  +  +  Innent�l Long k�dja a skeletonTesterb�l +  +  +  +  */
									
									System.out.println("\nClass: GameController\t Object: GameController@STATIC\t Alag�t lerombol�sa"); /* Ki�rat�s a Szkeleton vez�rl�s�nek */
									
									Boolean railCollectionIsFreeOfTunnels = false; /* A railCollectionben m�g vannak Tunnelek. Mivel deaktiv�ltunk egy alag�tsz�jat, az eddigi alagutat meg kell sz�ntetni. */
									
									while(!railCollectionIsFreeOfTunnels){ /* Am�g nem lesz Tunnel mentes a railCollection */
										railCollectionIsFreeOfTunnels = true; /* Feltessz�k, hogy �res, ha nem volt az akkor ezt a for-ban �t�rjuk. */
										Rail railToGetDeleted = null; /* Ezt akarjuk majd t�r�lni */
										
										for(Rail rail3:railCollection){ /* V�gigmegy�nk az �sszes s�nen */
											if(rail3.getClass() == Tunnel.class){ /* Ha tal�ltunk egy tunnelt, azt megjel�lj�k mint t�rlend� s�n �s be�ll�tjuk, hogy m�g nem volt tiszta a railCollection */
												railToGetDeleted = rail3;
												railCollectionIsFreeOfTunnels = false;
											}
										}
										
										if(!railCollectionIsFreeOfTunnels){ /* Ha tal�tunk t�rlend� elemet akkor azt itt most ki is t�r�lj�k. */
											railCollection.remove(railToGetDeleted); /* Kit�r�lj�k */
										}
									}
									break;
									
								} catch (Exception e) {
									// TODO: handle exception
								}
							}
						}
					} 
					
					else {
						thisEntrance.activate();
						activeEntranceCounter++;
						if (activeEntranceCounter == 2) {				/* �p�teni kell */
							for (Rail rail2 : railCollection) {			/* kikeress�k a m�sik nyitott alag�tsz�jat */
								try {
									TunnelEntrance otherEntrance = (TunnelEntrance)rail2;
									if (thisEntrance.equals(otherEntrance) || otherEntrance.checkIfActivated() == false) {
										continue;						/* ha ugyanazt az alag�tsz�jat tal�ltuk meg megint, vagy z�rt, folytatjuk a keres�st */
									}
									
									/* +  +  +  +  Innent�l Long k�dja a skeletonTesterb�l +  +  +  +  */
									
									/*Seg�dv�ltoz�k*/
									int e1X=thisEntrance.getX();
									int e1Y=thisEntrance.getY();
									int e2X=otherEntrance.getX();
									int e2Y=otherEntrance.getY();
									
									ArrayList<Rail> newTunnels= new ArrayList<Rail>();
									Tunnel tmp;
									
									/*Az els� r�sze a bel�p�si pont lesz*/
									newTunnels.add(thisEntrance);
									
									/*Viszintes tengelyen vizsg�ljuk*/
									/*Ha az els� alagut jobbr�bb van*/
									
									if(e1X>e2X){										
										while(e1X>e2X){									/*Amig nem �r�nk egy szintre a kij�rattal*/
											if(e1X-1==e2X && e1Y==e2Y){ 				/*Pont t�le jobbra van a kij�rat*/
												newTunnels.add(otherEntrance);				/*Bek�tj�k a kij�rathoz*/
												e1X--;
											}else{ 										/*Ha ez m�g csak egy alagut*/
												e1X--; 									/*K�zelebb hozzuk*/
												tmp=new Tunnel();						/*L�trehozzuk �s be�ll�tjuk az adatait*/
												tmp.setX(e1X);
												tmp.setY(e1Y);
												newTunnels.add(tmp); 					/*Fel�pitj�k*/
											}
										}
									}
									/*Ha az els� alagut balr�bb van*/
									else if(e1X<e2X){
										while(e1X<e2X){									/*Amig nem �r�nk egy szintre a kij�rattal*/
											if(e1X+1==e2X && e1Y==e2Y){				/*Pont t�le balrara van a kij�rat*/
												newTunnels.add(otherEntrance);				/*Bek�tj�k a kij�rathoz*/
												e1X++;
											}else{ 										/*Ha ez m�g csak egy alagut*/
												e1X++; 									/*K�zelebb hozzuk*/
												tmp=new Tunnel();						/*L�trehozzuk �s be�ll�tjuk az adatait*/
												tmp.setX(e1X);
												tmp.setY(e1Y);
												newTunnels.add(tmp); 					/*Fel�pitj�k*/
											}
										}
									}
									
									/*F�gg�leges tengelyen vizsg�ljuk*/
									/*Ha az els� alagut feljebb van*/
									if(e1Y>e2Y){										
										while(e1Y>e2Y){									/*Amig nem �r�nk egy szintre a kij�rattal*/
											if(e1Y-1==e2Y && e1X==e2X){ 				/*Pont felette van a kij�rat*/
												newTunnels.add(otherEntrance);				/*Bek�tj�k a kij�rathoz*/
												e1Y--;
											}else{ 										/*Ha ez m�g csak egy alagut*/
												e1Y--; 									/*K�zelebb hozzuk*/
												tmp=new Tunnel();						/*L�trehozzuk �s be�ll�tjuk az adatait*/
												tmp.setX(e1X);
												tmp.setY(e1Y);
												newTunnels.add(tmp); 					/*Fel�pitj�k*/
											}
										}
									}
									/*Ha az els� alagut lejjebb van*/
									else if(e1Y<e2Y){
										while(e1Y<e2Y){									/*Amig nem �r�nk egy szintre a kij�rattal*/
											if(e1Y+1==e2Y && e1X==e2X){ 				/*Pont alatta van a kij�rat*/
												newTunnels.add(otherEntrance);				/*Bek�tj�k a kij�rathoz*/
												e1Y++;
											}else{ 										/*Ha ez m�g csak egy alagut*/
												e1Y++;									/*K�zelebb hozzuk*/
												tmp=new Tunnel();						/*L�trehozzuk �s be�ll�tjuk az adatait*/
												tmp.setX(e1X);
												tmp.setY(e1Y);
												newTunnels.add(tmp); 					/*Fel�pitj�k*/
											}
										}
									}
									
									/*Minden alagutelemet felvett�nk*/			
									/*�sszek�tj�k �ket*/
									for(int i=0;i<newTunnels.size();i++){
										if(i-1>=0)newTunnels.get(i).addNeighbourRail(newTunnels.get(i-1));
										if(i+1<newTunnels.size())newTunnels.get(i).addNeighbourRail(newTunnels.get(i+1));
										if(i>0&&i<newTunnels.size()-1)railCollection.add(newTunnels.get(i));
									}
									
									break;
								} catch (Exception e) {
									// TODO: handle exception
								}
							}
						}
					}
					rail = thisEntrance;
					break;								/* Itt ki kell breakelni mert egy sz�vdobban�s alatt �tkonvert�ln� switchre a k�v. try-ban */
				} catch (Exception e) {
					System.out.println("A kattintott elem nem tunnelEntrance");
				}
				
				/*  Megn�zz�k switchre kattintott e a j�t�kos �s ha igen akkor mit kell tenni */
				try {
					Switch sw = (Switch)rail;			/* megpr�b�ljuk �tkasztolni */
					sw.switchRail();					/* ha siker�lt, �t�ll�tjuk */
					rail = sw;
				} catch (Exception e) {
					System.out.println("A kattintott elem nem switch");
				}
				break;
			}
		}
	}
	
	
	/**
	 * Kiz�r�lag a tesztel�shez l�trehozott met�dus.
	 * A p�ly�n megkeresi az els� tunnelEntrancet amit tal�l �s be�ll�tja a k�v�nt �llapotra.
	 * 
	 * A getClass fv csak az�rt kell bele, mert a v�gs� verzi�ban a GUI-n t�rt�n� kattint�sb�l egyb�l meg fogjuk tudni az eventet kiv�lt� objektum id-j�t,
	 * GUI hi�ny�ban azonban erre nincs lehet�s�g�nk. Ha nagyon sz�ks�ges, meg tudjuk oldani en�lk�l is (de az ocsm�nyabb lenne) ez�rt enn�l maradtunk.
	 */
	public void skeletonTesterSwitchATunnelEntrance(){
		Boolean tunnelEntranceFound = false;
		while(!tunnelEntranceFound){ /* Keres�nk egy TunnelEntrance-t */
			for(Rail oneRail:railCollection){
				if(oneRail.getClass() == TunnelEntrance.class){  /* v�gign�zz�k melyik a TunnelEntrance */
					TunnelEntrance oneTunnelEntrance = (TunnelEntrance) oneRail; /* �tkasztoljuk */
					oneTunnelEntrance.switchRail();/* v�ltoztatunk az �llapot�n egyet */
					tunnelEntranceFound = true;
					break;/* ha sz�ks�ges keres�nk egy k�vetkez�t. */ 
					
				}
			}
		}
	}
	
	
	/**
	 * Kiz�r�lag a tesztel�shez l�trehozott met�dus
	 * A Main-t�l megadott param�terekkel l�trehoz egy vonatot,
	 * 
	 * @param cabinColors	A kabinok sz�n�nek list�ja amib�l a vonat fel�p�l
	 */
	public void skeletonTesterAddNewTrain(ArrayList<Color> cabinColors){
		
		System.out.println("\nClass: GameController\t Object: GameController@STATIC\t �j vonat hozz�ad�sa");/* Ki�rat�s a Szkeleton vez�rl�s�nek */
		Train testTrain = new Train(cabinColors); /* A megadott param�terekkel l�trehozunk egy �j vonatot */
		
		EnterPoint enterPoint = null;
		
		Boolean enterPointFound = false; /* Kikeress�k melyik a railCollection-ben az EnterPoint �s azt elt�roljuk, hogy ut�na be�ll�tsuk els� s�nk�nt a vonatnak. */
		while(!enterPointFound){
			for(Rail oneRail:railCollection){
				if(oneRail.getClass() == EnterPoint.class){ 
					enterPoint = (EnterPoint) oneRail;	
					enterPointFound = true;
				}
			}
		}
		
		testTrain.setNextRail(enterPoint); /* Be�ll�tjuk els� s�nnek a bel�p�si pontot */
		trainCollection.addNewTrain(testTrain); /* Hozz�adjuk a trainCollection-be az �j vonatot. */
	}
	
	/**
	 * Kiz�r�lag a tesztel�shez l�trehozott met�dus
	 * L�pteti a vonatokat egyszer.
	 */
	public void skeletonTesterMakeTrainsMove(){
		System.out.println("\nClass: GameController\t Object: GameController@STATIC\t �tk�z�shez sz�ks�ges vonat hossz sz�ml�l�k cs�kkent�se");/* Ki�rat�s a Szkeleton vez�rl�s�nek */
		for(Rail oneRail: railCollection){ /* Minden eggyes s�nnek cs�kkentj�k eggyel a rajta m�g �thalad� kabinok sz�m�t, mivel l�p egyet minden vonat. */
			oneRail.lowerTrainLenghtCounter();
		}
		
		System.out.println("\nClass: GameController\t Object: GameController@STATIC\t Vonatok l�ptet�se"); /* Ki�rat�s a Szkeleton vez�rl�s�nek */
		trainCollection.moveAllTrains(); /* Minden egyes vonatot l�ptet�nk eggyel. K�s�bb ezt egy thread fogja csin�lni, jelen szkeletonban el�g ennyi. */
	}
	
	/**
	 * Kiz�r�lag a tesztel�shez l�trehozott met�dus
	 * 
	 * Kiiratjuk a fel�p�tett p�lya adatait egy f�jla hogy k�s�bb eleln�rizhess�k �ket
	 * @param mapNumber	a bet�ltend� p�lya sz�ma
	 * @param name ez lesz a l�trehozott f�jl neve
	 * @throws IOException 
	 */
	public void createMapTestFile(int mapNumber, String name) throws IOException{
		PrintWriter writer = new PrintWriter(name+".txt", "UTF-8"); /*Kell egy TXT �ro*/
		
		String[] splitresult; 							/*Egyetlen sor felbont�sa*/
		String result; 									/*Egy sor egy r�szlet�t t�rolja*/
		for(Rail rail:railCollection){         	 		/*Megn�z�nk minden sint*/
			splitresult=rail.toString().split("@");		/*Felmbontjuk az objekumot a pointere ment�n*/
			result=rail.getClass().getSimpleName();		/*Elk�rj�k az objektum oszt�ly�t*/
			
			
			/*Ha �llom�sunk volt, annak szine is van, akkor a sz�nre is sz�ks�g�nk lesz*/
			if(rail.getClass().getSimpleName().toString().trim().equals("TrainStation")){ 
				Color c =((TrainStation)rail).getColor();
				if (c.equals(Color.RED)) result+= "1";			/*Ha piros volt, a neve ut�n �runk egy 1 est*/
				if (c.equals(Color.GREEN)) result+= "2";		/*Ha z�ld volt, a neve ut�n �runk egy 2 est*/
				if (c.equals(Color.BLUE)) result+= "3";			/*Ha k�k volt, a neve ut�n �runk egy 3 mast*/
			}
			
			
			/*Egyebe f�zz�k az adatokat a k�vetkez� s�ma szerint:*/
			/*n�v [x,y] (pointer) {szomsz�d1, szomsz�d2,...} */
			result+=" ["+ rail.getX() +","+rail.getY()+"]"+
					" ("+ splitresult[1] +")";
			result+=" {";
			int comacounter=rail.getNeighbourRails().size() -1; /*Elemsz�m -1 vessz�t kell letenn�nk majd a f�jlba*/
			for(Rail neighbour:rail.getNeighbourRails()){
				splitresult=neighbour.toString().split("@");
				result+=splitresult[1];
				if(comacounter>0){ /*Sz�moljuk hogy h�ny vessz�t tett�nk le a szomszdok ut�na*/
					comacounter--;
					result+=",";
				}
			}
			result+="}"; /*Lez�rjuk*/
			writer.println(result); /*Kiiratjuk a f�jlba*/
		}
		writer.close(); /*Bez�rjuk az olvas�st*/
		System.out.println("Save Done");
		MapCreationTest.main("maps/map" + mapNumber + ".txt", name+".txt"); /*Ind�tjuk a tesztet*/
	}
	/**
	 * A sz��kezel�s tesztel�s�re �rt egyszer� program, fix id�k�z�nk�nt gener�l egy t�rt�n�st.
	 * 
	 */
	public void runThreadTest(){
		Thread testThread = new Thread(){ 	/*�j Thread hgoy a h�tt�rben fusson*/
			ArrayList<Color> testColors; 	/*Vonat kocsisz�neit t�rolja*/
			Train testTrain;				/*Egy teszt vonat*/
			EnterPoint enterPoint;			/*Egy v�letlen�l tal�lt bel�p�si pont*/
			
			public void run() {				/*Meg�rjuk az esemnyeket*/
				try {
					testColors=new ArrayList<Color>();  /*Inicializ�ljuk a list�t*/
					testColors.add(Color.BLUE);			/*Sz�nt adunk hozz�*/
					testTrain = new Train(testColors);	/*A szinekkel inicializ�lunk egy vonatot*/
		        
					for(Rail oneRail:railCollection){	/*Kikeres�nk egy bel�p�s�i pontot*/
						if(oneRail.getClass() == EnterPoint.class){ 
							enterPoint = (EnterPoint) oneRail;	
						}
					}
					
					if(enterPoint==null){ /*Amennyiben null az objektumunk, nem volt bel�p�si pont a p�ly�n*/
						System.out.println("Nincs bel�p�si pont a p�ly�n");
						return;		/*Ilyenkor v�ge a fut�snak*/
					}
					
					/*A bel�p�si pontra rakjuk a vonatunk*/
					testTrain.setNextRail(enterPoint); 	
					trainCollection.addNewTrain(testTrain);
					
					
					/*16 l�ptet�st hajtunk v�gre*/
					for(int i=0;i<16;i++){
						for(Rail oneRail: railCollection){ /* Minden eggyes s�nnek cs�kkentj�k eggyel a rajta m�g �thalad� kabinok sz�m�t, mivel l�p egyet minden vonat. */
							oneRail.lowerTrainLenghtCounter();
						}
						trainCollection.moveAllTrains();				
						drawToConsole();	/*K�zben friss�tjuk a n�zetet*/
						Thread.sleep(1000);
					}

					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		};
		
		testThread.start(); /*Ind�tuk a sz�lat*/
	}
	
	/**
	 * kirajzolja a p�ly�t a konzolra
	 */
	public void drawToConsole(){
		
		int maxX = 0;		/* mi a legnagyobb x index ( = p�lyasz�less�g - 1) */
		int maxY = 0;		/* mi a legnagyobb y index ( = p�lyamagass�g - 1) */
		for (Rail rail : railCollection) {
			if (rail.getY() > maxY) {
				maxY = rail.getY();
			}
			if (rail.getX() > maxX) {
				maxX = rail.getX();
			}
		}
		
		Rail[][] mapToDraw = new Rail[maxY + 1][maxX + 1];	/* sajnos a railcollectionben nem sorfojtonosan egym�s
														ut�n vannak az elemek. Kell egy map helyette */
		for (Rail rail : railCollection) {
			mapToDraw[rail.getY()][rail.getX()] = rail;	/*felt�ltj�k a mapet */
		}
		
		char[][] charMap = new char[maxY + 1][maxX + 1]; /* ebben vannak a konkr�t karakterek amiket ki fogunk �rni */
		for (char[] cs : charMap) {
			for (char c : cs) {
				c = ' '; 								/* kezdetben minden legyen �res, majd fel�l lesznek �rva elemekkel */	
			}
		}
		
		
		for (int line = 0; line < maxY + 1; line++) {	/* charmap felt�lt�se ki�rand� bet�kkel a mapToDraw alapj�n */
			for (int col = 0; col < maxX + 1; col++) {
				if (mapToDraw[line][col] == null) {		/* Ha egy cella �res a mapToDraw-ban, akkor az a charMap-ben ' ' marad */
					continue;
				}
				switch (mapToDraw[line][col].getClass().getSimpleName()) {
				case "Rail":
					charMap[line][col] = 'R';
					break;
				case "EnterPoint":
					charMap[line][col] = 'E';
					break;
				case "Switch":
					charMap[line][col] = 'S';
					break;
				case "Tunnel":
					charMap[line][col] = 'T';
					break;
				case "TunnelEntrance":
					charMap[line][col] = 'U';
					break;
				case "XRail":
					charMap[line][col] = 'X';
					break;
				case "TrainStation":
					TrainStation ts = (TrainStation)mapToDraw[line][col];
					if (ts.getColor() == Color.RED) {
						charMap[line][col] = '1';
					}
					if (ts.getColor() == Color.GREEN){
						charMap[line][col] = '2';
					}
					if (ts.getColor() == Color.BLUE){
						charMap[line][col] = '3';
					}

				default:
					charMap[line][col] = ' ';
					break;
				}
			}
		}
		
		/*------------------- Id�ig csak a statikus p�lya ki�r�sa volt, innet�l j�n a vonat berak�sa -------------------*/
		
		for (int line = 0; line < maxY + 1; line++) {	/* foglalt s�nt �t�rjuk 'V'-re (V mint Vonat) Sajnos a s�neknek fogalmuk sincs pontosan mi l�pett r�juk */
			for (int col = 0; col < maxX + 1; col++) {
				if (mapToDraw[line][col] == null) {
					continue;
				}
				if (mapToDraw[line][col].checkIfOccupied()) {
					if (charMap[line][col] != 'T') {		/* Alag�tban nem l�tsz�dhat a vonat. Ha a bet� eredetilet 'T' volt, meghagyjuk */
						charMap[line][col] = 'V';
					}
				}
			}
		}
		
		
		for (char[] cs : charMap) {			/* maga a konzolra �r�s */
			for (char c : cs) {
				System.out.print(c);
			}
			System.out.println();
		}
	}

}
