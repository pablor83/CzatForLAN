package chat;
import java.awt.Color;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

public class PanelForReceivedAndSend extends JPanel {
	
	private JTextArea windowOfReceivedMessages, fieldOfSendMessage;
	private JCheckBox checkBoxForSendingByTheEnter;
	
	

	public PanelForReceivedAndSend() {
		
		
		setLayout(null);
		
		windowOfReceivedMessages = new JTextArea();		
		JScrollPane scrollForReceivedMessage = new JScrollPane(windowOfReceivedMessages);
		
		fieldOfSendMessage = new JTextArea();
		JScrollPane scrollForSendMessage = new JScrollPane(fieldOfSendMessage);
		
		JLabel receivedLabel = new JLabel("Otrzymane wiadomoœci: ");
		receivedLabel.setBounds(20, 10, 150, 20);
		add(receivedLabel);
		
		JLabel sendLabel = new JLabel("Wyœlij: ");
		

		setBorder(BorderFactory.createLineBorder(Color.black, 1));
		
		TitledBorder borderForChat = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Chat");
		borderForChat.setTitleJustification(TitledBorder.CENTER);
		setBorder(borderForChat);

		
		addComponentListener(new ComponentAdapter() {
			
			public void componentResized(ComponentEvent e) {
								
				
				scrollForReceivedMessage.setBounds(20, 30, getWidth()-41, getHeight()-161);
				add(scrollForReceivedMessage);
				windowOfReceivedMessages.setLineWrap(true);
				
				sendLabel.setBounds(20, getHeight()-111, 100, 20);
				add(sendLabel);

				scrollForSendMessage.setBounds(20, getHeight()-86, getWidth()-41, 50);
				add(scrollForSendMessage);
				fieldOfSendMessage.setLineWrap(true);
				
				revalidate();
				repaint();
				
			}
		});

	}
	
	public synchronized void setTextInWindowChat(String text) {
		
		windowOfReceivedMessages.append(text);
		windowOfReceivedMessages.setCaretPosition(windowOfReceivedMessages.getDocument().getLength());
		
	}

}
