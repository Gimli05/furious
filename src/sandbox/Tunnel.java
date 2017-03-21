package sandbox;

import java.util.ArrayList;

/**
 * Speci�lis s�n ami k�t alag�tsz�j k�z�tt helyezkedik el. Ez a s�n mindenf�le k�vetkezm�ny n�lk�l keresztezhet m�s s�neket. 
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
