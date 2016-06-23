package BlackJackCore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;

public class Client {
	private ConfigData configData;
	private Socket s;
	private PrintWriter out;
	private BufferedReader in;
	private BlackJackCore editor;
	private UserAccount session;
	
	private class ClientListener implements Runnable {
		private BlackJackCore editor;
		private BufferedReader in;
		private PrintWriter out;
		public ClientListener(BufferedReader in, PrintWriter out, BlackJackCore editor) {
			this.in = in;
			this.out = out;
			this.editor = editor;
		}
		@Override
		public void run() {
			String line = "";
			while(s.isConnected()) {
				try {
					line = this.in.readLine();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(line != null) {
					if(line.startsWith("signup::")) {
						line = line.substring(8); //remove header
						if(line.startsWith("error")) {
							editor.handleSignupError();
						}
						else {
							session = UserAccount.fromString(line);
							editor.signIn(session);
						}
					}
					if(line.startsWith("login::")) {
						line = line.substring(7); //remove header
						if(line.startsWith("error")) {
							editor.handleLoginError();
						}
						else {
							session = UserAccount.fromString(line);
							editor.login(session);
						}
					}
				}
			}
			editor.handleOffline();
			
		}
		
	}
	
	public Client(BlackJackCore editor) throws IOException {
		this.editor = editor;
		configData = new ConfigParser("resources/config_files/client.config").parse();
		System.out.println(configData.ipAddress + "    " + configData.portNumber);
		try{
			s = new Socket(configData.ipAddress, configData.portNumber);
		}catch (ConnectException ce){
			new Server();
		}
		out = new PrintWriter(s.getOutputStream(), true);
		in = new BufferedReader(new InputStreamReader(s.getInputStream()));
		new Thread(new ClientListener(in, out, this.editor)).start();
	}

	public void signup(String username, String password) {
		String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
		out.println("signup::username:" + username + "&password:" + hashedPassword);
	}
	
	public void login(String username, String password) {
		out.println("login::username:" + username + "&password:" + password);
	}

	
}
