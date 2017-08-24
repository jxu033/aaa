package SimDistributedComputing;
import java.awt.Point;
public abstract class Message{
	
	protected Point start,end,position;
	private Link link;
	private float dx=0,dy=0;
	private boolean suspend=false;
	
	//in order to compute the real position to avoid the inaccurate of float->int in position
	private float r_x=0, r_y=0;  
		
	// speed is based on millisecond, the smaller, the faster
	static private int speed=50;
	private int times=speed;  //compute how many times should the message move
	protected Network parent;
	
	public Message(){}
	
	public void setMessage(Point s, Link l, Network p){
		position=new Point(s);
		r_x=position.x;
		r_y=position.y;
		parent=p;		
		start=s;				
		link=l;
		end=link.neighbor.getPosition();	
		times=speed;
		
		dx=(end.x-start.x)/(float)speed;
		dy=(end.y-start.y)/(float)speed;
		
		//before running, add this message into the message list
	}
	
	public Link getLink(){
		return link;
	}
	
	//this function is to change the speed of message delivered from one side to the other
	public void setSpeed(int s){
		speed=s;
		dx=(end.x-start.x)/(float)speed;
		dy=(end.y-start.y)/(float)speed;
	}
	
	//refreshing the position of this message, in order to show animation
	//the motion trail is linear
	 public void move(){		
		//the content of message list is always changing
		 
		//in the simulation module, there is no need for animation.
		 //So, the message will be sent to the nodes directly
		 if(!suspend){
			 if(times<0){
			//after processing, remove it from total messagelist			
				 link.neighbor.addMessage(this);
				 parent.getMessageList().remove(this);
			 } else {
				 r_x+=dx;
				 r_y+=dy;
				 position.x=parent.round(r_x);
				 position.y=parent.round(r_y);
				 if(times==0){
					 position.x=end.x;
					 position.y=end.y;
				 }
				 times--;
			 }	
		 }
	}
	 
	 public void suspend(boolean bool){
		 suspend=bool;
	 }
}
