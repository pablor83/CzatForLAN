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

	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy'r.' HH:mm:ss");

	private Thread thread;
	private Date date;

	private boolean sendMessage = true;
	private boolean closeServer = false;
	private boolean privateServer = false;

	private int port;
	private int numberOfPrivateServerThreads = 0;

	Server(PanelForReceivedAndSend panelForReceivedAndSend, ClientOfChat clientOfChat, int port) {

		this.panelForReceivedAndSend = panelForReceivedAndSend;
		this.clientOfChat = clientOfChat;
		System.out.println("Server port "+port);
		try {
			servSocket = new ServerSocket(port);
		} catch (IOException e) {

			e.printStackTrace();
		}

		startNewServer();

	}

	Server(PanelForReceivedAndSend panelForReceivedAndSend, ClientOfChat clientOfChat, PanelForClients panelForClients,
			int port) {

		this.panelForReceivedAndSend = panelForReceivedAndSend;
		this.clientOfChat = clientOfChat;
		this.panelForClients = panelForClients;
		this.port = port;
		
		try {
			servSocket = new ServerSocket(port);
		} catch (IOException e) {

			e.printStackTrace();
		}

		startNewServer();

	}

	public void startNewServer() {

		thread = new Thread(this);
		thread.start();

	}

	@Override
	public void run() {
		
		Socket socket = new Socket();
		InetAddress remoteIPAddress = null;
		InetAddress localIP;
		String name;

		try {
			socket = servSocket.accept();

			setIncreaseNumberOfPrivateServerThreads();			
			
			localIP = InetAddress.getLocalHost();
			remoteIPAddress = socket.getInetAddress();

			if (port == 4999 && !remoteIPAddress.getHostAddress().equals(localIP.getHostAddress())
					&& !remoteIPAddress.getHostAddress().equals(clientOfChat.getTheLastIPConnection())) {
				clientOfChat.runNewThreadOfClient(remoteIPAddress.getHostAddress(), 4999);
			}
			
			if(panelForClients == null && getNumberOfPrivateServerThreads() > 2) {
				
				InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
				BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
				int port = Integer.parseInt(bufferedReader.readLine());
				
				clientOfChat.runNewThreadOfClient(remoteIPAddress.getHostAddress(), port);
//				bufferedReader.close();
//				inputStreamReader.close();
			}

		} catch (IOException e) {

			e.printStackTrace();
		}

		name = remoteIPAddress.getHostName();

		if (panelForClients != null) {

			panelForClients.addNameOfClient(name);
			panelForClients.addIPToList(remoteIPAddress.getHostAddress());
		}

		date = new Date();
		panelForReceivedAndSend
				.setTextInWindowChat(name + "> " + "Po³¹czy³ siê" + "\n" + simpleDateFormat.format(date) + "\n\n");

		if (panelForClients != null) {

			startNewServer();
		} else {

			startNewServer();
		}
//		startNewServer();
		while (true) {

			try {

				InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
				BufferedReader bufferedStream = new BufferedReader(inputStreamReader);

				String text = bufferedStream.readLine();

				synchronized (this) {

					if (text == null) {

						panelForReceivedAndSend.setTextInWindowChat(
								name + "> " + "Zamkn¹³ okno" + "\n" + simpleDateFormat.format(date) + "\n\n");
//						setDecreaseNumberOfPrivateServerThreads();
						clientOfChat.setIPToDisconnect(remoteIPAddress.getHostAddress());
						socket.close();
						
						break;
					} else if(getStatusOfCloseServerOption()) {
																		
						socket.close();
//						servSocket.close();
						
						break;
					}

				}
				if (!getStatusOfCloseServerOption() && getSendMessage() && text != null && text.matches("/nick .+")) {
					String nameBeforeChange = name;

					name = text.substring(6);

					date = new Date();
					panelForReceivedAndSend.setTextInWindowChat(name + "> " + nameBeforeChange + " zmieni³ nick na "
							+ name + "\n" + simpleDateFormat.format(date) + "\n\n");
					if (panelForClients != null) {

						int numberOfCell = panelForClients.getTheTablePosition(nameBeforeChange);
						panelForClients.changeValueOfCell(name, numberOfCell, 0);
					}

				} else if (!getStatusOfCloseServerOption() && getSendMessage()) {

					if (!clientOfChat.getIsReceivingTime()) {

						panelForReceivedAndSend.setTextInWindowChat(
								name + "> " + text + "\n" + simpleDateFormat.format(new Date()) + "\n\n");
					}

					else {

						panelForReceivedAndSend.setTextInWindowChat(
								name + "> " + text + "\n" + clientOfChat.getSendindTime() + "\n\n");
						clientOfChat.setIsReceivingTime(false);
					}

				}

				setSendMessage(true);

			} catch (IOException e) {

//				e.printStackTrace();

				if (panelForClients != null) {
					int numberOfCell = panelForClients.getTheTablePosition(name);
					panelForClients.removeClientPanelList(panelForClients.getTheTablePosition(name));
					panelForClients.removeIPFromList(panelForClients.getTheTablePosition(name));
				}

				panelForReceivedAndSend.setTextInWindowChat(
						name + "> " + "Roz³¹czy³ siê" + "\n" + simpleDateFormat.format(date) + "\n\n");
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

	synchronized public void setDecreaseNumberOfPrivateServerThreads() {

		numberOfPrivateServerThreads--;
	}
	
	synchronized public void setIncreaseNumberOfPrivateServerThreads() {

		numberOfPrivateServerThreads++;
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

	synchronized public int getNumberOfPrivateServerThreads() {

		return numberOfPrivateServerThreads;
	}

}