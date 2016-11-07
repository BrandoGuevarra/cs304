import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JLabel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.AbstractAction;

public class GUI implements TableModelListener {

	String[] PLAYER_ENTRY = {"USERNAME", "REGION", "PASSWORD", "PLAYERLEVEL", "TIMESREPORTED", 
			"RIOTPOINTS", "IPPOINTS", "DIVISION", "ACCOUNTSTATUS"};
	String[] CHAMPION_ENTRY = {"NAME", "CHAMPIONLEVEL", "IPCOST", "ROLE", "PASSIVESKILL", 
			"FACTION", "SKILLPOINTS"};
	Database db = new Database();
	JFrame frame;
	DefaultTableModel model = null;
	JTable table;
	String currentTableName;
	String[] currentEntries;
	String errorMessage;
	JMenu mnSelect = new JMenu("Select");
	JMenu mnView = new JMenu("View");
	JLabel lblT = new JLabel("Messages");

	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public GUI() {
		initialize();
	}

	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 800, 550);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		menuBar.add(mnView);
		menuBar.add(mnSelect);
		
		JMenuItem mntmA = new JMenuItem("Players");
		mntmA.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String[][] data;
				try {
					data = db.select(PLAYER_ENTRY, "PLAYER", null);
					updateTable(data, PLAYER_ENTRY, "PLAYER");
				} catch (SQLException e1) {
					setErrorMessage(e1.getMessage());
				}
				table.setEnabled(true);
			}
		});
		mnView.add(mntmA);
		
		JMenuItem mntmB = new JMenuItem("Champions");
		mntmB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String[][] data;
				try {
					data = db.select(CHAMPION_ENTRY, "CHAMPION", null);
					updateTable(data, CHAMPION_ENTRY, "CHAMPION");
				} catch (SQLException e1) {
					setErrorMessage(e1.getMessage());
				}
				table.setEnabled(true);
			}
		});		
		mnView.add(mntmB);
		
		
		table = new JTable(model);
		frame.getContentPane().add(table, BorderLayout.CENTER);
		frame.getContentPane().add(new JScrollPane(table));
		frame.getContentPane().add(lblT, BorderLayout.SOUTH);
	}
	
	private void setErrorMessage(String error) {
		errorMessage = error;
    	lblT.setText(errorMessage);
    	lblT.setForeground(Color.red);
	}
	
	private void updateTable(String[][] data, String[] entry, String tableName) {
		model = new DefaultTableModel(data, entry);
		table.setModel(model);
		this.currentTableName = tableName;		
		this.currentEntries = entry;
		getSelection();
		table.getModel().addTableModelListener((TableModelListener) this);
	    lblT.setText(tableName);
	    lblT.setForeground(Color.blue);	
		table.setEnabled(false);
	}
	
	//an entry in the table is changed
	public void tableChanged(TableModelEvent e) {
        int row = e.getFirstRow();
        int column = e.getColumn();
        TableModel model = (TableModel)e.getSource();
        String columnName = model.getColumnName(column);
        Object data = model.getValueAt(row, column);
        String primaryKey = db.getPrimaryKeyName(this.currentTableName);
        String pkValue;
        
        //select query to get old values of database 
	    Object[][] oldData;
	    DefaultTableModel oldModel = null;
		try {
			oldData = db.select(this.currentEntries, this.currentTableName, null);
		    oldModel = new DefaultTableModel(oldData, this.currentTableName.split(""));

		} catch (SQLException e2) {
			e2.printStackTrace();
		}
	    
        //get primary key value at selected row
        for (column = 0; !model.getColumnName(column).equals(primaryKey); column++);
        pkValue = (String) oldModel.getValueAt(row, column);
        
        try {
			if (db.update(this.currentTableName, columnName, data, primaryKey, pkValue)) {
				String[][] newData = db.select(this.currentEntries, this.currentTableName, null);
				updateTable(newData, this.currentEntries, this.currentTableName);
				table.setEnabled(true);
				lblT.setText("UPDATE SUCCESFUL");
				lblT.setForeground(Color.blue);
			} else if (pkValue == null) {
				lblT.setText("UPDATE UNSUCCESFUL Not a valid row");
				lblT.setForeground(Color.red);
			}
		} catch (SQLException e1) {
			setErrorMessage(e1.getMessage());
			lblT.setText("UPDATE UNSUCCESFUL " + errorMessage);
		}

	}
	
	//Updates the menu to get current table columns
	private void getSelection() {
		mnSelect.removeAll();
		JMenuItem custom = new JMenuItem("Custom");
		mnSelect.add(custom);
		custom.setAction(new SwingAction("Custom", "custom", null));

	 	for (int i = 0; i < model.getColumnCount(); i++) {
	 		JMenu menus = new JMenu(table.getColumnName(i));
	 		mnSelect.add(menus);
	 		
	 		JMenuItem selectAll = new JMenuItem("All");
	 		menus.add(selectAll);	 		
	 		selectAll.setAction(new SwingAction("All", table.getColumnName(i), null));
	 		
	 		JMenuItem selectCustom = new JMenuItem("Custom");
	 		menus.add(selectCustom);	 		
	 		selectCustom.setAction(new SwingAction("Custom", "custom", table.getColumnName(i)));
	 	}
	}
	
	@SuppressWarnings("serial")
	private class SwingAction extends AbstractAction {
		String entry;
		String header;
		public SwingAction(String actionName, String entry, String header) {
			putValue(NAME, actionName);
			this.entry = entry;
			this.header = header;
		}
		//menu from select is pressed to get whole column or to specify the selection
		public void actionPerformed(ActionEvent e) {
			if (entry.equals("custom")) {
				Custom custom = new Custom();
				custom.textField.setText(this.header);
				custom.textField_1.setText(this.header);
				custom.setVisible(true);
				
				custom.btnNewButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						String customQuery = custom.textField_1.getText().toUpperCase() + " " + custom.buttonGroup.getSelection().getActionCommand() + 
						" '" + custom.textField_2.getText() + "'";
						String[] entry = custom.textField.getText().split(",");

						String[][] data;
						try {
							data = db.select(entry, currentTableName, customQuery);
							updateTable(data, entry, currentTableName);			
						} catch (SQLException e1) {
							setErrorMessage(e1.getMessage());
						}
						custom.setVisible(false);
					}
				});		
			} else {
				String[] entry = {this.entry};
				String[][] data;
				try {
					data = db.select(entry, currentTableName, null);
					updateTable(data, entry, currentTableName);
				} catch (SQLException e1) {
					setErrorMessage(e1.getMessage());
				}
			}
		}
	}
}