package example;
import java.awt.Point;

import SimDistributedComputing.*;

public class ElectionHypercubeMessage extends Message{
	
	public int id=-1;
	public int lev=-1;
	//in order to show both with path compress method and without path compress method
	//We use int[] instead of byte[]
	public int source[],dest[];
	public int type=0;  //0=WAKEUP; 1=ATTACK; 2=LEADER
	public int labeloflink=0;
	
	public ElectionHypercubeMessage(){}
	
	public ElectionHypercubeMessage(Point s, Link l, Network p){
		setMessage(s,l,p);
		
		source=new int[parent.getDimension()];
		dest=new int[parent.getDimension()];
		for(int i=0;i<parent.getDimension();i++){
			source[i]=0;
			dest[i]=0;
		}
	}
	
	public void setAttribute(Point s, Link l, Network p){		
		super.setMessage(s,l,p);
		labeloflink=l.label;
		
		if(source==null){
			source=new int[parent.getDimension()];
			for(int i=0;i<parent.getDimension();i++){
				source[i]=0;
			}
		}
		
		if(dest==null){
			dest=new int[parent.getDimension()];
			for(int i=0;i<parent.getDimension();i++){
				dest[i]=0;
			}
		}
	}
	
	public void setAttribute(int t,int i,int l,int s[],int d[]){
		type=t;
		id=i;
		lev=l;
		source=s;
		dest=d;
	}
	
	public void setAttribute(int t,int i,int l){
		type=t;
		id=i;
		lev=l;
	}
}
