package SimDistributedComputing;
//simulation session
//this is for message cost computing
//in order to speed up the process, this class inherent thread function
public class Compute implements Runnable{
	
	private MainFrame parent;
	private Data data;
	
	public Compute(MainFrame p){
		parent=p;
		//data= new Data();
		data=parent.getData();
	}
	
	public void run(){
		parent.setCalculateButton(false);
		data.getNetwork().syn=true;
		for (int i = 0; i < data.getDataList().length; i++) {		
			data.getDataList()[i][0] = (int) Math.pow(2, i + 1);
			//forbid animation
			data.getNetwork().setDimension(i+1);
			data.getNetwork().setEntityNum(data.getDataList()[i][0],false);			
			//in order to speed up the algorithm's process, we use synchronization model			
			data.getNetwork().start();
			//parent.start();
			//waiting for the algorithm completes
			while (!data.getNetwork().isTerminated()) {
				try{Thread.sleep(200);}catch(Exception ex){}
			}
			//exclude messages cost in wake up and notification
			data.getDataList()[i][1] = data.getNetwork().getMsgNum()-data.getDataList()[i][0]+1;
			data.currentcomputelevel=i;
			parent.updateGraph();
			System.out.println("message cost: "+(data.getNetwork().getMsgNum()-data.getDataList()[i][0]+1));
		}
		parent.setCalculateButton(true);
	}
}
