package seanyuan_CSCI201L_Assignment4;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FontFormatException;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class OnlineFileChooser extends JDialog {
	public enum FileChooserType {
		SAVE, OPEN
	}
	private FileChooserType type;
	private static final long serialVersionUID = 1L;
	private Client client;
	private JPanel fileChooserPanel;
	private JList<String> availableFileNames;
	private String documentText;
	public JTextField nameText;
	public JTabbedPane tabbedPane;
	
	JScrollPane jsp;
	
	private class FileSelectionListener implements ListSelectionListener {
		private JList<String> fileNames;
		private JTextField nameField;
		public FileSelectionListener(JList<String> fileNamesList, JTextField nameField) {
			this.fileNames = fileNamesList;
			this.nameField = nameField;
		}
		@Override
		public void valueChanged(ListSelectionEvent e) {
			this.nameField.setText(this.fileNames.getSelectedValue());
		}
	}
	
	private class SaveButtonActionListener implements ActionListener {
		
		private JTextField nameField;
		public SaveButtonActionListener(JTextField nameTextField) {
			this.nameField = nameTextField;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			String bodyText = OnlineFileChooser.this.documentText;
			String fname = this.nameField.getText();
			if(!fname.endsWith(".txt")){
				fname = fname+".txt";
			}
			OnlineFileChooser.this.client.saveFile(fname, bodyText);
			OnlineFileChooser.super.setVisible(false);
		}
		
	}
	
	private class OpenButtonActionListener implements ActionListener {
		private JTextField nameField;
		public OpenButtonActionListener(JTextField nameTextField) {
			this.nameField = nameTextField;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			OnlineFileChooser.this.client.openFile(this.nameField.getText());
			OnlineFileChooser.super.setVisible(false);
		}
		
	}
	
	public OnlineFileChooser(Client client){
		super((JFrame)null,"File Chooser");
		client.sendUserFilesListRequest();
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.client = client;
		this.client.setFileChooser(this);
		this.fileChooserPanel = new JPanel();
		this.type = FileChooserType.OPEN;
	}
	
	public OnlineFileChooser(Client client, String documentText, JTabbedPane tabbedPane) {
		this(client);
		this.tabbedPane = tabbedPane;
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.documentText = documentText;
		this.type = FileChooserType.SAVE;
	}
	
	public void buildGui(List<String> fileNames) {
		String[] str = fileNames.toArray(new String[fileNames.size()]);
		availableFileNames = new JList<String>(str);
		availableFileNames.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		availableFileNames.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		availableFileNames.setVisibleRowCount(-1);
		jsp = new JScrollPane(availableFileNames);
		jsp.setPreferredSize(new Dimension(250, 80));
	    jsp.setAlignmentX(LEFT_ALIGNMENT);
		
        JPanel namePane = new JPanel();
        JLabel name = new JLabel("Name:");
		nameText = new JTextField(20);
		//based off of example from: https://docs.oracle.com/javase/tutorial/displayCode.html?code=https://docs.oracle.com/javase/tutorial/uiswing/examples/components/ListDialogRunnerProject/src/components/ListDialog.java
		namePane.setLayout(new BoxLayout(namePane, BoxLayout.LINE_AXIS));
	    namePane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
	    namePane.add(Box.createHorizontalGlue());
	    namePane.add(name);
	    namePane.add(Box.createRigidArea(new Dimension(10, 0)));
	    namePane.add(nameText);
	    
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new CancelActionListener());
        JButton actionButton = null;
        if(this.type == FileChooserType.OPEN) {
        	actionButton = new JButton("Open");
        	actionButton.addActionListener(new OpenButtonActionListener(nameText));
        }
        else if(this.type == FileChooserType.SAVE){
        	actionButton = new JButton("Save");
        	actionButton.addActionListener(new SaveButtonActionListener(nameText));
        }
        getRootPane().setDefaultButton(actionButton);
              
        this.fileChooserPanel.setLayout(new BoxLayout(this.fileChooserPanel, BoxLayout.PAGE_AXIS));
        JLabel label = new JLabel("SELECT A FILE:");
        label.setLabelFor(availableFileNames);
        this.fileChooserPanel.add(label);
        this.fileChooserPanel.add(Box.createRigidArea(new Dimension(0,5)));
        this.fileChooserPanel.add(jsp);
        this.fileChooserPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
        buttonPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        buttonPane.add(Box.createHorizontalGlue());
        buttonPane.add(cancelButton);
        buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPane.add(actionButton);

        Container contentPane = getContentPane();
        contentPane.add(this.fileChooserPanel, BorderLayout.NORTH);
        this.fileChooserPanel.setPreferredSize(new Dimension(400, 300));
        contentPane.add(namePane, BorderLayout.CENTER);
        contentPane.add(buttonPane, BorderLayout.SOUTH);
        
        availableFileNames.addListSelectionListener(new FileSelectionListener(this.availableFileNames, nameText));
        
        contentPane.setVisible(true);
        super.setVisible(true);
        pack();
	}
	
	class CancelActionListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			dispose();
		}	
	}
	
	public void handleSaveFileError(){
		JOptionPane.showMessageDialog(null, "File failed to save.", "Save Failed", JOptionPane.ERROR_MESSAGE);
	}
	public void handleSaveFileSuccess(){
		String filename = this.nameText.getText();
		//System.out.println(filename);
		if(filename.endsWith(".txt")){
			tabbedPane.setTitleAt(tabbedPane.getSelectedIndex(), filename);
			JOptionPane.showMessageDialog(null, "File successfully saved.", "File Saved", JOptionPane.INFORMATION_MESSAGE);
		}
		else{
			tabbedPane.setTitleAt(tabbedPane.getSelectedIndex(), filename+".txt");
			JOptionPane.showMessageDialog(null, "File successfully saved.", "File Saved", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	public void handleUserFilesRequestError() {
		// TODO Auto-generated method stub
		
	}

}
