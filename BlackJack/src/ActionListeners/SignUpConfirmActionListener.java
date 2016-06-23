package ActionListeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import BlackJackCore.AccountManager;
import BlackJackCore.Client;

public class SignUpConfirmActionListener implements ActionListener{
	private JPanel buttonLayout;
	private String uname;
	private String pass;
	private String con;
	private JTextField unameTextLabel;
	private JPasswordField passTextLabel;
	private JPasswordField repTextLabel;
	private Client client;
	public SignUpConfirmActionListener(JPanel container, JTextField unameText, JPasswordField passText, JPasswordField repText
			, Client c) {
		buttonLayout = container;
		unameTextLabel = unameText;
		passTextLabel = passText;
		repTextLabel = repText;
		client = c;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		uname = unameTextLabel.getText();
		pass = new String(passTextLabel.getPassword());
		con = new String(repTextLabel.getPassword());
		if(pass.equals(con)){//meets criteria
			if(AccountManager.validatePassword(pass)){
				client.signup(uname, pass);
			}
			else{
				JOptionPane.showMessageDialog(null, "Passwords must contain at least: 1-number 1-uppercase letter", "Sign-up Failed", JOptionPane.ERROR_MESSAGE);
			}
		}
		else{
			JOptionPane.showMessageDialog(null, "Paswords do not match", "Sign-up Failed", JOptionPane.ERROR_MESSAGE);
		}
		
	}
}

