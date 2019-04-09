package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientForPrivateMessage implements Runnable {

	private int remotePort;
	private int myServerPort;
	private int remotePrivateServerPort;

	private String addressForConnection;
	private PanelForReceivedAndSend panelForReceivedAndSend;
	private ClientOfChat clientOfChat;
	private Server server;
	private Object name;
	private ServerForPrivateChat serverForPrivateChat;

	boolean reconnect = false;

	public ClientForPrivateMessage(String addressForConnection, PanelForReceivedAndSend panelForReceivedAndSend,
			int myServerPort, Object name, int remotePrivateServerPort, ServerForPrivateChat serverForPrivateChat,
			boolean reconnect) {

		this.addressForConnection = addressForConnection;
		this.panelForReceivedAndSend = panelForReceivedAndSend;
		this.myServerPort = myServerPort;
		this.name = name;
		this.remotePrivateServerPort = remotePrivateServerPort;
		this.serverForPrivateChat = serverForPrivateChat;
		this.reconnect = reconnect;

		try {

//			if (this.reconnect) {
//
//				clientOfChat = new ClientOfChat(panelForReceivedAndSend, true);
//			} else {
//
//				clientOfChat = new ClientOfChat(panelForReceivedAndSend);
//			}
			clientOfChat = new ClientOfChat(panelForReceivedAndSend);
			clientOfChat.setMyLocalServerPort(myServerPort);
			clientOfChat.setServerForPrivateChat(this.serverForPrivateChat);
			server = new Server(panelForReceivedAndSend, clientOfChat, myServerPort);
			server.setServerForPrivateChat(serverForPrivateChat);

			if (this.remotePrivateServerPort == 5111) {

				Socket socket = new Socket(this.addressForConnection, this.remotePrivateServerPort);
				PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
				printWriter.println(myServerPort);
				printWriter.flush();

				InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
				BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
				
				remotePort = Integer.parseInt(bufferedReader.readLine());

				bufferedReader.close();
				socket.close();
			}
			
//			else {
//
//				remotePort = this.remotePrivateServerPort;
//			}

			Thread thread = new Thread(this);
			thread.start();

		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	public int getRemotePort() {

		return remotePort;
	}

	@Override
	public void run() {

		PrivateChatWindow privateChatWindow = new PrivateChatWindow(addressForConnection, remotePort,
				panelForReceivedAndSend, clientOfChat, server, myServerPort, name, serverForPrivateChat);

	}

}
