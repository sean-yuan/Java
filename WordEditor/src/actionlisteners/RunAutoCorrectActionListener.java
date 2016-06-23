package actionlisteners;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JTextArea;

import seanyuan_CSCI201L_Assignment4.Autocorrector;
import seanyuan_CSCI201L_Assignment4.ConfigurePanel;
import tab.TabOpener;

public class RunAutoCorrectActionListener extends TabOpener implements ActionListener {
	private Autocorrector autocorrect;
	private ConfigurePanel cfPanel;
	public RunAutoCorrectActionListener(JFrame frame, Autocorrector autocorrect, ConfigurePanel cfPanel) {
		super(frame);
		this.autocorrect = autocorrect;
		this.cfPanel = cfPanel;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JTextArea textArea = super.getSelectedTextArea();
		autocorrect.setTextArea(textArea);
		autocorrect.showAutocorrect();
		String corpus = textArea.getText();
		try {
			autocorrect.generateSuggestions(corpus);
			autocorrect.updateValues();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		frame.remove(this.cfPanel);
		frame.repaint();
		frame.add(autocorrect, BorderLayout.LINE_END);
		frame.setVisible(true);
	}
}