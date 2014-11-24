package LSH;

import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;

import tool.ReadFile;
import tool.WriteFile;
/*
 * 处理相似用户文件，让文件方便导入到matlab里面
 */
public class handleFile {
	public static void handle(int n) throws IOException {
		TreeMap<Integer, String> userIndex = new TreeMap<Integer, String>();
		/**
		 * 用户评分
		 */
		String baseFile = "F:\\研究学习\\研究生论文\\小论文\\experimentResult\\matuser.txt";
		String matuser = "F:\\研究学习\\研究生论文\\小论文\\experimentResult\\MatricUser.txt";
		
		/**
		 * 电影聚类
		 */
//		String baseFile = "F:\\研究学习\\研究生论文\\小论文\\experimentResult\\matmovie.txt";
//		String matuser = "F:\\研究学习\\研究生论文\\小论文\\experimentResult\\MatricMovie.txt";
		ReadFile.Read2(baseFile, userIndex);
		int max = 0;
		for(int i=1; i <=n; i++){
			int length = userIndex.get(i).split(",").length;
			if(length > max){
				max = length;
			}
		}
//		System.out.println(max);
		/*
		 * 对不是最长的行补0
		 */
		for(int i=1; i <=n; i++){
			int length = userIndex.get(i).split(",").length;
			int k = max - length;
			if(k != 0){
				String str = userIndex.get(i);
				for(int j=length; j < max; j++){
					str = str + "," + 0;
				}
				userIndex.put(i, str);
			}
		}
//		for(Object o: userIndex.keySet()){
//			System.out.println(userIndex.get(o).split(",").length);
//		}

		WriteFile.Write1(matuser, userIndex);
	}
}
