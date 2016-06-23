package seanyuan_CSCI201L_Assignment4;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class MainOptions {

}

class OfflineActionListener implements ActionListener{
	private JPanel buttonLayout;
	private TextEditor tex;
	public OfflineActionListener(JPanel container, TextEditor te) {
		buttonLayout = container;
		this.tex = te;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		tex.toggleVisibilityOfLoginOnlyMenus();
		buttonLayout.removeAll();
		buttonLayout.repaint();
		buttonLayout.setVisible(true);
	}
}

class LoginActionListener implements ActionListener{
	private JPanel buttonLayout;
	private Client c;
	private TextEditor tex;
	public LoginActionListener(JPanel container, Client client, TextEditor te) {
		c = client;
		buttonLayout = container;
		this.tex = te;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		buttonLayout.removeAll();
		buttonLayout.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		JLabel uname = new JLabel("USERNAME:");
		JTextField unameText = new JTextField(20);
		JLabel pass = new JLabel("PASSWORD:");
		JPasswordField passText = new JPasswordField(20);
		passText.setEchoChar('*');
		CustomButton loginConfirm = new CustomButton("LOGIN");
		loginConfirm.addActionListener(new AuthenticateActionListener(buttonLayout, unameText,
				passText, c) );
		gbc.gridx = 0;
		gbc.gridy = 0;
		buttonLayout.add(uname, gbc);
		gbc.gridx = 1;
		gbc.gridy = 0;
		buttonLayout.add(unameText, gbc);
		gbc.gridx = 0;
		gbc.gridy = 1;
		buttonLayout.add(pass, gbc);
		gbc.gridx = 1;
		gbc.gridy = 1;
		buttonLayout.add(passText, gbc);
		gbc.gridx = 1;
		gbc.gridy = 2;
		buttonLayout.add(loginConfirm,gbc);
		buttonLayout.repaint();
		buttonLayout.getParent().repaint();
		tex.repaint();
		tex.pack();
		buttonLayout.setVisible(true);
	}
}

class AuthenticateActionListener implements ActionListener{
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


class SignUpActionListener implements ActionListener{
	private JPanel buttonLayout;
	private Client c;
	private TextEditor tex;
	public SignUpActionListener(JPanel container, Client client, TextEditor te) {
		c = client;
		buttonLayout = container;
		this.tex = te;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		buttonLayout.removeAll();
		refactor();
	}
	private void refactor(){
		buttonLayout.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		JLabel uname = new JLabel("USERNAME:");
		JTextField unameText = new JTextField(20);
		JLabel pass = new JLabel("PASSWORD:");
		JPasswordField passText = new JPasswordField(20);
		passText.setEchoChar('*');
		JLabel rep = new JLabel("REPEAT:");
		JPasswordField repText = new JPasswordField(20);
		CustomButton signUpConf = new CustomButton("LOGIN");
		signUpConf.addActionListener(new SignUpConfirmActionListener(buttonLayout, unameText,
										passText, repText, c) );
		repText.setEchoChar('*');
		gbc.gridx = 0;
		gbc.gridy = 0;
		buttonLayout.add(uname, gbc);
		gbc.gridx = 1;
		gbc.gridy = 0;
		buttonLayout.add(unameText, gbc);
		gbc.gridx = 0;
		gbc.gridy = 1;
		buttonLayout.add(pass, gbc);
		gbc.gridx = 1;
		gbc.gridy = 1;
		buttonLayout.add(passText, gbc);
		gbc.gridx = 0;
		gbc.gridy = 2;
		buttonLayout.add(rep, gbc);
		gbc.gridx = 1;
		gbc.gridy = 2;
		buttonLayout.add(repText, gbc);
		gbc.gridx = 1;
		gbc.gridy = 3;
		buttonLayout.add(signUpConf,gbc);
		buttonLayout.repaint();
		buttonLayout.getParent().repaint();
		tex.repaint();
		tex.pack();
		buttonLayout.setVisible(true);
	}
	
}

class SignUpConfirmActionListener implements ActionListener{
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
