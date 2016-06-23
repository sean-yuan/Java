package actionlisteners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import tab.TabOpener;

public class NewFileActionListener extends TabOpener implements ActionListener {
	public NewFileActionListener(JFrame frame) {
		super(frame);
	}
	public void actionPerformed(ActionEvent ae) {
		super.createNewPanelWithTextArea(NEW_FILE_NAME, "");
	}
}