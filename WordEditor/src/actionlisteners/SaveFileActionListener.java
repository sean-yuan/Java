package actionlisteners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import seanyuan_CSCI201L_Assignment4.OnlineFileChooser;
import seanyuan_CSCI201L_Assignment4.TextEditor;
import seanyuan_CSCI201L_Assignment4.UserAccount;
import tab.TabOpener;

public class SaveFileActionListener extends TabOpener implements ActionListener{
	private UserAccount regUser;
	public SaveFileActionListener(JFrame frame, UserAccount ua) {
		super(frame);
		regUser = ua;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		TextEditor editor = (TextEditor)frame;
		if(editor.registeredUser != null){
			Object[] options = {"ONLINE","OFFLINE"};
			int choice = JOptionPane.showOptionDialog(frame, "Where would you like to save the file?", "Save...",
			JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
			if(choice==0){
				OnlineFileChooser chooser = new OnlineFileChooser(editor.client, super.getSelectedTextArea().getText(), tabbedPane);
				chooser.setVisible(true);
			}
			else{
				offlineSaveFile();
			}
		}
		else if ((editor.registeredUser.getUsername()).equals("null")){
			JOptionPane.showMessageDialog(null, "Sever cannot be reached. Program in offline mode.", "Save Failed", JOptionPane.WARNING_MESSAGE);
			offlineSaveFile();
		}
		else{
			offlineSaveFile();
		}
	}
	private void offlineSaveFile() {
		FileFilter ft = new FileNameExtensionFilter ("Text Files", "txt");
		JFileChooser saver = new JFileChooser();
		saver.setFileFilter(ft);
		JTextArea textArea = super.getSelectedTextArea();
		String allText = textArea.getText();
		String title = super.getSelectedTitle();
		if(!title.equals(NEW_FILE_NAME)){
			saver.setSelectedFile(new File(title));		
		}
		int status = saver.showSaveDialog(null);
		File selectedFile;
		String fileName = " ";
		if (status == JFileChooser.APPROVE_OPTION) {			
			selectedFile = saver.getSelectedFile();//when saving a second time it creates a blank document
			try {
				fileName = selectedFile.getCanonicalPath();
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
		    //System.out.println(fileName);
		    if (!fileName.endsWith(".txt")) {
		    	fileName = fileName + ".txt";
		    }
		    if (selectedFile.exists()) {
		    	int result = JOptionPane.showConfirmDialog(null, "The file exists, overwrite?",
		        "Existing file", JOptionPane.YES_NO_CANCEL_OPTION);
		        if (result == JOptionPane.YES_OPTION) {
		        	try {
						writeFile(fileName, allText);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
		        }
		    }
		    else{
		     	try {
					writeFile(fileName, allText);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		    }
		}
	}
	public void writeFile(String fileName, String allText) throws IOException{
	    File file = new File(fileName);
	    tabbedPane.setTitleAt(tabbedPane.getSelectedIndex(), fileName);
	    BufferedWriter output = new BufferedWriter(new FileWriter(file));
	    output.write(allText);
	    output.close();
	}
}
