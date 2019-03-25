package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientForPrivateMessage implements Runnable {
	
	private int remotePort;
	private int myServerPort;
	
	private String addressForConnection;
	private PanelForReceivedAndSend panelForReceivedAndSend;
	private ClientOfChat clientOfChat;
	private Server server;
	private Object name;

	public ClientForPrivateMessage(String addressForConnection, PanelForReceivedAndSend panelForReceivedAndSend, int myServerPort, Object name) {
		
		this.addressForConnection = addressForConnection;
		this.panelForReceivedAndSend = panelForReceivedAndSend;
		this.myServerPort = myServerPort;
		this.name = name;

		try {
			Socket socket = new Socket(addressForConnection, 5111);

			clientOfChat = new ClientOfChat(panelForReceivedAndSend);
			server = new Server(panelForReceivedAndSend, clientOfChat, myServerPort);
			
			PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
			printWriter.println(myServerPort);
			printWriter.flush();

			InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

			remotePort = Integer.parseInt(bufferedReader.readLine());

			Thread thread = new Thread(this);
			thread.start();

			
			
			bufferedReader.close();
			socket.close();

		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}

	public int getRemotePort() {

		return remotePort;
	}

	@Override
	public void run() {
		
		PrivateChatWindow privateChatWindow = new PrivateChatWindow(addressForConnection, getRemotePort(),
				panelForReceivedAndSend, clientOfChat, server, myServerPort, name);
		
	}

}
