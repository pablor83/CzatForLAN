import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

public class PanelForOptions extends JPanel {

	
	public PanelForOptions() {
		
		setLayout(null);
		
		TitledBorder borderForButtons = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Opcje");
		borderForButtons.setTitleJustification(TitledBorder.CENTER);
		setBorder(borderForButtons);
	}
}
