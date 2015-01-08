package LSH;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import sun.reflect.generics.tree.Tree;
import tool.ReadFile;

public class test {
	public static void main(String[] args) throws IOException {
		/**
		 * 对用户评分进行分类
		 */
//		String baseFile1 = "./data/ua.base";
		String baseFile1 = "./data/ra.train";
		// 存放向量和名称
		TreeMap<Integer, ArrayList<Integer>> userfeature = new TreeMap<Integer, ArrayList<Integer>>();
		BufferedReader br = new BufferedReader(new FileReader(new File(baseFile1)));
		String line = "";
		String temp = "";
		ArrayList<Integer> tree = null;
		while((line=br.readLine()) != null){
			if(!temp.equals(line.split(",")[0])){
				temp = line.split(",")[0];
				tree = new ArrayList<>();
				tree.add(Integer.parseInt(line.split(",")[1]));
				userfeature.put(Integer.parseInt(temp), tree);
			}else{
				Integer t = Integer.parseInt(line.split(",")[1]);
				tree.add(t);
			}
		}
//		for(int i=1;i<=943;i++){
//			System.out.println(userfeature.get(i).size());
//			
//		}
		int max = 0;
		/**
		 * 相交个数
		 */
		int n = 0;

		int maxNumber = 0;
		int m = 0;
		double rate = 0.0;
		double rateMax = 0.0;
		for(int i=1;i<=9040;i++){
			
			ArrayList<Integer> tset = new ArrayList<>();
			ArrayList<Integer> tt = null; 
			tset = userfeature.get(i);
			for(int j=1;j<=9040;j++){
				n = 0;
				if(j!=i){
					tt = new ArrayList<>();
					tt = userfeature.get(j);
					for(int ts=0; ts<tset.size();ts++){
						if(tt.contains(tset.get(ts))){
							n++;
						}
					}
					rate = (double)n/(userfeature.get(i).size()+userfeature.get(j).size()-n);
					if(rate > rateMax){
						rateMax = rate;
						m = i;
						max = j;
						maxNumber = n;
					}
				}else{
					continue;
				}
			}
		}
		System.out.println(maxNumber);
		System.out.println(rateMax);
		System.out.println(m+" : "+userfeature.get(m).size());
		System.out.println(max+" : "+userfeature.get(max).size());
		System.out.println(userfeature.get(m));
		System.out.println(userfeature.get(max));
	}
}
