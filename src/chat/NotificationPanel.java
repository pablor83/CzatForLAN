package chat;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

public class NotificationPanel extends JPanel implements MouseListener {

	private DefaultTableModel tableModel;
	private JTable table;
	private ServerForPrivateChat serverForPrivateChat;

	public NotificationPanel() {

		setLayout(null);

		TitledBorder borderForButtons = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Powiadomienia");
		borderForButtons.setTitleJustification(TitledBorder.CENTER);
		setBorder(borderForButtons);

		tableModel = new DefaultTableModel() {

			@Override
			public boolean isCellEditable(int row, int column) {
				// TODO Auto-generated method stub
				return false;
			}
		};
		tableModel.addColumn("Przychodz¹ce powiadomienia");

		table = new JTable(tableModel);
		table.setTableHeader(null);
		table.setShowGrid(false);
		table.setRowHeight(40);
		table.setBackground(Color.green);
		table.addMouseListener(this);

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(5, 20, 190, 265);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		add(scrollPane);

	}

	synchronized public void setNotification(Object o) {

		Object[] object = { o };
		
		tableModel.addRow(object);
	}

	synchronized public void setPrivateServer(ServerForPrivateChat serverForPrivateChat) {

		this.serverForPrivateChat = serverForPrivateChat;
	}

	@Override
	synchronized public void mouseClicked(MouseEvent e) {

		serverForPrivateChat.openWindow(table.getValueAt(table.getSelectedRow(), 0));
		tableModel.removeRow(table.getSelectedRow());
		serverForPrivateChat.wakeUp();
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
