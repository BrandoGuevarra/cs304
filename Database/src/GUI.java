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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.AbstractAction;

public class GUI implements TableModelListener {

	String[] TABLE_NAMES = {"PLAYER", "CHAMPION", "ITEM", "ITEM_UPGRADINTO_ITEM", "CHAMPION_SKILLS1", "CHAMPION_WIELD_ITEM",
			"PURCHASE_AND_UPGRADE", "REPORT_A_PLAYER"};
	Database db = new Database();
	JFrame frame;
	DefaultTableModel model = null;
	JTable table;
	String currentTableName;
	String[] currentEntries;
	String errorMessage;
	JMenu mnSelect = new JMenu("Select");
	JMenu mnView = new JMenu("View");
	JMenu mnReport = new JMenu("Report");
	JMenu mnChampion = new JMenu("Champion");
	JMenu mnDelete = new JMenu("Delete");
	JMenu mnUpdate = new JMenu("Update");
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
		menuBar.add(mnReport);
		menuBar.add(mnChampion);
		menuBar.add(mnDelete);
		menuBar.add(mnUpdate);

		generateViewTable();
		generateReportMenu();
		generateChampionMenu();
		generateDeleteMenu();
		generateUpdateMenu();
		
		table = new JTable(model);
		frame.getContentPane().add(table, BorderLayout.CENTER);
		frame.getContentPane().add(new JScrollPane(table));
		frame.getContentPane().add(lblT, BorderLayout.SOUTH);
	}
	
	//Add menu for each table in views
	private void generateViewTable() {
		for (int i = 0; i < TABLE_NAMES.length; i++) {
			final int index = i;
			JMenuItem mntm = new JMenuItem(TABLE_NAMES[i]);
			mntm.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String[][] data;
					try {
						//remove trailing null values 
					    List<String> list = new ArrayList<String>(Arrays.asList(db.getEntries(TABLE_NAMES[index])));
					    list.removeAll(Collections.singleton(null));
					    String[] entries = list.toArray(new String[list.size()]);					
					    data = db.select(entries, TABLE_NAMES[index], null);
						updateTable(data, entries, TABLE_NAMES[index]);
					} catch (SQLException e1) {
						setErrorMessage(e1.getMessage());
					}
					table.setEnabled(true);
				}
			});		
			mnView.add(mntm);			
		}
	}
	
	private void generateReportMenu() {
		JMenuItem mntmReport = new JMenuItem("Report");
		mnReport.add(mntmReport);
		JMenuItem mntmCheckReport = new JMenuItem("Check reports");
		mnReport.add(mntmCheckReport);
		JMenuItem mntmReportedByAll = new JMenuItem("Reported by all (DIVISION)");
		mnReport.add(mntmReportedByAll);
		JMenuItem mntmReportAVG = new JMenuItem("Average report per player");
		mnReport.add(mntmReportAVG);
		JMenuItem mntmReportedAVG = new JMenuItem("Average level of most reported");
		mnReport.add(mntmReportedAVG);
		
		mntmReport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

			}
		});	
		
		mntmCheckReport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String[] reportInputName = {"REPORTERID", "REPORTERREGION"};
				UserInput reportInput = new UserInput(2, reportInputName);
				reportInput.setVisible(true);
				
				reportInput.btnGo.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						String[][] data;
						String[] entry = {"REPORTID", "REPORTEEID", "REPORTEEREGION", "REPORTEDID", "REPORTEDREGION"};
						String query = "SELECT REPORTID, REPORTEEID, REPORTEEREGION, REPORTEDID, REPORTEDREGION FROM REPORT_A_PLAYER "
								+ "WHERE REPORTEEID= '" + reportInput.textField[0].getText() +"' AND REPORTEEREGION= '"
								+ reportInput.textField[1].getText() + "'";		
								 
						data = db.exactQuery(query, entry);
						updateTable(data, entry, "REPORT_A_PLAYER");			
						reportInput.dispose();
					}
				});	
			}
		});	
		
		mntmReportedByAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//						String[][] data;
