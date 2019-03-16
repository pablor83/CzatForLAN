package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientForPrivateMessage implements Runnable {

	private int myServerPort = 5777;
	private int remotePort;
	
	private String addressForConnection;
	private PanelForReceivedAndSend panelForReceivedAndSend;
	private ClientOfChat clientOfChat;
	private Server server;

	public ClientForPrivateMessage(String addressForConnection, PanelForReceivedAndSend panelForReceivedAndSend) {
		
		this.addressForConnection = addressForConnection;
		this.panelForReceivedAndSend = panelForReceivedAndSend;

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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int getRemotePort() {

		return remotePort;
	}

	@Override
	public void run() {
		
		PrivateChatWindow privateChatWindow = new PrivateChatWindow(addressForConnection, getRemotePort(),
				panelForReceivedAndSend, clientOfChat, server, myServerPort);
		
		myServerPort++;
		
	}

}
