package game;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.util.Arrays;

public class Rail {
	public static final Color[] gcolors = {Color.gray, Color.white, Color.yellow, Color.blue, Color.orange, Color.red, Color.green, Color.pink, Color.black};
	public static final String[] colors = {"Gray", "White", "Yellow", "Blue", "Orange", "Red", "Green", "Pink", "Black"};
	public City a, b;
	
	public boolean twoRails;
	
	private static int railCount;
	public int rID;
	public int[] cID;
	public int numTrains;
	
	public double curvature;
	public double[] curvePoints;
	public Path2D[] paths;
	
	public Rail(City a, City b, boolean twoRails, double curvature, String[] cls, int numTrains) {
		this.a = a;
		this.b = b;
		this.twoRails = twoRails;
		this.curvature = curvature;
		rID = railCount++;
		
		curvePoints = new double[twoRails ? 4 : 2];
		paths = new Path2D[twoRails ? 2 : 1];
		cID = new int[cls.length];
		for (int i = 0; i < cls.length; i++) 
			for (int j = 0; j < colors.length; j++)
				if (colors[j].equals(cls[i]))
					cID[i] = j;
		this.numTrains = numTrains;
	}
	
	public City getOtherCity(City thisCity) {
		if (a == thisCity)
			return b;
		else if (b == thisCity)
			return a;
		return null;
	}
	
	public int compareTo(Rail r) {
		return rID - r.rID;
	}
	
	public void formPath() {
		if (a == null || b == null || a == b)
			return;
		curvePoints = new double[2];
		paths = new Path2D[twoRails ? 2 : 1];
		
		double slope = (b.y - a.y) / (b.x - a.x);
		slope = -1 / slope;
		double dx = 1, dy = slope;
		double div = Math.sqrt(dx * dx + dy * dy);
		dx *= curvature / div;
		dy *= curvature / div;
		double midX = (a.x + b.x) / 2, 
				midY = (a.y + b.y) / 2;
		curvePoints[0] = midX + dx;
		curvePoints[1] = midY + dy;
		
		paths[0] = new Path2D.Double();
		paths[0].moveTo(a.x, a.y);
		paths[0].quadTo(curvePoints[0], curvePoints[1], b.x, b.y);
		if (twoRails) {
			paths[1] = new Path2D.Double(paths[0]);
			double theta = Math.atan2(a.x - b.x, b.y - a.y);
			double dx2 = Math.cos(theta) * 11 / 2;
			double dy2 = Math.sin(theta) * 11 / 2;
			paths[0].transform(AffineTransform.getTranslateInstance(-dx2, -dy2));
			paths[1].transform(AffineTransform.getTranslateInstance(dx2, dy2));
		}
	}
	
	public void draw(Graphics2D gr) {
		if (paths == null) 
			return;
		Graphics2D g = (Graphics2D) gr.create();
		g.setStroke(new BasicStroke(10, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0f, new float[]{33, 3}, 22));
		for (int i = 0; i < paths.length; i++) 
			if (paths[i] != null && cID.length > i) {
				g.setColor(gcolors[cID[i]]);
				g.draw(paths[i]);
			}
	}
	
	public double[][] getParabolicCoords() {
		double[][] ret = new double[twoRails ? 2 : 1][6];
		for (int i = 0; i < paths.length; i++) {
			PathIterator it = paths[i].getPathIterator(AffineTransform.getScaleInstance(1, 1));
			double[] p1 = new double[2], 
					p2 = new double[4];
			it.currentSegment(p1);
			it.next();
			it.currentSegment(p2);
			ret[i][0] = p1[0];
			ret[i][1] = p1[1];
			ret[i][2] = p2[0];
			ret[i][3] = p2[1];
			ret[i][4] = p2[2];
			ret[i][5] = p2[3];
		}
		return ret;
	}
}
