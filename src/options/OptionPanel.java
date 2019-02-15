package options;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

import game.GameManager;
import main.MainFrame;

public class OptionPanel extends JPanel {
	
	public CityOptionPanel cityOptions;
	
	public OptionPanel() {
		setLayout(new BorderLayout(0, 0));
		
		JTabbedPane optionsTabbedPane = new JTabbedPane(JTabbedPane.LEFT);
		add(optionsTabbedPane, BorderLayout.CENTER);
		
		ViewOptionPanel viewOptions = new ViewOptionPanel();
		optionsTabbedPane.addTab("View", null, viewOptions, null);
		
		cityOptions = new CityOptionPanel();
		optionsTabbedPane.addTab("Cities", null, cityOptions, null);
		
		RailOptionPanel railsOptions = new RailOptionPanel();
		optionsTabbedPane.addTab("Rails", null, railsOptions, null);
		
		JPanel saveOptions = new JPanel();
		JButton btnSave = new JButton("Save Map");
		btnSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GameManager.saveData();
			}
		});
		saveOptions.add(btnSave, BorderLayout.CENTER);
		JButton btnLoad = new JButton("Load Map");
		btnLoad.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GameManager.readData();
				CityOptionPanel.updatePanel();
				RailOptionPanel.updatePanel();
				MainFrame.repaintFrame();
			}
		});
		saveOptions.add(btnLoad, BorderLayout.CENTER);
		JButton btnHRF = new JButton("How to Read .ttr File");
		btnHRF.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					BufferedReader in = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("misc/h2r.dat")));
					String txt = "";
					while (in.ready())
						txt += in.readLine() + "\n";
					JTextArea jta = new JTextArea(30, 80);
					jta.setText(txt);
					jta.setEditable(false);
					JScrollPane sp = new JScrollPane(jta);
					JOptionPane.showMessageDialog(null, sp, "How to Read .ttr File", JOptionPane.PLAIN_MESSAGE);
				} catch (Exception exc) {}
			}
		});
		saveOptions.add(btnHRF, BorderLayout.CENTER);
		optionsTabbedPane.addTab("Options", null, saveOptions, null);
	}
}
