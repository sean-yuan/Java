package actionlisteners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import seanyuan_CSCI201L_Assignment4.OnlineFileChooser;
import seanyuan_CSCI201L_Assignment4.TextEditor;
import tab.TabOpener;

public class OpenFileActionListener extends TabOpener implements ActionListener {
	public OpenFileActionListener(JFrame frame) {
		super(frame);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		TextEditor editor = (TextEditor)frame;
		if(editor.registeredUser != null){
			Object[] options = {"ONLINE","OFFLINE"};
			int choice = JOptionPane.showOptionDialog(frame, "Where would you like to open the file?", "Open...",
					JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
			if(choice==0){
				OnlineFileChooser chooser = new OnlineFileChooser(editor.client);
			
			}
			else{
				offlineOpen();
			}
		}
		else if ((editor.registeredUser.getUsername()).equals("null")){
			JOptionPane.showMessageDialog(null, "Sever cannot be reached. Program in offline mode.", "Open Failed", JOptionPane.WARNING_MESSAGE);
			offlineOpen();
		}
		else{
			offlineOpen();
		}
	}
	private void offlineOpen(){
		JFileChooser fileChooser = new JFileChooser();
	    FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "txt", "text");
	    fileChooser.setFileFilter(filter);
	    int returnValue = fileChooser.showOpenDialog(null);
	    if (returnValue == JFileChooser.APPROVE_OPTION) {
	    	File selectedFile = fileChooser.getSelectedFile();
	    	try {
	    	String text = generateText(selectedFile);
	        super.createNewPanelWithTextArea(selectedFile.getAbsolutePath(), text);
	    	}
	    	catch(IOException ioe) {
	    		ioe.printStackTrace();
	    		//what happens if it can't read the file
	    	}
	    }
		
	}
	
	public String generateText(File selectedFile) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(selectedFile));
	    try {
	        StringBuilder sb = new StringBuilder();
	        String line = br.readLine();

	        while (line != null) {
	            sb.append(line);
	            sb.append("\n");
	            line = br.readLine();
	        }
	        return sb.toString();
	    } finally {
	        br.close();
	    }
	}
	
		
}