package chat;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ClientUDP {

	private DatagramSocket datagramSocket;
	private DatagramPacket datagramPacket;
	private InetAddress inetAddress;
	private InetAddress takeLocalIP;
	private byte[] bufor = new byte[256];

	public ClientUDP() {

		try {
			datagramSocket = new DatagramSocket();
			inetAddress = InetAddress.getByName("230.0.0.0");
			takeLocalIP = InetAddress.getLocalHost();
			bufor = takeLocalIP.getHostAddress().getBytes();
			datagramPacket = new DatagramPacket(bufor, bufor.length, inetAddress, 4111);
			datagramSocket.send(datagramPacket);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		datagramSocket.close();
	}

}
