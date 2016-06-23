package actionlisteners;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import seanyuan_CSCI201L_Assignment4.Autocorrector;
import seanyuan_CSCI201L_Assignment4.ConfigurePanel;
import tab.TabOpener;

public class RunConfigureActionListener extends TabOpener implements ActionListener {
	private ConfigurePanel cfPanel;
	private Autocorrector autocorrect;
	public RunConfigureActionListener(JFrame frame, Autocorrector autocorrect, ConfigurePanel cfPanel){
		super(frame);
		this.autocorrect = autocorrect;
		this.cfPanel = cfPanel;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		frame.remove(this.autocorrect);
		frame.repaint();
		frame.add(cfPanel, BorderLayout.LINE_END);
		cfPanel.showPanel();
		frame.setVisible(true);
	}
}