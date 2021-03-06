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
	private Object name;

	public PrivateChatWindow(String ipForClientConnection, int portForClientConection,
			PanelForReceivedAndSend panelForReceiveAndSend, ClientOfChat privateClientOfChat, Server privateServer,
			int myPort, Object name, ServerForPrivateChat serverForPrivateChat) {

		this.name = name;;

		setTitle("Prywatna rozmowa z " + this.name);
		setMinimumSize(new Dimension(600, 600));
		setLocationRelativeTo(null);
//		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);

		InetAddress myIP = null;

		try {
			myIP = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {

			e.printStackTrace();
		}

		clientOfChat = privateClientOfChat;
		server = privateServer;

		clientOfChat.setMyLocalServerPort(myPort);
		
		privateClientOfChat.runNewThreadOfClient(myIP.getHostAddress(), myPort);

		try {
			privateClientOfChat.getThread().join(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		privateClientOfChat.runNewThreadOfClient(ipForClientConnection, portForClientConection);
		
		serverForPrivateChat.setStatusOfPrivateWindow(ipForClientConnection, true);
		serverForPrivateChat.setMyPrivateWindowPortForRemoteClient(ipForClientConnection, myPort);

		addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent windowEvent) {

				clientOfChat.closeThread(true);
				clientOfChat.setThreadsAsActive();
				
//				if(server.getNumberOfThreads() == 1) {
//					serverForPrivateChat.removePortFromHashMap(ipForClientConnection);
//				}
				
				serverForPrivateChat.removeMyPrivateWindowPortForRemoteClient(ipForClientConnection, myPort);
				serverForPrivateChat.removePortForMyClinet(ipForClientConnection, portForClientConection);
				serverForPrivateChat.removeStatusOfPrivateWindow(ipForClientConnection, true);
				
				server.setCloseServer(true);
				
				dispose();

			}
		});

		add(panelForReceiveAndSend);
		pack();
		setLayout(null);
	}
}
