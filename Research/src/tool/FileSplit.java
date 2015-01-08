package tool;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class FileSplit {
	public static void main(String[] args) throws IOException {
		//存放向量和名称
		TreeMap<Integer, String> map = new TreeMap<Integer, String>();
		
//		String baseFile = "F:/研究学习/研究生论文/小论文/数据集/ml-1m/ratings.dat";
		String baseFile = "F:/研究学习/研究生论文/小论文/数据集/ml-1m/900000.txt";
//		ReadFile.Read2(baseFile, map);
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(baseFile)));
		
		String line = null;
		int i=0;
		File f = null;
		BufferedWriter bw = null;
		while((line = br.readLine()) != null){
			if(i%100000==0){
				if(bw!=null){
					bw.flush();
				}
				f = new File("F:/研究学习/研究生论文/小论文/数据集/ml-1m/"+i+".txt");
				bw = new BufferedWriter(new FileWriter(f));
			}
			bw.write(line+"\n");
			i++;
		}
	}
}
