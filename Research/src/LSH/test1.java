package LSH;

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

import tool.ReadFile;

public class test1 {
	public static void main(String[] args) throws IOException {
		//存放向量和名称
		TreeMap<Integer, String> map = new TreeMap<Integer, String>();
		
		String baseFile = "F:\\研究学习\\研究生论文\\小论文\\experimentResult\\BaseMatric.txt";
//		ReadFile.Read2(baseFile, map);
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(baseFile)));
		String line = null;
		int i=1;
		while((line = br.readLine()) != null){
			map.put(i, line);
			i++;
		}
		for(Object o:map.keySet()){
			System.out.println(o +";" +map.get(o));
		}
	}
}
