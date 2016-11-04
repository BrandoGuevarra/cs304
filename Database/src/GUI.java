import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import java.awt.BorderLayout;
import javax.swing.JLabel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractAction;

public class GUI {

	Database db = new Database();
	JFrame frame;
	DefaultTableModel model = null;
	JTable table;
	String tableName;
	JMenu mnSelect = new JMenu("Select");

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
		
		menuBar.add(mnSelect);
		
		JMenu mnView = new JMenu("View");
		menuBar.add(mnView);
		
		JMenuItem mntmA = new JMenuItem("Players");
		mntmA.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String[] entry = {"USERNAME", "REGION", "PASSWORD", "PLAYERLEVEL", "TIMESREPORTED", 
									"RIOTPOINTS", "IPPOINTS", "DIVISION", "ACCOUNTSTATUS"};
				String[][] data = db.select(entry, "PLAYER", null);
				updateTable(data, entry, "PLAYER");
			}
		});
		mnView.add(mntmA);
		
		JMenuItem mntmB = new JMenuItem("Champions");
		mntmB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String[] entry = {"NAME", "CHAMPIONLEVEL", "IPCOST", "ROLE", "PASSIVESKILL", 
									"FACTION", "SKILLPOINTS"};
				String[][] data = db.select(entry, "CHAMPION", null);
				updateTable(data, entry, "CHAMPION");
			}
		});		
		mnView.add(mntmB);

		table = new JTable(model);
		table.setEnabled(false);
		frame.getContentPane().add(table, BorderLayout.CENTER);
		frame.getContentPane().add(new JScrollPane(table));
		
		JLabel lblT = new JLabel("Messages");
		frame.getContentPane().add(lblT, BorderLayout.SOUTH);
	}
	
	private void updateTable(String[][] data, String[] entry, String tableName) {
		model = new DefaultTableModel(data, entry);
		table.setModel(model);
		this.tableName = tableName;		
		getSelection();
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

						String[][] data = db.select(entry, tableName, customQuery);
						updateTable(data, entry, tableName);			
						custom.setVisible(false);
					}
				});		
			} else {
				String[] entry = {this.entry};
				String[][] data = db.select(entry, tableName, null);
				updateTable(data, entry, tableName);
			}
		}
	}
}