import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

public class WindowOfCzat extends JFrame{
			
	PanelForReceivedAndSend panelForReceivedAndSend = new PanelForReceivedAndSend();
	PanelForClients panelForClients = new PanelForClients();
	PanelForOptions panelForOptions = new PanelForOptions();
	
	JMenuBar menuBar = new JMenuBar();
	JMenu menu;
	
	
	
	private WindowOfCzat() {
		
		Dimension dimension = new Dimension(970, 570);	
		setMinimumSize(dimension);
		setTitle("Czat for LAN");
		setLocationRelativeTo(null);
		setLayout(new GridBagLayout());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		
		menu = new JMenu("Plik");
		menuBar.add(menu);
		
		setJMenuBar(menuBar);		
		
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.insets = new Insets(0, 0, 0, 0);
		gridBagConstraints.ipadx = 730;
		gridBagConstraints.ipady = 490;	
		
		add(panelForReceivedAndSend, gridBagConstraints);		
				
		gridBagConstraints.anchor = GridBagConstraints.NORTH;
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.insets = new Insets(0, 5, 0, 0);
		gridBagConstraints.ipadx = 200;
		gridBagConstraints.ipady = 200;
		add(panelForClients, gridBagConstraints);
		
		gridBagConstraints.anchor = GridBagConstraints.SOUTH;
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.insets = new Insets(0, 5, 0, 0);
		gridBagConstraints.ipadx = 200;
		gridBagConstraints.ipady = 290;	
		
		add(panelForOptions, gridBagConstraints);
		
//		repaint();
		
	}

	public static void main(String[] args) {
		
		WindowOfCzat windowOfCzat = new WindowOfCzat();

	}

}
