package chat;

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

public class WindowOfChat extends JFrame {

	private NotificationPanel notificationPanel = new NotificationPanel();
	private ServerForPrivateChat serverForPrivateChat = new ServerForPrivateChat(notificationPanel);
	
	private WindowOfChat() {

		setMinimumSize(new Dimension(970, 570));

		setTitle("Chat for LAN");
		setLocationRelativeTo(null);
		setLayout(new GridBagLayout());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
				
		PanelForReceivedAndSend panelForReceivedAndSend = new PanelForReceivedAndSend();		
		notificationPanel.setPrivateServer(serverForPrivateChat);		
		PanelForClients panelForClients = new PanelForClients(panelForReceivedAndSend, notificationPanel, serverForPrivateChat);
		serverForPrivateChat.setPanelForClient(panelForClients);
		
		ClientOfChat clientOfChat = new ClientOfChat(panelForReceivedAndSend, 4999);
		Server server = new Server(panelForReceivedAndSend, clientOfChat, panelForClients, 4999);
		ServerUDP serverUDP = new ServerUDP(clientOfChat);
		
		JMenuBar menuBar = new JMenuBar();

		GridBagConstraints gridBagConstraints = new GridBagConstraints();

		JMenu menu = new JMenu("Plik");
		menuBar.add(menu);

		JMenu help = new JMenu("Pomoc");
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

				if ((getHeight() - 380) >= 300) {
					gridBagConstraints.insets = new Insets(0, 5, getHeight() - 680, 0);
				} else
					gridBagConstraints.insets = new Insets(0, 5, 0, 0);

				gridBagConstraints.ipadx = 200;
				if ((getHeight() - 280) <= 400) {
					gridBagConstraints.ipady = 290;

				}
				
				add(notificationPanel, gridBagConstraints);

				revalidate();
			}
		});

		setVisible(true);

		ClientUDP clientUDP = new ClientUDP();
	}

	public static void main(String[] args) {

		WindowOfChat windowOfChat = new WindowOfChat();

	}

}
