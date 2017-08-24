package example;

import java.util.ArrayList;

import SimDistributedComputing.*;
//this class is used to achieve specific algorithm
public class ElectionHypercubeNode extends Entity {

	private int level = 0;
	private ArrayList<Integer> delete;
	
	//signal to judge whether use compress method
	private boolean compress=true;

	//In order to show both with path compress method and without path compress method
	//We use int[] instead of byte[]
	public int nextduellist[] = null;
	//public boolean animation = true; // showing animation or not

	public ElectionHypercubeNode(Network p) {

		parent = p;
		nextduellist = new int[parent.getDimension()];		
		delete = new ArrayList();

		for (int i = 0; i < parent.getDimension(); i++) {
			nextduellist[i] = 0;
		}
	}

	protected void send(Message msg, Link link) {
		((ElectionHypercubeMessage)msg).setAttribute(position, link, parent);		
		if(visualization)
			parent.addMessage(msg);
		else{
			//no need for message running	
			link.neighbor.addMessage(msg);
			parent.addMessage();  //only count of message cost++
			//System.out.println(id+" send message "+m.type+" to "+ l.neighbor.getID()+" through "+l.label);
		}
	}

	protected Message receive() {
		if (received_messages.size() > 0) {			
			return received_messages.poll();
		} else
			return null;
	}

	// in the wake_up step, every awake node will directly send ATTACK to link labeled 1
	protected void WakeUp() {
			level = 0; // id, source[] and dest[] have already been
						// initialed
			for (int i = 0; i < links.size(); i++) {
				if (links.get(i).label == 1) {
					synchronized (parent) {
						ElectionHypercubeMessage m = new ElectionHypercubeMessage();
						m.setAttribute(parent.M_ATTACK, id, level);
						send(m, links.get(i));
					}
					break;
				}
			}
			state = parent.S_DUELLIST;
	}

