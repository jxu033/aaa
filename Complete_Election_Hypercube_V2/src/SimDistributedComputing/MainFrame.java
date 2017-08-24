package SimDistributedComputing;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;

public class MainFrame extends JFrame {

	private int height = 650, width = 505; // size for this frame
	private int length = 0; // number of point to show on the graph
	private DisplayPanel display; // display panel
	private InfoPanel info; // info panel
	private Graphic graphic; // showing statistics
	private SettingDialog settingdialog; // setting panel
	private Compute compute; //this is used for data analysis
	private AboutDialog aboutdialog;
	private Data data;  //data center
	private JMenu menu;
	private JMenuBar menubar;
	private JMenuItem properities, about;
	private JTabbedPane tabpanel;
	private JPanel process, statistics;
	private JButton calculate;

	public MainFrame() {
		super("Election in Hypercube (CSI 5308)");
		this.setSize(width, height);
		this.setLayout(null);
		this.setResizable(false); // forbid change size of frame
		data = new Data();

		// get windows screen size;
		this.setLocation((data.getScreenSize().width - width) / 2, (data.getScreenSize().height - height) / 2);

		menubar = new JMenuBar();
		menu = new JMenu("Settings");
		menubar.add(menu);
		properities = new JMenuItem("properties");
		properities.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				propItemEvent();
			}
		});
		menu.add(properities);
		about = new JMenuItem("about");
		about.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				aboutdialog.setVisible(true);
			}
		});
		menu.add(about);
		this.setJMenuBar(menubar);

		display = new DisplayPanel(this);
		display.setVisible(true);
		display.setLocation(0, 0);
		display.updateUI();

		graphic = new Graphic(this);
		graphic.setLocation(0, 0);
		graphic.setBackground(Color.white);

		info = new InfoPanel(this);
		info.setVisible(true);
		info.setLocation(0, 500);

		process = new JPanel();
		process.setBounds(0, 0, 500, 600);
		process.setLayout(null);
		process.add(display);
		process.add(info);

		statistics = new JPanel();
		statistics.setBounds(0, 0, 500, 600);
		statistics.setLayout(null);
		statistics.add(graphic);

		calculate = new JButton("show");
		calculate.setBounds(375, 530, 100, 25);
		statistics.add(calculate);
		calculate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				showGraph();
			}
		});

		tabpanel = new JTabbedPane();
		tabpanel.add("Process", process);
		tabpanel.add("statistics", statistics);
		tabpanel.setBounds(0, 0, 500, 600);
		this.add(tabpanel);
		tabpanel.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				// TODO Auto-generated method stub
				if (((JTabbedPane) e.getSource()).getSelectedIndex() == 0) {
					// show process panel
					data.getNetwork().resetEntity();
					process.setVisible(true);
					statistics.setVisible(false);
					data.getNetwork().animation = true;
				} else {
					// show statistics panel
					// panel exchange only when the process has been stopped or
					// end
					data.getNetwork().animation = false;
				}
			}
		});

		settingdialog = new SettingDialog(data, this);
		aboutdialog = new AboutDialog(data);
		compute=new Compute(this);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void propItemEvent() {
		if (!settingdialog.isVisible()) {
			settingdialog.update();
			settingdialog.setVisible(true);
		}
	}

	public void enabledProp(boolean bool) {
		properities.setEnabled(bool);
	}

	public Data getData() {
		return data;
	}

	public void end() {
		tabpanel.setEnabled(true);
		info.end();
	}

	public DisplayPanel getDisplayPanel() {
		return display;
	}

	// start from beginning the process. ID will be re-assigned to each node
	public void start() {
		data.getNetwork().resetEntity();
		data.getNetwork().start();

		// In the graph model, no need for refresh display panel
		if (data.getNetwork().animation) {
			try {
				display.stop = false;
				Thread display_thread = new Thread(display);
				display_thread.start();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	public void continued() {
		display.stop = false;
		data.getNetwork().continued();
	}

	// to ensure that only when the process has been stopped or end, tab can be
	// used
	public void enabletab(boolean bool) {
		tabpanel.setEnabled(bool);
	}

	//calculate message complexity
	private void showGraph() {
		if (data.getDataList() == null) {
			data.setDataList(new int[length][2]);			
		}
		try {
			Thread compute_thread = new Thread(compute);
			compute_thread.start();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void updateGraph(){
		graphic.updateUI();
	}

	public void setLength(int num) {
		if (num > -1)
			length = num;
	}
	
	public void setCalculateButton(boolean bool){
		calculate.setEnabled(bool);
	}
}
