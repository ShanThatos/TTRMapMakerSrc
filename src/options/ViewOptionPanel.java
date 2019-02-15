package options;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import game.GameManager;
import main.MainFrame;

public class ViewOptionPanel extends JPanel {
	
	public static JCheckBox bgMapImage = new JCheckBox("Background Map Image"),
			viewCity = new JCheckBox("Cities"),
			chckbxRails = new JCheckBox("Rails");
	
	public ViewOptionPanel() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		ChangeListener k = new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				MainFrame.repaintFrame();
			}
		};
		
		bgMapImage.setSelected(true);
		bgMapImage.addChangeListener(k);
		add(bgMapImage);
		
		viewCity.setSelected(true);
		viewCity.addChangeListener(k);
		add(viewCity);
		
		chckbxRails.setSelected(true);
		chckbxRails.addChangeListener(k);
		add(chckbxRails);
		
		GameManager.createMap();
	}
}
