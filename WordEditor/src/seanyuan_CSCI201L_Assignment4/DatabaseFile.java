package seanyuan_CSCI201L_Assignment4;

import java.io.Serializable;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.serial.SerialBlob;

public class DatabaseFile implements Serializable {
	private static final String SELECT_BY_USER_ID = "SELECT * FROM word_file WHERE user_id = ?";
	private static final String SELECT_BY_FILENAME = "SELECT * FROM word_file WHERE file_name = ? AND user_id = ?";
	private static final String INSERT_FILE = "INSERT INTO word_file (user_id, file_name, file_data) VALUES (?, ?, ?)";
	private static final String UPDATE_FILE = "UPDATE word_file SET file_name=?, file_data=?, user_id=? where id=?";
	
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
	
	private Integer id;
	private String fileName;
	private String fileData;
	private Integer userId;
	
	public DatabaseFile(String fileName, String fileData, Integer userId) {
		super();
		this.fileName = fileName;
		this.fileData = fileData;
		this.userId = userId;
	}
	
	public DatabaseFile(int id, String fileName, String fileData, Integer userId) {
		this(fileName, fileData, userId);
		this.id = id;
	}
	
	public static DatabaseFile getFileByName(String fileName, Integer userId) {
		try {
			PreparedStatement ps = dbConnection.prepareStatement(SELECT_BY_FILENAME);
			ps.setString(1, fileName);
			ps.setInt(2,  userId);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				Blob fileData = rs.getBlob("file_data");
				byte[] arr = fileData.getBytes(1, (int)fileData.length());
				Integer fileId = rs.getInt("file_id");
				return new DatabaseFile(fileId, fileName, new String(arr), userId);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static List<DatabaseFile> getFilesByUserId(Integer userId) {
		try {
			PreparedStatement ps = dbConnection.prepareStatement(SELECT_BY_USER_ID);
			ps.setInt(1, userId);
			ResultSet rs = ps.executeQuery();
			List<DatabaseFile> files = new ArrayList<DatabaseFile>();
			while(rs.next()) {
				Integer id = rs.getInt("file_id");
				String fileName = rs.getString("file_name");
				Blob fileData = rs.getBlob("file_data");
				byte[] arr = fileData.getBytes(1, (int)fileData.length());
				files.add(new DatabaseFile(id, fileName, new String(arr), userId));
			}
			return files;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public void save() {
		if(this.id != null) {
			return;
		}
		if(getFileByName(this.fileName, this.userId) != null){
			this.update();
		}
		else{
			PreparedStatement ps = null;
			try {
				ps = dbConnection.prepareStatement(INSERT_FILE);
				ps.setInt(1, this.userId);
				ps.setString(2, this.fileName);
				//Blob blob = dbConnection.createBlob();
				Blob blob=null; 
				byte[] byteConent = fileData.getBytes();
				blob=new SerialBlob(byteConent);
				//blob.setBytes(1, this.fileData.getBytes());
				ps.setBlob(3, blob);
				ps.executeUpdate();
				ResultSet generatedKeys = ps.getGeneratedKeys();
				if(generatedKeys.next()) {
					this.id = generatedKeys.getInt(1);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void update() {
		if(this.id == null) {
			return;
		}
		try {
			PreparedStatement ps = dbConnection.prepareStatement(UPDATE_FILE);
			ps.setString(1, this.fileName);
			Blob blob = dbConnection.createBlob();
			blob.setBytes(1, this.fileData.getBytes());
			ps.setBlob(2, blob);
			ps.setInt(3, this.userId);
			ps.setInt(4, this.id);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFileData() {
		return this.fileData;
	}
	public void setFileData(String fileData) {
		this.fileData = fileData;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public int getId() {
		return id;
	}
	
	public String serialize() {
		String basicInfo = "filename:" + this.fileName + 
				"&fileData:" + DatabaseFile.serializeFileData(this.fileData) 
				+ "&userId:" + this.userId;
		if(this.id != null) {
			return basicInfo + "&id:" + this.id;
		}
		return basicInfo;
	}
	
	public static DatabaseFile deserialize(String serializedData) {
		String arr[] = serializedData.split("&");
		if(arr.length == 3){
			String filename = arr[0].split(":")[1];
			String fileData = deserializeFileData(arr[1].split(":")[1]);
			Integer userId = Integer.parseInt(arr[2].split(":")[1]);
			return new DatabaseFile(filename, fileData, userId);
		}
		if(arr.length == 4){
			String filename = arr[0].split(":")[1];
			String fileData = deserializeFileData(arr[1].split(":")[1]);
			Integer userId = Integer.parseInt(arr[2].split(":")[1]);
			Integer id = Integer.parseInt(arr[3].split(":")[1]);
			return new DatabaseFile(id, filename, fileData, userId);
		}
		else {
			//malformed
			System.out.println(arr.length + "doesnt equal 3 or 4");
			return null;
		}
	}
	
	public static String serializeFileData(String fileData) {
		return fileData.replace("&", "U+0026")
				.replace(":", "U+003A");
	}
	
	public static String deserializeFileData(String fileData) {
		return fileData.replace("U+0026", "&")
				.replace("U+003A", ":");
	}
}
