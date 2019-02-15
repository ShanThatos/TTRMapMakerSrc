package options;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import game.City;
import game.GameManager;
import imaging.CImage;
import main.MainFrame;

public class CityOptionPanel extends JPanel {
	private static CityOptionPanel cop;
	
	private JTextField txtCityName;
	private JTextField txtX;
	private JTextField txtY;
	private JToggleButton chooseLoc;
	
	private JPanel cityListPanel;
	private final String[] columnNames = {"#", "City Name", "X Pos", "Y Pos"};
	private Object[][] cityTableData = new Object[0][4];
	private JTable allCities = new JTable(cityTableData, columnNames);
	private CityListListener cll = new CityListListener(this);
	
	// Reference to mainframe
	private JLabel mapView;
	
	public CityOptionPanel() {
		cop = this;
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] {0, 0};
		gridBagLayout.rowHeights = new int[] {0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, 6.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JPanel addCityPanel = new JPanel();
		GridBagConstraints gbc_addCityPanel = new GridBagConstraints();
		gbc_addCityPanel.anchor = GridBagConstraints.PAGE_START;
		gbc_addCityPanel.fill = GridBagConstraints.BOTH;
		gbc_addCityPanel.gridx = 0;
		gbc_addCityPanel.gridy = 0;
		add(addCityPanel, gbc_addCityPanel);
		addCityPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JLabel lblCityName = new JLabel("City Name:");
		addCityPanel.add(lblCityName);
		
		txtCityName = new JTextField();
		txtCityName.setActionCommand("");
		addCityPanel.add(txtCityName);
		txtCityName.setColumns(10);
		
		JLabel lblXPosition = new JLabel("X Position: ");
		addCityPanel.add(lblXPosition);
		
		txtX = new JTextField();
		addCityPanel.add(txtX);
		txtX.setColumns(4);
		
		JLabel lblYPosition = new JLabel("Y Position: ");
		addCityPanel.add(lblYPosition);
		
		txtY = new JTextField();
		addCityPanel.add(txtY);
		txtY.setColumns(4);
		
		chooseLoc = new JToggleButton("Choose XY Position");
		addCityPanel.add(chooseLoc);
		
		JButton btnAddCity = new JButton("Add City");
		btnAddCity.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String cityName = txtCityName.getText();
				double xp = Double.parseDouble(txtX.getText()), 
						yp = Double.parseDouble(txtY.getText());
				City c = new City(cityName, xp, yp);
				GameManager.addCity(c);
				updateCityTable();
				MainFrame.repaintFrame();
			}
		});
		addCityPanel.add(btnAddCity);
		
		JButton btnDeleteCity = new JButton("Delete City");
		btnDeleteCity.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String cityName = txtCityName.getText();
				GameManager.removeCity(cityName);
				updateCityTable();
				MainFrame.repaintFrame();
			}
		});
		addCityPanel.add(btnDeleteCity);
		
		cityListPanel = new JPanel();
		cityListPanel.setBackground(Color.PINK);
		GridBagConstraints gbc_cityListPanel = new GridBagConstraints();
		gbc_cityListPanel.anchor = GridBagConstraints.PAGE_START;
		gbc_cityListPanel.fill = GridBagConstraints.BOTH;
		gbc_cityListPanel.gridx = 0;
		gbc_cityListPanel.gridy = 1;
		add(cityListPanel, gbc_cityListPanel);
		
		allCities.getSelectionModel().addListSelectionListener(cll);
		allCities.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		allCities.setDefaultEditor(Object.class, null);
		allCities.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		cityListPanel.setLayout(new BorderLayout());
		cityListPanel.add(allCities.getTableHeader(), BorderLayout.PAGE_START);
		JScrollPane listCityJSP = new JScrollPane(allCities);
		cityListPanel.add(listCityJSP, BorderLayout.CENTER);
		
	}
	
	public void updateMouseListenerForMapViewPort() {
		mapView = MainFrame.getMapViewPort();
		mapView.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
			@Override
			public void mousePressed(MouseEvent e) {}
			@Override
			public void mouseReleased(MouseEvent e) {
				double x = e.getX(), y = e.getY();
				BufferedImage origImg = CImage.getImage("board");
				x *= (double) origImg.getWidth() / mapView.getIcon().getIconWidth();
				y *= (double) origImg.getHeight() / mapView.getIcon().getIconHeight();
				if (chooseLoc.isSelected()) {
					txtX.setText((int) x + "");
					txtY.setText((int) y + "");
					chooseLoc.doClick();
				}
			}
		});
		chooseLoc.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (Component c : chooseLoc.getParent().getComponents()) 
					if (c != chooseLoc)
						c.setEnabled(!chooseLoc.isSelected());
				mapView.setCursor(new Cursor(chooseLoc.isSelected() ? Cursor.CROSSHAIR_CURSOR : Cursor.DEFAULT_CURSOR));
			}
		});
		
	}
	
	public void updateCityTable() {
		TreeMap<String, City> cityMap = GameManager.getCities();
		cityTableData = new Object[cityMap.size()][];
		int i = 0;
		for (City c : cityMap.values()) {
			cityTableData[i++] = new Object[]{
					i,
					c.cityName, 
					c.x,
					c.y
			};
		}
		final String[] columnNames = {"#", "City Name", "X Pos", "Y Pos"};
		cityListPanel.removeAll();
		allCities = new JTable(cityTableData, columnNames);
		allCities.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		allCities.getSelectionModel().addListSelectionListener(cll);
		allCities.setDefaultEditor(Object.class, null);
		allCities.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		cityListPanel.add(allCities.getTableHeader(), BorderLayout.PAGE_START);
		JScrollPane listCityJSP = new JScrollPane(allCities);
		cityListPanel.add(listCityJSP, BorderLayout.CENTER);
		
		revalidate();
		repaint();
	}
	
	public void updateSelection() {
		int rowIndex = allCities.getSelectedRow();
		Object[] cityData = cityTableData[rowIndex];
		txtCityName.setText((String) cityData[1]);
		txtX.setText(((double) cityData[2]) + "");
		txtY.setText(((double) cityData[3]) + "");
	}
	
	public static void updatePanel() {
		if (cop == null)
			return;
		cop.updateCityTable();
	}
}

class CityListListener implements ListSelectionListener {
	
	private CityOptionPanel cop;
	
	public CityListListener(CityOptionPanel cop) {
		this.cop = cop;
	}
	
	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting())
			return;
		cop.updateSelection();
	}
}