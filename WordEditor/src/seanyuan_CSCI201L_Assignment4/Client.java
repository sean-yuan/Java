package seanyuan_CSCI201L_Assignment4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

public class Client {
	private ConfigData configData;
	private Socket s;
	private PrintWriter out;
	private BufferedReader in;
	private TextEditor editor;
	private UserAccount session;
	private OnlineFileChooser chooser;
	
	private class ClientListener implements Runnable {
		private TextEditor editor;
		private BufferedReader in;
		private PrintWriter out;
		public ClientListener(BufferedReader in, PrintWriter out, TextEditor editor) {
			this.in = in;
			this.out = out;
			this.editor = editor;
		}
		@Override
		public void run() {
			String line = "";
			while(s.isConnected()) {
			//while(true){
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
					if(line.startsWith("userfiles::")) {
						//something like userfiles::text.txt&text2.txt&text3.txt
						line = line.substring(11); //remove header
						if(line.startsWith("error detected")) {
							chooser.handleUserFilesRequestError();
						}
						List<String> files = Arrays.asList(line.split("&")); 
						chooser.buildGui(files);
					}
					if(line.startsWith("file::")) {
						line = line.substring(6); //remove header
						if(line.startsWith("error")) {
							chooser.handleSaveFileError();
						}
						else{
							chooser.handleSaveFileSuccess();
						}
					}
					if(line.startsWith("fileopen::")) {
						line = line.substring(10); //remove header
						try {
							if(in.ready()){
								String g = in.readLine();
								g = line + "\n" + g + "\n";
								String m = "";
								while(in.ready()){
									m = in.readLine();
									g = g + m + "\n";
								}
								g = g.trim();
								String arr1[] = g.split("&");
								String name = arr1[0].split(":")[1];
								String data = arr1[1].split(":")[1];
								editor.handleOpenFileSuccess(name, data);

							}else{
								String arr[] = line.split("&");
								String name = arr[0].split(":")[1];
								String data = arr[1].split(":")[1];
								editor.handleOpenFileSuccess(name, data);
							}
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
				}
			}
			editor.handleOffline();
			
		}
		
	}
	
	public Client(TextEditor editor) throws IOException {
		this.editor = editor;
		configData = new ConfigParser("client.config").parse();
		//System.out.println(configData.ipAddress + "    " + configData.portNumber);
		s = new Socket(configData.ipAddress, configData.portNumber);		
		out = new PrintWriter(s.getOutputStream(), true);
		in = new BufferedReader(new InputStreamReader(s.getInputStream()));
		new Thread(new ClientListener(in, out, this.editor)).start();
	}
	
	public void setFileChooser(OnlineFileChooser chooser) {
		//System.out.println("chooser set");
		this.chooser = chooser;
	}
	
	public void sendUserFilesListRequest() {
		out.println("userfiles::" + this.session.toString());
	}
	
	public void signup(String username, String password) {
		String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
		out.println("signup::username:" + username + "&password:" + hashedPassword);
	}
	
	public void login(String username, String password) {
		out.println("login::username:" + username + "&password:" + password);
	}
	
	public void openFile(String filename) {
		if(session == null) {
			return; //or throw exception
		}
		out.println("openFile::filename:" + filename + "&userId:" + session.getId());
	}
	
	public void saveFile(String filename, String fileData) {
		if(session == null) {
			return; //or throw exception
		}
		this.out.println("saveFile::filename:" + filename + 
				"&fileData:" + DatabaseFile.serializeFileData(fileData) 
				+ "&userId:" + session.getId());
	}
	
}
