package SimDistributedComputing;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class Graphic extends JPanel {

	private int width = 500, height = 500;
	private Data data;
	private MainFrame parent;

	// point sequence to create arrow
	private int[] a_x = { 30, 25, 30, 35 }, a_y = { 30, 50, 45, 50 }, b_x = { 470, 450, 455, 450 },
			b_y = { 470, 475, 470, 465 };

	public Graphic(MainFrame p) {
		this.setSize(width, height);
		this.setLayout(null);
		this.setBorder(BorderFactory.createLoweredBevelBorder());

		parent = p;
		data = parent.getData();
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		drawAxis(g);
	}

	// draw x and y axis
	private void drawAxis(Graphics g) {
		g.setColor(Color.black);
		// x axis
		g.drawLine(30, 470, 470, 470);
		g.fillPolygon(a_x, a_y, 4);
		// y axis
		g.drawLine(30, 30, 30, 470);
		g.fillPolygon(b_x, b_y, 4);
		g.drawString("Message Cost", 40, 40);
		g.drawString("Number of Entities", 360, 460);

		if (data.getDataList() != null) {
			int last_x=data.getDataList()[0][0]*10+30, last_y=470-data.getDataList()[0][1],x=0,y=0;
			for (int i = 0; i <= data.currentcomputelevel; i++) {						
				x=data.getDataList()[i][0]*10+30;
				y=470-data.getDataList()[i][1]*4;
				g.setColor(Color.yellow);
				g.drawLine(30, y, x, y);
				g.drawLine(x, 470, x, y);
				
				g.setColor(Color.black);
				g.drawLine(last_x, last_y, x, y);
				last_x=x;
				last_y=y;
				g.fillOval(x-5, y-5, 10, 10);
				g.drawString(data.getDataList()[i][0] + "", x, 485);
				g.drawString(data.getDataList()[i][1] + "", 10, y);

			}
		}
	}
}
