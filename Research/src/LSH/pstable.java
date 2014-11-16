package LSH;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/*
 * ����p-stable LSH����
 * 
 * ������ɲ��֣�
 * 1��hash������2������������
 * 
 *
 * */

public class pstable {
	static int dimention=10;//ά��
	static int hashcount=10;//��ϣ�������
	public static void main(String[] args) {
		double[][] a = new double[hashcount][dimention];//p-Stable�ֲ���L=2����˹�ֲ������������
		double w=1;//LSH��w
		double b=Math.random()*100/(w+1);//LSH�������b
		ArrayList<String> alist = new ArrayList<>();
		Map<Integer, ArrayList<String>> map = new HashMap<Integer, ArrayList<String>>(); //�������������
		
		//�����Ե�����
		String[] feature = {"1,0,0,0,0,0,0,0,0,1,1,0,0,0",
				"1,0,0,1,1,1,1,1,1,0,1,0,1,0",
				"1,1,0,1,0,0,0,0,0,0,0,1,1,1"};
		
		//����ʼ
		for(int j=0;j<hashcount;j++)//����hashcount����ϣ��
		{
			for(int k=0;k<dimention;k++)
			{
				a[j][k]=NormalRandom(0,1,-1.5,1.5);
			}
		}
		
		for(int i = 0; i < 3; i++) {
			for(int l=0;l<hashcount;l++)//ÿһ��������hashcount��key
			{
				int hash_num=hashfamily(feature[i],a[l][0],b,w);//��ϣ
				int key=(int) (hash_num/w);//��ϣ���key
				
				//��ϣ�洢
				if(map.containsKey(key) && exist(map.get(key),feature[i])) {
					map.get(key).add(feature[i]);
				}else if(!map.containsKey(key)){
					map.put(key, alist(feature[i]));
				}
			}
		}
		print(map);
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
	
	//��ӡhashͰ�еĸ���ֵ
	public static void print(Map<Integer, ArrayList<String>> map) {
		for(Object o : map.keySet()){  
		    System.out.println(o + " : " + map.get(o));  
		}  
	}
	
	//ƽ���ֲ�
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
		for(int i=0;i<dimention;i++)
		{
			result+=Integer.parseInt(a[i])*((a_temp+i));
		}
		return (int)(result/w_temp);//���ع�ϣ���
	}
}

