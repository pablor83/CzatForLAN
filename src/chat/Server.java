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
	private InetAddress inetAddress;

	private PanelForReceivedAndSend panelForReceivedAndSend;

	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

	private Thread thread;
	private Date date;

	Server(PanelForReceivedAndSend panelForReceivedAndSend) {

		this.panelForReceivedAndSend = panelForReceivedAndSend;

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

		try {
			socket = servSocket.accept();
			inetAddress = socket.getInetAddress();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Po³¹czono");
		String name = inetAddress.getHostName();

		startNewServer();

		while (true) {

			try {
				InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
				BufferedReader bufferedStream = new BufferedReader(inputStreamReader);

				String text = bufferedStream.readLine();

				if (text == null) {

					panelForReceivedAndSend.setTextInWindowChat(
							name + "> " + "Roz³¹czy³ siê" + "\n" + simpleDateFormat.format(date) + "\n\n");
					socket.close();
					break;

				}

				if (text.matches("/nick .+")) {
					String nameBeforeChange = name;

					name = text.substring(6);

					date = new Date();
					panelForReceivedAndSend.setTextInWindowChat(name + "> " + nameBeforeChange + " zmieni³ nick na "
							+ name + "\n" + simpleDateFormat.format(date) + "\n\n");

				} else {
					date = new Date();
					panelForReceivedAndSend
							.setTextInWindowChat(name + "> " + text + "\n" + simpleDateFormat.format(date) + "\n\n");
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
//				e.printStackTrace();

				panelForReceivedAndSend.setTextInWindowChat(
						name + "> " + "Roz³¹czy³ siê" + "\n" + simpleDateFormat.format(date) + "\n\n");
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

}
