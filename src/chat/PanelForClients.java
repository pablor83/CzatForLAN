package chat;

import java.awt.Color;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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

	public PanelForClients(PanelForReceivedAndSend panelForReceivedAndSend) {

		setLayout(null);
		setBorder(BorderFactory.createLineBorder(Color.black, 1));
		TitledBorder borderForClients = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
				"Przy³¹czeni klienci");
		borderForClients.setTitleJustification(TitledBorder.CENTER);
		setBorder(borderForClients);

		this.panelForReceivedAndSend = panelForReceivedAndSend;

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

//				revalidate();
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

	synchronized public void setNextNumberOfClient() {

		numberOfClient++;
	}

	synchronized public void setDecreaseClientNumber() {

		numberOfClient--;
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

	synchronized public int getNumberOfClient() {

		return numberOfClient;
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

	@Override
	synchronized public void mouseClicked(MouseEvent e) {

		if (e.getClickCount() == 2) {

			PanelForReceivedAndSend panelForReceivedAndSend = new PanelForReceivedAndSend();
			ClientForPrivateMessage clientForPrivateMessage = new ClientForPrivateMessage(
					listOfIPAddresses.get(table.getSelectedRow()), panelForReceivedAndSend);
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
