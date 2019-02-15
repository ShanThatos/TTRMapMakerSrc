import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Tester {
	public static void main(String[] args) {
		JFrame fr = new JFrame();
		fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel jp = new JPanel() {
			@Override
			public void paintComponent(Graphics gr) {
				Graphics2D g = (Graphics2D) gr.create();
				Path2D path = new Path2D.Double();
				path.moveTo(250, 250);
				path.quadTo(325, 200, 400, 250);
				g.setColor(Color.blue);
				g.setStroke(new BasicStroke(5));
				g.draw(path);
			}
		};
		jp.setPreferredSize(new Dimension(500, 500));
		fr.setContentPane(jp);
		fr.pack();
		fr.setVisible(true);
	}
}
