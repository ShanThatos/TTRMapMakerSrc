package game;

import java.awt.Color;
import java.awt.Graphics2D;

public class City implements Comparable<City> {
	public String cityName;
	public double x, y;
	
	public City(String cityName, double x, double y) {
		this.cityName = cityName;
		this.x = x;
		this.y = y;
	}
	public int compareTo(City c) {
		return cityName.compareTo(c.cityName);
	}
	
	public void draw(Graphics2D gr) {
		Graphics2D g = (Graphics2D) gr.create();
		g.setColor(Color.red.darker());
		final int radius = 6;
		g.fillOval((int) x - radius, (int) y - radius, radius * 2, radius * 2);
		g.setColor(Color.black);
		g.drawOval((int) x - radius, (int) y - radius, radius * 2, radius * 2);
	}
	
	public String toString() {
		return cityName + String.format(" (%d, %d)", (int) x, (int) y);
	}
	
	public String getSaveData() {
		return String.format("%s %.2f %.2f", cityName, x, y);
	}
}
