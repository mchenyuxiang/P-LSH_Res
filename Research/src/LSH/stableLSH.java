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
import java.util.TreeMap;

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
	static int dimention = 1682;// 维度
	static int hashcount = 943;// 特征有多少行
	static int hashbucketcount = 5;// 哈希桶的数量
//	static int dimention = 19;// 维度
//	static int hashcount = 1682;// 特征有多少行
//	static int hashbucketcount = 5;// 哈希桶的数量

	public static void main(String[] args) throws IOException {
		long startTime = System.currentTimeMillis();
		double[][] a = new double[hashcount][dimention];// p-Stable分布（L=2；高斯分布）的随机向量
		double w = 150;// LSH的w
		double b = Math.random() * 100 / (w);// LSH的随机数b
		
		/**
		 * 对用户评分进行分类
		 */
		String baseFile1 = "F:\\研究学习\\研究生论文\\小论文\\experimentResult\\BaseMatric.txt";
		String matuser = "F:\\研究学习\\研究生论文\\小论文\\experimentResult\\matuser.txt";
//		String Treemap = "F:\\研究学习\\研究生论文\\小论文\\experimentResult\\userinfo.txt";
		
		/**
		 * 对电影进行分类
		 */
//		String baseFile1 = "F:\\研究学习\\研究生论文\\小论文\\experimentResult\\u.txt";
//		String matuser = "F:\\研究学习\\研究生论文\\小论文\\experimentResult\\matmovie.txt";
		
		// 存放向量和名称
		Map<Integer, ArrayList<String>> map = new TreeMap<Integer, ArrayList<String>>();
		// 用户存进了哪些桶中
		Map<Integer, ArrayList<String>> indexusermap = new TreeMap<Integer, ArrayList<String>>();
		// 一个桶中存了哪些用户
		Map<Integer, ArrayList<String>> indexbucketmap = new TreeMap<Integer, ArrayList<String>>();

		// usermap记录将所有桶中的用户放入到一个矩阵中
		Map<Integer, ArrayList<String>> usermap = new TreeMap<>();
		
		// 存放向量和名称
		TreeMap<Integer, String> userfeature = new TreeMap<Integer, String>();

		/*
		 * 得到用户特征
		 */
		ReadFile.Read2(baseFile1, userfeature);



		/**
		 * 程序开始
		 * */
		for (int keyindex = 1; keyindex <= hashcount; keyindex++) {
			long startTimeTemp = System.currentTimeMillis();
			/**
			 * 产生hashcount个哈希表
			 */
			for (int j = 0; j < hashcount; j++) {
				for (int k = 0; k < dimention; k++) {
					a[j][k] = NormalRandom(0, 1, -1.5, 1.5);
				}
			}



			for (int i = 1; i <= hashcount; i++) {
				for (int l = 0; l < hashbucketcount; l++)// 每一个特征的hashcount个key
				{
					int hash_num = hashfamily(userfeature.get(i), a[l][0], b, w);// 哈希
					int key = (int) (hash_num / w);// 哈希表的key

					// 哈希存储
					if (map.containsKey(key)
							&& exist(map.get(key), userfeature.get(i))) {
						map.get(key).add(userfeature.get(i));
					} else if (!map.containsKey(key)) {
						map.put(key, alist(userfeature.get(i)));
					}

					// 用户落在哪些桶中索引建立
					if (indexusermap.containsKey(i)
							&& exist(indexusermap.get(i), String.valueOf(key))) {
						indexusermap.get(i).add(String.valueOf(key));
					} else if (!indexusermap.containsKey(i)) {
						indexusermap.put(i, alist(String.valueOf(key)));
					}

					// 一个桶中包含哪些用户索引建立
					if (indexbucketmap.containsKey(key)
							&& exist(indexbucketmap.get(key), String.valueOf(i))) {
						indexbucketmap.get(key).add(String.valueOf(i));
					} else if (!indexbucketmap.containsKey(key)) {
						indexbucketmap.put(key, alist(String.valueOf(i)));
					}
				}
			}

			// sbucket取出39号用户在哪些桶中
			ArrayList<String> sbucket = indexusermap.get(keyindex);
			// userinfo为存放与39号用户同在一个桶中的用户列表
			ArrayList<String> userinfo = new ArrayList<>();

			/**
			 * 与39号用户相似的用户有哪些，存入userinfo中
			 * */
			for (int i = 0; i < sbucket.size(); i++) {
				ArrayList<String> is = indexbucketmap.get(Integer
						.parseInt(sbucket.get(i)));
				for (int j = 0; j < is.size(); j++) {
					if (exist(userinfo, is.get(j))) {
						userinfo.add(is.get(j));
					}
				}
			}

			/**
			 * 取出与39号用户相似的所有用户的评分，组成新的usermap矩阵
			 * */
			usermap.put(keyindex, userinfo);

			/**
			 * 将usermap中同一列全部为0的列删除掉，得到最终需要填充的矩阵users
			 * */


			long endTimeTemp = System.currentTimeMillis();
			long onceTime = endTimeTemp - startTimeTemp;
			System.out.println("第" + keyindex + "个用户");
			System.out.println("运行时间： " + onceTime + "ms");
		}
		WriteFile.Write(matuser, usermap);
//		WriteFile.Write(Treemap, userinfo);
		handleFile.handle(hashcount);
		long endTime = System.currentTimeMillis();
		long alltime = endTime - startTime;
		System.out.println("运行时间： " + alltime + "ms");

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

	// 平均分布
	public static double AverageRandom(double min, double max) {
		int randInteger = (int) (Math.random() * 10000);
		double resultInteger = randInteger * (max - min);
		return resultInteger / 10000.0 + min;
	}

	// 概率密度函数
	public static double Normal(double x, double miu, double sigma) {
		return 1.0 / Math.sqrt(2 * Math.PI * sigma)
				* Math.exp(-1 * (x - miu) * (x - miu) / (2 * sigma * sigma));
	}

	// 产生正态分布随机数
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

	// 哈希函数
	public static int hashfamily(String feature, double a_temp, double b_temp,
			double w_temp) {
		String[] a = feature.split(",");
		double result = b_temp;
		for (int i = 1; i < dimention; i++) {
			result += Integer.parseInt(a[i]) * ((a_temp + i));
		}
		return (int) (result / w_temp);// 返回哈希结果
	}
}
