package tool;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class WriteFile {
	public static void Write(String FileName,Integer[][] base,int hashcount,int dimention){
		try {
			BufferedWriter baseOut = new BufferedWriter(new FileWriter(FileName));
			for(int i = 0; i < hashcount; i++){
				for(int j = 0; j < dimention; j++){
					if(j != 1681)
						baseOut.write(base[i][j] + ",");
					else
						baseOut.write(base[i][j] + "");
				}
				baseOut.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void Write(String FileName,Map<Integer, ArrayList<String>> map) throws IOException{
		BufferedWriter lshtestFileOut = new BufferedWriter(new FileWriter(FileName));
		for(Object o : map.keySet()){ 
			try {
//				lshtestFileOut.write(o + ":");
				for(int j = 0; j < map.get(o).size(); j++){
					if(j != map.get(o).size()-1){
						lshtestFileOut.write(map.get(o).get(j) + ",");
					}
					else{
						lshtestFileOut.write(map.get(o).get(j) + "");
					}
				}
				lshtestFileOut.newLine();
				lshtestFileOut.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}

	
	public static void Write(String FileName,TreeMap<Integer, Integer> map) throws IOException{
		BufferedWriter lshtestFileOut = new BufferedWriter(new FileWriter(FileName));
		for(Object o : map.keySet()){ 
			try {
				lshtestFileOut.write(o + "," + map.get(o));
				lshtestFileOut.newLine();
				lshtestFileOut.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void Write1(String FileName,Map<Integer, ArrayList<String>> users) throws IOException{
		BufferedWriter lshtestFileOut = new BufferedWriter(new FileWriter(FileName));
		for(Object o : users.keySet()){ 
			try {
				lshtestFileOut.write((int) o);
				lshtestFileOut.newLine();
				lshtestFileOut.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
