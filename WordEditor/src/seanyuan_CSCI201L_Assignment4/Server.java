package seanyuan_CSCI201L_Assignment4;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Vector;
import tab.ServerFrame;

public class Server {
	public ServerFrame sf;
	private ServerSocket ss;
	private Vector<Socket> openSockets;
	private ConfigData configData;
	private class ServerListener implements Runnable {
		private Socket openSocket;
		private BufferedReader reader;
		private PrintWriter output;
		public ServerListener(Socket openSocket) {
			this.openSocket = openSocket;
			//sf.textArea.append("Server started on Port: " + ss.getLocalPort() + "\n");
			try {
				reader = new BufferedReader(new InputStreamReader(this.openSocket.getInputStream()));
				output = new PrintWriter(openSocket.getOutputStream(), true);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		@Override
		public void run() {
			while(true) {
				String line = " ";
				try {
					line = reader.readLine();
					if(line != null) {
						if(line.startsWith("signup::")) {
							//signup::username:sean&password:somepassword
							line = line.substring(8);
							String pairs[] = line.split("&");
							String username = pairs[0].split(":")[1];
							String password = pairs[1].split(":")[1];
							sf.textArea.append("Add user request: " + username + " " + "pass: " + password + "\n");
							UserAccount account = AccountManager.signup(username, password);
							if(account == null){
								sf.textArea.append("Add user request failed: " + username + "\n");
							}
							else{
								sf.textArea.append("Add user request success: " + username + "\n");
							}
							String serializingString = (account == null) ? "error" : account.toString();
							this.output.println("signup::" + serializingString);
						}
						else if(line.startsWith("login::")) {
							//do login::username:sean&password:pass
							line = line.substring(7);
							String pairs[] = line.split("&");
							String username = pairs[0].split(":")[1];
							String password = pairs[1].split(":")[1];
							String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
							UserAccount account = AccountManager.loginAccount(username, password);
							sf.textArea.append("User authentication request: " + username + " " + "pass: " + hashedPassword + "\n");
							String serializingString = (account == null) ? "error" : account.toString();
							this.output.println("login::" + serializingString);
							if(account != null){
								sf.textArea.append(sf.textArea.getText() + "User authentication success: " + username + "\n");
							}
							else{
								sf.textArea.append(sf.textArea.getText() + "User authentication failed: " + username + "\n");
							}
							
						}
						else if(line.startsWith("userfiles::")){
							line = line.substring(11);
							UserAccount session = UserAccount.fromString(line);
							List<DatabaseFile> files = DatabaseFile.getFilesByUserId(session.getId());
							StringBuffer serializedFileList = new StringBuffer();
							for(DatabaseFile file : files) {
								serializedFileList.append(file.getFileName() + "&");
							}
							if(serializedFileList.length() != 0) {
								serializedFileList.setLength(serializedFileList.length() - 1); //remove last &
							}
							this.output.println("userfiles::" + serializedFileList.toString());
						}
						else if(line.startsWith("openFile::")) {
							line = line.substring(10);
							String arr[] = line.split("&"); //arr[0] -> filename, arr[1] -> userId
							if(arr.length != 2) {
								//malformed request
							}
							Integer userId = Integer.parseInt(arr[1].split(":")[1]);
							String filename = arr[0].split(":")[1];
							DatabaseFile file = DatabaseFile.getFileByName(filename, userId);
							String fileData = file.getFileData();
							sf.textArea.append("Open file [" + filename + "] request." +  "\n");
							this.output.println("fileopen::filename:" + filename + "&data:"+fileData);
						}
						else if(line.startsWith("saveFile::")) {
							line = line.substring(10);
							if(reader.ready()){
								String g = reader.readLine();
								g = line + "\n" + g + "\n";
								String m = "";
								while(reader.ready()){
									m = reader.readLine();
									g = g + m + "\n";
								}
								g = g.trim();
								String arr1[] = g.split("&");
								if(arr1.length != 3) {
									this.output.println("file::error");
								}
								DatabaseFile file = DatabaseFile.deserialize(g);
								if(file==null){
									//
								}
								file.save();
								this.output.println("file::" + file.serialize());
								String filename = arr1[0].split(":")[1];
								if(!filename.endsWith(".txt")){
									sf.textArea.append("File [" + filename + ".txt] saved successfully." +  "\n");
								}
								else{
									sf.textArea.append("File [" + filename + "] saved successfully." +  "\n");
								}
							}
							else{
								String arr[] = line.split("&");
								if(arr.length != 3) {
									this.output.println("file::error");
								}
								DatabaseFile file = DatabaseFile.deserialize(line);
								if(file==null){
									//this.output.println("file::error");
								}
								file.save();
								output.flush();
								this.output.println("file::" + file.serialize());
								String filename = arr[0].split(":")[1];
								if(!filename.endsWith(".txt")){
									sf.textArea.append("File [" + filename + ".txt] saved successfully." +  "\n");
								}
								else{
									sf.textArea.append("File [" + filename + "] saved successfully." +  "\n");
								}
							}
						}
					}
				}
				catch(IOException ioe) {
					ioe.printStackTrace();
					return;
				}
			}
		}
		
	}
	
	private class ServerRunnable implements Runnable {
		//this class listens for new clients, and connects with them
		private ServerSocket ss;
		public ServerRunnable(ServerSocket ss) {
			this.ss = ss;
		}
		@Override
		public void run() {
			try {
				while(true) {
					Socket socket = ss.accept();
					openSockets.add(socket);
					new Thread(new ServerListener(socket)).start();
				}
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public Server() throws IOException {
		configData = new ConfigParser("server.config").parse();
		ss = new ServerSocket(configData.portNumber);
		openSockets = new Vector<Socket>();
		sf = new ServerFrame(ss);
		sf.setVisible(true);
		new Thread(new ServerRunnable(ss)).start();
	}
	
		
	public static void main(String [] args) {
		try {
			new Server();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
