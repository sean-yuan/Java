package seanyuan_CSCI201L_Assignment4;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.plaf.ComboBoxUI;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.synth.SynthComboBoxUI;

import seanyuan_CSCI201L_Assignment1.AutoCorrect;

class IgnoreActionListener implements ActionListener{
	private Autocorrector autocorrector;
	public IgnoreActionListener(Autocorrector auto){
		autocorrector = auto;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		autocorrector.updateValues();
		
	}
	
}

class AddActionListener implements ActionListener{
	private Autocorrector autocorrector;
	private AutoCorrect ac;
	public AddActionListener(Autocorrector auto, AutoCorrect corrector){
		autocorrector = auto;
		ac = corrector;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		ac.addToDictionary(autocorrector.getCurrentWord());
		
	}
}

class ChangeActionListener implements ActionListener {
	Autocorrector corrector;
	private JComboBox<String> suggestions;
	private JTextArea textArea;
	String currentWord;
	public ChangeActionListener(JComboBox<String> suggestions, JTextArea textArea, Autocorrector cor) {
		this.suggestions = suggestions;
		this.textArea = textArea;
		this.corrector = cor;
		
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		String selected = (String)suggestions.getSelectedItem();
		String corpus = textArea.getText();
		currentWord = corrector.getCurrentWord();
		corpus = corpus.replaceAll(currentWord, selected);
		textArea.setText(corpus);
	}
	
}

class ColorArrowUI extends BasicComboBoxUI {
    public static ComboBoxUI createUI(JComponent c) {
        return new ColorArrowUI();
    }

    @Override protected JButton createArrowButton() {
    	JButton arrow = new JButton();
    	ImageIcon ii = new ImageIcon("down.png");
    	arrow.setIcon(ii);
        /*return new BasicArrowButton(
            BasicArrowButton.SOUTH,
            Color.cyan, Color.magenta,
            Color.yellow, Color.blue);*/
    	return arrow;
    }
}

public class Autocorrector extends JPanel {
	private static final long serialVersionUID = 1L;
	JComboBox<String> suggestions;
	JLabel currentWord;
	String cWord;
	Map<String, List<String>> suggestionMap = null;
	Iterator<Entry<String, List<String>>> iterator = null;
	AutoCorrect corrector = new AutoCorrect();
	private CustomButton changeButton;
	
	private void buildGui() {
		this.setBackground(Color.gray);
		this.setBorder(BorderFactory.createTitledBorder("Spell Check"));
		CustomButton close = new CustomButton("Close");
		close.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Autocorrector.this.hideAutocorrect();
			}
		});
		suggestions = new JComboBox<String>();
		suggestions.setUI(ColorArrowUI.createUI(suggestions));
		CustomButton add = new CustomButton("Add");
		add.addActionListener(new AddActionListener(this, corrector));
		CustomButton ignore = new CustomButton("Ignore");
		ignore.addActionListener(new IgnoreActionListener(this));
		changeButton = new CustomButton("Change");	
		currentWord = new JLabel();
		currentWord.setFont(new Font("Sans-Serif", Font.BOLD, 20));
		this.setLayout(new BorderLayout());
		JPanel options = new JPanel();
		options.setBackground(Color.gray);
		options.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 2;
		options.add(currentWord, constraints);
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		options.add(ignore, constraints);
		constraints.gridx = 1;
		options.add(add, constraints);
		constraints.gridy = 2;
		constraints.gridx = 0;
		options.add(suggestions, constraints);
		constraints.gridx = 1;
		options.add(changeButton, constraints);
		this.add(options, BorderLayout.NORTH);
		JPanel spacing = new JPanel();
		spacing.setBackground(Color.gray);
		spacing.setPreferredSize(new Dimension(150, 200));
		this.add(spacing, BorderLayout.CENTER);
		this.add(close, BorderLayout.SOUTH);
	}
	
	public Autocorrector() {
		super();
		buildGui();
		this.hideAutocorrect();
	}
	
	public void showAutocorrect() {
		this.setVisible(true);
	}
	
	public String getCurrentWord(){
		return this.cWord;
	}
	
	public void setTextArea(JTextArea textArea) {
		ActionListener[] arr = changeButton.getActionListeners();
		if(arr.length > 0) {
			changeButton.removeActionListener(arr[0]);
		}
		changeButton.addActionListener(new ChangeActionListener(this.suggestions, textArea, this));
		
	}
	
	public void hideAutocorrect() {
		this.suggestions.removeAllItems();
		this.currentWord.setText("Spelling: ");
		this.setVisible(false);
	}
	
	public void updateValues(){
		if(iterator == null) {
			return;
		}

		if(iterator.hasNext()) {
			Entry<String, List<String>> entry = iterator.next();
			this.currentWord.setText("Spelling: " + entry.getKey());
			this.cWord = entry.getKey();
			this.suggestions.removeAllItems();
			for(String word: entry.getValue()){
				this.suggestions.addItem(word);
			}
		    Object cmboItem = this.suggestions.getSelectedItem();
		    this.setVisible(true);
		}
		else {
			this.hideAutocorrect();
		}
	}
	
	
	
	public void generateSuggestions(String corpus) throws IOException {
		corrector.parseCorpus(corpus);
		suggestionMap = corrector.getSuggestedWords();
		//System.out.println(suggestionMap);
		iterator = suggestionMap.entrySet().iterator();
		/*for(Map.Entry<String, List<String>> entry: rankMap.entrySet()){
			//every time they click ignore add or change, do action then change to next word
			String mpWord = entry.getKey();
			List<String> suggestions = entry.getValue();
			updateValues(mpWord, suggestions);
		}*/
		
	}
}
