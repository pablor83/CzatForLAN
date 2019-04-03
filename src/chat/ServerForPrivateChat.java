package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import javax.management.ObjectInstance;

public class ServerForPrivateChat implements Runnable {

	private int port = 5332;

	private ServerSocket serverSocket;
	private String remoteIP;
	private Thread thread;
	private HashMap<Object, Boolean> hashMapForNotify = new HashMap<>();
	private HashMap<String, Integer> checkPrivatePortOfWindowServer = new HashMap<>();
	private NotificationPanel notificationPanel;
	private PanelForClients panelForClients;

	public ServerForPrivateChat(NotificationPanel notificationPanel) {

		this.notificationPanel = notificationPanel;

		try {
			serverSocket = new ServerSocket(5111);
		} catch (IOException e) {

			e.printStackTrace();
		}

		thread = new Thread(this);
		thread.start();
	}

	@Override
	public void run() {
		
		Socket socket = new Socket();
		String addressForConnection;
		ClientOfChat clientOfChat = null;
		PanelForReceivedAndSend panelForReceivedAndSend = new PanelForReceivedAndSend();
		int port = getServerPort();
		setIncreasePort();
		int remotePort;

		try {
			socket = serverSocket.accept();

			runNewThread();

			InetAddress remoteAdress = socket.getInetAddress();

			Object nick;

			nick = panelForClients.getNickFromPanel(remoteAdress.getHostAddress());
			setNick(nick);

			InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

			addressForConnection = remoteAdress.getHostAddress();

			remotePort = Integer.parseInt(bufferedReader.readLine());

			if (getPortForPrivateWindow(addressForConnection) != null) {
				
				PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
				printWriter.println(getPortForPrivateWindow(addressForConnection));
				printWriter.flush();

			} else {

				setIPAndPortForCheck(addressForConnection, remotePort);
				PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
				printWriter.println(port);
				printWriter.flush();

				panelForReceivedAndSend = new PanelForReceivedAndSend();

				clientOfChat = new ClientOfChat(panelForReceivedAndSend);				
				Server server = new Server(panelForReceivedAndSend, clientOfChat, port);

				notificationPanel.setNotification(nick);

				synchronized (this) {

					try {
						wait();
					} catch (InterruptedException e) {

						e.printStackTrace();
					}
				}

				if (hashMapForNotify.get(nick)) {

					PrivateChatWindow privateWindow = new PrivateChatWindow(addressForConnection, remotePort,
							panelForReceivedAndSend, clientOfChat, server, port, nick, ServerForPrivateChat.this);
				}

			}

		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	synchronized public void runNewThread() {

		thread = new Thread(this);
		thread.start();
	}

	synchronized public void setIPAndPortForCheck(String ip, Integer port) {

		checkPrivatePortOfWindowServer.put(ip, port);
	}

	synchronized public void setRemoteIP(InetAddress ip) {

		remoteIP = ip.getHostAddress();
	}

	synchronized public void setIncreasePort() {

		port++;
	}

	synchronized public void setNick(Object nick) {

		hashMapForNotify.put(nick, false);
	}

	public void setPanelForClient(PanelForClients panelForClients) {

		this.panelForClients = panelForClients;
	}

	synchronized public void openWindow(Object nick) {

		hashMapForNotify.replace(nick, false, true);
	}
	
	synchronized public void removePortFromHashMap(String ip) {
		
		checkPrivatePortOfWindowServer.remove(ip, checkPrivatePortOfWindowServer.get(ip));
	}

	synchronized public Integer getPortForPrivateWindow(String ip) {

		return checkPrivatePortOfWindowServer.get(ip);
	}

	synchronized public String getRemoteIP() {

		return remoteIP;
	}

	synchronized public int getServerPort() {

		return port;
	}

//	synchronized public PrivateChatWindow getPrivateChatWindow(int i) {
//
//		return listOfPrivateChatWindows.get(i);
//	}

	synchronized public void wakeUp() {

		notifyAll();
	}

}
