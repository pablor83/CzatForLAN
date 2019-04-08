package chat;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.print.attribute.standard.Severity;
import javax.swing.plaf.metal.MetalBorders.Flush3DBorder;

public class ClientOfChat implements Runnable, KeyListener {

	private PanelForReceivedAndSend panelForReceivedAndSend;
	private ServerForPrivateChat serverForPrivateChat = null;
	private String textForSend = null;
	private int port;
	private int myLocalServerPort;
	private int numberOfConnectionAttempts = 0;
	private String ipToConnect;
	private String theLastIPConnection;
	private String sendingTime;

	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy'r.' HH:mm:ss");
	private Date date;
	private Thread thread;
	private boolean reconnect = false;

	private List<String> listOfIPAddressesToDisconnect = new ArrayList<>();

	private boolean runThisThread = false;
	private boolean isReceivingTime = false;
	private boolean close = false;

	public ClientOfChat(PanelForReceivedAndSend panelForReceivedAndSend) {

		this.panelForReceivedAndSend = panelForReceivedAndSend;
		this.panelForReceivedAndSend.getFieldOfSendMessage().addKeyListener(this);
	}

	public ClientOfChat(PanelForReceivedAndSend panelForReceivedAndSend, boolean reconnect) {

		this.panelForReceivedAndSend = panelForReceivedAndSend;
		this.panelForReceivedAndSend.getFieldOfSendMessage().addKeyListener(this);
		this.reconnect = reconnect;
	}

	public ClientOfChat(PanelForReceivedAndSend panelForReceivedAndSend, int port) {

		this.panelForReceivedAndSend = panelForReceivedAndSend;
		this.panelForReceivedAndSend.getFieldOfSendMessage().addKeyListener(this);
		this.port = port;

		InetAddress localIPtoConnect;

		try {
			localIPtoConnect = InetAddress.getLocalHost();
			runNewThreadOfClient(localIPtoConnect.getHostAddress(), port);
		} catch (UnknownHostException e) {

			e.printStackTrace();
		}

	}

	@Override
	public void run() {
		String t = thread.getName();
		String tName = thread.getName();
		Socket socket = new Socket();
		String ipToConnect = this.ipToConnect;
		InetSocketAddress inetSocketAddress = new InetSocketAddress(ipToConnect, getPort());
		System.out.println("CLIENT " + inetSocketAddress);
		String theIPThatThisThreadCoonectTo = null;
		InetAddress ipCheck = null;

		boolean startLoop = true;

		try {

			ipCheck = InetAddress.getLocalHost();
		} catch (UnknownHostException e1) {

			e1.printStackTrace();
		}

		try {

			socket.connect(inetSocketAddress, 3500);

			theLastIPConnection = getIPToConnect();
			InetAddress ipRemote = socket.getInetAddress();

			theIPThatThisThreadCoonectTo = ipRemote.getHostAddress();

			if (!theIPThatThisThreadCoonectTo.equals(ipCheck.getHostAddress()) && getPort() != 4999) {

				serverForPrivateChat.setIPAndPortForCheck(ipToConnect, port);
			}

			if (!theIPThatThisThreadCoonectTo.equals(ipCheck.getHostAddress()) && getReconnectStatus()) {

				sendMyServerPort(socket);
			}

		} catch (UnknownHostException e) {

			e.printStackTrace();

		} catch (IOException e) {
			
			numberOfConnectionAttempts++;
			
			try {
				socket.close();
			} catch (IOException e1) {

				e1.printStackTrace();
			}

			startLoop = false;

			if (numberOfConnectionAttempts < 2)
				getRemotePortAndConnect(ipToConnect);

//			e.printStackTrace();
		}

		while (startLoop) {

			try {

				synchronized (this) {
					if (!runThisThread) {

						wait();
					}
				}

				if (getStatusOfClosingOption()) {

					socket.close();
					break;
				}

				if (getIPToDisconnect(theIPThatThisThreadCoonectTo)) {

					socket.close();
					removeIPAfterDisconnection(theIPThatThisThreadCoonectTo);
					setSnoozeThreads();
					break;

				}

				if (getTextForSend() != null) {
					date = new Date();
					PrintWriter sendMessage = new PrintWriter(socket.getOutputStream());
					sendMessage.println(getTextForSend());
					sendMessage.flush();

				}

			} catch (IOException e) {

				e.printStackTrace();
			} catch (InterruptedException e) {

				e.printStackTrace();
			}

			setSnoozeThreads();
		}

//		try {
//			socket.close();
//		} catch (IOException e) {
//
//			e.printStackTrace();
//		}

		System.out.println("END " + t);
	}

