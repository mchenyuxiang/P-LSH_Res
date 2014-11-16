package LSH;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import tool.ReadFile;

public class compare {
	public static void main(String[] args) {
		//���myfile.txt�ļ�
		TreeMap<Integer, String> myfile = new TreeMap<>();
		String myfileIn = "D:\\�о�ѧϰ\\�о�������\\С����\\experimentResult\\myfile.txt";
		ReadFile.Read(myfileIn, myfile);
		
		//���matuser.txt�ļ�
		TreeMap<Integer, String> matuser = new TreeMap<>();
		String matuserIn = "D:\\�о�ѧϰ\\�о�������\\С����\\experimentResult\\matuser.txt";
		ReadFile.Read(matuserIn, matuser);
		
		//���userlist�ļ�
		ArrayList<String> userlist = new ArrayList<>();
		String userlistIn = "D:\\�о�ѧϰ\\�о�������\\С����\\experimentResult\\userlist.txt";
		ReadFile.Read(userlistIn, userlist);
		
		//���treemap�ļ�
		TreeMap<String, String> treemap  = new TreeMap<>();
		String treemapIn = "D:\\�о�ѧϰ\\�о�������\\С����\\experimentResult\\treemap.txt";
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
		//���ÿһ���û��Ƽ��ļ���
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
