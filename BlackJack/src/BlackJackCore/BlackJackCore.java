package BlackJackCore;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Enumeration;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

import ActionListeners.LoginActionListener;
import ActionListeners.OfflineActionListener;
import ActionListeners.SignUpActionListener;
import CustomUI.CustomButton;

public class BlackJackCore extends JFrame{
	public static final long serialVersionUID = 1;
	public static final String DEFAULT_VIEW = "Default card view";
	public static final String TAB_VIEW = "Tab view";
	public JTabbedPane tabbedPane;
	public JPanel bgView;
	public Client client;
	private JPanel buttonContainer;
	private JPanel frontView;
	public UserAccount registeredUser;
	String gotTitle;
	String gotData;
	
	public BlackJackCore() throws IOException, FontFormatException {
		super("CS Office 201");
		new Server();
		setLayout(new BorderLayout());
		bgView = new JPanel();
		tabbedPane = new JTabbedPane();
		client = new Client(this);
		bgView.setLayout(new CardLayout());
		JLabel logo = new JLabel("TROJAN BLACKJACK");
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
	    
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(bgView, BorderLayout.CENTER);
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
		//toggleVisibilityOfLoginOnlyMenus();
	}
	
	public void handleLoginError(){
		JOptionPane.showMessageDialog(null, "Username/Password Invailid", "Sign-in Failed", JOptionPane.ERROR_MESSAGE);
	}
	
	public void login(UserAccount loggedIn){
		registeredUser = loggedIn;
		buttonContainer.removeAll();
		buttonContainer.setVisible(true);
		buttonContainer.repaint();
		//toggleVisibilityOfLoginOnlyMenus();
	}
	
	public void handleOffline(){
		registeredUser = new UserAccount ("null");
		//toggleVisibilityOfLoginOnlyMenus();
	}
	
	public void toggleVisibilityOfLoginOnlyMenus() {
		//JMenuBar bar = this.getJMenuBar();
		//JMenu fileMenu = bar.getMenu(0);
		//fileMenu.setVisible(!fileMenu.isVisible());
	}
	
	public boolean areLoginOnlyMenusVisible() {
		//JMenuBar bar = this.getJMenuBar();
		//JMenu fileMenu = bar.getMenu(0);
		//return fileMenu.isVisible();
		return true;
	}
	
	private void populateButtons(JPanel buttonContainer, JPanel front){
		buttonContainer.setLayout(new BorderLayout());
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
	    Enumeration<Object> keys = UIManager.getDefaults().keys();
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
		BlackJackCore mfgui = null;
		try {
			mfgui = new BlackJackCore ();
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
