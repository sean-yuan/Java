package actionlisteners;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import seanyuan_CSCI201L_Assignment4.TextEditor;
import tab.TabOpener;

public class ExitFileActionListener extends TabOpener implements ActionListener {
	public ExitFileActionListener(JFrame frame){
		super(frame);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		int selected = tabbedPane.getSelectedIndex();
		if(selected >= 0) {
			tabbedPane.remove(selected);
			
		}
		else{
			System.exit(0);
		}
		if(tabbedPane.getComponents().length == 0) {
			TextEditor editor = (TextEditor)frame;
			editor.toggleVisibilityOfFileOnlyMenus();
			CardLayout cards = (CardLayout)editor.bgView.getLayout();
			cards.show(editor.bgView, editor.DEFAULT_VIEW);
		}
	}
	
}