package actionlisteners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import tab.TextModifier;

public class CopyActionListener extends TextModifier implements ActionListener {
	public CopyActionListener(JFrame frame){
		super(frame);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		super.copyTextToClipboard();
	}
}