//						String[] entry = {"REPORTID", "REPORTEEID", "REPORTEEREGION", "REPORTEDID", "REPORTEDREGION"};
//						String query = "SELECT REPORTID, REPORTEEID, REPORTEEREGION, REPORTEDID, REPORTEDREGION FROM REPORT_A_PLAYER "
//								+ "WHERE REPORTEEID= '" + reportInput.textField[0].getText() +"' AND REPORTEEREGION= '"
//								+ reportInput.textField[1].getText() + "'";		
//								 
//						data = db.exactQuery(query, entry);
//						updateTable(data, entry, null);			
			}
		});	
		
		mntmReportAVG.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String [][] data;
				String[] entry = {"AVG(TIMESREPORTED)"};
				String query = "SELECT avg(TIMESREPORTED) from PLAYER";
				data = db.exactQuery(query, entry);
				updateTable(data, entry, null);		
			}
		});	
		
		mntmReportedAVG.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String [][] data;
				String[] entry = {"AVG(PLAYERLEVEL)"};
				String query = "SELECT avg(PLAYERLEVEL) FROM (SELECT PLAYERLEVEL FROM PLAYER WHERE "
						+ "TIMESREPORTED = (SELECT MAX(TIMESREPORTED)FROM PLAYER))";
				data = db.exactQuery(query, entry);
				updateTable(data, entry, null);	
			}
		});	

	}
	
	private void generateChampionMenu() {
		JMenuItem mntmSkills = new JMenuItem("Skills");
		mnChampion.add(mntmSkills);
		JMenuItem mntmBought = new JMenuItem("Who bought (JOIN)");
		mnChampion.add(mntmBought);
		JMenuItem mntmMaxSkill = new JMenuItem("Max damage skill");
		mnChampion.add(mntmMaxSkill);
		JMenuItem mntmMostChampions = new JMenuItem("Players who purchased most champions");
		mnChampion.add(mntmMostChampions);


		mntmSkills.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String[] championInputName = {"PLAYERID", "PLAYERREGION", "CHAMPIONID"};
				UserInput championInput = new UserInput(3, championInputName);
				championInput.setVisible(true);
				
				championInput.btnGo.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						String[][] data;
//						String[] entry = {"REPORTID", "REPORTEEID", "REPORTEEREGION", "REPORTEDID", "REPORTEDREGION"};
//						String query = "SELECT REPORTID, REPORTEEID, REPORTEEREGION, REPORTEDID, REPORTEDREGION FROM REPORT_A_PLAYER "
//								+ "WHERE REPORTEEID= '" + reportInput.textField[0].getText() +"' AND REPORTEEREGION= '"
//								+ reportInput.textField[1].getText() + "'";		
//						SELECT s.Type, s.Description, s.Effect
//						FROM Player_Purchase_Champion pc, Champion_Skills1 s
//						WHERE pc.player_id = INPUT1 AND player_region = INPUT2 AND champion_id = INPUT3 AND s.championID = INPUT3
//						data = db.exactQuery(query, entry);
//						updateTable(data, entry, null);			
						championInput.dispose();
					}
				});	
			}
		});	
		
		mntmBought.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String[] championInputName = {"CHAMPIONID"};
				UserInput championInput = new UserInput(1, championInputName);
				championInput.setVisible(true);
				
				championInput.btnGo.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						String[][] data;
//						String[] entry = {"REPORTID", "REPORTEEID", "REPORTEEREGION", "REPORTEDID", "REPORTEDREGION"};
//						String query = "SELECT REPORTID, REPORTEEID, REPORTEEREGION, REPORTEDID, REPORTEDREGION FROM REPORT_A_PLAYER "
//								+ "WHERE REPORTEEID= '" + reportInput.textField[0].getText() +"' AND REPORTEEREGION= '"
//								+ reportInput.textField[1].getText() + "'";		
//						SELECT p.Username, p.Region, p.accountStatus, p.division
//						FROM Player p
//						JOIN Player_Purchase_Champion c
//						ON p.Username = c.playerID AND p.Region = c.playerRegion AND c.championID = INPUT1 
//						data = db.exactQuery(query, entry);
//						updateTable(data, entry, null);			
						championInput.dispose();
					}
				});	
			}
		});		
		
		mntmMaxSkill.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				String [][] data;
