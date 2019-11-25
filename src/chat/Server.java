package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Server implements Runnable {

	private ServerSocket servSocket;

	private PanelForReceivedAndSend panelForReceivedAndSend;
	private ClientOfChat clientOfChat;
	private PanelForClients panelForClients;
	private ServerForPrivateChat serverForPrivateChat;
	private NotificationPanel notificationPanel;

	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy'r.' HH:mm:ss");

	private Thread thread;
	private Date date;

	private boolean sendMessage = true;
	private boolean closeServer = false;
	private boolean privateServer = false;
	private boolean setNotification = true;
	private boolean windowOfPrivateChat;

	private int port;
	private int counterOfConnections = 0;
//	private int numberOfThreads = 0;

	Server(PanelForReceivedAndSend panelForReceivedAndSend, ClientOfChat clientOfChat, PanelForClients panelForClients,
			int port, NotificationPanel notificationPanel, boolean windowOfPrivateChat) {

		this.panelForReceivedAndSend = panelForReceivedAndSend;
		this.clientOfChat = clientOfChat;
		this.panelForClients = panelForClients;
		this.port = port;
		this.notificationPanel = notificationPanel;
		this.windowOfPrivateChat = windowOfPrivateChat;

		try {
			servSocket = new ServerSocket(this.port);
		} catch (IOException e) {

			e.printStackTrace();
		}

		startNewServer();

	}

//	Server(PanelForReceivedAndSend panelForReceivedAndSend, ClientOfChat clientOfChat, PanelForClients panelForClients,
//			int port) {
//
//		this.panelForReceivedAndSend = panelForReceivedAndSend;
//		this.clientOfChat = clientOfChat;
//		this.panelForClients = panelForClients;
//		this.port = port;
//
//		try {
//			servSocket = new ServerSocket(port);
//		} catch (IOException e) {
//
//			e.printStackTrace();
//		}
//
//		startNewServer();
//
//	}

	public void startNewServer() {

		thread = new Thread(this);
		thread.start();

	}

	@Override
	public void run() {
		String s = thread.getName();
		Thread thread = this.thread;
		Socket socket = new Socket();
		InetAddress remoteIPAddress = null;
		InetAddress localIP = null;
		String name = null;
		
		boolean startLoop = true;

		try {
			socket = servSocket.accept();
			setIncreaseNumberOfConnections();
			localIP = InetAddress.getLocalHost();
			remoteIPAddress = socket.getInetAddress();
			
//			if (remoteIPAddress.equals("127.0.0.1")) {
//				
//				startLoop = false;
//				socket.close();
//				servSocket.close();
//				
//			} else {

//				setIncreaseNumberOfThreads();

			if (port == 4999 && !remoteIPAddress.getHostAddress().equals(localIP.getHostAddress())
					&& !remoteIPAddress.getHostAddress().equals(clientOfChat.getTheLastIPConnection())) {

				clientOfChat.runNewThreadOfClient(remoteIPAddress.getHostAddress(), 4999);
			}

			if (windowOfPrivateChat == true && getNumberOfConnections() > 2
					&& !localIP.getHostAddress().equals(remoteIPAddress.getHostAddress())) {

//					InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
//					BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//					int port = Integer.parseInt(bufferedReader.readLine());
				
				if(serverForPrivateChat.getPortForMyClient(remoteIPAddress.getHostAddress()) != null) {
					
					clientOfChat.runNewThreadOfClient(remoteIPAddress.getHostAddress(),
							serverForPrivateChat.getPortForMyClient(remoteIPAddress.getHostAddress()));
				}
				
				

			}

//				if (panelForClients == null && getNumberOfConnections() > 2) {
//
//					InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
//					BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//					int port = Integer.parseInt(bufferedReader.readLine());
//					
//					clientOfChat.runNewThreadOfClient(remoteIPAddress.getHostAddress(), port);
//
//				}

			name = remoteIPAddress.getHostName();

			if (windowOfPrivateChat == false) {

				panelForClients.addNameOfClient(name);
				panelForClients.addIPToList(remoteIPAddress.getHostAddress());
			}

			date = new Date();
			panelForReceivedAndSend
					.setTextInWindowChat(name + "> " + "Połączył się" + "\n" + simpleDateFormat.format(date) + "\n\n");

			if (windowOfPrivateChat == false) {

				startNewServer();

			} else if (windowOfPrivateChat == true && getNumberOfConnections() < 2) {

				startNewServer();
			}

//			}

		} catch (IOException e) {

			e.printStackTrace();
		}

		while (startLoop) {

			try {

				InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
				BufferedReader bufferedStream = new BufferedReader(inputStreamReader);

				String text = bufferedStream.readLine();
				System.out.println(text);
				synchronized (this) {

					if (text == null) {

						panelForReceivedAndSend.setTextInWindowChat(
								name + "> " + "Zamknął okno" + "\n" + simpleDateFormat.format(date) + "\n\n");
//						setDecreaseNumberOfConnections();
//						setDecreaseNumberOfThreads();
						clientOfChat.setIPToDisconnect(remoteIPAddress.getHostAddress());
						clientOfChat.setThreadsAsActive();
						bufferedStream.close();
						inputStreamReader.close();
						socket.close();

						if (!getStatusOfCloseServerOption()) {
//							setIncreaseNumberOfConnections();
							startNewServer();
						} else {

//							Socket socketToCloseSocket = new Socket("localhost", port);
//							bufferedStream.close();
//							inputStreamReader.close();
//							socket.close();
//							socketToCloseSocket.close();
//							servSocket.close();
						}

						break;
					}

				}

				if (!getStatusOfCloseServerOption() && getSendMessage() && text != null && text.matches("/nick .+")) {

					String nameBeforeChange = name;

					name = text.substring(6);

					date = new Date();
					panelForReceivedAndSend.setTextInWindowChat(name + "> " + nameBeforeChange + " zmienił nick na "
							+ name + "\n" + simpleDateFormat.format(date) + "\n\n");

					

					if (windowOfPrivateChat == false) {

						int numberOfCell = panelForClients.getTheTablePosition(nameBeforeChange);
						panelForClients.changeValueOfCell(name, numberOfCell, 0);
					} else {
						
						serverForPrivateChat.setNewNick(name);
					}

				} else if (!getStatusOfCloseServerOption() && getSendMessage()) {

					if (!clientOfChat.getIsReceivingTime()) {

						panelForReceivedAndSend.setTextInWindowChat(
								name + "> " + text + "\n" + simpleDateFormat.format(new Date()) + "\n\n");

						if (setNotification && panelForClients != null && !localIP.getHostAddress().equals(remoteIPAddress.getHostAddress())
								&& windowOfPrivateChat == true) {

							Object nick = panelForClients.getNickFromPanel(remoteIPAddress.getHostAddress());
							serverForPrivateChat.setNick(nick);

							notificationPanel.setNotification(nick);
							setNotification = false;
						}

					} else {

						panelForReceivedAndSend.setTextInWindowChat(
								name + "> " + text + "\n" + clientOfChat.getSendindTime() + "\n\n");
						clientOfChat.setIsReceivingTime(false);

					}

				}

				setSendMessage(true);

			} catch (IOException e) {

//				e.printStackTrace();

				if (windowOfPrivateChat == false) {

					int numberOfCell = panelForClients.getTheTablePosition(name);
					panelForClients.removeClientPanelList(panelForClients.getTheTablePosition(name));
					panelForClients.removeIPFromList(panelForClients.getTheTablePosition(name));

				}

				panelForReceivedAndSend.setTextInWindowChat(
						name + "> " + "Rozłączył się" + "\n" + simpleDateFormat.format(date) + "\n\n");
				clientOfChat.setIPToDisconnect(remoteIPAddress.getHostAddress());
				setSendMessage(false);
				clientOfChat.setThreadsAsActive();
				try {
					socket.close();
				} catch (IOException e1) {

					e1.printStackTrace();
				}
				break;
			}

		}
		
	}
	

	synchronized public void setSendMessage(boolean sendMessage) {

		this.sendMessage = sendMessage;
	}

	public void setCloseServer(boolean b) {

		closeServer = b;
	}

	synchronized public void setPrivateServer(boolean b) {

		privateServer = b;
	}

	synchronized public void setDecreaseNumberOfConnections() {

		counterOfConnections--;
	}

//	synchronized public void setDecreaseNumberOfThreads() {
//
//		numberOfThreads--;
//	}

	synchronized public void setIncreaseNumberOfConnections() {

		counterOfConnections++;
	}

//	synchronized public void setIncreaseNumberOfThreads() {
//
//		numberOfThreads++;
//	}

	public void setServerForPrivateChat(ServerForPrivateChat serverForPrivateChat) {

		this.serverForPrivateChat = serverForPrivateChat;
	}

	synchronized public boolean getSendMessage() {

		return sendMessage;
	}

	public boolean getStatusOfCloseServerOption() {

		return closeServer;
	}

	synchronized public boolean getStatusOfPrivateServer() {

		return privateServer;
	}

	synchronized public int getNumberOfConnections() {

		return counterOfConnections;
	}

//	synchronized public int getNumberOfThreads() {
//
//		return numberOfThreads;
//	}

}