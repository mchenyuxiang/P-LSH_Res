package LSH;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.Vector;

import org.omg.CORBA.Current;

import tool.*;

/*
 * 基于p-stable LSH程序
 * 
 * 程序组成部分：
 * 1、hash函数；2、特征向量；  
 * 
 * */

public class stableLSH {
//	static int dimention = 3952;// 维度
	static int dimention = 1682;// 维度
	/**
	 * 特征有多少行
	 */
//	static int hashcount = 6040;
	static int hashcount = 943;
	/**
	 *  哈希桶的数量
	 */
	static int hashbucketcount = 5;
	

	public static void main(String[] args) throws IOException, InterruptedException {
		long startTime = System.currentTimeMillis();
		double[][] a = new double[hashcount][dimention];// p-Stable分布（L=2；高斯分布）的随机向量
		double w = 100.0;// LSH的w

		
		/**
		 * 对用户评分进行分类
		 */
//		String baseFile1 = "F:/研究学习/研究生论文/小论文/experimentResult/1M/BaseMatric1M.txt";
		String baseFile1 = "./data/BaseMatric100k.txt";
		
		/**
		 * 与用户相似用户有哪些
		 */
//		String matuser = "F:/研究学习/研究生论文/小论文/experimentResult/1M/matuser1M.txt";
		String matuser = "./data/matuser100k.txt";
		
		/**
		 * 对电影进行分类
		 */
//		String baseFile1 = "F:\\研究学习\\研究生论文\\小论文\\experimentResult\\u.txt";
//		String matuser = "F:\\研究学习\\研究生论文\\小论文\\experimentResult\\matmovie.txt";
		
		/**
		 * 
		 */
//		Map<Integer, ArrayList<String>> map = new TreeMap<Integer, ArrayList<String>>();
		/**
		 * 用户存放在哪些桶中
		 */
		Map<String, ArrayList<String>> indexusermap = new TreeMap<String, ArrayList<String>>();
		/**
		 * 桶中存放哪些用户
		 */
		Map<String, ArrayList<String>> indexbucketmap = new TreeMap<String, ArrayList<String>>();

		// usermap记录将所有桶中的用户放入到一个矩阵中
		Map<String, ArrayList<String>> usermap = new TreeMap<>();
		
		// 存放向量和名称
		TreeMap<String, String> userfeature = new TreeMap<String, String>();

		/*
		 * 得到用户特征
		 */
		long begin = System.currentTimeMillis();
		ReadFile.Read2(baseFile1, userfeature);
		System.out.println("-----------read file use:"+(System.currentTimeMillis()-begin));
		
		/**
		 * 产生hashcount个哈希表
		 */
		begin = System.currentTimeMillis();
		for (int j = 0; j < hashbucketcount; j++) {
			for (int k = 0; k < dimention; k++) {
				Random r = new Random();
//				a[j][k] = NormalRandom(0, 1, -1.5, 1.5);
//				a[j][k] = Math.abs(200*r.nextGaussian())+100;
				a[j][k] = r.nextGaussian();
//				System.out.println(a[j][k]);
//				Thread.sleep(5000);
			}
		}
		
		
		
		System.out.println("-----------init table2:"+(System.currentTimeMillis()-begin));
		begin = System.currentTimeMillis();
		for (int i = 1; i <= hashcount; i++) {
//			Vector test = new Vector<>(hashbucketcount,hashbucketcount);
			double b = Math.random() * w;// LSH的随机数b
			String key = null;
//			Vector test = new Vector<>(hashbucketcount,hashbucketcount);
			int sum = 0;
			for (int l = 0; l < hashbucketcount; l++)// 每一个特征的hashcount个key
			{
				
				
				Integer hash_num = hashfamily(userfeature.get(i), a[l], b, w);// 哈希
//				test.add(hash_num);
//				sum +=hash_num;
				key = String.valueOf(hash_num);
//				tempkey = tempkey+hash_num;
//				sum += hashKey;
				// 用户落在哪些桶中索引建立
				insertBucket(indexusermap,indexbucketmap,i,key);
			}
//			key = String.valueOf(sum % (Math.pow(2, 32) - 1)); 
//			key = test.toString();
//			insertBucket(indexusermap,indexbucketmap,i,key);
//			key = sum / hashbucketcount;

		}
		/**
		 * 程序开始
		 * */
		System.out.println("-----------put in hashton:"+(System.currentTimeMillis()-begin));
		begin = System.currentTimeMillis();
		long startTimeTemp = System.currentTimeMillis();
		for (int keyindex = 1; keyindex <= hashcount; keyindex++) {
			

			/**
			 * 取出桶中用户
			 */
			ArrayList<String> sbucket = indexusermap.get(String.valueOf(keyindex));
			/**
			 * 与keyindex用户在同一个桶中的用户
			 */
			ArrayList<String> userinfo = new ArrayList<>();

			/**
			 * 与39号用户相似的用户有哪些，存入userinfo中
			 * */
			for (int i = 0; i < sbucket.size(); i++) {
				ArrayList<String> is = indexbucketmap.get(sbucket.get(i));
				for (int j = 0; j < is.size(); j++) {
					if (exist(userinfo, is.get(j))) {
						userinfo.add(is.get(j));
					}
				}
			}

			/**
			 * 取出与39号用户相似的所有用户的评分，组成新的usermap矩阵
			 * */
			usermap.put(String.valueOf(keyindex), userinfo);

			/**
			 * 将usermap中同一列全部为0的列删除掉，得到最终需要填充的矩阵users
			 * */


			long endTimeTemp = System.currentTimeMillis();
			long onceTime = endTimeTemp - startTimeTemp;
			System.out.println("第" + keyindex + "个用户");
			System.out.println("运行时间： " + onceTime + "ms");
		}
		System.out.println("-----------count result:"+(System.currentTimeMillis()-begin));
		WriteFile.Write(matuser, usermap);
//		WriteFile.Write(Treemap, userinfo);
		handleFile.handle(hashcount);
		long endTime = System.currentTimeMillis();
		long alltime = endTime - startTime;
		System.out.println("运行时间： " + alltime + "ms");

	}

