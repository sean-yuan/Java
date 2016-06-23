package tab;

import java.awt.CardLayout;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import seanyuan_CSCI201L_Assignment4.TextEditor;

public class TabOpener {
	protected JFrame frame;
	public static final String NEW_FILE_NAME = "new";
	protected JTabbedPane tabbedPane;
	private JTextArea textArea;
	public TabOpener(JFrame frame) {
		this.frame = frame;
		this.tabbedPane = ((TextEditor)frame).tabbedPane;
		UIManager.put("TabbedPane.selected", new Color(255, 69, 0));          
		SwingUtilities.updateComponentTreeUI(tabbedPane);
	}
	public void createNewPanelWithTextArea(String filename, String file_text) {
		textArea = new JTextArea(file_text, 25, 30);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		Tab tab = new Tab(textArea);
		tabbedPane.add(filename, tab);
		tabbedPane.setSelectedComponent(tab);
		TextEditor editor = (TextEditor)frame;
		if(!editor.areFileOnlyMenusVisible()){
			editor.toggleVisibilityOfFileOnlyMenus();
		}
		CardLayout cards = (CardLayout)editor.bgView.getLayout();
		cards.show(editor.bgView, editor.TAB_VIEW);
		frame.setVisible(true);
	}
	public JTextArea getSelectedTextArea() {
		int selected = tabbedPane.getSelectedIndex();
		if(selected < 0) {
			return null;
		}
		int focusedTabIndex = tabbedPane.getSelectedIndex(); 
	    Tab jsp = (Tab)tabbedPane.getComponentAt(focusedTabIndex);
		JTextArea textArea = jsp.getTextArea();
		return textArea;
	}
	
	public String getSelectedTitle() {
		int selected = tabbedPane.getSelectedIndex();
		if(selected < 0) {
			return null;
		}
		int focusedTabIndex = tabbedPane.getSelectedIndex(); 
		return tabbedPane.getTitleAt(focusedTabIndex);
	}
}
