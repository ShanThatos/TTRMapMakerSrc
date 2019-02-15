package game;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.FileWriter;
import java.util.Scanner;
import java.util.TreeMap;

import javax.swing.JFileChooser;

import imaging.CImage;
import options.RailOptionPanel;
import options.ViewOptionPanel;

public class GameManager {
	
	static final JFileChooser fc = new JFileChooser();
	
	private static TreeMap<String, City> cities = new TreeMap<>();
	private static TreeMap<Integer, Rail> rails = new TreeMap<>();
	
	private static BufferedImage map;
	
	public static void addCity(City c) {
		cities.put(c.cityName, c);
		RailOptionPanel.updatePanel();
	}
	public static void removeCity(String cityName) {
		cities.remove(cityName);
		RailOptionPanel.updatePanel();
	}
	public static void addRail(Rail r) {
		rails.put(r.rID, r);
	}
	public static void removeRail(Integer railID) {
		rails.remove(railID);
	}
	
	public static City getCity(String cityName) {
		return cities.get(cityName);
	}
	public static TreeMap<String, City> getCities() {
		return cities;
	}
	
	public static TreeMap<Integer, Rail> getRails() {
		return rails;
	}
	
	public static BufferedImage getMap() {
		if (map == null)
			createMap();
		return map;
	}
	
	public static void createMap() {
		BufferedImage origMap = CImage.getImage("board");
		map = new BufferedImage(origMap.getWidth(), origMap.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = map.createGraphics();
		if (ViewOptionPanel.bgMapImage.isSelected())
			g.drawImage(origMap, 0, 0, null);
		if (ViewOptionPanel.chckbxRails.isSelected()) {
			for (Rail r : rails.values()) 
				r.draw(g);
			RailOptionPanel.currentRail.draw(g);
		}
		if (ViewOptionPanel.viewCity.isSelected())
			for (City c : cities.values()) 
				c.draw(g);
	}
	
	public static void saveData() {
		int retrival = fc.showSaveDialog(null);
		if (retrival == JFileChooser.APPROVE_OPTION) {
			try {
				FileWriter fw = new FileWriter(fc.getSelectedFile().toString().replaceAll(".ttr", "") + ".ttr");
				fw.write(getSaveData());
				fw.close();
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(-1);
			}
		}
	}
	private static String getSaveData() {
		String ret = "";
		ret += cities.size() + "\n";
		TreeMap<String, Integer> ctToIndex = new TreeMap<>();
		int index = 0;
		// City Index, Name, X Pos, Y Pos
		for (City c : cities.values()) {
			ret += String.format("%d %s %.0f %.0f%n", index, c.cityName.replaceAll(" ", "_"), c.x, c.y);
			ctToIndex.put(c.cityName, index++);
		}
		ret += rails.size() + "\n";
		// Rail ID, City A, City B, Number of Trains, 2 Rails?, Curvature
		// For each track: 
		// Color, Coord 1, Coord 2(Vertex of Parabola), Coord 3
		for (Rail r : rails.values()) {
			ret += String.format("%d %d %d %d %s %.2f%n", 
					r.rID, 
					ctToIndex.get(r.a.cityName), 
					ctToIndex.get(r.b.cityName), 
					r.numTrains, 
					r.twoRails + "", 
					r.curvature);
			double[][] parabolicCoords = r.getParabolicCoords();
			for (int i = 0; i < parabolicCoords.length; i++) {
				ret += Rail.colors[r.cID[i]] + " ";
				for (int j = 0; j < parabolicCoords[i].length; j++)
					ret += String.format("%.3f ", parabolicCoords[i][j]);
				ret += "\n";
			}
		}
		return ret;
	}
	
	public static void readData() {
		int retrival = fc.showOpenDialog(null);
		if (retrival == JFileChooser.APPROVE_OPTION) {
			try {
				cities.clear();
				rails.clear();
				Scanner in = new Scanner(fc.getSelectedFile());
				int nc = in.nextInt();
				City[] arr = new City[nc];
				for (int i = 0; i < nc; i++) {
					in.nextInt();
					String cityName = in.next().replaceAll("_", " ");
					int x = in.nextInt(), y = in.nextInt();
					arr[i] = new City(cityName, x, y);
					cities.put(cityName, arr[i]);
				}
				int rc = in.nextInt();
				for (int i = 0; i < rc; i++) {
					in.nextInt();
					int c1 = in.nextInt(), c2 = in.nextInt();
					int numTr = in.nextInt();
					boolean twoRails = in.nextBoolean();
					double curvature = in.nextDouble();
					String[] cls = new String[twoRails ? 2 : 1];
					for (int j = 0; j < cls.length; j++) {
						cls[j] = in.next();
						in.nextLine();
					}
					Rail r = new Rail(arr[c1], arr[c2], twoRails, curvature, cls, numTr);
					r.formPath();
					rails.put(r.rID, r);
				}
				in.close();
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(-1);
			}
		}
	}
}
