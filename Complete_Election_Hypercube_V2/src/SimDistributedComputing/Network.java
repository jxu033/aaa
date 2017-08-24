package SimDistributedComputing;

import java.util.ArrayList;
import example.*;
//this class is used to build the whole network according to user's algorithm
//this class also stores important data that used for display
//some signals will be defined in this class. Users can add as many as what they want.
public class Network {

	private ArrayList<Entity> nodes; //list to store entities
	private ArrayList<Message> messages;  //list to store total current messages that will be shown on the screen
	private ArrayList<Message> waiting_messages;  //list to store current suspended messages
	
	private int messagenum = 0; // store the total message cost for the whole process
	private int dimension = 1; //this is used for some specific network structures
	private int linknum = 1; //total link number in the network
	private int entitynum = 0; //total entity number in the network
	private ArrayList<Integer> idlist; // Store the random ID of entities, in order to assign them

	//number of entities that don't finish. To decided whether the whole network completes.
	public int activeentity = 0; 
	public float timekeeper = 0; //for count time by unit (0.2s/unit)
	public int TYPE = -1; // different structure of network
	public boolean terminate = true; // whether the process completes
	public boolean syn = false; // whether the entities start together
	public boolean animation = true; // if showing animation, message will be
										// delayed for certain time
	public boolean compress = true; //whether compress the path

	//this part is for test use. Assign ID to every node artificially
//	private int[] test={1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16};
//	private int i=0;
	// ----------------------------------------------
	// definition of some signal
	// ----------------------------------------------
	// signal for message
	static final public int M_WAKEUP = 0, M_ATTACK = 1, M_LEADER = 2;
	// signal for state of entity
	static final public int S_ASLEEP = 0, S_DUELLIST = 1, S_SECOND = 2, S_FOLLOW = 3, S_LEADER = 4;
	// signal for type of protocol
	static final public int PROTOCOL_HYPERCUBE_DIMENSION = 0;
	// signal for structure of network
	static final public int STRUCTURE_HYPERCUBE = 0, STRUCTURE_RING = 1, STRUCTURE_COMPLETE_GRAPH = 2;

	public Network() {
		nodes = new ArrayList();
		messages = new ArrayList();
		waiting_messages = new ArrayList();
		idlist = new ArrayList();
	}

	public Network(int NUMBER_OF_ENTITIES, int NETWORK_TYPE) {

		nodes = new ArrayList();
		messages = new ArrayList();
		waiting_messages = new ArrayList();
		idlist = new ArrayList();

		//default: we use hypercube structure
		if (NUMBER_OF_ENTITIES > 1 && NETWORK_TYPE > -1) {
			TYPE = NETWORK_TYPE;
			setEntityNum(NUMBER_OF_ENTITIES);
		} else {
			setEntityNum(2);
		}
	}

	public void buildNetwork() {
		buildNetwork(TYPE);
	}

	public void buildNetwork(int structure_type) {
		switch (structure_type) {
		case STRUCTURE_HYPERCUBE:
			Hypercube();
			break;
		case STRUCTURE_RING:
			Ring();
			break;
		case STRUCTURE_COMPLETE_GRAPH:
			CompleteGraph();
			break;
		default:
			break;
		}
	}

	public void createEntity() { // need to initial other nature
		ElectionHypercubeNode n = new ElectionHypercubeNode(this);
		n.setSynchronize(syn);
		n.compressPath(compress);
		n.setvisualization(animation);
				
		// id of entity can be created randomly
		int pos = (int) (Math.random() * idlist.size());
		n.setID(idlist.get(pos));
		idlist.remove(pos);
/*		n.setID(test[i]); // this is for test
		i++;*/
		nodes.add(n);
		
		// self-wakeup time can be randomly
		int time = (int) (Math.random() * 20) * 100;
		n.setStartTime(time);
	}

	// entity's number is the same as previous one
	public void resetEntity() {
		setEntityNum(entitynum);
	}
	
	//this is for data analysis
	public void setEntityNum(int num, boolean animation){
		this.animation=animation;
		setEntityNum(num);
	}

	public void setEntityNum(int num) {
		if (num > 0) {
			entitynum = num;
			messagenum = 0;
			nodes.clear();
			messages.clear();
			//i=0; // this is for test
			waiting_messages.clear();
			this.timekeeper=0;
			
			for (int i = 0; i < num; i++) {
				idlist.add(i + 1);
			}
			for (int i = 0; i < num; i++)
				createEntity();
			buildNetwork();
		}
	}

	public int getEntityNum() {
		return nodes.size();
	}

	public int getLinkNum() {
		return linknum;
	}

	public int getDimension() {
		return dimension;
	}

	// check whether the algorithm is finished
	// depending on different module, the algorithm is different
	public boolean isTerminated() {
		// if there are still some messages moving, the network is not
		// terminated
		if (animation) {
			if (messages.size() > 0) {
				terminate = false;
				return false;
			}
			// if some nodes are still working, the network is not terminated
			for (int i = 0; i < nodes.size(); i++)
				if (!nodes.get(i).terminate) {
					terminate = false;
					return false;
				}
			terminate = true;
			return true;
		} else {
			//System.out.println("activeentitynum="+activeentity+", activethread="+(Thread.activeCount()-3));
			if (activeentity == 0) {				
				terminate = true;
				return true;
			}
			terminate = false;
			return false;
		}
	}

