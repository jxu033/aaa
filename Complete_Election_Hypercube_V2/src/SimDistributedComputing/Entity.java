package SimDistributedComputing;

import java.lang.Thread;
import java.awt.Point;
import java.util.Queue;
import java.util.LinkedList;
import java.util.ArrayList;
import example.*;
// this abstract class is mainly to store basic data of entities
// new entity class should override send(), wake(), protocal(), and receive() function with specific algorithm
public abstract class Entity implements Runnable {

	protected int id = -1;
	protected int state = 0;
	protected ArrayList<Link> links;
	protected Point position;
	protected Queue<Message> received_messages; // stores received message
	protected ArrayList<Message> wait_messages;  // stores messages that is delayed
	protected boolean terminate = true, suspend = false, syn = false;	
	protected boolean visualization = true; //whether it is animation or simulation
	protected Network parent;
	private int time = 0;

	public Entity() {
		position = new Point(0, 0);
		links = new ArrayList();
		received_messages = new LinkedList<Message>();
		wait_messages = new ArrayList();
	}

	public void setPosition(int x, int y) {
		position.setLocation(x, y);
	}

	public ArrayList<Link> getLinkList() {
		return links;
	}

	public Point getPosition() {
		return position;
	}

	abstract protected Message receive();
	abstract protected void protocol();
	abstract protected void send(Message m, Link l);
	abstract protected void WakeUp();

	public void run() {
		terminate = false;
		// if module equals visualization
		if (visualization) {
			//this session is to achieve asynchronization by sleep for random time
			if (!syn) {
				int count=10,sleep=time/10;
				//when 
				while(state==parent.S_ASLEEP && count>0){
					try {
						Thread.sleep(sleep);
					} catch (Exception ex) {
						System.out.println("Process Wake up in ID="+id+" is invalid.");
						return;
					}
					count--;
				}
			}
		}
		WakeUp();
		while (!terminate) {
			if (!suspend) {
				protocol();
			}
			// every loop, message moves one unit step
			//in order not to overuse memory, every action need sometime to wait
			//both for animation and non-animation
			try {
				Thread.sleep(20);
			} catch (Exception ex) {
				System.out.println("Process protocol execution in ID="+id+" is invalid.");
				return;
			}
		}
	}
	
	protected void delayMessage(Message msg) {
		wait_messages.add(msg);		
		if(visualization)
			parent.getWaitingMessages().add(msg);
	}

	public void setStartTime(int time) {
		if (time > -1)
			this.time = time;
	}

	public void setSynchronize(boolean bool) {
		syn = bool;
	}

	public void setID(int num) {
		id = num;
	}

	public int getID() {
		return id;
	}

	public void addMessage(Message msg) {
		if (msg != null){
			//if this entity is still sleeping, then it will be waken up
			if(state==parent.S_ASLEEP)
				state=parent.S_DUELLIST;
			received_messages.offer(msg);
		}
	}

	public void endProcess() {
		terminate = true;
	}

	public void suspendProcess(boolean bool) {
		suspend = bool;
	}

	public void setvisualization(boolean bool) {
		visualization = bool;
	}
}
