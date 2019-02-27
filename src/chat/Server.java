package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Server implements Runnable {

	private ServerSocket servSocket;

	private PanelForReceivedAndSend panelForReceivedAndSend;
	private ClientOfChat clientOfChat;

	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy'r.' HH:mm:ss");

	private Thread thread;
	private Date date;

	boolean sendMessage = true;

	Server(PanelForReceivedAndSend panelForReceivedAndSend, ClientOfChat clientOfChat) {

		this.panelForReceivedAndSend = panelForReceivedAndSend;
		this.clientOfChat = clientOfChat;

		try {
			servSocket = new ServerSocket(4999);
		} catch (IOException e) {
			// TODO Auto-generated catch block
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

		try {
			socket = servSocket.accept();
			remoteIPAddress = socket.getInetAddress();
			String ipRemote = remoteIPAddress.getHostAddress();

			InetAddress localIP = InetAddress.getLocalHost();

			if (!remoteIPAddress.getHostAddress().equals(localIP.getHostAddress())
					&& !remoteIPAddress.getHostAddress().equals(clientOfChat.getTheLastIPConnection())) {
				clientOfChat.runNewThreadOfClient(remoteIPAddress.getHostAddress());
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String name = remoteIPAddress.getHostName();

		date = new Date();
		panelForReceivedAndSend
				.setTextInWindowChat(name + "> " + "Po³¹czy³ siê" + "\n" + simpleDateFormat.format(date) + "\n\n");

		startNewServer();

		while (true) {

			try {
				InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
				BufferedReader bufferedStream = new BufferedReader(inputStreamReader);

				String text = bufferedStream.readLine();

				if (text.matches("/nick .+")) {
					String nameBeforeChange = name;

					name = text.substring(6);

					date = new Date();
					panelForReceivedAndSend.setTextInWindowChat(name + "> " + nameBeforeChange + " zmieni³ nick na "
							+ name + "\n" + simpleDateFormat.format(date) + "\n\n");

				} else if (getSendMessage()) {

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
				// TODO Auto-generated catch block
//				e.printStackTrace();

				panelForReceivedAndSend.setTextInWindowChat(
						name + "> " + "Roz³¹czy³ siê" + "\n" + simpleDateFormat.format(date) + "\n\n");
				clientOfChat.setIPToDisconnect(remoteIPAddress.getHostAddress());
				sendMessage = false;
				clientOfChat.setThreadsAsActive();
				try {
					socket.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				break;
			}

		}

	}

	synchronized public void setSendMessage(boolean sendMessage) {

		this.sendMessage = sendMessage;
	}

	synchronized public boolean getSendMessage() {

		return sendMessage;
	}

}
