package LSH;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
/**
 * 讲电影的分类得出来，进行分类
 */
public class testFile {
	public static void main(String[] args) throws IOException {
		String baseFile = "F:\\研究学习\\研究生论文\\小论文\\数据集\\ml-1m\\ml-1m\\r1.test";
		String FileName = "F:\\研究学习\\研究生论文\\小论文\\experimentResult\\u.txt";
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(baseFile)));
		BufferedWriter bw = new BufferedWriter(new FileWriter(FileName));
		String line = null;
		String[] st = null;
		while((line = br.readLine()) != null){
			st = line.split("\\:\\:");
			String str = "";
			for(int i=5 ; i < st.length; i++){
				if(i == 5){
					str = str + st[i];
				}else{
					str = str + "," + st[i];
				}
			}
//			bw.write(str);
//			bw.newLine();
//			bw.flush();
			System.out.println(str);
		}
	}
}
