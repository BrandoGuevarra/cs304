import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.FormSpecs;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.Color;

public class Login extends JFrame {

	JTextField textField;
	JPasswordField passwordField;
	private JLabel lblRegion;
	final ButtonGroup buttonGroup = new ButtonGroup();
	private JRadioButton rdbtnNewRadioButton;
	private JRadioButton rdbtnNewRadioButton_1;
	private JRadioButton rdbtnNewRadioButton_2;
	JButton btnLogin;
	JLabel label;
	JLabel background;
	
	public Login() {	
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 960, 540);
		try {
			BufferedImage img = ImageIO.read(new File("img/background.jpg"));
			background = new JLabel(new ImageIcon(img));
			background.setBounds(0, 0, 944, 501);
			background.setOpaque(false);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		getContentPane().setLayout(null);
		getContentPane().add(background);
		background.setLayout(new FlowLayout());

		JLabel lblUsername = new JLabel("Username:");
		lblUsername.setForeground(Color.WHITE);
		background.add(lblUsername);
	
		textField = new JTextField();
		background.add(textField);
		textField.setColumns(10);

		JLabel lblPassword = new JLabel("Password: ");
		lblPassword.setForeground(Color.WHITE);
		background.add(lblPassword);
		
		passwordField = new JPasswordField();
		background.add(passwordField);
		passwordField.setColumns(10);

		lblRegion = new JLabel("Region:");
		lblRegion.setForeground(Color.WHITE);
		background.add(lblRegion);
		
		rdbtnNewRadioButton = new JRadioButton("NA");
		rdbtnNewRadioButton.setSelected(true);
		rdbtnNewRadioButton.setActionCommand("NA");
		buttonGroup.add(rdbtnNewRadioButton);
		background.add(rdbtnNewRadioButton);
		
		rdbtnNewRadioButton_1 = new JRadioButton("EU");
		rdbtnNewRadioButton_1.setActionCommand("EU");

		buttonGroup.add(rdbtnNewRadioButton_1);
		background.add(rdbtnNewRadioButton_1);
		
		rdbtnNewRadioButton_2 = new JRadioButton("KR");
		rdbtnNewRadioButton_2.setActionCommand("KR");

		buttonGroup.add(rdbtnNewRadioButton_2);
		background.add(rdbtnNewRadioButton_2);
		
		btnLogin = new JButton("Login");
		background.add(btnLogin);
		
		label = new JLabel("");
		background.add(label);
		
		
		
	}

}
