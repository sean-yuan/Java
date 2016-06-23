package seanyuan_CSCI201L_Assignment4;

import java.util.Map;

public class ConfigData {
	private static final String IP = "ip";
	private static final String PORT_NUM = "port";
	public String ipAddress;
	public int portNumber;
	
	public ConfigData(String ip, int port) {
		this.ipAddress = ip;
		this.portNumber = port;
	}
	
	public ConfigData(Map<String, String> data) {
		this.ipAddress = data.get(IP);
		String port = data.get(PORT_NUM);
		if(port != null) {
		this.portNumber = Integer.parseInt(port);
		}
	}
}

