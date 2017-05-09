package sandbox;

import java.awt.Graphics;
import java.util.ArrayList;

/**
 * Egyetlen voant megjeleniteseert felleos osztlya
 * @author Long
 *
 */
public class TrainView {
	/**Tarolt vagonok**/
	ArrayList<TrainBlock> train;
	
	/**Valtozasok listaja**/
	private String changeLog="";
	
	/**Lathatosag**/
	private int nextVisibility=1; 

	/**Szineket tartalmazo szoveglistaval inicializalo konstruktor*/
	public TrainView(ArrayList<String> list) {
		train=new ArrayList<TrainBlock>();
		/*Minden színt hozzaadunk*/
		for(String type:list){
			train.add(new TrainBlock(type));
		}
	}

	/**Beallitja a vagonok szoget**/
	public void setAngle(double a){
		for(TrainBlock block: train){
			block.setAngle(a);
		}
	}
	
	/**Adot poziciora tesz minden vagont**/
	public void setPos(int lastX, int lastY,int currentX, int currentY, int nextX, int nextY){
		for(TrainBlock block:train){
			block.setPos(lastX,lastY,currentX,currentY,nextX,nextY);
		}
	}
	
	/**A kovetkezo pozici ismereteben beallita minden vagon helyet**/
	public void updatePos(int nextX, int nextY){
		/*Lancszeruen mindig a kovetkezo vagon helyere helyezi a blokkokat*/
		for(int i=train.size()-1;i>0;i--){
			TrainBlock next = train.get(i-1);
			train.get(i).updatePos(next.getNextPosX(), next.getNextPosY());
		}	
		/*Az elso vagon az uj helyet kapja*/
		train.get(0).updatePos(nextX, nextY);

	}
	
	/** Az ido mulasaval minden blokkot leptetunk**/
	public void updateTime(int currentTime){
		for(TrainBlock block:train){
			block.updateTime(currentTime);
			changeLog+=block.getChangeLog();
		}
	}
	
	/** Lancszeruen allitjuk a lathatosagot, az alagut miatt, mint a pozicio**/
	public void setVisibility(int v){		
		for(int i=train.size()-1;i>0;i--){
			TrainBlock next = train.get(i-1);
			train.get(i).setVisibility(next.getVisibility());
		}	
		train.get(0).setVisibility(nextVisibility);
		nextVisibility=v;
		
	}
	
	/**Lekerjuk a valtozasok listajat**/
	public String getChangeLog(){
		String log = changeLog;
		changeLog="";
		return log;
	}
	
	/**Kirjzoljuk**/
	public void draw(Graphics g){
		for(int i = train.size()-1;i>=0;i--){
			if(train.get(i).getVisibility()==1)train.get(i).draw(g);
		}
	}

	/**A bemeno szovegtol fuggoen beallitjuk hogy a z adott vagonban van e utas**/
	public void setCabsState(String states){
		if(states.length()<1)return;
		
		String[] state = states.split("");
		for(int i=1;i<state.length;i++){
			/*Ha "empty" akkor nincs, amugy van*/
			if(state[i].equals("E"))train.get(i).emptyCab();
			else train.get(i).fillCab();
		}
	}
}
