import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;

@SuppressWarnings("serial")
public class Custom extends JFrame {

	private JPanel contentPane;
	 JTextField textField;
	 JTextField textField_1;
	 JTextField textField_2;
	 final ButtonGroup buttonGroup = new ButtonGroup();
	JButton btnNewButton = new JButton("Search"); 

	public Custom() {
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Column to select:");
		lblNewLabel.setBounds(33, 14, 100, 14);
		contentPane.add(lblNewLabel);
		
		textField = new JTextField();
		textField.setBounds(177, 11, 217, 20);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("Where column:");
		lblNewLabel_1.setBounds(33, 70, 100, 14);
		contentPane.add(lblNewLabel_1);
		
		textField_1 = new JTextField();
		textField_1.setBounds(177, 67, 217, 20);
		contentPane.add(textField_1);
		textField_1.setColumns(10);
		
		JRadioButton rdbtnNewRadioButton = new JRadioButton("=");
		rdbtnNewRadioButton.setSelected(true);
		rdbtnNewRadioButton.setActionCommand("=");
		buttonGroup.add(rdbtnNewRadioButton);
		rdbtnNewRadioButton.setBounds(87, 111, 38, 23);
		contentPane.add(rdbtnNewRadioButton);
		
		JRadioButton rdbtnNewRadioButton_1 = new JRadioButton("!=");
		buttonGroup.add(rdbtnNewRadioButton_1);
		rdbtnNewRadioButton_1.setActionCommand("!=");
		rdbtnNewRadioButton_1.setBounds(127, 111, 38, 23);
		contentPane.add(rdbtnNewRadioButton_1);
		
		JRadioButton rdbtnNewRadioButton_2 = new JRadioButton(">");
		rdbtnNewRadioButton_2.setActionCommand(">");
		buttonGroup.add(rdbtnNewRadioButton_2);
		rdbtnNewRadioButton_2.setBounds(167, 111, 37, 23);
		contentPane.add(rdbtnNewRadioButton_2);
		
		JRadioButton rdbtnNewRadioButton_3 = new JRadioButton("<");
		rdbtnNewRadioButton_3.setActionCommand("<");
		buttonGroup.add(rdbtnNewRadioButton_3);
		rdbtnNewRadioButton_3.setBounds(206, 111, 46, 23);
		contentPane.add(rdbtnNewRadioButton_3);
		
		JLabel lblNewLabel_2 = new JLabel("New label");
		lblNewLabel_2.setBounds(33, 156, 100, 14);
		contentPane.add(lblNewLabel_2);
		
		textField_2 = new JTextField();
		textField_2.setBounds(177, 153, 217, 20);
		contentPane.add(textField_2);
		textField_2.setColumns(10);
		
			
		btnNewButton.setBounds(174, 211, 89, 23);
		contentPane.add(btnNewButton);
	}
}