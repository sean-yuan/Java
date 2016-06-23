package ActionListeners;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import BlackJackCore.BlackJackCore;
import BlackJackCore.Client;
import CustomUI.CustomButton;

public class LoginActionListener implements ActionListener{
	private JPanel buttonLayout;
	private Client c;
	private BlackJackCore tex;
	public LoginActionListener(JPanel container, Client client, BlackJackCore te) {
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
