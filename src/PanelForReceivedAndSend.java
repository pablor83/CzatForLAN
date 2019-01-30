import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

public class PanelForReceivedAndSend extends JPanel{
	
	JTextArea windowOfReceivedMessages, fieldOfSendMessage;
	
	public PanelForReceivedAndSend() {
		
		setLayout(null);
		
		windowOfReceivedMessages = new JTextArea();
		windowOfReceivedMessages.setBounds(20, 25, 690, 330);
		
		fieldOfSendMessage = new JTextArea();
		fieldOfSendMessage.setBounds(20, 405, 690, 50);
		
		setBorder(BorderFactory.createLineBorder(Color.black, 1));
		add(windowOfReceivedMessages);
		add(fieldOfSendMessage);
		
		TitledBorder borderForChat = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Chat");
		borderForChat.setTitleJustification(TitledBorder.CENTER);
		setBorder(borderForChat);
		

	}

}