	/**
	 * 插入桶内
	 * @param indexusermap
	 * @param indexbucketmap
	 * @param i
	 * @param key
	 */
	public static void insertBucket(Map<String, ArrayList<String>> indexusermap,
			Map<String, ArrayList<String>> indexbucketmap,int i,String key){
		if (indexusermap.containsKey(String.valueOf(i))
				&& exist(indexusermap.get(String.valueOf(i)), key)) {
			indexusermap.get(String.valueOf(i)).add(key);
		} else if (!indexusermap.containsKey(String.valueOf(i))) {
			indexusermap.put(String.valueOf(i), alist(key));
		}
		
		// 一个桶中包含哪些用户索引建立
		if (indexbucketmap.containsKey(key)
				&& exist(indexbucketmap.get(key), String.valueOf(i))) {
			indexbucketmap.get(key).add(String.valueOf(i));
		} else if (!indexbucketmap.containsKey(key)) {
			indexbucketmap.put(key, alist(String.valueOf(i)));
		}
	}
	// 将二维数组一行用String返回
	public static String arrayTostring(Integer[] base) {
		String sb = "";
		for (int i = 0; i < base.length; i++) {
			if (i != base.length - 1) {
				sb = sb + base[i] + ",";
			} else {
				sb = sb + base[i];
			}
		}
		return sb;
	}

	// 判断在一个key中时候有相同的value值
	public static boolean exist(ArrayList<String> str1, String str2) {
		for (int i = 0; i < str1.size(); i++) {
			if (str1.get(i).equals(str2)) {
				return false;
			}
		}
		return true;
	}

	// 添加value
	public static ArrayList<String> alist(String s) {
		ArrayList<String> arr = new ArrayList<String>();
		arr.add(s);
		return arr;
	}

