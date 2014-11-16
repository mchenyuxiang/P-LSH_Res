package LSH;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import tool.ReadFile;

public class compare {
	public static void main(String[] args) {
		//存放myfile.txt文件
		TreeMap<Integer, String> myfile = new TreeMap<>();
		String myfileIn = "D:\\研究学习\\研究生论文\\小论文\\experimentResult\\myfile.txt";
		ReadFile.Read(myfileIn, myfile);
		
		//存放matuser.txt文件
		TreeMap<Integer, String> matuser = new TreeMap<>();
		String matuserIn = "D:\\研究学习\\研究生论文\\小论文\\experimentResult\\matuser.txt";
		ReadFile.Read(matuserIn, matuser);
		
		//存放userlist文件
		ArrayList<String> userlist = new ArrayList<>();
		String userlistIn = "D:\\研究学习\\研究生论文\\小论文\\experimentResult\\userlist.txt";
		ReadFile.Read(userlistIn, userlist);
		
		//存放treemap文件
		TreeMap<String, String> treemap  = new TreeMap<>();
		String treemapIn = "D:\\研究学习\\研究生论文\\小论文\\experimentResult\\treemap.txt";
		ReadFile.Read1(treemapIn, treemap);
		
		TreeMap<Integer,String> resUser = new TreeMap<>();
//		System.out.println(myfile.size());
//		System.out.println(matuser.size());
//		System.out.println(userlist.size());
//		System.out.println(treemap.size());
		
		String old = matuser.get(2);
		String matic = myfile.get(2);
//		System.out.println(old.length() + ":" + matic.length());
		TreeMap<Integer, ArrayList<Integer>> res = new TreeMap<>();
		//存放每一个用户推荐的集合
		ArrayList<Integer> reslist = new ArrayList<>();
		for(int i=0; i < matic.split(",").length; i++){
			if(!matic.split(",")[i].equals(String.valueOf(0))){
				reslist.add(Integer.parseInt(treemap.get(String.valueOf(i+1)))+1);
			}
		}
		res.put(2, reslist);
		
		
		System.out.println(res.get(2));
	}
}
