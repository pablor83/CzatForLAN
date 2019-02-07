import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

public class PanelForClients extends JPanel {
	
	
	
	public PanelForClients() {
		
		setLayout(null);
		
		setBorder(BorderFactory.createLineBorder(Color.black, 1));		
		TitledBorder borderForClients = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Przy³¹czeni klienci");
		borderForClients.setTitleJustification(TitledBorder.CENTER);
		setBorder(borderForClients);
	}

}
