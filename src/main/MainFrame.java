package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;

import game.GameManager;
import imaging.CImage;
import options.OptionPanel;

public class MainFrame extends JFrame {

	private static MainFrame fr;
	private JScrollPane jspane;
	private ImageIcon mapImage;
	private JLabel mapViewPort;
	
	private OptionPanel optionsViewPanel;
	
	public static void start() {
		fr = new MainFrame();
		fr.repaint();
		fr.optionsViewPanel.cityOptions.updateMouseListenerForMapViewPort();
	}
	
	public static void repaintFrame() {
		GameManager.createMap();
		fr.revalidate();
		fr.repaint();
		
		BufferedImage mapImg = GameManager.getMap();
		double widthToHeight = mapImg.getWidth() / (double) mapImg.getHeight();
		int newHeight = fr.mapImage.getIconHeight();
		fr.mapImage.setImage(CImage.getResizedImage(mapImg, (int) (widthToHeight * newHeight), newHeight));
		fr.jspane.getViewport().revalidate();
	}
	
	public static JLabel getMapViewPort() {
		return fr.mapViewPort;
	}
	
	public MainFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setOrientation(SwingConstants.VERTICAL);
		setContentPane(splitPane);
		
		
		JPanel jspanePanel = new JPanel();
		jspanePanel.setBackground(Color.LIGHT_GRAY);
		
		mapImage = new ImageIcon(GameManager.getMap());
		mapViewPort = new JLabel(mapImage);
		JPanel mapWrapper = new JPanel();
		jspane = new JScrollPane();
		jspane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		jspane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		jspane.setWheelScrollingEnabled(false);
		jspane.addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				double zoom = -e.getPreciseWheelRotation() * 10;
				BufferedImage origImg = GameManager.getMap();
				int origW = origImg.getWidth(), origH = origImg.getHeight();
				double w2h = 1.0 * origW / origH;
				int newW = (int) (mapImage.getIconWidth() + zoom), newH = (int) (newW / w2h);
				if (newH < jspane.getViewport().getHeight()) {
					newH = jspane.getViewport().getHeight();
					newW = (int) (w2h * newH);
				}
				mapImage.setImage(CImage.getResizedImage(origImg, newW, newH));
				mapViewPort.setIcon(mapImage);
				mapWrapper.removeAll();
				mapWrapper.add(mapViewPort);
				jspane.getViewport().revalidate();
				jspanePanel.repaint();
			}
		});
		mapWrapper.add(mapViewPort, BorderLayout.CENTER);
		jspane.setViewportView(mapWrapper);
		jspanePanel.add(jspane, BorderLayout.CENTER);
		jspanePanel.setMaximumSize(new Dimension(jspanePanel.getWidth(), jspanePanel.getHeight()));
		jspanePanel.setMinimumSize(new Dimension(jspanePanel.getWidth(), jspanePanel.getHeight()));
		splitPane.setLeftComponent(jspanePanel);
		
		optionsViewPanel = new OptionPanel();
		optionsViewPanel.setBackground(Color.LIGHT_GRAY);
		optionsViewPanel.setMaximumSize(new Dimension(400, 1000));
		optionsViewPanel.setMinimumSize(new Dimension(400, 100));
		optionsViewPanel.setPreferredSize(new Dimension(400, 500));
		splitPane.setRightComponent(optionsViewPanel);
		
		splitPane.setResizeWeight(1);
		splitPane.setMinimumSize(new Dimension(jspanePanel.getWidth() + optionsViewPanel.getWidth(), jspanePanel.getHeight()));
		
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
}
