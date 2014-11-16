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
 * ����p-stable LSH����
 * 
 * ������ɲ��֣�
 * 1��hash������2������������
 * 
 * */

public class PstableLSH {
	static int dimention=1682;//ά��
	static int hashcount=943;//�����ж�����
	static int hashbucketcount=10;//��ϣͰ������
	public static void main(String[] args) throws IOException {
		long startTime = System.currentTimeMillis();
		double[][] a = new double[hashcount][dimention];//p-Stable�ֲ���L=2����˹�ֲ������������
		double w=50;//LSH��w
		double b=Math.random()*100/(w);//LSH�������b
		//����������������������num����ȥ��
		int num = 5;
		//keyindexΪ׷�ٵ��û�����Ϊ��λ�û������Ƽ�
		int keyindex = 40;
//		System.out.println(b);
		ArrayList<String> alist = new ArrayList<>();
		//�������������
		Map<Integer, ArrayList<String>> map = new TreeMap<Integer, ArrayList<String>>(); 
		//�û��������ЩͰ��
		Map<Integer, ArrayList<String>> indexusermap = new TreeMap<Integer, ArrayList<String>>(); 
		//һ��Ͱ�д�����Щ�û�
		Map<Integer, ArrayList<String>> indexbucketmap = new TreeMap<Integer, ArrayList<String>>(); 
		
		ArrayList<Integer> moive = new ArrayList<>();
		//users��mapΪ������Ҫ���ľ���
		Map<Integer, ArrayList<String>> users = new TreeMap<>();
		
		ArrayList<String> userList = new ArrayList<>();
		//usermap��¼������Ͱ�е��û����뵽һ��������
		Map<Integer, ArrayList<String>> usermap = new TreeMap<>();
		//usernozreo��¼��usermap���û����ּ�¼�����λ��
		Map<Integer, ArrayList<String>> usernozero = new TreeMap<>();


		
		//treemap����Ϊԭ���������Ӧ�еĹ�ϵ
		TreeMap<Integer, Integer> treemap = new TreeMap<Integer, Integer>();
		
		/**
		 * ����ʼ
		 * */
		
		//��ʼ����ά����
		Integer[][] base = new Integer[hashcount][dimention];
		Init.InitArray(base, hashcount, dimention, 0);
		
		//��ʼ���Զ�ά����
		Integer[][] test = new Integer[hashcount][dimention];
		Init.InitArray(test, hashcount, dimention, 0);
		
		
//		/**
//		 * ��ʼ��treemap����Ӧ��Ӱ��
//		 * */
//		for(int i=0; i<dimention; i++){
//			treemap.put(i, i);
//		}
		
		/**
		 * ����hashcount����ϣ��
		 */
		for(int j=0;j<hashcount;j++)
		{
			for(int k=0;k<dimention;k++)
			{
				a[j][k]=NormalRandom(0,1,-1.5,1.5);
			}
		}
		
		

		
		String baseIn = "D:\\�о�ѧϰ\\�о�������\\С����\\���ݼ�\\ml-100k\\u1.base";
		ReadFile.Read(baseIn, base);
		
		String baseFile = "D:\\�о�ѧϰ\\�о�������\\С����\\experimentResult\\baseout.txt";
		WriteFile.Write(baseFile, base, hashcount, dimention);
		
		String testFile = "D:\\�о�ѧϰ\\�о�������\\С����\\���ݼ�\\ml-100k\\u1.test";
		ReadFile.Read(testFile, test);
		
		String basetestFile = "D:\\�о�ѧϰ\\�о�������\\С����\\experimentResult\\testout.txt";
		WriteFile.Write(basetestFile, test, hashcount, dimention);
		
		/**
		 * ����¼��ÿһ������ת��Ϊһ���ַ�������
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
			for(int l=0;l<hashbucketcount;l++)//ÿһ��������hashcount��key
			{
//				int hash_num=hashfamily(feature[i],a[l][0],b,w);//��ϣ
				int hash_num=hashfamily(userfeature.get(i+1),a[l][0],b,w);//��ϣ
				int key=(int) (hash_num/w);//��ϣ���key
				
				//��ϣ�洢
				if(map.containsKey(key) && exist(map.get(key),userfeature.get(i+1))) {
					map.get(key).add(userfeature.get(i+1));
				}else if(!map.containsKey(key)){
					map.put(key, alist(userfeature.get(i+1)));
				}
				
				//�û�������ЩͰ����������
				if(indexusermap.containsKey(i+1) && exist(indexusermap.get(i+1),String.valueOf(key))) {
					indexusermap.get(i+1).add(String.valueOf(key));
				}else if(!indexusermap.containsKey(i+1)){
					indexusermap.put(i+1, alist(String.valueOf(key)));
				}
				
				//һ��Ͱ�а�����Щ�û���������
				if(indexbucketmap.containsKey(key) && exist(indexbucketmap.get(key),String.valueOf(i+1))) {
					indexbucketmap.get(key).add(String.valueOf(i+1));
				}else if(!indexbucketmap.containsKey(key)){
					indexbucketmap.put(key, alist(String.valueOf(i+1)));
				}
			}
		}
		
		//sbucketȡ��39���û�����ЩͰ��
		ArrayList<String> sbucket = indexusermap.get(keyindex);
		//userinfoΪ�����39���û�ͬ��һ��Ͱ�е��û��б�
		ArrayList<String> userinfo = new ArrayList<>();

		
		/**
		 * ��39���û����Ƶ��û�����Щ������userinfo��
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
		 * ȡ����39���û����Ƶ������û������֣�����µ�usermap����
		 * */
		for(int i=0; i < userinfo.size(); i++) {
			usermap.put(Integer.parseInt(userinfo.get(i)), alist(arrayTostring(base[Integer.parseInt(userinfo.get(i))])));
		}
		

		
		/**
		 * ��usermap��ͬһ��ȫ��Ϊ0����ɾ�������õ�������Ҫ���ľ���users
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
					 * ��ÿλ�û����ֲ�Ϊ0�ĵ�Ӱ��¼����
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
				 * ����treemap�е�Ӱ���ִ��λ����
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
		String indexuser = "D:\\�о�ѧϰ\\�о�������\\С����\\experimentResult\\indexuser.txt";
		WriteFile.Write(indexuser, indexusermap);
		String bucketuser = "D:\\�о�ѧϰ\\�о�������\\С����\\experimentResult\\bucketuser.txt";
		WriteFile.Write(bucketuser, indexbucketmap);
		String matuser = "D:\\�о�ѧϰ\\�о�������\\С����\\experimentResult\\matuser.txt";
		WriteFile.Write(matuser, users);
		String Treemap = "D:\\�о�ѧϰ\\�о�������\\С����\\experimentResult\\Treemap.txt";
		WriteFile.Write(Treemap, treemap);
//		String userListIn = "D:\\�о�ѧϰ\\�о�������\\С����\\experimentResult\\userlist.txt";
//		WriteFile.Write1(userListIn, users);
		
		long endTime = System.currentTimeMillis();
		long alltime = endTime - startTime;
		System.out.println("����ʱ�䣺 " + alltime + "ms");
//		System.out.println(treemap.size());
	}
	
	//����ά����һ����String����
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
	
	//�ж���һ��key��ʱ������ͬ��valueֵ
	public static boolean exist(ArrayList<String> str1,String str2) {
		for(int i = 0; i < str1.size(); i++) {
			if(str1.get(i).equals(str2)) {
				return false;
			}
		}
		return true;
	}
	
	//���value
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
			System.out.println("������"+ map.size());  
			System.out.println("������"+ map.get(o).size());  
			break;
		}
	}
	
	//��ӡhashͰ�еĸ���ֵ
	public static void print1(Map<Integer, ArrayList<String>> map) {
		for(Object o : map.keySet()){  
		    System.out.println("������"+ map.get(o).size() + " : " + o + " : " + map.get(o));  
		}  
	}
	
	public static void print3(Map<Integer, ArrayList<String>> map) {
		for(Object o : map.keySet()){  
		    System.out.println(map.get(o));  
		}  
	}
	
	//��ӡhashͰ�еĸ���ֵ
	public static void print2(Map<Integer, ArrayList<String>> map) {
		for(Object o : map.keySet()){  
			for(int i = 0; i < map.get(o).size(); i++){
				System.out.println(i + " : " + map.get(o).get(i));  
			}
		}  
	}
	
	//��ӡhashͰ�еĸ���ֵ
	public static void print(Map<Integer, ArrayList<String>> map) throws IOException {
		for(Object o : map.keySet()){  
			String lshtestFile = "D:\\�о�ѧϰ\\�о�������\\С����\\experimentResult\\lsh\\"+ o +".txt";
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
	
	//ƽ���ֲ�
	public static double AverageRandom(double min,double max) {
	    int randInteger = (int) (Math.random()*10000);
	    double resultInteger = randInteger * (max - min) ;
	    return resultInteger/10000.0 + min;
	}

	 //�����ܶȺ���
	public static double Normal(double x,double miu,double sigma) {
		return 1.0/Math.sqrt(2*Math.PI*sigma) * Math.exp(-1*(x-miu)*(x-miu)/(2*sigma*sigma));
	}
	
	//������̬�ֲ������
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

	//��ϣ����
	public static int hashfamily(String feature,double a_temp,double b_temp,double w_temp) {
		String[] a = feature.split(",");
		double result=b_temp;
		for(int i=1;i<dimention;i++)
		{
			result+=Integer.parseInt(a[i])*((a_temp+i));
		}
		return (int)(result/w_temp);//���ع�ϣ���
	}
}
