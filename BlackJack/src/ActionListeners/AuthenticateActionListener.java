package ActionListeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import BlackJackCore.Client;

public class AuthenticateActionListener implements ActionListener{
	private JPanel buttonLayout;
	private String uname;
	private String pass;
	private JTextField unameTextLabel;
	private JPasswordField passTextLabel;
	private Client client;
	public AuthenticateActionListener(JPanel container, JTextField unameText, JPasswordField passText, Client c) {
		buttonLayout = container;
		unameTextLabel = unameText;
		passTextLabel = passText;
		client = c;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		uname = unameTextLabel.getText();
		pass = new String(passTextLabel.getPassword());
		client.login(uname, pass);
	}
}
