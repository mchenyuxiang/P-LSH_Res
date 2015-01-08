package tool;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class Init {
	public static void main(String[] args) {
		String fileName = "F:/研究学习/研究生论文/小论文/数据集/ml-1m/ml-1m/ratings.dat";
		String outName = "F:/研究学习/研究生论文/小论文/experimentResult/InitMatricMM.txt";
		long begin = System.currentTimeMillis();
		System.out.println("-----------begin-----------");
		Integer[][] base = new Integer[6040][3952];
		for(int i = 0; i < 6040; i++){
			for(int j = 0; j < 3952; j++){
					base[i][j] = 0;
			}
		}
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
			ReadFile.Read(fileName, base);
			WriteFile.Write(outName, base,6040,3952);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println(System.currentTimeMillis()-begin);
		System.out.println("-----------end------------");
		
	}

	public static void InitArray(Integer[][] base, int hashcount,
			int dimention, int init) {
		for(int i = 0; i < hashcount; i++){
			for(int j = 0; j < dimention; j++){
					base[i][j] = init;
			}
		}		
	}
}
