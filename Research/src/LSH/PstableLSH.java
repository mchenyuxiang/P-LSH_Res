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

public class PstableLSH {
	static int dimention=1682;//维度
	static int hashcount=943;//特征有多少行
	static int hashbucketcount=10;//哈希桶的数量
	public static void main(String[] args) throws IOException {
		long startTime = System.currentTimeMillis();
		double[][] a = new double[hashcount][dimention];//p-Stable分布（L=2；高斯分布）的随机向量
		double w=50;//LSH的w
		double b=Math.random()*100/(w);//LSH的随机数b
		//最终填充矩阵评分数量少于num的列去掉
		int num = 5;
		//keyindex为追踪的用户，即为哪位用户进行推荐
		int keyindex = 40;
//		System.out.println(b);
		ArrayList<String> alist = new ArrayList<>();
		//存放向量和名称
		Map<Integer, ArrayList<String>> map = new TreeMap<Integer, ArrayList<String>>(); 
		//用户存进了哪些桶中
		Map<Integer, ArrayList<String>> indexusermap = new TreeMap<Integer, ArrayList<String>>(); 
		//一个桶中存了哪些用户
		Map<Integer, ArrayList<String>> indexbucketmap = new TreeMap<Integer, ArrayList<String>>(); 
		
		ArrayList<Integer> moive = new ArrayList<>();
		//users的map为最终需要填充的矩阵
		Map<Integer, ArrayList<String>> users = new TreeMap<>();
		
		ArrayList<String> userList = new ArrayList<>();
		//usermap记录将所有桶中的用户放入到一个矩阵中
		Map<Integer, ArrayList<String>> usermap = new TreeMap<>();
		//usernozreo记录在usermap中用户评分记录非零的位置
		Map<Integer, ArrayList<String>> usernozero = new TreeMap<>();


		
		//treemap定义为原矩阵列与对应列的关系
		TreeMap<Integer, Integer> treemap = new TreeMap<Integer, Integer>();
		
		/**
		 * 程序开始
		 * */
		
		//初始化二维数组
		Integer[][] base = new Integer[hashcount][dimention];
		Init.InitArray(base, hashcount, dimention, 0);
		
		//初始测试二维数组
		Integer[][] test = new Integer[hashcount][dimention];
		Init.InitArray(test, hashcount, dimention, 0);
		
		
//		/**
//		 * 初始化treemap，对应电影号
//		 * */
//		for(int i=0; i<dimention; i++){
//			treemap.put(i, i);
//		}
		
		/**
		 * 产生hashcount个哈希表
		 */
		for(int j=0;j<hashcount;j++)
		{
			for(int k=0;k<dimention;k++)
			{
				a[j][k]=NormalRandom(0,1,-1.5,1.5);
			}
		}
		
		

		
		String baseIn = "D:\\研究学习\\研究生论文\\小论文\\数据集\\ml-100k\\u1.base";
		ReadFile.Read(baseIn, base);
		
		String baseFile = "D:\\研究学习\\研究生论文\\小论文\\experimentResult\\baseout.txt";
		WriteFile.Write(baseFile, base, hashcount, dimention);
		
		String testFile = "D:\\研究学习\\研究生论文\\小论文\\数据集\\ml-100k\\u1.test";
		ReadFile.Read(testFile, test);
		
		String basetestFile = "D:\\研究学习\\研究生论文\\小论文\\experimentResult\\testout.txt";
		WriteFile.Write(basetestFile, test, hashcount, dimention);
		
		/**
		 * 将记录的每一个数组转换为一个字符串数组
		 */
		Map<Integer, String> userfeature = new TreeMap<>();
		String[] feature = new String[hashcount];
		for(int i=0; i < hashcount; i++){
			feature[i] = "";
			for(int j=0; j<dimention; j++){
				if(j == 0){
					feature[i] = base[i][j]+"";
				}
				else {
					feature[i] += "," + base[i][j]; 
				}
			}
			userfeature.put(i+1, feature[i]);
		}
		
		for(int i = 0; i < hashcount; i++) {
			for(int l=0;l<hashbucketcount;l++)//每一个特征的hashcount个key
			{
//				int hash_num=hashfamily(feature[i],a[l][0],b,w);//哈希
				int hash_num=hashfamily(userfeature.get(i+1),a[l][0],b,w);//哈希
				int key=(int) (hash_num/w);//哈希表的key
				
				//哈希存储
				if(map.containsKey(key) && exist(map.get(key),userfeature.get(i+1))) {
					map.get(key).add(userfeature.get(i+1));
				}else if(!map.containsKey(key)){
					map.put(key, alist(userfeature.get(i+1)));
				}
				
				//用户落在哪些桶中索引建立
				if(indexusermap.containsKey(i+1) && exist(indexusermap.get(i+1),String.valueOf(key))) {
					indexusermap.get(i+1).add(String.valueOf(key));
				}else if(!indexusermap.containsKey(i+1)){
					indexusermap.put(i+1, alist(String.valueOf(key)));
				}
				
				//一个桶中包含哪些用户索引建立
				if(indexbucketmap.containsKey(key) && exist(indexbucketmap.get(key),String.valueOf(i+1))) {
					indexbucketmap.get(key).add(String.valueOf(i+1));
				}else if(!indexbucketmap.containsKey(key)){
					indexbucketmap.put(key, alist(String.valueOf(i+1)));
				}
			}
		}
		
		//sbucket取出39号用户在哪些桶中
		ArrayList<String> sbucket = indexusermap.get(keyindex);
		//userinfo为存放与39号用户同在一个桶中的用户列表
		ArrayList<String> userinfo = new ArrayList<>();

		
		/**
		 * 与39号用户相似的用户有哪些，存入userinfo中
		 * */
		for(int i=0; i < sbucket.size(); i++) {
			ArrayList<String> is = indexbucketmap.get(Integer.parseInt(sbucket.get(i)));
			for(int j = 0; j < is.size(); j++) {
				if(exist(userinfo, is.get(j))){
					userinfo.add(is.get(j));
				}
			}
		}
//		System.out.println(userinfo);
		
		/**
		 * 取出与39号用户相似的所有用户的评分，组成新的usermap矩阵
		 * */
		for(int i=0; i < userinfo.size(); i++) {
			usermap.put(Integer.parseInt(userinfo.get(i)), alist(arrayTostring(base[Integer.parseInt(userinfo.get(i))])));
		}
		

		
		/**
		 * 将usermap中同一列全部为0的列删除掉，得到最终需要填充的矩阵users
		 * */
		int tempdimention = dimention;
		int cntt = 0;
		for(int i=0; i < dimention; i++) {
			Object ot;
			int flag = 0;
			int cnt = 0;
			for(Object o:usermap.keySet()){
//				System.out.println(o);
				if(!usermap.get(o).get(0).split(",")[i].equals(String.valueOf(0))){
					cnt ++;
					flag = 1;
					ot = o;
					/**
					 * 将每位用户评分不为0的电影记录下来
					 * */
					if(usernozero.containsKey(o) && exist(usernozero.get(o),String.valueOf(i))) {
						usernozero.get(o).add(String.valueOf(i));
					}else if(!usernozero.containsKey(o)){
						usernozero.put((Integer) o, alist(String.valueOf(i)));
					}
				}
			}
			if(flag == 1 && cnt > num){
				cntt ++;
				/**
				 * 更新treemap中电影和现存的位置数
				 */
				treemap.put(cntt, i);
				moive.add(i);
				for(Object o: usermap.keySet()){
					if(users.containsKey(o)) {
						users.get(o).add(usermap.get(o).get(0).split(",")[i]);
					}else if(!users.containsKey(o)){
						users.put((Integer) o, alist(usermap.get(o).get(0).split(",")[i]));
					}
				}

			}
		}
		for(Object o:users.keySet()){
			System.out.println(o);
		}
//		for(int i=1; i < treemap.size(); i++){
//			System.out.println(i + ":" + treemap.get(i));
//		}
		
//		Map<Integer, ArrayList<String>> tempmap = new TreeMap<>();
//		for(int i=0; i < treemap.size(); i++) {
//			for(Object o : users.keySet()){
//				if(tempmap.containsKey(o)) {
//					tempmap.get(o).add(usermap.get(o).get(0).split(",")[i]);
//				}else if(!tempmap.containsKey(o)){
//					tempmap.put((Integer) o, alist(usermap.get(o).get(0).split(",")[i]));
//				}
//			}
//		}
		
//		print3(usernozero);
//		printNum(users);
//		printlist(moive);
//		print1(tempmap);
		String indexuser = "D:\\研究学习\\研究生论文\\小论文\\experimentResult\\indexuser.txt";
		WriteFile.Write(indexuser, indexusermap);
		String bucketuser = "D:\\研究学习\\研究生论文\\小论文\\experimentResult\\bucketuser.txt";
		WriteFile.Write(bucketuser, indexbucketmap);
		String matuser = "D:\\研究学习\\研究生论文\\小论文\\experimentResult\\matuser.txt";
		WriteFile.Write(matuser, users);
		String Treemap = "D:\\研究学习\\研究生论文\\小论文\\experimentResult\\Treemap.txt";
		WriteFile.Write(Treemap, treemap);
//		String userListIn = "D:\\研究学习\\研究生论文\\小论文\\experimentResult\\userlist.txt";
//		WriteFile.Write1(userListIn, users);
		
		long endTime = System.currentTimeMillis();
		long alltime = endTime - startTime;
		System.out.println("运行时间： " + alltime + "ms");
//		System.out.println(treemap.size());
	}
	
