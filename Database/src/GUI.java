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
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.AbstractAction;

public class GUI implements TableModelListener {

	String[] TABLE_NAMES = {"PLAYER", "CHAMPION", "ITEM", "ITEM_UPGRADEINTO_ITEM", "CHAMPION_SKILLS1", "CHAMPION_SKILLS2",
			"CHAMPION_WIELD_ITEM", "CHAMPION_STATS1", "CHAMPION_STATS2", "PLAYER_PURCHASE_CHAMPION",
			"PURCHASE_AND_UPGRADE", "REPORT_A_PLAYER"};
	static Database db = new Database();
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
	JMenu mnAdmin = new JMenu("Admin");
	JMenu mnAccount = new JMenu("Account");
	JLabel lblT = new JLabel("Messages");
	static String accountStatus = "offline";
	static String username;
	static String region;


	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					login();
//					GUI window = new GUI();
//					window.frame.setVisible(true);
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
		frame.setBounds(100, 100, 1000, 550);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		menuBar.add(mnView);
		menuBar.add(mnSelect);
		menuBar.add(mnReport);
		menuBar.add(mnChampion);


		generateViewTable();
		generateReportMenu();
		generateChampionMenu();

		if (accountStatus.equals("admin")) {
			menuBar.add(mnAdmin);
			menuBar.add(mnDelete);
			menuBar.add(mnUpdate);
			generateAdminMenu();
			generateDeleteMenu();
			generateUpdateMenu();
		}
		
