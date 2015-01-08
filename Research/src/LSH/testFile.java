package LSH;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.Vector;

import tool.WriteFile;
/**
 * 讲电影的分类得出来，进行分类
 */
public class testFile {
	/**
	 * 所预测矩阵有多少列
	 */
	static Integer dimention = 20;
	/**
	 * 有多少用户
	 */
	static Integer hashcount = 943;
	/**
	 * 一个桶由多少个hash函数组成
	 */
	static Integer hashbucketcount = 5;
	
	public static void main(String[] args) throws IOException, InterruptedException {
		/**
		 * 电影分类数据
		 */
		String filmFile = "./data/u.item";
		/**
		 * 用户的评分测试矩阵
		 */
		String userFile = "./data/ua.base";
		
		/**
		 * 电影所属分类的map表
		 */
		Map<Integer, ArrayList<Integer>> filmMap = new HashMap<Integer, ArrayList<Integer>>();
		
		Integer[][] base = new Integer[944][20];
		for(int i=0;i<944;i++){
			for(int j=0;j<20;j++){
				base[i][j]=0;
			}
		}
		
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filmFile)));
		BufferedReader bru = new BufferedReader(new InputStreamReader(new FileInputStream(userFile)));
		
		String line = null;
		
		//建立电影归类矩阵
		while((line = br.readLine()) != null){
//			System.out.println(line.split("\\|")[11]);
			//建立电影分类表
			int filmId = Integer.parseInt(line.split("\\|")[0]);
			ArrayList<Integer> tempScore = new ArrayList<>();
			for(int i=5;i<=23;i++){
				int score = Integer.parseInt(line.split("\\|")[i]);
				if(score != 0){
					tempScore.add(i-4);
				}
			}
			filmMap.put(filmId, tempScore);
		}
//		System.out.println(filmMap.get(328));
//		System.out.println(filmMap.get(788));
		
		while((line=bru.readLine()) != null){
			/**
			 * 用户ID
			 */
			Integer userId = Integer.parseInt(line.split("\\\t")[0]);
			/**
			 * 电影ID
			 */
			Integer userFilmId = Integer.parseInt(line.split("\\\t")[1]);
			/**
			 * 评分
			 */
			Integer userFilmScore = Integer.parseInt(line.split("\\\t")[2]);
			
			int length = filmMap.get(userFilmId).size();
			for(int i=0; i < length; i++){
				Integer classID = filmMap.get(userFilmId).get(i);
				base[userId][classID] += userFilmScore;
			}
		}
		
		
		double[][] stable = new double[hashcount][dimention];
		for (int j = 0; j < hashbucketcount; j++) {
			for (int k = 0; k < dimention; k++) {
				Random r = new Random();
//				a[j][k] = NormalRandom(0, 1, -1.5, 1.5);
				stable[j][k] = r.nextGaussian();
			}
		}
		
		
		/**
		 * 与用户相似用户有哪些
		 */
		String matuser = "F:/研究学习/研究生论文/小论文/experimentResult/1M/matuser100k.txt";
		String baseFile = "F:/研究学习/研究生论文/小论文/experimentResult/1M/MatricUser100k.txt";
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
		double w = 350.0;
		double b = Math.random() * w;// LSH的随机数b
//		for(int i=788;i<=788;i++){
//			Vector test1 = new Vector<>(hashbucketcount,hashbucketcount);
//			String key = null;
//			for (int l = 0; l < hashbucketcount; l++)// 每一个特征的hashcount个key
//			{
//
//				String test = arrayTostring(base[i]);
//				String ss = arrayTostring1(stable[l]);
////				System.out.println(ss);
//				Integer hash_num = hashfamily(test, stable[l], b, w);// 哈希
//				test1.add(hash_num);
//			}
//			key = test1.toString();
//			System.out.println(key);
//		}
		System.out.println("================================================");
		for (int i = 1; i <= hashcount; i++) {
			Vector test1 = new Vector<>(hashbucketcount,hashbucketcount);
			
			String key = null;
			int sum = 0;
			for (int l = 0; l < hashbucketcount; l++)// 每一个特征的hashcount个key
			{
//				double w = 400.0;
//				double b = Math.random() * w;// LSH的随机数b
				String test = arrayTostring(base[i]);
				String ss = arrayTostring1(stable[l]);
//				System.out.println(ss);
				Integer hash_num = hashfamily(test, stable[l], b, w);// 哈希
//				sum +=hash_num;
				test1.add(hash_num);
			}
//			key = String.valueOf(sum % (Math.pow(2, 32) - 1)); 
			key = test1.toString();
//			System.out.println(key);
			insertBucket(indexusermap,indexbucketmap,i,key);
			
//			for (int l = 0; l < hashbucketcount; l++)// 每一个特征的hashcount个key
//			{
//				double w = 5.0;
//				double b = Math.random() * w;// LSH的随机数b
//				String test = arrayTostring(base[i]);
//				Integer hash_num = hashfamily(test, stable[l], b, w);// 哈希
//				key = l+"-"+hash_num;
//				insertBucket(indexusermap,indexbucketmap,i,key);
//			}
		}
		
		/**
		 * 程序开始
		 * */
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
//			System.out.println("第" + keyindex + "个用户");
//			System.out.println("运行时间： " + onceTime + "ms");
		}
		WriteFile.Write(matuser, usermap);
		handleFile.handle(hashcount,matuser,baseFile);
		long endTime = System.currentTimeMillis();
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
		// 将二维数组一行用String返回
		public static String arrayTostring1(double[] base) {
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
	/**
	 * 判断在一个key中时候有相同的value值
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static boolean exist(ArrayList<String> str1, String str2) {
		for (int i = 0; i < str1.size(); i++) {
			if (str1.get(i).equals(str2)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 添加value
	 * @param s
	 * @return
	 */
	public static ArrayList<String> alist(String s) {
		ArrayList<String> arr = new ArrayList<String>();
		arr.add(s);
		return arr;
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
			result += Integer.parseInt(a[i]) * (a_temp[i]);
//			System.out.println(a[i] + " : " +a_temp[i]);
		}
		return (int)Math.floor(Math.abs(result) / w_temp);// 返回哈希结果
	}
}
