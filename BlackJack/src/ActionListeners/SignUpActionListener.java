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

public class SignUpActionListener implements ActionListener{
	private JPanel buttonLayout;
	private Client c;
	private BlackJackCore tex;
	public SignUpActionListener(JPanel container, Client client, BlackJackCore te) {
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