	//将二维数组一行用String返回
	public static String arrayTostring(Integer[] base) {
		String sb = "";
		for(int i = 0; i < base.length; i++) {
			if(i != base.length - 1){
				sb = sb + base[i] + ",";
			}else{
				sb = sb + base[i];
			}
		}
		return sb;
	}
	
	//判断在一个key中时候有相同的value值
	public static boolean exist(ArrayList<String> str1,String str2) {
		for(int i = 0; i < str1.size(); i++) {
			if(str1.get(i).equals(str2)) {
				return false;
			}
		}
		return true;
	}
	
	//添加value
	public static ArrayList<String> alist(String s) {
		ArrayList<String> arr = new ArrayList<String>();
		arr.add(s);
		return arr;
	}
	
	public static void printlist(ArrayList<Integer> list) {
		for(int i=0; i < list.size(); i++){  
			if(i!=list.size()-1){
				System.out.print(list.get(i) + ",");
			}else{
				System.out.println(list.get(i));
			}
		}  
	}
	
	public static void printNum(Map<Integer, ArrayList<String>> map) {
		for(Object o: map.keySet()){
			System.out.println("行数："+ map.size());  
			System.out.println("列数："+ map.get(o).size());  
			break;
		}
	}
	
	//打印hash桶中的各个值
	public static void print1(Map<Integer, ArrayList<String>> map) {
		for(Object o : map.keySet()){  
		    System.out.println("个数："+ map.get(o).size() + " : " + o + " : " + map.get(o));  
		}  
	}
	