		if (accountStatus.equals("online")) {
			menuBar.add(mnAccount);
			generateAccountMenu();
		}
		table = new JTable(model) {
			public String getToolTipText(MouseEvent e) {
				String tip = null;
				 java.awt.Point p = e.getPoint();
	             int row = rowAtPoint(p);
	             int col = columnAtPoint(p);
	             tip = (String) model.getValueAt(row, col);
				return tip;
			}
		};
		frame.getContentPane().add(table, BorderLayout.CENTER);
		frame.getContentPane().add(new JScrollPane(table));
		frame.getContentPane().add(lblT, BorderLayout.SOUTH);
	}
	
	//Add menu for each table in views
	private void generateViewTable() {
		for (int i = 0; i < TABLE_NAMES.length; i++) {
			final int index = i;
			//Skip any admin only VIEW menu options
			if (!(accountStatus.equals("online") && (TABLE_NAMES[i].equals("PLAYER") || TABLE_NAMES[i].equals("REPORT_A_PLAYER")))) {

				JMenuItem mntm = new JMenuItem(TABLE_NAMES[i]);
				mntm.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						String[][] data;
						try {
						    String[] entries = db.getEntries(TABLE_NAMES[index]);			
						    data = db.select(entries, TABLE_NAMES[index], null);
							updateTable(data, entries, TABLE_NAMES[index]);
						} catch (SQLException e1) {
	//						setErrorMessage(e1.getMessage());
							lblT.setText("Search not valid");
						}
						if (accountStatus.equals("admin")) {
							table.setEnabled(true);
						}
					}
				});		
				mnView.add(mntm);	
			}
		}
	}
	
	private void generateReportMenu() {

		JMenuItem mntmCheckReport = new JMenuItem("Check reports");
		mnReport.add(mntmCheckReport);
		
		JMenuItem mntmReportedByAll = new JMenuItem("Reported by all (DIVISION)");
		mnReport.add(mntmReportedByAll);
		
		JMenuItem mntmReportAVG = new JMenuItem("Average report per player");
		mnReport.add(mntmReportAVG);
		
		JMenuItem mntmReportedAVG = new JMenuItem("Average level of most reported");
		mnReport.add(mntmReportedAVG);
		mnReport.setToolTipText("Of the players with the most reports what is their average level");
				
		if (accountStatus.equals("online")) {
			JMenuItem mntmReport = new JMenuItem("Report");
			mnReport.add(mntmReport);
		
			mntmReport.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String[] reportInputName = {"REPORTEEID", "REPORTEEREGION", "OFFENDINGACTION"};
					final UserInput reportInput = new UserInput(reportInputName);
					reportInput.setVisible(true);
					
					reportInput.btnGo.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							DateFormat df = new SimpleDateFormat("HHddMMyy");
							Date dateobj = new Date();
							String update = "INSERT INTO REPORT_A_PLAYER VALUES ('" + (db.getRowCount("REPORT_A_PLAYER") + 1)
								+ "', '" + reportInput.textField[0].getText() + "', '" + reportInput.textField[1].getText() + "', '" + username + "', '" 
								+ region + "', '" + df.format(dateobj) + "', '"  
								+ reportInput.textField[2].getText() + "')";		
									 
							System.out.print(update);
							if (db.deletion(update)) {
								db.deletion("UPDATE PLAYER SET TIMESREPORTED = TIMESREPORTED + 1 WHERE USERNAME = '"
										+ reportInput.textField[0].getText() + "' AND REGION = '"
										+ reportInput.textField[1].getText() + "'");
								lblT.setText("REPORT SUCCESFUL");
								lblT.setForeground(Color.blue);
							} else {
								lblT.setText("REPORT UNSUCCESFUL");
								lblT.setForeground(Color.red);
							}
							reportInput.dispose();
						}
					});	
				}
			});	
		}
		
		mntmCheckReport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String[] reportInputName = {"REPORTERID", "REPORTERREGION"};
				UserInput reportInput = new UserInput(reportInputName);
				reportInput.textField[0].setText(username);
				reportInput.textField[1].setText(region);
				reportInput.setVisible(true);
					
				reportInput.btnGo.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						String[][] data;
						String[] entry = {"REPORTID", "REPORTEEID", "REPORTEEREGION", "REPORTERID", "REPORTERREGION", "REPORTEDTIMEDAYMONTHYEAR", "OFFENDINGACTION"};
						String query = "SELECT REPORTID, REPORTEEID, REPORTEEREGION, REPORTERID, REPORTERREGION, REPORTEDTIMEDAYMONTHYEAR, OFFENDINGACTION FROM REPORT_A_PLAYER "
								+ "WHERE REPORTERID= '" + reportInput.textField[0].getText() +"' AND REPORTERREGION= '"
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
					String[][] data;
					String[] entry = {"USERNAME", "REGION", "PASSWORD", "PLAYERLEVEL", "TIMESREPORTED", "RIOTPOINTS"
							, "IPPOINTS", "DIVISION", "ACCOUNTSTATUS"};
					String query = "SELECT * FROM PLAYER p WHERE NOT EXISTS(SELECT p1.USERNAME, p1.REGION FROM PLAYER p1"
							+ " where p1.USERNAME != p.USERNAME"
							+ " MINUS SELECT r.REPORTERID, r.REPORTERREGION FROM REPORT_A_PLAYER r "
							+ "WHERE r.REPORTEEID = p.USERNAME AND r.REPORTEEREGION = p.REGION)";		
								 
					data = db.exactQuery(query, entry);
					updateTable(data, entry, null);			
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
		JMenuItem mntmPurchase = new JMenuItem("Purchase");
		mnChampion.add(mntmPurchase);
		
		JMenuItem mntmSkills = new JMenuItem("Skills");
		mnChampion.add(mntmSkills);
		
		JMenuItem mntmBought = new JMenuItem("Who bought (JOIN)");
		mnChampion.add(mntmBought);
		mntmBought.setToolTipText("List all the players who bought a certain champion");

		JMenuItem mntmMaxSkill = new JMenuItem("Max damage skill");
		mnChampion.add(mntmMaxSkill);
		mntmMaxSkill.setToolTipText("The highest damage skill");
		
		JMenuItem mntmMostChampions = new JMenuItem("Players who purchased most champions");
		mnChampion.add(mntmMostChampions);
		
		mntmPurchase.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String[] championInputName = {"CHAMPION NAME"};
				UserInput championInput = new UserInput(championInputName);
				championInput.setVisible(true);
				
				championInput.btnGo.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						String cost = db.getValue("CHAMPION", "NAME", championInput.textField[0].getText(), "IPCOST");
						String insert = "INSERT INTO PLAYER_PURCHASE_CHAMPION VALUES ('" + username + "', '" + region
								+ "', '" + championInput.textField[0].getText() + "', '" + cost + "', 'IP')";
						System.out.println(insert);
						if (db.deletion(insert)) {
							lblT.setText("PURCHASE SUCCESFUL");
							lblT.setForeground(Color.blue);
						} else {
							lblT.setText("PURCHASE UNSUCCESFUL");
							lblT.setForeground(Color.red);
						}
						championInput.dispose();

					}
				});	
			}
		});

		mntmSkills.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String[] championInputName = {"PLAYERID", "PLAYERREGION", "CHAMPIONID"};
				UserInput championInput = new UserInput(championInputName);
				championInput.textField[0].setText(username);
				championInput.textField[1].setText(region);

				championInput.setVisible(true);
				
				championInput.btnGo.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						String[][] data;
						String[] entry = {"TYPE", "DESCRIPTION", "EFFECT"};
						String query = "SELECT s.TYPE, s.DESCRIPTION, s.EFFECT FROM PLAYER_PURCHASE_CHAMPION pc, CHAMPION_SKILLS1 s "
								+ "WHERE pc.PLAYERID= '" + championInput.textField[0].getText() + "' AND pc.PLAYERREGION= '"
								+ championInput.textField[1].getText() + "' AND pc.CHAMPIONID= '" + championInput.textField[2].getText()
								+ "' AND s.CHAMPIONID= '" + championInput.textField[2].getText() + "'";	

						data = db.exactQuery(query, entry);
						updateTable(data, entry, null);			
						championInput.dispose();

					}
				});	
			}
		});	
		
		mntmBought.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String[] championInputName = {"CHAMPIONID"};
				UserInput championInput = new UserInput(championInputName);
				championInput.setVisible(true);
				
				championInput.btnGo.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						String[][] data;
						String[] entry = {"USERNAME", "REGION", "ACCOUNTSTATUS", "DIVISION"};
						String query = "SELECT p.USERNAME, p.REGION, p.ACCOUNTSTATUS, p.DIVISION FROM PLAYER p "
								+ "JOIN PLAYER_PURCHASE_CHAMPION c ON p.USERNAME= c.PLAYERID AND p.REGION = c.PLAYERREGION AND"
								+ " c.CHAMPIONID= '" + championInput.textField[0].getText() + "'"; 
						data = db.exactQuery(query, entry);
						updateTable(data, entry, null);			
						championInput.dispose();
					}
				});	
			}
		});		
		
		mntmMaxSkill.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String [][] data;
				String[] entry = {"MAX(DAMAGE)"};
				String query = "SELECT max(DAMAGE) from CHAMPION_SKILLS2";
				data = db.exactQuery(query, entry);
				updateTable(data, entry, null);		
			}
		});	
		
		mntmMostChampions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
						String[][] data;
						String[] entry = {"USERNAME", "REGION", "COUNT(*)"};
						String query = "SELECT p.USERNAME, p.REGION, COUNT(*) FROM PLAYER p, PLAYER_PURCHASE_CHAMPION c "
								+ "WHERE p.USERNAME = c.PLAYERID AND p.REGION = c.PLAYERREGION GROUP BY p.USERNAME, p.REGION "
								+ "HAVING COUNT(*)= (SELECT MAX(c) FROM (SELECT COUNT(*) AS c FROM PLAYER p, PLAYER_PURCHASE_CHAMPION c"
								+ " WHERE p.USERNAME = c.PLAYERID AND p.REGION = c.PLAYERREGION GROUP BY p.USERNAME, p.REGION))"; 
						data = db.exactQuery(query, entry);
						updateTable(data, entry, null);			

			}
		});	
	}
	
	private void generateDeleteMenu() {
		JMenuItem mntmChampion = new JMenuItem("Champion (CASCADE)");
		mnDelete.add(mntmChampion);
		JMenuItem mntmSkill = new JMenuItem("Skills1 (NOT CASCADE)");
		mnDelete.add(mntmSkill);
		
		mntmChampion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String[] deleteInputName = {"CHAMPION NAME"};
				UserInput deleteInput = new UserInput(deleteInputName);
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
				UserInput deleteInput = new UserInput(deleteInputName);
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
		JMenuItem mntmUpdate = new JMenuItem("Update level");
		mnUpdate.add(mntmUpdate);
			
		mntmUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String[] updateInputName = {"LEVEL", "USERNAME", "REGION"};
				UserInput deleteInput = new UserInput(updateInputName);
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
	
	private void generateAdminMenu() {
		JMenuItem mntmAllReport = new JMenuItem("All reports");
		mnAdmin.add(mntmAllReport);
		JMenuItem mntmUpdateStatus = new JMenuItem("Update Player status");
		mnAdmin.add(mntmUpdateStatus);
		
		mntmAllReport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String [][] data;
				String[] entry = db.getEntries("REPORT_A_PLAYER");
				String query = "SELECT * from REPORT_A_PLAYER";
				data = db.exactQuery(query, entry);
				updateTable(data, entry, null);		
			}
		});	
		
		mntmUpdateStatus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String[] adminInputName = {"STATUS", "USERNAME", "REGION"};
				UserInput adminInput = new UserInput(adminInputName);
				adminInput.setVisible(true);
				
				adminInput.btnGo.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						String query = "UPDATE PLAYER SET ACCOUNTSTATUS= '" + adminInput.textField[0].getText() + "' WHERE USERNAME= '" 
								+ adminInput.textField[1].getText() + "' AND REGION= '" + adminInput.textField[2].getText() + "'";
						if (db.deletion(query)) {
							lblT.setText("UPDATE SUCCESFUL");
							lblT.setForeground(Color.blue);
						} else {
							lblT.setText("UPDATE UNSUCCESFUL");
							lblT.setForeground(Color.red);
						}
						adminInput.dispose();
					}
				});	
			}
		});
	}
	
	
	private void generateAccountMenu() {
		JMenuItem mntmStatus = new JMenuItem("My Status");
		mnAccount.add(mntmStatus);
		
		mntmStatus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String [][] data;
				String[] entry = {"USERNAME", "REGION", "PASSWORD", "PLAYERLEVEL", "TIMESREPORTED", "RIOTPOINTS"
						, "IPPOINTS", "DIVISION", "ACCOUNTSTATUS"};
				String query = "SELECT * from PLAYER WHERE USERNAME ='" + username +"' and region = '" + region + "'";
				data = db.exactQuery(query, entry);
				updateTable(data, entry, null);		
			}
		});	
		
	}
	
	private static void login() {
		Login login = new Login();
		login.setVisible(true);
		login.btnLogin.addActionListener(new ActionListener() {
		
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e) {
				if (login.textField.getText().equals("admin") && login.passwordField.getText().equals("admin")) {
					accountStatus = "admin";
					login.dispose();
					GUI window = new GUI();
					window.frame.setVisible(true);
				}
				String query = "SELECT USERNAME FROM PLAYER WHERE USERNAME= '" + login.textField.getText() + "' AND REGION= '"
						+ login.buttonGroup.getSelection().getActionCommand() + "' AND PASSWORD= '" + login.passwordField.getText() + "'";
				String[] entry = {"USERNAME"};
				String[][] data = db.exactQuery(query, entry);
				if (data[0][0] != null ) {
					accountStatus = "online";
					username = login.textField.getText();
					region = login.buttonGroup.getSelection().getActionCommand();
					login.dispose();
					GUI window = new GUI();
					window.frame.setVisible(true);
				} else {
					login.label.setText("Login unsuccesful");
			    	login.label.setForeground(Color.red);
				}

			}
		});	
		
	}

	private void setErrorMessage(String error) {
		errorMessage = error;
//    	lblT.setText(errorMessage);
//    	lblT.setForeground(Color.red);
		System.out.println("SQL Error: " +  error);
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
				if (accountStatus.equals("admin")) {
					table.setEnabled(true);
				}
				lblT.setText("UPDATE SUCCESFUL");
				lblT.setForeground(Color.blue);
			} else if (pkValue == null) {
				lblT.setText("UPDATE UNSUCCESFUL Not a valid row");
				lblT.setForeground(Color.red);
			}
		} catch (SQLException e1) {
			setErrorMessage(e1.getMessage());
			lblT.setText("UPDATE UNSUCCESFUL");
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
//					setErrorMessage(e1.getMessage());
					lblT.setText("Search not valid");
				}
			}
		}
	}
}