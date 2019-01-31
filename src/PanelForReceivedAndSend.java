import java.awt.Checkbox;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

public class PanelForReceivedAndSend extends JPanel {
	
	JTextArea windowOfReceivedMessages, fieldOfSendMessage;
	JCheckBox checkBoxForSendingByTheEnter;

	public PanelForReceivedAndSend() {

		setLayout(null);
		
		JLabel receivedLabel = new JLabel("Otrzymane wiadomo�ci:");
		receivedLabel.setBounds(20, 10, 140, 20);
		add(receivedLabel);
		
		JLabel sendLabel = new JLabel("Wy�lij:");
		sendLabel.setBounds(20, 380, 100, 20);
		add(sendLabel);		

		setBorder(BorderFactory.createLineBorder(Color.black, 1));
		
		checkBoxForSendingByTheEnter = new JCheckBox("Wysy�aj po naci�ni�ciu przycisku Enter");
		checkBoxForSendingByTheEnter.setBounds(450, 380, 250, 20);
		add(checkBoxForSendingByTheEnter);
		
		windowOfReceivedMessages = new JTextArea();
		windowOfReceivedMessages.setLineWrap(true);
		
		fieldOfSendMessage = new JTextArea();
		fieldOfSendMessage.setLineWrap(true);

		JScrollPane scrollForReceivedMessage = new JScrollPane(windowOfReceivedMessages);
		scrollForReceivedMessage.setBounds(20, 30, 690, 330);
		add(scrollForReceivedMessage);
		
		JScrollPane scrollForSendMessage = new JScrollPane(fieldOfSendMessage);
		scrollForSendMessage.setBounds(20, 405, 690, 50);
		add(scrollForSendMessage);

		TitledBorder borderForChat = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Chat");
		borderForChat.setTitleJustification(TitledBorder.CENTER);
		setBorder(borderForChat);

	}

}
