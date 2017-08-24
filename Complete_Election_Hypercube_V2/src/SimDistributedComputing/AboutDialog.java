package SimDistributedComputing;

import java.awt.GridLayout;

import javax.swing.JDialog;
import javax.swing.JLabel;

public class AboutDialog extends JDialog{
	
	private int width=200,height=250;
	private Data data;
	private JLabel label1,label2,label3,label4,label5,label6;
	
	public AboutDialog(Data d){
		data=d;
		this.setTitle("About");		
		this.setSize(width, height);
		this.setLayout(new GridLayout(8,1));
		this.setLocation((data.getScreenSize().width - width) / 2, (data.getScreenSize().height - height) / 2);
		this.setResizable(false);
		
		label1=new JLabel("  Group Member:");
		this.add(label1);
		label2=new JLabel("    Yiheng, Zhao(8332912)");
		this.add(label2);
		label3=new JLabel("    Jiaqi, Xu(7994003)");
		this.add(label3);
		label6=new JLabel("");
		this.add(label6);
		label4=new JLabel("  Software Version:");
		this.add(label4);
		label5=new JLabel("    1.2.1");
		this.add(label5);
	}
}
