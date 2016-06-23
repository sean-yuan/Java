package actionlisteners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JTextArea;

import tab.TextModifier;

public class CutActionListener extends TextModifier implements ActionListener{
	public CutActionListener(JFrame frame){
		super(frame);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		super.copyTextToClipboard();
		JTextArea textArea = super.getSelectedTextArea();
		textArea.replaceSelection("");
	}
}