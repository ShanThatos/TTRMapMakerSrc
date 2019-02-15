package options;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import game.City;
import game.GameManager;
import game.Rail;
import main.MainFrame;

public class RailOptionPanel extends JPanel {
	
	private static RailOptionPanel rop;
	
	private JPanel railListPanel;
	private JComboBox<City> cityABox, cityBBox;
	private JComboBox<String> cls1Box, cls2Box;
	private JCheckBox chckbxRails;
	private JSlider railCurve;
	private JSpinner spinner;
	public static Rail currentRail = new Rail(null, null, false, 0, new String[0], 0);
	
	private final String[] columnNames = {"#", "City A", "City B", "Num Tracks", "2 Rails?", "Color 1", "Color 2", "Rail Curve"};
	private Object[][] railTableData = new Object[0][4];
	private JTable allRails = new JTable(railTableData, columnNames);
	
	public RailOptionPanel() {
		rop = this;
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{100, 200, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, 6.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JPanel addRailPanel = new JPanel();
		GridBagConstraints gbc_addRailPanel = new GridBagConstraints();
		gbc_addRailPanel.anchor = GridBagConstraints.PAGE_START;
		gbc_addRailPanel.fill = GridBagConstraints.BOTH;
		gbc_addRailPanel.gridx = 0;
		gbc_addRailPanel.gridy = 0;
		add(addRailPanel, gbc_addRailPanel);
		addRailPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JLabel lblCityA = new JLabel("City A: ");
		addRailPanel.add(lblCityA);
		
		ActionListener railLis = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cls2Box.setVisible(chckbxRails.isSelected());
				updateCurrentRail();
			}
		};
		
		cityABox = new JComboBox<>();
		cityABox.addActionListener(railLis);
		addRailPanel.add(cityABox);
		
		JLabel lblCityB = new JLabel("City B: ");
		addRailPanel.add(lblCityB);
		
		cityBBox = new JComboBox<>();
		cityBBox.addActionListener(railLis);
		addRailPanel.add(cityBBox);
		
		chckbxRails = new JCheckBox("2 Rails?");
		chckbxRails.addActionListener(railLis);
		addRailPanel.add(chckbxRails);
		
		cls1Box = new JComboBox<>();
		cls2Box = new JComboBox<>();
		for (String s : Rail.colors) {
			cls1Box.addItem(s);
			cls2Box.addItem(s);
		}
		cls1Box.addActionListener(railLis);
		cls2Box.addActionListener(railLis);
		cls2Box.setVisible(false);
		
		JLabel lblColor = new JLabel("Colors: ");
		addRailPanel.add(lblColor);
		addRailPanel.add(cls1Box);
		addRailPanel.add(cls2Box);
		
		JLabel lblNumberOfTracks = new JLabel("Number of Tracks: ");
		addRailPanel.add(lblNumberOfTracks);
		
		spinner = new JSpinner();
		spinner.setModel(new SpinnerNumberModel(1, 1, 6, 1));
		spinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				updateCurrentRail();
			}
		});
		addRailPanel.add(spinner);
		
		JLabel lblRailCurve = new JLabel("Rail Curve: ");
		addRailPanel.add(lblRailCurve);
		
		railCurve = new JSlider();
		lblRailCurve.setLabelFor(railCurve);
		railCurve.setValue(0);
		railCurve.setMinimum(-100);
		railCurve.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				updateCurrentRail();
			}
		});
		addRailPanel.add(railCurve);
		
		JButton btnAddRail = new JButton("Add Rail");
		btnAddRail.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GameManager.addRail(currentRail);
				currentRail = new Rail(null, null, false, 0, new String[0], 0);
				updateRailTable();
				MainFrame.repaintFrame();
			}
		});
		addRailPanel.add(btnAddRail);
		JButton btnDeleteRail = new JButton("Delete Selected Rail");
		btnDeleteRail.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int railID = (int) railTableData[allRails.getSelectedRow()][0];
				GameManager.removeRail(railID);
				updateRailTable();
				MainFrame.repaintFrame();
			}
		});
		addRailPanel.add(btnDeleteRail);
		
		railListPanel = new JPanel();
		GridBagConstraints gbc_railListPanel = new GridBagConstraints();
		gbc_railListPanel.anchor = GridBagConstraints.NORTH;
		gbc_railListPanel.fill = GridBagConstraints.BOTH;
		gbc_railListPanel.gridx = 0;
		gbc_railListPanel.gridy = 1;
		add(railListPanel, gbc_railListPanel);
		
		allRails.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		allRails.setDefaultEditor(Object.class, null);
		allRails.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		railListPanel.setLayout(new BorderLayout());
		railListPanel.add(allRails.getTableHeader(), BorderLayout.PAGE_START);
		JScrollPane listRailJSP = new JScrollPane(allRails);
		railListPanel.add(listRailJSP, BorderLayout.CENTER);
	}
	
	public void updateCurrentRail() {
		City a = (City) cityABox.getSelectedItem();
		City b = (City) cityBBox.getSelectedItem();
		boolean twoRails = chckbxRails.isSelected();
		double rC = railCurve.getValue();
		String[] cls;
		if (twoRails)
			cls = new String[] {(String) cls1Box.getSelectedItem(), (String) cls2Box.getSelectedItem()};
		else 
			cls = new String[] {(String) cls1Box.getSelectedItem()};
		currentRail = new Rail(a, b, twoRails, rC, cls, (int) spinner.getValue());
		currentRail.formPath();
		MainFrame.repaintFrame();
	}
	
	public void updateRailTable() {
		TreeMap<Integer, Rail> rails = GameManager.getRails();
		railTableData = new Object[rails.size()][];
		int i = 0;
		for (Rail r : rails.values()) {
			railTableData[i++] = new Object[] {
					r.rID, 
					r.a.cityName, 
					r.b.cityName, 
					r.numTrains,
					r.twoRails, 
					Rail.colors[r.cID[0]], 
					r.cID.length > 1 ? Rail.colors[r.cID[1]] : "N/A", 
					String.format("%.3f", r.curvature)
			};
		}
		railListPanel.removeAll();
		allRails = new JTable(railTableData, columnNames);
		allRails.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		allRails.setDefaultEditor(Object.class, null);
		allRails.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		railListPanel.setLayout(new BorderLayout());
		railListPanel.add(allRails.getTableHeader(), BorderLayout.PAGE_START);
		JScrollPane listRailJSP = new JScrollPane(allRails);
		railListPanel.add(listRailJSP, BorderLayout.CENTER);
	}
	
	public static void updatePanel() {
		if (rop == null)
			return;
		TreeMap<String, City> cities = GameManager.getCities();
		rop.cityABox.removeAllItems();
		rop.cityBBox.removeAllItems();
		for (City c : cities.values()) {
			rop.cityABox.addItem(c);
			rop.cityBBox.addItem(c);
		}
		rop.updateRailTable();
	}
}