//				String[] entry = {"MAX(DAMAGE)"};
//				String query = "SELECT *, max(DAMAGE) from CHAMPION_SKILLS2";
//				data = db.exactQuery(query, entry);
//				updateTable(data, entry, null);		
			}
		});	
		
		mntmMostChampions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				String [][] data;
//				String[] entry = {"MAX(DAMAGE)"};
//				String query = "SELECT *, max(DAMAGE) from CHAMPION_SKILLS2";
//				data = db.exactQuery(query, entry);
//				updateTable(data, entry, null);		
			}
		});	
	}
	
	private void generateDeleteMenu() {
		JMenuItem mntmChampion = new JMenuItem("Champion (CASCADE)");
		mnDelete.add(mntmChampion);
		JMenuItem mntmSkill = new JMenuItem("Skill (NOT CASCADE)");
		mnDelete.add(mntmSkill);
		
		//CHECK integrity constraint ex: delete Ahri
		mntmChampion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String[] deleteInputName = {"CHAMPION NAME"};
				UserInput deleteInput = new UserInput(1, deleteInputName);
				deleteInput.setVisible(true);
				
				deleteInput.btnGo.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						String query = "DELETE FROM CHAMPION WHERE NAME= '" + deleteInput.textField[0].getText() + "'";
						if (db.deletion(query)) {
							lblT.setText("DELETE SUCCESFUL");
							lblT.setForeground(Color.blue);
						} else {
							lblT.setText("DELETE UNSUCCESFUL");
							lblT.setForeground(Color.red);
						}
						deleteInput.dispose();
					}
				});	
			}
		});	
		
		mntmSkill.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String[] deleteInputName = {"CHAMPION ID"};
				UserInput deleteInput = new UserInput(1, deleteInputName);
				deleteInput.setVisible(true);
				
				deleteInput.btnGo.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						String query = "DELETE FROM CHAMPION_SKILLS1 WHERE CHAMPIONID= '" + deleteInput.textField[0].getText() + "'";
						if (db.deletion(query)) {
							lblT.setText("DELETE SUCCESFUL");
							lblT.setForeground(Color.blue);
						} else {
							lblT.setText("DELETE UNSUCCESFUL");
							lblT.setForeground(Color.red);
						}
						deleteInput.dispose();
					}
				});	
			}
		});	
	}
	
	private void generateUpdateMenu() {
		JMenuItem mntmConstraint = new JMenuItem("Create level constraint");
		mnUpdate.add(mntmConstraint);
		JMenuItem mntmUpdate = new JMenuItem("Update level");
		mnUpdate.add(mntmUpdate);
		
//		mntmConstraint.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				String[] updateInputName = {"CHAMPION ID"};
//				UserInput updateInput = new UserInput(1, updateInputName);
//				updateInput.setVisible(true);
//				
//				updateInput.btnGo.addActionListener(new ActionListener() {
//					public void actionPerformed(ActionEvent e) {
//						String query = "DELETE FROM CHAMPION_SKILLS1 WHERE CHAMPIONID= '" + updateInput.textField[0].getText() + "'";
//						if (db.deletion(query)) {
//							lblT.setText("DELETE SUCCESFUL");
//							lblT.setForeground(Color.blue);
//						} else {
//							lblT.setText("DELETE UNSUCCESFUL");
//							lblT.setForeground(Color.red);
//						}
//						deleteInput.dispose();
//					}
//				});	
//			}
//		});
		
		mntmUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String[] updateInputName = {"LEVEL", "USERNAME", "REGION"};
				UserInput deleteInput = new UserInput(3, updateInputName);
				deleteInput.setVisible(true);
				
				deleteInput.btnGo.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						String query = "UPDATE PLAYER SET PLAYERLEVEL= '" + deleteInput.textField[0].getText() + "' WHERE USERNAME= '" 
								+ deleteInput.textField[1].getText() + "' AND REGION= '" + deleteInput.textField[2].getText() + "'";
						if (db.deletion(query)) {
							lblT.setText("UPDATE SUCCESFUL");
							lblT.setForeground(Color.blue);
						} else {
							lblT.setText("UPDATE UNSUCCESFUL");
							lblT.setForeground(Color.red);
						}
						deleteInput.dispose();
					}
				});	
			}
		});
		
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
						custom.dispose();
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