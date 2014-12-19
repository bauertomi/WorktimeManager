package hu.bme.mit.WorktimeManager.gui;

import hu.bme.mit.WorktimeManager.main.Message;
import hu.bme.mit.WorktimeManager.main.Record;
import hu.bme.mit.WorktimeManager.main.Storage;
import hu.bme.mit.WorktimeManager.main.Storage.StorageListener;

//import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import com.sun.jmx.snmp.Timestamp;

public class AppWindow extends JFrame implements StorageListener {

	private static final long serialVersionUID = 5985303282449765289L;

	private JMenuBar menu;
	private JMenu m1;
	private JMenuItem addNew, reset;
	private JPanel pMain, pNorth, pCenter;
	private MyTableModel mTableModel = new MyTableModel();
	private Storage mStorage = Storage.getInstance();

	private ArrayList<TableModelListener> listeners = new ArrayList<TableModelListener>();
	private ArrayList<Object[]> data = new ArrayList<Object[]>();

	private class MyTableModel implements TableModel {

		@Override
		public void addTableModelListener(TableModelListener arg0) {
			// TODO Auto-generated method stub
			if (listeners.contains(arg0))
				return;
			listeners.add(arg0);

		}

		@Override
		public Class<?> getColumnClass(int arg0) {
			switch (arg0) {
			case 0:
				return Timestamp.class;
			case 1:
				return Object.class;
			}
			return null;
		}

		@Override
		public int getColumnCount() {
			return 2;
		}

		@Override
		public String getColumnName(int arg0) {
			switch (arg0) {
			case 0:
				return "Time";
			case 1:
				return "ID";
			default:
				return "default";
			}
		}

		@Override
		public int getRowCount() {
			// TODO Auto-generated method stub
			return data.size();
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			Record row = mStorage.getRow(rowIndex);

			return row.getColumn(columnIndex);
		}

		@Override
		public boolean isCellEditable(int arg0, int arg1) {
			return false;
		}

		@Override
		public void removeTableModelListener(TableModelListener arg0) {
			listeners.remove(arg0);

		}

		@Override
		public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
			Record record = mStorage.getRow(rowIndex);
			//
			data.get(rowIndex)[columnIndex] = aValue;

			TableModelEvent valueAdded = new TableModelEvent(this, rowIndex,
					rowIndex, columnIndex, TableModelEvent.UPDATE);

			for (TableModelListener arg0 : listeners)
				arg0.tableChanged(valueAdded);
			//

			switch (columnIndex) {
			case 0:
				record.setID((String) aValue);
				break;
			case 1:
				record.setTimeStamp((Timestamp) aValue);

			default:
				break;
			}
		}

	}

	public AppWindow() {
		mStorage.registerStorageListener(this);
		// menu bar and menu item initialization
		menu = new JMenuBar();
		m1 = new JMenu("Detect Server");
		addNew = new JMenuItem("Add New");
		reset = new JMenuItem("Reset");

		Timestamp time = new Timestamp();

		Message message = new Message("1989");

		Record record = new Record(message, time);
		// initialization panel

		pNorth = new JPanel();
		pCenter = new JPanel();

		// add menu item to menu

		m1.add(addNew);
		m1.add(reset);
		menu.add(m1);

		// TODO
		addNew.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// Execute when button is pressed NETWORK DISCOVER
				try {
					Storage.saveStorage("probababa");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		reset.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// Execute when button is pressed NETWORK DISCOVER
				try {
					Storage.readStorage("storage.txt");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

		pCenter.setLayout(new BoxLayout(pMain, BoxLayout.Y_AXIS));
		pCenter.setLayout(new GridLayout(1, 1));

		pNorth.setBackground(Color.white);
		pNorth.add(menu);

		JTable table = new JTable(mTableModel);
		table.setModel(mTableModel);

		mStorage.addRow(record);

		JScrollPane scrollPane = new JScrollPane(table);
		pCenter.add(scrollPane, BorderLayout.CENTER);

		this.getContentPane().add(pCenter, "Center");
		this.getContentPane().add(pNorth, "North");
		this.setTitle("Working time manager");

		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	@Override
	public void onRowAdded(Record record) {
		Object[] obj = new Object[] { record };
		data.add(obj);
		TableModelEvent event = new TableModelEvent(mTableModel);
		for (TableModelListener l : listeners)
			l.tableChanged(event);
	}
}
