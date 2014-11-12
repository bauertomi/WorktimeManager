package hu.bme.mit.WorktimeManager.gui;

import javax.swing.*;
//import javax.swing.table.DefaultTableModel;

import java.awt.*;

public class AppWindow extends JFrame {

	private static final long serialVersionUID = 5985303282449765289L;
		
	private JMenuBar menu;
	private JMenu m1;
	private JMenuItem AddNew, Reset;
	private JPanel pMain,pNorth,pCenter;
	private JTextArea panel;

	public AppWindow()
	{
	  //menu bar and menu item initialization
	  menu = new JMenuBar();
	  m1 = new JMenu("Detect Server");
	  AddNew = new JMenuItem("Add New");
	  Reset = new JMenuItem("Reset");
	  
	  //text area initialization
      panel = new JTextArea(2,3);

	  panel.setText("Itt lesznek a feldolgozott adatok.");
	  panel.setEditable(false);
	  
	  //JTable table = new JTable(new DefaultTableModel(null, new Object[]{"Column1", "Column2"}));
	  

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
	   
	   setDefaultCloseOperation(EXIT_ON_CLOSE);
	   
	   this.getContentPane().add(pCenter,"Center");
	   this.getContentPane().add(pNorth,"North");
	   this.setTitle("Working time manager");
	   }
}
