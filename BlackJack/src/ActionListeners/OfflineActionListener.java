package ActionListeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import BlackJackCore.BlackJackCore;

public class OfflineActionListener implements ActionListener{
	private JPanel buttonLayout;
	private BlackJackCore tex;
	public OfflineActionListener(JPanel container, BlackJackCore te) {
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
