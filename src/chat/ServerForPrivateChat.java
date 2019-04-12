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
//	private HashMap<String, Integer> checkPrivatePortOfWindowServer = new HashMap<>();
	private HashMap<String, Boolean> isPrivateWindowOpen = new HashMap<>();
	private HashMap<String, Integer> myPrivateWindowPortForClient = new HashMap<>();
	private HashMap<String, Integer> remotePortForMyClinet = new HashMap<>();
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
//		setIncreasePort();
		int remotePort;

		try {
			socket = serverSocket.accept();

			runNewThread();

			InetAddress localHostIP = InetAddress.getLocalHost();

			InetAddress remoteAdress = socket.getInetAddress();

			InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

			addressForConnection = remoteAdress.getHostAddress();

			remotePort = Integer.parseInt(bufferedReader.readLine());

			if (!localHostIP.equals(remoteAdress)) { // add window ON
				System.out.println("SET");
				setPortForMyClient(addressForConnection, remotePort);

			}

//			if (localHostIP.getHostAddress().equals(remoteAdress.getHostAddress()) && getPortForPrivateWindow(addressForConnection) != null) {
//				
//				PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
//				printWriter.println(getPortForPrivateWindow(addressForConnection));
//				printWriter.flush();

			if (getStatusOfPrivateWindow(addressForConnection) != null
					&& getStatusOfPrivateWindow(addressForConnection)) {

				PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
				printWriter.println(getMyPrivateWindowPortForRemoteClient(addressForConnection));
				printWriter.flush();

			} else {

				setIncreasePort();
//				setIPAndPortForCheck(addressForConnection, remotePort);

				PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
				printWriter.println(port);
				printWriter.flush();

				panelForReceivedAndSend = new PanelForReceivedAndSend();

				clientOfChat = new ClientOfChat(panelForReceivedAndSend);
				clientOfChat.setServerForPrivateChat(ServerForPrivateChat.this);
				Server server = new Server(panelForReceivedAndSend, clientOfChat, panelForClients, port, notificationPanel, true);
				server.setServerForPrivateChat(ServerForPrivateChat.this);
								
				while (true) {

					synchronized (this) {

						try {

							wait();

						} catch (InterruptedException e) {

							e.printStackTrace();
						}
					}					

					Object nick = panelForClients.getNickFromPanel(addressForConnection);
					
					if (hashMapForNotify.get(nick)) {
						
						PrivateChatWindow privateWindow = new PrivateChatWindow(addressForConnection, remotePort,
								panelForReceivedAndSend, clientOfChat, server, port, nick, ServerForPrivateChat.this);
						break;
					}

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

//	synchronized public void setIPAndPortForCheck(String ip, Integer port) {
//
//		if(getPortForPrivateWindow(ip) != null) {
//			
//			checkPrivatePortOfWindowServer.replace(ip, port);
//			
//		} else {
//			
//			checkPrivatePortOfWindowServer.put(ip, port);
//		}
//		
//	}

	synchronized public void setStatusOfPrivateWindow(String remoteIP, Boolean on) {

		isPrivateWindowOpen.put(remoteIP, on);
	}

	synchronized public void setMyPrivateWindowPortForRemoteClient(String remoteIP, Integer myPort) {

		myPrivateWindowPortForClient.put(remoteIP, myPort);
	}

	synchronized public void setPortForMyClient(String remoteIP, Integer remotePort) {

		remotePortForMyClinet.put(remoteIP, remotePort);
	}

	synchronized public void removePortForMyClinet(String remoteIP, Integer remotePort) {

		remotePortForMyClinet.remove(remoteIP, remotePort);
	}

	synchronized public void removeStatusOfPrivateWindow(String ip, Boolean on) {

		isPrivateWindowOpen.remove(ip, on);
	}

	synchronized public void removeMyPrivateWindowPortForRemoteClient(String remoteIP, Integer myPort) {

		myPrivateWindowPortForClient.remove(remoteIP, myPort);
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
	
	synchronized public void setNewNick(Object nick) {
		
		hashMapForNotify.replace(nick, false);
	}

	public void setPanelForClient(PanelForClients panelForClients) {

		this.panelForClients = panelForClients;
	}

	synchronized public void openWindow(Object nick) {

		hashMapForNotify.replace(nick, false, true);
	}

//	synchronized public void removePortFromHashMap(String ip) {
//		
//		checkPrivatePortOfWindowServer.remove(ip, checkPrivatePortOfWindowServer.get(ip));
//	}

	synchronized public Boolean getStatusOfPrivateWindow(String remoteIP) {

		return isPrivateWindowOpen.get(remoteIP);
	}

	synchronized public Integer getMyPrivateWindowPortForRemoteClient(String remoteIP) {

		return myPrivateWindowPortForClient.get(remoteIP);
	}

//	synchronized public Integer getPortForPrivateWindow(String ip) {
//
//		return checkPrivatePortOfWindowServer.get(ip);
//	}

	synchronized public Integer getPortForMyClient(String remoteIP) {

		return remotePortForMyClinet.get(remoteIP);
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
