package LSH;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import tool.ReadFile;

public class test {
	public static void main(String[] args) {
		String a = "1, 7, 9, 11, 12, 13, 15, 22, 24, 25, 28, 64, 69, 70, 71, 79, 82,"
				+ " 83, 88, 89, 95, 98, 100, 111, 117, 118, 121, 124, 125, 127, 132,"
				+ " 143, 144, 151, 153, 172, 174, 176, 181, 182, 183, 186, 197, 203,"
				+ " 204, 210, 211, 222, 223, 225, 227, 233, 235, 237, 239, 240, 248,"
				+ " 250, 257, 258, 268, 275, 282, 286, 288, 289, 291, 293, 294, 310, "
				+ "313, 315, 321, 322, 323, 326, 328, 342, 367, 393, 403, 405, 410, "
				+ "411, 423, 427, 431, 471, 473, 475, 496, 508, 511, 515, 527, 528, "
				+ "550, 566, 568, 588, 591, 597, 603, 651, 655, 660, 663, 678, 744, "
				+ "748, 751, 763, 815, 824, 845, 864, 926, 1028";
		String b = "268,294,303,333,345,754,258,750,880,243,271,340,347";
		String[] a1 = a.split(", ");//推荐
		String[] b1 = b.split(",");//原始数据
		int cnt = 0;
		for(int i=0; i < a1.length; i++){
			for(int j=0; j < b1.length; j++){
				if(b1[j].equals(a1[i])){
					cnt ++;
				}
			}
		}
		System.out.println(cnt);
		System.out.println(a1.length);
		System.out.println(b1.length);
		float P = (float)cnt/b1.length;
		float R = (float)cnt/a1.length;
		Map<Integer, Integer> map = new TreeMap<>();
		for(int i=0; i < 10; i++){
			map.put(i, i);
		}
		for(int i=3; i < 10; i++){
			map.put(i, i+1);
			if(i==9){
				map.remove(i);
			}
		}

		System.out.println("F1:  " + 2 * P * R /(P + R));
	}
}
