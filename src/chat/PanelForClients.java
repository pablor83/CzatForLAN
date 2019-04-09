package chat;

import java.awt.Color;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

public class PanelForClients extends JPanel implements MouseListener {

	private DefaultTableModel tableModel;
	private JTable table;
	private Object[] nameOfClient;

	private int numberOfClient = 1;

	private ArrayList<String> listOfIPAddresses = new ArrayList<>();
	private PanelForReceivedAndSend panelForReceivedAndSend = null;
	private NotificationPanel notificationPanel;

	ServerForPrivateChat serverForPrivateChat;
	private int myServerPort = 5777;

	public PanelForClients(PanelForReceivedAndSend panelForReceivedAndSend, NotificationPanel notificationPanel,
			ServerForPrivateChat serverForPrivateChat) {

		setLayout(null);
		setBorder(BorderFactory.createLineBorder(Color.black, 1));
		TitledBorder borderForClients = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
				"Przy³¹czeni klienci");
		borderForClients.setTitleJustification(TitledBorder.CENTER);
		setBorder(borderForClients);

		this.panelForReceivedAndSend = panelForReceivedAndSend;
		this.notificationPanel = notificationPanel;
		this.serverForPrivateChat = serverForPrivateChat;

		tableModel = new DefaultTableModel() {

			public boolean isCellEditable(int rowIndex, int mColIndex) {

				return false;
			}
		};

		tableModel.addColumn("Connected clients");

		table = new JTable(tableModel);
		table.addMouseListener(this);
		table.setTableHeader(null);
		table.setRowHeight(30);
		table.setShowGrid(false);

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());

		addComponentListener(new ComponentAdapter() {

			public void componentResized(ComponentEvent e) {

				scrollPane.setBounds(6, 20, 190, getHeight() - 29);
				add(scrollPane);
			}
		});

	}

	synchronized public void addNameOfClient(Object nameOfClinet) {

		nameOfClient = new Object[1];
		nameOfClient[0] = nameOfClinet;
		tableModel.addRow(nameOfClient);
	}

	synchronized public void changeValueOfCell(Object newValue, int row, int column) {

		tableModel.setValueAt(newValue, row, column);
	}

	synchronized public void increaseMyServerPort() {

		myServerPort++;
	}

	synchronized public void removeClientPanelList(int i) {

		tableModel.removeRow(i);
	}

	synchronized public void addIPToList(String ip) {

		listOfIPAddresses.add(ip);
	}

	synchronized public void removeIPFromList(int i) {

		listOfIPAddresses.remove(i);
	}

	synchronized public String getIPFromList(Integer i) {

		return listOfIPAddresses.get(i);
	}

	synchronized public int getTheTablePosition(Object obj) {

		Object object = obj;
		int numberOfCell = 0;

		for (int i = 0; i < table.getRowCount(); i++) {

			if (obj.equals(table.getValueAt(i, 0))) {
				numberOfCell = i;
				break;
			}

		}

		return numberOfCell;
	}

	synchronized public int getMyPrivateClientServerPort() {

		return myServerPort;
	}

	synchronized public void checkPrivateServerPortAndPrivateClientPort() {

		while (true) {

			if (getMyPrivateClientServerPort() == serverForPrivateChat.getServerPort()) {

				increaseMyServerPort();
			} else
				break;
		}

	}

	public Object getNickFromPanel(String ip) {

		int i = 0;

		for (String ipAddress : listOfIPAddresses) {

			if (ipAddress.equals(ip)) {

				break;
			}

			i++;
		}

		return table.getValueAt(i, 0);
	}

	@Override
	public void mouseClicked(MouseEvent e) {

		if (e.getClickCount() == 2) {

			checkPrivateServerPortAndPrivateClientPort();
			InetAddress ip = null;

			try {
				ip = InetAddress.getByName(listOfIPAddresses.get(table.getSelectedRow()));

			} catch (UnknownHostException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			String addressForConnection = listOfIPAddresses.get(table.getSelectedRow());
			PanelForReceivedAndSend panelForReceivedAndSend = new PanelForReceivedAndSend();

			ClientForPrivateMessage clientForPrivateMessage = new ClientForPrivateMessage(addressForConnection,
					panelForReceivedAndSend, myServerPort, tableModel.getValueAt(table.getSelectedRow(), 0), 5111,
					serverForPrivateChat, false);

//			if (serverForPrivateChat.getPortForPrivateWindow(addressForConnection) == null) {				
//				
//				ClientForPrivateMessage clientForPrivateMessage = new ClientForPrivateMessage(addressForConnection,
//						panelForReceivedAndSend, myServerPort, tableModel.getValueAt(table.getSelectedRow(), 0), 5111, serverForPrivateChat, false);
//				
//				serverForPrivateChat.setIPAndPortForCheck(addressForConnection, clientForPrivateMessage.getRemotePort());
//				
//				
//				
//			} else {
//					
//				ClientForPrivateMessage clientForPrivateMessage = new ClientForPrivateMessage(addressForConnection,
//						panelForReceivedAndSend, myServerPort, tableModel.getValueAt(table.getSelectedRow(), 0), serverForPrivateChat.getPortForPrivateWindow(addressForConnection), serverForPrivateChat, true);
//				
//			}

			myServerPort++;
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}
