package SimDistributedComputing;

import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.JLabel;

public class ToolTipPanel extends JPanel{
	
	private int width=100,height=90;
	private JLabel id,state,level,nextduellist,source;
	
	public ToolTipPanel(){
		this.setSize(width, height);
		this.setBorder(BorderFactory.createLineBorder(Color.black));
		this.setLayout(null);
		
		id=new JLabel("null");
		id.setLocation(5, 5);
		id.setSize(90, 20);
		this.add(id);
		
		state=new JLabel("null");
		state.setLocation(5, 45);
		state.setSize(90, 20);
		this.add(state);
		
		level=new JLabel("null");
		level.setLocation(5, 25);
		level.setSize(90, 20);
		this.add(level);
		
		nextduellist=new JLabel("null");
		nextduellist.setLocation(5, 65);
		nextduellist.setSize(90, 20);
		this.add(nextduellist);
		
		source=new JLabel("null");
		source.setLocation(5, 45);
		source.setSize(90, 20);
		source.setVisible(false);
		this.add(source);
	}
	
	public void setInfo(int id, int level, int[] source,int[] dest){
		state.setVisible(false);
		this.source.setVisible(true);
		
		this.id.setText("ID: "+id+"");
		this.level.setText("LEVEL: "+level+"");
		String str1="",str2="";
		for(int i=0;i<source.length;i++){
			str1=str1+source[i];
			str2=str2+dest[i];
		}
		this.source.setText("SOURCE: "+str1);
		this.nextduellist.setText("DEST: "+str2);
	}
	
	public void setInfo(int id, int state, int level, int[] nextduellist){
		this.state.setVisible(true);
		source.setVisible(false);
		
		this.id.setText("ID: "+id+"");
		this.level.setText("LEVEL: "+level+"");
		if(nextduellist==null)
			this.nextduellist.setText("NEXT: null");
		else{
			String str="";
			for(int i=0;i<nextduellist.length;i++)
				str=str+nextduellist[i]+"";
			this.nextduellist.setText("NEXT: "+str);
		}
		
		switch(state){
		case 0: 
			this.state.setText("STATE: ASLEEP");
			break;
		case 1:
			this.state.setText("STATE: DUEL");
			break;
		case 2:
			this.state.setText("STATE: SECOND");
			break;
		case 3:
			this.state.setText("STATE: FOLLOW");
			break;
		case 4:
			this.state.setText("STATE: LEADER");
			break;
		default:
			this.state.setText("NULL");
			break;
		}
	}
}
