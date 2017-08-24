package SimDistributedComputing;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JButton;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InfoPanel extends JPanel{
	
	private int width=500,height=70;
	private Data data;

	private JButton start;
	private MainFrame parent;
	private boolean signal=false;  //judge whether the algorithm running
	
	public InfoPanel(MainFrame p){
		this.setSize(width, height);
		this.setLayout(null);
		this.setBorder(BorderFactory.createTitledBorder("State Cases:"));
		
		parent=p;
		data=parent.getData();
		
		start=new JButton("start");
		start.setLocation(390,30);
		start.setSize(90, 20);
		this.add(start);
		start.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	startEvent();
            }
        });			
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		//asleep
		g.setColor(Color.gray);
		g.fillRect(20, 25, 10, 10);
		g.drawString("Asleep", 40, 35);
		
		//duellist
		g.setColor(Color.blue);
		g.fillRect(100, 25, 10, 10);
		g.drawString("Duellist", 120, 35);
		
		//second
		g.setColor(Color.green);
		g.fillRect(180, 25, 10, 10);
		g.drawString("Second", 200, 35);
		
		//leader
		g.setColor(Color.yellow);
		g.fillRect(20, 45, 10, 10);
		g.drawString("Leader", 40, 55);
		
		//follow
		g.setColor(Color.black);
		g.fillRect(100, 45, 10, 10);
		g.drawString("Follow", 120, 55);
		
		//message
		g.setColor(Color.red);
		g.fillRect(180, 45, 10, 10);
		g.drawString("Message", 200, 55);
	}
	
	private void startEvent(){
		if(signal){	
			//when the process has been stopped or end
			parent.enabledProp(true);
			parent.enabletab(true);
			end();			
		}else{		
			//when the process starts or continues
			parent.enabledProp(false);
			parent.enabletab(false);
			start();			
		}
	}
	
	public void start(){		
		if(data.getNetwork().terminate){	
			parent.start();
		}else{
			parent.continued();
		}
		start.setLabel("stop");	
		signal=true;
	}
	
	public void end(){
		if(!data.getNetwork().terminate)
			data.getNetwork().suspend();  //need to build
		parent.getDisplayPanel().stop=true;
		parent.getDisplayPanel().updateUI();
		start.setLabel("start");
		parent.enabledProp(true);
		signal=false;
	}
}
