package sandbox;

import java.awt.Graphics;
import java.util.ArrayList;

public class TrainView {
	ArrayList<TrainBlock> train;
	
	private String changeLog="";
	
	private int nextVisibility=1; 

	public TrainView(ArrayList<String> list) {
		train=new ArrayList<TrainBlock>();
		for(String type:list){
			train.add(new TrainBlock(type));
		}
	}

	public void setAngle(double a){
		for(TrainBlock block: train){
			block.setAngle(a);
		}
	}
	
	public void setPos(int lastX, int lastY,int currentX, int currentY, int nextX, int nextY){
		for(TrainBlock block:train){
			block.setPos(lastX,lastY,currentX,currentY,nextX,nextY);
		}
	}
		
	public void updatePos(int nextX, int nextY){
		for(int i=train.size()-1;i>0;i--){
			TrainBlock next = train.get(i-1);
			train.get(i).updatePos(next.getNextPosX(), next.getNextPosY());
		}	
		train.get(0).updatePos(nextX, nextY);

	}
	
	public void updateTime(int currentTime){
		for(TrainBlock block:train){
			block.updateTime(currentTime);
			changeLog+=block.getChangeLog();
		}
	}
	
	public void setVisibility(int v){		
		for(int i=train.size()-1;i>0;i--){
			TrainBlock next = train.get(i-1);
			train.get(i).setVisibility(next.getVisibility());
		}	
		train.get(0).setVisibility(nextVisibility);
		nextVisibility=v;
		
	}
	
	public String getChangeLog(){
		String log = changeLog;
		changeLog="";
		return log;
	}

	public void draw(Graphics g){
		for(int i = train.size()-1;i>=0;i--){
			if(train.get(i).getVisibility()==1)train.get(i).draw(g);
		}
	}

	public void setCabsState(String states){
		if(states.length()<1)return;
		
		String[] state = states.split("");
		for(int i=1;i<state.length;i++){
			
			if(state[i].equals("E"))train.get(i).emptyCab();
			else train.get(i).fillCab();
		}
	}
}
