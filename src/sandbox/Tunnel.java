package sandbox;

import java.util.ArrayList;

/**
 * Speciális sín ami két alagútszáj között helyezkedik el. Ez a sín mindenféle következmény nélkül keresztezhet más síneket. 
 */
public class Tunnel extends Rail {
	public Tunnel(){
		System.out.println("Class: Tunnel\t Object: "+this+"\t Method: Constructor\t ");
	}
	
	public Tunnel(int X, int Y){
		super(X,Y);
		System.out.println("Class: Tunnel\t Object: "+this+"\t Method: Constructor\t ");
	}
}
