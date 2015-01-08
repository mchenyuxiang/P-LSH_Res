package tool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import javax.imageio.stream.FileImageInputStream;

public class ReadFile {
	public static Integer[][] Read(String FileName,Integer[][] base){
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(FileName)));
			String line = br.readLine();
			while(line != null){
				String[] str = line.split(",");
				base[Integer.parseInt(str[0])-1][Integer.parseInt(str[1])-1] = Integer.parseInt(str[2]);
				line = br.readLine();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		return base;
	}
}
