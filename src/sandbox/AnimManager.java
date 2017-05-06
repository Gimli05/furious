package sandbox;
import java.awt.Graphics;
import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;

public class AnimManager {
	
	private ArrayList<Animation> animations;
	
	public AnimManager(){
		animations=new ArrayList<Animation>();
	}
	
	public void addAnimation(int x, int y, String string) {
		Animation anim = new Animation(x, y, string);
		animations.add(anim);
	}

	public void removeAnimation(int x, int y, String type){
		Animation rem=null;
		for(Animation anim: animations){
			if(anim.getX()==x && anim.getY()==y && anim.getType()==type){
				rem=anim;
				break;
			}
		}
		animations.remove(rem);
	}
	
	public void updateAnimations(){
		boolean finished=false;
		Animation rem=null;
		while(!finished){
			finished=true;
			rem=null;
			for(Animation anim:animations){
				if(anim.ended){
					rem=anim;
					finished=false;
					break;
				}
			}
			if(rem!=null)animations.remove(rem);
		}
	}
	
	public void setUpdatedTiles(boolean[][] changeMap){
		for(Animation anim: animations){
			anim.setUpdatedTiles(changeMap);
		}
	}
	
	public void draw(Graphics g){	
		try{
		if(animations.size()>0){
			for(int i=0;i<animations.size();i++){
				if(animations.get(i)!=null)
				if(!animations.get(i).started())animations.get(i).start();
				if(animations.get(i).started()){
					if(!animations.get(i).ended())animations.get(i).draw(g);
					else{
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
