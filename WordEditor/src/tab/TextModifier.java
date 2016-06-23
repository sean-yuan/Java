package tab;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JTextArea;

public class TextModifier extends TabOpener {
	public TextModifier(JFrame frame) {
		super(frame);
	}
	public void copyTextToClipboard() {
		JTextArea textArea = super.getSelectedTextArea();
		String selectedText = textArea.getSelectedText();
		if(selectedText != null) {
			getClipboard().setContents(new StringSelection(selectedText), null);
		}
	}
	public void pasteTextFromClipboard() {
		JTextArea textArea = super.getSelectedTextArea();
		try {
			String copiedText = (String)getClipboard().getContents(TextModifier.this).getTransferData(DataFlavor.stringFlavor);
			textArea.replaceRange(copiedText, textArea.getSelectionStart(), textArea.getSelectionEnd());
		} catch (UnsupportedFlavorException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private Clipboard getClipboard() {
		return Toolkit.getDefaultToolkit().getSystemClipboard();
	}
}