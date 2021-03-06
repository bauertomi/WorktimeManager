package hu.bme.mit.WorktimeManager.gui;

import hu.bme.mit.WorktimeManager.main.Message;
import hu.bme.mit.WorktimeManager.main.Record;
import hu.bme.mit.WorktimeManager.main.Storage;
import hu.bme.mit.WorktimeManager.main.Storage.StorageListener;
import hu.bme.mit.WorktimeManager.network.NetworkClient;
import hu.bme.mit.WorktimeManager.network.NetworkClient.NetworkClientListener;
import hu.bme.mit.WorktimeManager.network.NetworkDiscover;
import hu.bme.mit.WorktimeManager.network.NetworkDiscover.NetworkDiscoverListener;

//import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import com.sun.jmx.snmp.Timestamp;

public class AppWindow extends JFrame implements StorageListener,
		NetworkDiscoverListener, NetworkClientListener {

	private static final long serialVersionUID = 5985303282449765289L;
	protected static final Font mStandardFont = new Font("Serif", Font.PLAIN,
			20);

	private JPanel pNorth, pCenter;
	private MyTableModel mTableModel = new MyTableModel();
	private Storage mStorage = Storage.getInstance();

	private ArrayList<TableModelListener> listeners = new ArrayList<TableModelListener>();
	private DefaultListModel<String> mAddresses;
	private JList<String> mAddressList;
	private NetworkClient mNetworkClient = new NetworkClient(this);

	private ArrayList<Object[]> data = new ArrayList<Object[]>();
	private NetworkDiscover networkListener = new NetworkDiscover(this);

	private class MyTableModel implements TableModel {

		@Override
		public void addTableModelListener(TableModelListener arg0) {
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
				record.setTimeStamp((Date) aValue);

			default:
				break;
			}
		}

	}

	public AppWindow() {

		mStorage.registerStorageListener(this);

		Message message = new Message("startup");

		Record record = new Record(message, Calendar.getInstance().getTime());

		mAddresses = new DefaultListModel<>();
		mAddressList = new JList<>(mAddresses);
		mAddressList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		mAddressList.setVisibleRowCount(5);
		mAddressList.setFont(mStandardFont);
		mAddressList.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				final String address = mAddressList.getSelectedValue();
				// PLACEHOLDER);
			}
		});

		// initialization panel

		pNorth = new JPanel();
		pCenter = new JPanel();

		// pCenter.setLayout(new BoxLayout(pMain, BoxLayout.Y_AXIS));
		// pCenter.setLayout(new GridLayout(1, 1));

		pNorth.setBackground(Color.white);

		JTable table = new JTable(mTableModel);
		table.setModel(mTableModel);

		JScrollPane scrollPane = new JScrollPane(table);
		pCenter.add(scrollPane, BorderLayout.CENTER);
		pCenter.add(mAddressList);

		this.getContentPane().add(pCenter, "Center");
		// this.getContentPane().add(pNorth, "North");
		this.setTitle("Working time manager");

		networkListener.startListening();

		try {
			mStorage.readStorage();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// mStorage.addRow(record);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	@Override
	public void onRowAdded(Record record) {
		Object[] obj = new Object[] { record };
		data.add(obj);
		TableModelEvent event = new TableModelEvent(mTableModel);
		for (TableModelListener l : listeners) {
			l.tableChanged(event);
		}
	}

	@Override
	public void onDiscover(InetAddress address, NetworkDiscover networkDiscover) {
		String IP = address.toString().substring(1);

		if (!mAddresses.contains(IP))
			mAddresses.addElement(IP);
		try {
			mNetworkClient.connect(address);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onDiscoveredTimeout(InetAddress address) {
		// TODO Auto-generated method stub
		// networkListener.stopListening();
		// mAddresses.removeElement(address.toString().substring(1));

	}

	@Override
	public void onReceive(String data) {
		Message message = new Message(data);
		Record record = new Record(message, Calendar.getInstance().getTime());

		mStorage.addRow(record);
	}

	@Override
	public void onConnect() {
		// TODO Auto-generated method stub
		mAddresses.addElement("Connected");

	}

	@Override
	public void onDisconnect() {
		// TODO Auto-generated method stub
		mAddresses.addElement("Disconnected");

	}
}
