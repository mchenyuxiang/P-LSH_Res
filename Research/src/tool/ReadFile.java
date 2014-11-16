package tool;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.TreeMap;

import javax.imageio.stream.FileImageInputStream;

public class ReadFile {
	public static Integer[][] Read(String FileName,Integer[][] base){
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(FileName)));
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
	
	public static TreeMap Read(String FileName,TreeMap treeMap){
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(FileName)));
			String line = br.readLine();
			int i = 0;
			while(line != null){
				i++;
				treeMap.put(i, line);
				line = br.readLine();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return treeMap;
	}
	
	public static TreeMap Read1(String FileName,TreeMap treeMap){
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(FileName)));
			String line = br.readLine();
			while(line != null){
				treeMap.put(line.split(",")[0], line.split(",")[1]);
				line = br.readLine();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return treeMap;
	}
	
	public static ArrayList<String> Read(String FileName,ArrayList<String> arraylist){
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(FileName)));
			String line = br.readLine();
			while(line != null){
				arraylist.add(line);
				line = br.readLine();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return arraylist;
	}
	
}