	public static void print3(Map<Integer, ArrayList<String>> map) {
		for(Object o : map.keySet()){  
		    System.out.println(map.get(o));  
		}  
	}
	
	//打印hash桶中的各个值
	public static void print2(Map<Integer, ArrayList<String>> map) {
		for(Object o : map.keySet()){  
			for(int i = 0; i < map.get(o).size(); i++){
				System.out.println(i + " : " + map.get(o).get(i));  
			}
		}  
	}
	
	//打印hash桶中的各个值
	public static void print(Map<Integer, ArrayList<String>> map) throws IOException {
		for(Object o : map.keySet()){  
			String lshtestFile = "D:\\研究学习\\研究生论文\\小论文\\experimentResult\\lsh\\"+ o +".txt";
			BufferedWriter lshtestFileOut = new BufferedWriter(new FileWriter(lshtestFile));
			for(int j = 0; j < map.get(o).size(); j++){
				if(j != map.get(o).size()-1){
					lshtestFileOut.write(map.get(o).get(j) + "");
				}
				else{
					lshtestFileOut.write(map.get(o).get(j) + "");
				}
				lshtestFileOut.newLine();
			}
			lshtestFileOut.flush();
		}  
	}
	
	//平均分布
	public static double AverageRandom(double min,double max) {
	    int randInteger = (int) (Math.random()*10000);
	    double resultInteger = randInteger * (max - min) ;
	    return resultInteger/10000.0 + min;
	}

	 //概率密度函数
	public static double Normal(double x,double miu,double sigma) {
		return 1.0/Math.sqrt(2*Math.PI*sigma) * Math.exp(-1*(x-miu)*(x-miu)/(2*sigma*sigma));
	}
	
	//产生正态分布随机数
	public static double NormalRandom(double miu,double sigma,double min,double max) {
	    double x;
	    double dScope;
	    double y;
	    do {
			x = AverageRandom(min,max); 
	        y = Normal(x, miu, sigma);
	        dScope = AverageRandom(0, Normal(miu,miu,sigma));
	     }while( dScope > y);
	     return x;
	}

	//哈希函数
	public static int hashfamily(String feature,double a_temp,double b_temp,double w_temp) {
		String[] a = feature.split(",");
		double result=b_temp;
		for(int i=1;i<dimention;i++)
		{
			result+=Integer.parseInt(a[i])*((a_temp+i));
		}
		return (int)(result/w_temp);//返回哈希结果
	}
}