	synchronized public void runNewThreadOfClient(String ipToConnect) {

		this.ipToConnect = ipToConnect;
		port = 4999;
		thread = new Thread(this);
		thread.start();
	}

	synchronized public void runNewThreadOfClient(String ipToConnect, int port) {

		thread = new Thread(this);
		this.ipToConnect = ipToConnect;
		this.port = port;
		thread.start();
	}

	synchronized public void setThreadsAsActive() {

		runThisThread = true;
		notifyAll();
	}

	synchronized public void setSnoozeThreads() {

		runThisThread = false;
	}

	synchronized public void setIPToDisconnect(String ip) {

		listOfIPAddressesToDisconnect.add(ip);
	}

	synchronized public void setPortToConnect(int port) {

		this.port = port;
	}

	synchronized public void setIsReceivingTime(boolean isReceivingTime) {

		this.isReceivingTime = isReceivingTime;
	}

	public void setMyLocalServerPort(int myLocalServerPort) {

		this.myLocalServerPort = myLocalServerPort;
	}

	public void setReconnect(boolean b) {

		reconnect = b;
	}

	public void setServerForPrivateChat(ServerForPrivateChat serverForPrivateChat) {

		this.serverForPrivateChat = serverForPrivateChat;
	}

	synchronized public void closeThread(boolean b) {

		close = b;
	}

	synchronized public boolean getReconnectStatus() {

		return reconnect;
	}

	synchronized public boolean getStatusOfClosingOption() {

		return close;
	}

	synchronized public String getTextForSend() {

		return textForSend;
	}

	synchronized public void removeIPAfterDisconnection(String ip) {

		listOfIPAddressesToDisconnect.remove(ip);
	}

	public void getRemotePortAndConnect(String ip) {

		try {

			Socket socketForPrivateServer = new Socket(ip, 5111);
			PrintWriter printWriter = new PrintWriter(socketForPrivateServer.getOutputStream());
			printWriter.println(myLocalServerPort);
			printWriter.flush();

			InputStreamReader inputStreamReader = new InputStreamReader(socketForPrivateServer.getInputStream());
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			int port = Integer.valueOf(bufferedReader.readLine());

			serverForPrivateChat.setIPAndPortForCheck(ip, port);
			System.out.println("AAAAAAAAAAA");
			runNewThreadOfClient(ip, port);

			bufferedReader.close();
			inputStreamReader.close();
			socketForPrivateServer.close();

		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	public String getTheLastIPConnection() {

		return theLastIPConnection;
	}

	synchronized public boolean getIPToDisconnect(String ipAddress) {

		boolean disconnect = false;

		for (String ip : listOfIPAddressesToDisconnect) {

			if (ipAddress.equals(ip))
				disconnect = true;
		}

		return disconnect;
	}

	synchronized public String getSendindTime() {

		return sendingTime;
	}

	synchronized public boolean getIsReceivingTime() {

		return isReceivingTime;
	}

	synchronized public String getIPToConnect() {

		return ipToConnect;
	}

	synchronized public int getPort() {

		return port;
	}

	synchronized public Thread getThread() {

		return thread;
	}

	synchronized public void sendMyServerPort(Socket socket) {

		try {
			PrintWriter sendMessage = new PrintWriter(socket.getOutputStream());
			sendMessage.println(myLocalServerPort);

			sendMessage.flush();

		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	synchronized public void keyPressed(KeyEvent e) {

		if (e.getKeyCode() == KeyEvent.VK_ENTER) {

			panelForReceivedAndSend.getFieldOfSendMessage().setEditable(false);

			String text = panelForReceivedAndSend.getTextFromfieldOfSendMessage();

			if (text.equals("/clear")) {

				panelForReceivedAndSend.clearWindow();
				panelForReceivedAndSend.getFieldOfSendMessage().setText("");

			} else if (text.matches(".+")) {

				textForSend = panelForReceivedAndSend.getTextFromfieldOfSendMessage();
				date = new Date();
				sendingTime = "Wys³ano: " + simpleDateFormat.format(date);
				isReceivingTime = true;
				panelForReceivedAndSend.getFieldOfSendMessage().setText("");
				setThreadsAsActive();
			}

		}
	}

	@Override
	public void keyReleased(KeyEvent e) {

		if (e.getKeyCode() == KeyEvent.VK_ENTER) {

			panelForReceivedAndSend.getFieldOfSendMessage().setEditable(true);

		}

	}

}
