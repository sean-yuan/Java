package seanyuan_CSCI201L_Assignment4;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

import actionlisteners.*;
import tab.Tab;

public class TextEditor extends JFrame{
	public static final long serialVersionUID = 1;
	public static final String DEFAULT_VIEW = "Default card view";
	public static final String TAB_VIEW = "Tab view";
	private Autocorrector autocorrect;
	private ConfigurePanel cfPanel;
	public JTabbedPane tabbedPane;
	public JPanel bgView;
	public Client client;
	private JPanel buttonContainer;
	private JPanel frontView;
	public UserAccount registeredUser;
	String gotTitle;
	String gotData;
	OpenFileActionListener g;
	
	public TextEditor() throws IOException, FontFormatException {
		super("CS Office 201");
		setLayout(new BorderLayout());
		bgView = new JPanel();
		tabbedPane = new JTabbedPane();
		autocorrect = new Autocorrector();
		client = new Client(this);
		g = new OpenFileActionListener(this);
		try {
			cfPanel = new ConfigurePanel(autocorrect.corrector);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		bgView.setLayout(new CardLayout());
		JLabel logo = new JLabel("TROJAN OFFICE");
		logo.setForeground(Color.orange);
		logo.setFont(Font.createFont(Font.TRUETYPE_FONT, new File("fonts/kenvector_future.ttf")).deriveFont(16.0f));
	    
		frontView = new JPanel();
	    GridBagConstraints cc = new GridBagConstraints();
	    cc.fill = GridBagConstraints.HORIZONTAL;
	    cc.gridx = 0;
	    cc.gridy = 0;
	    frontView.add(logo, cc);
	    frontView.setLayout(new GridBagLayout());
	    buttonContainer = new JPanel();
	    populateButtons(buttonContainer, frontView);
	    cc.gridx = 0;
	    cc.gridy = 1;
	    frontView.add(buttonContainer, cc);
	    frontView.setBackground(Color.gray);
	    bgView.add(frontView, DEFAULT_VIEW);
	    bgView.add(tabbedPane, TAB_VIEW);
	    bgView.setBackground(Color.gray);
	    bgView.setVisible(true);
		this.setPreferredSize(new Dimension(700, 500));
		JMenuBar bar = new JMenuBar(){
			private static final long serialVersionUID = 1L;

			public void paintComponent(Graphics g) {
			  g.drawImage(Toolkit.getDefaultToolkit().getImage("menubar.jpg"),0,0,this);
			}
		};
		JMenu fileMenu = new JMenu("File");
		JMenu editMenu = new JMenu("Edit");
		fileMenu.setVisible(false);
		editMenu.setVisible(false);
		JMenu spellCheckMenu = new JMenu("SpellCheck");
		spellCheckMenu.setVisible(false);
	    bar.add(fileMenu);
	    bar.add(editMenu);
	    bar.add(spellCheckMenu);
	    buildFileMenu(fileMenu);
	    buildEditMenu(editMenu);
	    buildSpellCheckMenu(spellCheckMenu);
	    
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setJMenuBar(bar);
        add(bgView, BorderLayout.CENTER);
	    
		setCursor(Toolkit.getDefaultToolkit().createCustomCursor(
				new ImageIcon("resources/img/icon/cursor.png").getImage(),
				new Point(0,0),"custom cursor"));
		pack();
		setVisible(true);	
	}
	
	public void handleSignupError(){
		JOptionPane.showMessageDialog(null, "Sign-up Error: Username taken. Please try again.", "Sign-up Failed", JOptionPane.ERROR_MESSAGE);
	}
	
	public void signIn(UserAccount signedIn){
		registeredUser = signedIn;
		buttonContainer.removeAll();
		buttonContainer.repaint();
		buttonContainer.setVisible(true);
		toggleVisibilityOfLoginOnlyMenus();
	}
	
	public void handleLoginError(){
		JOptionPane.showMessageDialog(null, "Username/Password Invailid", "Sign-in Failed", JOptionPane.ERROR_MESSAGE);
	}
	
	public void login(UserAccount loggedIn){
		registeredUser = loggedIn;
		buttonContainer.removeAll();
		buttonContainer.setVisible(true);
		buttonContainer.repaint();
		toggleVisibilityOfLoginOnlyMenus();
	}
	
	public void handleOpenFileSuccess(String title, String data){
		g.createNewPanelWithTextArea(title, data);
	}
	
	public void handleOffline(){
		registeredUser = new UserAccount ("null");
		toggleVisibilityOfLoginOnlyMenus();
	}
	
	private void buildFileMenu(JMenu fileMenu) {
	    fileMenu.setMnemonic('f');
	    ImageIcon newIcon = new ImageIcon("new.png",
                " ");
	    JMenuItem newMenuItem = fileMenu.add(new JMenuItem("New", newIcon));
	    newMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, Event.CTRL_MASK));
	    newMenuItem.addActionListener(new NewFileActionListener(this));

	    
	    ImageIcon openIcon = new ImageIcon("open.png",
                " ");
	    JMenuItem openMenuItem = fileMenu.add(new JMenuItem("Open", openIcon));
	    openMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, Event.CTRL_MASK));
	    openMenuItem.addActionListener(g);
	    
	    ImageIcon saveIcon = new ImageIcon("save.png",
                " ");
	    JMenuItem saveMenuItem = fileMenu.add(new JMenuItem("Save", saveIcon));
	    saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Event.CTRL_MASK));
	    saveMenuItem.addActionListener(new SaveFileActionListener(this, registeredUser));
	    
	    ImageIcon closeIcon = new ImageIcon("close.png",
                " ");
	    JMenuItem closeMenuItem = fileMenu.add(new JMenuItem("Close", closeIcon));
	    closeMenuItem.addActionListener(new ExitFileActionListener(this));
	}
	private void buildEditMenu(JMenu editMenu) {
		editMenu.setMnemonic('e');
		
		 ImageIcon undoIcon = new ImageIcon("undo.png",
	                " ");
	    JMenuItem undoItem = editMenu.add(new JMenuItem("Undo", undoIcon));
	    undoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U, Event.CTRL_MASK));
	    undoItem.addActionListener(new UndoActionListener(this));
	    
	    ImageIcon redoIcon = new ImageIcon("redo.png",
                " ");
	    JMenuItem redoItem = editMenu.add(new JMenuItem("Redo", redoIcon));
	    redoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, Event.CTRL_MASK));
	    redoItem.addActionListener(new RedoActionListener(this) );
	    
	    ImageIcon cutIcon = new ImageIcon("cut.png",
                " ");
	    editMenu.addSeparator();
	    JMenuItem cutItem = editMenu.add(new JMenuItem("Cut", cutIcon));
	    cutItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, Event.CTRL_MASK));
	    cutItem.addActionListener(new CutActionListener(this));
	    
	    ImageIcon copyIcon = new ImageIcon("copy.png",
                " ");
	    JMenuItem copyItem = editMenu.add(new JMenuItem("Copy", copyIcon));
	    copyItem.addActionListener(new CopyActionListener(this));
	    copyItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, Event.CTRL_MASK));
	    
	    ImageIcon pasteIcon = new ImageIcon("paste.png",
                " ");
	    JMenuItem pasteItem = editMenu.add(new JMenuItem("Paste", pasteIcon));
	    pasteItem.addActionListener(new PasteActionListener(this));
	    pasteItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, Event.CTRL_MASK));
	    editMenu.addSeparator();
	    
	    ImageIcon sAIcon = new ImageIcon("open.png",
                " ");
	    JMenuItem selectAllItem = editMenu.add(new JMenuItem("Select All", sAIcon));
	    selectAllItem.addActionListener(new SelectAllActionListener(this));
	    selectAllItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, Event.CTRL_MASK));
	}
	
	public void toggleVisibilityOfFileOnlyMenus() {
		JMenuBar bar = this.getJMenuBar();
		JMenu editMenu = bar.getMenu(1);
		JMenu spellCheckMenu = bar.getMenu(2);
		editMenu.setVisible(!editMenu.isVisible());
		spellCheckMenu.setVisible(!spellCheckMenu.isVisible());
	}
	
	public boolean areFileOnlyMenusVisible() {
		JMenuBar bar = this.getJMenuBar();
		JMenu editMenu = bar.getMenu(1);
		JMenu spellCheckMenu = bar.getMenu(2);
		return editMenu.isVisible() && spellCheckMenu.isVisible();
	}
	
	public void toggleVisibilityOfLoginOnlyMenus() {
		JMenuBar bar = this.getJMenuBar();
		JMenu fileMenu = bar.getMenu(0);
		fileMenu.setVisible(!fileMenu.isVisible());
	}
	
	public boolean areLoginOnlyMenusVisible() {
		JMenuBar bar = this.getJMenuBar();
		JMenu fileMenu = bar.getMenu(0);
		return fileMenu.isVisible();
	}
	
	private void buildSpellCheckMenu(JMenu spellCheckMenu) {
		spellCheckMenu.setMnemonic('s');
		
		ImageIcon runIcon = new ImageIcon("run.png",
                " ");
	    JMenuItem runItem = spellCheckMenu.add(new JMenuItem("Run", runIcon));
	    runItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, Event.CTRL_MASK));
	    runItem.addActionListener(new RunAutoCorrectActionListener(this, autocorrect, cfPanel));
	    
	    ImageIcon configIcon = new ImageIcon("config.png",
                " ");
	    JMenuItem configItem = spellCheckMenu.add(new JMenuItem("Configure", configIcon));
	    configItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, Event.CTRL_MASK));
	    configItem.addActionListener(new RunConfigureActionListener(this, autocorrect, cfPanel) );
	}
	
	private void populateButtons(JPanel buttonContainer, JPanel front){
		buttonContainer.setLayout(new BorderLayout());
	    //three buttons
		CustomButton login = new CustomButton("LOGIN");
	    login.addActionListener(new LoginActionListener(buttonContainer, client, this) );
	    buttonContainer.add(login, BorderLayout.WEST);
	    CustomButton signup = new CustomButton("SIGNUP");
	    signup.addActionListener(new SignUpActionListener(buttonContainer, client, this) );
	    buttonContainer.add(signup, BorderLayout.EAST);
	    CustomButton offline = new CustomButton("OFFLINE");
	    offline.addActionListener(new OfflineActionListener(buttonContainer, this) );
	    buttonContainer.add(offline, BorderLayout.SOUTH);
	    //three buttons
	    buttonContainer.setBackground(Color.gray);
	    buttonContainer.setVisible(true);
	}
	
	public static void setUIFont (FontUIResource f){
		//found at http://stackoverflow.com/questions/7434845/setting-the-default-font-of-swing-program
	    java.util.Enumeration keys = UIManager.getDefaults().keys();
	    while (keys.hasMoreElements()) {
	      Object key = keys.nextElement();
	      Object value = UIManager.get (key);
	      if (value != null && value instanceof javax.swing.plaf.FontUIResource)
	        UIManager.put (key, f);
	      }
	}

	public static void main (String[] args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException{
		try{
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch(Exception e){
			System.out.println("Warning! Cross-platform L&F not used!");
		}
		setUIFont(new FontUIResource(new Font("Verdana", Font.BOLD, 12)));
		try {
			setUIFont(new javax.swing.plaf.FontUIResource(Font.createFont(Font.TRUETYPE_FONT, new File("fonts/kenvector_future.ttf")).deriveFont(12.0f)));
		} catch (FontFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		TextEditor mfgui = null;
		try {
			mfgui = new TextEditor ();
			Image icon = Toolkit.getDefaultToolkit().getImage("icon.png");//set icon?
			mfgui.setIconImage(icon);
			
			Class<?> applicationClass = Class.forName("com.apple.eawt.Application");
			Method getApplicationMethod = applicationClass.getMethod("getApplication");
			Method setDockIconMethod = applicationClass.getMethod("setDockIconImage", java.awt.Image.class);
			Object macOSXApplication = getApplicationMethod.invoke(null);
			setDockIconMethod.invoke(macOSXApplication, icon);

			mfgui.setVisible(true);
		} catch (IOException | FontFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
