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

public class ServerForPrivateChat implements Runnable {

	private int port = 5332;

	private ServerSocket serverSocket;
	private String remoteIP;
	private Thread thread;
	private HashMap<Object, Boolean> hashMapForNotify = new HashMap<>();
	private Object nick;

	public ServerForPrivateChat() {

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
		int port = getServerPort();
		int remotePort;
		

		try {
			socket = serverSocket.accept();
			
			Object nick = this.nick;
			
			runNewThread();

			InetAddress remoteAdress = socket.getInetAddress();

			InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

			addressForConnection = remoteAdress.getHostAddress();

			remotePort = Integer.parseInt(bufferedReader.readLine());

			PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
			printWriter.println(port);
			printWriter.flush();

			PanelForReceivedAndSend panelForReceivedAndSend = new PanelForReceivedAndSend();
			panelForReceivedAndSend.setTextInWindowChat("serwer\n");

			ClientOfChat clientOfChat = new ClientOfChat(panelForReceivedAndSend);
			Server server = new Server(panelForReceivedAndSend, clientOfChat, getServerPort());
			
			setIncreasePort();
			
			synchronized (this) {				
				
				try {
					wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			System.out.println(hashMapForNotify.get(nick));
			if(hashMapForNotify.get(nick)) {
				System.out.println(hashMapForNotify.get(nick));
				PrivateChatWindow privateWindow = new PrivateChatWindow(addressForConnection, remotePort, panelForReceivedAndSend, clientOfChat, server, port, nick);
			}
			
			

			

		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	synchronized public void runNewThread() {

		thread = new Thread(this);
		thread.start();
	}

	synchronized public void setRemoteIP(InetAddress ip) {

		remoteIP = ip.getHostAddress();
	}

	synchronized public void setIncreasePort() {

		port++;
	}
	
	synchronized public void setNick(Object nick) {
		
		this.nick = nick;
		hashMapForNotify.put(nick, false);
	}
	
	synchronized public void openWindow(Object nick) {
		
		hashMapForNotify.replace(nick, false, true);
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
