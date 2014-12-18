package hu.bme.mit.WorktimeManager.gui;

import hu.bme.mit.WorktimeManager.main.Record;
import hu.bme.mit.WorktimeManager.main.Storage;
import hu.bme.mit.WorktimeManager.main.Storage.StorageListener;
import hu.bme.mit.WorktimeManager.network.NetworkDiscover;
import hu.bme.mit.WorktimeManager.network.NetworkDiscover.NetworkDiscoverListener;

import java.sql.Savepoint;
import java.util.Date;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;














import sun.security.jca.GetInstance.Instance;







//import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class AppWindow extends JFrame implements StorageListener {

	private static final long serialVersionUID = 5985303282449765289L;
		
	private JMenuBar menu;
	private JMenu m1;
	private JMenuItem AddNew, Reset;
	private JPanel pMain,pNorth,pCenter;
	private JTextArea panel;
	private MyTableModel mTableModel = new MyTableModel();
	private Storage mStorage = Storage.getInstance();
	
	private class MyTableModel implements TableModel {

		@Override
		public void addTableModelListener(TableModelListener arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public Class<?> getColumnClass(int arg0) {
			switch (arg0) {
			case 0:
				return Date.class;
			case 1:
				return Object.class;
// TODO
				//kitöröl default!!!
			default:
				return Object.class;
			}
		}

		@Override
		public int getColumnCount() {
			return 3;
		}

		@Override
		public String getColumnName(int arg0) {
			switch (arg0) {
			case 0:
				return "Time";
			case 1:
				return "ID";
			case 2:
				return "State";
			default:
				return "default";
			}
		}

		@Override
		public int getRowCount() {
			// TODO Auto-generated method stub
			return 0;
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
			// TODO Auto-generated method stub
			
		}

		@Override
		public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
			Record record = mStorage.getRow(rowIndex);
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

	public AppWindow()
	{
		mStorage.registerStorageListener(this);
	  //menu bar and menu item initialization
	  menu = new JMenuBar();
	  m1 = new JMenu("Detect Server");
	  AddNew = new JMenuItem("Add New");
	  Reset = new JMenuItem("Reset");
  
	  //initialization panel

	   pNorth = new JPanel();
       pCenter = new JPanel();

	   //add menu item to menu

	   m1.add(AddNew);
	   m1.add(Reset);
	   menu.add(m1);
	   
	   //TODO
	   AddNew.addActionListener(new ActionListener() {
		   
           public void actionPerformed(ActionEvent e)
           {
               //Execute when button is pressed NETWORK DISCOVER
               try {
				Storage.saveStorage("probababa");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
           }
       });
	   Reset.addActionListener(null);
	   
	   pCenter.setLayout(new BoxLayout(pMain,BoxLayout.Y_AXIS));
	   pCenter.setLayout(new GridLayout(1,1));

	   pNorth.setBackground(Color.white);
	   pNorth.add(menu);    
		    
		    //JTable table = new JTable(rowData, columnNames);
		    JTable table = new JTable(mTableModel);
		    table.setModel(mTableModel);

		    JScrollPane scrollPane = new JScrollPane(table);
		    pCenter.add(scrollPane, BorderLayout.CENTER);
		    
	   this.getContentPane().add(pCenter,"Center");
	   this.getContentPane().add(pNorth,"North");
	   this.setTitle("Working time manager");
	   
	   
	   setDefaultCloseOperation(EXIT_ON_CLOSE);
	   }

	@Override
	public void onRowAdded(Record record) {
		// TODO Auto-generated method stub
		
	}
}