	public static void printlist(ArrayList<Integer> list) {
		for (int i = 0; i < list.size(); i++) {
			if (i != list.size() - 1) {
				System.out.print(list.get(i) + ",");
			} else {
				System.out.println(list.get(i));
			}
		}
	}

	public static void printNum(Map<Integer, ArrayList<String>> map) {
		for (Object o : map.keySet()) {
			System.out.println("行数：" + map.size());
			System.out.println("列数：" + map.get(o).size());
			break;
		}
	}

	// 打印hash桶中的各个值
	public static void print1(Map<Integer, ArrayList<String>> map) {
		for (Object o : map.keySet()) {
			System.out.println("个数：" + map.get(o).size() + " : " + o + " : "
					+ map.get(o));
		}
	}

	public static void print3(Map<Integer, ArrayList<String>> map) {
		for (Object o : map.keySet()) {
			System.out.println(map.get(o));
		}
	}

	// 打印hash桶中的各个值
	public static void print2(Map<Integer, ArrayList<String>> map) {
		for (Object o : map.keySet()) {
			for (int i = 0; i < map.get(o).size(); i++) {
				System.out.println(i + " : " + map.get(o).get(i));
			}
		}
	}

	// 打印hash桶中的各个值
	public static void print(Map<Integer, ArrayList<String>> map)
			throws IOException {
		for (Object o : map.keySet()) {
			String lshtestFile = "F:\\研究学习\\研究生论文\\小论文\\experimentResult\\lsh\\"
					+ o + ".txt";
			BufferedWriter lshtestFileOut = new BufferedWriter(new FileWriter(
					lshtestFile));
			for (int j = 0; j < map.get(o).size(); j++) {
				if (j != map.get(o).size() - 1) {
					lshtestFileOut.write(map.get(o).get(j) + "");
				} else {
					lshtestFileOut.write(map.get(o).get(j) + "");
				}
				lshtestFileOut.newLine();
			}
			lshtestFileOut.flush();
		}
	}

	/**
	 *  平均分布
	 * @param min
	 * @param max
	 * @return
	 */
	public static double AverageRandom(double min, double max) {
		int randInteger = (int) (Math.random() * 10000);
		double resultInteger = randInteger * (max - min);
		return resultInteger / 10000.0 + min;
	}

	/**
	 *  概率密度函数
	 * @param x
	 * @param miu
	 * @param sigma
	 * @return
	 */
	public static double Normal(double x, double miu, double sigma) {
		return 1.0 / Math.sqrt(2 * Math.PI * sigma)
				* Math.exp(-1 * (x - miu) * (x - miu) / (2 * sigma * sigma));
	}

	/**
	 *  产生正态分布随机数
	 * @param miu
	 * @param sigma
	 * @param min
	 * @param max
	 * @return
	 */
	public static double NormalRandom(double miu, double sigma, double min,
			double max) {
		double x;
		double dScope;
		double y;
		do {
			x = AverageRandom(min, max);
			y = Normal(x, miu, sigma);
			dScope = AverageRandom(0, Normal(miu, miu, sigma));
		} while (dScope > y);
		return x;
	}

	/**
	 *  哈希函数
	 * @param feature
	 * @param a_temp
	 * @param b_temp
	 * @param w_temp
	 * @return
	 * @throws InterruptedException 
	 */
	public static int hashfamily(String feature, double[] a_temp, double b_temp,
			double w_temp) throws InterruptedException {
		String[] a = feature.split(",");
		double result = b_temp;
		for (int i = 0; i < dimention; i++) {
			double atest = 0.0;
			atest = Double.parseDouble(a[i]);

			result += Math.abs(atest *10000* (a_temp[i]));
//			System.out.println(a[i] + " : " +a_temp[i]);
		}
		return (int)Math.floor(Math.abs(result) / w_temp);// 返回哈希结果
	}
}
