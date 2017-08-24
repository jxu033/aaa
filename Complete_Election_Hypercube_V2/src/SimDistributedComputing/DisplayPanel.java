package SimDistributedComputing;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.text.DecimalFormat;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import example.ElectionHypercubeNode;
import example.ElectionHypercubeMessage;;

public class DisplayPanel extends JPanel implements Runnable, MouseMotionListener {

	private int width = 500, height = 500;
	private int rnode = 10; // l is the diameter of the link circle; r is the
							// radius of nodes;
	private Data data;
	private MainFrame parent;
	private ToolTipPanel tooltip;
	private DecimalFormat fnum; //used to scare length of time showing on the screen
	
	public boolean stop = false;

	public DisplayPanel(MainFrame p) {
		this.setSize(width, height);
		this.setLayout(null);
		this.setBorder(BorderFactory.createLoweredBevelBorder());
		this.setBackground(Color.white);
		parent = p;
		data = parent.getData();
		fnum=new  DecimalFormat("##0.0");

		tooltip = new ToolTipPanel();
		tooltip.setLocation(0, 0);
		tooltip.setVisible(false);
		this.add(tooltip);

		this.addMouseMotionListener(this);
	}

	public void mouseDragged(MouseEvent e) {

	}

	// mouse event to show relevant information about selected entities
	public void mouseMoved(MouseEvent e) {

		// show Entity's information
		boolean signal = true;
		if (data.getNetwork().animation) {
			if(!stop && !data.getNetwork().terminate)
				data.getNetwork().timekeeper+=0.1; 
			for (int i = 0; i < data.getNetwork().getEntityList().size(); i++) {
				ElectionHypercubeNode node = (ElectionHypercubeNode) data.getNetwork().getEntityList().get(i);
				if (e.getX() >= node.position.x && e.getX() < node.position.x + 10 && e.getY() >= node.position.y
						&& e.getY() < node.position.y + 10) {

					int x = node.position.x + 5, y = node.position.y + 5;
					if (node.position.x + 5 + 100 > width)
						x = node.position.x + 5 - 100;
					if (node.position.y + 5 + 90 > height)
						y = node.position.y + 5 - 90;

					tooltip.setLocation(x, y);
					tooltip.setInfo(node.id, node.state, node.getLevel(), node.nextduellist);
					tooltip.setVisible(true);
					signal = false;
					break;
				} else {
					tooltip.setVisible(false);
				}
			}

			// show message's information
			if (signal) {
				for (int i = 0; i < data.getNetwork().getMessageList().size(); i++) {
					ElectionHypercubeMessage msg = (ElectionHypercubeMessage) data.getNetwork().getMessageList().get(i);
					if (e.getX() >= msg.position.x && e.getX() < msg.position.x + 10 && e.getY() >= msg.position.y
							&& e.getY() < msg.position.y + 10) {

						int x = msg.position.x + 5, y = msg.position.y + 5;
						if (msg.position.x + 5 + 100 > width)
							x = msg.position.x + 5 - 100;
						if (msg.position.y + 5 + 90 > height)
							y = msg.position.y + 5 - 90;

						tooltip.setLocation(x, y);
						tooltip.setInfo(msg.id, msg.lev, msg.source, msg.dest);
						tooltip.setVisible(true);
						break;
					} else {
						tooltip.setVisible(false);
						this.updateUI();
					}
				}
			}
		}
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		// show entities and links
		if (data.getNetwork().animation) {
			//show message cost:
			g.drawString("Message Cost: " + data.getNetwork().getMsgNum(), 5, 15);
			//show time cost:
			g.drawString("Time Cost: " + fnum.format(data.getNetwork().timekeeper)+" units", 5, 35);

			for (int i = 0; i < data.getNetwork().getEntityNum(); i++) {
				// show entity
				Entity e = data.getNetwork().getEntityList().get(i);
				if (e.state == data.getNetwork().S_ASLEEP)
					g.setColor(Color.GRAY);
				else if (e.state == data.getNetwork().S_DUELLIST)
					g.setColor(Color.blue);
				else if (e.state == data.getNetwork().S_SECOND)
					g.setColor(Color.green);
				else if (e.state == data.getNetwork().S_FOLLOW)
					g.setColor(Color.black);
				else
					g.setColor(Color.orange);

				g.fillOval(data.getNetwork().getEntityList().get(i).getPosition().x,
						data.getNetwork().getEntityList().get(i).getPosition().y, rnode, rnode);

				g.drawString(data.getNetwork().getEntityList().get(i).id + "",
						data.getNetwork().getEntityList().get(i).getPosition().x,
						data.getNetwork().getEntityList().get(i).getPosition().y);

				// show relevant links
				g.setColor(Color.black);
				for (int j = 0; j < data.getNetwork().getEntityList().get(i).getLinkList().size(); j++) {
					// ensure that no repeated links is painted
					Entity en = data.getNetwork().getEntityList().get(i).getLinkList().get(j).neighbor;
					int x1 = data.getNetwork().getEntityList().get(i).getPosition().x, x2 = en.getPosition().x,
							y1 = data.getNetwork().getEntityList().get(i).getPosition().y, y2 = en.getPosition().y;
					g.drawLine(data.getNetwork().getEntityList().get(i).getPosition().x + 5,
							data.getNetwork().getEntityList().get(i).getPosition().y + 5, en.getPosition().x + 5,
							en.getPosition().y + 5);

					// draw label of link
					String str = data.getNetwork().getEntityList().get(i).getLinkList().get(j).label + "";
					g.drawString(str, (x1 + x2) / 2 + 5, (y1 + y2) / 2 + 5);
				}
			}

			// show messages
			g.setColor(Color.red);
			for (int i = 0; i < data.getNetwork().getMessageList().size(); i++) {
				if (data.getNetwork().getMessageList().get(i) != null) {
					g.fillOval(data.getNetwork().getMessageList().get(i).position.x,
							data.getNetwork().getMessageList().get(i).position.y, 10, 10);
					data.getNetwork().getMessageList().get(i).move();
				}
			}

			// show waiting messages
			for (int i = 0; i < data.getNetwork().getWaitingMessages().size(); i++) {
				if (data.getNetwork().getWaitingMessages().get(i) != null) {
					g.fillOval(data.getNetwork().getWaitingMessages().get(i).position.x,
							data.getNetwork().getWaitingMessages().get(i).position.y, 12, 12);
				}
			}
		}
	}

	public void run() {
		data.getNetwork().timekeeper=0;  //clear time counter
		while (!data.getNetwork().isTerminated()) {
			if (!stop) {
				this.updateUI();
				data.getNetwork().timekeeper+=0.1;
			}
			try {
				Thread.sleep(data.getSpeed());
			} catch (java.lang.Exception ex) {
				return;
			}
		}
		// final update to ensure that there is no message left on the screen
		this.updateUI();
		parent.end();
	}
	
	public void clearTimeKeeper(){
		data.getNetwork().timekeeper=0;
	}
}
