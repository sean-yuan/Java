package tab;

import java.awt.Color;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.UndoManager;

import CustomUI.MyScrollBarUI;

public class Tab extends JScrollPane {
	private static final long serialVersionUID = 1L;
	public UndoManager undo;
	protected JTextArea textArea;
	public Tab(JTextArea textArea) {
		this.getVerticalScrollBar().setUI(new MyScrollBarUI());
		this.undo = new UndoManager();
		this.textArea = textArea;
		this.textArea.setSelectionColor(Color.orange);
		this.textArea.getDocument().addUndoableEditListener(new UndoableEditListener() {
			@Override
			public void undoableEditHappened(UndoableEditEvent e) {
				undo.addEdit(e.getEdit());
			}
		});
		this.getViewport().setView(this.textArea);
	}
	public String getText() {
		return this.getTextArea().getText();
	}
	public JTextArea getTextArea() {
		return (JTextArea)this.getViewport().getView();
	}
}
