package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerForPrivateChat implements Runnable {

	int port = 5332;
	ServerSocket serverSocket;
	String remoteIP;
	Thread thread;

	public ServerForPrivateChat() {

		try {
			serverSocket = new ServerSocket(5111);
		} catch (IOException e) {

			e.printStackTrace();
		}

		thread = new Thread(this);
		thread.start();
	}

//	public static void main(String[] args) {
//
//		ServerForPrivateChat serv = new ServerForPrivateChat();
//
//	}

	@Override
	public void run() {

		Socket socket = new Socket();
		String addressForConnection;
		int port = getServerPort();
		int remotePort;

		try {
			socket = serverSocket.accept();

//			runNewThread();

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
			panelForReceivedAndSend.setTextInWindowChat("Lubie placki\nJohny Bravo");
			
			ClientOfChat clientOfChat = new ClientOfChat(panelForReceivedAndSend);
			Server server = new Server(panelForReceivedAndSend, clientOfChat, getServerPort());
			
			System.out.println(addressForConnection+" S "+remotePort);
			PrivateChatWindow privateWindow = new PrivateChatWindow(addressForConnection, remotePort,
					panelForReceivedAndSend, clientOfChat, server, getServerPort());

//			setIncreasePort();

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

	synchronized public String getRemoteIP() {

		return remoteIP;
	}

	synchronized public int getServerPort() {

		return port;
	}

}
