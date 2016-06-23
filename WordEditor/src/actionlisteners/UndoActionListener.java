package actionlisteners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.undo.CannotUndoException;

import tab.Tab;
import tab.TabOpener;


public class UndoActionListener extends TabOpener implements ActionListener {
	public UndoActionListener(JFrame frame){
		super(frame);
	}
	@Override
	public void actionPerformed(ActionEvent evt) {
		try {
			Tab tab = (Tab)super.tabbedPane.getSelectedComponent();
			if (tab.undo.canUndo()) {
				tab.undo.undo();
            }
        } catch (CannotUndoException e) {
             }
    }
}
