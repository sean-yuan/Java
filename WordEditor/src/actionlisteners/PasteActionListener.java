package actionlisteners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import tab.TextModifier;

public class PasteActionListener extends TextModifier implements ActionListener {
	public PasteActionListener(JFrame frame){
		super(frame);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		super.pasteTextFromClipboard();
	}
}