package SimDistributedComputing;
//import javax.swing.JDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
public class SettingDialog extends JDialog{
	
	private int width=250,height=300;
	private Data data;
	private ButtonGroup module1, module2;
	private JRadioButton syn,asyn,compress,non_compress;//3&4 whether the entities start at the same time
	private JLabel label1,label2,label3,label4,label5;
	private JButton ensure,cancel;
	private JTextField inputnum,totalnum;
	private MainFrame parent;
	
	public SettingDialog(Data d, MainFrame p){
		data=d;
		parent=p;
		
		this.setTitle("Settings");		
		this.setSize(width, height);
		this.setLayout(null);
		this.setLocation((data.getScreenSize().width - width) / 2, (data.getScreenSize().height - height) / 2);
		this.setResizable(false);
		
		label1=new JLabel("Build the network:");
		label1.setBounds(10, 10, 100, 20);
		this.add(label1);
		
		label2=new JLabel("Dimention for Network: (1-5)");  //dimension should not be so large
		label2.setBounds(10, 30, 300, 20);
		this.add(label2);
		
		inputnum=new JTextField();
		inputnum.setBounds(10, 50, 150, 20);
		inputnum.setText("1");
		this.add(inputnum);
		
		label3=new JLabel("Model for Entities:");  //dimension should not be so large
		label3.setBounds(10, 75, 300, 20);
		this.add(label3);
		
		syn=new JRadioButton("synchronization");
		syn.setBounds(10,95,150,20);
		this.add(syn);
				
		asyn=new JRadioButton("asynchronization");
		asyn.setBounds(10,110,150,20);
		this.add(asyn);
		asyn.setSelected(true);
		
		module2=new ButtonGroup();
		module2.add(syn);
		module2.add(asyn);
		
		label4=new JLabel("Model:");
		label4.setBounds(10, 135, 100, 20);
		this.add(label4);
		
		compress=new JRadioButton("compress the path");
		compress.setBounds(10,150,150,20);
		compress.setSelected(true);
		this.add(compress);
				
		non_compress=new JRadioButton("no compression");
		non_compress.setBounds(10,165,150,20);
		this.add(non_compress);
		
		module1=new ButtonGroup();
		module1.add(compress);
		module1.add(non_compress);
		
		label5=new JLabel("Number of Entity (for test):");
		label5.setBounds(10, 190, 200, 20);
		this.add(label5);
		
		totalnum=new JTextField();
		totalnum.setBounds(10, 210, 150, 20);
		totalnum.setText("0");
		totalnum.setEnabled(false);
		this.add(totalnum);
		
		ensure=new JButton("ensure");
		ensure.setBounds(15,245,100,20);
		this.add(ensure);
		ensure.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {    
            	int num=0;
            	if(syn.isSelected())
            		data.getNetwork().syn=true;
            	else 
            		data.getNetwork().syn=false;
            	
            	if(compress.isSelected())
            		data.getNetwork().compress=true;
            	else 
            		data.getNetwork().compress=false;
            	
            	try{            		
            		num=Integer.valueOf(inputnum.getText());
            	}catch(java.lang.Exception exc){
            		JOptionPane.showMessageDialog(null,"Number is illegal!","Error",JOptionPane.ERROR_MESSAGE);
            		return;
            	}
            	if(num<=5 && num>=1){          		
            		data.getNetwork().setDimension(num);
            		if(!totalnum.isEnabled())
            			data.getNetwork().setEntityNum((int)Math.pow(2, num));  
            		else
            			data.getNetwork().setEntityNum(Integer.valueOf(totalnum.getText()));  
            		parent.getDisplayPanel().clearTimeKeeper();
            		parent.getDisplayPanel().updateUI();
            		close();
            	} else {
            		JOptionPane.showMessageDialog(null,"Number is out of the range!","Error",JOptionPane.ERROR_MESSAGE);
            	}
            }
        });
		
		cancel=new JButton("cancel");
		cancel.setBounds(120,245,100,20);
		this.add(cancel);
		cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	close();
            }
        });
	}
	
	public void update(){
		if(data.getNetwork().syn)
			syn.setSelected(true);
		else
			asyn.setSelected(true);
		
		if(data.getNetwork().compress)
			compress.setSelected(true);
		else
			non_compress.setSelected(true);
	}
	
	public void close(){
		this.setVisible(false);
	}
}
