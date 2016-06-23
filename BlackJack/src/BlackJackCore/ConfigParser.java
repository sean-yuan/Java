package BlackJackCore;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class ConfigParser {
	
	private BufferedReader reader; 
	private static final String SPLIT_STR = ":";
	public ConfigParser(String filename) {
		try {
			this.reader = new BufferedReader( new FileReader(new File(filename)) );
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public ConfigData parse() throws IOException {
		String line;
		Map<String, String> data = new HashMap<>();
		while((line = this.reader.readLine()) != null) {
			String arr[] = line.split(SPLIT_STR);
			if(arr.length != 2) {
				continue;
			}
			data.put(arr[0].trim(),  arr[1].trim());
		}
		return new ConfigData(data);
	}
	
}
