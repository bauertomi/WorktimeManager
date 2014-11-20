package hu.bme.mit.WorktimeManager.gui;

import hu.bme.mit.WorktimeManager.main.Record;
import hu.bme.mit.WorktimeManager.main.Storage;
import hu.bme.mit.WorktimeManager.main.Storage.StorageListener;

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
import javax.swing.table.TableModel;






//import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;

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
// TODO
				//kitöröl default!!!
			default:
				return Object.class;
			}
		}

		@Override
		public int getColumnCount() {
			// TODO Auto-generated method stub
			return 4;
		}

		@Override
		public String getColumnName(int arg0) {
			switch (arg0) {
			case 0:
				return "Time";
//TODO többi oszlop!!!
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
	  
	  //text area initialization
      panel = new JTextArea(2,3);

	  panel.setText("Itt lesznek a feldolgozott adatok.");
	  panel.setEditable(false);
	  
	  //initialization panel

	   pNorth = new JPanel();
       pCenter = new JPanel();

	   //add menu item to menu

	   m1.add(AddNew);
	   m1.add(Reset);
	   menu.add(m1);
	   
	   pCenter.setLayout(new BoxLayout(pMain,BoxLayout.Y_AXIS));
	   pCenter.setLayout(new GridLayout(1,1));
	   pCenter.add(panel);

	   pNorth.setBackground(Color.white);
	   pNorth.add(menu);
	   
	   
		  /*Vector<String> rowOne = new Vector<String>();
		    rowOne.addElement("Row1-Column1");
		    rowOne.addElement("Row1-Column2");
		    rowOne.addElement("Row1-Column3");
		    
		    Vector<String> rowTwo = new Vector<String>();
		    rowTwo.addElement("Row2-Column1");
		    rowTwo.addElement("Row2-Column2");
		    rowTwo.addElement("Row2-Column3");
		    
		    Vector<Vector<?>> rowData = new Vector<Vector<?>>();
		    rowData.addElement(rowOne);
		    rowData.addElement(rowTwo);
		    
		    Vector<String> columnNames = new Vector<String>();
		    columnNames.addElement("Column One");
		    columnNames.addElement("Column Two");
		    columnNames.addElement("Column Three");
		    
		    
		    
		    JTable table = new JTable(rowData, columnNames);*/
		    JTable table = new JTable(mTableModel);
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
