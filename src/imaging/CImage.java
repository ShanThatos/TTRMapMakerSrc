package imaging;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Scanner;
import java.util.TreeMap;

import javax.imageio.ImageIO;

public class CImage {
	
	private static TreeMap<String, Integer> map;
	private static String[] imgNames;
	private static BufferedImage[] imgs;
	
	public static void initiate() {
		Scanner in = new Scanner((new CImage()).getClass().getClassLoader().getResourceAsStream("imgs/_allImgs.dat"));
		int n = in.nextInt();
		in.nextLine();
		imgNames = new String[n];
		imgs = new BufferedImage[n];
		map = new TreeMap<>();
		for (int i = 0; i < n; i++) {
			imgNames[i] = in.next().trim().toLowerCase();
			String fileName = in.next().trim();
			try {
				imgs[i] = ImageIO.read((new CImage()).getClass().getClassLoader().getResourceAsStream("imgs/" + fileName));
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(-1);
			}
			map.put(imgNames[i], i);
		}
		in.close();
	}
	
	public static BufferedImage getImage(String imgName) {
		if (imgNames == null) 
			initiate();
		return imgs[map.get(imgName)];
	}
	
	public static BufferedImage getResizedImage(BufferedImage img, int newWidth, int newHeight) {
		BufferedImage ret = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = ret.createGraphics();
		g.drawImage(img, 0, 0, newWidth, newHeight, null);
		g.dispose();
		return ret;
	}
}
