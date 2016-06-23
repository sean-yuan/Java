package seanyuan_CSCI201L_Assignment4;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import tab.ServerFrame;

public class UserAccount {
	private static Connection dbConnection;
	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			dbConnection = DriverManager.getConnection("jdbc:mysql://localhost/WordEditorCloud?user=root&password=2157rMT*");
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	private int userId;
	private String username;
	private String password;
	
	public String getUsername() {
		return this.username;
	}
	
	public String getPassword() {
		return this.password;
	}
	
	public Integer getId() {
		return this.userId;
	}
	
	public UserAccount (int u_id, String uname, String pass) {
		this.userId = u_id;
		this.username = uname;
		this.password = pass;
	}
	
	public UserAccount (String uname) {
		this.username = uname;
	}
	
	public static UserAccount authenticate(String username, String unhashedPassword) {
		UserAccount user = findByUsername(username);
		if(user == null) {
			return null;
		}
		if(BCrypt.checkpw(unhashedPassword, user.getPassword())) {
			return user;
		}
		return null;
	}
	
	public static UserAccount findById(int userId) {
		try {
			PreparedStatement ps = dbConnection.prepareStatement("SELECT * FROM word_user WHERE user_id = ?");
			ps.setInt(1, userId);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				String username = rs.getString("username");
				String password = rs.getString("password");
				return new UserAccount(userId, username, password);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static UserAccount findByUsername(String username) {
		try {
			PreparedStatement ps = dbConnection.prepareStatement("SELECT * FROM word_user WHERE username = ?");
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				Integer userId = rs.getInt("user_id");
				String password = rs.getString("password");
				return new UserAccount(userId, username, password);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;		
	}
	
	public static UserAccount createAccount(String username, String hashedPassword) {
		try {
			PreparedStatement ps = dbConnection.prepareStatement("INSERT INTO word_user (username, password) VALUES (?, ?)");
			ps.setString(1, username);
			ps.setString(2, hashedPassword);
			ps.executeUpdate();
			return findByUsername(username);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public String toString() {
		return "username=" + this.getUsername() + "&password=" + this.getPassword() + "&id=" + this.getId();
	}
	
	public static UserAccount fromString(String serialized) {
		String parts[] = serialized.split("&");
		if(parts.length != 3) {
			throw new IllegalArgumentException("Incorrect serialization of UserAccount class");
		}
		String username[] = parts[0].split("=");
		String password[] = parts[1].split("=");
		String id[] = parts[2].split("=");
		if(username.length != 2 || password.length != 2 || id.length != 2) {
			throw new IllegalArgumentException("Incorrect serialization of UserAccount class");
		}
		return new UserAccount(Integer.parseInt(id[1]), username[1], password[1]);
	}
}
