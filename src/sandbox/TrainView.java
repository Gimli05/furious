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
		for(TrainBlock block:train){
			if(block.getVisibility()==1)block.draw(g);
		}
	}
}

// Threads
