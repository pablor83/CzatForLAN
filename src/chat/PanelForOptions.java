package chat;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

public class PanelForOptions extends JPanel {

	JButton button;
	
	public PanelForOptions() {
		
		setLayout(null);
		
		TitledBorder borderForButtons = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Opcje");
		borderForButtons.setTitleJustification(TitledBorder.CENTER);
		setBorder(borderForButtons);
		
		button = new JButton("Prywatna rozmowa");
		button.setBounds(15, 20, 170, 30);
		add(button);
		
	}
}
