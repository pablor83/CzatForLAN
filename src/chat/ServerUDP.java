package chat;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class ServerUDP implements Runnable {

	private MulticastSocket multicastSocket;
	private InetAddress inetAddress;
	private DatagramPacket datagramPacket;
	private byte[] bufor = new byte[256];
	private String takeIP;
	private ClientOfChat clientOfChat;

	public ServerUDP(ClientOfChat clientOfChat) {

		this.clientOfChat = clientOfChat;

		Thread thread = new Thread(this);
		thread.start();
	}

	@Override
	public void run() {

		try {
			multicastSocket = new MulticastSocket(4111);
			inetAddress = InetAddress.getByName("230.0.0.0");
			multicastSocket.joinGroup(inetAddress);
			
			InetAddress localIP = InetAddress.getLocalHost();

			while (true) {

				datagramPacket = new DatagramPacket(bufor, bufor.length);

				multicastSocket.receive(datagramPacket);

				takeIP = new String(bufor).trim();
				
				if (takeIP != null && !takeIP.equals(localIP.getHostAddress())) {
					clientOfChat.runNewThreadOfClient(takeIP);
					System.out.println("��cz� "+takeIP);
				}
				takeIP = null;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

//	public static void main(String[] args) {
//		
//		ServerUDP serverUDP = new ServerUDP();
//		
//		try {
//			serverUDP.multicastSocket = new MulticastSocket(4111);
//			serverUDP.inetAddress = InetAddress.getByName("230.0.0.0");
//			serverUDP.multicastSocket.joinGroup(serverUDP.inetAddress);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} System.out.println("Server UPD");
//		
//		while(true) {
//			
//			serverUDP.datagramPacket = new DatagramPacket(serverUDP.bufor, serverUDP.bufor.length);
//			try {
//				serverUDP.multicastSocket.receive(serverUDP.datagramPacket);
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//			serverUDP.takeIP = new String(serverUDP.bufor);
//			System.out.println(serverUDP.takeIP);
//			
//		}
//		
//
//	}

}
