package chat;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.print.attribute.standard.RequestingUserName;

public class ClientOfChat implements Runnable, KeyListener {

	private PanelForReceivedAndSend panelForReceivedAndSend;
	private String textForSend;
	
	private String ipToConnect;
	private String theLastIPConnection;
	
	private Thread thread;

	public ClientOfChat(PanelForReceivedAndSend panelForReceivedAndSend) {

		this.panelForReceivedAndSend = panelForReceivedAndSend;
		this.panelForReceivedAndSend.getFieldOfSendMessage().addKeyListener(this);
		
		InetAddress localIPtoConnect;
		
		try {
			localIPtoConnect = InetAddress.getLocalHost();
			runNewThreadOfClient(localIPtoConnect.getHostAddress());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	@Override
	public void run() {
		System.out.println("Client "+thread.getName());
		Socket socket = new Socket();
		InetSocketAddress inetSocketAddress = new InetSocketAddress(ipToConnect, 4999);
		try {			
			socket.connect(inetSocketAddress);
			theLastIPConnection = ipToConnect;
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		while (true) {

			try {
				
				PrintWriter sendMessage = new PrintWriter(socket.getOutputStream());
				if(textForSend != null) {
				sendMessage.println(textForSend);
				textForSend = null;
				}
				sendMessage.flush();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	
	public void runNewThreadOfClient(String ipToConnect) {
		
		this.ipToConnect = ipToConnect;
		thread = new Thread(this);
		thread.start();
	}
	
	public String getTheLastIPConnection() {
		
		return theLastIPConnection;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {

		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			panelForReceivedAndSend.getFieldOfSendMessage().setEditable(false);
			textForSend = panelForReceivedAndSend.getTextFromfieldOfSendMessage();
			panelForReceivedAndSend.getFieldOfSendMessage().setText("");
//			panelForReceivedAndSend.setTextInWindowChat(textForSend);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			
			panelForReceivedAndSend.getFieldOfSendMessage().setEditable(true);
			
		}

	}

}
