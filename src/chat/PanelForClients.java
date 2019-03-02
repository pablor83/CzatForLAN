package chat;

import java.awt.Color;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

public class PanelForClients extends JPanel {

	private DefaultTableModel tableModel;
	private JTable table;
	private Object[] nameOfClient;
	private int numberOfClient = 1;

	public PanelForClients() {

		setLayout(null);
		setBorder(BorderFactory.createLineBorder(Color.black, 1));
		TitledBorder borderForClients = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
				"Przy³¹czeni klienci");
		borderForClients.setTitleJustification(TitledBorder.CENTER);
		setBorder(borderForClients);

		tableModel = new DefaultTableModel() {

			public boolean isCellEditable(int rowIndex, int mColIndex) {
				
				return false;
			}
		};		
				
		tableModel.addColumn("Connected clients");

		table = new JTable(tableModel);
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
	
	synchronized public void removeClientFromList(int i) {
		
		tableModel.removeRow(i-1);
	}
	
	synchronized public int getNumberOfClient() {
		
		return numberOfClient;
	}
	
	synchronized public int getTheTablePosition(Object obj) {
		
		Object object = obj;
		int numberOfCell = 0;
		
		for(int i = 0; i < table.getRowCount(); i++) {
			
			if(object.equals(table.getValueAt(i, 0))) {
				numberOfCell = i;
				break;
			}
				
		}
		
		return numberOfCell;
	}

}
