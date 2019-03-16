package chat;

public class InitiatorOfPrivateConversation {

	int portServer = 5777;

	String ipForConnection = "10.0.0.2";
	int portForConnection = 5332;

	public InitiatorOfPrivateConversation() {

		PanelForReceivedAndSend panelForReceivedAndSend = new PanelForReceivedAndSend();

//		ClientForPrivateMessage clientForPrivateMessage = new ClientForPrivateMessage(ipForConnection, panelForReceivedAndSend,portServer, portServer);

		portServer++;
	}

	public static void main(String[] args) {

		new InitiatorOfPrivateConversation();

	}

}
