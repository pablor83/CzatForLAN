package chat;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ClientOfChat implements Runnable, KeyListener {

	private PanelForReceivedAndSend panelForReceivedAndSend;
	private String textForSend = null;
	private int port;
	private String ipToConnect;
	private String theLastIPConnection;
	private String sendingTime;

	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy'r.' HH:mm:ss");
	private Date date;
	private Thread thread;

	private List<String> listOfIPAddressesToDisconnect = new ArrayList<>();

	private boolean runThisThread = false;
	private boolean isReceivingTime = false;
	private boolean close = false;

	public ClientOfChat(PanelForReceivedAndSend panelForReceivedAndSend) {

		this.panelForReceivedAndSend = panelForReceivedAndSend;
		this.panelForReceivedAndSend.getFieldOfSendMessage().addKeyListener(this);
	}

	public ClientOfChat(PanelForReceivedAndSend panelForReceivedAndSend, int port) {

		this.panelForReceivedAndSend = panelForReceivedAndSend;
		this.panelForReceivedAndSend.getFieldOfSendMessage().addKeyListener(this);

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

		String tName = thread.getName();
		Socket socket = new Socket();
		InetSocketAddress inetSocketAddress = new InetSocketAddress(getIPToConnect(), getPort());
		System.out.println(inetSocketAddress);
		String theIPThatThisThreadCoonectTo = null;
		try {
			socket.connect(inetSocketAddress);
			theLastIPConnection = getIPToConnect();
			InetAddress ipRemote = socket.getInetAddress();
			theIPThatThisThreadCoonectTo = ipRemote.getHostAddress();
			
		} catch (UnknownHostException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}

		while (true) {

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

	synchronized public void closeThread(boolean b) {

		close = b;
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
