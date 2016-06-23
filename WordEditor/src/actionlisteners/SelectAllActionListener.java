package actionlisteners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JTextArea;

import tab.TabOpener;

public class SelectAllActionListener extends TabOpener implements ActionListener{
	public SelectAllActionListener(JFrame frame){
		super(frame);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JTextArea textSelection = super.getSelectedTextArea();
		textSelection.selectAll();
	}
}