	protected void protocol() {
		ElectionHypercubeMessage msg, m;
		if ((msg = (ElectionHypercubeMessage) receive()) != null) {
			System.out.println(id+" receive message "+msg.type+" from"+ msg.id);
			switch (state) {
			case 0: // S_ASLEEP: the most algorithm of this part is achieved in wake_up state
				// if message!=WAKEUP then delay message
				state = parent.S_DUELLIST;
				level = 0; // id, source[] and dest[] have already been initialed
				if (msg.type != parent.M_WAKEUP) 
					delayMessage(msg);								
				break;

			case 1: // S_DUELLIST
				if (msg.type == parent.M_ATTACK && level == msg.lev) {					
					if (id > msg.id) {
						//***************************************************
						//In the original algorithm msg.lev == n, which should
						//be replaced by msg.lev == n-1. otherwise, the algorithm
						//can not terminate
						//***************************************************
						if (msg.lev == links.size() - 1) { 
							state = parent.S_LEADER;
							System.out.print("Leader "+ id+" send term to ");
							for (int i = 0; i < links.size(); i++) {
								synchronized (parent) {
									m = new ElectionHypercubeMessage();
								}
								m.type = parent.M_LEADER;
								m.id = id;
								send(m, links.get(i));
								System.out.print(links.get(i).neighbor.getID()+", ");
							}
							System.out.println();
							
							parent.activeentity--; // current awake entity--
							terminate = true;
						} else {
							level++;
							for (int i = 0; i < links.size(); i++) {
								//***************************************************
								//In the original algorithm label == r + 1, which should
								//be replaced by level + 1. Otherwist, the algorithm
								//will get into deadlock in some specific id order.
								//***************************************************
								if (links.get(i).label == level + 1) { 
									synchronized (parent) {
										m = new ElectionHypercubeMessage();
									}
									m.setAttribute(parent.M_ATTACK, id, level);
									send(m, links.get(i));
									break;
								}
							}

							// accept delayed message with lev = levelp if arrived
							acceptMessage();
						}
					} else if (id < msg.id) {
						state = parent.S_SECOND;
						// reverse
						synchronized (parent) {
							nextduellist = msg.source.clone();
						}
						
						if(!compress){
						//this version doesn't use path compress
							nextduellist[msg.labeloflink - 1]++;
						} else {
						//this version use path compress
							reverse(nextduellist, msg.labeloflink - 1);
						}
						
						// accept all delayed messages if any
						acceptAllDelayMsg();
					}
				} else if (msg.type == parent.M_ATTACK && msg.lev > level) {
					delayMessage(msg);
				} else if (msg.type == parent.M_LEADER) {
					state = parent.S_FOLLOW;
					System.out.print("Duellist "+ id+" send term to ");
					for (int i = 0; i < links.size(); i++) {
						if (msg.labeloflink > links.get(i).label) {
							synchronized (parent) {
								m = new ElectionHypercubeMessage();
							}
							m.setAttribute(parent.M_LEADER, msg.id, level);
							send(m, links.get(i));
							System.out.print(links.get(i).neighbor.getID()+", ");
						}
					}
					System.out.println();
					parent.activeentity--; // current awake entity--
					terminate = true;
				}
				break;

			case 2: // S_SECOND
				if (msg.type == parent.M_ATTACK) {
					if (!isEmpty(msg.dest)) {
												
						int l = first(msg.dest);
						if(!compress){
							//this version doesn't use path compress
							msg.dest[l]--;
							msg.source[msg.labeloflink - 1]++;
						} else {
						//this version use path compress
							reverse(msg.dest, l);
							reverse(msg.source, msg.labeloflink - 1); // source:=source+r*/
						}
						
						for (int i = 0; i < links.size(); i++) {
							if (links.get(i).label == l + 1) {
								send(msg, links.get(i));
								break;
							}
						}
					} else { // if dest=[]
						synchronized (parent) {
							msg.dest = nextduellist.clone();
						}
						int l = first(msg.dest);
						
						if(!compress){
						//this version doesn't use path compress
							msg.dest[l]--;
							msg.source[msg.labeloflink - 1]++;
						} else {
						//this version use path compress
							reverse(msg.dest, l);
							reverse(msg.source, msg.labeloflink - 1);
						}
						
						for (int i = 0; i < links.size(); i++) {
							if (links.get(i).label == l + 1) {
								send(msg, links.get(i));
								break;
							}
						}
					}
				} else if (msg.type == parent.M_LEADER) {
					state = parent.S_FOLLOW;
					System.out.print("Second "+ id+" send term to ");
					for (int i = 0; i < links.size(); i++) {
						if (msg.labeloflink > links.get(i).label) {
							synchronized (parent) {
								m = new ElectionHypercubeMessage();
							}
							m.setAttribute(parent.M_LEADER, msg.id, level);
							send(m, links.get(i));
							System.out.print("<"+links.get(i).neighbor.getID()+">, ");
						}
					}
					System.out.println(id+" dead");
					parent.activeentity--; // current awake entity--
					terminate = true;
				}
				break;

			default:
				break;
			}
		}
	}

	//improvement
	private void acceptMessage() {
		for (int i = 0; i < wait_messages.size(); i++) {
			if (level == ((ElectionHypercubeMessage)wait_messages.get(i)).lev) {
				received_messages.offer(wait_messages.get(i));				
				delete.add(i);
				if(visualization)
					parent.getWaitingMessages().remove(wait_messages.get(i));
			}
		}
		for (int i = delete.size()-1; i >-1; i--)
			wait_messages.remove(delete.get(i));
		delete.clear();
	}

	private void acceptAllDelayMsg() {
		for (int i = 0; i < wait_messages.size(); i++) {
			received_messages.offer(wait_messages.get(i));			
			if(visualization)
				parent.getWaitingMessages().remove(wait_messages.get(i));
		}
		wait_messages.clear();
	}

	// whether a given list empty
	private boolean isEmpty(int[] list) {
		for (int i = 0; i < parent.getDimension(); i++)
			if (list[i] != 0)
				return false;
		return true;
	}

	private int first(int[] list) { // leftmost
		for (int i = 0; i < parent.getDimension(); i++)
			if (list[i] != 0)
				return i;
		return -1;
	}

	//for path compress
	private void reverse(int[] list, int pos) {
		list[pos] = (1 - (int) list[pos]);
	}

	public int getLevel() {
		return level;
	}
	
	public void compressPath(boolean bool){
		compress=bool;
	}

}
