
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

public class WindowOfCzat extends JFrame {

	PanelForReceivedAndSend panelForReceivedAndSend = new PanelForReceivedAndSend();
	PanelForClients panelForClients = new PanelForClients();
	PanelForOptions panelForOptions = new PanelForOptions();

	JMenuBar menuBar = new JMenuBar();
	JMenu menu, help;

	private WindowOfCzat() {

		setMinimumSize(new Dimension(970, 570));

		setTitle("Czat for LAN");
		setLocationRelativeTo(null);
		setLayout(new GridBagLayout());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);

		GridBagConstraints gridBagConstraints = new GridBagConstraints();

		menu = new JMenu("Plik");
		menuBar.add(menu);

		help = new JMenu("Pomoc");
		menuBar.add(Box.createHorizontalGlue());
		menuBar.add(help);

		setJMenuBar(menuBar);

		getContentPane().addComponentListener(new ComponentAdapter() {

			public void componentResized(ComponentEvent e) {

				gridBagConstraints.anchor = GridBagConstraints.WEST;
				gridBagConstraints.gridx = 0;
				gridBagConstraints.gridy = 0;
				gridBagConstraints.insets = new Insets(0, 0, 0, 10);
				gridBagConstraints.ipadx = getWidth() - 240;
				gridBagConstraints.ipady = getHeight() - 80;

				add(panelForReceivedAndSend, gridBagConstraints);

				gridBagConstraints.anchor = GridBagConstraints.NORTH;
				gridBagConstraints.gridx = 1;
				gridBagConstraints.gridy = 0;
				gridBagConstraints.insets = new Insets(0, 5, 0, 0);
				gridBagConstraints.ipadx = 200;

				if ((getHeight() - 280) < 400) {
					gridBagConstraints.ipady = getHeight() - 370;
				} else
					gridBagConstraints.ipady = 300;

				add(panelForClients, gridBagConstraints);

				gridBagConstraints.anchor = GridBagConstraints.SOUTH;
				gridBagConstraints.gridx = 1;
				gridBagConstraints.gridy = 0;
				
				if((getHeight() - 380) >= 300) {
				gridBagConstraints.insets = new Insets(0, 5, getHeight()-680, 0);
				}else
					gridBagConstraints.insets = new Insets(0, 5, 0, 0);
				
				gridBagConstraints.ipadx = 200;
//				gridBagConstraints.ipady = 290; (getHeight() - 280) >= 290
				if ((getHeight() - 280) <= 400) {
					gridBagConstraints.ipady = 290;
					
				}

				add(panelForOptions, gridBagConstraints);

				revalidate();
			}
		});
	}

	public static void main(String[] args) {

		WindowOfCzat windowOfCzat = new WindowOfCzat();

	}

}
