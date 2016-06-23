package seanyuan_CSCI201L_Assignment4;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.ComboBoxUI;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicComboBoxUI;

import seanyuan_CSCI201L_Assignment1.AutoCorrect;

class FileChoose {
	private FileFilter filter;
	private JLabel label;
	public FileChoose(FileNameExtensionFilter filter, JLabel label) {
		this.filter = filter;
		this.label = label;
	}
	public String getSelectedFile() throws IOException {
		JFileChooser jfc = new JFileChooser();
	    jfc.setFileFilter(this.filter);
	    int returnValue = jfc.showOpenDialog(null);
	    if(returnValue == JFileChooser.APPROVE_OPTION) {
	    	String path = jfc.getSelectedFile().getCanonicalPath(); 
	    	label.setText(jfc.getSelectedFile().getName());
	    	return path;
	    }
	    return null;
	}
}


class KeyboardChooserActionListener extends FileChoose implements ActionListener {

	private ConfigurePanel config;
	public KeyboardChooserActionListener(ConfigurePanel config, JLabel label) {
		super(new FileNameExtensionFilter("Keyboard files", "kb"), label);
		this.config = config;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		String file = "";
		try {
			file = super.getSelectedFile();
			if(file == null) {
				return; //do something on error maybe
			}
			config.setKeyboardFile(file);
		}
		catch(IOException ioe) {
			ioe.printStackTrace();
		}	
	}
}

class WordListChooserActionListener extends FileChoose implements ActionListener {

	private ConfigurePanel config;
	public WordListChooserActionListener(ConfigurePanel config, JLabel label) {
		super(new FileNameExtensionFilter("Wordlist files", "wl"), label);
		this.config = config;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		String file = "";
		try {
			file = super.getSelectedFile();
			if(file == null) {
				return; //do something on error maybe
			}
			config.setWordListFile(file);
		}
		catch(IOException ioe) {
			ioe.printStackTrace();
		}	
	}
}


public class ConfigurePanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private AutoCorrect autocorrect;
	public ConfigurePanel(AutoCorrect autocorrect) throws IOException {
		this.autocorrect = autocorrect;
		build();
		this.hidePanel();
	}
	
	private void build() throws IOException {
		this.setBorder(BorderFactory.createTitledBorder("Configure"));
	    CustomButton close = new CustomButton("Close");
		close.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ConfigurePanel.this.hidePanel();
			}	
		});
		this.setBackground(Color.gray);
		JLabel currentWordListLabel = new JLabel(autocorrect.getDictionaryFile());
		JLabel currentKeyboardFileLabel = new JLabel(autocorrect.getKeyboardFile());
		CustomButton selectWL = new CustomButton("Select WordList...");
		selectWL.addActionListener(new WordListChooserActionListener(this, currentWordListLabel));
		CustomButton selectKB = new CustomButton("Select Keyboard...");
		selectKB.addActionListener(new KeyboardChooserActionListener(this, currentKeyboardFileLabel));
		
		JPanel jPan = new JPanel();
		jPan.setBackground(Color.gray);
		jPan.setLayout(new BoxLayout(jPan, BoxLayout.Y_AXIS));
		jPan.add(currentWordListLabel);
		jPan.add(selectWL);
		jPan.add(currentKeyboardFileLabel);
		jPan.add(selectKB);
		jPan.add(Box.createRigidArea(new Dimension(0,200)));
		jPan.add(close);
		add(jPan);
		
	}
	
	public ConfigurePanel hidePanel() {
		this.setVisible(false);
		return this;
	}
	public ConfigurePanel showPanel() {
		this.setVisible(true);
		return this;
	}
	
	public void setKeyboardFile(String keyboardFile) {
		try {
			this.autocorrect.configureKeyboard(keyboardFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setWordListFile(String dict) {
		try {
			this.autocorrect.configureWordList(dict);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