	// pause the whole network
	public void suspend() {
		// suspend entities
		for (int i = 0; i < nodes.size(); i++) {
			nodes.get(i).suspendProcess(true);
		}
		// suspend messages
		for (int i = 0; i < messages.size(); i++) {
			messages.get(i).suspend(true);
		}
	}

	// start from previous stop
	public void continued() {
		// suspend entities
		for (int i = 0; i < nodes.size(); i++) {
			nodes.get(i).suspendProcess(false);
		}
		// suspend messages
		for (int i = 0; i < messages.size(); i++) {
			messages.get(i).suspend(false);
		}
	}
	
	//complete graph
	private void CompleteGraph(){
		int centerx = 240, centery = 240, r=225,num=0; //position of the circle
		//these two below are used to compute location of each node
		double radian = 6.28/entitynum,ang=0; 
		
		for(int i=0;i<nodes.size();i++){	
			//first step: allocate position for each node
			nodes.get(i).getPosition().x = (int) (r * Math.cos(ang)) + centerx;
			nodes.get(i).getPosition().y = (int) (r * Math.sin(ang)) + centery;
			ang+=radian;
			//second step: create links for nodes
			for(int j=0;j<nodes.size();j++){
				if(i!=j){
					Link link = new Link(nodes.get(j));
					nodes.get(i).getLinkList().add(link);	
					num++;
				}
			}
		}
		linknum=num;
	}
	
	//build the ring structure
	private void Ring(){
		int centerx = 240, centery = 240, r=225; //position of the circle
		//these two below are used to compute location of each node
		double radian = 6.28/entitynum,ang=0; 
				
		for(int i=0;i<nodes.size()-1;i++){	
			//first step: allocate position for each node
			nodes.get(i).getPosition().x = (int) (r * Math.cos(ang)) + centerx;
			nodes.get(i).getPosition().y = (int) (r * Math.sin(ang)) + centery;
			ang+=radian;
			//second step: create links for nodes
			Link link1 = new Link(nodes.get(i));
			Link link2 = new Link(nodes.get(i+1));
			nodes.get(i+1).getLinkList().add(link1);
			nodes.get(i).getLinkList().add(link2);
			
		}
		//allocate last node
		nodes.get(nodes.size()-1).getPosition().x = (int) (r * Math.cos(ang)) + centerx;
		nodes.get(nodes.size()-1).getPosition().y = (int) (r * Math.sin(ang)) + centery;
		
		//the first and last node should be linked
		Link link1 = new Link(nodes.get(0));
		Link link2 = new Link(nodes.get(nodes.size()-1));
		nodes.get(nodes.size()-1).getLinkList().add(link1);
		nodes.get(0).getLinkList().add(link2);
		
		//computer the total 
		linknum=nodes.size();
	}

	// build the connection among entities
	private void Hypercube() {
		int num, centerx = 240, centery = 240, r=225; //position of the circle
		//these two below are used to compute location of each node
		double radian = 6.28/entitynum,ang=0;
		
		//allocate node position
		for (int i = 0; i < nodes.size(); i++) {			
			nodes.get(i).getPosition().x = (int) (r * Math.cos(ang)) + centerx;
			nodes.get(i).getPosition().y = (int) (r * Math.sin(ang)) + centery;
			ang+=radian;
		}

		for (int i = 1; i <= dimension; i++) {
			for (int j = 0; j < nodes.size(); j++) {
				if (nodes.get(j).getLinkList().size() < i) {
					num = (int) (j + Math.pow(2, i - 1));
					Link link1 = new Link(nodes.get(num));
					Link link2 = new Link(nodes.get(j));

					// ------------------
					// set label for each link (dimensional)
					// if user want to use other ways to label the link
					// please change this section
					// ------------------
					link1.label = i;
					link2.label = i;
					nodes.get(j).getLinkList().add(link1);
					nodes.get(num).getLinkList().add(link2);
				}
			}
		}
		//compute the total number of link
		linknum = dimension * nodes.size();
	}

	public ArrayList<Entity> getEntityList() {
		return nodes;
	}

	public void setDimension(int i) {
		if (i > 0) {
			dimension = i;
		}
	}

	public ArrayList<Message> getMessageList() {
		return messages;
	}

	public ArrayList<Message> getWaitingMessages() {
		return waiting_messages;
	}

	public void setMessageList(ArrayList<Message> list) {
		messages = list;
	}

	// start the algorithm from the beginning
	public void start() {
		terminate = false;
		for (int i = 0; i < nodes.size(); i++) {
			Thread thread = new Thread(nodes.get(i));
			thread.start();
		}
		activeentity = nodes.size();
	}

	// round of: 3.4=3; 3.6=4; 3.5=4
	public int round(float num) {
		int n = (int) num;
		return num - 0.5 >= n ? n + 1 : n;
	}

	public int getMsgNum() {
		return messagenum;
	}

	public void addMessage(Message m) {
		messages.add(m);
		messagenum++;
	}
	
	public void addMessage(){
		messagenum++;
	}
}
