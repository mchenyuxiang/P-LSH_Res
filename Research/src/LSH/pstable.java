package LSH;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/*
 * 基于p-stable LSH程序
 * 
 * 程序组成部分：
 * 1、hash函数；2、特征向量；
 * 
 *
 * */

public class pstable {
	static int dimention=10;//维度
	static int hashcount=10;//哈希表的数量
	public static void main(String[] args) {
		double[][] a = new double[hashcount][dimention];//p-Stable分布（L=2；高斯分布）的随机向量
		double w=1;//LSH的w
		double b=Math.random()*100/(w+1);//LSH的随机数b
		ArrayList<String> alist = new ArrayList<>();
		Map<Integer, ArrayList<String>> map = new HashMap<Integer, ArrayList<String>>(); //存放向量和名称
		
		//待测试的特征
		String[] feature = {"1,0,0,0,0,0,0,0,0,1,1,0,0,0",
				"1,0,0,1,1,1,1,1,1,0,1,0,1,0",
				"1,1,0,1,0,0,0,0,0,0,0,1,1,1"};
		
		//程序开始
		for(int j=0;j<hashcount;j++)//产生hashcount个哈希表
		{
			for(int k=0;k<dimention;k++)
			{
				a[j][k]=NormalRandom(0,1,-1.5,1.5);
			}
		}
		
		for(int i = 0; i < 3; i++) {
			for(int l=0;l<hashcount;l++)//每一个特征的hashcount个key
			{
				int hash_num=hashfamily(feature[i],a[l][0],b,w);//哈希
				int key=(int) (hash_num/w);//哈希表的key
				
				//哈希存储
				if(map.containsKey(key) && exist(map.get(key),feature[i])) {
					map.get(key).add(feature[i]);
				}else if(!map.containsKey(key)){
					map.put(key, alist(feature[i]));
				}
			}
		}
		print(map);
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
	
	//打印hash桶中的各个值
	public static void print(Map<Integer, ArrayList<String>> map) {
		for(Object o : map.keySet()){  
		    System.out.println(o + " : " + map.get(o));  
		}  
	}
	
	//平均分布
//	public static double AverageRandom(double min,double max) {
//	    int minInteger = (int)(min*10000);
//	    int maxInteger = (int)(max*10000);
//	    int randInteger = (int) (Math.random()*100 * Math.random() * 100);
//	    int diffInteger = maxInteger - minInteger;
//	    int resultInteger = randInteger % diffInteger + minInteger;
//	    return resultInteger/10000.0;
//	}
	
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
		for(int i=0;i<dimention;i++)
		{
			result+=Integer.parseInt(a[i])*((a_temp+i));
		}
		return (int)(result/w_temp);//返回哈希结果
	}
}

