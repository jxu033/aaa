package SimDistributedComputing;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
public class Data {
	
	private Dimension screen=null;
	private int moduletype=0;  //o=dimension; 1=distance;
	private int speed=50;  //speed for animation
	public int currentcomputelevel=0;  //for showing graph
	
	//store the data to show on the graph:
	//first dimension shows the number of data
	//second dimension shows number of entities in the network and total messages
	private int[][] datalist=null;  
	private Network network;
	
	public Data(){
		Toolkit kit = Toolkit.getDefaultToolkit();  
        screen = kit.getScreenSize();
        network=new Network(2,network.STRUCTURE_HYPERCUBE);  
	}
	
	public Dimension getScreenSize(){
		return screen;
	}
	
	public void setModule(int type){
		moduletype=type;
	}
	
	public int getModule(){
		return moduletype;
	}
	
	public void setSpeed(int i){
		if(i>0)
			speed=i;
	}
	
	public int getSpeed(){
		return speed;
	}
	
	public Network getNetwork(){
		return network;
	}
	
	public void setDataList(int[][] list){
		datalist=list;
	}
	
	public int[][] getDataList(){
		return datalist;
	}
}
