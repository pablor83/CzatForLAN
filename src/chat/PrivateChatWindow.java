package chat;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JFrame;

public class PrivateChatWindow extends JFrame {

	private ClientOfChat clientOfChat;
	private Server server;

	public PrivateChatWindow(String ipForClientConnection, int portForClientConection,
			PanelForReceivedAndSend panelForReceiveAndSend, ClientOfChat privateClientOfChat, Server privateServer, int myPort) {

		setTitle("Prywatna rozmowa");
		setMinimumSize(new Dimension(600, 600));
		setLocationRelativeTo(null);
//		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);

		InetAddress myIP = null;

//		this.clientOfChat = clientOfChat;
//		this.server = server;

		try {
			myIP = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (privateClientOfChat == null && privateServer == null) {

//			privateClientOfChat = new ClientOfChat(panelForReceiveAndSend);
//			privateServer = new Server(panelForReceiveAndSend, privateClientOfChat, runServerOnPort);
		} else {

			clientOfChat = privateClientOfChat;
			server = privateServer;
		}

		privateClientOfChat.runNewThreadOfClient(myIP.getHostAddress(), myPort);

		try {
			privateClientOfChat.getThread().join(10);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(ipForClientConnection + " C " + portForClientConection);
		privateClientOfChat.runNewThreadOfClient(ipForClientConnection, portForClientConection);

		addWindowListener(new WindowAdapter() {
			
			public void windowClosing(WindowEvent windowEvent) {
				
				server.setCloseServer(true);
				clientOfChat.closeThread(true);				
				clientOfChat.setThreadsAsActive();

				dispose();

			}
		});

		add(panelForReceiveAndSend);
		pack();
		setLayout(null);
	}

}
