package actionlisteners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.undo.CannotRedoException;

import tab.Tab;
import tab.TabOpener;


public class RedoActionListener extends TabOpener implements ActionListener {
	public RedoActionListener(JFrame frame){
		super(frame);
	}
	@Override
	public void actionPerformed(ActionEvent evt) {
        try {
        	Tab tab = (Tab)super.tabbedPane.getSelectedComponent();
            if (tab.undo.canRedo()) {
                tab.undo.redo();
            }
        } catch (CannotRedoException e) {
        }
    }
}