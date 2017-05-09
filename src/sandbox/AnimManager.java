package sandbox;
import java.awt.Graphics;
import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;

/**Tobb animaciot tartol es azaokat kezeli**/
public class AnimManager {
	
	/**Animaciokat trolo tomb**/
	private ArrayList<Animation> animations;
	
	/**konstruktor**/
	public AnimManager(){
		animations=new ArrayList<Animation>();
	}
	
	/*Hozzaad egy kert animaciot a tombunkhoz**/
	public void addAnimation(int x, int y, String string) {
		Animation anim = new Animation(x, y, string);
		animations.add(anim);
	}

	/**kivszi az adott helyen levo adott tipusu animaciot**/
	public void removeAnimation(int x, int y, String type){
		Animation rem=null;
		/*Ha Xben Yban es tipusban megeggyezik akkor toroljuk*/
		for(Animation anim: animations){
			if(anim.getX()==x && anim.getY()==y && anim.getType()==type){
				rem=anim;
				break;
			}
		}
		animations.remove(rem);
	}
	
	/**Vegig nezi a lstita hogy an e animacio ami veget ert es ha igen akor kiveszi a listabol**/
	public void updateAnimations(){
		boolean finished=false;
		Animation rem=null;
		/*Amig a kereses nem ert veget*/
		while(!finished){
			finished=true;
			rem=null;
			/*Ha mindeki fut akkor a kereses veget er*/
			for(Animation anim:animations){
				/*Ha van valaki aki nem fut akkor nem  ertun ka vegere a tombnek*/
				/*Ilyenkor majd ujra kell keresni*/
				if(anim.ended){
					rem=anim;
					finished=false;
					break;
				}
			}
			/*Ha talaltunk valakit akkor toroljuk*/
			if(rem!=null)animations.remove(rem);
		}
	}
	
	/**Beallita az adott tarolon a veltozott pontokat**/
	public void setUpdatedTiles(boolean[][] changeMap){
		for(Animation anim: animations){
			anim.setUpdatedTiles(changeMap);
		}
	}
	
	/**MInde animacior kirajzol es ha meg nem fut elidnita**/
	public void draw(Graphics g){	
		try{
		if(animations.size()>0){
			for(int i=0;i<animations.size();i++){
				/*Ha valaki nem fut elidnitja*/
				if(animations.get(i)!=null)
				if(!animations.get(i).started())animations.get(i).start();
				if(animations.get(i).started()){
					/*Lekerjuk az uj kepcokat es rajzoljuk*/
					if(!animations.get(i).ended())animations.get(i).draw(g);
					else{
						/*Ha veget ert kiszedjuk a tarolobol*/
						animations.remove(i);
						i--;
					}
				}
			}
		}
		}catch(IndexOutOfBoundsException e){
			System.out.println("idx Error" );
		}
		
	}
	
	/**MInden animaciot megallitunk es toroljuk oket**/
	public void endAllAnimation(){
		for(Animation anim:animations){
			anim.endFreeze();
			anim.endLoop();
		}
		updateAnimations();
		if(animations.size()>0){
			animations.clear();
		}
	}
}
